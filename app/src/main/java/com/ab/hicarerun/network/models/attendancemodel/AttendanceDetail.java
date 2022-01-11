package com.ab.hicarerun.network.models.attendancemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Arjun Bhatt on 10/24/2019.
 */
public class AttendanceDetail {
    @SerializedName("TotalNoOfDays")
    @Expose
    private Integer totalNoOfDays;
    @SerializedName("TotalNoOfDaysPresent")
    @Expose
    private Integer totalNoOfDaysPresent;
    @SerializedName("TotalDaysLateCome")
    @Expose
    private Integer totalDaysLateCome;

    public Integer getTotalNoOfDays() {
        return totalNoOfDays;
    }

    public void setTotalNoOfDays(Integer totalNoOfDays) {
        this.totalNoOfDays = totalNoOfDays;
    }

    public Integer getTotalNoOfDaysPresent() {
        return totalNoOfDaysPresent;
    }

    public void setTotalNoOfDaysPresent(Integer totalNoOfDaysPresent) {
        this.totalNoOfDaysPresent = totalNoOfDaysPresent;
    }

    public Integer getTotalDaysLateCome() {
        return totalDaysLateCome;
    }

    public void setTotalDaysLateCome(Integer totalDaysLateCome) {
        this.totalDaysLateCome = totalDaysLateCome;
    }
}
