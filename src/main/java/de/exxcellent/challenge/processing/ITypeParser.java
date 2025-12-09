package de.exxcellent.challenge.processing;

import de.exxcellent.challenge.data.Table;

import java.util.Optional;

public interface ITypeParser<O> {
    Optional<Table<O>> parseStringTableData(Table<String> dataToParse);
}
