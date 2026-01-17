package harshsector.features.retreat;

import com.fs.starfarer.api.PluginPick;
import com.fs.starfarer.api.campaign.BaseCampaignPlugin;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.CampaignPlugin;
import com.fs.starfarer.api.campaign.InteractionDialogPlugin;
import com.fs.starfarer.api.campaign.SectorEntityToken;

/**
 * Campaign plugin that provides our custom fleet interaction dialog.
 * This allows us to modify fleet interaction options (like removing story point escape).
 */
public class RetreatCampaignPlugin extends BaseCampaignPlugin {

    @Override
    public String getId() {
        return "harshSectorRetreatCampaignPlugin";
    }

    @Override
    public boolean isTransient() {
        return true;
    }

    @Override
    public PluginPick<InteractionDialogPlugin> pickInteractionDialogPlugin(SectorEntityToken interactionTarget) {
        // Only intercept fleet interactions when story point escape is disabled
        if (!RetreatFeature.isStoryPointEscapeDisabled()) {
            return null;
        }

        if (interactionTarget instanceof CampaignFleetAPI) {
            return new PluginPick<InteractionDialogPlugin>(
                new RetreatFleetDialog(),
                CampaignPlugin.PickPriority.MOD_SPECIFIC
            );
        }

        return null;
    }
}
