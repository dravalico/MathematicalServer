package it.units.advancedprogramming.project.parsing;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ExpressionParser extends AbstractTokenParser<AbstractNode> {
    private final String string;
    private int cursor;

    public ExpressionParser(String string) {
        super();
        this.string = string.replace(" ", "");
        cursor = 0;
    }

    @Override
    public AbstractNode parse() throws IllegalArgumentException {
        Token token;

        token = TokenType.CONSTANT.next(string, cursor);
        if (token != null && token.start == cursor) {
            cursor = token.end;
            return new Constant(Double.parseDouble(string.substring(token.start, token.end)));
        }

        token = TokenType.VARIABLE.next(string, cursor);
        if (token != null && token.start == cursor) {
            cursor = token.end;
            return new Variable(string.substring(token.start, token.end));
        }

        token = TokenType.OPEN_BRACKET.next(string, cursor);
        if (token != null && token.start == cursor) {
            cursor = token.end;
            AbstractNode child1 = parse();
            Token operatorToken = TokenType.OPERATOR.next(string, cursor);
            if (operatorToken != null && operatorToken.start == cursor) {
                cursor = operatorToken.end;
            } else {
                throw new IllegalArgumentException(String.format(
                        "Unexpected char at %d instead of operator: '%s'",
                        cursor,
                        string.charAt(cursor)
                ));
            }
            AbstractNode child2 = parse();
            Token closedBracketToken = TokenType.CLOSED_BRACKET.next(string, cursor);
            if (closedBracketToken != null && closedBracketToken.start == cursor) {
                cursor = closedBracketToken.end;
            } else {
                throw new IllegalArgumentException(String.format(
                        "Unexpected char at %d instead of closed bracket: '%s'",
                        cursor,
                        string.charAt(cursor)
                ));
            }
            Operator.Type operatorType = null;
            String operatorString = string.substring(operatorToken.start, operatorToken.end);
            for (Operator.Type type : Operator.Type.values()) {
                if (operatorString.equals(Character.toString(type.getSymbol()))) {
                    operatorType = type;
                    break;
                }
            }
            if (operatorType == null) {
                throw new IllegalArgumentException(String.format(
                        "Unknown operator at %d: '%s'",
                        operatorToken.start,
                        operatorString
                ));
            }
            return new Operator(operatorType, Arrays.asList(child1, child2));
        }

        throw new IllegalArgumentException(String.format(
                "Unexpected char at %d: '%s'",
                cursor,
                string.charAt(cursor)
        ));
    }

    public Set<String> getVariablesFromExpression() {
        Set<String> variables = new HashSet<>();
        Token token;
        while (cursor < string.length()) {
            token = TokenType.VARIABLE.next(string, cursor);
            if (token != null && token.start == cursor) {
                cursor = token.end;
                variables.add(new Variable(string.substring(token.start, token.end)).toString());
            }
            token = TokenType.CONSTANT.next(string, cursor);
            if (token != null && token.start == cursor) {
                cursor = token.end;
            }
            token = TokenType.OPERATOR.next(string, cursor);
            if (token != null && token.start == cursor) {
                cursor = token.end;
            }
            token = TokenType.OPEN_BRACKET.next(string, cursor);
            if (token != null && token.start == cursor) {
                cursor = token.end;
            }
            token = TokenType.CLOSED_BRACKET.next(string, cursor);
            if (token != null && token.start == cursor) {
                cursor = token.end;
            }
        }
        return variables;
    }

}
