package harshsector;

import com.fs.starfarer.api.Global;
import org.apache.log4j.Logger;

/**
 * Settings helper for Harsh Sector with soft dependency on LunaLib.
 *
 * If LunaLib is installed: reads settings from in-game menu (F2)
 * If LunaLib is not installed: returns sensible defaults
 *
 * Settings are read fresh each call to support live updates without restart.
 */
public class HarshSectorSettings {

    private static final Logger log = Global.getLogger(HarshSectorSettings.class);

    public static final String MOD_ID = "harsh_sector";

    // Setting field IDs (must match LunaSettings.csv)
    private static final String TRANSPONDER_CHECK_ID = "harshsector_transponder_check";
    private static final String TACTICAL_RETREAT_ENABLED_ID = "harshsector_retreat_enabled";
    private static final String RETREAT_DELAY_PER_BURN_ID = "harshsector_retreat_delay";
    private static final String RETREAT_MAX_DELAY_ID = "harshsector_retreat_max_delay";

    // Default values (used when LunaLib is not installed)
    private static final boolean DEFAULT_TRANSPONDER_CHECK = true;
    private static final boolean DEFAULT_TACTICAL_RETREAT_ENABLED = true;
    private static final float DEFAULT_RETREAT_DELAY_PER_BURN = 30.0f;
    private static final float DEFAULT_RETREAT_MAX_DELAY = 180.0f;

    // Cache LunaLib availability (doesn't change during runtime)
    private static Boolean lunaLibAvailable = null;

    /**
     * Check if LunaLib is installed and enabled.
     */
    public static boolean isLunaLibAvailable() {
        if (lunaLibAvailable == null) {
            lunaLibAvailable = Global.getSettings().getModManager().isModEnabled("lunalib");
            if (lunaLibAvailable) {
                log.info("Harsh Sector: LunaLib detected, using in-game settings");
            } else {
                log.info("Harsh Sector: LunaLib not found, using default settings");
            }
        }
        return lunaLibAvailable;
    }

    /**
     * Check if the transponder black market check is enabled.
     *
     * When enabled: players must turn transponder OFF to access black markets
     * When disabled: black markets work like vanilla (always accessible)
     */
    public static boolean isTransponderCheckEnabled() {
        if (!isLunaLibAvailable()) {
            return DEFAULT_TRANSPONDER_CHECK;
        }

        try {
            return LunaSettingsWrapper.getBoolean(MOD_ID, TRANSPONDER_CHECK_ID, DEFAULT_TRANSPONDER_CHECK);
        } catch (Exception e) {
            log.warn("Harsh Sector: Error reading LunaLib setting, using default", e);
            return DEFAULT_TRANSPONDER_CHECK;
        }
    }

    /**
     * Check if the tactical retreat system is enabled.
     *
     * When enabled: enemy ships in retreat battles are delayed based on burn speed
     * When disabled: retreat battles work like vanilla
     */
    public static boolean isTacticalRetreatEnabled() {
        if (!isLunaLibAvailable()) {
            return DEFAULT_TACTICAL_RETREAT_ENABLED;
        }

        try {
            return LunaSettingsWrapper.getBoolean(MOD_ID, TACTICAL_RETREAT_ENABLED_ID, DEFAULT_TACTICAL_RETREAT_ENABLED);
        } catch (Exception e) {
            log.warn("Harsh Sector: Error reading tactical retreat enabled setting, using default", e);
            return DEFAULT_TACTICAL_RETREAT_ENABLED;
        }
    }

    /**
     * Get the delay per burn level difference in seconds.
     *
     * For each point of burn speed difference between the player's slowest ship
     * and an enemy ship, that enemy is delayed by this many seconds.
     */
    public static float getRetreatDelayPerBurn() {
        if (!isLunaLibAvailable()) {
            return DEFAULT_RETREAT_DELAY_PER_BURN;
        }

        try {
            return LunaSettingsWrapper.getFloat(MOD_ID, RETREAT_DELAY_PER_BURN_ID, DEFAULT_RETREAT_DELAY_PER_BURN);
        } catch (Exception e) {
            log.warn("Harsh Sector: Error reading retreat delay setting, using default", e);
            return DEFAULT_RETREAT_DELAY_PER_BURN;
        }
    }

    /**
     * Get the maximum delay cap in seconds.
     *
     * No enemy ship will be delayed more than this, regardless of burn speed difference.
     */
    public static float getRetreatMaxDelay() {
        if (!isLunaLibAvailable()) {
            return DEFAULT_RETREAT_MAX_DELAY;
        }

        try {
            return LunaSettingsWrapper.getFloat(MOD_ID, RETREAT_MAX_DELAY_ID, DEFAULT_RETREAT_MAX_DELAY);
        } catch (Exception e) {
            log.warn("Harsh Sector: Error reading retreat max delay setting, using default", e);
            return DEFAULT_RETREAT_MAX_DELAY;
        }
    }

    /**
     * Inner class that wraps LunaLib calls.
     * Isolated to prevent ClassNotFoundException when LunaLib isn't present.
     */
    private static class LunaSettingsWrapper {
        static boolean getBoolean(String modId, String fieldId, boolean defaultValue) {
            Boolean value = lunalib.lunaSettings.LunaSettings.getBoolean(modId, fieldId);
            return value != null ? value : defaultValue;
        }

        static float getFloat(String modId, String fieldId, float defaultValue) {
            Float value = lunalib.lunaSettings.LunaSettings.getFloat(modId, fieldId);
            return value != null ? value : defaultValue;
        }
    }
}
