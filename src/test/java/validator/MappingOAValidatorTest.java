package validator;

import load.DataProcessor;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Tao.Jiang on 2017/11/15.
 */
public class MappingOAValidatorTest {
    @Before
    public void before()throws Exception{}

    @After
    public void after()throws Exception{

    }

    @Test
    public void testIslegal()throws Exception{
        boolean actual = false;
        actual = MappingOAValidator.isLegal(DataProcessor.mappingOAList);
        boolean expect = true;
        Assert.assertSame(expect,actual);
    }
}
