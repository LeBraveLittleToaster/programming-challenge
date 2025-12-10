package de.exxcellent.challenge.data;

import java.util.Optional;

/**
 * Represents one datapoint from a dataset.
 * Currently only supports one value type per row.
 *
 * @param values Array of values, can be of any Type T.
 * @param <T>    generic to capture possible differences in data type
 */
public record Row<T>(T[] values) {

    /**
     * Returns value at index if in range of the rows values
     * @param index index in the values array
     * @return Optional.empty if not in the index range or the value at the given index, wrapped in Optional
     */
    public Optional<T> getValueAt(int index) {
        if (index < 0 || index >= values.length) {
            return Optional.empty();
        }
        return Optional.ofNullable(values[index]);
    }
}