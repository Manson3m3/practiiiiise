package load;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/** 
* Step2DataProcessor Tester. 
* 
* @author Li.Hou
* @date 01/03/2018
*/ 
public class Step2DataProcessorTest { 

    @Before
    public void before() throws Exception {

    } 

    @After
    public void after() throws Exception { 
    } 

    /** 
    * 
    * Method: isDevProj(String projectId) 
    * 
    */ 
    @Test
    public void testIsDevProj() throws Exception { 
        String[] testCases = {"PNA001","PNA003","市场推广"};
        boolean[] expected = {true,false,false};
        boolean[] results = new boolean[3];
        for (int i = 0; i < testCases.length ; i++) {
            results[i] = Step2DataProcessor.isDevProj(testCases[i]);
        }
        Assert.assertArrayEquals(expected,results);
    } 

    /** 
    * 
    * Method: isDevDept(String department) 
    * 
    */ 
    @Test
    public void testIsDevDept() throws Exception {
        String[] testCases = {"一甲", "二乙"};
        boolean[] expected = {false,true};
        boolean[] actuals = new boolean[2];
        for (int i = 0; i < testCases.length ; i++) {
            actuals[i] = Step2DataProcessor.isDevDept(testCases[i]);
        }
        Assert.assertArrayEquals(expected,actuals);
    } 

    /** 
    * 
    * Method: getDeptId(String department) 
    * 
    */ 
    @Test
    public void testGetDeptId() throws Exception {
        String[] testCases = {"一甲", "二乙"};
        String[] expected = {"01","02"};
        String[] actuals = new String[2];
        for (int i = 0; i < testCases.length ; i++) {
            actuals[i] = Step2DataProcessor.getDeptId(testCases[i]);
        }
        Assert.assertArrayEquals(expected,actuals);
    } 

    @Test
    public void testGetDepartmentData() throws Exception {
        String[][] result = Step2DataProcessor.departmentData;

        Assert.assertNotNull("部门数据不可以为空！",result);
    }

    @Test
    public void testGetOAProData() throws Exception {
        String[][] result = Step2DataProcessor.OAProjectData;
        Assert.assertNotNull("OA项目数据不可以为空！",result);
    }

    @Test
    public void testGetSubjectCodeData() throws Exception {
        String[][] result = Step2DataProcessor.subjectCodeData;
        Assert.assertNotNull("科目编码表不可以为空！",result);
    }
} 
