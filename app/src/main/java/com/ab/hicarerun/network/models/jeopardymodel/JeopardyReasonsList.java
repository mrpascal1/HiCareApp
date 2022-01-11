package com.ab.hicarerun.network.models.jeopardymodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JeopardyReasonsList {
    @SerializedName("Name")
    @Expose
    private String ResonName;
    @SerializedName("DisplayName")
    @Expose
    private String DisplayName;

    public String getResonName() {
        return ResonName;
    }

    public void setResonName(String resonName) {
        ResonName = resonName;
    }

    public String getDisplayName() {
        return DisplayName;
    }

    public void setDisplayName(String displayName) {
        DisplayName = displayName;
    }
}
