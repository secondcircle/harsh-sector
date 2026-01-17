package harshsector;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CoreUIAPI;
import com.fs.starfarer.api.impl.campaign.submarkets.BlackMarketPlugin;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.awt.Color;

/**
 * A black market that requires transponder OFF to access.
 *
 * This extends the vanilla BlackMarketPlugin and overrides two methods:
 * - isEnabled() - returns false when transponder is ON (disables the tab)
 * - createTooltip() - adds explanation text when disabled
 *
 * Everything else (buying, selling, tariffs, etc.) works exactly like vanilla.
 */
public class RegulatedBlackMarket extends BlackMarketPlugin {

    /**
     * Check if the black market should be accessible.
     * This is THE key method - returning false grays out the submarket tab.
     *
     * @param ui The UI context (not used here)
     * @return true if accessible, false if disabled
     */
    @Override
    public boolean isEnabled(CoreUIAPI ui) {
        // If the feature is disabled via settings, always allow access
        if (!HarshSectorSettings.isTransponderCheckEnabled()) {
            return true;
        }
        // If transponder is ON, black market is disabled
        // If transponder is OFF, black market is enabled
        return !isTransponderOn();
    }

    /**
     * Check the player's transponder state.
     * This is the one API call that makes this whole feature work.
     */
    private boolean isTransponderOn() {
        return Global.getSector().getPlayerFleet().isTransponderOn();
    }

    /**
     * Create the tooltip shown when hovering over the submarket tab.
     * We add our own message explaining why it might be disabled.
     */
    @Override
    public void createTooltip(CoreUIAPI ui, TooltipMakerAPI tooltip, boolean expanded) {
        // First, let the vanilla tooltip do its thing
        super.createTooltip(ui, tooltip, expanded);

        // Skip custom tooltip if feature is disabled
        if (!HarshSectorSettings.isTransponderCheckEnabled()) {
            return;
        }

        // Add our warning message
        float pad = 10f;
        Color highlightColor = Misc.getHighlightColor();
        Color negativeColor = Misc.getNegativeHighlightColor();

        if (isTransponderOn()) {
            // Transponder is ON - explain why market is disabled
            tooltip.addPara(
                "The black market is currently inaccessible. " +
                "You must turn your transponder OFF to conduct business here.",
                negativeColor,
                pad
            );
        } else {
            // Transponder is OFF - market is accessible
            tooltip.addPara(
                "Your transponder is off. The black market is available.",
                highlightColor,
                pad
            );
        }
    }

    /**
     * Get the name shown on the submarket tab.
     * We keep "Black Market" but you could customize this.
     */
    @Override
    public String getName() {
        return "Black Market";
    }
}
