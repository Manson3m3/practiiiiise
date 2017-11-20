package utils;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

/**
 * Created by Tao.Jiang on 2017/11/9.
 */
public class LogUtil {

    private LogUtil() {
    }

    public static final String OUTPUTPATH = setOutputpath();
    public static final String MY_LOGGER = "my.logger";
    private static final SimpleDateFormat sdf = new SimpleDateFormat(
            "yyyy-MM-dd");

    public static final String LOG_FOLDER_NAME = setOutputpath();

    private static final String LOG_FILE_SUFFIX = ".log";

    private static final FileHandler fileHandler = getFileHandler();

    private static MyLogFormatter myLogFormatter = new MyLogFormatter();

    private synchronized static String getLogFilePath(String outputPath) {
        StringBuffer logFilePath = new StringBuffer();
        logFilePath.append(LOG_FOLDER_NAME);

        File file = new File(logFilePath.toString());
        if (!file.exists())
            file.mkdirs();

        logFilePath.append(File.separatorChar);
        logFilePath.append(sdf.format(new Date()));
        logFilePath.append(LOG_FILE_SUFFIX);

        return logFilePath.toString();
    }

    public synchronized static Logger setLoggerHanlder(Logger logger, String outputPath) {
        return setLoggerHanlder(logger, Level.ALL, outputPath);
    }

    public synchronized static Logger setLoggerHanlder(Logger logger,
                                                       Level level, String outputPath) {
        try {

            //以文本的形式输出
            fileHandler.setFormatter(myLogFormatter);

            if (logger.getHandlers().length == 0) {
                logger.addHandler(fileHandler);
            }
            logger.setLevel(level);

        } catch (SecurityException e) {
            logger.severe(populateExceptionStackTrace(e));
        }
        return logger;
    }

    private synchronized static String populateExceptionStackTrace(Exception e) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(e.toString()).append("\n");
        for (StackTraceElement elem : e.getStackTrace()) {
            stringBuilder.append("\tat ").append(elem).append("\n");
        }
        return stringBuilder.toString();
    }

    private static FileHandler getFileHandler() {
        FileHandler fileHandler = null;
        boolean APPEND_MODE = true;
        try {
            //文件日志内容标记为可追加
            fileHandler = new FileHandler(getLogFilePath(LogUtil.OUTPUTPATH), APPEND_MODE);
            return fileHandler;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取输出文件夹的路径
     *
     * @return
     */
    private static String setOutputpath() {
        String outputPath = null;
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/ddhh:mm:ss");
        outputPath = JarToolUtil.getJarDir() + "\\" + dateFormat.format(new Date()).replaceAll("/", "").replaceAll(":", "");
        return outputPath;
    }

    /**
     * Created by Tao.Jiang on 2017/11/17.
     */
    //日志格式
    static class MyLogFormatter extends Formatter {

        // Create a DateFormat to format the logger timestamp.
        private static final DateFormat df = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss.SSS");

        public String format(LogRecord record) {
            StringBuilder builder = new StringBuilder(1000);
            builder.append("[").append(record.getLevel()).append("] - ");
            builder.append(df.format(new Date(record.getMillis()))).append(" - ");
            builder.append("[").append(record.getSourceClassName()).append(".");
            builder.append(record.getSourceMethodName()).append("]  \r\n");
            builder.append(" \t\t ");
            builder.append(formatMessage(record));
            builder.append(" \r\n\r\n ");
            return builder.toString();
        }

        public String getHead(Handler handler) {
            return super.getHead(handler);
        }

        public String getTail(Handler handler) {
            return super.getTail(handler);
        }
    }
}
