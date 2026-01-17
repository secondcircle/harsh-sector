package harshsector.combat;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BattleCreationContext;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatFleetManagerAPI;
import com.fs.starfarer.api.combat.EveryFrameCombatPlugin;
import com.fs.starfarer.api.combat.ViewportAPI;
import com.fs.starfarer.api.fleet.FleetGoal;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.mission.FleetSide;

import org.apache.log4j.Logger;
import org.lwjgl.util.vector.Vector2f;

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

    private CombatEngineAPI engine;
    private boolean initialized = false;
    private boolean isRetreatBattle = false;
    private int playerMinBurn = 0;
    private float elapsedTime = 0f;

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

        // Skip if in simulation/mission
        if (engine.isSimulation()) {
            log.info("TacticalRetreat: Skipping (simulation mode)");
            return;
        }

        // Check if feature is enabled
        if (!harshsector.HarshSectorSettings.isTacticalRetreatEnabled()) {
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

        // Calculate player's minimum burn level (slowest ship)
        playerMinBurn = BurnSpeedCalculator.getPlayerFleetMinBurn();
        log.info("TacticalRetreat: Player min burn = " + playerMinBurn);

        // Build delay schedule and remove slow ships from reserves
        buildDelaySchedule();

        initialized = true;
        log.info("TacticalRetreat: Initialized with " + delayedShips.size() + " ships scheduled for delayed deployment");
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

        float delayPerBurn = harshsector.HarshSectorSettings.getRetreatDelayPerBurn();
        float maxDelay = harshsector.HarshSectorSettings.getRetreatMaxDelay();

        // Get all enemy fleet members in reserves (make a copy since we'll modify)
        List<FleetMemberAPI> reserves = enemyManager.getReservesCopy();
        log.info("TacticalRetreat: Enemy has " + reserves.size() + " ships in reserves");

        for (FleetMemberAPI member : reserves) {
            int shipBurn = BurnSpeedCalculator.getShipBurn(member);
            float delay = BurnSpeedCalculator.calculateDelay(shipBurn, playerMinBurn, delayPerBurn, maxDelay);

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
        if (delayedShips.isEmpty()) return;

        elapsedTime += amount;

        CombatFleetManagerAPI enemyManager = engine.getFleetManager(FleetSide.ENEMY);
        if (enemyManager == null) return;

        // Check each delayed ship
        List<DelayedShip> toRemove = new ArrayList<DelayedShip>();
        for (DelayedShip delayed : delayedShips) {
            String memberId = delayed.member.getId();

            // Skip if already deployed
            if (deployedShips.containsKey(memberId)) continue;

            // Check if delay has elapsed
            if (elapsedTime >= delayed.delaySeconds) {
                // Time to deploy this ship
                log.info("TacticalRetreat: [" + String.format("%.1f", elapsedTime) + "s] " +
                         "Deploying " + delayed.member.getShipName() + " (delay elapsed)");

                // Spawn the ship at the enemy's spawn location
                // Enemy ships in pursuit typically spawn at the bottom of the map
                Vector2f spawnLocation = getEnemySpawnLocation();
                float facing = 90f; // Facing up (toward player retreat direction)

                try {
                    enemyManager.spawnFleetMember(delayed.member, spawnLocation, facing, 0f);
                    deployedShips.put(memberId, true);
                    toRemove.add(delayed);
                    log.info("TacticalRetreat: Successfully spawned " + delayed.member.getShipName());
                } catch (Exception e) {
                    log.error("TacticalRetreat: Failed to spawn " + delayed.member.getShipName(), e);
                    // Mark as deployed anyway to avoid retry spam
                    deployedShips.put(memberId, true);
                    toRemove.add(delayed);
                }
            }
        }

        // Remove deployed ships from tracking
        delayedShips.removeAll(toRemove);
    }

    /**
     * Get a spawn location for enemy reinforcements.
     * In pursuit battles, enemies spawn from the bottom of the map.
     */
    private Vector2f getEnemySpawnLocation() {
        // Get map dimensions
        float mapWidth = engine.getMapWidth();
        float mapHeight = engine.getMapHeight();

        // Spawn at bottom center, with some randomization
        float x = (mapWidth / 2f) + ((float)Math.random() - 0.5f) * mapWidth * 0.3f;
        float y = -mapHeight / 2f + 500f; // Near bottom edge

        return new Vector2f(x, y);
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
