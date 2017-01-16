package com.samoryka.verecipesclient.Utilities;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class contains methods currently used to fromat objects to strings
 */
public class StringFormatUtility {

    public static String DateToYYYYMMDD(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }
}
