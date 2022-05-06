package com.ab.hicarerun.network.models.incentivemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IncentiveMonthList {
    @SerializedName("MonthAndYear")
    @Expose
    private String monthAndYear;

    @SerializedName("IsSelected")
    @Expose
    private Boolean IsSelected;

    public String getMonthAndYear() {
        return monthAndYear;
    }

    public void setMonthAndYear(String monthAndYear) {
        this.monthAndYear = monthAndYear;
    }

    public Boolean getSelected() {
        return IsSelected;
    }

    public void setSelected(Boolean selected) {
        IsSelected = selected;
    }
}
