package com.ab.hicarerun.network.models.selfassessmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Arjun Bhatt on 5/21/2020.
 */
public class AssessmentReport {
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
    @SerializedName("Created_On_Text")
    @Expose
    private String createdOnText;
    @SerializedName("CreatedBy")
    @Expose
    private String createdBy;

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

    public String getCreatedOnText() {
        return createdOnText;
    }

    public void setCreatedOnText(String createdOnText) {
        this.createdOnText = createdOnText;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
