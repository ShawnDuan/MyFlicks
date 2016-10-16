package com.example.sduan.myflicks;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sduan on 10/16/16.
 */

public class Utils {
    public static String dateToString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String result = dateFormat.format(date);
        return result;
    }
}
