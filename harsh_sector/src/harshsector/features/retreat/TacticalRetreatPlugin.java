package harshsector.features.retreat;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.characters.AbilityPlugin;
import com.fs.starfarer.api.combat.BattleCreationContext;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatFleetManagerAPI;
import com.fs.starfarer.api.combat.EveryFrameCombatPlugin;
import com.fs.starfarer.api.combat.ViewportAPI;
import com.fs.starfarer.api.fleet.FleetGoal;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.ids.Abilities;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.mission.FleetSide;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Combat plugin that delays enemy reinforcements in retreat battles based on burn speed.
 *
 * When the player is fleeing (pursuit battle), enemy ships with lower burn speed
 * than the player's slowest ship are delayed from entering combat.
 *
 * Delay formula: (playerMinBurn - enemyShipBurn) * delayPerBurnLevel seconds
 */
public class TacticalRetreatPlugin implements EveryFrameCombatPlugin {

    private static final Logger log = Global.getLogger(TacticalRetreatPlugin.class);

    // Status key for the UI indicator
    private static final Object STATUS_KEY = new Object();

    private CombatEngineAPI engine;
    private boolean initialized = false;
    private boolean isRetreatBattle = false;
    private int playerMinBurn = 0;
    private float elapsedTime = 0f;
    private boolean playerWasEmergencyBurning = false;
    private boolean enemyWasEmergencyBurning = false;

    // Ships we've removed from reserves, waiting to deploy
    private List<DelayedShip> delayedShips = new ArrayList<DelayedShip>();

    // Track which ships have already been deployed
    private Map<String, Boolean> deployedShips = new HashMap<String, Boolean>();

    /**
     * Simple holder for a ship and its deployment delay.
     */
    private static class DelayedShip {
        FleetMemberAPI member;
        float delaySeconds;

        DelayedShip(FleetMemberAPI member, float delaySeconds) {
            this.member = member;
            this.delaySeconds = delaySeconds;
        }
    }

    @Override
    public void init(CombatEngineAPI engine) {
        this.engine = engine;
        this.initialized = false;
        this.isRetreatBattle = false;
        this.elapsedTime = 0f;
        this.delayedShips.clear();
        this.deployedShips.clear();
        this.playerWasEmergencyBurning = false;
        this.enemyWasEmergencyBurning = false;

        // Skip if in simulation/mission
        if (engine.isSimulation()) {
            log.info("TacticalRetreat: Skipping (simulation mode)");
            return;
        }

        // Check if feature is enabled
        if (!RetreatFeature.isTacticalRetreatEnabled()) {
            log.info("TacticalRetreat: Disabled in settings");
            return;
        }

        // Get battle context
        BattleCreationContext context = engine.getContext();
        if (context == null) {
            log.info("TacticalRetreat: No battle context available");
            return;
        }

        // Check if player is retreating (FleetGoal.ESCAPE)
        FleetGoal playerGoal = context.getPlayerGoal();
        if (playerGoal != FleetGoal.ESCAPE) {
            log.info("TacticalRetreat: Player goal is " + playerGoal + ", not ESCAPE - skipping");
            return;
        }

        isRetreatBattle = true;
        log.info("TacticalRetreat: Retreat battle detected! Player is fleeing.");

        // Detect emergency burn status for both fleets (must do this early, before abilities deactivate)
        detectEmergencyBurnStatus(context);

        // Calculate player's minimum burn level (slowest ship)
        playerMinBurn = BurnSpeedCalculator.getPlayerFleetMinBurn();
        log.info("TacticalRetreat: Player min burn = " + playerMinBurn);

        // Build delay schedule and remove slow ships from reserves
        buildDelaySchedule();

        initialized = true;
        log.info("TacticalRetreat: Initialized with " + delayedShips.size() + " ships scheduled for delayed deployment");
    }

