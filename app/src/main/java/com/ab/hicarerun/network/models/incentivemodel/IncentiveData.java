package com.ab.hicarerun.network.models.incentivemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Arjun Bhatt on 7/10/2020.
 */
public class IncentiveData {
    @SerializedName("TotalPoints")
    @Expose
    private Integer totalPoints;
    @SerializedName("ProgressBarPercent")
    @Expose
    private Integer progressBarPercent;
    @SerializedName("TotalIncentiveAmount")
    @Expose
    private Integer totalIncentiveAmount;
    @SerializedName("ResourceId")
    @Expose
    private String resourceId;
    @SerializedName("ResourceName")
    @Expose
    private String resourceName;
    @SerializedName("Month")
    @Expose
    private String month;
    @SerializedName("Year")
    @Expose
    private String year;
    @SerializedName("IncentiveCriteriaList")
    @Expose
    private List<IncentiveCriteriaList> incentiveCriteriaList = null;
    @SerializedName("IncentiveDetailList")
    @Expose
    private List<IncentiiveDetailList> incentiveDetailList = null;
    @SerializedName("MonthList")
    @Expose
    private List<IncentiveMonthList> incentiveMonthList = null;

    public Integer getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(Integer totalPoints) {
        this.totalPoints = totalPoints;
    }

    public Integer getProgressBarPercent() {
        return progressBarPercent;
    }

    public void setProgressBarPercent(Integer progressBarPercent) {
        this.progressBarPercent = progressBarPercent;
    }

    public Integer getTotalIncentiveAmount() {
        return totalIncentiveAmount;
    }

    public void setTotalIncentiveAmount(Integer totalIncentiveAmount) {
        this.totalIncentiveAmount = totalIncentiveAmount;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public List<IncentiveCriteriaList> getIncentiveCriteriaList() {
        return incentiveCriteriaList;
    }

    public void setIncentiveCriteriaList(List<IncentiveCriteriaList> incentiveCriteriaList) {
        this.incentiveCriteriaList = incentiveCriteriaList;
    }

    public List<IncentiiveDetailList> getIncentiveDetailList() {
        return incentiveDetailList;
    }

    public void setIncentiveDetailList(List<IncentiiveDetailList> incentiveDetailList) {
        this.incentiveDetailList = incentiveDetailList;
    }

    public List<IncentiveMonthList> getIncentiveMonthList() {
        return incentiveMonthList;
    }

    public void setIncentiveMonthList(List<IncentiveMonthList> incentiveMonthList) {
        this.incentiveMonthList = incentiveMonthList;
    }
}
