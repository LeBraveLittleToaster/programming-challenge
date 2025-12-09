package de.exxcellent.challenge.processing.parser;

import de.exxcellent.challenge.data.Row;
import de.exxcellent.challenge.data.Table;
import de.exxcellent.challenge.processing.ITypeParser;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TypeParserDoubleTable implements ITypeParser<Double> {


    @Override
    public Optional<Table<Double>> parseStringTableData(Table<String> dataToParse) {
        Map<Integer, Row<Double>> dataDoubleTable = new HashMap<>();
        // Non parsable rows are ignored
        dataToParse
                .rows()
                .forEach((idx, value) -> parseRowToDoubleRow(value).ifPresent((parsedRow) -> dataDoubleTable.put(idx, parsedRow)));
        return Optional.of(new Table<>(dataToParse.header(), dataDoubleTable));
    }

    /**
     * Parses a {@link de.exxcellent.challenge.data.Row<String>} to {@link de.exxcellent.challenge.data.Row<Double>} for aggregating values
     *
     * @param dataToParse {@link de.exxcellent.challenge.data.Row<String>} with current data (can also be non-numerical
     * @return {@link de.exxcellent.challenge.data.Row<Double>} with parsed values, of Optional.empty if any strings are no Double values of null
     */
    private Optional<Row<Double>> parseRowToDoubleRow(Row<String> dataToParse) {
        try {
            return Optional.of(new Row<>(Arrays.stream(dataToParse.values())
                    .map(Double::valueOf)
                    .toArray(Double[]::new)));
        } catch (NullPointerException | NumberFormatException e) {
            System.err.println(e.getMessage());
        }
        return Optional.empty();
    }
}
