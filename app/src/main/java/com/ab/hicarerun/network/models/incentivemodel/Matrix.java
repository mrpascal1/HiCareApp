package com.ab.hicarerun.network.models.incentivemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Arjun Bhatt on 10/16/2019.
 */
public class Matrix {
    @SerializedName("Text")
    @Expose
    private String matrix;

    @SerializedName("Value")
    @Expose
    private String incentiveAmount;

    public String getMatrix() {
        return matrix;
    }

    public void setMatrix(String matrix) {
        this.matrix = matrix;
    }

    public String getIncentiveAmount() {
        return incentiveAmount;
    }

    public void setIncentiveAmount(String incentiveAmount) {
        this.incentiveAmount = incentiveAmount;
    }
}
