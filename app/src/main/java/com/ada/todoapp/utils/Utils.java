package com.ada.todoapp.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ada on 9/29/16.
 */
public class Utils {

    public static final String FORMAT_DAY = "m/d/yy";

    public static String formatDay(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat dt = new SimpleDateFormat(FORMAT_DAY);
        return dt.format(date);
    }

}
