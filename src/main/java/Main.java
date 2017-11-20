import constant.Constant;
import entity.BaseSalary;
import entity.MappingOA;
import entity.WorkingHoursMonthReport;
import load.DataProcessor;
import output.CombinedStatement;
import output.CostSummary;
import output.XMonthCostSummary;
import utils.CalendarUtil;
import utils.ExcelUtil;
import utils.LogUtil;
import utils.ResultUtil;
import validator.BaseSalaryValidator;
import validator.BaseSocialSecurityValidator;
import validator.MappingOAValidator;
import validator.WorkHoursMonthValidator;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
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

    public static void main(String[] args) throws Exception {
        logger.info("--------------------------------开始读取Excel表数据---------------------\n");
        if (read()) {
            createFile("Read.SUCCESS");
            logger.info("----------------------------------读取Excel成功-----------------------\n");
            logger.info("--------------------------------开始验证各表数据-------------------------\n");
            if (validate()) {
                createFile("Validate.SUCCESS");
                logger.info("------------------------------------验证完毕，所有表通过-------------------\n");
                logger.info("----------------------------------开始输出数据----------------------------\n");
                if (write()) {
                    createFile("Write.SUCCESS");
                    logger.info("-------------------------输出完毕，所有数据输出成功---------------------------\n");
                } else {
                    createFile("Write.FAILURE");
                    logger.severe("-----------------------输出失败，请查看log-----------------------------\n");
                }
            } else {
                createFile("Validate.FAILURE");
                logger.severe("-------------------------------输入表验证不通过-------------------\n");
            }
        } else {
            createFile("Read.FAILURE");
            logger.severe("----------------------------------读取Excel失败-------------------------\n");
        }
    }

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

    private static boolean validate() {
        return (MappingOAValidator.isLegal(DataProcessor.mappingOAList) &&
                BaseSalaryValidator.isLegal(DataProcessor.baseSalaryList) &&
                BaseSocialSecurityValidator.isLegal(DataProcessor.baseSocialSecurityData) &&
                WorkHoursMonthValidator.isLegal(DataProcessor.workHoursMonthList));
    }


    private static boolean write() {
        String outputFilePath = filePath + "\\" + Constant.FileName.TIME_SHEET_1;
        return (ResultUtil.resultFolder()
                &&ExcelUtil.writeExcel(XMonthCostSummary.getOutputData(), outputFilePath, Constant.SheetName.XX_MONTH_COST_SHEET_NAME, 1, Constant.SheetDataTypeArray.XX_MONTH_COST)
                && ExcelUtil.writeExcel(CostSummary.getOutputData(), outputFilePath, Constant.SheetName.COST_SHEET_NAME, 1, Constant.SheetDataTypeArray.COST)
                && ExcelUtil.writeExcel(CombinedStatement.getOutputData(), outputFilePath, Constant.SheetName.COALESCING_SHEET_NAME, 1, Constant.SheetDataTypeArray.COALESCING)
                && ExcelUtil.writeExcel(CalendarUtil.getWeekNums(),outputFilePath,Constant.SheetName.WEEK_SHEET_NAME,1,Constant.SheetDataTypeArray.WEEK));
    }


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
