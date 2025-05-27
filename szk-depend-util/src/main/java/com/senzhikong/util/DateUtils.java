package com.senzhikong.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


/**
 * 日期工具类
 *
 * @author Shu.zhou
 */
@Slf4j
public class DateUtils {

    public static final String YYYY = "yyyy";
    public static final String YYYY_MM = "yyyy-MM";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYY_MM_DD_HH = "yyyy-MM-dd HH";
    public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYY_MM_DD_HH_MM_SS_SSS = "yyyy-MM-dd HH:mm:ss:SSS";
    public static final String YYYYMM = "yyyyMM";
    public static final String YYYYMMDD = "yyyyMMdd";
    public static final String YYYYMMDDHH = "yyyyMMddHH";
    public static final String YYYYMMDDHHMM = "yyyyMMddHHmm";
    public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public static final String YYYYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";

    public static final String YYYY_MM_DDTHH_MM_SSXXX = "yyyy-MM-dd'T'HH:mm:ssXXX";

    /**
     * 转换字符串为date，格式yyyy-MM-dd HH:mm:ss
     *
     * @return 时间
     */
    public static Date getToday() {
        return parseDate(formatDate(new Date(), YYYYMMDD), YYYYMMDD);
    }

    /**
     * 转换字符串为date，格式yyyy-MM-dd HH:mm:ss
     *
     * @param date 日期字符串
     * @return 时间
     */
    public static Date parseDate(String date) {
        return parseDate(date, YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 转换字符串为date
     *
     * @param startDate 日期字符串
     * @param pattern   日期格式
     * @return 时间
     */
    public static Date parseDate(String startDate, String pattern) {
        if (StringUtils.isBlank(startDate)) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat();
        format.applyPattern(pattern);
        Date date;
        try {
            date = format.parse(startDate);
        } catch (ParseException e) {
            throw new RuntimeException("时间解析失败", e);
        }
        return date;
    }

    /**
     * 返回当前时间字符串，格式yyyy-MM-dd HH:mm:ss
     *
     * @return 时间字符串
     */
    public static String formatDate() {
        String dateStr;
        SimpleDateFormat format = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
        dateStr = format.format(new Date());
        return dateStr;
    }

    /**
     * 返回指定时间字符串，格式yyyy-MM-dd HH:mm:ss
     *
     * @param date 时间
     * @return 时间字符串
     */
    public static String formatDate(Date date) {
        if (null == date) {
            return null;
        }
        String dateStr;
        SimpleDateFormat format = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
        dateStr = format.format(date);
        return dateStr;
    }

    /**
     * 转换指定时间指定格式的字符串到指定的格式字符串
     *
     * @param date        时间字符串
     * @param fromPattern 原时间格式
     * @param toPattern   需要转换到的时间格式
     * @return 时间字符串
     */
    public static String formatDate(String date, String fromPattern, String toPattern) {
        if (null == date) {
            return null;
        }
        String dateStr = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat(toPattern);
            dateStr = format.format(parseDate(date, fromPattern));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return dateStr;
    }

    /**
     * 返回指定时间指定格式的字符串
     *
     * @param date    指定时间
     * @param pattern 指定格式
     * @return 时间字符串
     */
    public static String formatDate(Date date, String pattern) {
        if (date == null) {
            return null;
        }
        String dateStr;
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        dateStr = format.format(date);
        return dateStr;
    }

    public static String formatNow(String pattern) {
        return formatDate(new Date(), pattern);
    }

    /**
     * 计算两个时间的相隔天数
     *
     * @param date1 较小的时间
     * @param date2 较大的时间
     * @return 相隔分钟数
     */
    public static int daysBetween(Date date1, Date date2) {
        long time1 = date1.getTime();
        long time2 = date2.getTime();
        return Integer.parseInt(String.valueOf((time2 - time1) / (1000 * 60 * 60 * 24)));
    }

    /**
     * 计算两个时间的相隔小时数
     *
     * @param date1 较小的时间
     * @param date2 较大的时间
     * @return 相隔小时数
     */
    public static long hoursBetween(Date date1, Date date2) {
        long time1 = date1.getTime();
        long time2 = date2.getTime();
        return (time2 - time1) / (1000 * 60 * 60);
    }

    /**
     * 计算两个时间的相隔分钟数
     *
     * @param date1 较小的时间
     * @param date2 较大的时间
     * @return 相隔分钟数
     */
    public static long minutesBetween(Date date1, Date date2) {
        long time1 = date1.getTime();
        long time2 = date2.getTime();
        return (time2 - time1) / (1000 * 60);
    }

    /**
     * 计算两个时间的相隔秒数
     *
     * @param date1 较小的时间
     * @param date2 较大的时间
     * @return 相隔秒数
     */
    public static long secondsBetween(Date date1, Date date2) {
        long time1 = date1.getTime();
        long time2 = date2.getTime();
        return (time2 - time1) / (1000);
    }

    /**
     * 判断当前时间是否在时间段内
     *
     * @param startDate   开始时间
     * @param endDate     结束时间
     * @param currentDate 当前时间
     */
    public static boolean isBetweenDate(Date startDate, Date endDate, Date currentDate) {
        return currentDate.compareTo(startDate) >= 0 && endDate.compareTo(currentDate) >= 0;
    }

    /**
     * 获取当前月天数
     **/
    public static int getDayOfMonth() {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        return calendar.getActualMaximum(Calendar.DATE);
    }

    /**
     * 获取传入月份天数
     **/
    public static int getDayOfMonth(int month) {

        Calendar time = Calendar.getInstance();
        time.clear();
        time.set(Calendar.YEAR, time.get(Calendar.YEAR));
        // year年
        time.set(Calendar.MONTH, month - 1);
        // Calendar对象默认一月为0,month月
        return time.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取某年第一天日期
     *
     * @param year 年份
     * @return Date
     */
    public static Date getYearFirstDay(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        return calendar.getTime();
    }

    /**
     * 获取某年最后一天日期
     *
     * @param year 年份
     * @return Date
     */
    public static Date getYearLastDay(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        return calendar.getTime();
    }

    /**
     * 获取本周第一天
     */
    public static Date getWeekFirstDay() {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, 0);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取本月第一天
     *
     * @return 日期
     */
    public static Date getMonthFirstDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 0);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date getZeroOfToday() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取传入日期月份
     **/
    public static int getDateMonth(Date date) {
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        return cd.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取当前月份
     **/
    public static int getCurrentMonth() {
        return Calendar.getInstance()
                .get(Calendar.MONTH) + 1;
    }

    /**
     * 获取当前年份
     **/
    public static int getCurrentYear() {
        return Calendar.getInstance()
                .get(Calendar.YEAR);
    }

    /**
     * 获取当前几号
     **/
    public static int getCurrentDate() {
        return Calendar.getInstance()
                .get(Calendar.DATE);
    }

    public static Date addYear(Date date, int years) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.YEAR, years);
        return c.getTime();
    }

    public static Date addMonth(Date date, int months) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, months);
        return c.getTime();
    }

    public static Date addDay(Date date, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_YEAR, days);
        return c.getTime();
    }

    public static Date addHour(Date date, int hours) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.HOUR_OF_DAY, hours);
        return c.getTime();
    }

    public static Date addMinute(Date date, int minutes) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MINUTE, minutes);
        return c.getTime();
    }

    public static Date addSecond(Date date, int seconds) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.SECOND, seconds);
        return c.getTime();
    }

    /**
     * 获得当前日期与本周一相差的天数
     *
     * @return 天数
     */
    public static int getMondayPlus() {
        Calendar cd = Calendar.getInstance();
        // 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == 1) {
            return -6;
        } else {
            return 2 - dayOfWeek;
        }
    }

    /**
     * 获得当前周- 周一的日期
     *
     * @return 周一日期
     */
    public static String getCurrentMonday() {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus);
        Date monday = currentDate.getTime();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(monday);
    }

    /**
     * 获得当前周- 周日 的日期
     *
     * @return 周日 的日期
     */
    public static String getPreviousSunday() {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 6);
        Date monday = currentDate.getTime();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(monday);
    }

    /**
     * 获得当前月--开始日期
     *
     * @param date 日期
     * @return 月开始日期
     */
    public static String getMinMonthDate(String date) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(new SimpleDateFormat(YYYY_MM_DD).parse(date));
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
            return new SimpleDateFormat(YYYY_MM_DD).format(calendar.getTime());
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 获得当前月--结束日期
     *
     * @param date 日期
     * @return 月结束日期
     */
    public static String getMaxMonthDate(String date) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(YYYY_MM_DD);
        try {
            calendar.setTime(dateFormat.parse(date));
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            return dateFormat.format(calendar.getTime());
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 获取当前时间的时间戳
     *
     * @return 时间戳
     */
    public static long getTimestamp() {
        Date dNow = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dNow);
        return calendar.getTimeInMillis() / 1000;
    }

    /**
     * 时间戳转时间
     *
     * @param time 时间戳
     * @return 时间
     */
    public static Date timestampToDate(long time) {
        Timestamp timestamp = new Timestamp(time * 1000);
        return new Date(timestamp.getTime());
    }

    /**
     * 时间戳转时间字符串
     *
     * @param time 时间戳
     * @return 时间字符串
     */
    public static String timestampToString(Long time) {
        if (null == time) {
            return null;
        }
        Timestamp timestamp = new Timestamp(time * 1000);
        Date date = new Date(timestamp.getTime());
        return formatDate(date);
    }

}
