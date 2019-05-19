package com.augmentedreality.simplus.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


import static com.augmentedreality.simplus.util.DateParserUtils.toDisplay;
import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.DAY_OF_WEEK;
import static java.util.Calendar.DECEMBER;
import static java.util.Calendar.FRIDAY;
import static java.util.Calendar.HOUR;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MILLISECOND;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.MONDAY;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.SATURDAY;
import static java.util.Calendar.SECOND;
import static java.util.Calendar.SUNDAY;
import static java.util.Calendar.THURSDAY;
import static java.util.Calendar.TUESDAY;
import static java.util.Calendar.WEDNESDAY;
import static java.util.Calendar.WEEK_OF_YEAR;
import static java.util.Calendar.YEAR;

public class TimeUtils {

    private static final String TIMEZONE = "XXX";
    private static final String HOUR_MINUTE_FORMAT = "HH:mm:ss";
    private static final String ZERO_HOUR_MINUTE_FORMAT = "00:00:00";
    private static final String ISO_8601_DATE_TIME_FORMAT =
        "yyyy-MM-dd'T'" + HOUR_MINUTE_FORMAT + "Z";
    private static final String ISO_8601_FORMAT_WITH_MILLISECONDS =
        "yyyy-MM-dd'T'" + HOUR_MINUTE_FORMAT + ".SSSZ";
    private static final String ISO_8601_DATE_TIME_FORMAT_WITHOUT_TIMEZONE =
        "yyyy-MM-dd'T'" + HOUR_MINUTE_FORMAT;

    private static final String GENERIC_DATE_FORMAT =
        "dd/MM/yyyy h:mm a";

    private static final String GENERIC_DATE_FORMAT2 = "dd-MMM-yyyy h:mm a";

    private static final String GENERIC_DATE_FORMAT_WITHOUT_HOUR_MINUTE =
        "dd/MM/yyyy";

    private static final String COMMON_HOURS_MINUTES_FORMAT = "h:mm a";

    private static final String FORMAL_DATE_FORMAT = "dd MMM yyyy";

    private static final String MONTH_DATE_FORMAT = "dd MMM";


    /**
     * This class must not be instantiated
     */
    private TimeUtils() {}

    public static long daysBetween(long start, long end) {
        if (end < start) return 0;
        return (end - start) / (1000 * 60 * 60 * 24);
    }

    public static long hoursBetween(long start, long end) {
        if (end < start) return 0;
        return (end - start) / (1000 * 60 * 60);
    }

    public static long minutesBetween(long start, long end) {
        if (end < start) return 0;
        return (end - start) / (1000 * 60);
    }

    public static long secondsBetween(long start, long end) {
        if (end < start) return 0;
        return (end - start) / (1000);
    }

    /**
     * @return the given dateTime in <code>dd MMM yyyy</code> format
     */
    public static String toCommonDateFormat(long dateTime) {
        return new SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                   .format(dateTime);
    }

    /**
     * @return the given dateTime in MMM, dd yyyy
     */
    public static String toDate(long dateTime) {
        return new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                   .format(dateTime);
    }

    public static String todayIso8601WithoutTimezone() {
        return format(System.currentTimeMillis(), "yyyy-MM-dd'T'HH:mm:ss.SSS") + "Z";
    }

    /**
     * @return the given dateTime in MMM dd, yyyy HH:MM A
     */
    public static String toDateTime(long dateTime) {
        return new SimpleDateFormat("MMM dd, yyyy HH:mm a", Locale.getDefault())
                   .format(dateTime);
    }

    /**
     * @return Hour:Minute:Seconds where Hour is in 24hour format (0-23)
     */
    static String toHourMinuteSecond(long date) {
        return new SimpleDateFormat(HOUR_MINUTE_FORMAT, Locale.getDefault()).format(date);
    }

    /**
     * @return e.g., 12:00 PM
     */
    public static String to12HourFormat(int hour, int minute) {
        String amPm;
        if (hour > 12) {
            hour -= 12;
            amPm = "PM";
        } else {
            amPm = "AM";
        }
        String formatted = hour < 10 ? "0" + hour : "" + hour;
        formatted += ":";
        formatted += minute < 10 ? "0" + minute : minute;
        formatted += " " + amPm;
        return formatted;
    }

    public static String to24HourFormat(int hour, int minute) {
        String formatted = hour < 10 ? "0" + hour : "" + hour;
        formatted += ":";
        formatted += minute < 10 ? "0" + minute : minute;
        return formatted;
    }

