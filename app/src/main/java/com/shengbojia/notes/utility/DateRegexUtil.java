package com.shengbojia.notes.utility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class DateRegexUtil {

    public static DateFormat getLongDateInstanceWithoutYears() {
        try {
            SimpleDateFormat sdf = (SimpleDateFormat) DateFormat.getDateInstance(DateFormat.LONG);
            sdf.applyPattern(sdf.toPattern()
                    .replaceAll(
                            "([^\\p{Alpha}']|('[\\p{Alpha}]+'))*y+([^\\p{Alpha}']|('[\\p{Alpha}]+'))*",
                            "")
                    .replaceAll(
                            "年*",
                            ""));
            return sdf;
        } catch (Exception ex) {
            return DateFormat.getDateInstance(DateFormat.LONG);
        }

    }
}
