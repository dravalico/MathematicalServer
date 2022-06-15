package it.units.advancedprogramming.project.util;

import java.util.logging.Logger;

public class LoggerUtils {
    public static final Logger LOGGER;

    static {
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tF %1$tT] %4$-7s %5$s %n");
        LOGGER = Logger.getGlobal();
    }

}
