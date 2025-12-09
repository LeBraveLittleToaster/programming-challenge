package de.exxcellent.challenge.processing;

import de.exxcellent.challenge.data.Table;

import java.util.Optional;

public interface ITypeParser<T> {
    Optional<Table<T>> parseStringTableData(Table<String> dataToParse);
}