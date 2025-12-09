package de.exxcellent.challenge;

/**
 * Enum for both challengetypes. Also stores the columns needed for the aggregation stage encoded into the enum values
 */
public enum ChallengeType {

    WEATHER("MxT", "MnT", "Day"),
    FOOTBALL("Goals", "Goals Allowed", "Team");

    public final String[] aggregationColums;

    ChallengeType(String... aggregationColums) {
        this.aggregationColums = aggregationColums;
    }
}
