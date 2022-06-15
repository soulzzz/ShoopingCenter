package com.xmut.shoppingcenter.utils;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TimeUtil {
    private static TimeUtil instance;

    public static TimeUtil getInstance() {
        if (instance == null) {
            instance = new TimeUtil();
        }
        return instance;
    }
    /*
     0 for Inputing TextView , 1 for Comparing Time
     */
    public List<String> getFormatTime(){
        Date date =new Date();
        String strDateFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
        String strDateFormat2 = "yyyyMMddHHmmss";
        SimpleDateFormat sdf2 = new SimpleDateFormat(strDateFormat2);
        List<String> list = new ArrayList<>();
        list.add(sdf.format(date));
        list.add(sdf2.format(date));
        return list;
    }
    /**
     *@Description:日期转换，将接口返回的20180524转为2018-05-24
     *@param time 传递的日期字符串
     */
    public String FormatDayTime(String time) {
        Date parse = null;
        String dateString = "";
        try {
            parse = new SimpleDateFormat("yyyyMMdd").parse(time);
            dateString = new SimpleDateFormat("yyyy-MM-dd").format(parse);
        } catch (ParseException e) {
            dateString=null;
        }

        return dateString;
    }

    /**
     *@Description:日期转换，将接口返回的20180524101010转为yyyy-MM-dd HH:mm:ss
     *@param time 传递的日期字符串
     */
    public String FormatHourTime(String time) {
        Date parse = null;
        String dateString = "";
        try {
            parse = new SimpleDateFormat("yyyyMMddHHmmss").parse(time);
            dateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(parse);
        } catch (ParseException e) {
            dateString=null;
        }

        return dateString;
    }

    /**
     *@Description:日期转换，将yyyy-MM-dd转为yyyyMMdd
     *@param str 传递的日期字符串
     */
    private static String StringToDate(String str) {
        Date parse = null;
        String dateString = "";
        try {
            parse=new SimpleDateFormat("yyyy-MM-dd").parse(str);
            dateString = new SimpleDateFormat("yyyyMMdd").format(parse);
        } catch (ParseException e) {
            dateString=null;
        }
        return dateString;
    }



}
