package com.qianlima.reptile.statistics.utils;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @ClassName: DateUtils
 * @Description: 时间操作工具类
 * @author liuchanglin
 * @date 2019/10/25 11:06 上午
 * @version 1.0
 */
public class DateUtils {
    public static final SimpleDateFormat EXACTSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static final SimpleDateFormat FUZSDF = new SimpleDateFormat("yyyy-MM-dd");

<<<<<<< HEAD
    public static String getNowFormatDateStr() {
=======
    public static final DateTimeFormatter DTF =DateTimeFormatter.ofPattern("yyyy-MM");


    public static YearMonth getLocalDate(String date) {
        return YearMonth.parse(date, DTF);
    }

    public static String getNowFormatDateStr(){
>>>>>>> 1a5ccfac969f9ff1ce3aa40620843759251b92f1
        return EXACTSDF.format(new Date());
    }

    public static String getFormatDateStr(Long timestmp) {
        if (timestmp == null) {
            return "";
        }
        return EXACTSDF.format(new Date(timestmp));
    }

    public static Date getFormatDate(Date date) {
        if (date == null) {
            return new Date();
        }
        Date parse;
        try {
            String format = EXACTSDF.format(new Date());
            parse = EXACTSDF.parse(format);
        } catch (Exception e) {
            e.printStackTrace();
            parse = new Date();
        }

        return parse;
    }

    /**
     * 返回 指定格式的日期字符串
     *
     * @param timestmp dateFormat
     * @return format date String
     */
    public static String getFormatDateStr(Long timestmp, SimpleDateFormat SDF) {
        if (timestmp == null) {
            return "";
        }
        return SDF.format(new Date(timestmp));
    }


    /**
     * java.util.Date 返回 yyyy-MM-dd String 类型
     *
     * @param date
     * @return format date String
     */
    public static String getFormatDateStr(Date date) {
        if (date == null) {
            return "";
        }
        return EXACTSDF.format(date);
    }


    /**
     * 返回 指定格式的日期字符串
     *
     * @param date dateFormat(例如 yyyy-MM-dd)
     * @return format date String
     */
    public static String getFormatDateStr(Date date, String dateFormat) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat SDF = new SimpleDateFormat(dateFormat);
        return SDF.format(date);
    }


    /**
     * java.lang.Integer Epoch 返回 yyyy-MM-dd String 类型
     *
     * @param num
     * @return format date String
     */
    public static String getFormatEpochStr(int num) {
        long time = num * 1000L;
        return EXACTSDF.format(new Date(time));
    }

    /**
     * yyyy-MM-dd 返回 java.util.Date
     * 不会返回null, 产生异常时返回当前时间的Date对象的实例
     *
     * @param formatStr
     * @return
     */
    public static Date parseDateFromDateStr(String formatStr) {
        Date date;
        try {
            date = EXACTSDF.parse(formatStr);
        } catch (Exception exp) {
            date = new Date();
        }
        return date;
    }

    /**
     * 获取自昨天12点到今天12点的int类型
     *
     * @return {昨天12点epoch_int, 今天12点epoch_int}
     */
    public static int[] getDate() {
        Calendar calendar = getCalendar();
        int[] res = new int[2];
        res[1] = (int) (calendar.getTimeInMillis() / 1000);
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        res[0] = (int) (calendar.getTimeInMillis() / 1000);
        return res;
    }

    /**
     * 获取yyyy-MM-dd 形式的前后日期时间
     * [0] prev_day [1] current day
     *
     * @param formatDate
     * @return
     */
    public static int[] getDate(String formatDate) {
        Calendar calendar = getCalendar();
        String[] dateStr = formatDate.split("-");
        calendar.set(Integer.valueOf(dateStr[0]), Integer.valueOf(dateStr[1]) - 1, Integer.valueOf(dateStr[2]));
        int[] res = new int[2];
        res[1] = (int) (calendar.getTimeInMillis() / 1000);
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        res[0] = (int) (calendar.getTimeInMillis() / 1000);
        return res;
    }

    /**
     * 获取某一天的上一天 12点到当天12点的int类型
     *
     * @return {上一天12点epoch_int, 当天12点epoch_int}
     */
    public static int[] getBetweenDate(int n) {
        Calendar calendar = getCalendar();
        int[] res = new int[2];
        calendar.add(Calendar.DAY_OF_YEAR, 0 - n);
        res[1] = (int) (calendar.getTimeInMillis() / 1000);
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        res[0] = (int) (calendar.getTimeInMillis() / 1000);
        return res;
    }

    /**
     * @param gap      数量
     * @param isFuture true 将来, false 过去
     * @return [Date ,expire time epoch Integer type]
     */
    public static Object[] getEpoch(int gap, boolean isFuture) {
        Calendar calendar = getCalendar();
        gap *= isFuture ? 1 : -1;
        calendar.add(Calendar.DAY_OF_YEAR, gap);
        return new Object[]{calendar.getTime(), (int) (calendar.getTimeInMillis() / 1000)};
    }

    private static Calendar getCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar;
    }

    /**
     * 比较两个时间的大小
     */
    public static int compareDate(String DATE1, String DATE2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }


    /**
     * 将字符串时间格式转换成Date时间格式，参数String类型
     * 比如字符串时间："2017-12-15 21:49:03"
     * 转换后的date时间：Fri Dec 15 21:49:03 CST 2017
     *
     * @param datetime 类型为String
     * @return
     */
    public static Date StringToDate(String datetime, SimpleDateFormat sdFormat) {
//        SimpleDateFormat sdFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        try {
            date = sdFormat.parse(datetime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * @return a
     * @description 判断一个日期是否是本月最后一天
     * @author gyx
     * @date 2020-03-18 15:5
     * @parameter * @param null
     * @since
     */
    public boolean isLastDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH) == calendar
                .getActualMaximum(Calendar.DAY_OF_MONTH);
    }

}
