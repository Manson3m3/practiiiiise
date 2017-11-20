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
    private static String basePath = LoadConfigUtil.getBaseDir();

    /**
     * 获取mappingOA员工表
     * @return
     * @throws Exception
     */
    static List<MappingOA> getMappingOAList() throws Exception{
        String handle = Constant.Handle.MAPPING_OA_HANDLE;
        int startRow = Constant.StartRow.MAPPING_OA_STARTROW;
        LoadConfigUtil.SheetForm sheetForm = LoadConfigUtil.getFilesAndSheets(handle);
        String filepath = basePath+sheetForm.getFileName();
        String sheetName = sheetForm.getSheetName();
        String[][] data = ReadData.readData(filepath,sheetName,startRow);

        if (data[0].length != Constant.SheetColumnNum.MAPPING_OA_COL_NUM) {
            throw new DataException("mappingOA表格式不正确");
        }
        List<MappingOA> mappingOAList = new ArrayList<>();
        for (String[] str:
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
                throw new DataException("mappingOA表数据格式不正确！");
            }
            if (mappingOAList.contains(mappingOA)) {
                throw new DataException("mappingOA表数据中重复！");
            }
            mappingOAList.add(mappingOA);
        }
        if (mappingOAList.isEmpty()) {
            throw new DataException("mappingOA表中数据为空！");
        }
        return mappingOAList;
    }

    static String[][] getMappingOAData() throws Exception{
        String handle = Constant.Handle.MAPPING_OA_HANDLE;
        int startRow = Constant.StartRow.MAPPING_OA_STARTROW;
        LoadConfigUtil.SheetForm sheetForm = LoadConfigUtil.getFilesAndSheets(handle);
        String filepath = basePath+sheetForm.getFileName();
        String sheetName = sheetForm.getSheetName();
        String[][] data = ReadData.readData(filepath,sheetName,startRow);

        if (data[0].length != Constant.SheetColumnNum.MAPPING_OA_COL_NUM) {
            throw new DataException("mappingOA表格式不正确");
        }
        for (String[] str: data) {
            for (int i = 0; i < data[0].length; i++) {
                if (str[i] == null) {
                    throw new DataException("mappingOA表中出现空数据！第"+(i+1)+"行数据为空！");
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
        String handle = Constant.Handle.WORK_HOURS_MONTHLY_HANDLE;
        int startRow = Constant.StartRow.WORKING_HOURS_MONTH_STARTROW;
        LoadConfigUtil.SheetForm sheetForm = LoadConfigUtil.getFilesAndSheets(handle);
        String filepath = basePath+sheetForm.getFileName();
        String sheetName = sheetForm.getSheetName();
        String[][] data = ReadData.readData(filepath,sheetName,startRow);

        if (data[0].length != Constant.SheetColumnNum.WORKING_HOURS_MONTH_REPORT_COL_NUM) {
            throw new DataException("工时月报表格式不正确");
        }
        List<WorkingHoursMonthReport> workingHoursMonthReportList = new ArrayList<>();
        for (int j = 0;j<data.length;j++) {
            String[] str = data[j];
            for (int i = 0; i < data[0].length; i++) {
                if (str[i] == null) {
                    throw new DataException("工时月报表中出现空数据！第"+(i+1)+"行数据为空！");
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
                throw new DataException("工时月报表数据中重复！");
            }
            workingHoursMonthReportList.add(workingHoursMonthReport);
        }
        if (workingHoursMonthReportList.isEmpty()) {
            throw new DataException("工时月报表中数据为空！");
        }
        return workingHoursMonthReportList;
    }

    static String[][] getWorkingHoursMonthData()throws Exception {
        String handle = Constant.Handle.WORK_HOURS_MONTHLY_HANDLE;
        int startRow = Constant.StartRow.WORKING_HOURS_MONTH_STARTROW;
        LoadConfigUtil.SheetForm sheetForm = LoadConfigUtil.getFilesAndSheets(handle);
        String filepath = basePath+sheetForm.getFileName();
        String sheetName = sheetForm.getSheetName();
        String[][] data = ReadData.readData(filepath,sheetName,startRow);

        if (data[0].length != Constant.SheetColumnNum.WORKING_HOURS_MONTH_REPORT_COL_NUM) {
            throw new DataException("工时月报表格式不正确");
        }
        for (String[] str: data) {
            for (int i = 0; i < data[0].length; i++) {
                if (str[i] == null) {
                    throw new DataException("工时月报表中出现空数据！第"+(i+1)+"行数据为空！");
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
        String handle = Constant.Handle.XX_MONTH_SALARY_PRIMARY_HANDLE;
        int startRow = Constant.StartRow.BASE_SALARY_STARTROW;
        LoadConfigUtil.SheetForm sheetForm = LoadConfigUtil.getFilesAndSheets(handle);
        String filepath = basePath+sheetForm.getFileName();
        String sheetName = sheetForm.getSheetName();
        String[][] data = ReadData.readData(filepath,sheetName,startRow);

        if (data[0].length != Constant.SheetColumnNum.BASE_SALART_COL_NUM) {
            throw new DataException("员工基本工资表格式不正确");
        }
        List<BaseSalary> baseSalaryList = new ArrayList<>();
        for (String[] str:
                data) {
            BaseSalary baseSalary;
            for (int i = 0; i < data[0].length; i++) {
                if (str[i] == null) {
                    throw new DataException("员工基本工资表中出现空数据！第"+(i+1)+"行数据为空！");
                }
            }
            try {
                String employeeId =str[0];
                String employeeName = str[1];
                double baseSalaryNum = Double.parseDouble(str[2]);
                baseSalary = new BaseSalary(employeeId,employeeName,baseSalaryNum);
            } catch (Exception e) {
                throw new DataException("员工基本工资表数据格式不正确！");
            }
            if (baseSalaryList.contains(baseSalary)) {
                throw new DataException("员工基本工资表数据中重复！");
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
    static List<BaseSocialSecurity> getBaseSocialSecurityList() throws Exception{
        String handle = Constant.Handle.XX_MONTH_SOCIAL_SECURITY_HANDLE;
        int startRow = Constant.StartRow.BASE_SOCIAL_SECURITY_STARTROW;
        LoadConfigUtil.SheetForm sheetForm = LoadConfigUtil.getFilesAndSheets(handle);
        String filepath = basePath+sheetForm.getFileName();
        String sheetName = sheetForm.getSheetName();
        String[][] data = ReadData.readData(filepath,sheetName,startRow);

        if (data[0].length != Constant.SheetColumnNum.BASE_SOCIAL_SECURITY_COL_NUM) {
            throw new DataException("员工基本社保表格式不正确");
        }
        List<BaseSocialSecurity> baseSocialSecurityList = new ArrayList<>();
        for (String[] str:
                data) {
            for (int i = 0; i < data[0].length-1; i++) {
                if (str[i] == null) {
                    throw new DataException("员工基本社保表中出现空数据！第"+(i+1)+"行数据为空！");
                }
            }
            BaseSocialSecurity baseSocialSecurity;
            try {
                for (int i = 3; i < 22; i++) {
                    double tmpNum = Double.parseDouble(str[i]);
                    if (tmpNum < 0.00) {
                        throw new DataException("员工基本社保表中数据不能为负值！");
                    }
                }
                String employeeId =str[0];
                String employeeName = str[1];
                String socialSecurityCity = str[2];
                String payMonth = str[22];
                baseSocialSecurity = new BaseSocialSecurity(employeeId,employeeName,socialSecurityCity,payMonth);
            } catch (Exception e) {
                throw new DataException("员工基本社保表数据格式不正确！");
            }
            if (baseSocialSecurityList.contains(baseSocialSecurity)) {
                throw new DataException("员工基本社保表数据中重复！");
            }
            baseSocialSecurityList.add(baseSocialSecurity);
        }
        if (baseSocialSecurityList.isEmpty()) {
            throw new DataException("员工基本社保表中数据为空！");
        }
        return baseSocialSecurityList;
    }

    static String[][] getBaseSocialSecurityData() throws Exception{
        String handle = Constant.Handle.XX_MONTH_SOCIAL_SECURITY_HANDLE;
        int startRow = Constant.StartRow.BASE_SOCIAL_SECURITY_STARTROW;
        LoadConfigUtil.SheetForm sheetForm = LoadConfigUtil.getFilesAndSheets(handle);
        String filepath = basePath+sheetForm.getFileName();
        String sheetName = sheetForm.getSheetName();
        String[][] data = ReadData.readData(filepath,sheetName,startRow);
        if (data[0].length != Constant.SheetColumnNum.BASE_SOCIAL_SECURITY_COL_NUM) {
            throw new DataException("员工基本社保表格式不正确");
        }
        for (String[] str: data) {
            if (str == null) {
                throw new DataException("员工基本社保表数据为空！");
            }
            for (String s: str) {
                if (s == null) {
                    throw new DataException("员工基本社保表数据为空！");
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
    static Map<String,String> getEmployeeIdNameMap() throws Exception{
        String handle = Constant.Handle.XX_HANDLE;
        int startRow = Constant.StartRow.EMPLOYEE_ID_NAME_STARTROW;
        LoadConfigUtil.SheetForm sheetForm = LoadConfigUtil.getFilesAndSheets(handle);
        String filepath = basePath+sheetForm.getFileName();
        String sheetName = sheetForm.getSheetName();
        String[][] data = ReadData.readData(filepath,sheetName,startRow);

        int colNums = data[0].length;
        if (colNums != Constant.SheetColumnNum.MAPPING_ID_NAME) {
            throw new DataException("员工id姓名表格式不正确！");
        }
        Map<String,String> employeeIdNameMap = new HashMap<>();
        for (String[] str:
                data) {
            for (int i = 0; i < colNums; i++) {
                if (str[i] == null) {
                    throw new DataException("员工id姓名表中出现空数据！第"+(i+1)+"行数据为空！");
                }
            }
            if (employeeIdNameMap.containsKey(str[1]) || employeeIdNameMap.containsValue(str[0])) {
                throw new DataException("员工id姓名表中出现重复数据！");
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
    static Map<String,String> getWorkTypeMap() throws Exception{
        String handle = Constant.Handle.WORK_TYPE_HANDLE;
        int startRow = Constant.StartRow.WORK_TYPE_STARTROW;
        LoadConfigUtil.SheetForm sheetForm = LoadConfigUtil.getFilesAndSheets(handle);
        String filepath = basePath+sheetForm.getFileName();
        String sheetName = sheetForm.getSheetName();
        String[][] data = ReadData.readData(filepath,sheetName,startRow);

        int colNums = data[0].length;
        if (colNums != Constant.SheetColumnNum.MAPPING_WORKTYPE) {
            throw new DataException("工作类型表格式不正确！");
        }
        Map<String,String> workTypeMap = new HashMap<>();
        for (String[] str:
                data) {
            for (int i = 0; i < colNums; i++) {
                if (str[i] == null) {
                    throw new DataException("工作类型表中出现空数据！第"+(i+1)+"行数据为空！");
                }
            }
            if (workTypeMap.containsKey(str[0])) {
                throw new DataException("工作类型表中出现重复数据！");
            }
            workTypeMap.put(str[0],str[1]);
        }
        if (workTypeMap.isEmpty()) {
            throw new DataException("工作类型表中数据为空！");
        }
        logger.info("工作类型表读取且验证成功！");
        return workTypeMap;
    }
}
