package load;

import exception.DataException;
import utils.ExcelUtil;
import utils.LogUtil;

import java.util.logging.Logger;

/**
 * 读取Excel表中数据到数组中
 * Created by Li.Hou on 2017/11/9.
 */
public final class ReadData {

    private static final Logger logger  = LogUtil.setLoggerHanlder(Logger.getLogger(LogUtil.MY_LOGGER),LogUtil.OUTPUTPATH);
    public static String[][] readData(String fileName,String sheetName,int startRow) throws DataException{
        String[][] data = null;
        try {
            data = ExcelUtil.readExcel(fileName, sheetName, startRow);
        } catch (Exception e) {
           logger.severe(e.getMessage());
        }
        if (data == null) {
            throw new DataException(sheetName+"表数据为空！");
        }
        return data;
    }
}
