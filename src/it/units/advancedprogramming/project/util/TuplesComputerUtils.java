package it.units.advancedprogramming.project.util;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

public final class TuplesComputerUtils {

    private TuplesComputerUtils() {
    }

    public static <T extends Number> List<List<T>> cartesianProduct(List<List<T>> tuples) throws ArithmeticException {
        if ((tuples.size() == 1) && (tuples.get(0) == null)) {
            throw new ArithmeticException("Cannot compute: only one variable with no values");
        }

        List<List<T>> result = new ArrayList<>();
        for (List<T> values : tuples) {
            if (!values.isEmpty()) {
                result.add(values);
            }
        }
        return Lists.cartesianProduct(result);
    }

    public static <T extends Number> List<List<T>> elementWiseMerging(List<List<T>> tuples) throws ArithmeticException {
        if ((tuples.size() == 1) && (tuples.get(0) == null)) {
            throw new ArithmeticException("Cannot compute: only one variable with no values");
        }

        boolean isSameLength = tuples.stream()
                .allMatch(l -> l.size() == tuples.get(0).size());
        if (!isSameLength) {
            throw new ArithmeticException("Variables have different number of values");
        }

        List<List<T>> result = new ArrayList<>();
        for (int i = 0; i < tuples.get(0).size(); i++) {
            List<T> tuple = new ArrayList<>();
            for (int j = 0; j < tuples.size(); j++) {
                tuple.add(j, tuples.get(j).get(i));
            }
            result.add(tuple);
        }
        return result;
    }

}
