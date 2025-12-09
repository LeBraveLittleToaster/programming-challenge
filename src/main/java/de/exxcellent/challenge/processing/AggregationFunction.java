package de.exxcellent.challenge.processing;

import de.exxcellent.challenge.constants.ChallengeType;
import de.exxcellent.challenge.data.Table;

import java.util.Optional;

public abstract class AggregationFunction {

    protected final ChallengeType challengeType;

    protected AggregationFunction(ChallengeType challengeType) {
        this.challengeType = challengeType;
    }

    abstract public Optional<String> aggregate(Table<String> dataTable);


}
