package com.ab.hicarerun.network.models.selfassessmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Arjun Bhatt on 5/20/2020.
 */
public class SelfAssessmentRequest {
    @SerializedName("OptionId")
    @Expose
    private Integer optionId;
    @SerializedName("OptionTitle")
    @Expose
    private String optionTitle;
    @SerializedName("DisplayOptionTitle")
    @Expose
    private String displayOptionTitle;
    @SerializedName("IsSelected")
    @Expose
    private Boolean isSelected;
    @SerializedName("OptionText")
    @Expose
    private String optionText;
    @SerializedName("Created_On")
    @Expose
    private String createdOn;
    @SerializedName("CreatedBy")
    @Expose
    private String createdBy;
    private String type;
    private int max;
    private int min;

    public SelfAssessmentRequest() {
        optionId = 0;
        optionTitle = "";
        displayOptionTitle = "";
        isSelected = false;
        optionText = "";
        createdOn = "";
        createdBy = "";
    }

    public Integer getOptionId() {
        return optionId;
    }

    public void setOptionId(Integer optionId) {
        this.optionId = optionId;
    }

    public String getOptionTitle() {
        return optionTitle;
    }

    public void setOptionTitle(String optionTitle) {
        this.optionTitle = optionTitle;
    }

    public String getDisplayOptionTitle() {
        return displayOptionTitle;
    }

    public void setDisplayOptionTitle(String displayOptionTitle) {
        this.displayOptionTitle = displayOptionTitle;
    }

    public Boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(Boolean isSelected) {
        this.isSelected = isSelected;
    }

    public String getOptionText() {
        return optionText;
    }

    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }
}
