package load;


import constant.Constant;
import entity.BaseSalary;
import entity.BaseSocialSecurity;
import entity.MappingOA;
import entity.WorkingHoursMonthReport;
import exception.DataException;
import utils.LoadConfigUtil;
import utils.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * 加载数组中数据到数据结构中
 * Created by Li.Hou on 2017/11/8.
 */
public final class LoadInputData {

    private static final Logger logger = LogUtil.setLoggerHanlder(Logger.getLogger(LogUtil.MY_LOGGER), LogUtil.OUTPUTPATH);
    private LoadInputData(){

    }

    //step1阶段的目录
    private static String basePath = LoadConfigUtil.getBaseDir();

    //step2阶段的目录
    private static String basePath2 = LoadConfigUtil.getBaseDir2();


    private static String handle;
    private static int startRow;
    private static LoadConfigUtil.SheetForm sheetForm;
    private static String filePath;
    private static String sheetName;
    private static String[][] data;
    private static String logInfo;

    /**
     * 获取mappingOA员工表
     *
     * @return
     * @throws Exception
     */
    static List<MappingOA> getMappingOAList() throws Exception {
        handle = Constant.Handle.MAPPING_OA_HANDLE;
        startRow = Constant.StartRow.MAPPING_OA_STARTROW;
        sheetForm = LoadConfigUtil.getFilesAndSheets(handle);
        filePath = basePath + sheetForm.getFileName();
        sheetName = sheetForm.getSheetName();
        data = ReadData.readData(filePath, sheetName, startRow);

        if (data[0].length != Constant.SheetColumnNum.MAPPING_OA_COL_NUM) {
            throw new DataException("mappingOA表格式不正确,预计列数为"+Constant.SheetColumnNum.MAPPING_OA_COL_NUM+",实际列数为"+data[0].length);
        }
        List<MappingOA> mappingOAList = new ArrayList<>();
        for (String[] str :
                data) {
            for (int i = 0; i < data[0].length; i++) {
                if (str[i] == null) {
                    throw new DataException("mappingOA表中出现空数据！第"+(i+1)+"行数据为空！");
                }
            }
            MappingOA mappingOA;
            try {
                String employeeId = str[0];
                String employeeName = str[1];
                String OAEmployeeInfo = str[2];
                String costAssignment = str[3];
                String costNature = str[4];
                mappingOA = new MappingOA(employeeId,employeeName,OAEmployeeInfo,costAssignment,costNature);
            } catch (Exception e) {
                throw new DataException("mappingOA表数据格式不正确！员工id为"+str[0]);
            }
            if (mappingOAList.contains(mappingOA)) {
                throw new DataException("mappingOA表数据中重复！员工id为"+str[0]);
            }
            mappingOAList.add(mappingOA);
        }
        if (mappingOAList.isEmpty()) {
            throw new DataException("mappingOA表中数据为空！");
        }
        return mappingOAList;
    }

    static String[][] getMappingOAData() throws Exception {
        handle = Constant.Handle.MAPPING_OA_HANDLE;
        startRow = Constant.StartRow.MAPPING_OA_STARTROW;
        sheetForm = LoadConfigUtil.getFilesAndSheets(handle);
        filePath = basePath + sheetForm.getFileName();
        sheetName = sheetForm.getSheetName();
        data = ReadData.readData(filePath, sheetName, startRow);

        if (data[0].length != Constant.SheetColumnNum.MAPPING_OA_COL_NUM) {
            throw new DataException("mappingOA表格式不正确,预计列数为"+Constant.SheetColumnNum.MAPPING_OA_COL_NUM+",实际列数为"+data[0].length);
        }
        for (String[] str: data) {
            for (int i = 0; i < data[0].length; i++) {
                if (str[i] == null) {
                    throw new DataException("mappingOA表中出现空数据！员工id为:"+str[0]+",第"+(i+1)+"列数据为空！");
                }
            }
        }
        return data;
    }


