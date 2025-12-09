package de.exxcellent.challenge;

import de.exxcellent.challenge.constants.PathConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileAnalyzerTests {

    private FileAnalyzer fileAnalyzer;

    @BeforeEach
    public void setup() {
        fileAnalyzer = FileAnalyzer.create(PathConstants.FILE_BASE_PATH + PathConstants.WEATHER_FILENAME, ChallengeType.WEATHER).get();
    }

    @Test
    public void factoryTest(){
        assertTrue(FileAnalyzer.create("file-with-non-sense-ending.cool", ChallengeType.FOOTBALL).isEmpty());
        assertTrue(FileAnalyzer.create(PathConstants.FILE_BASE_PATH + PathConstants.FOOTBALL_FILENAME + "v", ChallengeType.FOOTBALL).isEmpty());

        assertTrue(FileAnalyzer.create(PathConstants.FILE_BASE_PATH + PathConstants.FOOTBALL_FILENAME, ChallengeType.FOOTBALL).isPresent());
        assertTrue(FileAnalyzer.create(PathConstants.FILE_BASE_PATH + PathConstants.WEATHER_FILENAME, ChallengeType.FOOTBALL).isPresent());
        assertTrue(FileAnalyzer.create(PathConstants.FILE_BASE_PATH + PathConstants.FOOTBALL_FILENAME, ChallengeType.WEATHER).isPresent());
        assertTrue(FileAnalyzer.create(PathConstants.FILE_BASE_PATH + PathConstants.WEATHER_FILENAME, ChallengeType.WEATHER).isPresent());
    }

    @Test
    public void tryRunTest() throws IOException {
        fileAnalyzer.analyze();
    }
}
