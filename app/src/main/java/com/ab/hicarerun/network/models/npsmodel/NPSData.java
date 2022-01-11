package com.ab.hicarerun.network.models.npsmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Arjun Bhatt on 3/4/2020.
 */
public class NPSData {
    @SerializedName("TotalJobs")
    @Expose
    private String totalJobs;
    @SerializedName("Monthly_NPS")
    @Expose
    private String monthlyNPS;
    @SerializedName("PreviousDay_NPS")
    @Expose
    private String previousDayNPS;
    @SerializedName("Tech_Badge")
    @Expose
    private String techBadge;
    @SerializedName("NPS_Month")
    @Expose
    private String NPS_Month;

    public String getTotalJobs() {
        return totalJobs;
    }

    public void setTotalJobs(String totalJobs) {
        this.totalJobs = totalJobs;
    }

    public String getMonthlyNPS() {
        return monthlyNPS;
    }

    public void setMonthlyNPS(String monthlyNPS) {
        this.monthlyNPS = monthlyNPS;
    }

    public String getPreviousDayNPS() {
        return previousDayNPS;
    }

    public void setPreviousDayNPS(String previousDayNPS) {
        this.previousDayNPS = previousDayNPS;
    }

    public String getTechBadge() {
        return techBadge;
    }

    public void setTechBadge(String techBadge) {
        this.techBadge = techBadge;
    }

    public String getNPS_Month() {
        return NPS_Month;
    }

    public void setNPS_Month(String NPS_Month) {
        this.NPS_Month = NPS_Month;
    }
}
