package utils;


import constant.Constant;
import exception.DataException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;


/**
 * Created by Li.Hou on 2017/11/6.
 */
public final class CalendarUtil {

    //日期格式
    public static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    //一周天数
    public static final int WEEKNUMBERS = 7;
    private static final Logger logger = LogUtil.setLoggerHanlder(Logger.getLogger(LogUtil.MY_LOGGER), LogUtil.OUTPUTPATH);
    //日历工具
    private static Calendar calendar = Calendar.getInstance();

    private CalendarUtil() {


    }

    /**
     * 获取周数对应表
     *
     * @return
     */
    public static String[][] getWeekNums(int year) {
        Date start, end;
        try {
            start = simpleDateFormat.parse(year + "-01-01");
            end = simpleDateFormat.parse(year + "-12-31");
        } catch (ParseException e) {
            logger.severe("解析日期出错，获取周数表错误:" + e.getMessage());
            return null;
        }
        //获取相差的天数
        int diffDays = (int) ((end.getTime() - start.getTime()) / (1000 * 3600 * 24));
        //计算数组的行数
        int rowsNum = (diffDays / WEEKNUMBERS) + 2;
        String[][] result = new String[rowsNum][2];
        result[0][0] = Constant.ColumnName.START_DATE;
        result[0][1] = Constant.ColumnName.WEEK_NUM;
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(start);

        //将起始日期调整到周一
        int weekNum = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (weekNum == 0) {
            calendar.add(Calendar.DAY_OF_WEEK, 1);
        }
        if (!(1 == weekNum) && weekNum != 0) {
            calendar.add(Calendar.DAY_OF_WEEK, 8 - weekNum);
        }
        //在截止日期前写入日期对应周数
        int i = 1;
        while (!calendar.getTime().after(end)) {
            result[i][0] = simpleDateFormat.format(calendar.getTime());
            result[i][1] = getWeekNum(result[i][0]).toString();
            calendar.add(Calendar.DAY_OF_WEEK, WEEKNUMBERS);
            i++;
        }
        //避免末尾出现null
        if (result[rowsNum - 1][0] == null && result[rowsNum - 1][1] == null) {
            result = Arrays.copyOfRange(result, 0, rowsNum - 1);
        }
        return result;
    }

