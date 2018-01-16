import constant.Constant;
import entity.BaseSalary;
import entity.ErrorRecord;
import entity.MappingOA;
import entity.WorkingHoursMonthReport;
import load.DataProcessor;
import load.Step2DataProcessor;
import output.CombinedStatement;
import output.CostSummary;
import output.Step2Result;
import output.XMonthCostSummary;
import utils.*;
import validator.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by Tao.Jiang on 2017/11/7.
 */
public class Main {
    private static final Logger logger = LogUtil.setLoggerHanlder(Logger.getLogger(LogUtil.MY_LOGGER), LogUtil.OUTPUTPATH);
    //输出文件夹路径
    private static final String filePath = LogUtil.OUTPUTPATH;

    private static final int year = Integer.parseInt(LoadConfigUtil.getYear());

    public static void main(String[] args) throws Exception {
        logger.info("--------------------------------第一阶段开始-----------------------------");
        logger.info("--------------------------------开始读取Excel表数据---------------------\n");
        ResultUtil.resultFolder();
        if (read()) {
            createFile("Read1.SUCCESS");
            logger.info("----------------------------------读取Excel成功-----------------------\n");
            LogUtil.stage = "验证阶段";
            logger.info("--------------------------------开始验证各表数据-------------------------\n");
            if (validate()) {
                createFile("Validate1.SUCCESS");
                logger.info("------------------------------------验证完毕，所有表通过-------------------\n");
                LogUtil.stage = "输出阶段";
                logger.info("----------------------------------开始输出数据----------------------------\n");
                if (write()) {
                    createFile("Write1.SUCCESS");
                    logger.info("-------------------------输出完毕，所有数据输出成功---------------------------\n");
                    logger.info("-------------------------");
                    logger.info("--------------------------第二阶段开始------------------------------------");
                    logger.info("--------------------------开始读取数据----------------------------");
                    if (read2()) {
                        createFile("Read2.SUCCESS");
                        logger.info("---------------------------读取数据成功--------------------------");
                        if (write2()) {
                            createFile("Write2.SUCCESS");
                            logger.info("---------------------------输出数据成功--------------------");
                        } else {
                            createFile("Write2.FAILURE");
                            logger.info("---------------------------输出数据失败-------------------------");
                        }
                    } else {
                        createFile("Read2.FAILURE");
                        logger.info("-------------------------------读取数据失败-----------------------------");
                    }
                    logger.info("----------------------第二阶段完成-----------------------");
                } else {
                    createFile("Write1.FAILURE");
                    logger.severe("-----------------------输出失败，请查看log-----------------------------\n");
                }
            } else {
                createFile("Validate1.FAILURE");
                logger.severe("-------------------------------输入表验证不通过-------------------\n");
            }
        } else {
            createFile("Read1.FAILURE");
            logger.severe("----------------------------------读取Excel失败-------------------------\n");
        }
        writeErrorRecord(Constant.FolderNames.STEP1OUTPUT);
        writeErrorRecord(Constant.FolderNames.STEP2OUTPUT);
    }

    /**
     * 第一阶段读取数据
     *
     * @return
     */
    private static boolean read() {
        Map<String, String> idNameMap = DataProcessor.employeeIdNameMap;
        Map<String, String> workTypeMap = DataProcessor.workTypeMap;
        List<BaseSalary> baseSalaryList = DataProcessor.baseSalaryList;
        String[][] baseSocialSecurityData = DataProcessor.baseSocialSecurityData;
        List<MappingOA> mappingOAList = DataProcessor.mappingOAList;
        List<WorkingHoursMonthReport> workingHoursMonthReportList = DataProcessor.workHoursMonthList;

        return (idNameMap != null && workTypeMap != null
                && baseSalaryList != null && baseSocialSecurityData != null
                && mappingOAList != null && workingHoursMonthReportList != null);

    }

    /**
     * 读取第二阶段数据
     *
     * @return
     */
    private static boolean read2() {
        return (Step2DataProcessor.departmentData != null && Step2DataProcessor.OAProjectData != null &&
                Step2DataProcessor.subjectCodeData != null);
    }

    /**
     * 一阶段验证
     * @return
     */
    private static boolean validate() {
        return (MappingOAValidator.isLegal(DataProcessor.mappingOAList) &&
                BaseSalaryValidator.isLegal(DataProcessor.baseSalaryList) &&
                BaseSocialSecurityValidator.isLegal(DataProcessor.baseSocialSecurityData) &&
                WorkHoursMonthValidator.isLegal());
    }

