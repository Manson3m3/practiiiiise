package utils;

import constant.Constant;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Created by Tao.Jiang on 2017/11/17.
 */
public class ResultUtil {
    private static final Logger logger = LogUtil.setLoggerHanlder(Logger.getLogger(LogUtil.MY_LOGGER), LogUtil.OUTPUTPATH);

    /**
     * 在输出文件夹的路径下创建Excel文件
     *
     * @return
     */
    public static boolean createResultExcel() {
        String outputExcelPath = null;
        boolean ifSucceed = false;
        FileOutputStream fileOutputStream = null;
        try {
            HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
            hssfWorkbook.createSheet(Constant.SheetName.COALESCING_SHEET_NAME);
            hssfWorkbook.createSheet(Constant.SheetName.COST_SHEET_NAME);
            hssfWorkbook.createSheet(Constant.SheetName.XX_MONTH_COST_SHEET_NAME);
            hssfWorkbook.createSheet(Constant.SheetName.WEEK_SHEET_NAME);
            outputExcelPath = LogUtil.OUTPUTPATH + "\\" + Constant.FolderNames.STEP1OUTPUT + "\\" + Constant.FileName.TIME_SHEET_1;
            File file = new File(outputExcelPath);
            fileOutputStream = new FileOutputStream(file);
            hssfWorkbook.write(fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            ifSucceed = true;
        } catch (IOException e) {
            e.printStackTrace();
            logger.severe("创建输出Excel文件失败");
        }
        return ifSucceed;
    }

    public static boolean createResultExcel2() {
        String outputExcelPath = null;
        boolean ifSucceed = false;
        FileOutputStream fileOutputStream = null;
        try {
            HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
            hssfWorkbook.createSheet(Constant.SheetName.STEP2_RESULT_SHEET_NAME);
            hssfWorkbook.createSheet(Constant.SheetName.DEPARTMENT_SHEET_NAME);
            hssfWorkbook.createSheet(Constant.SheetName.OA_PROJECT_SHEET_NAME);
            hssfWorkbook.createSheet(Constant.SheetName.SUBJECT_CODE_SHEET_NAME);
            hssfWorkbook.createSheet(Constant.SheetName.COALESCING_SHEET_NAME);
            outputExcelPath = LogUtil.OUTPUTPATH + "\\" + Constant.FolderNames.STEP2OUTPUT + "\\" + Constant.FileName.TIME_SHEET_2;
            File file = new File(outputExcelPath);
            fileOutputStream = new FileOutputStream(file);
            hssfWorkbook.write(fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            ifSucceed = true;
        } catch (IOException e) {
            e.printStackTrace();
            logger.severe("创建输出Excel文件失败");
        }
        return ifSucceed;
    }

    /**
     * 创建异常记录表
     *
     * @return
     */
    public static boolean createErrorRecordExcel(String subFolderName) {
        boolean ifSucceed = false;
        String outputExcelPath = null;
        FileOutputStream fileOutputStream = null;
        try {
            HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
            hssfWorkbook.createSheet(Constant.SheetName.ERROR_SHEET_NAME);
            outputExcelPath = LogUtil.OUTPUTPATH + "\\" + subFolderName + "\\" + Constant.FileName.ERROR_RECORD;
            File file = new File(outputExcelPath);
            fileOutputStream = new FileOutputStream(file);
            hssfWorkbook.write(fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            ifSucceed = true;
        } catch (IOException e) {
            e.printStackTrace();
            logger.severe("创建异常记录Excel文件失败");
        }
        return ifSucceed;
    }

    /**
     * 将运行时读取的json文件复制到文件夹下
     */
    public static boolean copyJson(String jsonpath, String jsonname) {
        boolean ifSucceed = false;
        //将读取的json文件复制到输出子文件夹下
        try {
            FileInputStream fileInputStream = new FileInputStream(jsonpath);
            FileOutputStream fileOutputStream = new FileOutputStream(LogUtil.OUTPUTPATH + "\\" + Constant.FolderNames.INPUT + "\\" + jsonname);
            byte[] b = new byte[1024];
            int n = 0;
            while ((n = fileInputStream.read(b)) != -1) {
                fileOutputStream.write(b, 0, n);
            }
            fileInputStream.close();
            fileOutputStream.flush();
            fileOutputStream.close();
            ifSucceed = true;
        } catch (IOException e) {
            e.printStackTrace();
            logger.severe("找不到json文件");
        }
        return ifSucceed;
    }

    /**
     * 将单个Excel文件复制到文件夹下
     */
    public static boolean copyInputExcel(String filePath, String fileName) {
        boolean ifSucceed = false;
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath + "\\" + fileName);
            File file = new File(LogUtil.OUTPUTPATH + "\\" + Constant.FolderNames.INPUT + "\\" + fileName);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] b = new byte[1024];
            int n = 0;
            while ((n = fileInputStream.read(b)) != -1) {
                fileOutputStream.write(b, 0, n);
            }
            fileInputStream.close();
            fileOutputStream.flush();
            fileOutputStream.close();
            ifSucceed = true;
        } catch (IOException e) {
            e.printStackTrace();
            logger.severe(filePath + "\\" + fileName + "复制失败");
        }
        return ifSucceed;
    }

