package entity;

import com.sun.istack.internal.NotNull;

/**
 * 工时月报表类
 * Created by Li.Hou on 2017/11/8.
 */


public class WorkingHoursMonthReport {

    //申请人
    @NotNull
    private String applicant;
    //申请部门
    @NotNull
    private String applyDepartment;
    //工作期始
    @NotNull
    private String startDate;
    //工作日期止
    @NotNull
    private String endDate;
    //项目编号
    @NotNull
    private String projectId;
    //项目合计
    @NotNull
    private double projectTotal;
    //单号
    @NotNull
    private String listId;

    public WorkingHoursMonthReport(String applicant, String applyDepartment, String startDate, String endDate, String projectId, double projectTotal, String listId) {
        this.applicant = applicant;
        this.applyDepartment = applyDepartment;
        this.startDate = startDate;
        this.endDate = endDate;
        this.projectId = projectId;
        this.projectTotal = projectTotal;
        this.listId = listId;
    }

    public String getApplicant() {
        return applicant;
    }

    public String getApplyDepartment() {
        return applyDepartment;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getProjectId() {
        return projectId;
    }

    public double getProjectTotal() {
        return projectTotal;
    }

    public String getListId() {
        return listId;
    }
}
