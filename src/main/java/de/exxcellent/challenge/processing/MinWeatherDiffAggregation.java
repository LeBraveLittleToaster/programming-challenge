package de.exxcellent.challenge.processing;

import de.exxcellent.challenge.ChallengeType;
import de.exxcellent.challenge.data.Table;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MinWeatherDiffAggregation extends AggregationFunction {

    private final ITypeParser<Double> typeParser;

    public MinWeatherDiffAggregation() {
        super(ChallengeType.WEATHER);
        this.typeParser = new TypeParserDoubleTable();
    }

    @Override
    public Optional<String> aggregate(Table<String> dataTable) throws RuntimeException{
        var dataDoubleTable = typeParser.parseStringTableData(dataTable);
        if (dataDoubleTable.isEmpty()) {return Optional.empty();}

        var tempsPerDay = getTemperatureDistancesPerDay(dataDoubleTable.get(),
                this.challengeType.aggregationColums[0],
                this.challengeType.aggregationColums[1],
                this.challengeType.aggregationColums[2]);

        Integer dayNumber = getLowestDifferenceDayNumber(tempsPerDay);
        return dayNumber == -1 ? Optional.empty() : Optional.of(dayNumber + "");
    }

    private static Integer getLowestDifferenceDayNumber(Map<Integer, Double> tempsPerDay) {
        Double highestValue = Double.MAX_VALUE;
        Integer dayNumber = -1;
        for(Map.Entry<Integer, Double> entry : tempsPerDay.entrySet()){
            if(entry.getValue() < highestValue){
                highestValue = entry.getValue();
                dayNumber = entry.getKey();
            }
        }
        return dayNumber;
    }

    private Map<Integer, Double> getTemperatureDistancesPerDay(Table<Double> dataTable, String maxColumnName, String minColumnName, String dayColumnName) throws RuntimeException {
        Map<Integer, Double> temperatureDistancesPerDay = new HashMap<>();
        int maxColumnIndex = dataTable.getHeaderIndexForName(maxColumnName).orElseThrow(() -> new RuntimeException("maxColumnName not found"));
        int minColumnIndex = dataTable.getHeaderIndexForName(minColumnName).orElseThrow(() -> new RuntimeException("minColumnName not found"));;
        int dayColumnIndex = dataTable.getHeaderIndexForName(dayColumnName).orElseThrow(() -> new RuntimeException("dayColumnName not found"));;

        dataTable.rows().values().forEach(row -> {
           Integer dayNumber =  row.getValueAt(dayColumnIndex).orElseThrow(() -> new RuntimeException("Day not present in row")).intValue();
           Double maxValue =  row.getValueAt(maxColumnIndex).orElseThrow(() -> new RuntimeException("Max not present in row"));
           Double minValue =  row.getValueAt(minColumnIndex).orElseThrow(() -> new RuntimeException("Min not present in row"));

           temperatureDistancesPerDay.put(dayNumber, maxValue - minValue);
        });
        return temperatureDistancesPerDay;
    }


}
