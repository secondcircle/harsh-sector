package harshsector;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.PlayerMarketTransaction;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.econ.SubmarketAPI;
import com.fs.starfarer.api.campaign.listeners.ColonyInteractionListener;
import com.fs.starfarer.api.impl.campaign.ids.Submarkets;
import org.apache.log4j.Logger;

/**
 * Listens for when the player opens a market.
 * When they do, we swap the vanilla black market plugin with our regulated version.
 *
 * Why a listener? Because markets are loaded/created dynamically. We can't just
 * replace them once at game start - we need to swap them each time the player
 * interacts with a market.
 */
public class SubmarketSwapper implements ColonyInteractionListener {

    private static final Logger log = Global.getLogger(SubmarketSwapper.class);

    // ID for our regulated black market (defined in submarkets.csv)
    public static final String REGULATED_BLACK_MARKET = "harsh_sector_black_market";

    /**
     * Register this listener with the game.
     * Called from HarshSectorModPlugin.onGameLoad()
     */
    public static void register() {
        // Check if we're already registered (prevents duplicates on save/load)
        for (Object listener : Global.getSector().getListenerManager().getListeners(ColonyInteractionListener.class)) {
            if (listener instanceof SubmarketSwapper) {
                log.info("SubmarketSwapper already registered, skipping");
                return;
            }
        }

        SubmarketSwapper swapper = new SubmarketSwapper();
        // The 'true' parameter means this listener is transient (not saved)
        // We re-register it on each game load anyway
        Global.getSector().getListenerManager().addListener(swapper, true);
        log.info("SubmarketSwapper registered");
    }

    /**
     * Called when the player opens a market and cargo is updated.
     * This is our chance to swap the black market plugin.
     */
    @Override
    public void reportPlayerOpenedMarket(MarketAPI market) {
        // This method is called first, before cargo update
    }

    @Override
    public void reportPlayerClosedMarket(MarketAPI market) {
        // Called when player leaves the market
    }

    @Override
    public void reportPlayerMarketTransaction(PlayerMarketTransaction transaction) {
        // Called when player completes a transaction (buy/sell)
        // Not needed for our feature, but required by the interface
    }

    @Override
    public void reportPlayerOpenedMarketAndCargoUpdated(MarketAPI market) {
        // This is the main hook - market is fully ready
        swapBlackMarket(market);
    }

    /**
     * Replace the vanilla black market plugin with our regulated version.
     */
    private void swapBlackMarket(MarketAPI market) {
        // Check if this market has a black market
        SubmarketAPI blackMarket = market.getSubmarket(Submarkets.SUBMARKET_BLACK);
        if (blackMarket == null) {
            return; // No black market here, nothing to do
        }

        // Check if it's already our regulated version
        if (blackMarket.getPlugin() instanceof RegulatedBlackMarket) {
            return; // Already swapped
        }

        log.info("Swapping black market at: " + market.getName());

        // Store the existing cargo/inventory so we don't lose it
        // Note: The submarket spec in submarkets.csv will create our plugin

        // Remove vanilla black market
        market.removeSubmarket(Submarkets.SUBMARKET_BLACK);

        // Add our regulated version (defined in data/campaign/submarkets.csv)
        market.addSubmarket(REGULATED_BLACK_MARKET);

        // Copy over the existing cargo if there was any
        SubmarketAPI newBlackMarket = market.getSubmarket(REGULATED_BLACK_MARKET);
        if (newBlackMarket != null && blackMarket.getCargo() != null) {
            newBlackMarket.getCargo().addAll(blackMarket.getCargo());
        }
    }
}
