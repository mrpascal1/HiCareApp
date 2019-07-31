package com.ab.hicarerun.network.models.JeopardyModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JeopardyReasonsList {
    @SerializedName("Name")
    @Expose
    private String ResonName;

    public String getResonName() {
        return ResonName;
    }

    public void setResonName(String resonName) {
        ResonName = resonName;
    }
}
