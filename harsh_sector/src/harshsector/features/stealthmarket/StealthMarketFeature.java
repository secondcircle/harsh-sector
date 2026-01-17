package harshsector.features.stealthmarket;

import com.fs.starfarer.api.Global;
import harshsector.core.Settings;
import org.apache.log4j.Logger;

/**
 * Stealth Market Feature
 *
 * Requires players to turn their transponder OFF to access black markets.
 * Broadcasting your identity while conducting illegal business is unwise.
 *
 * Components:
 * - RegulatedBlackMarket: Black market plugin that checks transponder state
 * - SubmarketSwapper: Listener that swaps vanilla black markets with regulated version
 */
public class StealthMarketFeature {

    private static final Logger log = Global.getLogger(StealthMarketFeature.class);

    // Setting IDs (must match LunaSettings.csv)
    private static final String TRANSPONDER_CHECK_ID = "harshsector_transponder_check";

    // Default values
    private static final boolean DEFAULT_TRANSPONDER_CHECK = true;

    /**
     * Register the stealth market feature with the game.
     * Called from HarshSectorModPlugin.onGameLoad()
     */
    public static void register() {
        log.info("Harsh Sector: Registering Stealth Market feature");
        SubmarketSwapper.register();
    }

    /**
     * Check if the transponder black market check is enabled.
     *
     * When enabled: players must turn transponder OFF to access black markets
     * When disabled: black markets work like vanilla (always accessible)
     */
    public static boolean isTransponderCheckEnabled() {
        return Settings.getBoolean(TRANSPONDER_CHECK_ID, DEFAULT_TRANSPONDER_CHECK);
    }
}
