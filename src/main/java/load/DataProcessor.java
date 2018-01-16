package load;

import entity.BaseSalary;
import entity.BaseSocialSecurity;
import entity.MappingOA;
import entity.WorkingHoursMonthReport;
import exception.DataException;
import utils.LogUtil;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 * 此类封装了对数据的操作，
 * 比如从数组中获取某列数据，从列表中获取某个对象，以及获取具体字段等
 * Created by Li.Hou on 2017/11/13.
 */
public class DataProcessor {

    private static final Logger logger = LogUtil.setLoggerHanlder(Logger.getLogger(LogUtil.MY_LOGGER), LogUtil.OUTPUTPATH);

    public static final List<BaseSalary> baseSalaryList = getBaseSalaryList();
    public static final List<MappingOA> mappingOAList = getMappingOAList();
    public static final List<WorkingHoursMonthReport> workHoursMonthList = getWorkingHoursMonthReportList();

    public static final String[][] mappingOAData = getMappingOAData();
    public static final String[][] baseSocialSecurityData = getBaseSocialSecurityData();
    public static final String[][] workHoursMonthData = getWorkingHoursMonthData();

    public static final Map<String,String> workTypeMap = getWorkTypeMap();
    public static final Map<String, String> employeeIdNameMap = getEmployeeIdNameMap();

    /**
     * 根据用户id获取mappingOA员工信息
     *
     * @param employeeId 用户id
     * @return
     */
    public static MappingOA getMappingOAById(String employeeId) {
        if (employeeId == null) {
            return null;
        }
        if (mappingOAList == null || mappingOAList.isEmpty()) {
            return null;
        }
        for (MappingOA m :
                mappingOAList) {
            if (employeeId.equals(m.getEmpolyeeId())) {
                return m;
            }
        }
        return null;
    }

