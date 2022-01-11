package com.ab.hicarerun.network.models.offersmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Arjun Bhatt on 3/26/2020.
 */
public class RewardHistoryList {
    @SerializedName("Id")
    @Expose
    private String id;
    @SerializedName("TotalPointsEarned")
    @Expose
    private Integer totalPointsEarned;
    @SerializedName("TotalPointsMissed")
    @Expose
    private Integer TotalPointsMissed;
    @SerializedName("IsRewardScratchDone")
    @Expose
    private Boolean isRewardScratchDone;
    @SerializedName("RewardDate")
    @Expose
    private String rewardDate;
    @SerializedName("RewardDateFormat")
    @Expose
    private String rewardDateFormat;
    @SerializedName("RewardDetailSummary")
    @Expose
    private List<RewardDetailHistorySummary> rewardDetailSummary = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getTotalPointsEarned() {
        return totalPointsEarned;
    }

    public void setTotalPointsEarned(Integer totalPointsEarned) {
        this.totalPointsEarned = totalPointsEarned;
    }

    public Boolean getIsRewardScratchDone() {
        return isRewardScratchDone;
    }

    public void setIsRewardScratchDone(Boolean isRewardScratchDone) {
        this.isRewardScratchDone = isRewardScratchDone;
    }

    public String getRewardDate() {
        return rewardDate;
    }

    public void setRewardDate(String rewardDate) {
        this.rewardDate = rewardDate;
    }

    public String getRewardDateFormat() {
        return rewardDateFormat;
    }

    public void setRewardDateFormat(String rewardDateFormat) {
        this.rewardDateFormat = rewardDateFormat;
    }

    public List<RewardDetailHistorySummary> getRewardDetailSummary() {
        return rewardDetailSummary;
    }

    public void setRewardDetailSummary(List<RewardDetailHistorySummary> rewardDetailSummary) {
        this.rewardDetailSummary = rewardDetailSummary;
    }
}
