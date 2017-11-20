package entity;

import com.sun.istack.internal.NotNull;

/**
 * mapping OA 员工表类
 * Created by Li.Hou on 2017/11/8.
 */

public class MappingOA {
    //员工编号
    @NotNull
    private String empolyeeId;
    //员工姓名
    @NotNull
    private String empolyeeName;
    //OA员工信息
    @NotNull
    private String OAEmployeeInfo;
    //费用归属
    @NotNull
    private String costAssignment;
    //费用性质
    @NotNull
    private String costNature;

    public MappingOA(String empolyeeId, String empolyeeName, String OAEmployeeInfo, String costAssignment, String costNature) {
        this.empolyeeId = empolyeeId;
        this.empolyeeName = empolyeeName;
        this.OAEmployeeInfo = OAEmployeeInfo;
        this.costAssignment = costAssignment;
        this.costNature = costNature;
    }

    public String getEmpolyeeId() {
        return empolyeeId;
    }

    public String getEmpolyeeName() {
        return empolyeeName;
    }

    public String getOAEmployeeInfo() {
        return OAEmployeeInfo;
    }

    public String getCostAssignment() {
        return costAssignment;
    }

    public String getCostNature() {
        return costNature;
    }
}
