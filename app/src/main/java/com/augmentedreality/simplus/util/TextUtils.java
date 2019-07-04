package com.augmentedreality.simplus.util;


import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Locale;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import static java.lang.String.valueOf;

public class TextUtils {

    private TextUtils() { }

    public static String toDoubleDecimalPlaces(double number) {
        try {
            DecimalFormat decimalFormat = new DecimalFormat("###,###,##0.00");
            decimalFormat.setRoundingMode(RoundingMode.DOWN);
            return decimalFormat.format(number);
        } catch (IllegalArgumentException e) {
            return "#.##";
        }
    }

    public static String toDoubleDigitText(int number) {
        if (number < 10) {
            return "0" + number;
        }
        return "" + number;
    }

    public static String toLowerCase(String text) {
        return text.toLowerCase(Locale.getDefault());
    }

    @NonNull
    public static String toNonNull(@Nullable String string) {
        if (string == null) return "";
        return string;
    }

    public static String toNullIfEmpty(String string) {
        try {
            if (string.isEmpty()) return null;
            return string;
        } catch (NullPointerException e) {
            return null;
        }
    }

    public static boolean isNotNullOrEmpty(String string) {
        return string != null && !string.isEmpty();
    }

    public static String titleCase(String text) {
        if (text.length() > 1) {
            return text.substring(0, 1)
                       .toUpperCase(Locale.getDefault())
                       + text.substring(1, text.length()).toLowerCase(Locale.getDefault());
        } else if (text.length() == 1) {
            return text.toUpperCase(Locale.getDefault());
        } else {
            return text;
        }
    }

    public static String initials(String text) {
        if (null != text && !text.isEmpty()) {
            String[] parsedText = text.toUpperCase(Locale.getDefault()).split(" ");
            if (parsedText.length == 1) {
                return valueOf(parsedText[0].charAt(0));
            } else if (parsedText.length > 1) {
                return valueOf(parsedText[0].charAt(0)) + " " + valueOf(parsedText[1].charAt(0));
            } else {
                return "";
            }
        } else {
            return "";
        }
    }

}
