package mcp.mobius.waila.utils;

import java.text.NumberFormat;
import java.util.Locale;

public class NumberFormatter {

    public static String format(long number) {
        return NumberFormat.getInstance(Locale.US).format(number);
    }

}
