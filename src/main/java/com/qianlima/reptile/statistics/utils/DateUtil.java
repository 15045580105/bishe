package com.qianlima.reptile.statistics.utils;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 日期工具类，提供与日期相关的常用方法
 *
 * @Class Name DateUtil
 * @Author sa
 * @Create In 2008-10-6
 */
public class DateUtil {
    //~ Static fields/initializers =============================================

    private static Log log = LogFactory.getLog(DateUtil.class);
    public static String datePattern = "yyyy-MM-dd"; //20040807 tufei
    public static String timePattern = datePattern + " HH:mm:ss";
    public static SimpleDateFormat sdf = new SimpleDateFormat(timePattern);
    public static SimpleDateFormat sdf1 = new SimpleDateFormat(datePattern);

    //~ Methods ================================================================

    /**
     * Return default datePattern (yyyy-MM-dd)
     *
     * @return a string representing the date pattern on the UI
     */
    public static String getDatePattern() {
        return datePattern;
    }

    public static String getTimePattern() {
        return timePattern;
    }

    /**
     * This method attempts to convert an Oracle-formatted date
     * in the form dd-MMM-yyyy to yyyy-mm-dd.
     *
     * @param aDate date from database as a string
     * @return formatted string for the ui
     */
    public static final String getDate(Date aDate) {
        String returnValue = "";
        if (aDate != null) {
            returnValue = sdf1.format(aDate);
        }
        return (returnValue);
    }

