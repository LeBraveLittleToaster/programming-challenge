package de.exxcellent.challenge.data;

import java.util.Optional;

/**
 * Represents one datapoint from a dataset.
 * Currently only supports one value type per row.
 *
 * @param values Array of values, can be of any Type T. Consider {@link ITypeParser} instance for transforming the Row to another datatype
 * @param <T>    generic to capture possible differences in data type
 */
public record Row<T>(T[] values) {
    public Optional<T> getValueAt(int index) {
        if (index < 0 || index >= values.length) {
            return Optional.empty();
        }
        return Optional.ofNullable(values[index]);
    }
}