package de.exxcellent.challenge.processing;

import de.exxcellent.challenge.ChallengeType;
import de.exxcellent.challenge.data.Table;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class MinGoalSpreadAggregation extends AggregationFunction {

    public MinGoalSpreadAggregation() {
        super(ChallengeType.FOOTBALL);
    }

    @Override
    public Optional<String> aggregate(Table<String> dataTable) throws RuntimeException {
        return getSmallestGoalSpreadTeam(dataTable,
                this.challengeType.aggregationColums[0],
                this.challengeType.aggregationColums[1],
                this.challengeType.aggregationColums[2]);

    }

    private Optional<String> getSmallestGoalSpreadTeam(Table<String> dataTable, String goalsColumnName, String goalsAllowedColumnName, String teamColumnName) throws RuntimeException {
        var rsltTeamName = new AtomicReference<String>(null);
        var minGoalSpread = new AtomicInteger(Integer.MAX_VALUE);

        int goalsColumnIndex = dataTable.getHeaderIndexForName(goalsColumnName).orElseThrow(() -> new RuntimeException("GoalsColumnName not found"));
        int goalsAllowedColumnIndex = dataTable.getHeaderIndexForName(goalsAllowedColumnName).orElseThrow(() -> new RuntimeException("goalsAllowedColumnName not found"));
        ;
        int teamColumnIndex = dataTable.getHeaderIndexForName(teamColumnName).orElseThrow(() -> new RuntimeException("teamColumnName not found"));
        ;

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
