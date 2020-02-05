package com.ab.hicarerun.network.models.HandShakeModel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class HandShake implements Serializable {
    @SerializedName("Text")
    @Expose
    private String Text;
    @SerializedName("Value")
    @Expose
    private String Value;
    @SerializedName("IsSelected")
    @Expose
    private String IsSelected;
    @SerializedName("UserId")
    @Expose
    private String UserId;
    @SerializedName("CreatedBy")
    @Expose
    private String CreatedBy;
    @SerializedName("PageType")
    @Expose
    private String PageType;

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    public String getIsSelected() {
        return IsSelected;
    }

    public void setIsSelected(String isSelected) {
        IsSelected = isSelected;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    public String getPageType() {
        return PageType;
    }

    public void setPageType(String pageType) {
        PageType = pageType;
    }

}
