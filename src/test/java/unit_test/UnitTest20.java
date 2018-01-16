package unit_test;

import constant.Constant;
import load.DataProcessor;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import utils.LogUtil;
import validator.BaseSalaryValidator;
import validator.BaseSocialSecurityValidator;
import validator.MappingOAValidator;
import validator.WorkHoursMonthValidator;

import java.util.logging.Logger;

/**
 * Created by Tao.Jiang on 2017/11/21.
 */
public class UnitTest20 {
    private static final Logger logger = LogUtil.setLoggerHanlder(Logger.getLogger(LogUtil.MY_LOGGER), LogUtil.OUTPUTPATH);

    @Before
    public void before() {

    }

    @After
    public void after() {

    }

    @Test
    public void unitTest20() {
        logger.info("正在测试第20个测试文件\n\n\n\n");
        Constant.JsonPath.JSONPATH = "./file" + "/configs" + "/config20.json";
        boolean[] actuals = new boolean[4];
        actuals[0] = BaseSalaryValidator.isLegal(DataProcessor.baseSalaryList);
        actuals[1] = BaseSocialSecurityValidator.isLegal(DataProcessor.baseSocialSecurityData);
        actuals[2] = MappingOAValidator.isLegal(DataProcessor.mappingOAList);
        actuals[3] = WorkHoursMonthValidator.isLegal();
        boolean[] expect = {false, false, false, true};
        Assert.assertArrayEquals(expect, actuals);
        logger.info("第20个测试文件测试完毕\n\n\n\n");
    }
}

