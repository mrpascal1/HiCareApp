package com.ab.hicarerun.network.models.TechnicianRoutineModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Arjun Bhatt on 8/13/2020.
 */
public class TechnicianData {
    @SerializedName("TechnicianId")
    @Expose
    private String technicianId;
    @SerializedName("TechnicianName")
    @Expose
    private String technicianName;
    @SerializedName("EmployeeCode")
    @Expose
    private String employeeCode;
    @SerializedName("MobileNo")
    @Expose
    private String mobileNo;
    @SerializedName("RoutineChecklistSubmitted")
    @Expose
    private Boolean routineChecklistSubmitted;

    public String getTechnicianId() {
        return technicianId;
    }

    public void setTechnicianId(String technicianId) {
        this.technicianId = technicianId;
    }

    public String getTechnicianName() {
        return technicianName;
    }

    public void setTechnicianName(String technicianName) {
        this.technicianName = technicianName;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public Boolean getRoutineChecklistSubmitted() {
        return routineChecklistSubmitted;
    }

    public void setRoutineChecklistSubmitted(Boolean routineChecklistSubmitted) {
        this.routineChecklistSubmitted = routineChecklistSubmitted;
    }
}
