package business;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

/** 
* AddColumnsAndAbortRows Tester.
* 
* @author Jiang.tao
* @date 11/07/2017
*/ 
public class AddColumnsAndAbortRowsTest {

    @Before
    public void before() throws Exception { 
    } 

    @After
    public void after() throws Exception { 
    } 

    /** 
    * 
    * Method: getWeekNums(Date start, Date end) 
    * 
    */ 
    @Test
    public void testAddColumnsAndAbortRows() throws Exception {
        String[][] in = {{"韩梅梅","公司高管","2017-10-09","2017-10-15","2016-SC-ALL-R&D","4.0","GSTB-201709-624"},{"韩梅梅","公司高管","2017-10-09","2017-10-15","2016-SC-ALL-R&D","4.0","GSTB-201709-624"},{"","","","","",""},{"","","","","",""}};
        String[][] expect = AddColumnsAndAbortRows.addColumnsAndAbortRows(in);
        String[][] result ={{"韩梅梅","公司高管","2017-10-09","2017-10-15","2016-SC-ALL-R&D","4.0","GSTB-201709-624","41","韩梅梅公司高管"},{"韩梅梅","公司高管","2017-10-09","2017-10-15","2016-SC-ALL-R&D","4.0","GSTB-201709-624","41","韩梅梅公司高管"}};
        assertArrayEquals(expect,result);
    }
} 
