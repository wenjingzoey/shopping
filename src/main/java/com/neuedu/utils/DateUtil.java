package com.neuedu.utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

public class DateUtil {
    private static final String STANDARD_FORMAT="yyyy-MM-dd HH:mm:ss";

    /**
     *date-str
     */
    public static String dateToStr(Date date,String formate) {
        DateTime dataTime = new DateTime(date);
        return dataTime.toString(formate);
    }
    public static String dateToStr(Date date){
        DateTime dataTime = new DateTime(date);
        return dataTime.toString(STANDARD_FORMAT);
    }
    /**
     *str-date
     */
    public static Date strToDate(String str){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(STANDARD_FORMAT);
        DateTime dateTime = dateTimeFormatter.parseDateTime(str);
        return dateTime.toDate();
    }
    public static Date strToDate(String str,String format){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(format);
        DateTime dateTime = dateTimeFormatter.parseDateTime(str);
        return dateTime.toDate();
    }

//    public static void main(String[] args) {
//        System.out.println(dateToStr(new Date()));
//        System.out.println(strToDate("2018-12-10 11:48:32"));
//    }
}
