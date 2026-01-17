package harshsector.core;

import com.fs.starfarer.api.Global;
import org.apache.log4j.Logger;

/**
 * Shared settings utilities for Harsh Sector with soft dependency on LunaLib.
 *
 * If LunaLib is installed: reads settings from in-game menu (F2)
 * If LunaLib is not installed: returns sensible defaults
 *
 * Individual features should define their own setting IDs and defaults,
 * then use these utility methods to read them.
 */
public class Settings {

    private static final Logger log = Global.getLogger(Settings.class);

    public static final String MOD_ID = "harsh_sector";

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
     * Get a boolean setting from LunaLib, or return default if unavailable.
     */
    public static boolean getBoolean(String fieldId, boolean defaultValue) {
        if (!isLunaLibAvailable()) {
            return defaultValue;
        }

        try {
            return LunaSettingsWrapper.getBoolean(MOD_ID, fieldId, defaultValue);
        } catch (Exception e) {
            log.warn("Harsh Sector: Error reading setting " + fieldId + ", using default", e);
            return defaultValue;
        }
    }

    /**
     * Get a float setting from LunaLib, or return default if unavailable.
     */
    public static float getFloat(String fieldId, float defaultValue) {
        if (!isLunaLibAvailable()) {
            return defaultValue;
        }

        try {
            return LunaSettingsWrapper.getFloat(MOD_ID, fieldId, defaultValue);
        } catch (Exception e) {
            log.warn("Harsh Sector: Error reading setting " + fieldId + ", using default", e);
            return defaultValue;
        }
    }

    /**
     * Get an int setting from LunaLib, or return default if unavailable.
     */
    public static int getInt(String fieldId, int defaultValue) {
        if (!isLunaLibAvailable()) {
            return defaultValue;
        }

        try {
            return LunaSettingsWrapper.getInt(MOD_ID, fieldId, defaultValue);
        } catch (Exception e) {
            log.warn("Harsh Sector: Error reading setting " + fieldId + ", using default", e);
            return defaultValue;
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

        static int getInt(String modId, String fieldId, int defaultValue) {
            Integer value = lunalib.lunaSettings.LunaSettings.getInt(modId, fieldId);
            return value != null ? value : defaultValue;
        }
    }
}
