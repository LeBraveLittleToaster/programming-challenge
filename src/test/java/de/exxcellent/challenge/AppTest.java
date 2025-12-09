package de.exxcellent.challenge;

import de.exxcellent.challenge.constants.PathConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Example JUnit 5 test case.
 * @author Benjamin Schmid <benjamin.schmid@exxcellent.de>
 */
class AppTest {

    private String successLabel = "not successful";
    private FileAnalyzer fileAnalyzerWeather;
    private FileAnalyzer fileAnalyzerFootball;


    @BeforeEach
    public void setup() {
        successLabel = "successful";
        fileAnalyzerWeather = FileAnalyzer.create(PathConstants.FILE_BASE_PATH + PathConstants.WEATHER_FILENAME, ChallengeType.WEATHER).get();
        fileAnalyzerFootball = FileAnalyzer.create(PathConstants.FILE_BASE_PATH + PathConstants.FOOTBALL_FILENAME, ChallengeType.FOOTBALL).get();
    }

    @Test
    void aPointlessTest() {
        assertEquals("successful", successLabel, "My expectations were not met");
    }

    @Test
    void runFootball() {
        App.main("--football", "football.csv");
    }

    @Test
    void runWeather() {
        App.main("--weather", "weather.csv");
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

}