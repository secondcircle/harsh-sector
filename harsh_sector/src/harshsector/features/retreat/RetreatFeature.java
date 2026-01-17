package harshsector.features.retreat;

import com.fs.starfarer.api.Global;
import harshsector.core.Settings;
import org.apache.log4j.Logger;

/**
 * Tactical Retreat Feature
 *
 * Modifies retreat battles so enemy ships are delayed based on their burn speed
 * relative to the player's slowest ship. Faster enemies catch up quickly; slower
 * enemies take longer to arrive.
 *
 * Components:
 * - TacticalRetreatPlugin: Combat plugin that delays enemy reinforcements
 * - BurnSpeedCalculator: Utility for calculating burn speeds and delays
 * - RetreatCampaignPlugin: Provides custom fleet interaction dialog
 * - RetreatFleetDialog: Removes story point escape option
 */
public class RetreatFeature {

    private static final Logger log = Global.getLogger(RetreatFeature.class);

    // Setting IDs (must match LunaSettings.csv)
    private static final String TACTICAL_RETREAT_ENABLED_ID = "harshsector_retreat_enabled";
    private static final String RETREAT_DELAY_PER_BURN_ID = "harshsector_retreat_delay";
    private static final String RETREAT_MAX_DELAY_ID = "harshsector_retreat_max_delay";
    private static final String STORY_ESCAPE_DISABLED_ID = "harshsector_story_escape_disabled";
    private static final String EBURN_MODIFIER_ENABLED_ID = "harshsector_eburn_enabled";
    private static final String EBURN_MODIFIER_AMOUNT_ID = "harshsector_eburn_modifier";

    // Default values
    private static final boolean DEFAULT_TACTICAL_RETREAT_ENABLED = true;
    private static final float DEFAULT_RETREAT_DELAY_PER_BURN = 30.0f;
    private static final float DEFAULT_RETREAT_MAX_DELAY = 180.0f;
    private static final boolean DEFAULT_STORY_ESCAPE_DISABLED = true;
    private static final boolean DEFAULT_EBURN_MODIFIER_ENABLED = true;
    private static final int DEFAULT_EBURN_MODIFIER_AMOUNT = 1;

    /**
     * Register the retreat feature with the game.
     * Called from HarshSectorModPlugin.onGameLoad()
     */
    public static void register() {
        log.info("Harsh Sector: Registering Retreat feature");
        Global.getSector().registerPlugin(new RetreatCampaignPlugin());
    }

    /**
     * Check if the tactical retreat system is enabled.
     *
     * When enabled: enemy ships in retreat battles are delayed based on burn speed
     * When disabled: retreat battles work like vanilla
     */
    public static boolean isTacticalRetreatEnabled() {
        return Settings.getBoolean(TACTICAL_RETREAT_ENABLED_ID, DEFAULT_TACTICAL_RETREAT_ENABLED);
    }

    /**
     * Get the delay per burn level difference in seconds.
     *
     * For each point of burn speed difference between the player's slowest ship
     * and an enemy ship, that enemy is delayed by this many seconds.
     */
    public static float getRetreatDelayPerBurn() {
        return Settings.getFloat(RETREAT_DELAY_PER_BURN_ID, DEFAULT_RETREAT_DELAY_PER_BURN);
    }

    /**
     * Get the maximum delay cap in seconds.
     *
     * No enemy ship will be delayed more than this, regardless of burn speed difference.
     */
    public static float getRetreatMaxDelay() {
        return Settings.getFloat(RETREAT_MAX_DELAY_ID, DEFAULT_RETREAT_MAX_DELAY);
    }

    /**
     * Check if the story point combat escape is disabled.
     *
     * When enabled: the "Disengage by executing special maneuvers" story point option is removed
     * When disabled: story point escape works like vanilla
     */
    public static boolean isStoryPointEscapeDisabled() {
        return Settings.getBoolean(STORY_ESCAPE_DISABLED_ID, DEFAULT_STORY_ESCAPE_DISABLED);
    }

    /**
     * Check if the emergency burn modifier is enabled.
     *
     * When enabled: emergency burn status affects pursuit delays
     * When disabled: emergency burn has no effect on pursuit
     */
    public static boolean isEmergencyBurnModifierEnabled() {
        return Settings.getBoolean(EBURN_MODIFIER_ENABLED_ID, DEFAULT_EBURN_MODIFIER_ENABLED);
    }

    /**
     * Get the emergency burn modifier amount (in burn levels).
     *
     * Player emergency burning: +modifier to effective burn (enemies delayed longer)
     * Enemy emergency burning: -modifier to effective burn (enemies deploy faster)
     */
    public static int getEmergencyBurnModifier() {
        return Settings.getInt(EBURN_MODIFIER_AMOUNT_ID, DEFAULT_EBURN_MODIFIER_AMOUNT);
    }
}
