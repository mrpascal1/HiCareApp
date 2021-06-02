package com.ab.hicarerun.network.models.NewRewardsModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Arjun Bhatt on 7/2/2020.
 */
public class RewardListObject {
    @SerializedName("Id")
    @Expose
    private String id;
    @SerializedName("TotalPointsEarned")
    @Expose
    private Integer totalPointsEarned;
    @SerializedName("IsRewardScratchDone")
    @Expose
    private Boolean isRewardScratchDone;
    @SerializedName("RewardDetailSummary")
    @Expose
    private List<NewRewardDetailSummary> rewardDetailSummary;

    public RewardListObject() {
        this.id = "NA";
        this.totalPointsEarned = 0;
        this.isRewardScratchDone = false;
        this.rewardDetailSummary = null;
    }

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

    public List<NewRewardDetailSummary> getRewardDetailSummary() {
        return rewardDetailSummary;
    }

    public void setRewardDetailSummary(List<NewRewardDetailSummary> rewardDetailSummary) {
        this.rewardDetailSummary = rewardDetailSummary;
    }

}
