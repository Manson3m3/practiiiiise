package utils;

import constant.Constant;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * LoadConfigUtil Tester.
 *
 * @author Jiang.tao
 * @date 11/07/2017
 */
public class LoadConfigUtilTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    @Test
    public void testGetFilesAndSheets() throws Exception {
        LoadConfigUtil.SheetForm sheetForm = LoadConfigUtil.getFilesAndSheets(Constant.Handle.XX_HANDLE);
        Assert.assertNotEquals("",sheetForm.toString());
    }

    @Test
    public void testGetBaseDir() throws Exception {
        String baseDir = LoadConfigUtil.getBaseDir();
        Assert.assertNotEquals("",baseDir);
    }

    @Test
    public void testGetYear() throws Exception {
        String year = LoadConfigUtil.getYear();
        Assert.assertNotEquals( "",year);
    }

    @Test
    public void testGetMonth() throws Exception {
        String month = LoadConfigUtil.getMonth();
        Assert.assertNotEquals("",month);
    }

    @Test
    public void testGetNewStaff(){
        String newStaff = LoadConfigUtil.getNewStaff();
        Assert.assertNotEquals("",newStaff);
    }

    @Test
    public void testGetBaseDir2(){
        String baseDir2 = LoadConfigUtil.getBaseDir2();
        Assert.assertNotEquals("",baseDir2);
    }

}