    /**
     * 获取工时月报表
     * @return
     * @throws Exception
     */
    static List<WorkingHoursMonthReport> getWorkingHoursMonthReportList() throws Exception {
        handle = Constant.Handle.WORK_HOURS_MONTHLY_HANDLE;
        startRow = Constant.StartRow.WORKING_HOURS_MONTH_STARTROW;
        sheetForm = LoadConfigUtil.getFilesAndSheets(handle);
        filePath = basePath + sheetForm.getFileName();
        sheetName = sheetForm.getSheetName();
        data = ReadData.readData(filePath, sheetName, startRow);

        if (data[0].length != Constant.SheetColumnNum.WORKING_HOURS_MONTH_REPORT_COL_NUM) {
            throw new DataException("工时月报表格式不正确,预计列数为"+Constant.SheetColumnNum.WORKING_HOURS_MONTH_REPORT_COL_NUM+",实际列数为"+data[0].length);
        }
        List<WorkingHoursMonthReport> workingHoursMonthReportList = new ArrayList<>();
        for (int j = 0;j<data.length;j++) {
            String[] str = data[j];
            for (int i = 0; i < data[0].length; i++) {
                if (str[i] == null) {
                    throw new DataException("工时月报表中出现空数据！第"+j+"行"+(i+1)+"列数据为空！");
                }
            }
            WorkingHoursMonthReport workingHoursMonthReport;
            try {
                String applicant = str[0];
                String applyDepartment = str[1];
                String startDate = str[2];
                String endDate = str[3];
                String projectId =str[4];
                double projectTotal = Double.parseDouble(str[5]);
                String listId = str[6];
                workingHoursMonthReport = new WorkingHoursMonthReport(applicant,applyDepartment,startDate,endDate,projectId,projectTotal,listId);
            } catch (Exception e) {
                throw new DataException("工时月报表数据格式不正确！第"+(j+1)+"行数据出错！");
            }
            if (workingHoursMonthReportList.contains(workingHoursMonthReport)) {
                throw new DataException("工时月报表数据中重复！第"+(j+1)+"行数据重复！");
            }
            workingHoursMonthReportList.add(workingHoursMonthReport);
        }
        if (workingHoursMonthReportList.isEmpty()) {
            throw new DataException("工时月报表中数据为空！");
        }
        return workingHoursMonthReportList;
    }

    static String[][] getWorkingHoursMonthData() throws Exception {
        handle = Constant.Handle.WORK_HOURS_MONTHLY_HANDLE;
        startRow = Constant.StartRow.WORKING_HOURS_MONTH_STARTROW;
        sheetForm = LoadConfigUtil.getFilesAndSheets(handle);
        filePath = basePath + sheetForm.getFileName();
        sheetName = sheetForm.getSheetName();
        data = ReadData.readData(filePath, sheetName, startRow);

        if (data[0].length != Constant.SheetColumnNum.WORKING_HOURS_MONTH_REPORT_COL_NUM) {
            throw new DataException("工时月报表格式不正确,预计列数为"+Constant.SheetColumnNum.WORKING_HOURS_MONTH_REPORT_COL_NUM+",实际列数为"+data[0].length);
        }
        for (String[] str: data) {
            for (int i = 0; i < data[0].length; i++) {
                if (str[i] == null) {
                    throw new DataException("工时月报表中出现空数据！第"+(i+1)+"列数据为空！");
                }
            }
        }
        return data;
    }
    /**
     * 获取员工基本工资表
     * @return
     * @throws Exception
     */
    static List<BaseSalary> getBaseSalaryList() throws Exception {
        handle = Constant.Handle.XX_MONTH_SALARY_PRIMARY_HANDLE;
        startRow = Constant.StartRow.BASE_SALARY_STARTROW;
        sheetForm = LoadConfigUtil.getFilesAndSheets(handle);
        filePath = basePath + sheetForm.getFileName();
        sheetName = sheetForm.getSheetName();
        data = ReadData.readData(filePath, sheetName, startRow);

        if (data[0].length != Constant.SheetColumnNum.BASE_SALART_COL_NUM) {
            throw new DataException("员工基本工资表格式不正确，预计列数为"+Constant.SheetColumnNum.BASE_SALART_COL_NUM+",实际列数为"+data[0].length);
        }
        List<BaseSalary> baseSalaryList = new ArrayList<>();
        for (String[] str:
                data) {
            BaseSalary baseSalary;
            for (int i = 0; i < data[0].length; i++) {
                if (str[i] == null) {
                    throw new DataException("员工基本工资表中出现空数据!员工id为:"+str[0]+",第"+(i+1)+"列数据为空！");
                }
            }
            try {
                String employeeId =str[0];
                String employeeName = str[1];
                double baseSalaryNum = Double.parseDouble(str[2]);
                baseSalary = new BaseSalary(employeeId,employeeName,baseSalaryNum);
            } catch (Exception e) {
                throw new DataException("员工基本工资表数据格式不正确！员工id为"+str[0]);
            }
            if (baseSalaryList.contains(baseSalary)) {
                throw new DataException("员工基本工资表数据中重复！员工id为"+str[0]);
            }
            baseSalaryList.add(baseSalary);
        }
        if (baseSalaryList.isEmpty()) {
            throw new DataException("员工基本工资表中数据为空！");
        }
        return baseSalaryList;
    }


