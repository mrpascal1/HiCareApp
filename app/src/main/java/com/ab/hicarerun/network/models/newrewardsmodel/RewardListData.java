package com.ab.hicarerun.network.models.newrewardsmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Arjun Bhatt on 7/2/2020.
 */
public class RewardListData {
    @SerializedName("RewardDate")
    @Expose
    private String rewardDate;
    @SerializedName("RewardDateFormat")
    @Expose
    private String rewardDateFormat;
    @SerializedName("RewardEarnedList")
    @Expose
    private RewardListObject rewardsObject;
//    @SerializedName("RewardMissedList")
//    @Expose
//    private RewardsMissedObject rewardMissedObject;

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

    public RewardListObject getRewardsObject() {
        return rewardsObject;
    }

    public void setRewardsObject(RewardListObject rewardsObject) {
        this.rewardsObject = rewardsObject;
    }

//    public RewardsMissedObject getRewardMissedObject() {
//        return rewardMissedObject;
//    }
//
//    public void setRewardMissedObject(RewardsMissedObject rewardMissedObject) {
//        this.rewardMissedObject = rewardMissedObject;
//    }
}
