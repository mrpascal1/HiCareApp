package com.ab.hicarerun.network.models.ConsulationModel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Optionlist {
    @Expose
    @SerializedName("IsSelected")
    private boolean Isselected;
    @Expose
    @SerializedName("OptionValue")
    private String Optionvalue;
    @Expose
    @SerializedName("OptionValueDisplayText")
    private String OptionValueDisplayText;
    @Expose
    @SerializedName("isSelectedAndDisabled")
    private boolean isSelectedAndDisabled;
    @Expose
    @SerializedName("SortValue")
    private int SortValue;

    public boolean getIsselected() {
        return Isselected;
    }

    public void setIsselected(boolean Isselected) {
        this.Isselected = Isselected;
    }

    public String getOptionvalue() {
        return Optionvalue;
    }

    public void setOptionvalue(String Optionvalue) {
        this.Optionvalue = Optionvalue;
    }

    public String getOptionValueDisplayText() {
        return OptionValueDisplayText;
    }

    public void setOptionValueDisplayText(String optionValueDisplayText) {
        OptionValueDisplayText = optionValueDisplayText;
    }

    public boolean isSelectedAndDisabled() {
        return isSelectedAndDisabled;
    }

    public void setSelectedAndDisabled(boolean selectedAndDisabled) {
        isSelectedAndDisabled = selectedAndDisabled;
    }

    public int getSortValue() {
        return SortValue;
    }

    public void setSortValue(int sortValue) {
        SortValue = sortValue;
    }
}
