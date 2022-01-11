package com.ab.hicarerun.network.models.incentivemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Arjun Bhatt on 7/10/2020.
 */
public class IncentiiveDetailList {
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("Points")
    @Expose
    private Integer points;
    @SerializedName("Amount")
    @Expose
    private Integer amount;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

}
