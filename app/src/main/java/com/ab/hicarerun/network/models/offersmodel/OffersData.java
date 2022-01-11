package com.ab.hicarerun.network.models.offersmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Arjun Bhatt on 3/26/2020.
 */
public class OffersData {

    @SerializedName("ResourceId")
    @Expose
    private String resourceId;
    @SerializedName("TotalPointsEarned")
    @Expose
    private Integer totalPointsEarned;
    @SerializedName("TotalPointsMissed")
    @Expose
    private Integer TotalPointsMissed;
    @SerializedName("RewardList")
    @Expose
    private List<RewardList> rewardList = null;

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public Integer getTotalPointsEarned() {
        return totalPointsEarned;
    }

    public void setTotalPointsEarned(Integer totalPointsEarned) {
        this.totalPointsEarned = totalPointsEarned;
    }

    public List<RewardList> getRewardList() {
        return rewardList;
    }

    public void setRewardList(List<RewardList> rewardList) {
        this.rewardList = rewardList;
    }

    public Integer getTotalPointsMissed() {
        return TotalPointsMissed;
    }

    public void setTotalPointsMissed(Integer totalPointsMissed) {
        TotalPointsMissed = totalPointsMissed;
    }
}
