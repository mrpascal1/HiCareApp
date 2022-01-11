package com.ab.hicarerun.network.models.generalmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class IncompleteReason extends RealmObject {

    @SerializedName("ShowSlot")
    @Expose
    private Boolean showSlot;

    @SerializedName("Value")
    @Expose
    private String reason;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Boolean getShowSlot() {
        return showSlot;
    }

    public void setShowSlot(Boolean showSlot) {
        this.showSlot = showSlot;
    }
}
