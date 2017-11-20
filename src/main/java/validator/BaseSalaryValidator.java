package validator;

import entity.BaseSalary;
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

    private static final Logger logger  = LogUtil.setLoggerHanlder(Logger.getLogger(LogUtil.MY_LOGGER),LogUtil.OUTPUTPATH);
    public static boolean isLegal(List<BaseSalary> baseSalaryList) {
        if (baseSalaryList == null || baseSalaryList.isEmpty()) {
            return false;
        }
        List<MappingOA> mappingOAList = DataProcessor.mappingOAList;
        if (!MappingOAValidator.isLegal(mappingOAList)) {
           logger.severe("MappingOA表不合规格！");
            return false;
        }
        if (baseSalaryList.size() != mappingOAList.size()) {
           logger.severe("基本工资表和mappingOA表数据量不符!");
            return false;
        }
        for (BaseSalary b:baseSalaryList) {
            MappingOA mappingOA = DataProcessor.getMappingOAById(b.getEmployeeId());
            if (mappingOA == null || !mappingOA.getEmpolyeeName().equals(b.getEmployeeName())) {
               logger.severe("基本工资表信息与mappingOA表信息不符！");
                return false;
            }
            if (b.getBaseSalary() < 0.00) {
               logger.severe("基本工资数据出错，出错id为"+b.getEmployeeId());
                return false;
            }
        }
        logger.info("基本工资表检验通过！");
        return true;
    }
}
