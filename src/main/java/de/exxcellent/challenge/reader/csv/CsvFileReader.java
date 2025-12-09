package de.exxcellent.challenge.reader.csv;

import de.exxcellent.challenge.data.Row;
import de.exxcellent.challenge.data.Table;
import de.exxcellent.challenge.reader.IFileReader;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class CsvFileReader implements IFileReader {

    /**
     * Takes a filepath, reads the whole file and returns the content as {@link de.exxcellent.challenge.data.Table<String>}.
     *
     * @param csvFile File which should be read
     * @return If file is not available, throws IOException. If the file is empty, or the content cannot be parsed, return Optional.empty(). Otherwise, returns Optional of {@link de.exxcellent.challenge.data.Table<String>}
     */
    @Override
    public Optional<Table<String>> readFileToTable(InputStream csvFile) throws IOException {
        if(csvFile == null){return Optional.empty();}

        BufferedReader reader = new BufferedReader(new InputStreamReader(csvFile, StandardCharsets.UTF_8));
        var headerLine = reader.readLine();

        if(headerLine == null || headerLine.isEmpty()){
            System.out.println("No headerfile found");
            return Optional.empty();
        }

        var header = parseHeaderLine(headerLine);
        var rows = getValueLines(reader, header);
        if(rows.isEmpty()){
            System.out.println("No rows found");
            return Optional.empty();
        }

        return Optional.of(new Table<>(header, rows));
    }

    private HashMap<Integer, Row<String>> getValueLines(BufferedReader reader, HashMap<String, Integer> header) throws IOException {
        var rows = new HashMap<Integer, Row<String>>();
        String line;
        AtomicInteger counter = new AtomicInteger(0);
        while((line = reader.readLine()) != null){
            var parsedValueLine = parseValueLine(line, header.size());
            parsedValueLine.ifPresent(row -> rows.put(counter.getAndIncrement(), row));
        }
        return rows;
    }

    private HashMap<String, Integer> parseHeaderLine(String headerLine){
        var headerValues = headerLine.split(",");
        var headerMap = new HashMap<String, Integer>();
        for(int i = 0; i < headerValues.length; i++){
            headerMap.put(headerValues[i], i);
        }
        return headerMap;
    }

    private Optional<Row<String>> parseValueLine(String line, int expectedLineWidth){
        var values = line.split(",");
        if(values.length != expectedLineWidth){
            System.err.printf("Invalid line length: %d, expected: %d\n", expectedLineWidth, values.length);
            return Optional.empty();
        }
        return Optional.of(new Row<>(values));
    }
}
