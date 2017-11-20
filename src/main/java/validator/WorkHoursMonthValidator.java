package validator;

import constant.Constant;
import entity.WorkingHoursMonthReport;
import load.DataProcessor;
import utils.CalendarUtil;
import utils.LoadConfigUtil;
import utils.LogUtil;

import java.util.*;
import java.util.logging.Logger;

import static load.DataProcessor.getIdbyName;


/**
 * 验证工时月报表
 * Created by Li.Hou on 2017/11/9.
 */
public class WorkHoursMonthValidator {
    private static final Logger logger = LogUtil.setLoggerHanlder(Logger.getLogger(LogUtil.MY_LOGGER), LogUtil.OUTPUTPATH);

    private static final double WEEKLY_WORK_HOURS = 40.00;

    /**
     * 验证工时月报表
     *
     * @param workingHoursMonthReportList
     * @return
     */
    public static boolean isLegal(List<WorkingHoursMonthReport> workingHoursMonthReportList) {
        if (workingHoursMonthReportList == null) {
            logger.severe("工时月报表列表为空！");
            return false;
        }
        int year = Integer.parseInt(LoadConfigUtil.getYear());
        int month = Integer.parseInt(LoadConfigUtil.getMonth());
        if (!isWeeksLegal(workingHoursMonthReportList)) {
            return false;
        }
        //个人周工时汇总
        Map<String, Double> nameDateProjectTotalMap = new HashMap<>();
        //姓名单号汇总
        Map<String, String> nameDateListIdMap = new HashMap<>();
        //姓名set
        Set<String> nameSet = new HashSet<>();
        Map<String, String> idNameMap = DataProcessor.employeeIdNameMap;
        try {
            for (WorkingHoursMonthReport workingHoursMonthReport : workingHoursMonthReportList) {
                String employeeId = getIdbyName(workingHoursMonthReport.getApplicant());
                if (employeeId == null) {
                    logger.severe("月报表员工与IdName映射表不符，数据信息为，姓名" + workingHoursMonthReport.getApplicant() + " 申请日期"
                            + workingHoursMonthReport.getStartDate() + "项目编号" + workingHoursMonthReport.getProjectId());
                    return false;
                }

                //验证工作期始
                String startDate = workingHoursMonthReport.getStartDate();
                if (!(CalendarUtil.inThisMonthAndIsMonday(year, month, startDate) && CalendarUtil.isMonday(startDate))) {
                    logger.severe("工作期始不符！数据信息为，姓名" + workingHoursMonthReport.getApplicant() + " 申请日期"
                            + workingHoursMonthReport.getStartDate() + "项目编号" + workingHoursMonthReport.getProjectId());
                    return false;
                }
                //验证工作期止
                String endDate = workingHoursMonthReport.getEndDate();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(CalendarUtil.simpleDateFormat.parse(startDate));
                calendar.add(Calendar.DAY_OF_WEEK, CalendarUtil.WEEKNUMBERS - 1);
                if (!(CalendarUtil.isSameDate(CalendarUtil.simpleDateFormat.parse(endDate), calendar.getTime()))) {
                    logger.severe("工作期止不符！数据信息为，姓名" + workingHoursMonthReport.getApplicant() + " 申请日期"
                            + workingHoursMonthReport.getStartDate() + "项目编号" + workingHoursMonthReport.getProjectId());
                    return false;
                }
                //验证工时和单号
                String nameDate = workingHoursMonthReport.getApplicant() + workingHoursMonthReport.getStartDate();
                if (nameDateProjectTotalMap.containsKey(nameDate)) {
                    nameDateProjectTotalMap.put(nameDate, nameDateProjectTotalMap.get(nameDate) + workingHoursMonthReport.getProjectTotal());
                } else {
                    nameDateProjectTotalMap.put(nameDate, workingHoursMonthReport.getProjectTotal());
                }
                nameDateListIdMap.put(nameDate, workingHoursMonthReport.getListId());
                nameSet.add(workingHoursMonthReport.getApplicant());
            }
            //验证工时月报表数据量
            if (nameSet.size() != idNameMap.size()) {
                logger.severe("工时月报表员工数量与idname映射表数量不符！");
                return false;
            }
            //验证工时和单号
            if (nameDateProjectTotalMap.size() != nameDateListIdMap.size()) {
                logger.severe("工时月报表姓名初始日期项目总计映射数量与姓名日期单号数量不符！");
                return false;
            }
            for (Map.Entry<String, Double> entry :
                    nameDateProjectTotalMap.entrySet()) {
                if (entry.getValue() != WEEKLY_WORK_HOURS) {
                    logger.severe("个人周工时不符，数据信息为，姓名时间为" + entry.getKey() + " 周工时"
                            + entry.getValue());
                    return false;
                }
            }
            if (nameDateListIdMap.keySet().size() != new HashSet<>(nameDateListIdMap.values()).size()) {
                logger.severe("工时月报表单号与姓名工作期始不对应");
                return false;
            }


        } catch (Exception e) {
            logger.severe(e.getMessage());
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 检验周数是否正确
     *
     * @param workingHoursMonthReportList
     * @return
     */
    private static boolean isWeeksLegal(List<WorkingHoursMonthReport> workingHoursMonthReportList) {
        //循环整个列表
        for (int count = 0; count < workingHoursMonthReportList.size(); count++) {
            //获取列表第一条数据的申请人,将该申请人的所有数据放入tempWorkingHoursMonthReportList
            String applicant = workingHoursMonthReportList.get(count).getApplicant();
            List<WorkingHoursMonthReport> tempWorkingHoursMonthReportList = new LinkedList<>();
            for (int i = 0; i < workingHoursMonthReportList.size(); i++) {
                if (workingHoursMonthReportList.get(i).getApplicant().equals(applicant)) {
                    tempWorkingHoursMonthReportList.add(workingHoursMonthReportList.get(i));
                    count++;
                }
            }
            //获取该申请人的所有周数，放入weekNumList
            List<Integer> weekNumList = new LinkedList<>();
            weekNumList.add(CalendarUtil.getWeekNum(tempWorkingHoursMonthReportList.get(0).getStartDate().toString()));
            for (int i = 1; i < tempWorkingHoursMonthReportList.size(); i++) {
                if (weekNumList.contains(CalendarUtil.getWeekNum(tempWorkingHoursMonthReportList.get(i).getStartDate().toString()))) {
                    continue;
                }
                weekNumList.add(CalendarUtil.getWeekNum(tempWorkingHoursMonthReportList.get(i).getStartDate().toString()));
            }
            //找到最小的周数
            int minWeek = weekNumList.get(0);
            for (int i = 0; i < weekNumList.size(); i++) {
                if (minWeek <= weekNumList.get(i)) {
                    continue;
                }
                minWeek = weekNumList.get(i);
            }
            //检验是否有新员工专用的项目编号
            for (WorkingHoursMonthReport workingHoursMonthReport : tempWorkingHoursMonthReportList) {
                if (workingHoursMonthReport.getProjectId().equals(Constant.NewStaff.NEW_STAFF)) {
                    if (CalendarUtil.getWeekNum(workingHoursMonthReport.getStartDate().toString()) != minWeek) {
                        logger.severe("新员工" + workingHoursMonthReport.getApplicant() + "专属项目编号的周数不是最小周");
                        return false;
                    }
                }
            }
            //检验周数是否连续
            for (int i = 1; i < weekNumList.size() - 1; i++) {
                if (!weekNumList.contains(minWeek + i)) {
                    logger.severe("找不到" + " " + tempWorkingHoursMonthReportList.get(0).getApplicant() + " " + (minWeek + i) + " " + "week");
                    return false;
                }
            }
            //检验周数是否足够
            if (weekNumList.size() != CalendarUtil.weeksOfMonth(tempWorkingHoursMonthReportList.get(0).getStartDate())) {
                logger.info(tempWorkingHoursMonthReportList.get(0).getApplicant() + " " + "周数不足");
                return false;
            }
        }
        logger.info("工时月报表周数规范性检验通过");
        return true;
    }
}
