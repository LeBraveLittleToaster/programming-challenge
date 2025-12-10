package de.exxcellent.challenge.processing;

import de.exxcellent.challenge.constants.ChallengeType;
import de.exxcellent.challenge.data.Table;

import java.util.Optional;

/**
 * Generic class for storing and running Aggregation functions in the {@link de.exxcellent.challenge.FileAnalyzer} object
 */
public abstract class AggregationFunction {

    /**
     * Indicates which aggregation should be called, is statically set in the subclass constructor
     */
    protected final ChallengeType challengeType;

    /**
     * Super constructor for statically indicating which aggregationfunctions should be used
     */
    protected AggregationFunction(ChallengeType challengeType) {
        this.challengeType = challengeType;
    }

    /**
     * Runs the aggregation on the dataset. The dataset is provided, but additional aggregation parameters should be provided in the subclass constructor
     * @param dataTable Read input file as {@link de.exxcellent.challenge.data.Table<String>}
     * @return Result as String Optional of empty
     */
    abstract public Optional<String> aggregate(Table<String> dataTable);


}
