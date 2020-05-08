package com.sptan.exec.rediscache.utils;


import com.sptan.exec.rediscache.exception.BadRequestException;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The type Date utils.
 *
 * @author liupeng
 * @version 1.0
 */
public final class DateUtils {

    /**
     * Generate date sequence list.
     *
     * @param start the start
     * @param end   the end
     * @return the list
     */
    public static List<LocalDate> generateDateSequence(LocalDate start, LocalDate end) {
        if (start == null || end == null) {
            return new ArrayList<>();
        }

        long days = end.toEpochDay() - start.toEpochDay();
        List<LocalDate> result = Stream.iterate(start, d -> d.plusDays(1))
            .limit(days).collect(Collectors.toList());
        return result;
    }

    /**
     * 计算时间差-分钟.
     *
     * @param from the from
     * @param to   the to
     * @return the int
     */
    public static int differenceMinute(Date from, Date to) {
        if (from == null || to == null) {
            throw new BadRequestException("非法的时间");
        }
        int minutes = (int) ((to.getTime() - from.getTime()) / (1000 * 60));
        return minutes;
    }

    /**
     * 判断时间是否在指定时间区间内
     *
     * @param nowTime
     * @param startTime
     * @param endTime
     * @return boolean
     */
    public static boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
        if (nowTime.getTime() == startTime.getTime()
                || nowTime.getTime() == endTime.getTime()) {
            return true;
        }
        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);
        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);
        Calendar end = Calendar.getInstance();
        end.setTime(endTime);
        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取年月日时分秒加八位随机数.
     *
     * @return string date string
     */
    public static String getDateString() {
        Calendar now = Calendar.getInstance();
        String year = String.valueOf(now.get(Calendar.YEAR));
        String month = String.valueOf(now.get(Calendar.MONTH) + 1);
        String day = String.valueOf(now.get(Calendar.DAY_OF_MONTH));
        String hour = String.valueOf(now.get(Calendar.HOUR_OF_DAY));
        String min = String.valueOf(now.get(Calendar.MINUTE));
        String sec = String.valueOf(now.get(Calendar.SECOND));
        //取四位随机数
        String random = String.valueOf((int) ((Math.random() * 9 + 1) * 10000000));
        String result = " ";
        if (month.length() == 1) {
            month = "0" + month;
        }
        if (day.length() == 1) {
            day = "0" + day;
        }
        if (hour.length() == 1) {
            hour = "0" + hour;
        }
        if (min.length() == 1) {
            min = "0" + min;
        }
        if (sec.length() == 1) {
            sec = "0" + sec;
        }
        return (year + month + day + hour + min + sec + random);
    }

    /**
     * 时间戳转时间(10位时间戳)
     * @param time
     * @return
     */
    public static String timestamp10(long time) {
        String dateTime;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        long timeLong = time /1000;
        return String.valueOf(timeLong);
    }
}
