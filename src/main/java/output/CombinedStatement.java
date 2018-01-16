package output;

import constant.Constant;
import load.DataProcessor;
import utils.CalendarUtil;
import utils.LoadConfigUtil;
import utils.LogUtil;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * 合并表
 * Created by Li.Hou on 2017/11/13.
 */
public class CombinedStatement {

    private static final Logger logger = LogUtil.setLoggerHanlder(Logger.getLogger(LogUtil.MY_LOGGER), LogUtil.OUTPUTPATH);
    private static final DecimalFormat df = new DecimalFormat("######0.00");

    public static String[][] getOutputData() {

        //获取工作类型映射
        Map<String, String> workTypeMap = DataProcessor.workTypeMap;
        String[][] workHoursMonthData = addEmployeeIdAndRemoveRookie(DataProcessor.workHoursMonthData);
        int rows = workHoursMonthData.length + 1;
        String[][] result = new String[rows][Constant.SheetColumnNum.COALESCING];
        int year = Integer.valueOf(LoadConfigUtil.getYear());
        int month = Integer.valueOf(LoadConfigUtil.getMonth());
        result[0] = Constant.SheetTitleArray.COALESCING;
        for (int i = 1; i < rows; i++) {
            //年
            result[i][0] = String.valueOf(year);
            //月
            result[i][1] = String.valueOf(month);
            //工作期始
            result[i][2] = workHoursMonthData[i - 1][2];
            //项目编号
            result[i][3] = workHoursMonthData[i - 1][4];
            //项目合计
            result[i][4] = workHoursMonthData[i - 1][5];
            //周数
            result[i][5] = String.valueOf(CalendarUtil.getWeekNum(result[i][2]));
            //员工信息
            result[i][6] = DataProcessor.getMappingOAById(workHoursMonthData[i - 1][7]).getOAEmployeeInfo();
            //状态
            result[i][7] = "done";
            //中智员工号
            result[i][8] = String.valueOf(workHoursMonthData[i - 1][7]);
            //获取单条成本信息
            String[] tmp = CostSummary.getOneCostDataById(result[i][8]);
            if (tmp == null) {
                logger.severe("成本汇总数据与工时月报表数据不关联");
                return null;
            }
            //姓名、OA部门、费用归属、费用性质
            System.arraycopy(tmp, 2, result[i], 9, 4);
            System.arraycopy(tmp, 6, result[i], 13, 12);
            double proportion = getProportion(result[i][4], result[i][8]);
            for (int j = 13; j < 25; j++) {
                double finalData = Double.valueOf(result[i][j]) * proportion;

                result[i][j] = df.format(finalData);
            }
            //Z列,如果工作类型表中存在就使用相应值
            //否则使用项目编号
            if (workTypeMap.containsKey(result[i][3])) {
                result[i][25] = workTypeMap.get(result[i][3]);
            } else {
                result[i][25] = result[i][3];
            }
        }
        return result;
    }

    /**
     * 根据id及项目耗时获取占比
     *
     * @param projectTotal
     * @param employeeId
     * @return
     */
    private static double getProportion(String projectTotal, String employeeId) {
        Map<String, Double> idTotalHourMap = getIdTotalHourMap();
        if (idTotalHourMap == null || idTotalHourMap.isEmpty()) {
            logger.severe("员工编号总工时映射为空！");
            return 0.00;
        }
        double projectHour = Double.valueOf(projectTotal);
        double idTotalHour = idTotalHourMap.get(employeeId);
        return projectHour / idTotalHour;
    }

    /**
     * 工时月报表中id 总工时映射
     *
     * @return
     */
    private static Map<String, Double> getIdTotalHourMap() {
        String[][] workHoursMonthData = addEmployeeIdAndRemoveRookie(DataProcessor.workHoursMonthData);
        if (workHoursMonthData == null) {
            return null;
        }
        Map<String, Double> idTotalHourMap = new HashMap<>();
        for (String[] str : workHoursMonthData) {
            if (!idTotalHourMap.containsKey(str[7])) {
                idTotalHourMap.put(str[7], Double.valueOf(str[5]));
            } else {
                double newValue = idTotalHourMap.get(str[7]) + Double.valueOf(str[5]);
                idTotalHourMap.put(str[7], newValue);
            }
        }
        return idTotalHourMap;
    }

    /**
     * 工时月报表添加一列为编号
     * 并且删除新员工行
     *
     * @param workHoursMonthData
     * @return
     */
    private static String[][] addEmployeeIdAndRemoveRookie(String[][] workHoursMonthData) {
        if (workHoursMonthData == null) {
            return null;
        }
        //新员工项目编号
        String rookieId = LoadConfigUtil.getNewStaff();
        int rookieNum = 0;
        for (String[] str : workHoursMonthData) {

            if (str[4].equals(rookieId)) {
                rookieNum++;
            }
        }
        String[][] strings = new String[workHoursMonthData.length - rookieNum][Constant.SheetColumnNum.WORKING_HOURS_MONTH_REPORT_COL_NUM + 1];
        int tmp = 0;
        for (int i = 0; i < workHoursMonthData.length; i++) {
            if (workHoursMonthData[i][4].equals(rookieId)) {
                tmp++;
            }
            if (!workHoursMonthData[i][4].equals(rookieId)) {
                System.arraycopy(workHoursMonthData[i], 0, strings[i - tmp], 0, Constant.SheetColumnNum.WORKING_HOURS_MONTH_REPORT_COL_NUM);
                strings[i - tmp][Constant.SheetColumnNum.WORKING_HOURS_MONTH_REPORT_COL_NUM] = DataProcessor.getIdbyName(strings[i - tmp][0]);
            }
        }
        return strings;
    }
}