    /**
     * Detect if either fleet was emergency burning when the battle started.
     * This affects pursuit delay calculations.
     */
    private void detectEmergencyBurnStatus(BattleCreationContext context) {
        if (!RetreatFeature.isEmergencyBurnModifierEnabled()) {
            log.info("TacticalRetreat: Emergency burn modifier disabled in settings");
            return;
        }

        // Check player emergency burn
        CampaignFleetAPI playerFleet = Global.getSector().getPlayerFleet();
        if (playerFleet != null) {
            AbilityPlugin playerEburn = playerFleet.getAbility(Abilities.EMERGENCY_BURN);
            if (playerEburn != null && playerEburn.isActiveOrInProgress()) {
                playerWasEmergencyBurning = true;
                log.info("TacticalRetreat: Player was emergency burning when caught!");
            }
        }

        // Check enemy emergency burn
        CampaignFleetAPI enemyFleet = context.getOtherFleet();
        if (enemyFleet != null) {
            AbilityPlugin enemyEburn = enemyFleet.getAbility(Abilities.EMERGENCY_BURN);
            if (enemyEburn != null && enemyEburn.isActiveOrInProgress()) {
                enemyWasEmergencyBurning = true;
                log.info("TacticalRetreat: Enemy was emergency burning when catching player!");
            }
        }
    }

    /**
     * Build a schedule of which enemy ships should be delayed.
     * Removes delayed ships from reserves so the AI won't deploy them.
     */
    private void buildDelaySchedule() {
        CombatFleetManagerAPI enemyManager = engine.getFleetManager(FleetSide.ENEMY);
        if (enemyManager == null) {
            log.warn("TacticalRetreat: No enemy fleet manager");
            return;
        }

        float delayPerBurn = RetreatFeature.getRetreatDelayPerBurn();
        float maxDelay = RetreatFeature.getRetreatMaxDelay();

        // Calculate effective player min burn with emergency burn modifier
        int effectivePlayerMinBurn = playerMinBurn;
        int burnModifier = RetreatFeature.getEmergencyBurnModifier();

        // Player emergency burning = enemies delayed longer (player was faster)
        if (playerWasEmergencyBurning) {
            effectivePlayerMinBurn += burnModifier;
            log.info("TacticalRetreat: Player eburn bonus: +" + burnModifier + " effective burn");
        }
        // Enemy emergency burning = enemies deploy faster (they were also boosting)
        if (enemyWasEmergencyBurning) {
            effectivePlayerMinBurn -= burnModifier;
            log.info("TacticalRetreat: Enemy eburn penalty: -" + burnModifier + " effective burn");
        }

        if (effectivePlayerMinBurn != playerMinBurn) {
            log.info("TacticalRetreat: Effective player min burn = " + effectivePlayerMinBurn +
                     " (base: " + playerMinBurn + ")");
        }

        // Get all enemy fleet members in reserves (make a copy since we'll modify)
        List<FleetMemberAPI> reserves = enemyManager.getReservesCopy();
        log.info("TacticalRetreat: Enemy has " + reserves.size() + " ships in reserves");

        for (FleetMemberAPI member : reserves) {
            int shipBurn = BurnSpeedCalculator.getShipBurn(member);
            float delay = BurnSpeedCalculator.calculateDelay(shipBurn, effectivePlayerMinBurn, delayPerBurn, maxDelay);

            if (delay > 0) {
                // This ship is too slow - remove from reserves and track for delayed deployment
                enemyManager.removeFromReserves(member);
                delayedShips.add(new DelayedShip(member, delay));
                log.info("TacticalRetreat: " + member.getShipName() + " (burn " + shipBurn +
                         ") removed from reserves, will deploy after " + delay + "s");
            } else {
                log.info("TacticalRetreat: " + member.getShipName() + " (burn " + shipBurn +
                         ") - no delay (fast enough to catch up)");
            }
        }
    }

