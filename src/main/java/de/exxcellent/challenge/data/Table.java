package de.exxcellent.challenge.data;

import java.util.Map;

/**
 * Represents a dataset, read by {@link de.exxcellent.challenge.reader.IFileReader} instance.
 *
 * Rows can be mapped to input format and indexing is ensured to be unique by Map constraints
 * @param header Mapping header name to index in the source dataset
 * @param rows Represent single lines of data from the source dataset
 * @param <T> Rows are considered of the same type in the current implementation
 */
public record Table<T> (Map<String, Integer> header, Map<Integer, Row<T>> rows) {}
