package validator;

import load.DataProcessor;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Tao.Jiang on 2017/11/14.
 */
public class BaseSalaryValidatorTest {
    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    @Test
    public void testIsLegal(){
        boolean actual = false;
        actual = BaseSalaryValidator.isLegal(DataProcessor.baseSalaryList);
        boolean expect = true;
        Assert.assertSame(expect,actual);
    }
}
