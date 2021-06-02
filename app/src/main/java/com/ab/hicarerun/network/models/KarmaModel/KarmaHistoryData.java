package com.ab.hicarerun.network.models.KarmaModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Arjun Bhatt on 10/13/2020.
 */
public class KarmaHistoryData {
    @SerializedName("ResourceId")
    @Expose
    private String resourceId;
    @SerializedName("LifeLineIndex")
    @Expose
    private Integer lifeLineIndex;
    @SerializedName("IsActiveLifeLine")
    @Expose
    private Boolean isActiveLifeLine;
    @SerializedName("LivesLeftDisplay")
    @Expose
    private String LivesLeftDisplay;
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
    private List<KarmaHistoryDetails> karmaDetailList;

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

    public Boolean getIsActiveLifeLine() {
        return isActiveLifeLine;
    }

    public void setIsActiveLifeLine(Boolean isActiveLifeLine) {
        this.isActiveLifeLine = isActiveLifeLine;
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

    public List<KarmaHistoryDetails> getKarmaDetailList() {
        return karmaDetailList;
    }

    public void setKarmaDetailList(List<KarmaHistoryDetails> karmaDetailList) {
        this.karmaDetailList = karmaDetailList;
    }

    public String getLivesLeftDisplay() {
        return LivesLeftDisplay;
    }

    public void setLivesLeftDisplay(String livesLeftDisplay) {
        LivesLeftDisplay = livesLeftDisplay;
    }
}
