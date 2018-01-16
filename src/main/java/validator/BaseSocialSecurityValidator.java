package validator;

import constant.Constant;
import entity.ErrorRecord;
import entity.MappingOA;
import load.DataProcessor;
import utils.LoadConfigUtil;
import utils.LogUtil;

import java.util.List;
import java.util.logging.Logger;

/**
 * 基本社保表验证器
 * Created by Li.Hou on 2017/11/14.
 */
public class BaseSocialSecurityValidator {
    private static final Logger logger = LogUtil.setLoggerHanlder(Logger.getLogger(LogUtil.MY_LOGGER), LogUtil.OUTPUTPATH);

    public static boolean isLegal(String[][] baseSocialSecurityData) {
        List<MappingOA> mappingOAList = DataProcessor.mappingOAList;
        if (baseSocialSecurityData == null) {
            logger.severe("基本社保数据为空！");
            return false;
        }
        if (baseSocialSecurityData.length != mappingOAList.size()) {
            logger.severe("基本社保表数据量与MappingOA表不符！");
            return false;
        }
        for (String[] str : baseSocialSecurityData) {
            MappingOA mappingOA = DataProcessor.getMappingOAById(str[0]);
            if (mappingOA == null || !mappingOA.getEmpolyeeName().equals(str[1])) {
                String logInfo = "mappingOA表中没有该人数据！员工id为"+str[0];
                logger.severe(logInfo);
                String employee = str[1];
                String employeeId = str[0];
                ErrorRecord record = new ErrorRecord(LogUtil.stage, Constant.LogLevel.ERROR.toString(),employee,employeeId,logInfo);
                LogUtil.errorRecords.add(record);

                return false;
            }
            for (int i = 3; i < 22; i++) {
                if (str[i] == null||str[i].equals("")) {
                    String logInfo = "基本社保表数据出现空值！雇员id为"+str[0]+"第"+(i+1)+"列数据为空";
                    logger.severe(logInfo);
                    String employee = str[1];
                    String employeeId = str[0];
                    ErrorRecord record = new ErrorRecord(LogUtil.stage, Constant.LogLevel.ERROR.toString(),employee,employeeId,logInfo);
                    LogUtil.errorRecords.add(record);
                    return false;
                }
                try {
                    double tmp = Double.valueOf(str[i]);
                    if (tmp < 0) {
                        String logInfo = "基本社保表中数据出现负值!雇员id为"+str[0]+"第"+(i+1)+"列数据为负";
                        logger.severe(logInfo);
                        String employee = str[1];
                        String employeeId = str[0];
                        ErrorRecord record = new ErrorRecord(LogUtil.stage, Constant.LogLevel.ERROR.toString(),employee,employeeId,logInfo);
                        LogUtil.errorRecords.add(record);
                        return false;
                }
                } catch (NumberFormatException e) {
                    String logInfo ="该字符串无法解析为数据"+e.getMessage()+",雇员id:"+str[0]+"第"+(i+1)+"列数据无法解析";
                    logger.severe(logInfo);
                    String employee = str[1];
                    String employeeId = str[0];
                    ErrorRecord record = new ErrorRecord(LogUtil.stage, Constant.LogLevel.ERROR.toString(),employee,employeeId,logInfo);
                    LogUtil.errorRecords.add(record);
                    return false;
                }

            }
            if (!str[22].equals(LoadConfigUtil.getYear() + "." + LoadConfigUtil.getMonth())) {
                logger.severe("基本社保表中年月与json中不符！");
                return false;
            }
        }
        logger.info("基本社保表检验通过！");
        return true;
    }
}
