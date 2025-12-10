package de.exxcellent.challenge.reader.csv;

import de.exxcellent.challenge.constants.PathConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class CsvFileReaderTest {

    private CsvFileReader csvFileReader;

    @BeforeEach
    public void createCsvFileReader() throws RuntimeException, IOException {
        csvFileReader = new CsvFileReader();
    }

    /**
     * Intentionally trying to load wrong or non existing files
     * @throws IOException Files do not exist
     */
    @Test
    public void loadingWrongFile() throws IOException {
        assertTrue(csvFileReader.readFileToTable(null).isEmpty());
        assertTrue(csvFileReader.readFileToTable(this.getClass().getResourceAsStream("wrong/path/in/system" + PathConstants.WEATHER_FILENAME)).isEmpty());
        assertTrue(csvFileReader.readFileToTable(this.getClass().getResourceAsStream("")).isEmpty());
        assertTrue(csvFileReader.readFileToTable(this.getClass().getResourceAsStream(PathConstants.FILE_BASE_PATH + "WRONG_FILE_NAME.csv")).isEmpty());
    }

    /**
     * Try to load correct files and file paths with multiple slashes
     * @throws IOException Files do not exist
     */
    @Test
    public void loadingCorrectFile() throws IOException {
        assertTrue(csvFileReader.readFileToTable(this.getClass().getResourceAsStream(PathConstants.FILE_BASE_PATH+ PathConstants.FOOTBALL_FILENAME)).isPresent());
        assertTrue(csvFileReader.readFileToTable(this.getClass().getResourceAsStream(PathConstants.FILE_BASE_PATH+ PathConstants.WEATHER_FILENAME)).isPresent());
        assertTrue(csvFileReader.readFileToTable(this.getClass().getResourceAsStream(PathConstants.FILE_BASE_PATH + "/////" + PathConstants.FOOTBALL_FILENAME)).isPresent());
    }

    /**
     * Test that all lines are parsed when loading files
     * @throws IOException Files do not exist
     */
    @Test
    public void testAmountOfLoadedLinesAndHeader() throws IOException {
        var tableOpt = csvFileReader.readFileToTable(this.getClass().getResourceAsStream(PathConstants.FILE_BASE_PATH + PathConstants.FOOTBALL_FILENAME));
        assertTrue(tableOpt.isPresent());
        assertEquals(8,tableOpt.get().header().keySet().size());
        assertEquals(20,tableOpt.get().rows().size());

        tableOpt = csvFileReader.readFileToTable(this.getClass().getResourceAsStream(PathConstants.FILE_BASE_PATH + PathConstants.WEATHER_FILENAME));
        assertTrue(tableOpt.isPresent());
        assertEquals(14,tableOpt.get().header().keySet().size());
        assertEquals(30,tableOpt.get().rows().size());
    }
}
