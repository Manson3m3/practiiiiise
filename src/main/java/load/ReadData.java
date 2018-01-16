package load;

import exception.DataException;
import utils.ExcelUtil;
import utils.LogUtil;

import java.util.logging.Logger;

/**
 * 读取Excel表中数据到数组中
 * Created by Li.Hou on 2017/11/9.
 */
final class ReadData {

    private static final Logger logger = LogUtil.setLoggerHanlder(Logger.getLogger(LogUtil.MY_LOGGER), LogUtil.OUTPUTPATH);

    static String[][] readData(String filePath, String sheetName, int startRow) throws DataException {
        String[][] data = null;
        try {
            data = ExcelUtil.readExcel(filePath, sheetName, startRow);
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
        if (data == null) {
            throw new DataException(sheetName + "表数据为空！");
        }
        return data;
    }
}
