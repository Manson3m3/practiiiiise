package output;

import constant.Constant;
import utils.LogUtil;

import java.util.logging.Logger;

/**
 * 成本汇总表
 * Created by Li.Hou on 2017/11/13.
 */
public class CostSummary {
    private static final Logger logger = LogUtil.setLoggerHanlder(Logger.getLogger(LogUtil.MY_LOGGER), LogUtil.OUTPUTPATH);
    private static final String[][] data = getOutputData();
    public static String[][] getOutputData() {
        String[][] xMonthCost = XMonthCostSummary.getOutputData();
        if (xMonthCost == null) {
            logger.severe("月成本表数据为空！");
            return null;
        }
        int rows = xMonthCost.length;
        String[][] result = new String[rows][Constant.SheetColumnNum.COST];
        result[0] = Constant.SheetTitleArray.COST;
        for (int i = 1; i < rows; i++) {
            System.arraycopy(xMonthCost[i], 0, result[i], 1, 5);
            result[i][0] = result[i][3];
            System.arraycopy(xMonthCost[i], 5, result[i], 6, 11);
            result[i][17] = xMonthCost[i][17];
        }
        return result;
    }

    /**
     * 根据id获取一条数据
     *
     * @param employeeId
     * @return
     */
    public static String[] getOneCostDataById(String employeeId) {
        if (employeeId == null) {
            return null;
        }
        if (data == null) {
            return null;
        }
        for (String[] str : data) {
            if (employeeId.equals(str[1])) {
                return str;
            }
        }
        return null;
    }
}
