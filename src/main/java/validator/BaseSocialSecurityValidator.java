package validator;

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
        if (!MappingOAValidator.isLegal(mappingOAList)) {
            logger.severe("mappingOA表不合规格！");
            return false;
        }

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
                logger.severe("mappingOA表中没有该人数据！");
                return false;
            }
            for (int i = 3; i < 22; i++) {
                if (str[i] == null||str[i].equals("")) {
                    logger.severe("基本社保表数据出现空值！雇员id为"+str[0]);
                    return false;
                }
                try {
                    double tmp = Double.valueOf(str[i]);
                    if (tmp < 0) {
                        logger.severe("基本社保表中数据出现负值!");
                        return false;
                }
                } catch (NumberFormatException e) {
                    logger.severe("该字符串无法解析为数据"+e.getMessage()+",雇员id:"+str[0]);
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
