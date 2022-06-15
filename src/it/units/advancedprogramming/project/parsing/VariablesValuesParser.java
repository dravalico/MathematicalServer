package it.units.advancedprogramming.project.parsing;

import it.units.advancedprogramming.project.enumeration.ItemsSeparator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VariablesValuesParser extends AbstractTokenParser<Map<Variable, List<Constant>>> {
    private final String string;
    private int cursor;

    public VariablesValuesParser(String string) {
        super();
        this.string = string;
        cursor = 0;
    }

    @Override
    public Map<Variable, List<Constant>> parse() throws IllegalArgumentException, StringIndexOutOfBoundsException {
        Map<Variable, List<Constant>> parsedVariableValues = new HashMap<>();
        Token token;
        final int variableConstantsNumber = 3;

        while (cursor < string.length()) {
            token = TokenType.VARIABLE.next(string, cursor);
            if (token != null && token.start == cursor) {
                cursor = token.end;
                Token tempToken = token;
                List<Constant> values = new ArrayList<>();
                Variable variable = new Variable(string.substring(tempToken.start, tempToken.end));

                if (cursor == string.length()) {
                    if (parsedVariableValues.containsKey(variable)) {
                        throw new IllegalArgumentException("Variables with same name");
                    }
                    parsedVariableValues.put(variable, values);
                    return parsedVariableValues;
                }

                token = TokenType.VARIABLE_SEPARATOR.next(string, cursor);
                if (token != null && token.start == cursor) {
                    cursor = token.end;
                } else {
                    for (int i = 0; i < variableConstantsNumber; i++) {
                        token = TokenType.VALUES_SEPARATOR.next(string, cursor);
                        if (token != null && token.start == cursor) {
                            cursor = token.end;

                            token = TokenType.ALSO_NEGATIVE_CONSTANT.next(string, cursor);
                            if (token != null && token.start == cursor) {
                                cursor = token.end;
                                values.add(new Constant(Double.parseDouble(string.substring(token.start, token.end))));
                            } else {
                                throw new StringIndexOutOfBoundsException();
                            }
                        } else {
                            throw new IllegalArgumentException(String.format(
                                    "Unexpected char at %d: '%s'",
                                    cursor,
                                    string.charAt(cursor)
                            ));
                        }
                    }
                    if (cursor == string.length()) {
                        if (parsedVariableValues.containsKey(variable)) {
                            throw new IllegalArgumentException("Variables with same name");
                        }
                        parsedVariableValues.put(variable, values);
                        break;
                    }
                    token = TokenType.VARIABLE_SEPARATOR.next(string, cursor);
                    if (token != null && token.start == cursor) {
                        cursor = token.end;
                    } else {
                        throw new IllegalArgumentException(String.format(
                                "Unexpected char at %d instead of variables separator '%s': '%s'",
                                cursor,
                                ItemsSeparator.VARIABLE.getSymbol(),
                                string.charAt(cursor)
                        ));
                    }
                }
                if (parsedVariableValues.containsKey(variable)) {
                    throw new IllegalArgumentException("Variables with same name");
                }
                parsedVariableValues.put(variable, values);
            } else {
                throw new IllegalArgumentException(String.format(
                        "Unexpected char at %d instead of variable: '%s'",
                        cursor,
                        string.charAt(cursor)
                ));
            }
        }
        return parsedVariableValues;
    }

}
