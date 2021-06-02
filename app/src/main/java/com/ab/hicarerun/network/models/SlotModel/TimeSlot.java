package com.ab.hicarerun.network.models.SlotModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Arjun Bhatt on 10/17/2020.
 */
public class TimeSlot {
    @SerializedName("Selected")
    @Expose
    private Boolean selected;
    @SerializedName("StartDateTime")
    @Expose
    private String startDateTime;
    @SerializedName("StartDate")
    @Expose
    private String startDate;
    @SerializedName("StartTime")
    @Expose
    private String startTime;
    @SerializedName("StartDateTimeText")
    @Expose
    private String startDateTimeText;
    @SerializedName("EndDateTime")
    @Expose
    private String endDateTime;
    @SerializedName("FinishDate")
    @Expose
    private String finishDate;
    @SerializedName("FinishTime")
    @Expose
    private String finishTime;
    @SerializedName("EndDateTimeText")
    @Expose
    private String endDateTimeText;
    @SerializedName("Score")
    @Expose
    private Integer score;
    @SerializedName("Grade")
    @Expose
    private String grade;
    @SerializedName("RulesViolated")
    @Expose
    private Object rulesViolated;

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStartDateTimeText() {
        return startDateTimeText;
    }

    public void setStartDateTimeText(String startDateTimeText) {
        this.startDateTimeText = startDateTimeText;
    }

    public String getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(String finishDate) {
        this.finishDate = finishDate;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public String getEndDateTimeText() {
        return endDateTimeText;
    }

    public void setEndDateTimeText(String endDateTimeText) {
        this.endDateTimeText = endDateTimeText;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Object getRulesViolated() {
        return rulesViolated;
    }

    public void setRulesViolated(Object rulesViolated) {
        this.rulesViolated = rulesViolated;
    }

}
