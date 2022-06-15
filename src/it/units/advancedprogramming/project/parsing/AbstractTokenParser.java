package it.units.advancedprogramming.project.parsing;

import it.units.advancedprogramming.project.enumeration.ItemsSeparator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractTokenParser<T> {

    public AbstractTokenParser() {
    }

    protected enum TokenType {
        CONSTANT("[0-9]+(\\.[0-9]+)?"),
        ALSO_NEGATIVE_CONSTANT("\\-?[0-9]+(\\.[0-9]+)?"),
        VARIABLE("[a-z][a-z0-9]*"),
        OPERATOR("[+\\-\\*/\\^]"),
        OPEN_BRACKET("\\("),
        CLOSED_BRACKET("\\)"),
        VALUES_SEPARATOR("\\" + ItemsSeparator.VALUES.getSymbol()),
        VARIABLE_SEPARATOR("\\" + ItemsSeparator.VARIABLE.getSymbol());

        private final String regex;

        TokenType(String regex) {
            this.regex = regex;
        }

        public Token next(String s, int i) {
            Matcher matcher = Pattern.compile(regex).matcher(s);
            if (!matcher.find(i)) {
                return null;
            }
            return new Token(matcher.start(), matcher.end());
        }
    }

    protected static class Token {
        public final int start;
        public final int end;

        public Token(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }

    protected abstract T parse();

}
