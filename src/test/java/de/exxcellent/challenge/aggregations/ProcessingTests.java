package de.exxcellent.challenge.aggregations;

import de.exxcellent.challenge.ChallengeType;
import de.exxcellent.challenge.FileAnalyzer;
import de.exxcellent.challenge.constants.PathConstants;
import de.exxcellent.challenge.data.Row;
import de.exxcellent.challenge.data.Table;
import de.exxcellent.challenge.processing.MinWeatherDiffAggregation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class ProcessingTests {

    private static final Integer ONE_ENTRY_MAX = 20;
    private static final Integer ONE_ENTRY_MIN = 10;
    private static final Integer ONE_ENTRY_DAY = 5;

    private FileAnalyzer fileAnalyzerWeather;
    private FileAnalyzer fileAnalyzerFootball;

    private static Table<String> testTable1Correct;
    private static Table<String> testTable2Correct;
    private static Table<String> testTable3WrongLabels;
    private static Table<String> testTable4EmptyTable;
    private static Table<String> testTable5OneEntry;

    @BeforeEach
    public void setup() {
        fileAnalyzerWeather = FileAnalyzer.create(PathConstants.FILE_BASE_PATH + PathConstants.WEATHER_FILENAME, ChallengeType.WEATHER).get();
        //fileAnalyzerFootball = FileAnalyzer.create(PathConstants.FILE_BASE_PATH + PathConstants.FOOTBALL_FILENAME, ChallengeType.FOOTBALL).get();
    }

    @BeforeAll
    public static void setupTestDataset() {
        Random random = new Random();
        testTable1Correct = generateTable("MxT", "MnT", "Day", random.nextInt(50, 150));
        testTable2Correct = generateTable("MxT", "MnT", "Day", random.nextInt(50, 150));
        testTable3WrongLabels = generateTable("Max", "Min", "Days", random.nextInt(50, 150));
        testTable4EmptyTable = generateTable("Max", "Min", "Days", 0);
        testTable5OneEntry = generateOneEntryTable("MxT", "MnT", "Day");
    }

    @Test
    public void testWeatherAnswerIsCorrect() throws IOException {
        var result = fileAnalyzerWeather.analyze();
        assertEquals("14", result);
    }

    @Test
    public void testFootballAnswerIsCorrect() throws IOException {

    }

    @RepeatedTest(1000)
    public void testAggregationFunctionWeather(){

        var aggre = new MinWeatherDiffAggregation();
        aggre.aggregate(testTable1Correct);
        aggre.aggregate(testTable2Correct);
        assertThrows(RuntimeException.class, () -> aggre.aggregate(testTable3WrongLabels));
        assertThrows(RuntimeException.class, () -> aggre.aggregate(testTable4EmptyTable));
        assertEquals(Integer.toString(ONE_ENTRY_DAY),aggre.aggregate(testTable5OneEntry).get());
    }

    private static Table<String> generateTable(String headerMax, String headerMin, String headerDay, int numRows){
        Map<String, Integer> header = Map.of(
                headerMax, 0,
                headerMin, 1,
                headerDay, 2
        );

        Map<Integer, Row<String>> rows = new HashMap<>();
        Random random = new Random();

        for (int day = 1; day <= numRows; day++) {
            int maxT = 60 + random.nextInt(40);
            int minT = 30 + random.nextInt(30);
            rows.put(day, new Row<>(
                    new String[]{
                            Integer.toString(maxT),
                            Integer.toString(minT),
                            Integer.toString(day)}));
        }

        return new Table<>(header, rows);
    }

    private static Table<String> generateOneEntryTable(String headerMax, String headerMin, String headerDay){
        Map<String, Integer> header = Map.of(
                headerMax, 0,
                headerMin, 1,
                headerDay, 2
        );

        Map<Integer, Row<String>> rows = new HashMap<>();
        rows.put(1, new Row<>(
                    new String[]{
                            Integer.toString(ONE_ENTRY_MAX),
                            Integer.toString(ONE_ENTRY_MIN),
                            Integer.toString(ONE_ENTRY_DAY)}));

        return new Table<>(header, rows);
    }
}
