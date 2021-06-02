package com.ab.hicarerun.network.models.TechnicianRoutineModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmModel;
import io.realm.RealmObject;

/**
 * Created by Arjun Bhatt on 8/19/2020.
 */
public class ValueData extends RealmObject {
    @SerializedName("OptionValue")
    @Expose
    private String value;
    @SerializedName("OptionValueDisplayText")
    @Expose
    private String OptionValueDisplayText;
    @SerializedName("IsSelected")
    @Expose
    private  boolean isSelected=false;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public String getOptionValueDisplayText() {
        return OptionValueDisplayText;
    }

    public void setOptionValueDisplayText(String optionValueDisplayText) {
        OptionValueDisplayText = optionValueDisplayText;
    }
}
