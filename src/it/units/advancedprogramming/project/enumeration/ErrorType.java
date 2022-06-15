package it.units.advancedprogramming.project.enumeration;

public enum ErrorType {
    INPUT("(InputException)"),
    COMPUTATION("(ComputationException)"),
    MEMORY("(MemoryException)");

    private final String message;

    ErrorType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
