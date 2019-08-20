package com.puskesmascilandak.e_jiwa.util;

import java.util.Calendar;

public class CalendarHelper {
    public static String getDateInString(String splitter) {
        return getDateInString(today(), splitter);
    }

    public static String getDefaultDateInString() {
        return getDateInString("/");
    }

    public static String getDateInString(Calendar from, String splitter) {
        Calendar calendar = from;

        if (calendar == null) calendar = today();

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        return day + splitter + (month + 1) + splitter + year;
    }

    public static String getDefaultDateInString(Calendar from) {
        return getDateInString(from, "/");
    }

    public static Calendar today() {
        return Calendar.getInstance();
    }
}
