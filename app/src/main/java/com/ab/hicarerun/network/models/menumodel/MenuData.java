package com.ab.hicarerun.network.models.menumodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MenuData {
    @SerializedName("Sequence")
    @Expose
    private Integer sequence;
    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("Icon")
    @Expose
    private String icon;
    @SerializedName("IsVisible")
    @Expose
    private Boolean isVisible;
    @SerializedName("Color")
    @Expose
    private String color;
    @SerializedName("IntentName")
    @Expose
    private String intentName;
    @SerializedName("SubMenuItem")
    @Expose
    private Object subMenuItem;

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Boolean getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(Boolean isVisible) {
        this.isVisible = isVisible;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getIntentName() {
        return intentName;
    }

    public void setIntentName(String intentName) {
        this.intentName = intentName;
    }

    public Object getSubMenuItem() {
        return subMenuItem;
    }

    public void setSubMenuItem(Object subMenuItem) {
        this.subMenuItem = subMenuItem;
    }
}
