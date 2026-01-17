package harshsector;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import org.apache.log4j.Logger;

/**
 * Main entry point for Harsh Sector mod.
 *
 * Starsector calls methods on this class at specific times:
 * - onApplicationLoad() - when the game first starts (before main menu)
 * - onGameLoad() - when a save is loaded or new game starts
 * - onNewGame() - only when starting a new game
 *
 * We use onGameLoad() to register our listener that swaps black markets.
 */
public class HarshSectorModPlugin extends BaseModPlugin {

    // Logger for debugging - output goes to starsector.log
    private static final Logger log = Global.getLogger(HarshSectorModPlugin.class);

    @Override
    public void onGameLoad(boolean newGame) {
        // This runs every time a save is loaded (or new game starts)
        log.info("Harsh Sector: Registering submarket swapper");

        // Register our listener - it will swap black markets when player opens markets
        // The 'true' means this listener persists across saves
        SubmarketSwapper.register();
    }
}
