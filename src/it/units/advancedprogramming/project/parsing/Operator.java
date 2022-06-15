package it.units.advancedprogramming.project.parsing;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Operator extends AbstractNode {

    public enum Type {
        SUM('+'),
        SUBTRACTION('-'),
        MULTIPLICATION('*'),
        DIVISION('/'),
        POWER('^');
        private final char symbol;

        Type(char symbol) {
            this.symbol = symbol;
        }

        public char getSymbol() {
            return symbol;
        }
    }

    private final Type type;

    public Operator(Type type, List<AbstractNode> children) {
        super(children);
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Operator operator = (Operator) o;
        return type == operator.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }

    @Override
    public String toString() {
        return "(" +
                getChildren().stream()
                        .map(AbstractNode::toString)
                        .collect(Collectors.joining(" " + type.symbol + " ")) +
                ")";
    }

}
