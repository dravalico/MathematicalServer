package it.units.advancedprogramming.project.entity;

import it.units.advancedprogramming.project.enumeration.ItemsSeparator;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public final class ResponseBuilder {
    private static final int MAXIMUM_FRACTION_DIGITS_TIME = 3;
    private static final int MAXIMUM_FRACTION_DIGITS_RESULT = 6;

    private enum Type {
        OK, ERR
    }

    public ResponseBuilder() {
    }

    public static String buildOkResponse(double time, double result) {
        return Type.OK
                + ItemsSeparator.CONTENT.getSymbol()
                + format(time, MAXIMUM_FRACTION_DIGITS_TIME)
                + ItemsSeparator.CONTENT.getSymbol()
                + format(result, MAXIMUM_FRACTION_DIGITS_RESULT);
    }

    public static String buildErrorResponse(String message) {
        return Type.ERR + ItemsSeparator.CONTENT.getSymbol() + message;
    }

    private static String format(double d, int maximumFractionDigits) {
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("0.0000000", decimalFormatSymbols);
        decimalFormat.setMaximumFractionDigits(maximumFractionDigits);
        decimalFormat.setRoundingMode(RoundingMode.DOWN);
        return decimalFormat.format(d);
    }

}
