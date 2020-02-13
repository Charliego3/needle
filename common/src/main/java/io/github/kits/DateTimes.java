package io.github.kits;

import java.sql.Timestamp;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

/**
 * 时间日期工具类
 *
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public class DateTimes {

    public static final String YYYY_MM_DD_MM_HH_MM_SS_SSS = "yyyy-MM-dd HH:mm:ss:SSS";

    public static final String YYYY_MM_DD_MM_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    public static final String YYYY_MM_DD_MM_HH_MM = "yyyy-MM-dd HH:mm";

    public static final String YYYYMMDDMMHHMMSS = "yyyyMMddHHmmss";

    public static final String YYYYMMDDMMHHMM = "yyyyMMddHHmm";

    public static final String YYYYMMDD = "yyyyMMdd";

    public static Date get(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(YYYY_MM_DD_MM_HH_MM_SS);
        LocalDateTime localDateTime = LocalDateTime.parse(date, formatter);
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    /**
     * 格式化时间
     *
     * @param date      日期
     * @param pattern    格式
     * @return          格式化后的字符串
     */
    public static String format(Date date, String pattern) {
        return DateTimeFormatter.ofPattern(pattern).format(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
    }

    /**
     * 格式化时间
     *
     * @param time      日期
     * @param pattern    格式
     * @return          格式化后的字符串
     */
    public static String format(long time, String pattern) {
        return format(new Date(time), pattern);
    }

    /**
     * 格式化日期为字符串格式,
     *      默认格式为: yyyy-MM-dd HH:mm:ss
     *
     * @param date      日期对象
     * @return          格式化后的字符串时间
     */
    public static String formatToDefaultPattern(Date date) {
        return format(date, YYYY_MM_DD_MM_HH_MM_SS);
    }

    /**
     * 格式化日期为字符串格式,
     *      默认格式为: yyyy-MM-dd HH:mm:ss
     *
     * @param time      时间戳
     * @return          格式化后的字符串时间
     */
    public static String formatToDefaultPattern(long time) {
        return format(time, YYYY_MM_DD_MM_HH_MM_SS);
    }

    /**
     * 获取当前的时间，并转换为date字符串
     *
     * @param pattern   格式
     * @return          格式化后的字符串
     */
    public static String now(String pattern) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 获取当前的时间
     *
     * @return          格式化后的字符串时间
     */
    public static String now() {
        return now(YYYY_MM_DD_MM_HH_MM_SS);
    }

    /**
     * 获取当前的时间
     *
     * @return          格式化后的字符串时间
     */
    public static String nowGMT() {
        return DateTimeFormatter.RFC_1123_DATE_TIME.format(LocalDateTime.now().atZone(ZoneId.of("GMT")));
    }

    /**
     * 获取当前的时间
     *
     * @return          当前时间戳
     */
    public static long nowMillis() {
        return Clock.systemDefaultZone().millis();
    }

    /**
     * 获取当前Timestamp时间
     *
     * @return          Timestamp对象
     */
    public static Timestamp nowTimestamp() {
        return new Timestamp(nowMillis());
    }

    /**
     * 根据指定时间戳（毫秒ms），获取那天的0时0分0秒的时间戳
     *
     * @param time      要获取的那天时间戳
     * @return          时间戳
     */
    public static long getStartTimeOfDay(long time) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault())
                .with(LocalTime.MIN).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * 根据指定时间戳（毫秒ms），获取那天的0时0分0秒的时间戳
     *
     * @param time      要获取的那天时间戳
     * @return          时间戳
     */
    public static long getStartTimeOfDay(Date time) {
        return time.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().with(LocalTime.MIN)
                .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * 获取当天的开始时间0时0分0秒的时间戳
     *
     * @return          时间戳
     */
    public static long getStartTimeOfDay() {
        return getStartTimeOfDay(nowMillis());
    }

    /**
     * 根据指定时间戳（毫秒ms），获取那天的23时59分59秒的时间戳
     *
     * @param time      要获取的当天的任意时间戳
     * @return          结束时间戳
     */
    public static long getEndTimeOfDay(long time) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault())
                .with(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * 根据指定时间戳（毫秒ms），获取那天的23时59分59秒的时间戳
     *
     * @param time      要获取的当天的任意时间
     * @return          结束时间戳
     */
    public static long getEndTimeOfDay(Date time) {
        return time.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().with(LocalTime.MAX)
                .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * 获取当天的结束时间23时59分59秒的时间戳
     *
     * @return          当天结束时间戳
     */
    public static long getEndTimeOfDay() {
        return getEndTimeOfDay(nowMillis());
    }

}
