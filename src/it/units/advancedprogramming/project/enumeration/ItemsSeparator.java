package it.units.advancedprogramming.project.enumeration;

public enum ItemsSeparator {
    CONTENT(";"),
    COMPUTATION("_"),
    VALUES(":"),
    VARIABLE(",");

    private final String symbol;

    ItemsSeparator(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

}
