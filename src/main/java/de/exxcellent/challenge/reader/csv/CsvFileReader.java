package de.exxcellent.challenge.reader.csv;

import de.exxcellent.challenge.data.Row;
import de.exxcellent.challenge.data.Table;
import de.exxcellent.challenge.reader.IFileReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Reads Csv File and provides the content as {@link de.exxcellent.challenge.data.Table<String>} data structure
 */
public class CsvFileReader implements IFileReader {

    /**
     * Takes a filepath, reads the whole file and returns the content as {@link de.exxcellent.challenge.data.Table<String>}.
     *
     * @param csvFile File which should be read
     * @return If file is not available, throws IOException. If the file is empty, or the content cannot be parsed, return Optional.empty(). Otherwise, returns Optional of {@link de.exxcellent.challenge.data.Table<String>}
     */
    @Override
    public Optional<Table<String>> readFileToTable(InputStream csvFile) throws IOException {
        if (csvFile == null) {
            return Optional.empty();
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(csvFile, StandardCharsets.UTF_8));
        var headerLine = reader.readLine();

        if (headerLine == null || headerLine.isEmpty()) {
            System.out.println("No headerfile found");
            return Optional.empty();
        }

        var header = parseHeaderLine(headerLine);
        var rows = getValueLines(reader, header);
        if (rows.isEmpty()) {
            System.out.println("No rows found");
            return Optional.empty();
        }

        return Optional.of(new Table<>(header, rows));
    }

    /**
     * Parses lines which concrete values into {@link de.exxcellent.challenge.data.Row<String>} objects. Each column is converted to an entry in the row values array
     * @param reader Reader wrapping the inputstream of the file
     * @param header Contains header definitions of the read CSV
     * @return Map with the Row index and the concrete values as {@link de.exxcellent.challenge.data.Row<String>}
     * @throws IOException If the file is not present or accessible
     */
    private HashMap<Integer, Row<String>> getValueLines(BufferedReader reader, HashMap<String, Integer> header) throws IOException {
        var rows = new HashMap<Integer, Row<String>>();
        String line;
        AtomicInteger counter = new AtomicInteger(0);
        while ((line = reader.readLine()) != null) {
            var parsedValueLine = parseValueLine(line, header.size());
            parsedValueLine.ifPresent(row -> rows.put(counter.getAndIncrement(), row));
        }
        return rows;
    }

    /**
     * Parses only the first headerline of the csv into a Map object.
     * @param headerLine whole headerline as String already read from file
     * @return Map with the header name, mapped on the column index of the header
     */
    private HashMap<String, Integer> parseHeaderLine(String headerLine) {
        var headerValues = headerLine.split(",");
        var headerMap = new HashMap<String, Integer>();
        for (int i = 0; i < headerValues.length; i++) {
            headerMap.put(headerValues[i], i);
        }
        return headerMap;
    }

    /**
     * Parses one value line with concrete column values into a {@link de.exxcellent.challenge.data.Row<String>} object.
     * @param line whole line of file already read from file
     * @param expectedLineWidth expected length of line, based on amount of header columns for integrity check
     * @return Row with the concrete column values as {@link de.exxcellent.challenge.data.Row<String>}
     */
    private Optional<Row<String>> parseValueLine(String line, int expectedLineWidth) {
        var values = line.split(",");
        if (values.length != expectedLineWidth) {
            System.err.printf("Invalid line length: %d, expected: %d\n", expectedLineWidth, values.length);
            return Optional.empty();
        }
        return Optional.of(new Row<>(values));
    }
}
