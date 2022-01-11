package com.ab.hicarerun.network.models.karmamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Arjun Bhatt on 10/8/2020.
 */
public class Karma {
    @SerializedName("ResourceId")
    @Expose
    private String resourceId;
    @SerializedName("LifeLineIndex")
    @Expose
    private Integer lifeLineIndex;
    @SerializedName("TotalPointsDeducted")
    @Expose
    private Integer totalPointsDeducted;
    @SerializedName("TotalPoints")
    @Expose
    private Integer totalPoints;
    @SerializedName("TotalPointsPending")
    @Expose
    private Integer totalPointsPending;
    @SerializedName("KarmaDetailList")
    @Expose
    private List<KarmaDetailList> karmaDetailList = null;

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public Integer getLifeLineIndex() {
        return lifeLineIndex;
    }

    public void setLifeLineIndex(Integer lifeLineIndex) {
        this.lifeLineIndex = lifeLineIndex;
    }

    public Integer getTotalPointsDeducted() {
        return totalPointsDeducted;
    }

    public void setTotalPointsDeducted(Integer totalPointsDeducted) {
        this.totalPointsDeducted = totalPointsDeducted;
    }

    public Integer getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(Integer totalPoints) {
        this.totalPoints = totalPoints;
    }

    public Integer getTotalPointsPending() {
        return totalPointsPending;
    }

    public void setTotalPointsPending(Integer totalPointsPending) {
        this.totalPointsPending = totalPointsPending;
    }

    public List<KarmaDetailList> getKarmaDetailList() {
        return karmaDetailList;
    }

    public void setKarmaDetailList(List<KarmaDetailList> karmaDetailList) {
        this.karmaDetailList = karmaDetailList;
    }
}
