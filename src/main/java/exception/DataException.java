package exception;

/**
 * 处理数据异常
 * Created by Li.Hou on 2017/11/8.
 */
public class DataException extends Exception {
    //错误信息

    public DataException(String message) {
        super(message);
    }

    public DataException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    @Override
    public synchronized Throwable getCause() {
        return super.getCause();
    }
}
