package harshsector.features.retreat;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.impl.campaign.FleetInteractionDialogPluginImpl;
import org.apache.log4j.Logger;

/**
 * Custom fleet interaction dialog that removes the story point escape option.
 *
 * The vanilla game allows spending 1 story point to "Disengage by executing
 * a series of special maneuvers" which guarantees escape. This plugin removes
 * that option to make combat encounters more consequential.
 */
public class RetreatFleetDialog extends FleetInteractionDialogPluginImpl {

    private static final Logger log = Global.getLogger(RetreatFleetDialog.class);

    @Override
    public void optionSelected(String optionText, Object optionData) {
        // Let the parent handle the option first (this populates UI, handles state, etc.)
        super.optionSelected(optionText, optionData);

        // After parent processes, remove the CLEAN_DISENGAGE option if it exists
        // This effectively disables the story point escape mechanic
        if (this.options != null && this.options.hasOption(OptionId.CLEAN_DISENGAGE)) {
            this.options.removeOption(OptionId.CLEAN_DISENGAGE);
            log.info("HarshSector: Removed story point escape option (CLEAN_DISENGAGE)");
        }
    }
}
