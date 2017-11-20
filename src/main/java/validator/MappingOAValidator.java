package validator;

import entity.MappingOA;
import load.DataProcessor;
import utils.LogUtil;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;


/**
 * 验证MappingOA表
 * Created by Li.Hou on 2017/11/9.
 */
public class MappingOAValidator{

    private static final Logger logger  = LogUtil.setLoggerHanlder(Logger.getLogger(LogUtil.MY_LOGGER),LogUtil.OUTPUTPATH);
    /**
     * 验证mappingOA表是否符合规范
     * @param mappingOAList
     * @return
     */
    public static boolean isLegal(List<MappingOA> mappingOAList) {
        Map<String, String> employeeIdNameMap = DataProcessor.employeeIdNameMap;
        if (mappingOAList.size() != employeeIdNameMap.size()) {
           logger.severe("mappingOA表数据量与idName表不符");
            return false;
        }
        for (MappingOA mappingOA:
                mappingOAList) {
            //判断是否存在于idNameMap中
            if (!employeeIdNameMap.containsKey(mappingOA.getEmpolyeeId())) {
               logger.severe("mappingOA表中数据与IDName表不符！雇员id："+mappingOA.getEmpolyeeId());
                return false;
            }
            int nameLength = mappingOA.getEmpolyeeName().length();
            String splitName = mappingOA.getOAEmployeeInfo().substring(0,nameLength);
            //验证姓名和OA员工信息表的前缀是否相同
            if (!mappingOA.getEmpolyeeName().equals(splitName)) {
               logger.severe("姓名与OA员工信息表不符！雇员id："+mappingOA.getEmpolyeeId());
                return false;
            }
            //验证MappinOA表中姓名与idName表中姓名相符
            splitName = employeeIdNameMap.get(mappingOA.getEmpolyeeId()).substring(0,nameLength);
            if (!mappingOA.getEmpolyeeName().equals(splitName)) {
               logger.severe("mappingOA表中数据与IDName表不符！雇员id："+mappingOA.getEmpolyeeId());
                return false;
            }
        }
        logger.info("mappingOA表检验通过！");
        return true;
    }

}
