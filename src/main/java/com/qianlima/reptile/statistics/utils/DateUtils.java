package com.qianlima.reptile.statistics.utils;


import com.baomidou.mybatisplus.core.toolkit.Assert;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author liuchanglin
 * @version 1.0
 * @ClassName: DateUtils
 * @Description: 时间操作工具类
 * @date 2019/10/25 11:06 上午
 */
public class DateUtils {
    public static final SimpleDateFormat EXACTSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static final SimpleDateFormat FUZSDF = new SimpleDateFormat("yyyy-MM-dd");

    public static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM");

    public static final DateTimeFormatter LDTF = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static final DateTimeFormatter LTDTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static final String dateStartStr = " 00:00:00";

    public static final String dateEndStr = " 23:59:59";

    public static final String monthStart = "-01";

    public static final String monthEnd = "-31";

    public static YearMonth getYearMonth(String date) {
        return YearMonth.parse(date, DTF);
    }

    public static LocalDate getLocalDate(String date) {
        return LocalDate.parse(date, LDTF);
    }

    public static LocalDateTime getLocalDateTime(String time) {
        return LocalDateTime.parse(time, LTDTF);
    }

    public static LocalDate getLocalDate(long timestamp) {
        return Instant.ofEpochMilli(timestamp).atZone(ZoneOffset.ofHours(8)).toLocalDate();
    }

    public static long getTimeStamp(LocalDate localDate) {
        return localDate.atStartOfDay(ZoneOffset.ofHours(8)).toInstant().toEpochMilli();
    }

    /**
     * 将字符串转日期成Long类型的时间戳，格式为：yyyy-MM-dd HH:mm:ss
     */
    public static Long convertTimeToLong(String time) {
        Assert.notNull(time, "time is null");
        LocalDateTime parse = getLocalDateTime(time);
        return LocalDateTime.from(parse).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static String getNowFormatDateStr() {
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


    public static String getFormatDateStrBitAdd(Long timestmp, SimpleDateFormat SDF) {
        if (timestmp == null) {
            return "";
        }
        return SDF.format(new Date(timestmp * 1000));
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

    public static Long getYesterTodayStartTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH) - 1, 0, 0, 0);
        long tt = calendar.getTime().getTime() / 1000;
        return tt;

    }

    public static Long getYesterTodayEndTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH) - 1, 0, 0, 0);
        long tt = calendar.getTime().getTime() / 1000;
        return tt + 86399;
    }


    /**
     * 判断给定日期是否为月末的一天
     *
     * @param date
     * @return true:是|false:不是
     */
    public static boolean isLastDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DATE, (calendar.get(Calendar.DATE) + 1));
        if (calendar.get(Calendar.DAY_OF_MONTH) == 1) {
            return true;
        }
        return false;
    }


    /**
     * @return a
     * @description 获取某一日期前一天
     * @author gyx
     * @date 2020-03-22 15:39
     * @parameter * @param null
     * @since
     */
    public static String getLastDay(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date date = null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.setTime(date);
        int day = calendar.get(Calendar.DATE);
        //                      此处修改为+1则是获取后一天
        calendar.set(Calendar.DATE, day - 1);

        String lastDay = sdf.format(calendar.getTime());
        return lastDay;
    }

    /**
     * @return a
     * @description 修改日期格式
     * @author gyx
     * @date 2020-03-22 20:5
     * @parameter * @param null
     * @since
     */
    public static String getDateByYm(String dateStr) throws ParseException {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
        String now = new SimpleDateFormat("yyyy-MM").format(date);
        return now;
    }


    /**
     * @param startMonth @example: "2016-01"
     * @param endMonth   @example: "2019-11"
     * @throws ParseException
     * @return 两个时间之间的月份(含开始, 结束)
     */
    public static List<String> getMonths(String startMonth, String endMonth) throws ParseException {
        LinkedList<String> months = new LinkedList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Calendar minCalender = Calendar.getInstance();
        Calendar maxCalender = Calendar.getInstance();

        // 设置开始月份
        minCalender.setTime(sdf.parse(startMonth));
        minCalender.set(minCalender.get(Calendar.YEAR), minCalender.get(Calendar.MONTH), 1);

        maxCalender.setTime(sdf.parse(endMonth));
        // 设置日期,保证最后一个日期参数 大于 开始时间日历
        maxCalender.set(maxCalender.get(Calendar.YEAR), maxCalender.get(Calendar.MONTH), 2);

        while (minCalender.before(maxCalender)) {
            months.add(sdf.format(minCalender.getTime()));
            minCalender.add(Calendar.MONTH, 1);
        }
        return months;
    }

    /**
     * @return a
     * @description 获取两个日期之间全部日期
     * @author gyx
     * @ate 2020-03-24 23:34
     * @parameter * @param null
     * @since
     */
    public static List<String> getDates(String startDate, String endDate) {
        List<String> list = new ArrayList<String>();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            //保存日期的集合 
            Date date_start = sdf.parse(startDate);
            Date date = date_start;
            //用Calendar 进行日期比较判断
            SimpleDateFormat sdfMonth = new SimpleDateFormat("yyyy-MM");
            Date validateDate = sdfMonth.parse(endDate.substring(0, 7));
            Calendar cdMonth = Calendar.getInstance();
            cdMonth.setTime(validateDate);
            cdMonth.add(Calendar.MONTH, 1);
            Date date_end = cdMonth.getTime();
            Calendar cd = Calendar.getInstance();
            while (date.getTime() < date_end.getTime()) {
                list.add(sdf.format(date));
                cd.setTime(date);
                //增加一天 放入集合
                cd.add(Calendar.DATE, 1);
                date = cd.getTime();
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static Long str2TimeStamp(String time, SimpleDateFormat format) {
        Date date = null;
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //日期转时间戳（秒）
        return date.getTime() / 1000;
    }


    public static String monthEarly(String data) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String maxDateStr = data;
        String minDateStr = "";
        Calendar calc = Calendar.getInstance();
        try {
            calc.setTime(sdf.parse(maxDateStr));
            calc.add(calc.DATE, -30);
            Date minDate = calc.getTime();
            minDateStr = sdf.format(minDate);
            return (minDateStr);
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        return "";
    }

}
