package it.units.advancedprogramming.project.entity;

import it.units.advancedprogramming.project.parsing.AbstractNode;
import it.units.advancedprogramming.project.parsing.ExpressionParser;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.util.*;

public class ExpressionsList {
    private final List<AbstractNode> expressions;

    public ExpressionsList(List<AbstractNode> expressions) {
        this.expressions = expressions;
    }

    public static ExpressionsList fromStrings(String... expressions) throws IllegalArgumentException {
        List<AbstractNode> expressionsAsList = new ArrayList<>();
        for (String expression : expressions) {
            expressionsAsList.add(new ExpressionParser(expression).parse());
        }
        return new ExpressionsList(expressionsAsList);
    }

    public List<List<Double>> evaluateExpressions(Map<String, List<Double>> variablesAndAssumedValues, int variableValuesNumber) throws ArithmeticException {
        checkExistenceOfVariablesInExpressions(variablesAndAssumedValues.keySet());

        List<List<Double>> results = new ArrayList<>();
        for (AbstractNode nodeExpression : expressions) {
            Map<String, Double> toCompute = new HashMap<>();
            List<Double> expressionsResults = new ArrayList<>();
            ExpressionBuilder expressionBuilder = new ExpressionBuilder(nodeExpression.toString());
            for (int i = 0; i < variableValuesNumber; i++) {
                for (String key : variablesAndAssumedValues.keySet()) {
                    if (variablesAndAssumedValues.get(key).isEmpty()) {
                        Set<String> variablesFromExpression = new ExpressionParser(nodeExpression.toString()).getVariablesFromExpression();
                        if (variablesFromExpression.contains(key)) {
                            throw new ArithmeticException(String.format("Variable '%s' exists but has no value", key));
                        }
                    } else {
                        toCompute.put(key, variablesAndAssumedValues.get(key).get(i));
                        expressionBuilder.variable(key);
                    }
                }
                double result = expressionBuilder
                        .build()
                        .setVariables(toCompute)
                        .evaluate();
                expressionsResults.add(result);
                results.add(expressionsResults);
            }
        }
        return results;
    }

    private void checkExistenceOfVariablesInExpressions(Set<String> variables) throws ArithmeticException {
        for (AbstractNode expression : expressions) {
            Set<String> variablesFromExpression = new ExpressionParser(expression.toString()).getVariablesFromExpression();
            for (String variable : variablesFromExpression) {
                if (!variables.contains(variable)) {
                    throw new ArithmeticException("Unvalued variable " + variable);
                }
            }
        }
    }

}
