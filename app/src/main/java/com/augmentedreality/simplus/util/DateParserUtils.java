package com.augmentedreality.simplus.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateParserUtils {

    private DateParserUtils() { }

    private static final Locale LOCALE = Locale.getDefault();

    public static String toDisplay(String displayFormat, String date) {
        try {
            SimpleDateFormat defaultParseFormat =
                new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'", LOCALE);
            return new SimpleDateFormat(displayFormat, LOCALE).format(
                defaultParseFormat.parse(date));
        } catch (ParseException e) {
            return "";
        }
    }

    public static String toDisplay(String displayFormat, Date date) {
        return new SimpleDateFormat(displayFormat, LOCALE).format(date);
    }

    public static String toDisplay(String parseFormat, String displayFormat, String date) {
        try {
            return new SimpleDateFormat(displayFormat, LOCALE)
                       .format(new SimpleDateFormat(parseFormat, LOCALE).parse(date));
        } catch (ParseException e) {
            return "";
        }
    }
}
