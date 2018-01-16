package load;

import exception.DataException;
import utils.LogUtil;

import java.util.Arrays;
import java.util.logging.Logger;

/**
 * Created on 2017/12/29.
 */
public class Step2DataProcessor {

    private static final Logger logger = LogUtil.setLoggerHanlder(Logger.getLogger(LogUtil.MY_LOGGER), LogUtil.OUTPUTPATH);
    public static final String[][] departmentData = getDepartmentData();
    public static final String[][] OAProjectData = getOAProData();
    public static final String[][] subjectCodeData = getSubjectCodeData();
    public static final String[] costName = {"工资", "养老保险", "失业保险", "工伤保险", "生育保险",
            "医疗保险", "住房公积金", "补充公积金"};


    /**
     * 获取部门表数据
     *
     * @return
     */
    private static String[][] getDepartmentData() {
        String[][] result = null;
        try {
            result = LoadInputData.getDepartmentData();
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
        if (result != null) {
            logger.info("获取部门表数据成功！");
        }
        return result;
    }

    /**
     * 获取OA项目表
     *
     * @return
     */
    private static String[][] getOAProData() {
        String[][] result = null;
        try {
            result = LoadInputData.getOAProData();
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
        if (result != null) {
            logger.info("获取OA项目表数据成功！");
        }
        if (result == null) {
            logger.severe("OA项目表数据为空！");
        }
        return result;
    }

    private static String[][] getSubjectCodeData() {
        String[][] result = null;
        try {
            result = LoadInputData.getSubjectCodeData();
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
        if (result != null) {
            logger.info("获取科目编码表数据成功！");
        }
        return result;
    }

    /**
     * 判断是否研发项目
     *
     * @param projectId 项目编码
     * @return
     */
    public static boolean isDevProj(String projectId) throws DataException {
        if (OAProjectData == null || OAProjectData.length == 0) {
            String logInfo = "OA项目数据为空！";
            logger.severe(logInfo);
            throw new DataException(logInfo);
        }
        for (String[] strings : OAProjectData) {
            if (strings[4].equals(projectId)) {
                return strings[9].equals("是");
            }
        }
        String logInfo = "OA项目表中不存在该项目编码，项目编码为："+projectId;
        logger.severe(logInfo);
        throw new DataException(logInfo);
    }

    /**
     * 判断是否研发部门
     *
     * @param department 部门名称
     * @return 是否研发
     */
    public static boolean isDevDept(String department) throws DataException {
        for (String[] strings : departmentData) {
            if (strings[1].equals(department)) {
                return strings[2] != null && strings[2].equals("是");
            }
        }
        throw new DataException("部门表中不包含该部门数据，部门名称为："+department);
    }

    /**
     * 获取部门编号
     *
     * @param department 部门名称
     * @return 部门编号
     */
    public static String getDeptId(String department) throws DataException{
        for (String[] strings : departmentData) {
            if (strings[1].equals(department)) {
                return strings[0];
            }
        }
        throw new DataException("部门表中不包含该部门数据，部门名称为："+department);
    }

    /**
     * 获取科目编码
     *
     * @param subjectName 科目名称
     * @return 科目编码
     */
    public static String getSubjectCode(String subjectName) throws DataException {
        for (String[] strings : subjectCodeData) {
            if (strings[3].trim().equals(subjectName)) {
                return strings[2];
            }
        }
        throw new DataException("科目代码表中不含该科目数据,科目名称为："+subjectName);
    }
}
