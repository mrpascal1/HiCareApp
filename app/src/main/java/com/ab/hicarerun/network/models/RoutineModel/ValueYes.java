package com.ab.hicarerun.network.models.RoutineModel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Arjun Bhatt on 8/21/2020.
 */
public class ValueYes {
    @SerializedName("Type")
    @Expose
    private String type;

    @SerializedName("IsNumber")
    @Expose
    private Boolean IsNumber;

    @SerializedName("Values")
    @Expose
    private List<RoutineNoValue> value;
    private String str;
    private int spnPosition;
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<RoutineNoValue> getValue() {
        return value;
    }

    public void setValue(List<RoutineNoValue> value) {
        this.value = value;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public int getSpnPosition() {
        return spnPosition;
    }

    public void setSpnPosition(int spnPosition) {
        this.spnPosition = spnPosition;
    }

    public Boolean getNumber() {
        return IsNumber;
    }

    public void setNumber(Boolean number) {
        IsNumber = number;
    }
}
