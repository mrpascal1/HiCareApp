package com.ab.hicarerun.network.models.NewRewardsModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Arjun Bhatt on 7/2/2020.
 */
public class RewardsMissedObject {
    @SerializedName("Id")
    @Expose
    private String id;
    @SerializedName("TotalPointsMissed")
    @Expose
    private Integer totalPointsMissed;
    @SerializedName("RewardDetailSummary")
    @Expose
    private List<MissedRewardDetailSummary> rewardDetailSummary = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getTotalPointsMissed() {
        return totalPointsMissed;
    }

    public void setTotalPointsMissed(Integer totalPointsMissed) {
        this.totalPointsMissed = totalPointsMissed;
    }

    public List<MissedRewardDetailSummary> getRewardDetailSummary() {
        return rewardDetailSummary;
    }

    public void setRewardDetailSummary(List<MissedRewardDetailSummary> rewardDetailSummary) {
        this.rewardDetailSummary = rewardDetailSummary;
    }
}
