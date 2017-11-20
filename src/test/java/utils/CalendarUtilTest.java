package utils; 

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;

import java.text.SimpleDateFormat;
import java.util.Date;

/** 
* CalendarUtil Tester. 
* 
* @author Li.Hou
* @date 11/07/2017
*/ 
public class CalendarUtilTest { 

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
    public void testGetWeekNums() throws Exception {
        String [][] result = CalendarUtil.getWeekNums();
        Assert.assertNotNull(result);
    }

    /**
     *
     * Method: getWeekNum(String date)
     *
     */
    @Test
    public void testGetWeekNum() throws Exception {
        int[] actuals = new int[3];
        actuals[0] = CalendarUtil.getWeekNum("2017-11-07");
        actuals[1] = CalendarUtil.getWeekNum("2018-01-01");
        actuals[2] = CalendarUtil.getWeekNum("2018-12-31");

        int[] expecteds = {45,1,53};
        assertArrayEquals(expecteds,actuals);
    }


    /**
    *
    * Method: isMonday(String date)
    *
    */
    @Test
    public void testIsMonday() throws Exception {
        String date = "2017-11-09";
        boolean result = CalendarUtil.isMonday(date);
        Assert.assertFalse(result);
    }

    /**
    *
    * Method: inThisMonth(int year, int month, String date)
    *
    */
    @Test
    public void testInThisMonthAndIsMonday() throws Exception {
        boolean[] actuals = new boolean[6];
        actuals[0] = CalendarUtil.inThisMonthAndIsMonday(2017,11,"2017-11-09");
        actuals[1] = CalendarUtil.inThisMonthAndIsMonday(2017,11,"2017-11-20");
        actuals[2] = CalendarUtil.inThisMonthAndIsMonday(2017,10,"2017-10-23");
        actuals[3] = CalendarUtil.inThisMonthAndIsMonday(2017,11,"2017-10-30");
        actuals[4] = CalendarUtil.inThisMonthAndIsMonday(2017,5,"2017-05-29");
        actuals[5] = CalendarUtil.inThisMonthAndIsMonday(2018,4,"2018-04-30");
        boolean[] expecteds = {false,true,true,true,true,false};
        Assert.assertArrayEquals(expecteds,actuals);
    }

    /**
    *
    * Method: isSameDate(Date date1, Date date2)
    *
    */
    @Test
    public void testIsSameDate() throws Exception {
        boolean[] actuals = new boolean[2];
        SimpleDateFormat s = CalendarUtil.simpleDateFormat;
        actuals[0] = CalendarUtil.isSameDate(s.parse("2017-11-09"),s.parse("2017-11-09"));
        actuals[1] = CalendarUtil.isSameDate(s.parse("2017-11-09"),s.parse("2018-12-31"));
        boolean[] expecteds = {true,false};
        Assert.assertArrayEquals(expecteds,actuals);
    } 

    @Test
    public void testWeeksOfMonth()throws Exception{
        int[] actuals = new int[4];
        String[] actualsDate = new String[4];
        actualsDate[0] = "2017-07";
        actualsDate[1] = "2017-08";
        actualsDate[2] = "2017-09";
        actualsDate[3] = "2017-10";
        actuals[0]=CalendarUtil.weeksOfMonth(actualsDate[0]);
        actuals[1]=CalendarUtil.weeksOfMonth(actualsDate[1]);
        actuals[2]=CalendarUtil.weeksOfMonth(actualsDate[2]);
        actuals[3]=CalendarUtil.weeksOfMonth(actualsDate[3]);
        int[] expect ={4,5,4,4};
        Assert.assertArrayEquals(actuals,expect);
    }

} 
