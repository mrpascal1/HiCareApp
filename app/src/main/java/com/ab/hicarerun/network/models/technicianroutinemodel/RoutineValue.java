package com.ab.hicarerun.network.models.technicianroutinemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Arjun Bhatt on 8/20/2020.
 */
public class RoutineValue {
    @SerializedName("Key")
    @Expose
    private String Key;
    @SerializedName("Value")
    @Expose
    private String value;
    @SerializedName("IsSelected")
    @Expose
    private boolean isSelected;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        this.isSelected = selected;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }
}
