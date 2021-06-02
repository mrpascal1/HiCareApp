package com.ab.hicarerun.network.models.OffersModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Arjun Bhatt on 3/26/2020.
 */
public class RewardList {
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
    @SerializedName("RewardDetailSummary")
    @Expose
    private List<RewardDetailSummary> rewardDetailSummary = null;

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

    public List<RewardDetailSummary> getRewardDetailSummary() {
        return rewardDetailSummary;
    }

    public void setRewardDetailSummary(List<RewardDetailSummary> rewardDetailSummary) {
        this.rewardDetailSummary = rewardDetailSummary;
    }

    public Integer getTotalPointsMissed() {
        return TotalPointsMissed;
    }

    public void setTotalPointsMissed(Integer totalPointsMissed) {
        TotalPointsMissed = totalPointsMissed;
    }
}
