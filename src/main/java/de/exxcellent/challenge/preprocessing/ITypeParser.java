package de.exxcellent.challenge.preprocessing;

import java.util.Optional;

public interface ITypeParser<I, O> {
    Optional<O> parseData(I dataToParse);
}
