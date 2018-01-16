package validator;

import constant.Constant;
import entity.ErrorRecord;
import exception.DataException;
import load.DataProcessor;
import utils.CalendarUtil;
import utils.LoadConfigUtil;
import utils.LogUtil;

import java.text.ParseException;
import java.util.*;
import java.util.logging.Logger;


/**
 * 验证工时月报表
 * Created by Li.Hou on 2017/11/9.
 */
public class WorkHoursMonthValidator {
    private static final Logger logger = LogUtil.setLoggerHanlder(Logger.getLogger(LogUtil.MY_LOGGER), LogUtil.OUTPUTPATH);

    private static final double WEEKLY_WORK_HOURS = 40.00;

    private static int year = Integer.parseInt(LoadConfigUtil.getYear());
    //获取期始日期
    private static String startDate = LoadConfigUtil.getStartDate();
    //检测周数，最低是1
    private static int weekNums = Integer.parseInt(LoadConfigUtil.getWeekNum());

    /**
     * 验证工时月报表
     *
     * @param
     * @return
     */
    public static boolean isLegal() {
        //经过日期筛选后的月报表数据
        String[][] workHours = getTargetData();
        if (workHours == null) {
            return false;
        }
        //获取新员工标识
        String rookieId = LoadConfigUtil.getNewStaff();
        if (!isWeeksLegal(workHours)) {
            return false;
        }
        //个人周工时汇总
        Map<String, Double> nameDateProjectTotalMap = new HashMap<>();
        //姓名单号汇总
        Map<String, String> nameDateListIdMap = new HashMap<>();
        //员工项目编号汇总
        Map<String, ArrayList<String>> nameProjMap = new HashMap<>();
        //员工姓名集合
        Set<String> employeeSet = new HashSet<>();
        try {
            for (String[] strings : workHours) {
                //验证工作期始
                if (!(CalendarUtil.isMonday(strings[2]))) {
                    String logInfo = "工作期始不是周一！数据信息为，姓名" + strings[0] + " 申请日期"
                            + strings[2] + "项目编号" + strings[4];
                    logger.severe(logInfo);
                    String employeeName = strings[0];
                    ErrorRecord record = new ErrorRecord(LogUtil.stage, Constant.LogLevel.ERROR.toString(), employeeName, DataProcessor.getIdbyName(employeeName), logInfo);
                    LogUtil.errorRecords.add(record);
                    return false;
                }
                //验证工作期止
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(CalendarUtil.simpleDateFormat.parse(strings[2]));
                calendar.add(Calendar.DAY_OF_WEEK, CalendarUtil.WEEKNUMBERS - 1);
                if (!(CalendarUtil.isSameDate(CalendarUtil.simpleDateFormat.parse(strings[3]), calendar.getTime()))) {
                    String logInfo = "工作期止不符！数据信息为，姓名" + strings[0] + " 申请日期"
                            + strings[2] + "项目编号" + strings[4];
                    logger.severe(logInfo);
                    String employeeName = strings[0];
                    ErrorRecord record = new ErrorRecord(LogUtil.stage, Constant.LogLevel.ERROR.toString(), employeeName, DataProcessor.getIdbyName(employeeName), logInfo);
                    LogUtil.errorRecords.add(record);
                    return false;
                }
                //验证工时和单号
                String nameDate = strings[0] + "," + strings[2];
                if (nameDateProjectTotalMap.containsKey(nameDate)) {
                    nameDateProjectTotalMap.put(nameDate, nameDateProjectTotalMap.get(nameDate) + Double.parseDouble(strings[5]));
                } else {
                    nameDateProjectTotalMap.put(nameDate, Double.parseDouble(strings[5]));
                }
                if (nameDateListIdMap.containsKey(nameDate) && !nameDateListIdMap.get(nameDate).equals(strings[6])) {
                    String logInfo = "该员工同一周内单号不同！数据信息为，姓名" + strings[0] + " 申请日期"
                            + strings[2];
                    logger.severe(logInfo);
                    String employeeName = strings[0];
                    ErrorRecord record = new ErrorRecord(LogUtil.stage, Constant.LogLevel.ERROR.toString(), employeeName, DataProcessor.getIdbyName(employeeName), logInfo);
                    LogUtil.errorRecords.add(record);
                    return false;
                }
                nameDateListIdMap.put(nameDate, strings[6]);
                //添加员工及其项目到集合中
                employeeSet.add(strings[0]);
                if (nameProjMap.containsKey(strings[0])) {
                    nameProjMap.get(strings[0]).add(strings[2] + "," + strings[4]);
                } else {
                    ArrayList<String> arrayList = new ArrayList<>();
                    arrayList.add(strings[2] + "," + strings[4]);
                    nameProjMap.put(strings[0], arrayList);
                }
            }
            //判断是否为新员工
            for (Map.Entry<String, ArrayList<String>> entry : nameProjMap.entrySet()) {
                ArrayList<String> list = entry.getValue();
                for (int i = 0; i < list.size(); i++) {
                    String s = list.get(i);
                    //起始日期
                    String tmp = s.split(",")[0];
                    //项目编号
                    String proj = s.split(",")[1];
                    if (proj.equals(rookieId)) {
                        for (int j = 0; j <= i; j++) {
                            if (!list.get(j).split(",")[1].equals(rookieId) && !list.get(j).split(",")[0].equals(tmp)) {
                                for (int k = list.size() - 1; k > i; k--) {
                                    if (!list.get(k).split(",")[1].equals(rookieId) && !list.get(j).split(",")[0].equals(tmp)) {
                                        String logInfo = "该员工项目编号为新员工的日期不正确！员工名称为：" + entry.getKey() + ",项目申请时间为" + tmp + "项目编号为" + proj;
                                        logger.severe(logInfo);
                                        //加入errorRecord
                                        LogUtil.errorRecords.add(new ErrorRecord(LogUtil.stage, Constant.LogLevel.ERROR.toString(), entry.getKey(), DataProcessor.getIdbyName(entry.getKey()), logInfo));
                                        return false;
                                    }
                                }
                            } else {
                                String logInfo = "该员工为新员工！数据信息为:姓名" + entry.getKey() + " 申请日期"
                                        + tmp + "项目编号" + proj;
                                logger.config(logInfo);
                                String employeeName = entry.getKey();
                                ErrorRecord record = new ErrorRecord(LogUtil.stage, Constant.LogLevel.WARNING.toString(), employeeName, DataProcessor.getIdbyName(employeeName), logInfo);
                                LogUtil.errorRecords.add(record);
                                break;
                            }
                        }
                    }
                }
            }
            for (Map.Entry<String, Double> entry :
                    nameDateProjectTotalMap.entrySet()) {
                if (entry.getValue() != WEEKLY_WORK_HOURS) {
                    String logInfo = "个人周工时不符，数据信息为，姓名时间为" + entry.getKey() + " 周工时"
                            + entry.getValue();
                    logger.severe(logInfo);
                    ErrorRecord record = new ErrorRecord(LogUtil.stage, Constant.LogLevel.ERROR.toString(), null, null, logInfo);
                    LogUtil.errorRecords.add(record);
                    return false;
                }
            }
            HashSet<String> hashSet = new HashSet<>(DataProcessor.employeeIdNameMap.values());
            if (hashSet.removeAll(employeeSet) && hashSet.size() != 0) {
                logger.severe("工时月报表缺少idName映射表里部分员工数据！员工姓名为：" + DataProcessor.setToStrings(hashSet));
                return false;
            }

        } catch (ParseException e) {
            logger.severe(e.getMessage());
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 检验周数是否正确
     *
     * @param
     * @return
     */
    private static boolean isWeeksLegal(String[][] workHours) {
        if (workHours == null) {
            return false;
        }
        //构造以员工姓名为key，工作日起始集合为value的map
        Map<String, HashSet<String>> nameStartDateMap = new LinkedHashMap<>();
        for (int i = 0; i < workHours.length; i++) {
            String[] strings = workHours[i];
            if (nameStartDateMap.containsKey(strings[0])) {
                nameStartDateMap.get(strings[0]).add(strings[2]);
            } else {
                HashSet<String> set = new HashSet<>();
                set.add(strings[2]);
                nameStartDateMap.put(strings[0], set);
            }
        }
        if (nameStartDateMap.size() == 0) {
            logger.severe("员工工作期始集map为空！");
            return false;
        }
        //获取验证日期集合
        HashSet<String> validatorSet = getValidatorWeeks(startDate, getEndDate());
        if (validatorSet == null) {
            logger.severe("计划验证日期集合为空！");
            return false;
        }
        for (Map.Entry<String, HashSet<String>> entry : nameStartDateMap.entrySet()) {
            HashSet<String> tmp = new HashSet<>(validatorSet);
            tmp.removeAll(entry.getValue());
            //存在缺少周
            if (tmp.size() != 0) {
                String missDates = DataProcessor.setToStrings(tmp);
                String logInfo = "工时月报表中" + entry.getKey() + "缺少起始日期：" + missDates;
                logger.config(logInfo);
                ErrorRecord record = new ErrorRecord(LogUtil.stage, Constant.LogLevel.WARNING.toString(), entry.getKey(), DataProcessor.getIdbyName(entry.getKey()), logInfo);
                LogUtil.errorRecords.add(record);
            }
        }
        return true;
    }

    /**
     * 获取目标时间段的数据
     *
     * @return
     */
    private static String[][] getTargetData() {

        //原始工时月报表数据
        String[][] workReports = DataProcessor.workHoursMonthData;
        //验证工时数据的起始和截止日期
        String endDate = getEndDate();
        if (workReports == null) {
            logger.severe("工时月报表为空！");
            return null;
        }

        //统计符合验证时间内的数据
        ArrayList<String[]> arrayList = new ArrayList<>();
        for (int i = 0; i < workReports.length; i++) {
            String[] strings = workReports[i];

            //验证idName表映射
            String employeeId = DataProcessor.getIdbyName(strings[0]);
            if (employeeId == null) {
                String logInfo = "月报表员工与IdName映射表不符，数据信息为，姓名" + strings[0] + " 申请日期"
                        + strings[2] + "项目编号" + strings[4];
                logger.severe(logInfo);
                String employee = strings[0];
                ErrorRecord record = new ErrorRecord(LogUtil.stage, Constant.LogLevel.ERROR.toString(), employee, DataProcessor.getIdbyName(employee), logInfo);
                LogUtil.errorRecords.add(record);
                return null;
            }
            boolean inPeriod;
            try {
                inPeriod = CalendarUtil.isBetween(strings[2], startDate, endDate);
            } catch (DataException e) {
                return null;
            }
            //判断该数据是否在规定时间段内
            if (!inPeriod) {
                String logInfo = "工时月报表该数据不在验证日期内，数据信息为，姓名" + strings[0] + " 申请日期"
                        + strings[2] + "项目编号" + strings[4];
                logger.config(logInfo);
                String employee = strings[0];
                ErrorRecord record = new ErrorRecord(LogUtil.stage, Constant.LogLevel.WARNING.toString(), employee, DataProcessor.getIdbyName(employee), logInfo);
                LogUtil.errorRecords.add(record);
                continue;
            }
            //将符合时间段的数据加入目标数据结构中
            arrayList.add(strings);
        }
        String[][] result = new String[arrayList.size()][Constant.SheetColumnNum.WORKING_HOURS_MONTH_REPORT_COL_NUM];
        for (int i = 0; i < arrayList.size(); i++) {
            result[i] = arrayList.get(i);
        }
        return result;

    }

    /**
     * 获取验证截止日期
     *
     * @return
     */
    private static String getEndDate() {

        //获取该年对应周数表
        String[][] weeks = CalendarUtil.getWeekNums(year);
        if (weeks == null) {
            logger.severe(year + "年对应周数表为空！");
            return null;
        }
        try {
            if (CalendarUtil.simpleDateFormat.parse(startDate).after(new Date())) {
                logger.severe("验证起始日期不可晚于当前日期");
                return null;
            }
        } catch (ParseException p) {
            logger.severe("起始日期无法解析，起始日期为：" + startDate);
            return null;
        }
        int startWeek = CalendarUtil.getWeekNum(startDate);
        //获取该年最后一周
        int lastWeek = weeks.length - 1;
        if (startWeek < 1 || startWeek > lastWeek) {
            logger.severe("工时月报表检测,起始周数不合规范！");
        }
        String endDate;
        if (weekNums < 1) {
            logger.severe("验证周数不能小于1！");
            return null;
        }
        if (startWeek + weekNums - 1 > lastWeek) {
            String[][] nextYearWeeks = CalendarUtil.getWeekNums(year + 1);
            if (nextYearWeeks == null) {
                return null;
            }
            int offset = startWeek + weekNums - 1 - lastWeek;
            endDate = nextYearWeeks[offset][0];
        } else {
            endDate = weeks[startWeek + weekNums - 1][0];
        }
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(CalendarUtil.simpleDateFormat.parse(endDate));
        } catch (ParseException e) {
            logger.severe("验证截至日期解析出错，日期为：" + endDate);
            return null;
        }
        calendar.add(Calendar.DAY_OF_WEEK, 6);
        endDate = CalendarUtil.simpleDateFormat.format(calendar.getTime());
        return endDate;
    }

    /**
     * 获取验证日期集合
     *
     * @param startDate
     * @param endDate
     * @return
     */
    private static HashSet<String> getValidatorWeeks(String startDate, String endDate) {
        boolean isMonday;
        try {
            isMonday = CalendarUtil.isMonday(startDate);
        } catch (Exception e) {
            logger.severe("期始日期解析出错！");
            return null;
        }
        if (!isMonday) {
            logger.severe("起始日期不是周一！");
            return null;
        }
        HashSet<String> stringHashSet = new HashSet<>();
        String date = startDate;
        try {
            do {
                stringHashSet.add(date);
                date = CalendarUtil.get7daysAfter(date);
            } while (CalendarUtil.simpleDateFormat.parse(date).before(CalendarUtil.simpleDateFormat.parse(endDate)));
        } catch (ParseException e) {
            logger.severe("日期解析出错！出错日期为：" + date);
            return null;
        }
        return stringHashSet;

    }

}
