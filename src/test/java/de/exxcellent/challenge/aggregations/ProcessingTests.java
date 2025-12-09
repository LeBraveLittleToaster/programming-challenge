package de.exxcellent.challenge.aggregations;

import de.exxcellent.challenge.constants.ChallengeType;
import de.exxcellent.challenge.FileAnalyzer;
import de.exxcellent.challenge.constants.PathConstants;
import de.exxcellent.challenge.data.Table;
import de.exxcellent.challenge.processing.aggregations.MinGoalSpreadAggregation;
import de.exxcellent.challenge.processing.aggregations.MinWeatherDiffAggregation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;

import java.util.Random;

import static de.exxcellent.challenge.aggregations.Generators.*;
import static org.junit.jupiter.api.Assertions.*;

public class ProcessingTests {



    private FileAnalyzer fileAnalyzerWeather;
    private FileAnalyzer fileAnalyzerFootball;

    private static Table<String> weatherTestTable1Correct;
    private static Table<String> weatherTable2Correct;
    private static Table<String> weatherTable3WrongLabels;
    private static Table<String> weatherTable4EmptyTable;
    private static Table<String> weatherTable5OneEntry;

    private static Table<String> footballTestTable1Correct;
    private static Table<String> footballTestTable2Correct;
    private static Table<String> footballTestTable3WrongLabels;

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

        footballTestTable1Correct = generateFootballTable("Team", "Goals", "Goals Allowed", random.nextInt(50, 150));
        footballTestTable2Correct = generateFootballTable( "Team", "Goals", "Goals Allowed", random.nextInt(50, 150));
        footballTestTable3WrongLabels = generateFootballTable("Goals", "GoalsAAllowed", "Team", random.nextInt(50, 150));

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


    @RepeatedTest(1000)
    public void testAggregationFunctionFootball(){

        var aggre = new MinGoalSpreadAggregation();
        aggre.aggregate(footballTestTable1Correct);
        aggre.aggregate(footballTestTable2Correct);
        assertThrows(RuntimeException.class, () -> aggre.aggregate(footballTestTable3WrongLabels));
    }

}
