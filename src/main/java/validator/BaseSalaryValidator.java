package validator;

import constant.Constant;
import entity.BaseSalary;
import entity.ErrorRecord;
import entity.MappingOA;
import load.DataProcessor;
import utils.LogUtil;

import java.util.List;
import java.util.logging.Logger;

/**
 * 基本薪资表验证器
 * Created by Li.Hou on 2017/11/14.
 */
public class BaseSalaryValidator {

    private static final Logger logger  = LogUtil.setLoggerHanlder(Logger.getLogger(LogUtil.MY_LOGGER), LogUtil.OUTPUTPATH);
    public static boolean isLegal(List<BaseSalary> baseSalaryList) {
        if (baseSalaryList == null || baseSalaryList.isEmpty()) {
            return false;
        }
        List<MappingOA> mappingOAList = DataProcessor.mappingOAList;
        if (baseSalaryList.size() != mappingOAList.size()) {
            logger.severe("基本工资表和mappingOA表员工数据量不符!");
            return false;
        }
        for (BaseSalary b:baseSalaryList) {
            MappingOA mappingOA = DataProcessor.getMappingOAById(b.getEmployeeId());
            if (mappingOA == null || !mappingOA.getEmpolyeeName().equals(b.getEmployeeName())) {
               logger.severe("基本工资表信息与mappingOA表员工名称不符！员工编码为："+b.getEmployeeId());
                return false;
            }
            if (b.getBaseSalary() < 0.00) {
                String logInfo = "基本工资数据出错数值为负，出错员工id为" + b.getEmployeeId();
                String employee = DataProcessor.employeeIdNameMap.get(b.getEmployeeId());
               logger.severe(logInfo);
                ErrorRecord record = new ErrorRecord(LogUtil.stage, Constant.LogLevel.ERROR.toString(),employee,b.getEmployeeId(),logInfo);
                LogUtil.errorRecords.add(record);
                return false;
            }
        }
        logger.info("基本工资表检验通过！");
        return true;
    }
}
