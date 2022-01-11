package com.ab.hicarerun.network.models.selfassessmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Arjun Bhatt on 5/20/2020.
 */
public class ResourceCheckList {
    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("DisplayTitle")
    @Expose
    private String displayTitle;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("CType")
    @Expose
    private String cType;
    @SerializedName("ShowText")
    @Expose
    private Boolean showText;
    @SerializedName("IsActive")
    @Expose
    private Boolean isActive;
    @SerializedName("Created_On")
    @Expose
    private String createdOn;
    @SerializedName("Created_by_id_user")
    @Expose
    private Object createdByIdUser;
    @SerializedName("Options")
    @Expose
    private String options;
    @SerializedName("OptionList")
    @Expose
    private List<ResourceOptionList> optionList = null;
    @SerializedName("OptionType")
    @Expose
    private String optionType;
    @SerializedName("MinValue")
    @Expose
    private Integer minValue;
    @SerializedName("MaxValue")
    @Expose
    private Integer maxValue;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDisplayTitle() {
        return displayTitle;
    }

    public void setDisplayTitle(String displayTitle) {
        this.displayTitle = displayTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCType() {
        return cType;
    }

    public void setCType(String cType) {
        this.cType = cType;
    }

    public Boolean getShowText() {
        return showText;
    }

    public void setShowText(Boolean showText) {
        this.showText = showText;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public Object getCreatedByIdUser() {
        return createdByIdUser;
    }

    public void setCreatedByIdUser(Object createdByIdUser) {
        this.createdByIdUser = createdByIdUser;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public List<ResourceOptionList> getOptionList() {
        return optionList;
    }

    public void setOptionList(List<ResourceOptionList> optionList) {
        this.optionList = optionList;
    }

    public String getOptionType() {
        return optionType;
    }

    public void setOptionType(String optionType) {
        this.optionType = optionType;
    }

    public Integer getMinValue() {
        return minValue;
    }

    public void setMinValue(Integer minValue) {
        this.minValue = minValue;
    }

    public Integer getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Integer maxValue) {
        this.maxValue = maxValue;
    }
}
