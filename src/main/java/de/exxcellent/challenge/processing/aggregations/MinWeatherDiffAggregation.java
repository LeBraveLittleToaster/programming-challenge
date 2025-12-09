package de.exxcellent.challenge.processing.aggregations;

import de.exxcellent.challenge.constants.ChallengeType;
import de.exxcellent.challenge.data.Table;
import de.exxcellent.challenge.processing.AggregationFunction;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MinWeatherDiffAggregation extends AggregationFunction {

    public MinWeatherDiffAggregation() {
        super(ChallengeType.WEATHER);
    }

    @Override
    public Optional<String> aggregate(Table<String> dataTable) throws RuntimeException {
        var tempsPerDay = getTemperatureDistancesPerDay(dataTable,
                this.challengeType.aggregationColums[0],
                this.challengeType.aggregationColums[1],
                this.challengeType.aggregationColums[2]);

        Integer dayNumber = getLowestDifferenceDayNumber(tempsPerDay);
        return dayNumber == -1 ? Optional.empty() : Optional.of(dayNumber + "");
    }

    private static Integer getLowestDifferenceDayNumber(Map<Integer, Double> tempsPerDay) {
        Double highestValue = Double.MAX_VALUE;
        Integer dayNumber = -1;
        for (Map.Entry<Integer, Double> entry : tempsPerDay.entrySet()) {
            if (entry.getValue() < highestValue) {
                highestValue = entry.getValue();
                dayNumber = entry.getKey();
            }
        }
        return dayNumber;
    }

    private Map<Integer, Double> getTemperatureDistancesPerDay(Table<String> dataTable, String maxColumnName, String minColumnName, String dayColumnName) throws RuntimeException {
        Map<Integer, Double> temperatureDistancesPerDay = new HashMap<>();

        int maxColumnIndex = dataTable.getHeaderIndexForName(maxColumnName).orElseThrow(() -> new RuntimeException("maxColumnName not found"));
        int minColumnIndex = dataTable.getHeaderIndexForName(minColumnName).orElseThrow(() -> new RuntimeException("minColumnName not found"));
        int dayColumnIndex = dataTable.getHeaderIndexForName(dayColumnName).orElseThrow(() -> new RuntimeException("dayColumnName not found"));

        dataTable.rows().values().forEach(row -> {
            Integer dayNumber = Integer.parseInt(row.getValueAt(dayColumnIndex).orElseThrow(() -> new RuntimeException("Day not present in row")));
            Double maxValue = Double.parseDouble(row.getValueAt(maxColumnIndex).orElseThrow(() -> new RuntimeException("Max not present in row")));
            Double minValue = Double.parseDouble(row.getValueAt(minColumnIndex).orElseThrow(() -> new RuntimeException("Min not present in row")));

            temperatureDistancesPerDay.put(dayNumber, maxValue - minValue);
        });
        return temperatureDistancesPerDay;
    }


}
