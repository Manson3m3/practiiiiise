package output;

import constant.Constant;
import entity.BaseSalary;
import entity.MappingOA;
import load.DataProcessor;
import utils.LogUtil;
import validator.BaseSalaryValidator;
import validator.BaseSocialSecurityValidator;
import validator.MappingOAValidator;

import java.util.List;
import java.util.logging.Logger;

/**
 * X月成本汇总
 * Created by Li.Hou on 2017/11/14.
 */
public class XMonthCostSummary {


    public static String[][] getOutputData() {
        List<MappingOA> mappingOAList = DataProcessor.mappingOAList;

        int rows = mappingOAList.size() + 1;
        String[][] result = new String[rows][Constant.SheetColumnNum.XX_MONTH_COST];
        result[0] = Constant.SheetTitleArray.XX_MONTH_COST;
        String[][] mappingOAData = DataProcessor.mappingOAData;
        for (int i = 1; i < rows; i++) {
            System.arraycopy(mappingOAData[i - 1], 0, result[i], 0, Constant.SheetColumnNum.MAPPING_OA_COL_NUM);
            result[i] = getOneRowData(result[i]);
        }

        return result;
    }

    /**
     * 装载除mappingOA数据的其他薪资社保数据
     *
     * @param preData 预先装载好mappingOA数据的数组
     * @return
     */
    private static String[] getOneRowData(String[] preData) {
        //获取薪资
        preData[5] = String.valueOf(DataProcessor.getBaseSalaryById(preData[0]).getBaseSalary());
        //获取一条社保数据
        String[] baseSocialSecurityData = DataProcessor.getBaseSocialSecurityById(preData[0]);
        System.arraycopy(baseSocialSecurityData, 5, preData, 6, 7);
        preData[13] = baseSocialSecurityData[15];
        preData[14] = baseSocialSecurityData[14];
        preData[15] = getSum(preData, 5, 14);
        preData[16] = " ";
        preData[17] = getSum(preData, 13, 14);
        return preData;

    }

    private static String getSum(String[] strings, int start, int end) {
        if (strings == null || end >= strings.length) {
            return null;
        }
        double sum = 0.00;
        for (int i = start; i <= end; i++) {
            double tmp = Double.valueOf(strings[i]);
            sum += tmp;
        }
        return String.valueOf(sum);
    }
}
