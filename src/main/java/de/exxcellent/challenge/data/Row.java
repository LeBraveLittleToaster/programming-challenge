package de.exxcellent.challenge.data;

import java.util.List;

/**
 * Represents one datapoint from a dataset.
 * Currently only supports one value type per row.
 * @param values Array of values, can be of any Type T. Consider {@link de.exxcellent.challenge.preprocessing.ITypeParser} instance for transforming the Row to another datatype
 * @param <T> generic to capture possible differences in data type
 */
public record Row<T> (T[] values){}