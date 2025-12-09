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

    private static Table<String> weatherTestTable1Correct;
    private static Table<String> weatherTable2Correct;
    private static Table<String> weatherTable3WrongLabels;
    private static Table<String> weatherTable4EmptyTable;
    private static Table<String> weatherTable5OneEntry;

    private static Table<String> footballTestTable1Correct;
    private static Table<String> footballTestTable2Correct;

    @BeforeEach
    public void setup() {
        fileAnalyzerWeather = FileAnalyzer.create(PathConstants.FILE_BASE_PATH + PathConstants.WEATHER_FILENAME, ChallengeType.WEATHER).get();
        fileAnalyzerFootball = FileAnalyzer.create(PathConstants.FILE_BASE_PATH + PathConstants.FOOTBALL_FILENAME, ChallengeType.FOOTBALL).get();
    }

    @BeforeAll
    public static void setupTestDataset() {
        Random random = new Random();
        weatherTestTable1Correct = generateWeatherTable("MxT", "MnT", "Day", random.nextInt(50, 150));
        weatherTable2Correct = generateWeatherTable("MxT", "MnT", "Day", random.nextInt(50, 150));
        weatherTable3WrongLabels = generateWeatherTable("Max", "Min", "Days", random.nextInt(50, 150));
        weatherTable4EmptyTable = generateWeatherTable("Max", "Min", "Days", 0);
        weatherTable5OneEntry = generateWeatherOneEntryTable("MxT", "MnT", "Day");

        footballTestTable1Correct = generateFootballTable("Goals", "Goals Allowed", "Team", random.nextInt(50, 150));
        footballTestTable2Correct = generateFootballTable("Goals", "Goals Allowed", "Team", random.nextInt(50, 150));

    }

    @Test
    public void testWeatherAnswerIsCorrect() throws IOException {
        var result = fileAnalyzerWeather.analyze();
        assertEquals("14", result);
    }

    @Test
    public void testFootballAnswerIsCorrect() throws IOException {
        var result = fileAnalyzerFootball.analyze();
        assertEquals("Aston_Villa", result);
    }

    @RepeatedTest(1000)
    public void testAggregationFunctionWeather(){

        var aggre = new MinWeatherDiffAggregation();
        aggre.aggregate(weatherTestTable1Correct);
        aggre.aggregate(weatherTable2Correct);
        assertThrows(RuntimeException.class, () -> aggre.aggregate(weatherTable3WrongLabels));
        assertThrows(RuntimeException.class, () -> aggre.aggregate(weatherTable4EmptyTable));
        assertEquals(Integer.toString(ONE_ENTRY_DAY),aggre.aggregate(weatherTable5OneEntry).get());
    }

    private static Table<String> generateFootballTable(String headerTeam, String headerGoals, String headerGoalsAllowed, int numRows){
        Map<String, Integer> header = Map.of(
                headerGoals, 0,
                headerGoalsAllowed, 1,
                headerTeam, 2
        );

        Map<Integer, Row<String>> rows = new HashMap<>();
        Random random = new Random();

        for (int day = 1; day <= numRows; day++) {
            int goals = 60 + random.nextInt(40);
            int goalsAllowed = 30 + random.nextInt(30);
            rows.put(day, new Row<>(
                    new String[]{
                            Integer.toString(goals),
                            Integer.toString(goalsAllowed),
                            "Team_" + day}));
        }

        return new Table<>(header, rows);
    }

    private static Table<String> generateWeatherTable(String headerMax, String headerMin, String headerDay, int numRows){
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

    private static Table<String> generateWeatherOneEntryTable(String headerMax, String headerMin, String headerDay){
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
