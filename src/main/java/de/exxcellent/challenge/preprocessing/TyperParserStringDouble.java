package de.exxcellent.challenge.preprocessing;

import java.util.Optional;

public class TyperParserStringDouble implements  ITypeParser<String, Double> {
    @Override
    public Optional<Double> parseData(String dataToParse) {
        try {
            return Optional.of(Double.parseDouble(dataToParse));
        }catch (NullPointerException | NumberFormatException e){
            System.err.println(e.getMessage());
        }
        return Optional.empty();
    }
}