    @Override
    public void advance(float amount, List<InputEventAPI> events) {
        if (engine == null || engine.isPaused()) return;
        if (!initialized || !isRetreatBattle) return;

        // Update UI even if paused, but only advance time when not paused
        updateReinforcementStatus();

        if (delayedShips.isEmpty()) return;

        elapsedTime += amount;

        CombatFleetManagerAPI enemyManager = engine.getFleetManager(FleetSide.ENEMY);
        if (enemyManager == null) return;

        // Check each delayed ship - add back to reserves when delay expires
        List<DelayedShip> toRemove = new ArrayList<DelayedShip>();
        for (DelayedShip delayed : delayedShips) {
            String memberId = delayed.member.getId();

            // Skip if already released to reserves
            if (deployedShips.containsKey(memberId)) continue;

            // Check if delay has elapsed
            if (elapsedTime >= delayed.delaySeconds) {
                // Time to release this ship back to reserves
                log.info("TacticalRetreat: [" + String.format("%.1f", elapsedTime) + "s] " +
                         "Releasing " + delayed.member.getShipName() + " to reserves (delay elapsed)");

                // Add back to reserves - the game's AI will deploy when ready
                // This respects battle size limits, deployment points, etc.
                enemyManager.addToReserves(delayed.member);
                deployedShips.put(memberId, true);
                toRemove.add(delayed);
            }
        }

        // Remove released ships from tracking
        delayedShips.removeAll(toRemove);
    }

    /**
     * Update the UI status showing incoming enemy reinforcements.
     * Uses the native ship status display for a consistent look.
     *
     * Ships are grouped by arrival time (based on burn speed).
     * All ships at the same burn level arrive together as a "wave".
     */
    private void updateReinforcementStatus() {
        if (delayedShips.isEmpty()) return;

        // Group ships by arrival time (with small tolerance for floating point)
        // Find the next wave and count ships in it
        float nextWaveTime = Float.MAX_VALUE;
        int shipsInNextWave = 0;
        int totalRemaining = 0;

        // First pass: find the earliest arrival time
        for (DelayedShip delayed : delayedShips) {
            if (!deployedShips.containsKey(delayed.member.getId())) {
                totalRemaining++;
                float timeRemaining = delayed.delaySeconds - elapsedTime;
                if (timeRemaining < nextWaveTime) {
                    nextWaveTime = timeRemaining;
                }
            }
        }

        if (totalRemaining == 0) return;

        // Second pass: count ships arriving at that time (within 1s tolerance)
        for (DelayedShip delayed : delayedShips) {
            if (!deployedShips.containsKey(delayed.member.getId())) {
                float timeRemaining = delayed.delaySeconds - elapsedTime;
                if (Math.abs(timeRemaining - nextWaveTime) < 1.0f) {
                    shipsInNextWave++;
                }
            }
        }

        // Format the status message
        String icon = Global.getSettings().getSpriteName("ui", "icon_tactical_cr_penalty");
        String title = "Enemy Reinforcements";
        String data;

        int seconds = (int) Math.ceil(Math.max(0, nextWaveTime));

        if (nextWaveTime <= 0) {
            // Wave becoming available now
            if (shipsInNextWave == 1) {
                data = "1 ship now available";
            } else {
                data = shipsInNextWave + " ships now available";
            }
        } else if (shipsInNextWave == totalRemaining) {
            // All remaining ships arrive together
            if (shipsInNextWave == 1) {
                data = "1 ship in " + seconds + "s";
            } else {
                data = shipsInNextWave + " ships in " + seconds + "s";
            }
        } else {
            // Multiple waves remaining
            if (shipsInNextWave == 1) {
                data = "1 ship in " + seconds + "s (" + totalRemaining + " total)";
            } else {
                data = shipsInNextWave + " ships in " + seconds + "s (" + totalRemaining + " total)";
            }
        }

        // Show as a "negative" status (red-ish) since it's enemy reinforcements
        engine.maintainStatusForPlayerShip(STATUS_KEY, icon, title, data, true);
    }

    @Override
    public void renderInWorldCoords(ViewportAPI viewport) {
        // Not needed
    }

    @Override
    public void renderInUICoords(ViewportAPI viewport) {
        // Not needed
    }

    @Override
    public void processInputPreCoreControls(float amount, List<InputEventAPI> events) {
        // Not needed
    }
}
