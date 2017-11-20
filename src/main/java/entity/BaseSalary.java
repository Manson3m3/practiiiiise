package entity;


import com.sun.istack.internal.NotNull;

/**
 * X月原始工资表类
 * Created by Li.Hou on 2017/11/8.
 */


public class BaseSalary {

    //雇员编号
    @NotNull
    private String employeeId;
    //雇员姓名
    @NotNull
    private String employeeName;
    //基础薪资
    @NotNull
    private double baseSalary;

    public BaseSalary(String employeeId, String employeeName, double baseSalary) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.baseSalary = baseSalary;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public double getBaseSalary() {
        return baseSalary;
    }
}
