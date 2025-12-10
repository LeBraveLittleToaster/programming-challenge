package de.exxcellent.challenge.reader;

import de.exxcellent.challenge.data.Table;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

/**
 * File Reader interface to parse source dataset to internal Table representation.
 *
 */
public interface IFileReader {
    /**
     * Take file as inputstream and transforms it to a {@link de.exxcellent.challenge.data.Table<String>}.
     * <p>
     * Optional and Throws definition depend on the implementation, but should be used as follows:
     * - IOExceptions for disk access problems
     * - Optional.empty() for parsing problems
     *
     * @param file File as inputstream (e.g. resource stream)
     * @return If exists, {@link de.exxcellent.challenge.data.Table<String>} containing the source data in 2D space
     * @throws IOException Should be used for IO related errors (e.g. File not Found, File not accessible)
     */
    Optional<Table<String>> readFileToTable(InputStream file) throws IOException;
}
