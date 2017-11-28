package constant;

import utils.JarToolUtil;
import utils.LoadConfigUtil;

/**
 * Created by Tao.Jiang on 2017/11/7.
 */
public class Constant {
    //列数类
    public static class ColumnNum {
        public static final int ABORTCOLUMN = 4;
        public static final int NAMECOLUMN = 0;
        public static final int DEPARTMENTCOLUMN = 1;
        public static final int PROJECTSTARTTIMECOLUMN = 2;
    }

    //sheet表列数
    public static class SheetColumnNum {
        public static final int MAPPING_OA_COL_NUM = 5;
        public static final int WORKING_HOURS_MONTH_REPORT_COL_NUM = 7;
        public static final int BASE_SALART_COL_NUM = 3;
        public static final int BASE_SOCIAL_SECURITY_COL_NUM = 25;
        public static final int MAPPING_ID_NAME = 2;
        public static final int MAPPING_WORKTYPE = 2;
        public static final int XX_MONTH_COST = 18;
        public static final int COST = 18;
        //合并表
        public static final int COALESCING = 26;
    }

    //文件名类
    public static class FileName {
        public static final String TIME_SHEET_DRAFT = "timesheet草稿.xls";
        public static final String TIME_SHEET_TOTAL = "timesheet分配总表.xlsx";
        public static final String TIME_SHEET_1 = "输出表.xls";
    }

    //sheet名类
    public static class SheetName {
        public static final String MAPPING_OA_SHEET_NAME = "Mapping表OA员工信息";
        public static final String WORK_HOURS_MONTHLY_SHEET_NAME = "工时月报表";
        public static final String XX_MONTH_SALARY_PRIMARY_SHEET_NAME = "XX月工资原始";
        public static final String XX_MONTH_SOCIAL_SECURITY_SHEET_NAME = "XX月社保原始";
        public static final String XX_SHEET_NAME = "XX表";
        public static final String WORK_TYPE_SHEET_NAME = "工作类型表";
        public static final String COST_SHEET_NAME = "成本汇总";
        public static final String XX_MONTH_COST_SHEET_NAME = LoadConfigUtil.getMonth() + "月成本汇总";
        public static final String COALESCING_SHEET_NAME = "合并表";
        public static final String WEEK_SHEET_NAME = "周数表";
    }

    //列名类
    public static class ColumnName {
        public static final String APPLICANT = "申请人";
        public static final String APPLY_DEPARTMENT = "申请部门";
        public static final String EMPLOYEE_NO = "雇员编号";
        public static final String EMPLOYEE_NAME = "雇员姓名";
        public static final String START_DATE = "工作期始";
        public static final String WEEK_NUM = "周数";
    }

    //Json路径
    public static class JsonPath {
        public static String JSONPATH = JarToolUtil.getJarDir() + "\\config.json";
    }

    //Handle
    public static class Handle {
        public static final String MAPPING_OA_HANDLE = "Mapping表OA员工信息";
        public static final String WORK_HOURS_MONTHLY_HANDLE = "工时月报表";
        public static final String XX_MONTH_SALARY_PRIMARY_HANDLE = "XX月工资原始";
        public static final String XX_MONTH_SOCIAL_SECURITY_HANDLE = "XX月社保原始";
        public static final String XX_HANDLE = "IdName表";
        public static final String WORK_TYPE_HANDLE = "WorkType表";
        public static final String NEW_STAFF_HANDLE = "新员工专用";
    }

    //输出路径
    public static class JsonRowNum {
        public static final int BASEDIR_ROW_NUM = 1;
        public static final int YEAR_ROW_NUM = 2;
        public static final int MONTH_ROW_NUM = 3;
        public static final int OUTPUTPATH_ROW_NUM = 29;
        public static final int NEW_STAFF_ROW_NUM = 4;
    }

    //startRow
    public static class StartRow {
        public static final int MAPPING_OA_STARTROW = 1;
        public static final int WORKING_HOURS_MONTH_STARTROW = 1;
        public static final int EMPLOYEE_ID_NAME_STARTROW = 1;
        public static final int BASE_SALARY_STARTROW = 3;
        public static final int BASE_SOCIAL_SECURITY_STARTROW = 1;
        public static final int WORK_TYPE_STARTROW = 1;
    }

    //每个输出表的数据类型枚举
    public static class SheetDataTypeArray {
        //0表示String，1表示int，2表示double
        public static final int[] XX_MONTH_COST = {0, 0, 0, 0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2};
        public static final int[] COST = {0, 0, 0, 0, 0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2};
        //合并表
        public static final int[] COALESCING = {0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0};
        public static final int[] WEEK = {0, 0};
    }

    public static class NewStaff {
        public static final String NEW_STAFF = LoadConfigUtil.getNewStaff();
    }

    //每列
    public static class SheetTitleArray {
        public static final String[] XX_MONTH_COST = {"员工编号", "员工姓名", "OA员工信息", "费用归属",
                "费用性质", "薪酬总计", "养老总计", "失业总计", "工伤总计", "生育总计", "医疗总计", "住房公积金总计",
                "补充住房公积金", "管理费总计", "工资支付服务费总计", "成本合计", " ", "人事费用合计"};


        public static final String[] COST = {"OA员工信息", "员工编号", "员工姓名", "OA员工信息",
                "费用归属", "费用性质", "薪酬总计", "养老总计", "失业总计", "工伤总计", "生育总计", "医疗总计",
                "住房公积金总计", "补充住房公积金", "管理费总计", "工资支付服务费总计", "成本合计",
                "人事费用合计"};
        //合并表
        public static final String[] COALESCING = {"年", "月", "工作日期始", "项目编号",
                "项目合计", "周数", "OA员工信息", "状态", "中智员工号", "姓名", "OA部门", "费用归属",
                "费用性质", "薪酬总计", "养老总计", "失业总计", "工伤总计", "生育总计",
                "医疗总计", "住房公积金总计", "补充公积金总计", "管理费总计", "工资支付服务费总计",
                "成本合计", "人事费用合计", "项目名称"
        };
    }

}



