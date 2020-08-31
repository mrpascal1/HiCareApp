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


}