    public static long toMillis(int year, int month, int dayOfMonth) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.set(YEAR, year);
        calendar.set(MONTH, month);
        calendar.set(DAY_OF_MONTH, dayOfMonth);
        calendar.set(HOUR, 0);
        calendar.set(MINUTE, 0);
        calendar.set(SECOND, 0);
        calendar.set(MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * @return the date and time formatted in YYYY-MM-DD[T]HH:mm:ss[Z]
     */
    public static String toIso8601(long dateTime) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(dateTime);

        SimpleDateFormat simpleDateFormat =
            new SimpleDateFormat(ISO_8601_DATE_TIME_FORMAT, Locale.getDefault());
        return simpleDateFormat.format(calendar.getTime());
    }

    /**
     * @return the date and time formatted in YYYY-MM-DD[T]HH:mm:ss.SSS[Z]
     */
    public static String toIso8601WithMilliseconds(long dateTime) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(dateTime);

        SimpleDateFormat simpleDateFormat =
            new SimpleDateFormat(ISO_8601_FORMAT_WITH_MILLISECONDS, Locale.getDefault());
        return simpleDateFormat.format(calendar.getTime());
    }

    /**
     * @return the date and time formatted in YYYY-MM-DD[T]HH:mm:ss.SSS[Z]
     */
    public static String toIso8601StartOrEndDate(long dateTime, boolean isStartDate) {
        GregorianCalendar calendar = new GregorianCalendar();
        long date;
        if (isStartDate) {
            date = removeHoursMinuteSecondsMilliseconds(dateTime);
        } else {
            date = appendDefaultHoursMinuteSecondsMilliseconds(dateTime);
        }
        calendar.setTimeInMillis(date);

        SimpleDateFormat simpleDateFormat =
            new SimpleDateFormat(ISO_8601_FORMAT_WITH_MILLISECONDS, Locale.getDefault());
        return simpleDateFormat.format(calendar.getTime());
    }

    /**
     * @param dateTime the date and time formatted in YYYY-MM-DD[T]HH:mm:ss[Z]
     */
    public static long fromIso8601(String dateTime) {
        try {
            SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat(ISO_8601_DATE_TIME_FORMAT, Locale.getDefault());
            return simpleDateFormat.parse(dateTime)
                                   .getTime();
        } catch (ParseException e) {
            return fromIso8601WithMilliseconds(dateTime);
        }
    }

    /**
     * @param dateTime the date and time formatted in YYYY-MM-DD[T]HH:mm:ss.SSS[Z]
     */
    public static long fromIso8601WithMilliseconds(String dateTime) {
        // A dateTime format is appened with "Z" if the timezone is UTC
        if (dateTime.endsWith("Z"))
            dateTime = dateTime.substring(0, dateTime.length() - 1) + "+0000";

        try {
            SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat(ISO_8601_FORMAT_WITH_MILLISECONDS, Locale.getDefault());
            return simpleDateFormat.parse(dateTime)
                                   .getTime();
        } catch (ParseException pe) {
            return 0;
        }
    }

    /**
     * Translates a {@code double} number into an {@code NNh NNm} format text.
     * For example, 8.5 will be translated to {@code 8h 30m}
     */
    public static String toHoursMinutes(double duration) {
        if (minutesFrom(duration).equals("")) {
            return hoursFrom(duration) + "h";
        } else {
            return hoursFrom(duration) + "h " + minutesFrom(duration) + "m";
        }
    }

    /**
     * Translates the hours from a double totalDuration. See the tests for values and usages.
     */
    public static String hoursFrom(double duration) {
        return ((duration < 10) ? "0" : "") + (int) duration;
    }

    /**
     * Translates the minutes from a double duration. See the tests for values and usages.
     *
     * @see #toHoursMinutes(double)
     */
    public static String minutesFrom(double duration) {
        if (duration % 1 == 0) return "";
        if (duration > 1) duration -= (long) duration;
        int minutes = (int) Math.round(60d * duration);
        if (minutes < 10) return "0" + minutes;
        return Integer.toString(minutes);
    }

    /**
     * @return (e.g., March 2017)
     */
    static String toMonthYear(long dateTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        return simpleDateFormat.format(dateTime);
    }

    /**
     * @return removes all other units (day, hour, minute, seconds) but the month and year of the
     * given dateTime
     */
    public static long toMonthYearOnly(long dateTime) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(dateTime);
        calendar.set(DAY_OF_MONTH, 1);
        calendar.set(MINUTE, 0);
        calendar.set(HOUR, 0);
        calendar.set(HOUR_OF_DAY, 0);
        calendar.set(SECOND, 0);
        calendar.set(MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * @return dateTime in MM yyyy format (e.g., Jan 2017)
     */
    public static String toShortMonthYear(long dateTime) {
        return format(dateTime, "MMM yyyy");
    }

    /**
     * @return the date in yyyy, MMMM dd
     */
    static String toYearMonthDay(long dateTime) {
        return new SimpleDateFormat("yyyy MMMM dd", Locale.getDefault()).format(dateTime);
    }

    public static long removeHoursMinuteSecondsMilliseconds(long dateTime) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTimeInMillis(dateTime);
        gregorianCalendar.set(HOUR_OF_DAY, 0);
        gregorianCalendar.set(MINUTE, 0);
        gregorianCalendar.set(SECOND, 0);
        gregorianCalendar.set(MILLISECOND, 0);
        return gregorianCalendar.getTimeInMillis();
    }

    private static long appendDefaultHoursMinuteSecondsMilliseconds(long dateTime) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTimeInMillis(dateTime);
        gregorianCalendar.set(HOUR_OF_DAY, 23);
        gregorianCalendar.set(MINUTE, 59);
        gregorianCalendar.set(SECOND, 59);
        gregorianCalendar.set(MILLISECOND, 0);
        return gregorianCalendar.getTimeInMillis();
    }

    static long lastTimestampOfDate(long dateTime) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTimeInMillis(dateTime);
        gregorianCalendar.set(HOUR_OF_DAY, 23);
        gregorianCalendar.set(MINUTE, 59);
        gregorianCalendar.set(SECOND, 59);
        gregorianCalendar.set(MILLISECOND, 59);
        return gregorianCalendar.getTimeInMillis();
    }

    static boolean sameDay(long date1, long date2) {
        GregorianCalendar first = new GregorianCalendar();
        first.setTimeInMillis(date1);

        GregorianCalendar second = new GregorianCalendar();
        second.setTimeInMillis(date2);

        return first.get(DAY_OF_MONTH) == second.get(DAY_OF_MONTH)
                   && first.get(MONTH) == second.get(MONTH)
                   && first.get(YEAR) == second.get(YEAR);
    }

    public static int yearOf(long dateTime) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(dateTime);
        return calendar.get(YEAR);
    }

    public static int dayOf(long dateTime) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(dateTime);
        return calendar.get(DAY_OF_MONTH);
    }

    public static int monthOf(long dateTime) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(dateTime);
        return calendar.get(MONTH);
    }

    public static String dayOfWeek(long dateTime) {
        String weekDay;
        SimpleDateFormat dayFormat = new SimpleDateFormat("E", Locale.getDefault());

        weekDay = dayFormat.format(dateTime);
        return weekDay;
    }


    public static double toDuration(String hours, String minutes) {
        double duration;
        if (hours.isEmpty()) {
            duration = 0;
        } else {
            duration = Double.parseDouble(hours);
        }
        double minutesNumber;
        if (minutes.isEmpty()) {
            minutesNumber = 0;
        } else {
            minutesNumber = Double.parseDouble(minutes);
        }
        duration += minutesNumber / 60;
        return duration;
    }

    public static String format(long dateTime, String format) {
        return new SimpleDateFormat(format, Locale.getDefault()).format(dateTime);
    }

    /**
     * @return December 31 of the current year, with the time being 23:59:59.000
     */
    public static long lastTimeOfYear() {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.set(MONTH, DECEMBER);
        calendar.set(DAY_OF_MONTH, 31);
        calendar.set(HOUR, 23);
        calendar.set(MINUTE, 59);
        calendar.set(SECOND, 59);
        calendar.set(MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * @return returns the date-time in milliseconds
     * (stripped through {@link #removeHoursMinuteSecondsMilliseconds(long)}) of the Sunday of the
     * dateTime's week. For example, the sunday of October 12 2017 is October 8. As such, the
     * milliseconds equivalent of October 8 2017 will be returned when the dateTime is of
     * October 12, 2017.
     */
    public static long sundayOf(long dateTime) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(removeHoursMinuteSecondsMilliseconds(dateTime));

        long day = TimeUnit.DAYS.toMillis(1);

        switch (calendar.get(DAY_OF_WEEK)) {
            case MONDAY:
                calendar.setTimeInMillis(calendar.getTimeInMillis() - day);
                break;
            case TUESDAY:
                calendar.setTimeInMillis(calendar.getTimeInMillis() - day * 2);
                break;
            case WEDNESDAY:
                calendar.setTimeInMillis(calendar.getTimeInMillis() - day * 3);
                break;
            case THURSDAY:
                calendar.setTimeInMillis(calendar.getTimeInMillis() - day * 4);
                break;
            case FRIDAY:
                calendar.setTimeInMillis(calendar.getTimeInMillis() - day * 5);
                break;
            case SATURDAY:
                calendar.setTimeInMillis(calendar.getTimeInMillis() - day * 6);
                break;
            default:
        }

        return calendar.getTimeInMillis();
    }

    /**
     * @return returns the date-time in milliseconds
     * (stripped through {@link #removeHoursMinuteSecondsMilliseconds(long)}) of the Sunday of the
     * dateTime's week. For example, the sunday of October 12 2017 is October 8. As such, the
     * milliseconds equivalent of October 8 2017 will be returned when the dateTime is of
     * October 12, 2017.
     */
    public static long saturdayOf(long dateTime) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(removeHoursMinuteSecondsMilliseconds(dateTime));

        long day = TimeUnit.DAYS.toMillis(1);

        switch (calendar.get(DAY_OF_WEEK)) {
            case MONDAY:
                calendar.setTimeInMillis(calendar.getTimeInMillis() + day * 5);
                break;
            case TUESDAY:
                calendar.setTimeInMillis(calendar.getTimeInMillis() + day * 4);
                break;
            case WEDNESDAY:
                calendar.setTimeInMillis(calendar.getTimeInMillis() + day * 3);
                break;
            case THURSDAY:
                calendar.setTimeInMillis(calendar.getTimeInMillis() + day * 2);
                break;
            case FRIDAY:
                calendar.setTimeInMillis(calendar.getTimeInMillis() + day);
                break;
            case SUNDAY:
                calendar.setTimeInMillis(calendar.getTimeInMillis() + day * 6);
                break;
            default:
        }

        return calendar.getTimeInMillis();
    }

    public static String weekRange(int weekNumber) {
        String dateFormat = "dd MMM";

        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(SUNDAY);
        calendar.set(WEEK_OF_YEAR, weekNumber);

        calendar.set(DAY_OF_WEEK, SUNDAY);
        Date startDate = calendar.getTime();

        calendar.set(DAY_OF_WEEK, SATURDAY);
        Date endDate = calendar.getTime();
        return toDisplay(dateFormat, startDate) + " - " + toDisplay(dateFormat, endDate);
    }

    /**
     * @return date in milliseconds without time.
     */
    public static long date(int month, int dayOfMonth, int year) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.set(YEAR, year);
        calendar.set(MONTH, month);
        calendar.set(DAY_OF_MONTH, dayOfMonth);
        calendar.set(HOUR_OF_DAY, 0);
        calendar.set(MINUTE, 0);
        calendar.set(SECOND, 0);
        calendar.set(MILLISECOND, 0);

        return calendar.getTimeInMillis();
    }

    public static String toTimezone(long dateTime) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(dateTime);

        SimpleDateFormat simpleDateFormat =
            new SimpleDateFormat(TIMEZONE, Locale.getDefault());
        return simpleDateFormat.format(calendar.getTime());
    }


    public static String getDurationTime(Long durationInMillis) {
        long second = 1000;
        long minute = second * 60;
        long hour = minute * 60;
        String ss = "00";
        String mm = "00";
        String hh = "00";

        if (durationInMillis >= hour) {
            int durationHours = (int) (durationInMillis / hour);
            durationInMillis -= (durationHours * hour);
            hh = String.format("%02d", durationHours);
        }

        if (durationInMillis >= minute) {
            int durationMinutes = (int) (durationInMillis / minute);
            durationInMillis -= (durationMinutes * minute);
            mm = String.format("%02d", durationMinutes);
        }

        if (durationInMillis >= second) {
            int durationSeconds = (int) (durationInMillis / second);
            ss = String.format("%02d", durationSeconds);
        }

        return hh + ":" + mm + ":" + ss;
    }

    public static String convertFormat(String value, String originalPattern, String newPattern) {
        String result = "";
        DateFormat originalFormat = new SimpleDateFormat(originalPattern, Locale.getDefault());
        DateFormat newFormat = new SimpleDateFormat(newPattern, Locale.getDefault());

        try {
            result = newFormat.format(originalFormat.parse(value));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String formatCurrentDateTime() {
        String result = "";
        DateFormat iFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault());
        DateFormat oFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        try {
            result = oFormatter.format(iFormatter.parse(TimeUtils.toIso8601(new Date().getTime())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static long computeDuration(String start, String finish, String pattern) {
        Date formattedStart = new Date();
        Date formattedFinish = new Date();

        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.getDefault());
        try {
            formattedStart = format.parse(start);
            formattedFinish = format.parse(finish);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long diffInMs = formattedFinish.getTime() - formattedStart.getTime();
        return TimeUnit.MILLISECONDS.toSeconds(diffInMs);
    }

    public static String toIso8601WithZeroMinutes(long dateTime) {
        final String dateFormat =
            "yyyy-MM-dd'T'" + ZERO_HOUR_MINUTE_FORMAT;
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(dateTime);

        SimpleDateFormat simpleDateFormat =
            new SimpleDateFormat(dateFormat, Locale.getDefault());
        return simpleDateFormat.format(calendar.getTime());
    }

    public static long fromIso8601WithZeroMinutes(String dateTime) {
        try {
            SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("yyyy-MM-dd'T'" + ZERO_HOUR_MINUTE_FORMAT, Locale.getDefault());
            return simpleDateFormat.parse(dateTime)
                                   .getTime();
        } catch (ParseException e) {
            return fromIso8601WithMilliseconds(dateTime);
        }
    }

    public static long fromIso8601WithoutTimezone(String dateTime) {
        try {
            SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat(ISO_8601_DATE_TIME_FORMAT_WITHOUT_TIMEZONE, Locale.getDefault());
            return simpleDateFormat.parse(dateTime)
                                   .getTime();
        } catch (ParseException e) {
            return fromIso8601WithMilliseconds(dateTime);
        }
    }

    public static String toGenericDateFormat(long dateTime) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(dateTime);

        SimpleDateFormat simpleDateFormat =
            new SimpleDateFormat(GENERIC_DATE_FORMAT, Locale.getDefault());
        return simpleDateFormat.format(calendar.getTime());
    }

    public static String toGenericDateFormat2(long dateTime) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(dateTime);

        SimpleDateFormat simpleDateFormat =
            new SimpleDateFormat(GENERIC_DATE_FORMAT2, Locale.getDefault());
        return simpleDateFormat.format(calendar.getTime());
    }

    public static String toGenericDateFormatWithoutHourMinute(long dateTime) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(dateTime);

        SimpleDateFormat simpleDateFormat =
            new SimpleDateFormat(GENERIC_DATE_FORMAT_WITHOUT_HOUR_MINUTE, Locale.getDefault());
        return simpleDateFormat.format(calendar.getTime());
    }

    public static String toMonthDateFormat(long dateTime) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(dateTime);

        SimpleDateFormat simpleDateFormat =
            new SimpleDateFormat(MONTH_DATE_FORMAT, Locale.getDefault());
        return simpleDateFormat.format(calendar.getTime());
    }

    public static String toHoursMinutes(long dateTime) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(dateTime);

        SimpleDateFormat simpleDateFormat =
            new SimpleDateFormat(COMMON_HOURS_MINUTES_FORMAT, Locale.getDefault());
        return simpleDateFormat.format(calendar.getTime());
    }


    public static String getHoursMinutesDuration(long seconds) {
        if (seconds > 0) {
            long hours = seconds / 3600; //since both are ints, you get an int
            long minutes = (seconds % 3600) / 60;

            return String.format(Locale.getDefault(), "%d:%02d", hours, minutes);
        }
        return "00:00";
    }

    public static String toIso8601WithoutTimeZone(long dateTime) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(dateTime);

        SimpleDateFormat simpleDateFormat =
            new SimpleDateFormat(ISO_8601_DATE_TIME_FORMAT_WITHOUT_TIMEZONE, Locale.getDefault());
        return simpleDateFormat.format(calendar.getTime());
    }

    public static String toFormalDateFormat(long dateTime) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(dateTime);

        SimpleDateFormat simpleDateFormat =
            new SimpleDateFormat(FORMAL_DATE_FORMAT, Locale.getDefault());
        return simpleDateFormat.format(calendar.getTime());
    }

    public static String getDate1WeekAfter(long date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(date));
        calendar.add(Calendar.DATE, 6);
        return TimeUtils.toIso8601WithZeroMinutes(calendar.getTime()
                                           .getTime());
    }
}
