package utils;

import com.google.gson.JsonObject;
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
        LoadConfigUtil.SheetForm sheetForm1 = new LoadConfigUtil.SheetForm();
        sheetForm1.setSheetName("j");
        sheetForm1.setFileName("i");
        Assert.assertEquals(sheetForm.toString(), sheetForm1.toString());
    }

    @Test
    public void testGetBaseDir() throws Exception {
        String baseDir = LoadConfigUtil.getBaseDir();
        String baseDir1 = "z";
        Assert.assertEquals(baseDir, baseDir1);
    }

    @Test
    public void testGetYear() throws Exception {
        String year = LoadConfigUtil.getYear();
        String year1 = "2";
        Assert.assertEquals(year, year1);
    }

    @Test
    public void testGetMonth() throws Exception {
        String month = LoadConfigUtil.getMonth();
        String month1 = "2";
        Assert.assertEquals(month, month1);
    }

    @Test
    public void testGetContentByJson() throws Exception {
        JsonObject jsonObject = LoadConfigUtil.getJsonContentsByJson();
        JsonObject jsonObjectAspected = new JsonObject();
        jsonObjectAspected.addProperty("BaseDir", "z");
        jsonObjectAspected.addProperty("Year", "2");
        jsonObjectAspected.addProperty("Month", "2");
        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty("FileName", "a");
        jsonObject1.addProperty("SheetName", "b");
        jsonObjectAspected.add("Mapping表OA员工信息表", jsonObject1);
        JsonObject jsonObject2 = new JsonObject();
        jsonObject2.addProperty("FileName", "c");
        jsonObject2.addProperty("SheetName", "d");
        jsonObjectAspected.add("工时月报表", jsonObject2);
        JsonObject jsonObject3 = new JsonObject();
        jsonObject3.addProperty("FileName", "e");
        jsonObject3.addProperty("SheetName", "f");
        jsonObjectAspected.add("XX月工资原始表", jsonObject3);
        JsonObject jsonObject4 = new JsonObject();
        jsonObject4.addProperty("FileName", "g");
        jsonObject4.addProperty("SheetName", "h");
        jsonObjectAspected.add("XX月社保原始表", jsonObject4);
        JsonObject jsonObject5 = new JsonObject();
        jsonObject5.addProperty("FileName", "i");
        jsonObject5.addProperty("SheetName", "j");
        jsonObjectAspected.add("XX表", jsonObject5);
        JsonObject jsonObject6 = new JsonObject();
        jsonObject6.addProperty("FileName", "k");
        jsonObject6.addProperty("SheetName", "l");
        jsonObjectAspected.add("Sheet7表", jsonObject6);
        JsonObject jsonObject7 = new JsonObject();
        jsonObject7.addProperty("OutFileName", "m");
        jsonObjectAspected.add("输出路径", jsonObject7);
        Assert.assertEquals(jsonObject.toString(), jsonObjectAspected.toString());
    }

    @Test
    public void testGetOutputPath() throws Exception {
        String outputPath = LoadConfigUtil.getOutputPath();
        String outputPath1 = "m";
        Assert.assertEquals(outputPath, outputPath1);
    }
}
