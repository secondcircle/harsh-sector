package harshsector;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import harshsector.features.retreat.RetreatFeature;
import harshsector.features.stealthmarket.StealthMarketFeature;
import org.apache.log4j.Logger;

/**
 * Main entry point for Harsh Sector mod.
 *
 * Starsector calls methods on this class at specific times:
 * - onApplicationLoad() - when the game first starts (before main menu)
 * - onGameLoad() - when a save is loaded or new game starts
 * - onNewGame() - only when starting a new game
 *
 * Each feature is self-contained in its own package under features/.
 * This plugin discovers and registers them on game load.
 */
public class HarshSectorModPlugin extends BaseModPlugin {

    private static final Logger log = Global.getLogger(HarshSectorModPlugin.class);

    @Override
    public void onGameLoad(boolean newGame) {
        log.info("Harsh Sector: Initializing features");

        // Register features - each handles its own setup
        StealthMarketFeature.register();
        RetreatFeature.register();

        log.info("Harsh Sector: All features registered");
    }
}
