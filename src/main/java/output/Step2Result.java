package output;

import constant.Constant;
import exception.DataException;
import load.DataProcessor;
import load.Step2DataProcessor;
import utils.LoadConfigUtil;
import utils.LogUtil;

import java.text.DecimalFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Logger;

/**
 * Created on 2017/12/28.
 */
public class Step2Result {
    private static final Logger logger = LogUtil.setLoggerHanlder(Logger.getLogger(LogUtil.MY_LOGGER), LogUtil.OUTPUTPATH);
    private static final String MANAGECOST_DEV = "管理费用-研发费用";
    private static final String SUMMARY = "计提" + LoadConfigUtil.getMonth() + "月";
    private static final String CURRENCY = "人民币";
    private static final DecimalFormat df = new DecimalFormat("######0.00");
    private static final Double ZERO = 0.0;
    private static final String PRO_CODE = "97";

    public static String[][] getOutputData() {
        Map<String, Double> subjDeptProjsCost = getSubjDeptProjCost();
        Map<String, Double> subjLoanCost = getSubjLoanCost();
        if (subjDeptProjsCost == null || subjLoanCost == null) {
            return null;
        }
        int rows = subjDeptProjsCost.size() + subjLoanCost.size() + 1;
        int cols = Constant.SheetColumnNum.STEP2_RESULT;
        String[][] result = new String[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = "";
            }
        }
        result[0] = Constant.SheetTitleArray.STEP2_RESULT;
        int index = 1;
        //借方金额，以科目代码+项目编号+部门编号为主键
        for (Entry<String, Double> entry : subjDeptProjsCost.entrySet()) {
            String[] tmp = entry.getKey().split(",");
            String subjCode = tmp[0];
            String dept = tmp[1];
            String proj = tmp[2];
            String costName = tmp[3];
            result[index][10] = subjCode;
            result[index][11] = SUMMARY + costName;
            result[index][15] = CURRENCY;
            result[index][22] = df.format(entry.getValue());
            result[index][23] = df.format(ZERO);
            try {
                result[index][24] = Step2DataProcessor.getDeptId(dept);
            } catch (DataException e) {
                logger.severe(e.getMessage());
                return null;
            }
            result[index][28] = PRO_CODE;
            result[index][29] = convertProj(proj);
            index++;
        }
        for (Entry<String, Double> entry : subjLoanCost.entrySet()) {
            String[] tmp = entry.getKey().split(",");
            String subjCode = tmp[0];
            String costName = tmp[1];
            result[index][10] = subjCode;
            result[index][11] = SUMMARY + costName;
            result[index][15] = CURRENCY;
            result[index][22] = df.format(ZERO);
            result[index][23] = df.format(entry.getValue());
            index++;
        }
        return result;
    }

    /**
     * 获取以项目编码和费用归属组成的key，以各项费用数组组成value的map
     *
     * @return
     */
    private static Map<String, Double[]> getDeptProjCostMap() {
        //获取合并表的数据
        String[][] combinedState = CombinedStatement.getOutputData();
        if (combinedState == null) {
            logger.severe("合并表数据出错！");
            return null;
        }
        Map<String, Double[]> deptProjCostMap = new LinkedHashMap<>();
        Double[] costDetail = new Double[8];
        //按照项目编码和费用归属部门组合成key值
        //如果map中已经存在该key则将细节数据加起来
        //如果没有该key，则存储该映射
        for (int i = 1; i < combinedState.length; i++) {
            String[] strings = combinedState[i];
            //strings[3] 项目编码、strings[11]费用归属、strings[12]费用性质
            String deptProj = strings[3] + "," + strings[11] + "," + strings[12];
            for (int j = 0; j < costDetail.length; j++) {
                costDetail[j] = Double.valueOf(strings[j + 13]);
            }
            Double[] tmp = new Double[costDetail.length];
            for (int j = 0; j < tmp.length; j++) {
                tmp[j] = costDetail[j];
            }
            if (!deptProjCostMap.containsKey(deptProj)) {
                deptProjCostMap.put(deptProj, tmp);
            } else {
                Double[] oldCostDetail = deptProjCostMap.get(deptProj);
                for (int j = 0; j < tmp.length; j++) {
                    tmp[j] = tmp[j] + oldCostDetail[j];
                }
                deptProjCostMap.put(deptProj, tmp);
            }
        }
        return deptProjCostMap;
    }

    /**
     * 获取科目编码+项目编号+费用归属+费用性质为key,借贷金额为value的map
     *
     * @return
     */
    private static Map<String, Double> getSubjDeptProjCost() {
        Map<String, Double[]> deptProjCostMap = getDeptProjCostMap();
        Map<String, String> workType = DataProcessor.workTypeMap;
        if (deptProjCostMap == null || deptProjCostMap.size() == 0) {
            return null;
        }
        Map<String, Double> subjDeptProjsCost = new LinkedHashMap<>();
        String[] costNames = Step2DataProcessor.costName;
        for (Entry<String, Double[]> entry : deptProjCostMap.entrySet()) {
            String proj = entry.getKey().split(",")[0];
            String dept = entry.getKey().split(",")[1];
            String costProp = entry.getKey().split(",")[2];
            boolean devDept;
            try {
                devDept = Step2DataProcessor.isDevDept(dept);
            } catch (DataException e) {
                logger.severe(e.getMessage());
                return null;
            }
            try {
                if (Step2DataProcessor.isDevProj(proj) && devDept) {
                    costProp = MANAGECOST_DEV;
                }
            } catch (DataException e) {
                return null;
            }

            Double[] costDetail = entry.getValue();
            //费用名称，科目名称、科目编码、科目编码+费用归属+项目编号
            String costName, subjName, subjCode, subjDeptProj;
            for (int i = 0; i < costDetail.length; i++) {
                //如果金额为0则不计入结果
                if (costDetail[i] == 0.0) {
                    continue;
                }
                costName = costNames[i];
                subjName = costProp + "-" + costName;
                try {
                    subjCode = Step2DataProcessor.getSubjectCode(subjName);
                } catch (DataException e) {
                    logger.severe(e.getMessage());
                    return null;
                }

                if (workType.containsKey(proj)) {
                    proj = workType.get(proj);
                }
                subjDeptProj = subjCode + "," + dept + "," + proj + "," + costName;
                if (subjDeptProjsCost.containsKey(subjDeptProj)) {
                    Double tmp = subjDeptProjsCost.get(subjDeptProj) + costDetail[i];
                    subjDeptProjsCost.put(subjDeptProj, tmp);
                } else {
                    subjDeptProjsCost.put(subjDeptProj, costDetail[i]);
                }

            }
        }
        return subjDeptProjsCost;
    }

    /**
     * 获取贷方金额汇总
     *
     * @return
     */
    private static Map<String, Double> getSubjLoanCost() {
        String[] costNames = Step2DataProcessor.costName;
        Double[] loanCost = new Double[8];
        for (int i = 0; i < loanCost.length; i++) {
            loanCost[i] = 0.00;
        }
        Map<String, Double[]> deptProjCostMap = getDeptProjCostMap();
        Map<String, Double> subjLoanCost = new LinkedHashMap<>();
        if (deptProjCostMap == null || deptProjCostMap.size() == 0) {
            return null;
        }
        for (Map.Entry<String, Double[]> entry : deptProjCostMap.entrySet()) {
            Double[] costDetail = entry.getValue();
            for (int i = 0; i < 8; i++) {
                loanCost[i] += costDetail[i];
            }
        }
        //工资
        if (loanCost[0] != 0.0) {
            subjLoanCost.put("221101," + costNames[0], loanCost[0]);
        }
        //养老
        if (loanCost[1] != 0.0) {
            subjLoanCost.put("221106," + costNames[1], loanCost[1]);
        }
        //失业
        if (loanCost[2] != 0.0) {
            subjLoanCost.put("221108," + costNames[2], loanCost[2]);
        }
        //工伤
        if (loanCost[3] != 0.0) {
            subjLoanCost.put("221109," + costNames[3], loanCost[3]);
        }
        //生育
        if (loanCost[4] != 0.0) {
            subjLoanCost.put("221110," + costNames[4], loanCost[4]);
        }
        //医疗
        if (loanCost[5] != 0.0) {
            subjLoanCost.put("221107," + costNames[5], loanCost[5]);
        }
        //住房公积金
        if (loanCost[6] != 0.0) {
            subjLoanCost.put("221111," + costNames[6], loanCost[6]);
        }
        //补充公积金
        if (loanCost[7] != 0.0) {
            subjLoanCost.put("221118," + costNames[7], loanCost[7]);
        }
        return subjLoanCost;
    }

    /**
     * 转换项目编码
     *
     * @param string
     * @return
     */
    private static String convertProj(String string) {
        if (string == null) {
            return null;
        }
        char[] result = string.toCharArray();
        if (string.equals("A")) {
            return "001";
        }
        for (int i = 0; i < result.length; i++) {
            if (result[i] == '&') {
                result[i] = '+';
            }
        }
        return new String(result);
    }
}
