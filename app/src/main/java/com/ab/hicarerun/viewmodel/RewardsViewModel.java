package com.ab.hicarerun.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.ab.hicarerun.network.models.NewRewardsModel.RewardListData;
import com.ab.hicarerun.network.models.OffersModel.RewardList;

/**
 * Created by Arjun Bhatt on 3/26/2020.
 */
public class RewardsViewModel implements Parcelable {
    private String id;
    private Integer totalPointsEarned;
    private Boolean IsRewardScratchDone;


    public RewardsViewModel() {
        this.id = "NA";
        this.totalPointsEarned = 0;
        this.IsRewardScratchDone = false;
    }


    private RewardsViewModel(Parcel in) {
        id = in.readString();
        if (in.readByte() == 0) {
            totalPointsEarned = null;
        } else {
            totalPointsEarned = in.readInt();
        }
        byte tmpIsRewardScratchDone = in.readByte();
        IsRewardScratchDone = tmpIsRewardScratchDone == 0 ? null : tmpIsRewardScratchDone == 1;
    }

    public static final Creator<RewardsViewModel> CREATOR = new Creator<RewardsViewModel>() {
        @Override
        public RewardsViewModel createFromParcel(Parcel in) {
            return new RewardsViewModel(in);
        }

        @Override
        public RewardsViewModel[] newArray(int size) {
            return new RewardsViewModel[size];
        }
    };

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

    public Boolean getRewardScratchDone() {
        return IsRewardScratchDone;
    }

    public void setRewardScratchDone(Boolean rewardScratchDone) {
        IsRewardScratchDone = rewardScratchDone;
    }



    public void clone(RewardList rewards) {
        this.id = rewards.getId();
        this.totalPointsEarned = rewards.getTotalPointsEarned();
        this.IsRewardScratchDone = rewards.getIsRewardScratchDone();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        if (totalPointsEarned == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(totalPointsEarned);
        }
        parcel.writeByte((byte) (IsRewardScratchDone == null ? 0 : IsRewardScratchDone ? 1 : 2));
    }
}
