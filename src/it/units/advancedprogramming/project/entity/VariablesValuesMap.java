package it.units.advancedprogramming.project.entity;

import it.units.advancedprogramming.project.parsing.Constant;
import it.units.advancedprogramming.project.parsing.Variable;
import it.units.advancedprogramming.project.parsing.VariablesValuesParser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class VariablesValuesMap {
    private final Map<Variable, List<Constant>> variablesValuesMap;

    public VariablesValuesMap(Map<Variable, List<Constant>> variablesValuesMap) {
        this.variablesValuesMap = variablesValuesMap;
    }

    public static VariablesValuesMap fromString(String variablesValues) throws IllegalArgumentException {
        Map<Variable, List<Constant>> parsedVariablesValues;
        try {
            parsedVariablesValues = new VariablesValuesParser(variablesValues).parse();
        } catch (StringIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Wrong values format for variable");
        }
        return new VariablesValuesMap(parsedVariablesValues);
    }

    public Map<String, List<Double>> elaborate() throws IllegalArgumentException {
        Map<String, List<Double>> variablesValues = new TreeMap<>();
        for (Map.Entry<Variable, List<Constant>> entry : variablesValuesMap.entrySet()) {
            List<Double> values = new ArrayList<>();
            if (entry.getValue().isEmpty()) {
                variablesValues.put(String.valueOf(entry.getKey()), values);
            } else {
                double xStep = entry.getValue().get(1).getValue();
                if (xStep < 0) {
                    throw new IllegalArgumentException("Step cannot be negative");
                }
                double xLower = entry.getValue().get(0).getValue();
                double xUpper = entry.getValue().get(2).getValue();
                for (BigDecimal i = BigDecimal.valueOf(xLower); i.doubleValue() <= xUpper; i = i.add(BigDecimal.valueOf(xStep))) {
                    values.add(i.doubleValue());
                }
                variablesValues.put(String.valueOf(entry.getKey()), values);
            }
        }
        return variablesValues;
    }

}
