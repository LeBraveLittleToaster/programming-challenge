package de.exxcellent.challenge.processing.aggregations;

import de.exxcellent.challenge.constants.ChallengeType;
import de.exxcellent.challenge.data.Table;
import de.exxcellent.challenge.processing.AggregationFunction;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Aggregation Function for calculating the Minimum Goal Difference between shot and received goals
 */
public class MinGoalSpreadAggregation extends AggregationFunction {

    public MinGoalSpreadAggregation() {
        super(ChallengeType.FOOTBALL);
    }

    /**
     * Aggregates the minimum goal difference and return the Teamname of the team with the least difference
     * @param dataTable Read input file as {@link de.exxcellent.challenge.data.Table<String>}
     * @return Optional.empty if no team could be specified or columns missing, or Optional with teamname as String content
     * @throws RuntimeException If the table does not contain the needed columns for aggregation
     */
    @Override
    public Optional<String> aggregate(Table<String> dataTable) throws RuntimeException {
        return getSmallestGoalSpreadTeam(dataTable,
                this.challengeType.aggregationColums[0],
                this.challengeType.aggregationColums[1],
                this.challengeType.aggregationColums[2]);

    }

    /**
     * Computes the minimum goal spread by converting the data table entries to the correct format and tracking the lowest spread
     * @param dataTable Read input file as {@link de.exxcellent.challenge.data.Table<String>}
     * @param goalsColumnName Column name in the header of the dataset for Goals
     * @param goalsAllowedColumnName Column name in the header of the dataset for Goals Allowed
     * @param teamColumnName Column name in the header of the dataset for Teamname
     * @return Optional.empty if no team could be specified or columns missing, or Optional with teamname as String content
     * @throws RuntimeException If the table does not contain the needed columns for aggregation
     */
    private Optional<String> getSmallestGoalSpreadTeam(Table<String> dataTable, String goalsColumnName, String goalsAllowedColumnName, String teamColumnName) throws RuntimeException {
        var rsltTeamName = new AtomicReference<String>(null);
        var minGoalSpread = new AtomicInteger(Integer.MAX_VALUE);

        int goalsColumnIndex = dataTable.getHeaderIndexForName(goalsColumnName).orElseThrow(() -> new RuntimeException("GoalsColumnName not found"));
        int goalsAllowedColumnIndex = dataTable.getHeaderIndexForName(goalsAllowedColumnName).orElseThrow(() -> new RuntimeException("goalsAllowedColumnName not found"));

        int teamColumnIndex = dataTable.getHeaderIndexForName(teamColumnName).orElseThrow(() -> new RuntimeException("teamColumnName not found"));

        dataTable.rows().values().forEach(row -> {
            String teamName = row.getValueAt(teamColumnIndex)
                    .orElseThrow(() -> new RuntimeException("Team not present in row"));
            Integer goalsNumber = Integer.parseInt(row.getValueAt(goalsColumnIndex)
                    .orElseThrow(() -> new RuntimeException("Goals not present in row")));
            Integer goalsAllowedNumber = Integer.parseInt(row.getValueAt(goalsAllowedColumnIndex)
                    .orElseThrow(() -> new RuntimeException("Goals allowed not present in row")));
            int goalSpread = Math.abs(goalsNumber - goalsAllowedNumber);
            if (goalSpread < minGoalSpread.get()) {
                minGoalSpread.set(goalSpread);
                rsltTeamName.set(teamName);
            }
        });
        return rsltTeamName.get() == null ? Optional.empty() : Optional.of(rsltTeamName.get());
    }
}