    /**
     * 获取员工基本社保表
     * @return
     * @throws Exception
     */
    static List<BaseSocialSecurity> getBaseSocialSecurityList() throws Exception {
        handle = Constant.Handle.XX_MONTH_SOCIAL_SECURITY_HANDLE;
        startRow = Constant.StartRow.BASE_SOCIAL_SECURITY_STARTROW;
        sheetForm = LoadConfigUtil.getFilesAndSheets(handle);
        filePath = basePath + sheetForm.getFileName();
        sheetName = sheetForm.getSheetName();
        data = ReadData.readData(filePath, sheetName, startRow);

        if (data[0].length != Constant.SheetColumnNum.BASE_SOCIAL_SECURITY_COL_NUM) {
            throw new DataException("员工基本社保表格式不正确,预计列数为"+Constant.SheetColumnNum.BASE_SOCIAL_SECURITY_COL_NUM+",实际列数为"+data[0].length);
        }
        List<BaseSocialSecurity> baseSocialSecurityList = new ArrayList<>();
        for (String[] str:
                data) {
            for (int i = 0; i < data[0].length-1; i++) {
                if (str[i] == null) {
                    throw new DataException("员工基本社保表中出现空数据！员工id为:"+str[0]+",第"+(i+1)+"列数据为空！");
                }
            }
            BaseSocialSecurity baseSocialSecurity;
            try {
                for (int i = 3; i < 22; i++) {
                    double tmpNum = Double.parseDouble(str[i]);
                    if (tmpNum < 0.00) {
                        throw new DataException("员工基本社保表中数据不能为负值！员工id为："+str[0]+",第"+(i+1)+"列数据为负！");
                    }
                }
                String employeeId =str[0];
                String employeeName = str[1];
                String socialSecurityCity = str[2];
                String payMonth = str[22];
                baseSocialSecurity = new BaseSocialSecurity(employeeId,employeeName,socialSecurityCity,payMonth);
            } catch (Exception e) {
                throw new DataException("员工基本社保表数据格式不正确！员工id为"+str[0]);
            }
            if (baseSocialSecurityList.contains(baseSocialSecurity)) {
                throw new DataException("员工基本社保表数据中重复！员工id为"+str[0]);
            }
            baseSocialSecurityList.add(baseSocialSecurity);
        }
        if (baseSocialSecurityList.isEmpty()) {
            throw new DataException("员工基本社保表中数据为空！");
        }
        return baseSocialSecurityList;
    }

    static String[][] getBaseSocialSecurityData() throws Exception {
        handle = Constant.Handle.XX_MONTH_SOCIAL_SECURITY_HANDLE;
        startRow = Constant.StartRow.BASE_SOCIAL_SECURITY_STARTROW;
        sheetForm = LoadConfigUtil.getFilesAndSheets(handle);
        filePath = basePath + sheetForm.getFileName();
        sheetName = sheetForm.getSheetName();
        data = ReadData.readData(filePath, sheetName, startRow);
        if (data[0].length != Constant.SheetColumnNum.BASE_SOCIAL_SECURITY_COL_NUM) {
            throw new DataException("员工基本社保表格式不正确,预计列数为"+Constant.SheetColumnNum.BASE_SOCIAL_SECURITY_COL_NUM+",实际列数为"+data[0].length);
        }
        for (int j = 0;j<data.length;j++) {
            String[] str = data[j];
            if (str == null) {
                throw new DataException("员工基本社保表中出现空数据!第"+(j+1)+"行数据为空！");
            }
            for (int i = 0;i<str.length;i++) {
                if (str[i] == null) {
                    throw new DataException("员工基本社保表数据出现空数据，员工id为:"+str[0]+",第"+(i+1)+"列数据为负！");
                }
            }
        }
        return data;
    }
    /**
     * 获取员工id姓名map
     * @return
     * @throws Exception
     */
    static Map<String, String> getEmployeeIdNameMap() throws Exception {
        handle = Constant.Handle.XX_HANDLE;
        startRow = Constant.StartRow.EMPLOYEE_ID_NAME_STARTROW;
        sheetForm = LoadConfigUtil.getFilesAndSheets(handle);
        filePath = basePath + sheetForm.getFileName();
        sheetName = sheetForm.getSheetName();
        data = ReadData.readData(filePath, sheetName, startRow);

        int colNums = data[0].length;
        if (colNums != Constant.SheetColumnNum.MAPPING_ID_NAME) {
            throw new DataException("员工id姓名表格式不正确！预计列数为"+Constant.SheetColumnNum.MAPPING_ID_NAME+",实际列数为"+data[0].length);
        }
        Map<String,String> employeeIdNameMap = new HashMap<>();
        for (String[] str:
                data) {
            for (int i = 0; i < colNums; i++) {
                if (str[i] == null) {
                    throw new DataException("员工id姓名表中出现空数据！员工id为:"+str[0]+"第"+(i+1)+"列数据为空！");
                }
            }
            if (employeeIdNameMap.containsKey(str[1]) || employeeIdNameMap.containsValue(str[0])) {
                throw new DataException("员工id姓名表中出现重复数据！员工id为"+str[1]);
            }
            employeeIdNameMap.put(str[1],str[0]);
        }
        if (employeeIdNameMap.isEmpty()) {
            throw new DataException("员工id姓名表中无数据！");
        }
        logger.info("idName表读取验证成功！");
        return employeeIdNameMap;
    }

