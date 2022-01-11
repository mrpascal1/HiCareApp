package com.ab.hicarerun.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.ab.hicarerun.network.models.newrewardsmodel.RewardListData;
import com.ab.hicarerun.network.models.newrewardsmodel.RewardListObject;

/**
 * Created by Arjun Bhatt on 7/2/2020.
 */
public class NewRewardsViewModel implements Parcelable {
    private String RewardDate;
    private String RewardDateFormat;
    private String Id;
    private Integer TotalPointsEarned;
    private Boolean isScratchDone;
    private RewardListObject rewardListObject;
//    private RewardsMissedObject rewardsMissedObject;

    public NewRewardsViewModel() {
        this.RewardDate = "NA";
        this.RewardDateFormat = "NA";
        this.Id = "NA";
        this.TotalPointsEarned = 0;
        this.isScratchDone = false;
//        this.rewardsMissedObject = null;
        this.rewardListObject = null;
    }

    public String getRewardDate() {
        return RewardDate;
    }

    public void setRewardDate(String rewardDate) {
        RewardDate = rewardDate;
    }

    public String getRewardDateFormat() {
        return RewardDateFormat;
    }

    public void setRewardDateFormat(String rewardDateFormat) {
        RewardDateFormat = rewardDateFormat;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public Integer getTotalPointsEarned() {
        return TotalPointsEarned;
    }

    public void setTotalPointsEarned(Integer totalPointsEarned) {
        TotalPointsEarned = totalPointsEarned;
    }

    public Boolean getScratchDone() {
        return isScratchDone;
    }

    public void setScratchDone(Boolean scratchDone) {
        isScratchDone = scratchDone;
    }

    public RewardListObject getRewardListObject() {
        return rewardListObject;
    }

    public void setRewardListObject(RewardListObject rewardListObject) {
        this.rewardListObject = rewardListObject;
    }

//    public RewardsMissedObject getRewardsMissedObject() {
//        return rewardsMissedObject;
//    }
//
//    public void setRewardsMissedObject(RewardsMissedObject rewardsMissedObject) {
//        this.rewardsMissedObject = rewardsMissedObject;
//    }

    protected NewRewardsViewModel(Parcel in) {
        RewardDate = in.readString();
        RewardDateFormat = in.readString();
        Id = in.readString();
        if (in.readByte() == 0) {
            TotalPointsEarned = null;
        } else {
            TotalPointsEarned = in.readInt();
        }
        byte tmpIsScratchDone = in.readByte();
        isScratchDone = tmpIsScratchDone == 0 ? null : tmpIsScratchDone == 1;
    }

    public static final Creator<NewRewardsViewModel> CREATOR = new Creator<NewRewardsViewModel>() {
        @Override
        public NewRewardsViewModel createFromParcel(Parcel in) {
            return new NewRewardsViewModel(in);
        }

        @Override
        public NewRewardsViewModel[] newArray(int size) {
            return new NewRewardsViewModel[size];
        }
    };

    public void clone(RewardListData rewards) {
        this.RewardDate = rewards.getRewardDate();
        this.RewardDateFormat = rewards.getRewardDateFormat();
        this.Id = rewards.getRewardsObject().getId();
        this.isScratchDone = rewards.getRewardsObject().getIsRewardScratchDone();
        this.TotalPointsEarned = rewards.getRewardsObject().getTotalPointsEarned();
        this.rewardListObject = rewards.getRewardsObject();
//        this.rewardsMissedObject = rewards.getRewardMissedObject();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(RewardDate);
        dest.writeString(RewardDateFormat);
        dest.writeString(Id);
        if (TotalPointsEarned == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(TotalPointsEarned);
        }
        dest.writeByte((byte) (isScratchDone == null ? 0 : isScratchDone ? 1 : 2));
    }
}
