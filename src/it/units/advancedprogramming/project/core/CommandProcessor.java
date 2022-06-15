package it.units.advancedprogramming.project.core;

@FunctionalInterface
public interface CommandProcessor {
    String process(String input);
}
