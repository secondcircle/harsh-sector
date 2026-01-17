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

    // Default values (used when LunaLib is not installed)
    private static final boolean DEFAULT_TRANSPONDER_CHECK = true;

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
     * Inner class that wraps LunaLib calls.
     * Isolated to prevent ClassNotFoundException when LunaLib isn't present.
     */
    private static class LunaSettingsWrapper {
        static boolean getBoolean(String modId, String fieldId, boolean defaultValue) {
            Boolean value = lunalib.lunaSettings.LunaSettings.getBoolean(modId, fieldId);
            return value != null ? value : defaultValue;
        }
    }
}
