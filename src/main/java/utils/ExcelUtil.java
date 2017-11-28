package utils;

import com.alibaba.fastjson.JSONObject;
import constant.Constant;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Created by Tao.Jiang on 2017/11/2.
 */
public final class ExcelUtil {
    private static final Logger logger = LogUtil.setLoggerHanlder(Logger.getLogger(LogUtil.MY_LOGGER), LogUtil.OUTPUTPATH);

    private ExcelUtil() {
    }

    private static HSSFSheet hssfSheet;
    private static HSSFWorkbook hssfWorkbook;

    //获取文件名及数量
    public static ArrayList<JSONObject> fileNames(String fileName) {
        ArrayList<JSONObject> jsonObjectArrayList = new ArrayList<JSONObject>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            String row = null;
            Integer rowNum = 0;
            while ((row = bufferedReader.readLine()) != null) {
                rowNum++;
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("Num", rowNum);
                jsonObject.put("Name", row);
                jsonObjectArrayList.add(jsonObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.severe("Json File Read Failed");
        }
        return jsonObjectArrayList;
    }


    /**
     * 读取excel表
     *
     * @param filePath  文件路径
     * @param sheetName sheet名
     * @param startRow  起始行
     * @return
     * @throws Exception
     */
    public static String[][] readExcel(String filePath, String sheetName, int startRow) {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(filePath);
            hssfWorkbook = new HSSFWorkbook(fileInputStream);
            hssfSheet = hssfWorkbook.getSheet(sheetName);
            //计算实际存在的行数，列数
            int rows = 1;
            int columns = 0;
            if(hssfSheet==null){
                logger.severe("找不到"+sheetName+"表");
                fileInputStream.close();
                return null;
            }
            for (; rows < hssfSheet.getPhysicalNumberOfRows(); rows++) {
                //XX表
                if (sheetName.equals(LoadConfigUtil.getFilesAndSheets(Constant.Handle.XX_HANDLE).getSheetName())) {
                    if (hssfSheet.getRow(rows).getCell(0).getStringCellValue().equals("")) {
                        break;
                    }
                }
                //其他表
                else {
                    if (hssfSheet.getRow(rows).getCell(0) == null) {
                        break;
                    }
                }
                columns = columns > hssfSheet.getRow(rows).getPhysicalNumberOfCells() ? columns : hssfSheet.getRow(rows).getPhysicalNumberOfCells();
            }
            rows = rows - startRow;
            String[][] result = new String[rows][columns];
            //将Excel文件内的数据读入数组
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    if (hssfSheet.getRow(i + startRow).getCell(j) != null) {
                        result[i][j] = hssfSheet.getRow(i + startRow).getCell(j).toString();
                    } else {
                        result[i][j] = "";
                    }
                }
                if (hssfSheet.getRow(i + startRow) == null) {
                    break;
                }
            }
            fileInputStream.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    logger.severe("输入文件关闭失败！文件可能受到损坏！");
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 写入excel
     *
     * @param result
     * @param outputPath      输出路径
     * @param outputSheetName 输出sheet名
     * @throws Exception
     */
    public static boolean writeExcel(String[][] result, String outputPath, String outputSheetName, int startRow, int[] typeArray) {
        FileInputStream fileInputStream = null;
        boolean ifSucceed =false;
        if (result == null) {
            logger.severe("数据有错误，不能写入");
            return ifSucceed;
        }
        try {
            //输入流找到要写的位置
            fileInputStream = new FileInputStream(outputPath);
            POIFSFileSystem poifsFileSystem = new POIFSFileSystem(fileInputStream);
            hssfWorkbook = new HSSFWorkbook(poifsFileSystem);
            //输出流写
            FileOutputStream fileOutputStream = new FileOutputStream(outputPath);
            //判断是否存在表outputSheetName
            for (int i = 0; i < hssfWorkbook.getNumberOfSheets(); i++) {
                if (hssfWorkbook.getSheetName(i).equals(outputSheetName)) {
                    hssfSheet = hssfWorkbook.getSheet(outputSheetName);
                    break;
                }
                if (i == hssfWorkbook.getNumberOfSheets() - 1) {
                    hssfSheet = hssfWorkbook.createSheet(outputSheetName);
                }
            }
            //写入第一行
            Row row = hssfSheet.createRow(startRow - 1);
            for (int j = 0; j < result[0].length; j++) {
                Cell cell = row.createCell(j);
                cell.setCellValue(result[startRow - 1][j]);
            }
            //写入数据
            for (int i = startRow; i < result.length + startRow - 1; i++) {
                row = hssfSheet.createRow(i);
                HSSFCellStyle cellStyle = hssfWorkbook.createCellStyle();
                cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
                for (int j = 0; j < result[0].length; j++) {
                    Cell cell = row.createCell(j);
                    switch (typeArray[j]) {
                        case 0:
                            cell.setCellValue(result[i - startRow + 1][j]);
                            break;
                        case 1:
                            cell.setCellValue(Double.valueOf(result[i - startRow + 1][j]).intValue());
                            break;
                        case 2:
                            if (!(result[i - startRow + 1][j]).equals(" ")) {
                                cell.setCellValue(Double.valueOf(result[i - startRow + 1][j]));
                                cell.setCellStyle(cellStyle);
                            } else {
                                cell.setCellValue(result[i - startRow + 1][j]);
                            }
                            break;
                        default:
                            cell.setCellValue(result[i - startRow + 1][j]);
                            break;
                    }
                }
            }
            fileOutputStream.flush();
            hssfWorkbook.write(fileOutputStream);
            logger.info(outputSheetName + " " + "Written");
            fileInputStream.close();
            ifSucceed = true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.severe(outputPath + " " + "Written Failed");
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    logger.severe("输出文件关闭失败！文件可能受到损坏！");
                    e.printStackTrace();
                }
            }
        }
        return ifSucceed;
    }

}

