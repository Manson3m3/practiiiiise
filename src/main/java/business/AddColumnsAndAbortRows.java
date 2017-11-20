package business;

import constant.Constant;
import utils.CalendarUtil;
import utils.LogUtil;

import java.util.LinkedList;
import java.util.logging.Logger;

/**
 * Created by Tao.Jiang on 2017/11/7.
 */
public class AddColumnsAndAbortRows {
    private static final Logger logger= LogUtil.setLoggerHanlder(Logger.getLogger(LogUtil.MY_LOGGER),LogUtil.OUTPUTPATH);

    /**
     * 增加周数列，OA员工信息列,删除一些“新员工专属”行
     *
     * @param in 目标表数据
     * @return
     */
    public static String[][] addColumnsAndAbortRows(String[][] in) {
        //记录“新员工专属”数量及位置
        LinkedList<Integer> abortRows = new LinkedList<>();
        for (int i = 0; i < in.length; i++) {
            if (in[i][Constant.ColumnNum.ABORTCOLUMN].equals(""))
                abortRows.add(i);
        }
        //返回结果有in.length+2行，in[1].length-abortRows.size()列
        String[][] result = new String[in.length - abortRows.size()][in[0].length];
        //记录当前“新员工专属”
        int count = 1;
        for (int i = 0; i < result.length; i++) {
            //如果当前行是abortRow
            if (abortRows.size() > 0 && i == abortRows.get(count)) {
                count++;
                continue;
            }
            for (int j = 0; j < in[i].length; j++) {
                result[i - count + 1][j] = in[i][j];
            }
//            //result的第i行倒数第二列是in的第i行第四列的万年历值
//            result[i][in[i].length] = CalendarUtil.getWeekNum(in[i][Constant.ColumnNum.PROJECTSTARTTIMECOLUMN]).toString();
//            //result的第i行倒数第一列是in的第i行第二列和第三列相加
//            result[i][in[i].length + 1] = in[i][Constant.ColumnNum.NAMECOLUMN] + in[i][Constant.ColumnNum.DEPARTMENTCOLUMN];
        }
        logger.info("Add Columns And Abort Rows Succeeded");
        return result;
    }
}
