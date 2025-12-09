package de.exxcellent.challenge.aggregations;

import de.exxcellent.challenge.data.Row;
import de.exxcellent.challenge.data.Table;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Generators {

    public static final Integer ONE_ENTRY_MAX = 20;
    public static final Integer ONE_ENTRY_MIN = 10;
    public static final Integer ONE_ENTRY_DAY = 5;

    static Table<String> generateFootballTable(String headerTeam, String headerGoals, String headerGoalsAllowed, int numRows){
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

    public static Table<String> generateWeatherTable(String headerMax, String headerMin, String headerDay, int numRows){
        Map<String, Integer> header = Map.of(
                headerMax, 0,
                headerMin, 1,
                headerDay, 2
        );

        Map<Integer, Row<String>> rows = new HashMap<>();
        Random random = new Random();

        for (int day = 0; day < numRows; day++) {
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

    static Table<String> generateWeatherOneEntryTable(String headerMax, String headerMin, String headerDay){
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
