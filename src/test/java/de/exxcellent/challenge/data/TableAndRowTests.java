package de.exxcellent.challenge.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static de.exxcellent.challenge.aggregations.Generators.generateWeatherTable;
import static org.junit.jupiter.api.Assertions.*;

public class TableAndRowTests {
    private Table<String> weatherTestTable1Correct;
    private Table<String> weatherTestTable2Correct;

    @BeforeEach
    public void setup() {
        weatherTestTable1Correct = generateWeatherTable("MxT", "MnT", "Day", 150);
        weatherTestTable2Correct = generateWeatherTable("MxT", "MnT", "Day", 1);
    }

    @Test
    public void tableRowsValidityCheck(){
        assertEquals(150, weatherTestTable1Correct.rows().size());

        for(int i = 0; i < weatherTestTable1Correct.rows().size(); i++){
            weatherTestTable1Correct.rows().get(i).getValueAt(0);
            weatherTestTable1Correct.rows().get(i).getValueAt(1);
            weatherTestTable1Correct.rows().get(i).getValueAt(2);
            assertTrue(weatherTestTable1Correct.rows().get(i).getValueAt(3).isEmpty());
            assertTrue(weatherTestTable1Correct.rows().get(i).getValueAt(-1).isEmpty());
            assertTrue(weatherTestTable1Correct.rows().get(i).getValueAt(32465).isEmpty());
        }

        assertEquals(1, weatherTestTable2Correct.rows().size());

    }

    @Test
    public void tableHeaderValidityCheck(){
        assertEquals(3, weatherTestTable1Correct.header().size());

        assertEquals(3, weatherTestTable2Correct.header().size());

        assertTrue(weatherTestTable2Correct.getHeaderIndexForName("MxT").isPresent());
        assertTrue(weatherTestTable2Correct.getHeaderIndexForName("MnT").isPresent());
        assertTrue(weatherTestTable2Correct.getHeaderIndexForName("Day").isPresent());
        assertTrue(weatherTestTable2Correct.getHeaderIndexForName("Hello World").isEmpty());
        assertTrue(weatherTestTable2Correct.getHeaderIndexForName(null).isEmpty());
    }
}