    /**
     * 将筛选需要复制的文件，避免重复复制
     */
    public static boolean excelToCopy(int period) {
        boolean ifSucceed = false;
        ArrayList<String> excelToCopyList = new ArrayList<>();
        try {
            String fileName1 = LoadConfigUtil.getFilesAndSheets(Constant.Handle.WORK_HOURS_MONTHLY_HANDLE).getFileName();
            excelToCopyList.add(fileName1);
            String fileName2 = LoadConfigUtil.getFilesAndSheets(Constant.Handle.XX_MONTH_SALARY_PRIMARY_HANDLE).getFileName();
            if (!excelToCopyList.contains(fileName2)) {
                excelToCopyList.add(fileName2);
            }
            String fileName3 = LoadConfigUtil.getFilesAndSheets(Constant.Handle.WORK_TYPE_HANDLE).getFileName();
            if (!excelToCopyList.contains(fileName3)) {
                excelToCopyList.add(fileName3);
            }
            String fileName4 = LoadConfigUtil.getFilesAndSheets(Constant.Handle.XX_MONTH_SOCIAL_SECURITY_HANDLE).getFileName();
            if (!excelToCopyList.contains(fileName4)) {
                excelToCopyList.add(fileName4);
            }
            String fileName5 = LoadConfigUtil.getFilesAndSheets(Constant.Handle.MAPPING_OA_HANDLE).getFileName();
            if (!excelToCopyList.contains(fileName5)) {
                excelToCopyList.add(fileName5);
            }
            String fileName6 = LoadConfigUtil.getFilesAndSheets(Constant.Handle.XX_HANDLE).getFileName();
            if (!excelToCopyList.contains(fileName6)) {
                excelToCopyList.add(fileName6);
            }

            String fileName7 = LoadConfigUtil.getFilesAndSheets(Constant.Handle.DEPARTMENT_HANDLE).getFileName();
            if (!excelToCopyList.contains(fileName7)) {
                excelToCopyList.add(fileName7);
            }
            String fileName8 = LoadConfigUtil.getFilesAndSheets(Constant.Handle.OA_PROJECT).getFileName();
            if (!excelToCopyList.contains(fileName8)) {
                excelToCopyList.add(fileName8);
            }
            String fileName9 = LoadConfigUtil.getFilesAndSheets(Constant.Handle.SUBJECT_CODE).getFileName();
            if (!excelToCopyList.contains(fileName9)) {
                excelToCopyList.add(fileName9);
            }
            for (int i = 0; i < excelToCopyList.size(); i++) {
                if (period == 1) {
                    ifSucceed = copyInputExcel(LoadConfigUtil.getBaseDir(), excelToCopyList.get(i));
                } else if (period == 2) {
                    ifSucceed = copyInputExcel(LoadConfigUtil.getBaseDir2(), excelToCopyList.get(i));
                }
                if (ifSucceed == false) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.severe("json文件读取失败");
        }
        return ifSucceed;
    }

    //创建结果文件夹
    public static boolean resultFolder() {
        subFolders(Constant.FolderNames.INPUT);
        subFolders(Constant.FolderNames.STEP1OUTPUT);
        subFolders(Constant.FolderNames.STEP2OUTPUT);
        return (copyJson(Constant.JsonPath.JSONPATH, "config.json") && copyJson(Constant.JsonPath.JSONPATH2, "config2.json") && excelToCopy(1) && excelToCopy(2) && createResultExcel() && createResultExcel2());
    }

    //创建子文件夹
    public static void subFolders(String folderName) {
        //创建step1输出文件夹
        StringBuffer stringBuffer1 = new StringBuffer(LogUtil.OUTPUTPATH);
        stringBuffer1 = stringBuffer1.append("\\" + folderName);
        File step1File = new File(stringBuffer1.toString());
        if (!step1File.exists())
            step1File.mkdirs();
    }
}