    /**
     * 获取工作表类型
     * @return
     * @throws Exception
     */
    static Map<String, String> getWorkTypeMap() throws Exception {
        handle = Constant.Handle.WORK_TYPE_HANDLE;
        startRow = Constant.StartRow.WORK_TYPE_STARTROW;
        sheetForm = LoadConfigUtil.getFilesAndSheets(handle);
        filePath = basePath + sheetForm.getFileName();
        sheetName = sheetForm.getSheetName();
        data = ReadData.readData(filePath, sheetName, startRow);

        int colNums = data[0].length;
        if (colNums != Constant.SheetColumnNum.MAPPING_WORKTYPE) {
            throw new DataException("工作类型表格式不正确！预计列数为"+Constant.SheetColumnNum.MAPPING_WORKTYPE+",实际列数为"+data[0].length);
        }
        Map<String,String> workTypeMap = new HashMap<>();
        for (String[] str:
                data) {
            for (int i = 0; i < colNums; i++) {
                if (str[i] == null) {
                    throw new DataException("工作类型表中出现空数据！项目编码为:"+str[0]+"第"+(i+1)+"列数据为空！");
                }
            }
            if (workTypeMap.containsKey(str[0])) {
                throw new DataException("工作类型表中出现重复数据！项目编码为"+str[0]);
            }
            workTypeMap.put(str[0],str[1]);
        }
        if (workTypeMap.isEmpty()) {
            throw new DataException("工作类型表中数据为空！");
        }
        logger.info("工作类型表读取且验证成功！");
        return workTypeMap;
    }

    /**
     * step2获取部门表数据
     *
     * @return data
     * @throws Exception
     */
    static String[][] getDepartmentData() throws Exception {
        handle = Constant.Handle.DEPARTMENT_HANDLE;
        startRow = Constant.StartRow.DEPARTMENT_STARTROW;
        sheetForm = LoadConfigUtil.getFilesAndSheets(handle);
        filePath = basePath2 + sheetForm.getFileName();
        sheetName = sheetForm.getSheetName();
        data = ReadData.readData(filePath, sheetName, startRow);
        for (int i = 0; i < data.length; i++) {
            String[] strings = data[i];
            if (strings[0] == null || strings[1] == null) {
                logInfo = "部门表数据格式不正确，第" + i + "行数据出现错误";
                throw new DataException(logInfo);
            }
        }
        return data;
    }

    /**
     * step2
     * 获取OA项目数据
     *
     * @return data
     * @throws Exception
     */
    static String[][] getOAProData() throws Exception {
        handle = Constant.Handle.OA_PROJECT;
        startRow = Constant.StartRow.OA_PROJECT_STARTROW;
        sheetForm = LoadConfigUtil.getFilesAndSheets(handle);
        filePath = basePath2 + sheetForm.getFileName();
        sheetName = sheetForm.getSheetName();
        data = ReadData.readData(filePath, sheetName, startRow);
        for (int i = 0; i < data.length; i++) {
            if (data[i][4] == null || data[i][9] == null) {
                logInfo = "OA项目表数据格式不正确，第" + (i + 1) + "行数据出现错误";
                throw new DataException(logInfo);
            }
        }

        return data;
    }

    /**
     * step2
     * 获取科目代码表
     *
     * @return
     * @throws Exception
     */
    static String[][] getSubjectCodeData() throws Exception {
        handle = Constant.Handle.SUBJECT_CODE;
        startRow = Constant.StartRow.SUBJECT_CODE_STARTROW;
        sheetForm = LoadConfigUtil.getFilesAndSheets(handle);
        filePath = basePath2 + sheetForm.getFileName();
        sheetName = sheetForm.getSheetName();
        data = ReadData.readData(filePath, sheetName, startRow);
        for (int i = 0; i < data.length; i++) {
            if (data[i][2] == null || data[i][3] == null) {
                logInfo = "科目编码表数据格式不正确，第" + (i + 1) + "行数据出现错误";
                throw new DataException(logInfo);
            }
        }
        return data;
    }
}
