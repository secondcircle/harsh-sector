package harshsector.features.retreat;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;

import org.apache.log4j.Logger;

/**
 * Utility class for calculating burn speeds and delays for the tactical retreat system.
 */
public class BurnSpeedCalculator {

    private static final Logger log = Global.getLogger(BurnSpeedCalculator.class);

    // Default burn level if we can't determine the actual value
    private static final int DEFAULT_BURN = 8;

    /**
     * Get the minimum burn level of the player's fleet (slowest non-mothballed ship).
     * This determines the fleet's effective travel speed.
     *
     * @return The lowest burn level among active player ships
     */
    public static int getPlayerFleetMinBurn() {
        CampaignFleetAPI playerFleet = Global.getSector().getPlayerFleet();
        if (playerFleet == null) {
            log.warn("BurnSpeedCalculator: No player fleet found");
            return DEFAULT_BURN;
        }

        int minBurn = Integer.MAX_VALUE;
        for (FleetMemberAPI member : playerFleet.getFleetData().getMembersListCopy()) {
            // Skip mothballed ships - they don't affect fleet speed
            if (member.isMothballed()) continue;

            int burn = getShipBurn(member);
            if (burn < minBurn) {
                minBurn = burn;
            }
        }

        // Fallback if somehow no ships
        if (minBurn == Integer.MAX_VALUE) {
            log.warn("BurnSpeedCalculator: No active ships in player fleet");
            return DEFAULT_BURN;
        }

        return minBurn;
    }

    /**
     * Get the burn level for a specific ship.
     *
     * Uses FleetMemberAPI.getStats().getMaxBurnLevel() which accounts for
     * hull spec base burn plus any modifiers from hullmods, skills, etc.
     *
     * Typical values: frigates ~10, destroyers ~9, cruisers ~8, capitals ~7-8
     *
     * @param member The fleet member to check
     * @return The ship's effective burn level
     */
    public static int getShipBurn(FleetMemberAPI member) {
        if (member == null) return DEFAULT_BURN;

        try {
            // Get the modified burn level from ship stats
            // This includes base hull burn + any modifications from hullmods, etc.
            if (member.getStats() != null && member.getStats().getMaxBurnLevel() != null) {
                int burn = member.getStats().getMaxBurnLevel().getModifiedInt();
                // Sanity check - burn should be reasonable (1-20 range)
                if (burn > 0 && burn <= 20) {
                    return burn;
                }
            }

            // Fallback: try to estimate from hull size if stats unavailable
            log.warn("BurnSpeedCalculator: Could not get stats for " + member.getShipName() +
                     ", using hull size estimate");
            return estimateBurnFromHullSize(member);

        } catch (Exception e) {
            log.warn("BurnSpeedCalculator: Error getting burn for " + member.getShipName(), e);
            return estimateBurnFromHullSize(member);
        }
    }

    /**
     * Estimate burn level from hull size as a fallback.
     * These are rough approximations based on typical vanilla values.
     */
    private static int estimateBurnFromHullSize(FleetMemberAPI member) {
        if (member.isCapital()) return 7;
        if (member.isCruiser()) return 8;
        if (member.isDestroyer()) return 9;
        if (member.isFrigate()) return 10;
        return DEFAULT_BURN;
    }

    /**
     * Calculate the delay in seconds for an enemy ship based on burn speed differential.
     *
     * Ships with burn >= playerMinBurn get no delay (they can catch up immediately).
     * Slower ships are delayed by (playerMinBurn - shipBurn) * delayPerBurn seconds.
     *
     * @param shipBurn The enemy ship's burn level
     * @param playerMinBurn The player fleet's minimum burn level
     * @param delayPerBurn Seconds of delay per burn level difference
     * @param maxDelay Maximum delay cap in seconds
     * @return Delay in seconds (0 if ship is fast enough)
     */
    public static float calculateDelay(int shipBurn, int playerMinBurn, float delayPerBurn, float maxDelay) {
        if (shipBurn >= playerMinBurn) {
            // Ship is fast enough to catch up immediately
            return 0f;
        }

        int burnDiff = playerMinBurn - shipBurn;
        float delay = burnDiff * delayPerBurn;

        // Apply maximum delay cap
        return Math.min(delay, maxDelay);
    }
}
