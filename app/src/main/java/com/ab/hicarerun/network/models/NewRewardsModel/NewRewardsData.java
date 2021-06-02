package com.ab.hicarerun.network.models.NewRewardsModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Arjun Bhatt on 7/2/2020.
 */
public class NewRewardsData {
    @SerializedName("ResourceId")
    @Expose
    private String resourceId;
    @SerializedName("TotalPointsEarned")
    @Expose
    private Integer totalPointsEarned;
    @SerializedName("TotalPointsMissed")
    @Expose
    private Integer totalPointsMissed;
    @SerializedName("RewardList")
    @Expose
    private List<RewardListData> rewardList = null;

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

    public Integer getTotalPointsMissed() {
        return totalPointsMissed;
    }

    public void setTotalPointsMissed(Integer totalPointsMissed) {
        this.totalPointsMissed = totalPointsMissed;
    }

    public List<RewardListData> getRewardList() {
        return rewardList;
    }

    public void setRewardList(List<RewardListData> rewardList) {
        this.rewardList = rewardList;
    }


}
