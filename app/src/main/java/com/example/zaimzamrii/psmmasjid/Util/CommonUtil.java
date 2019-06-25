package com.example.zaimzamrii.psmmasjid.Util;

import java.text.SimpleDateFormat;

public class CommonUtil {
    public String changeDateFormat(String dateString, String currentDateFormat, String changeToFormat) {
        try {
            if (dateString != null && !"".equalsIgnoreCase(dateString)) {
                if (((dateString.length()) == 10) && (currentDateFormat.length() == 8)
                        && dateString.substring(4, 5).equalsIgnoreCase("/") && (currentDateFormat.indexOf("/") != 0)) {
                    currentDateFormat = "yyyy/MM/dd";
                } else if (((dateString.length()) == 10) && (currentDateFormat.length() == 8)
                        && dateString.substring(4, 5).equalsIgnoreCase("-") && (currentDateFormat.indexOf("-") != 0)) {
                    currentDateFormat = "yyyy-MM-dd";
                } else if (((dateString.length()) == 8) && (currentDateFormat.length() == 10)
                        && (currentDateFormat.substring(0, 4).equalsIgnoreCase("yyyy"))
                        && dateString.substring(2, 3).equalsIgnoreCase("-") && (currentDateFormat.indexOf("-") != 0)) {
                    currentDateFormat = "yy-MM-dd";
                } else if (((dateString.length()) == 8) && (currentDateFormat.length() == 10)
                        && (currentDateFormat.substring(0, 2).equalsIgnoreCase("dd"))
                        && dateString.substring(2, 3).equalsIgnoreCase("-") && (currentDateFormat.indexOf("-") != 0)) {
                    currentDateFormat = "dd-MM-yy";
                } else if (((dateString.length()) == 8) && (currentDateFormat.length() == 10)
                        && (currentDateFormat.substring(0, 4).equalsIgnoreCase("yyyy"))
                        && dateString.substring(2, 3).equalsIgnoreCase("/") && (currentDateFormat.indexOf("/") != 0)) {
                    currentDateFormat = "yy/MM/dd";
                } else if (((dateString.length()) == 8) && (currentDateFormat.length() == 10)
                        && (currentDateFormat.substring(0, 2).equalsIgnoreCase("dd"))
                        && dateString.substring(2, 3).equalsIgnoreCase("/") && (currentDateFormat.indexOf("/") != 0)) {
                    currentDateFormat = "dd/MM/yy";
                } else if (((dateString.length()) == 10) && (currentDateFormat.length() == 10)
                        && (currentDateFormat.substring(0, 2).equalsIgnoreCase("dd"))
                        && dateString.substring(2, 3).equalsIgnoreCase("/") && (currentDateFormat.indexOf("/") != 0)) {
                    currentDateFormat = "dd/MM/yyyy";
                } else if (((dateString.length()) == 10) && (currentDateFormat.length() == 10)
                        && (currentDateFormat.substring(0, 2).equalsIgnoreCase("dd"))
                        && dateString.substring(2, 3).equalsIgnoreCase("-") && (currentDateFormat.indexOf("-") != 0)) {
                    currentDateFormat = "dd-MM-yyyy";
                } else if (((dateString.length()) == 10) && (currentDateFormat.length() == 10)
                        && (currentDateFormat.substring(0, 4).equalsIgnoreCase("yyyy"))
                        && dateString.substring(4, 5).equalsIgnoreCase("/") && (currentDateFormat.indexOf("/") != 0)) {
                    currentDateFormat = "yyyy/MM/dd";
                } else if (((dateString.length()) == 10) && (currentDateFormat.length() == 10)
                        && (currentDateFormat.substring(0, 2).equalsIgnoreCase("yyyy"))
                        && dateString.substring(4, 5).equalsIgnoreCase("-") && (currentDateFormat.indexOf("-") != 0)) {
                    currentDateFormat = "yyyy-MM-dd";

                } else if (((dateString.length()) == 8) && (currentDateFormat.length() == 8)
                        && (currentDateFormat.substring(0, 2).equalsIgnoreCase("dd"))
                        && dateString.substring(2, 3).equalsIgnoreCase("/") && (currentDateFormat.indexOf("/") != 0)) {
                    currentDateFormat = "dd/MM/yy";
                } else if (((dateString.length()) == 8) && (currentDateFormat.length() == 8)
                        && (currentDateFormat.substring(0, 2).equalsIgnoreCase("dd"))
                        && dateString.substring(2, 3).equalsIgnoreCase("-") && (currentDateFormat.indexOf("-") != 0)) {
                    currentDateFormat = "dd-MM-yy";
                } else if (((dateString.length()) == 8) && (currentDateFormat.length() == 8)
                        && (currentDateFormat.substring(0, 2).equalsIgnoreCase("yy"))
                        && dateString.substring(2, 3).equalsIgnoreCase("/") && (currentDateFormat.indexOf("/") != 0)) {
                    currentDateFormat = "yy/MM/dd";
                } else if (((dateString.length()) == 8) && (currentDateFormat.length() == 8)
                        && (currentDateFormat.substring(0, 2).equalsIgnoreCase("yyyy"))
                        && dateString.substring(2, 3).equalsIgnoreCase("-") && (currentDateFormat.indexOf("-") != 0)) {
                    currentDateFormat = "yy-MM-dd";
                }
                SimpleDateFormat sdf1 = new SimpleDateFormat(currentDateFormat);
                SimpleDateFormat sdf2 = new SimpleDateFormat(changeToFormat);
                return sdf2.format(sdf1.parse(dateString));
            } else {
                return "";
            }
        } catch (java.text.ParseException pe) {
            return dateString;
        }
    }
}
