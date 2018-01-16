package entity;

import org.apache.poi.util.StringUtil;

/**
 * Created by Tao.Jiang on 2017/12/27.
 */
public class ErrorRecord {

    private String errorPeriod;
    private String level;
    private String staff;
    private String code;
    private String errorInfo;

    public ErrorRecord(String errorPeriod, String level, String staff, String code,String errorInfo){
        this.errorPeriod = errorPeriod;
        this.level = level;
        this.staff = staff;
        this.code = code;
        this.errorInfo = errorInfo;
    }

    public String[] toArray(){
        String[] strings = new String[5];
        strings[0] = this.errorPeriod;
        strings[1] = this.level;
        strings[2] = this.staff;
        strings[3] = this.code;
        strings[4] = this.errorInfo;
        return strings;
    }
}

