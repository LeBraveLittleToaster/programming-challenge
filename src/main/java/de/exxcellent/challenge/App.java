package de.exxcellent.challenge;

import static de.exxcellent.challenge.constants.PathConstants.*;

/**
 * The entry class for your solution. This class is only aimed as starting point and not intended as baseline for your software
 * design. Read: create your own classes and packages as appropriate.
 *
 * @author Benjamin Schmid <benjamin.schmid@exxcellent.de>
 */
public final class App {


    /**
     * This is the main entry method of your program.
     *
     * @param args The CLI arguments passed
     */
    public static void main(String... args) {

        if (args.length != 2 || !args[0].equals(FOOTBALL_ARGUMENT) && !args[0].equals(WEATHER_ARGUMENT)) {
            System.err.println("Arguments not as intended.\nArguments: (--football or --weather) <filename.csv>");
        }
        FileAnalyzer analyzer = null;
        if (args[0].equals(FOOTBALL_ARGUMENT)) {
            analyzer = FileAnalyzer
                    .create(FILE_BASE_PATH + args[1], ChallengeType.FOOTBALL)
                    .orElseThrow(() -> new RuntimeException("Not FileReader possible"));

            try {
                String teamWithSmallestGoalSpread = analyzer.analyze();
                System.out.printf("Team with smallest goal spread       : %s%n", teamWithSmallestGoalSpread);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        } else {
            analyzer = FileAnalyzer
                    .create(FILE_BASE_PATH + args[1], ChallengeType.WEATHER)
                    .orElseThrow(() -> new RuntimeException("Not FileReader possible"));
            ;
            try {
                String dayWithSmallestTempSpread = analyzer.analyze();
                System.out.printf("Day with smallest temperature spread : %s%n", dayWithSmallestTempSpread);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }


    }
}
