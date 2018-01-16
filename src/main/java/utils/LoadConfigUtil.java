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
    private static final Logger logger = LogUtil.setLoggerHanlder(Logger.getLogger(LogUtil.MY_LOGGER), LogUtil.OUTPUTPATH);

    private LoadConfigUtil() {

    }

    private static final JsonObject jsonObject1 = getJson1ContentsByJson();
    private static final JsonObject jsonObject2 = getJson2ContentsByJson();

//    /**
//     * 从json文件中读取内容，放入List并返回
//     *
//     * @return
//     * @throws Exception
//     */
//    private static List<String> getJsonContents(String jsonPath) {
//        ArrayList<String> contentArrayList = new ArrayList<String>();
//        try {
//            FileInputStream fileInputStream = new FileInputStream(jsonPath);
//            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
//            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//
//            String rowContent = null;
//            while ((rowContent = bufferedReader.readLine()) != null) {
//                rowContent = rowContent.trim();
//                rowContent = rowContent.replaceAll("\"", "");
//                rowContent = rowContent.replaceAll(",", "");
//                if (rowContent.equals("")) {
//                    continue;
//                }
//                contentArrayList.add(rowContent);
//            }
//            bufferedReader.close();
//            return contentArrayList;
//        } catch (Exception e) {
//            e.printStackTrace();
//            logger.severe("找不到json文件");
//        }
//        return null;
//    }

    /**
     * 用JsonObject获取json1文件内容
     *
     * @return
     */
    private static JsonObject getJson1ContentsByJson() {
        JsonParser jsonParser = new JsonParser();
        try {
            JsonObject jsonObject = (JsonObject) jsonParser.parse(new BufferedReader(new InputStreamReader(new FileInputStream(Constant.JsonPath.JSONPATH), "UTF-8")));
            return jsonObject;
        } catch (JsonIOException | JsonSyntaxException | FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 用JsonObject获取json1文件内容
     *
     * @return
     */
    private static JsonObject getJson2ContentsByJson() {
        JsonParser jsonParser = new JsonParser();
        try {
            JsonObject jsonObject = (JsonObject) jsonParser.parse(new BufferedReader(new InputStreamReader(new FileInputStream(Constant.JsonPath.JSONPATH2), "UTF-8")));
            return jsonObject;
        } catch (JsonIOException | JsonSyntaxException | FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 读取的链表中获取输入的Excel文件路径，文件名和sheet名
     */
    public static SheetForm getFilesAndSheets(String handle) throws Exception {
        SheetForm sheetForm = new SheetForm();
        JsonObject jsonObject = new JsonObject();
        if (handle.equals(Constant.Handle.DEPARTMENT_HANDLE)
                || handle.equals(Constant.Handle.OA_PROJECT)
                || handle.equals(Constant.Handle.SUBJECT_CODE)) {
            jsonObject = jsonObject2;
        } else {
            jsonObject = jsonObject1;
        }
        sheetForm.setFileName(jsonObject.get(handle).getAsJsonObject().get("FileName").getAsString());
        sheetForm.setSheetName(jsonObject.get(handle).getAsJsonObject().get("SheetName").getAsString());
        return sheetForm;
    }

    /**
     * 获取BaseDir
     *
     * @return
     * @throws Exception
     */
    public static String getBaseDir() {
        String baseDir = null;
        baseDir = jsonObject1.get("BaseDir").getAsString();
        return baseDir;
    }

    /**
     * 获取year
     *
     * @return
     * @throws Exception
     */
    public static String getYear() {
        String year = null;
        year = jsonObject1.get("Year").getAsString();
        return year;
    }

    /**
     * 获取month
     *
     * @return
     * @throws Exception
     */
    public static String getMonth() {
        String month = null;
        month = jsonObject1.get("Month").getAsString();
        return month;
    }

    /**
     * 获取新员工专用的标记
     *
     * @return
     */
    public static String getNewStaff() {
        String newStaff = null;
        newStaff = jsonObject1.get("新员工专属").getAsString();
        return newStaff;
    }

    /**
     * 获取开始周数
     * @return
     */
    public static String getStartDate(){
        String startDate = null;
        startDate = jsonObject1.get("StartDate").getAsString();
        return startDate;
    }

    /**
     * 获取周数
     * @return
     */
    public static String getWeekNum (){
        String weekNum = null;
        weekNum  = jsonObject1.get("WeekNum").getAsString();
        return weekNum;
    }

    /**
     * 读取config2.json的BaseDir
     *
     * @return
     */
    public static String getBaseDir2() {
        String baseDir2 = null;
        baseDir2 = jsonObject2.get("BaseDir2").getAsString();
        return baseDir2;
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
