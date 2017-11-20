package exception;

import java.io.IOException;

/**
 * Created by Tao.Jiang on 2017/11/7.
 */
public class ExcelIOException extends IOException {
    //错误信息
    private String errorInfo;

    public ExcelIOException(String errorInfo){
        this.errorInfo=errorInfo;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    @Override
    public String toString(){return errorInfo;}
}