    /**
     * @return a
     * @description 获取十三位时间戳
     * @author gyx
     * @date 2020-01-20 16:5
     * @parameter * @param null
     * @since
     */
    public static String date2TimeStamp(String date_str) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return String.valueOf(sdf.parse(date_str).getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String date3TimeStamp(String date_str) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return String.valueOf(sdf.parse(date_str).getTime() / 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getfromatString(String datestr) {
        String dstr = "";
        if (datestr != null && !"".equals(datestr)) {
            try {
                dstr = convertDateToString(new SimpleDateFormat(datePattern).parse(datestr));
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return dstr;
    }

    @SuppressWarnings("deprecation")
    public static Date getLastDayOfMonth(Date sDate1) {
        Calendar cDay1 = Calendar.getInstance();
        cDay1.setTime(sDate1);
        final int lastDay = cDay1.getActualMaximum(Calendar.DAY_OF_MONTH);
        Date lastDate = cDay1.getTime();
        lastDate.setDate(lastDay);
        return lastDate;
    }

    @SuppressWarnings("deprecation")
    public static Date getFirstDayOfMonth(Date sDate1) {
        Calendar cDay1 = Calendar.getInstance();
        cDay1.setTime(sDate1);
        final int lastDay = cDay1.getActualMinimum(Calendar.DAY_OF_MONTH);
        Date lastDate = cDay1.getTime();
        lastDate.setDate(lastDay);
        return lastDate;
    }


    /**
     * This method generates a string representation of a date/time
     * in the format you specify on input
     *
     * @param aMask   the date pattern the string is in
     * @param strDate a string representation of a date
     * @return a converted Date object
     * @throws ParseException
     * @see SimpleDateFormat
     */
    public static final Date convertStringToDate(String aMask, String strDate) throws ParseException {
        SimpleDateFormat df = null;
        Date date = null;
        df = new SimpleDateFormat(aMask);

        if (log.isDebugEnabled()) {
            log.debug("converting '" + strDate + "' to date with mask '"
                    + aMask + "'");
        }

        try {
            date = df.parse(strDate);
        } catch (ParseException pe) {
            //log.error("ParseException: " + pe);
            throw new ParseException(pe.getMessage(), pe.getErrorOffset());
        }

        return (date);
    }

    /**
     * This method returns the current date time in the format:
     * yyyy-MM-dd HH:MM a
     *
     * @param theTime the current time
     * @return the current date/time
     */
    public static String getTimeNow(Date theTime) {
        return getDateTime(timePattern, theTime);
    }

    /**
     * This method returns the current date in the format: MM/dd/yyyy
     *
     * @return the current date
     * @throws ParseException
     */
    public static Calendar getToday() throws ParseException {
        Date today = new Date();
        SimpleDateFormat df = new SimpleDateFormat(datePattern);

        // This seems like quite a hack (date -> string -> date),
        // but it works ;-)
        String todayAsString = df.format(today);
        Calendar cal = new GregorianCalendar();
        cal.setTime(convertStringToDate(todayAsString));

        return cal;
    }

    /**
     * 获取当前日期，日期格式为年月日
     *
     * @return java.util.Date
     * @Methods Name getCurrentDate
     * @Create In 2008-10-6 By sa
     */
    public static Date getCurrentDate() {
        Date today = new Date();
        SimpleDateFormat df = new SimpleDateFormat(datePattern);

        String todayAsString = df.format(today);
        Calendar cal = new GregorianCalendar();
        //         try {
        cal.setTime(convertStringToDate(todayAsString));
        //		} catch (ParseException e) {
        //			e.printStackTrace();
        //		}

        return (Date) cal.getTime();
    }

    /**
     * 获取当前的日期，日期格式为年月日时分秒
     *
     * @return java.util.Date
     * @Methods Name getCurrentDateTime
     * @Create In 2008-10-6 By sa
     */
    public static Date getCurrentDateTime() {
        Date today = new Date();
        SimpleDateFormat df = new SimpleDateFormat(DateUtil.timePattern);

        String todayAsString = df.format(today);
        Calendar cal = new GregorianCalendar();
        //        try {
        cal.setTime(convertStringToDate(todayAsString));
        //		} catch (ParseException e) {
        //			e.printStackTrace();
        //		}

        return (Date) cal.getTime();
    }


    public static java.sql.Date getCurrentSQLDate() {
        Date today = new Date();
        SimpleDateFormat df = new SimpleDateFormat(datePattern);

        String todayAsString = df.format(today);
        Calendar cal = new GregorianCalendar();
        //        try {
        cal.setTime(convertStringToDate(todayAsString));
        //		} catch (ParseException e) {
        //			e.printStackTrace();
        //		}

        return (java.sql.Date) cal.getTime();
    }

    /**
     * This method generates a string representation of a date's date/time
     * in the format you specify on input
     *
     * @param aMask the date pattern the string is in
     * @param aDate a date object
     * @return a formatted string representation of the date
     * @see SimpleDateFormat
     */
    public static final String getDateTime(String aMask, Date aDate) {
        SimpleDateFormat df = null;
        String returnValue = "";

        if (aDate == null) {
            log.error("aDate is null!");
        } else {
            df = new SimpleDateFormat(aMask);
            returnValue = df.format(aDate);
        }

        return (returnValue);
    }

    /**
     * 将日期转成 年月日 格式的字符串
     *
     * @param aDate A date to convert
     * @return a string representation of the date
     */
    public static final String convertDateToString(Date aDate) {
        return getDateTime(datePattern, aDate);
    }

    /**
     * 将日期转成年月日 时分秒格式的字符串
     *
     * @param aDate
     * @return String
     * @Methods Name convertDateTimeToString
     * @Create In Apr 13, 2009 By sa
     */
    public static final String convertDateTimeToString(Date aDate) {
        return getDateTime(timePattern, aDate);
    }

    public static final Date convertStringToDateTime(String aDate) {
        try {
            return convertStringToDate(timePattern, aDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This method converts a String to a date using the datePattern
     *
     * @param strDate the date to convert (in format yyyy-MM-dd/)
     * @return a date object
     * @throws ParseException
     */
    public static Date convertStringToDate(String strDate) {
        Date aDate = null;

        try {
            if (log.isDebugEnabled()) {
                log.debug("converting date with pattern: " + datePattern);
            }
            aDate = convertStringToDate(datePattern, strDate);
        } catch (ParseException pe) {
            log.error("Could not convert '" + strDate
                    + "' to a date, throwing exception");
            pe.printStackTrace();
			/*throw new ParseException(pe.getMessage(),
                                     pe.getErrorOffset());*/

        }

        return aDate;
    }


    public static Date addDays(Date date, int days) {
        return add(date, days, Calendar.DATE);
    }


    public static Date addMonths(Date date, int months) {
        return add(date, months, Calendar.MONTH);
    }

    public static Date add(Date date, int amount, int field) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);
        calendar.add(field, amount);

        return calendar.getTime();
    }

    /**
     * 把yyyy-MM-dd HH类型的字符创转换成日期类型
     *
     * @param da
     * @return
     */
    public static Date getStringToDateFull(String da) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(da);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getDateToStringFull(Date date) {
        //SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //Date date=null;
        String da = sdf.format(date);
        return da;
    }

    public static String getDateToStringFull2(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        //Date date=null;
        String da = sdf.format(date);
        return da;
    }

    //获取指定范围的随机数
    public static int getRandom(int _min, int _max) {
        int random = 0;
        int min = _min;
        int max = _max + 1;
        boolean flag = true;

        while (flag) {
            random = (int) Math.round(Math.floor(Math.random() * max));
            if (random >= min) {
                flag = false;
            }
        }
        return random;
    }

    public static int getDateNum(Date date) {
        Calendar cDay1 = Calendar.getInstance();
        cDay1.setTime(date);
        int num = cDay1.getActualMaximum(Calendar.DAY_OF_MONTH);
        return num;
    }

    /**
     * 获取唯一值id，年月日时分秒+三位随即数
     *
     * @return
     */
    public static String getdateAndRandom() {
        String s = getDateToStringFull2(new Date()) + getRandom(100, 999);
        return s;
    }

    //获取2日期之间有多少工作日
    public static int getWorkDays(String intime, String endtime) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date begDate = sdf.parse(intime);
        Date endDate = sdf.parse(endtime);
        if (begDate.after(endDate)) throw new Exception("日期范围非法");
        // 总天数
        int days = (int) ((endDate.getTime() - begDate.getTime()) / (24 * 60 * 60 * 1000)) + 1;
        // 总周数，
        int weeks = days / 7;
        int rs = 0;
        // 整数周
        if (days % 7 == 0) {
            rs = days - 2 * weeks;
        } else {
            Calendar begCalendar = Calendar.getInstance();
            Calendar endCalendar = Calendar.getInstance();
            begCalendar.setTime(begDate);
            endCalendar.setTime(endDate);
            // 周日为1，周六为7
            int beg = begCalendar.get(Calendar.DAY_OF_WEEK);
            int end = endCalendar.get(Calendar.DAY_OF_WEEK);
            if (beg > end) {
                rs = days - 2 * (weeks + 1);
            } else if (beg < end) {
                if (end == 7) {
                    rs = days - 2 * weeks - 1;
                } else {
                    rs = days - 2 * weeks;
                }
            } else {
                if (beg == 1 || beg == 7) {
                    rs = days - 2 * weeks - 1;
                } else {
                    rs = days - 2 * weeks;
                }
            }
        }
        // System.out.println(sdf.format(begDate)+"到"+sdf.format(endDate)+"中间有"+rs+"个工作日");
        return rs;
    }

    //当前输入日期是否是工作日 true:是工作日  false：不是
    public static boolean isWork(String date) {
        String[] strs = date.split("-");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Integer.parseInt(strs[0]));
        cal.set(Calendar.MONTH, Integer.parseInt(strs[1]) - 1);
        cal.set(Calendar.DATE, Integer.parseInt(strs[2]) - 1);
        int day = cal.get(Calendar.DAY_OF_WEEK);
        if (day == Calendar.SATURDAY || day == Calendar.SUNDAY) {
            return false;
        }
        return true;
    }

    //验证输入日期格式是否异常
    public static void checkdateFormat(String date) throws Exception {
        String[] strs = date.split("-");
        if (strs.length == 1) {
            throw new Exception("日期格式不正确！");
        }
    }


    /**
     * 随机睡眠时间段
     *
     * @param minNumber
     * @param maxNumber
     */
    public static void threadSleep(int minNumber, int maxNumber) throws Exception {
        int x = (int) (minNumber + Math.random() * (maxNumber - minNumber));
        log.info("随机睡眠" + x + "秒");
        Thread.sleep((x * 1000));
    }

    /**
     * 随机睡眠时间段
     *
     * @param minNumber
     * @param maxNumber
     */
    public static void threadSleepMs(int minNumber, int maxNumber) throws Exception {
        int x = (int) (minNumber + Math.random() * (maxNumber - minNumber));
        log.info("随机睡眠" + x + "毫秒");
        Thread.sleep((x));
    }

    public static Long getHourTime(Integer hour) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTimeInMillis() / 1000L;
    }

    public static String getDateTime(Integer days) {
        return sdf1.format(addDays(new Date(), days));
    }

    public static String getFormatDate(Long time) {
        return sdf.format(new Date(time));
    }

    public static void main(String[] args) {
        try {
//            System.out.println(sdf1.format(addDays(new Date(), -1)));

//            log.info(StringUtils.containsAny("abc", "ax"));

            Map<String,Integer> map=new HashMap<>();
            map.put("A", 1000);
            map.put("B", 2000);
            map.put("C", 3000);

            // add new key value ("A",99), if key "A" exist in map then do function  "(X,Y) -> (Y+X)" ,the function return value will replace the oldvalue.
            Integer newValue1 = map.merge("A", 99, (X,Y) -> (Y+X));
//            Integer newValue2 = map.merge("A", 888, (X,Y) -> Y);
            // add new key value ("D",666), if key "D" not exist in map then insert the key value to map
            Integer newValue3 = map.merge("D", 666, (X,Y) -> Y);
            Integer newValue4 = map.merge("F", 777, (X,Y) -> Y);
            Integer newValue5 = map.merge(null, 5555, (X,Y) -> Y);


            for(String s:map.keySet()){
                System.out.println(s+" : "+map.get(s));
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
