package entity;

import com.sun.istack.internal.NotNull;

/**
 * X月原始社保表类
 * Created by Li.Hou on 2017/11/8.
 */

public class BaseSocialSecurity {

    //雇员编号
    @NotNull
    private String employeeId;
    //雇员姓名
    @NotNull
    private String employeeName;
    //社保城市
    @NotNull
    private String socialSecurityCity;
    //缴纳月份
    @NotNull
    private String payMonth;

    public BaseSocialSecurity(String employeeId, String employeeName, String socialSecurityCity, String payMonth) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.socialSecurityCity = socialSecurityCity;
        this.payMonth = payMonth;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public String getSocialSecurityCity() {
        return socialSecurityCity;
    }

    public String getPayMonth() {
        return payMonth;
    }
}
