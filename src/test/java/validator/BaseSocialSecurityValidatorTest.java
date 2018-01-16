package validator;

import load.DataProcessor;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Tao.Jiang on 2017/11/15.
 */
public class BaseSocialSecurityValidatorTest {
    @Before
    public void before ()throws Exception{

    }
    @After
    public void after()throws Exception{

    }
    @Test
    public void testIsLegal()throws Exception{
        String[][] data = DataProcessor.baseSocialSecurityData;
        boolean actual =  BaseSocialSecurityValidator.isLegal(data);
        boolean expect = true;
        Assert.assertSame(actual,expect);
    }
}
