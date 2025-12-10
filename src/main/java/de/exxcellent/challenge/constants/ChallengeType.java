package de.exxcellent.challenge.constants;

/**
 * Enum for both challengetypes. Also stores the columns needed for the aggregation stage encoded into the enum values
 */
public enum ChallengeType {

    /**
     * Weather Challenge columns used for aggregation (MxT - MnT) with Day being returned
     */
    WEATHER("MxT", "MnT", "Day"),
    /**
     * Football Challenge, columns used for aggregation (Goals - Goals Allowed) with Team being returned
     */
    FOOTBALL("Goals", "Goals Allowed", "Team");

    /**
     * Column names for aggregation
     */
    public final String[] aggregationColums;

    /**
     * 1..n Columns per Enum value for aggregation
     * @param aggregationColums Columns names (header) as String
     */
    ChallengeType(String... aggregationColums) {
        this.aggregationColums = aggregationColums;
    }
}
