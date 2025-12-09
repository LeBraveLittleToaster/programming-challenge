package de.exxcellent.challenge;

import de.exxcellent.challenge.data.Table;
import de.exxcellent.challenge.processing.AggregationFunction;
import de.exxcellent.challenge.processing.MinGoalSpreadAggregation;
import de.exxcellent.challenge.processing.MinWeatherDiffAggregation;
import de.exxcellent.challenge.reader.IFileReader;
import de.exxcellent.challenge.reader.csv.CsvFileReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

/**
 * Central analyzer capabilities. Provides factory method for creating an instance of the analyzer with the appropriate FileReader for the given file ending.
 * After creation, analyzer instance orchestrates the loading, parsing, transforming and aggregating of the given dataset filepath.
 * <p>
 * The ChallengeType enum provides the needed definitions of columns for aggregations on the data.
 */
public class FileAnalyzer {
    /**
     * Filepath to the source dataset
     */
    private final String filePath;
    /**
     * FileReader depending on the file ending/type
     */
    private final IFileReader fileReader;

    private final AggregationFunction aggregationFunction;

    /**
     *
     * @param filePath   Filepath to the source dataset
     * @param fileReader FileReader depending on the file ending of filePath parameter
     */
    private FileAnalyzer(String filePath, IFileReader fileReader, AggregationFunction aggregationFunction) {
        this.filePath = filePath;
        this.fileReader = fileReader;
        this.aggregationFunction = aggregationFunction;
    }

    /**
     * Factory method for creating file analyzer instance
     *
     * @param absolutFilePath Filepath to the source dataset
     * @param challengeType   Challengetype for retrieving the columns for aggregation
     * @return Optional.empty() if the filenending is not supported or an optional with the FileAnalyzer instance
     */
    public static Optional<FileAnalyzer> create(String absolutFilePath, ChallengeType challengeType) {
        try {
            var fileReader = getFileReaderFromFileEnding(absolutFilePath);
            var aggregationFunction = getAggregationFunctionFromChallengeType(challengeType);
            return Optional.of(new FileAnalyzer(absolutFilePath, fileReader, aggregationFunction));
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            return Optional.empty();
        }

    }

    private static AggregationFunction getAggregationFunctionFromChallengeType(ChallengeType challengeType) {
        return switch (challengeType) {
            case WEATHER -> new MinWeatherDiffAggregation();
            case FOOTBALL -> new MinGoalSpreadAggregation();
        };
    }

    /**
     * Gets FileReader instance based on filepath ending
     *
     * @param filePath Filepath to source dataset file
     * @return {@link de.exxcellent.challenge.reader.IFileReader} instance
     * @throws IllegalArgumentException Thrown if the ending is not supported or not in the path
     */
    private static IFileReader getFileReaderFromFileEnding(String filePath) throws IllegalArgumentException {
        var fileEnding = filePath.substring(filePath.lastIndexOf('.'));

        System.out.println("File ending is: " + fileEnding);
        return switch (fileEnding) {
            case ".csv" -> new CsvFileReader();
            // Here could be JSON etc.
            default -> throw new IllegalArgumentException("File Ending not registered");
        };
    }

    public String analyze() throws RuntimeException, IOException {
        InputStream fileAsInStream = this.getClass().getResourceAsStream(this.filePath);
        Table<String> table = fileReader.readFileToTable(fileAsInStream).orElseThrow(() -> new RuntimeException("Failed to read file!"));
        return aggregationFunction.aggregate(table).orElse("ERROR in aggregation function");
    }

}