    /**
     * 一阶段输出
     * @return
     */
    private static boolean write() {
        String outputFilePath = filePath + "\\step1输出\\" + Constant.FileName.TIME_SHEET_1;
        return (ExcelUtil.writeExcel(XMonthCostSummary.getOutputData(), outputFilePath, Constant.SheetName.XX_MONTH_COST_SHEET_NAME, 1, Constant.SheetDataTypeArray.XX_MONTH_COST)
                && ExcelUtil.writeExcel(CostSummary.getOutputData(), outputFilePath, Constant.SheetName.COST_SHEET_NAME, 1, Constant.SheetDataTypeArray.COST)
                && ExcelUtil.writeExcel(CombinedStatement.getOutputData(), outputFilePath, Constant.SheetName.COALESCING_SHEET_NAME, 1, Constant.SheetDataTypeArray.COALESCING)
                && ExcelUtil.writeExcel(CalendarUtil.getWeekNums(year), outputFilePath, Constant.SheetName.WEEK_SHEET_NAME, 1, Constant.SheetDataTypeArray.WEEK));
    }

    /**
     * 二阶段输出
     * @return
     * @throws Exception
     */
    private static boolean write2() throws Exception {
        String outputFilePath = filePath + "\\step2输出\\" + Constant.FileName.TIME_SHEET_2;
        return (ExcelUtil.writeExcel(Step2Result.getOutputData(), outputFilePath, Constant.SheetName.STEP2_RESULT_SHEET_NAME, 1, Constant.SheetDataTypeArray.STEP2)
                //读取部门表再写入
                && ExcelUtil.writeExcel(ExcelUtil.readExcel(LoadConfigUtil.getBaseDir2() + LoadConfigUtil.getFilesAndSheets(Constant.Handle.DEPARTMENT_HANDLE).getFileName(), LoadConfigUtil.getFilesAndSheets(Constant.Handle.DEPARTMENT_HANDLE).getSheetName(), 0), LogUtil.OUTPUTPATH + "\\" + Constant.FolderNames.STEP2OUTPUT + "\\" + Constant.FileName.TIME_SHEET_2, Constant.SheetName.DEPARTMENT_SHEET_NAME, 1, Constant.SheetDataTypeArray.DEPARTMENT)
                //OA项目表
                && ExcelUtil.writeExcel(ExcelUtil.readExcel(LoadConfigUtil.getBaseDir2() + LoadConfigUtil.getFilesAndSheets(Constant.Handle.OA_PROJECT).getFileName(), LoadConfigUtil.getFilesAndSheets(Constant.Handle.OA_PROJECT).getSheetName(), 0), LogUtil.OUTPUTPATH + "\\" + Constant.FolderNames.STEP2OUTPUT + "\\" + Constant.FileName.TIME_SHEET_2, Constant.SheetName.OA_PROJECT_SHEET_NAME, 1, Constant.SheetDataTypeArray.PROJECT)
                //科目代码表
                && ExcelUtil.writeExcel(ExcelUtil.readExcel(LoadConfigUtil.getBaseDir2() + LoadConfigUtil.getFilesAndSheets(Constant.Handle.SUBJECT_CODE).getFileName(), LoadConfigUtil.getFilesAndSheets(Constant.Handle.SUBJECT_CODE).getSheetName(), 0), LogUtil.OUTPUTPATH + "\\" + Constant.FolderNames.STEP2OUTPUT + "\\" + Constant.FileName.TIME_SHEET_2, Constant.SheetName.SUBJECT_CODE_SHEET_NAME, 1, Constant.SheetDataTypeArray.SUBJECT)
                //合并表
                && ExcelUtil.writeExcel(ExcelUtil.readExcel(LogUtil.OUTPUTPATH + "\\" + Constant.FolderNames.STEP1OUTPUT + "\\" + Constant.FileName.TIME_SHEET_1, Constant.SheetName.COALESCING_SHEET_NAME, 0), LogUtil.OUTPUTPATH + "\\" + Constant.FolderNames.STEP2OUTPUT + "\\" + Constant.FileName.TIME_SHEET_2, Constant.SheetName.COALESCING_SHEET_NAME, 1, Constant.SheetDataTypeArray.COALESCING)
        );
    }

    /**
     * 将异常信息写入对应子文件夹下的异常输出表.xls
     * @param subFolderName 子文件夹名
     * @return
     */
    private static boolean writeErrorRecord(String subFolderName) {
        String outputFilePath = filePath + "\\" + subFolderName + "\\" + Constant.FileName.ERROR_RECORD;
        ArrayList<ErrorRecord> errorRecords = new ArrayList<>();
        if (subFolderName.equals(Constant.FolderNames.STEP1OUTPUT)) {
            errorRecords = LogUtil.errorRecords;
        } else {
            errorRecords = LogUtil.errorRecords2;
        }
        return ResultUtil.createErrorRecordExcel(subFolderName)
                && ExcelUtil.writeExcel(LogUtil.errorRecordsToArray(errorRecords), outputFilePath, Constant.SheetName.ERROR_SHEET_NAME, 1, Constant.SheetDataTypeArray.ERROR_RECORD);
    }

    /**
     * 生成状态文件
     * @param fileName  文件名
     */
    private static void createFile(String fileName) {
        try {
            File path = new File(filePath);
            if (!path.exists()) {
                path.mkdir();
            }
            File file = new File(path, fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            logger.severe("创建文件" + fileName + "失败！");
            logger.severe(e.getMessage());
        }

    }

}