    /**
     * 根据日期获取周数
     *
     * @param date 目标日期
     * @return
     */
    public static Integer getWeekNum(String date) {
        try {
            calendar.setTime(simpleDateFormat.parse(date));
        } catch (Exception e) {
            e.printStackTrace();
        }
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        //获取当前周数
        int year = calendar.get(Calendar.YEAR);
        int week = calendar.get(Calendar.WEEK_OF_YEAR) - 1;
        //获取月份
        Date tmp = calendar.getTime();
        try {
            calendar.setTime(simpleDateFormat.parse(String.valueOf(year) + "-01-01"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        //如果是遇到一月一日是周一的情况，设置该日为第一周起点
        if (calendar.get(Calendar.DAY_OF_WEEK) - 1 == 1) {
            week += 1;
        }

        calendar.setTime(tmp);
        int month = calendar.get(Calendar.MONTH);
        //防止出现12-31是第一周的情况
        if (month >= 11 && week <= 1) {
            week += 52;
        }
        return week;
    }

    /**
     * 是否周一
     *
     * @param date
     * @return
     * @throws Exception
     */
    public static boolean isMonday(String date) throws ParseException {
        calendar.setTime(simpleDateFormat.parse(date));
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        return calendar.get(Calendar.DAY_OF_WEEK) - 1 == 1;
    }

    /**
     * 判断某一天是否属于某年某月
     * 该月的起始和截止日，如果工作日三天及以上在本月，则算作本月，如果不是，则算作上月或者下月
     *
     * @param month
     * @param date
     * @return
     * @throws Exception
     */
    public static boolean inThisMonthAndIsMonday(int year, int month, String date) throws Exception {
        if (!isMonday(date)) {
            return false;
        }
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 0);
        int lastWeek = calendar.get(Calendar.WEEK_OF_MONTH);
        calendar.setTime(simpleDateFormat.parse(date));
        int weekNum = calendar.get(Calendar.WEEK_OF_MONTH);
        if (weekNum >= 1 && weekNum < lastWeek) {
            return calendar.get(Calendar.YEAR) == year && calendar.get(Calendar.MONTH) + 1 == month;
        }
        if (weekNum == lastWeek) {
            calendar.add(Calendar.MONTH, 1);
            calendar.set(Calendar.DAY_OF_MONTH, 0);
            if (calendar.get(Calendar.DAY_OF_WEEK) - 1 > 2) {
                return calendar.get(Calendar.YEAR) == year && calendar.get(Calendar.MONTH) + 1 == month;
            } else {
                if (calendar.get(Calendar.MONTH) == Calendar.DECEMBER) {
                    return calendar.get(Calendar.YEAR) + 1 == year && month == Calendar.JANUARY;
                } else {
                    return calendar.get(Calendar.YEAR) == year && calendar.get(Calendar.MONTH) + 2 == month;
                }
            }
        }
        return false;
    }

    /**
     * 获取某月第一周的年周数
     *
     * @param year
     * @param month
     * @return
     */
    public static Integer getMonthWeekNum(int year, int month) {
        String strMonth = month < 10 ? ("0" + month) : String.valueOf(month);
        int weekNum;
        try {
            calendar.setTime(simpleDateFormat.parse(year + "-" + strMonth + "-01"));
            String date = simpleDateFormat.format(calendar.getTime());
            if (calendar.get(Calendar.DAY_OF_WEEK) - 1 >= 1 && calendar.get(Calendar.DAY_OF_WEEK) - 1 <= 3) {
                return getWeekNum(date);
            }

            while (!inThisMonthAndIsMonday(year, month, date)) {
                calendar.add(Calendar.DAY_OF_WEEK, 1);
                date = simpleDateFormat.format(calendar.getTime());
            }
            weekNum = getWeekNum(date);
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return -1;
        }
        return weekNum;
    }

    /**
     * 验证是否同一天
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameDate(Date date1, Date date2) {
        calendar.setTime(date1);
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date2);
        return ((calendar.get(Calendar.YEAR) == calendar1.get(Calendar.YEAR))
                && calendar.get(Calendar.MONTH) == calendar1.get(Calendar.MONTH)
                && calendar.get(Calendar.DAY_OF_MONTH) == calendar1.get(Calendar.DAY_OF_MONTH));
    }

    /**
     * 判断一个月有几周
     *
     * @return
     */
    public static int weeksOfMonth(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            Date s = sdf.parse(date);
            Calendar ca = Calendar.getInstance();
            ca.setTime(s);
            ca.setFirstDayOfWeek(Calendar.WEDNESDAY);
            return ca.getActualMaximum(Calendar.WEEK_OF_MONTH) - 1;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 判断目标日期是否在日期段内
     *
     * @param time
     * @param from
     * @param end
     * @return
     * @throws DataException
     */
    public static boolean isBetween(String time, String from, String end) throws DataException {
        if (time == null || from == null || end == null) {
            throw new DataException("日期检测数据不可以为空！");
        }
        if (time.equals(from) || time.equals(end)) {
            return true;
        }
        //映射入参
        Date t, f, e;
        try {
            t = simpleDateFormat.parse(time);
            f = simpleDateFormat.parse(from);
            e = simpleDateFormat.parse(end);
            return t.after(f) && t.before(e);
        } catch (ParseException e1) {
            logger.severe(e1.getMessage() + "日期解析出错,目标时间：" + time + "，起止时间：" + from + "，终止时间：" + end);
            throw new DataException("日期解析出错！");
        }
    }

    /**
     * 获取七天后的日期
     *
     * @param date
     * @return
     */
    public static String get7daysAfter(String date) {
        try {
            calendar.setTime(simpleDateFormat.parse(date));
            calendar.add(Calendar.WEEK_OF_MONTH, 1);
            return simpleDateFormat.format(calendar.getTime());
        } catch (ParseException e) {
            logger.severe("解析日期出错，出错日期为" + date);
            return null;
        }
    }
}
