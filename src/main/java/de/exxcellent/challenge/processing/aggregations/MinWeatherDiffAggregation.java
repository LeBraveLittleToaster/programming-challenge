package de.exxcellent.challenge.processing.aggregations;

import de.exxcellent.challenge.constants.ChallengeType;
import de.exxcellent.challenge.data.Table;
import de.exxcellent.challenge.processing.AggregationFunction;

import java.util.Optional;

/**
 * Aggregation Function for calculating the Minimum weather temperature Difference per day
 */
public class MinWeatherDiffAggregation extends AggregationFunction {


    public MinWeatherDiffAggregation() {
        super(ChallengeType.WEATHER);
    }

    /**
     * Aggregates the minimum temperature difference and return the Day  with the lowest difference
     * @param dataTable Read input file as {@link de.exxcellent.challenge.data.Table<String>}
     * @return Optional.empty if no Day could be specified or columns missing, or Optional with Day as String
     * @throws RuntimeException If the table does not contain the needed columns for aggregation
     */
    @Override
    public Optional<String> aggregate(Table<String> dataTable) throws RuntimeException {
        var dayNumber = getTemperatureDistancesPerDay(dataTable,
                this.challengeType.aggregationColums[0],
                this.challengeType.aggregationColums[1],
                this.challengeType.aggregationColums[2]);

        return dayNumber.isPresent() ? Optional.of(String.valueOf(dayNumber.get())) : Optional.empty();
    }

    /**
     * Computes the minimum temperature difference per day by converting the data table entries to the correct format and tracking the lowest temperature difference
     * @param dataTable Read input file as {@link de.exxcellent.challenge.data.Table<String>}
     * @param maxColumnName Column name in the header of the dataset for max temperature
     * @param minColumnName Column name in the header of the dataset for min temperature
     * @param dayColumnName Column name in the header of the dataset for days
     * @return Optional.empty if no day could be specified or columns missing, or Optional with Day as String
     * @throws RuntimeException If the table does not contain the needed columns for aggregation
     */
    private Optional<Integer> getTemperatureDistancesPerDay(Table<String> dataTable, String maxColumnName, String minColumnName, String dayColumnName) throws RuntimeException {
        int maxColumnIndex = dataTable.getHeaderIndexForName(maxColumnName)
                .orElseThrow(() -> new RuntimeException("maxColumnName not found"));
        int minColumnIndex = dataTable.getHeaderIndexForName(minColumnName)
                .orElseThrow(() -> new RuntimeException("minColumnName not found"));
        int dayColumnIndex = dataTable.getHeaderIndexForName(dayColumnName)
                .orElseThrow(() -> new RuntimeException("dayColumnName not found"));

        double lowestDiff = Double.MAX_VALUE;
        int bestDay = -1;

        for (var row : dataTable.rows().values()) {
            int day = Integer.parseInt(
                    row.getValueAt(dayColumnIndex)
                            .orElseThrow(() -> new RuntimeException("Day not present in row"))
            );

            double maxVal = Double.parseDouble(
                    row.getValueAt(maxColumnIndex)
                            .orElseThrow(() -> new RuntimeException("Max not present in row"))
            );

            double minVal = Double.parseDouble(
                    row.getValueAt(minColumnIndex)
                            .orElseThrow(() -> new RuntimeException("Min not present in row"))
            );

            double diff = maxVal - minVal;
            if (diff < lowestDiff) {
                lowestDiff = diff;
                bestDay = day;
            }
        }

        return bestDay == - 1 ? Optional.empty() : Optional.of(bestDay);
    }
}
