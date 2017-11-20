package utils;

import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import constant.Constant;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Tao.Jiang on 2017/11/8.
 */
public class LoadConfigUtil {

    private LoadConfigUtil() {

    }

    /**
     * 从json文件中读取内容，放入List并返回
     *
     * @return
     * @throws Exception
     */
    private static List<String> getJsonContents() {
        ArrayList<String> contentArrayList = new ArrayList<String>();
        try {
            FileInputStream fileInputStream = new FileInputStream(Constant.JsonPath.JSONPATH);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String rowContent = null;
            while ((rowContent = bufferedReader.readLine()) != null) {
                rowContent = rowContent.trim();
                rowContent = rowContent.replaceAll("\"", "");
                rowContent = rowContent.replaceAll(",", "");
                if (rowContent.equals("")) {
                    continue;
                }
                contentArrayList.add(rowContent);
            }
            bufferedReader.close();
            return contentArrayList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 读取的链表中获取输入的Excel文件路径，文件名和sheet名
     */
    public static SheetForm getFilesAndSheets(String handle) throws Exception {
        SheetForm sheetForm = new SheetForm();
        List<String> contentList = getJsonContents();
        //装载每次用“:”切割出的数组
        String[] tempContentSplited = null;
        try {
            int count = 1;
            while (count < contentList.size()) {
                if (contentList.get(count).contains(handle)) {
                    //向下一行找到FileName
                    count++;
                    tempContentSplited = contentList.get(count).split(":");
                    sheetForm.setFileName(tempContentSplited[1]);
                    //向下一行找到SheetName
                    count++;
                    tempContentSplited = contentList.get(count).split(":");
                    sheetForm.setSheetName(tempContentSplited[1]);
                    break;
                }
                count++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sheetForm;
    }

    /**
     * 获取BaseDir
     *
     * @return
     * @throws Exception
     */
    public static String getBaseDir() {
        List<String> contentList = getJsonContents();
        String baseDir = null;
        baseDir = contentList.get(Constant.JsonRowNum.BASEDIR_ROW_NUM).replaceAll("BaseDir:", "");
        return baseDir;
    }

    /**
     * 获取year
     *
     * @return
     * @throws Exception
     */
    public static String getYear() {
        List<String> contentList = getJsonContents();
        String year = null;
        year = contentList.get(Constant.JsonRowNum.YEAR_ROW_NUM).replaceAll("Year:", "");
        return year;
    }

    /**
     * 获取month
     *
     * @return
     * @throws Exception
     */
    public static String getMonth() {
        List<String> contentList = getJsonContents();
        String month = null;
        month = contentList.get(Constant.JsonRowNum.MONTH_ROW_NUM).replaceAll("Month:", "");
        return month;
    }

    /**
     * 从读取的链表中获取文件的输出路径
     *
     * @return
     * @throws Exception
     */
    public static String getOutputPath() {
        String outputPath = null;
        outputPath = JarToolUtil.getJarDir();
        return outputPath;
    }

    /**
     * 获取新员工专用的标记
     * @return
     */
    public static String getNewStaff() {
        String newStaff = null;
        newStaff = getJsonContents().get(Constant.JsonRowNum.NEW_STAFF_ROW_NUM).replaceAll("新员工专属:","");
        return newStaff;
    }

    /**
     * 用JsonObject获取json文件内容
     *
     * @return
     */
    public static JsonObject getJsonContentsByJson() {
        JsonParser jsonParser = new JsonParser();
        try {
            JsonObject jsonObject = (JsonObject) jsonParser.parse(new FileReader(Constant.JsonPath.JSONPATH));
            jsonObject.get("BaseDir:");
            jsonObject.get("Year").getAsInt();
            jsonObject.get("Month").getAsInt();
            jsonObject.get("新员工专用").getAsString();
            jsonObject.get("Mapping表OA员工信息表").getAsJsonObject();
            jsonObject.get("工时月报表").getAsJsonObject();
            jsonObject.get("XX月工资原始表").getAsJsonObject();
            jsonObject.get("XX月社保原始表").getAsJsonObject();
            jsonObject.get("XX表").getAsJsonObject();
            jsonObject.get("Sheet7表").getAsJsonObject();
            return jsonObject;
        } catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    //返回sheet信息
    public static class SheetForm {
        private String FileName;
        private String SheetName;

        public String getFileName() {
            return FileName;
        }

        public void setFileName(String fileName) {
            FileName = fileName;
        }

        public String getSheetName() {
            return SheetName;
        }

        public void setSheetName(String sheetName) {
            SheetName = sheetName;
        }

        public String toString() {
            return FileName + SheetName;
        }
    }
}