    /**
     * 获取mappingOAlist
     *
     * @return
     */
    private static List<MappingOA> getMappingOAList() {
        List<MappingOA> list = null;
        try {
            list = LoadInputData.getMappingOAList();
        } catch (DataException d) {
            logger.severe(d.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            logger.severe(e.getMessage());

        }
        if (list != null) {
            logger.info("读取mappingOA列表成功！");
        }
        return list;
    }

    private static String[][] getMappingOAData() {
        String[][] result = null;
        try {
            result = LoadInputData.getMappingOAData();
        } catch (DataException d) {
            logger.severe(d.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            logger.severe(e.getMessage());
        }
        if (result != null) {
            logger.info("读取mappingOA数组数据成功！");
        }
        return result;
    }

    /**
     * 获取员工idName映射
     *
     * @return
     */
    private static Map<String, String> getEmployeeIdNameMap() {
        Map<String, String> map = null;
        try {
            map = LoadInputData.getEmployeeIdNameMap();
        } catch (DataException d) {
            logger.severe(d.getMessage());
        } catch (Exception e) {

            logger.severe(e.getMessage());
            e.printStackTrace();
        }
        if (map != null) {
//            logger.info("读取员工idName映射成功！");
        }
        return map;
    }

    /**
     * 根据姓名获取id
     *
     * @param name
     * @return
     */
    public static String getIdbyName(String name) {
        for (Map.Entry<String, String> entry :
                employeeIdNameMap.entrySet()) {
            if (entry.getValue().equals(name)) {
                return entry.getKey();
            }

        }
        return null;
    }

    /**
     * 获取月报表列表
     *
     * @return
     */
    private static List<WorkingHoursMonthReport> getWorkingHoursMonthReportList() {
        List<WorkingHoursMonthReport> workingHoursMonthReportList = null;
        try {
            workingHoursMonthReportList = LoadInputData.getWorkingHoursMonthReportList();
        } catch (DataException d) {
            logger.severe(d.getMessage());
        } catch (Exception e) {
            logger.severe(e.getMessage());
            e.printStackTrace();
        }
        if (workingHoursMonthReportList != null) {
            logger.info("读取月报表成功！");
        }
        return workingHoursMonthReportList;
    }

    private static String[][] getWorkingHoursMonthData() {
        String[][] result = null;
        try {
            result = LoadInputData.getWorkingHoursMonthData();
        } catch (DataException d) {
            logger.severe(d.getMessage());
        } catch (Exception e) {
            logger.severe(e.getMessage());
            e.printStackTrace();
        }
        if (result != null) {
            logger.info("读取月报表数组成功！");
        }
        return result;
    }

    /**
     * 根据申请人，起始日期，项目id获取一条月报表数据
     *
     * @param applicant
     * @param start
     * @param projectId
     * @return
     */
    public static WorkingHoursMonthReport getWHMonthDataByNameAndStartDateAndProId(String applicant, String start, String projectId) {
        if (applicant == null || start == null || projectId == null) {
            return null;
        }
        List<WorkingHoursMonthReport> workingHoursMonthReportList = getWorkingHoursMonthReportList();
        if (workingHoursMonthReportList == null || workingHoursMonthReportList.isEmpty()) {
            return null;
        }
        for (WorkingHoursMonthReport w :
                workingHoursMonthReportList) {
            if (applicant.equals(w.getApplicant()) && start.equals(w.getStartDate()) && projectId.equals(w.getProjectId())) {
                return w;
            }
        }
        return null;
    }

    /**
     * 获取基本工资列表
     *
     * @return
     */
    private static List<BaseSalary> getBaseSalaryList() {
        List<BaseSalary> baseSalaryList = null;
        try {
            baseSalaryList = LoadInputData.getBaseSalaryList();
        } catch (DataException d) {
            logger.severe(d.getMessage());
        } catch (Exception e) {
            logger.severe(e.getMessage());
            e.printStackTrace();
        }
        if (baseSalaryList != null) {
            logger.info("读取基本薪资表成功！");
        }
        return baseSalaryList;
    }

    /**
     * 根据雇员id获取一条薪资信息
     *
     * @param employeeId
     * @return
     */
    public static BaseSalary getBaseSalaryById(String employeeId) {
        if (employeeId == null) {
            return null;
        }
        if (baseSalaryList == null || baseSalaryList.isEmpty()) {
            return null;
        }
        for (BaseSalary b :
                baseSalaryList) {
            if (employeeId.equals(b.getEmployeeId())) {
                return b;
            }

        }
        return null;
    }

    /**
     * 获取基本社保表列表
     *
     * @return
     */
    private static List<BaseSocialSecurity> getBaseSocialSecurityList() {
        List<BaseSocialSecurity> baseSocialSecurityList = null;
        try {
            baseSocialSecurityList = LoadInputData.getBaseSocialSecurityList();
        } catch (DataException d) {
            logger.severe(d.getMessage());
        } catch (Exception e) {
            logger.severe(e.getMessage());
            e.printStackTrace();
        }
        if (baseSocialSecurityList != null) {
            logger.info("读取基本社保表成功！");
        }
        return baseSocialSecurityList;
    }

    /**
     * 获取数组数据
     *
     * @return
     */
    private static String[][] getBaseSocialSecurityData() {
        String[][] result = null;
        try {
            result = LoadInputData.getBaseSocialSecurityData();
        } catch (DataException d) {
            logger.severe(d.getMessage());
        } catch (Exception e) {
            logger.severe(e.getMessage());
            e.printStackTrace();
        }
        if (result != null) {
            logger.info("读取基本社保表成功！");
        }
        return result;
    }

    /**
     * 根据id获取一条社保数据
     *
     * @param employeeId
     * @return
     */
    public static String[] getBaseSocialSecurityById(String employeeId) {
        if (employeeId == null) {
            return null;
        }

        for (String[] str : baseSocialSecurityData) {
            if (str == null) {
                return null;
            }
            if (employeeId.equals(str[0])) {
                return str;
            }
        }
        return null;
    }

    /**
     * 获取工作类型映射
     *
     * @return
     */
    private static Map<String, String> getWorkTypeMap() {
        Map<String, String> workTypeMap = null;
        try {
            workTypeMap = LoadInputData.getWorkTypeMap();
        } catch (DataException d) {
            logger.severe(d.getMessage());
        } catch (Exception e) {
            logger.severe(e.getMessage());
            e.printStackTrace();
        }
        if (workTypeMap != null) {
            logger.info("获取工作类型表成功！");
        }
        return workTypeMap;
    }

    /**
     * 将set转化为字符串
     *
     * @param set
     * @return
     */
    public static String setToStrings(Set<String> set) {
        if (set == null || set.size() == 0) {
            return null;
        }
        String string = null;
        for (String s : set) {
            if (string == null) {
                string = s;
            } else {
                string = string + "," + s;
            }
        }
        return string;
    }

}
