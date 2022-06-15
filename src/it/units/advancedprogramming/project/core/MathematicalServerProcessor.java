package it.units.advancedprogramming.project.core;

import it.units.advancedprogramming.project.entity.ExpressionsList;
import it.units.advancedprogramming.project.entity.ResponseBuilder;
import it.units.advancedprogramming.project.entity.VariablesValuesMap;
import it.units.advancedprogramming.project.enumeration.ErrorType;
import it.units.advancedprogramming.project.enumeration.ItemsSeparator;
import it.units.advancedprogramming.project.util.LoggerUtils;
import it.units.advancedprogramming.project.util.TuplesComputerUtils;

import java.util.*;

public class MathematicalServerProcessor implements CommandProcessor {
    private final MathematicalServerStatistics mathematicalServerStatistics;

    private enum StatisticRequest {
        STAT_REQS, STAT_AVG_TIME, STAT_MAX_TIME
    }

    private enum ValueKind {
        GRID, LIST
    }

    private enum ComputationKind {
        MIN, MAX, AVG, COUNT
    }

    public MathematicalServerProcessor() {
        this.mathematicalServerStatistics = new MathematicalServerStatistics();
    }

    @Override
    public String process(String request) {
        double startProcessingTime = System.nanoTime();
        double processedRequest;

        if (isInEnum(StatisticRequest.class, request)) {
            synchronized (mathematicalServerStatistics) {
                processedRequest = processStatRequest(StatisticRequest.valueOf(request));
                double elapsedTime = ((System.nanoTime() - startProcessingTime) / 1e9);
                updateStatistics(elapsedTime);
                return ResponseBuilder.buildOkResponse(elapsedTime, processedRequest);
            }
        } else {
            try {
                processedRequest = processComputationRequest(request);
                synchronized (mathematicalServerStatistics) {
                    double elapsedTime = ((System.nanoTime() - startProcessingTime) / 1e9);
                    updateStatistics(elapsedTime);
                    return ResponseBuilder.buildOkResponse(elapsedTime, processedRequest);
                }
            } catch (IllegalArgumentException e) {
                String message = ErrorType.INPUT.getMessage() + " " + e.getMessage();
                LoggerUtils.LOGGER.warning(message);
                return ResponseBuilder.buildErrorResponse(message);
            } catch (ArithmeticException e) {
                String message = ErrorType.COMPUTATION.getMessage() + " " + e.getMessage();
                LoggerUtils.LOGGER.warning(message);
                return ResponseBuilder.buildErrorResponse(message);
            } catch (OutOfMemoryError e) {
                String message = ErrorType.MEMORY.getMessage() + " Computation too large";
                LoggerUtils.LOGGER.warning(message);
                return ResponseBuilder.buildErrorResponse(message);
            }
        }
    }

    private double processStatRequest(StatisticRequest statisticRequest) {
        return switch (statisticRequest) {
            case STAT_REQS -> mathematicalServerStatistics.getOkResponseCount();
            case STAT_AVG_TIME -> mathematicalServerStatistics.getAvgResponseTime();
            case STAT_MAX_TIME -> mathematicalServerStatistics.getMaxResponseTime();
        };
    }

    private static double processComputationRequest(String computationRequest) throws IllegalArgumentException, ArithmeticException {
        final int minCommandItemsNumber = 3;

        String[] splittedComputationRequest = computationRequest.split(ItemsSeparator.CONTENT.getSymbol());
        if (splittedComputationRequest.length < minCommandItemsNumber) {
            throw new IllegalArgumentException("Wrong command format");
        }
        String[] commandKinds = splittedComputationRequest[0].split(ItemsSeparator.COMPUTATION.getSymbol());
        if (commandKinds.length != 2) {
            throw new IllegalArgumentException("Incorrect syntax for 'kind' commands");
        }
        if (!isInEnum(ComputationKind.class, commandKinds[0])) {
            throw new IllegalArgumentException(String.format("'%s' is not a valid computation kind", commandKinds[0]));
        }
        if (!isInEnum(ValueKind.class, commandKinds[1])) {
            throw new IllegalArgumentException(String.format("'%s' is not a valid values kind", commandKinds[1]));
        }

        VariablesValuesMap variablesValuesMap = VariablesValuesMap.fromString(splittedComputationRequest[1]);
        Map<String, List<Double>> elaboratedVariablesValues = variablesValuesMap.elaborate();

        List<List<Double>> tuples = handleValuesKind(ValueKind.valueOf(commandKinds[1]), elaboratedVariablesValues.values().stream().toList());

        ExpressionsList expressionList = ExpressionsList.fromStrings(Arrays.copyOfRange(splittedComputationRequest, 2, splittedComputationRequest.length));
        Map<String, List<Double>> variablesTupleValues = new TreeMap<>();
        List<Double> tupleValues = new ArrayList<>();
        int j = 0;
        for (String key : elaboratedVariablesValues.keySet()) {
            if (elaboratedVariablesValues.get(key).isEmpty()) {
                variablesTupleValues.put(key, elaboratedVariablesValues.get(key));
            } else {
                for (List<Double> tuple : tuples) {
                    tupleValues.add(tuple.get(j));
                }
                variablesTupleValues.put(key, tupleValues);
                j++;
                tupleValues = new ArrayList<>();
            }
        }
        List<List<Double>> evaluatedExpressions = new ArrayList<>();
        if (!ComputationKind.valueOf(commandKinds[0]).equals(ComputationKind.COUNT)) {
            evaluatedExpressions = expressionList.evaluateExpressions(variablesTupleValues, tuples.size());
        }
        return handleComputationKind(ComputationKind.valueOf(commandKinds[0]), evaluatedExpressions, tuples.size());
    }

    private static List<List<Double>> handleValuesKind(ValueKind valueKind, List<List<Double>> values) throws ArithmeticException {
        return switch (valueKind) {
            case GRID -> TuplesComputerUtils.cartesianProduct(values);
            case LIST -> TuplesComputerUtils.elementWiseMerging(values);
        };
    }

    private static double handleComputationKind(ComputationKind computationKind, List<List<Double>> results, double tuplesSize) {
        return switch (computationKind) {
            case MIN -> Collections.min(results.stream()
                    .flatMap(List::stream)
                    .toList()
            );
            case MAX -> Collections.max(results.stream()
                    .flatMap(List::stream)
                    .toList()
            );
            case AVG -> (results.get(0).stream()
                    .mapToDouble(d -> d)
                    .sum() / tuplesSize
            );
            case COUNT -> tuplesSize;
        };
    }

    private static boolean isInEnum(Class<? extends Enum<?>> enumClass, String s) {
        return Arrays.stream(enumClass.getEnumConstants())
                .anyMatch(e -> e.toString().equals(s)
                );
    }

    private void updateStatistics(double time) {
        mathematicalServerStatistics.incrementOkResponseCount();
        mathematicalServerStatistics.updateAvgResponseTime(time);
        mathematicalServerStatistics.updateMaxResponseTimeIfGreater(time);
    }

}
