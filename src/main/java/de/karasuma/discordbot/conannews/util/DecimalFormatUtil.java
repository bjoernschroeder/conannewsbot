package de.karasuma.discordbot.conannews.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class DecimalFormatUtil {

    DecimalFormat decimalFormat;

    public DecimalFormat getDecimalFormatter() {
        if (decimalFormat == null) {
            decimalFormat = new DecimalFormat();
            DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(Locale.GERMAN);
            decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
        }
        return decimalFormat;
    }

}
