package de.exxcellent.challenge.aggregations;

import de.exxcellent.challenge.FileAnalyzer;
import de.exxcellent.challenge.constants.ChallengeType;
import de.exxcellent.challenge.constants.PathConstants;
import de.exxcellent.challenge.data.Row;
import de.exxcellent.challenge.data.Table;
import de.exxcellent.challenge.processing.aggregations.MinGoalSpreadAggregation;
import de.exxcellent.challenge.processing.aggregations.MinWeatherDiffAggregation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.*;

import static de.exxcellent.challenge.aggregations.Generators.*;
import static org.junit.jupiter.api.Assertions.*;

public class ProcessingTests {

    private static Table<String> weatherTestTable1Correct;
    private static Table<String> weatherTable2Correct;
    private static Table<String> weatherTable3WrongLabels;
    private static Table<String> weatherTable4EmptyTable;
    private static Table<String> weatherTable5OneEntry;

    private static Table<String> footballTestTable1Correct;
    private static Table<String> footballTestTable2Correct;
    private static Table<String> footballTestTable3WrongLabels;


    /**
     * Generates Datasets for testing
     */
    @BeforeAll
    public static void setupTestDataset() {
        Random random = new Random();
        weatherTestTable1Correct = generateWeatherTable("MxT", "MnT", "Day", 100);
        weatherTable2Correct = generateWeatherTable("MxT", "MnT", "Day", random.nextInt(50, 150));
        weatherTable3WrongLabels = generateWeatherTable("Max", "Min", "Days", random.nextInt(50, 150));
        weatherTable4EmptyTable = generateWeatherTable("Max", "Min", "Days", 0);
        weatherTable5OneEntry = generateWeatherOneEntryTable("MxT", "MnT", "Day");

        footballTestTable1Correct = generateFootballTable("Team", "Goals", "Goals Allowed", random.nextInt(50, 150));
        footballTestTable2Correct = generateFootballTable("Team", "Goals", "Goals Allowed", random.nextInt(50, 150));
        footballTestTable3WrongLabels = generateFootballTable("Goals", "GoalsAAllowed", "Team", random.nextInt(50, 150));

    }

    /**
     * Weather: Runs aggregations multiple times to ensure the method is stable
     */
    @RepeatedTest(100)
    public void testAggregationFunctionWeather() {
        var aggre = new MinWeatherDiffAggregation();
        aggre.aggregate(weatherTestTable1Correct);
        aggre.aggregate(weatherTable2Correct);
        assertThrows(RuntimeException.class, () -> aggre.aggregate(weatherTable3WrongLabels));
        assertThrows(RuntimeException.class, () -> aggre.aggregate(weatherTable4EmptyTable));
        assertEquals(Integer.toString(ONE_ENTRY_DAY), aggre.aggregate(weatherTable5OneEntry).get());
    }


    /**
     * Football: Runs aggregations multiple times to ensure the method is stable
     */
    @RepeatedTest(100)
    public void testAggregationFunctionFootball() {
        var aggre = new MinGoalSpreadAggregation();
        aggre.aggregate(footballTestTable1Correct);
        aggre.aggregate(footballTestTable2Correct);
        assertThrows(RuntimeException.class, () -> aggre.aggregate(footballTestTable3WrongLabels));
    }

    /**
     * Weather: Uses random dataset produced in @BeforeEach and checks that the aggregation never outputs the same result twice by removing the found value
     */
    @RepeatedTest(100)
    public void testWeatherCorrectness() {
        var aggre = new MinWeatherDiffAggregation();

        List<Table<String>> datasets = List.of(weatherTestTable1Correct, weatherTable2Correct);
        for (var dataset : datasets) {
            List<String> days = new LinkedList<>();
            Map<String, Integer> headerCopy = new HashMap<>(dataset.header());
            Map<Integer, Row<String>> rowsCopy = new HashMap<>(dataset.rows());

            Table<String> workingTable = new Table<>(headerCopy, rowsCopy);

            int daysColumnIndex = workingTable.getHeaderIndexForName("Day")
                    .orElseThrow(() -> new RuntimeException("Team column not found"));

            Optional<String> result;
            while ((result = aggre.aggregate(workingTable)).isPresent()) {
                String foundDay = result.get();
                days.add(foundDay);
                // removes days already present as result, no days should be present multiple times
                workingTable.rows().entrySet().removeIf(entry -> days.contains(entry.getValue().getValueAt(daysColumnIndex).get()));
            }
            assertTrue(workingTable.rows().isEmpty());
        }
    }

    /**
     * Weather: Uses random dataset produced in @BeforeEach and checks that the aggregation never outputs the same result twice by removing the found value
     */
    @Test
    public void testFootballCorrectness() {
        var aggre = new MinGoalSpreadAggregation();

        List<Table<String>> datasets = List.of(footballTestTable1Correct, footballTestTable2Correct);
        for (var dataset : datasets) {
            List<String> teams = new LinkedList<>();
            Map<String, Integer> headerCopy = new HashMap<>(dataset.header());
            Map<Integer, Row<String>> rowsCopy = new HashMap<>(dataset.rows());

            Table<String> workingTable = new Table<>(headerCopy, rowsCopy);

            int teamColumnIndex = workingTable.getHeaderIndexForName("Team")
                    .orElseThrow(() -> new RuntimeException("Team column not found"));

            Optional<String> result;
            while ((result = aggre.aggregate(workingTable)).isPresent()) {
                String foundTeam = result.get();
                teams.add(foundTeam);
                // removes days already present as result, no days should be present multiple times
                workingTable.rows().entrySet().removeIf(entry -> teams.contains(entry.getValue().getValueAt(teamColumnIndex).get()));
            }
            assertTrue(workingTable.rows().isEmpty());
        }
    }

}
