package com.ab.hicarerun.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.ab.hicarerun.network.models.IncentiveModel.IncentiveCriteriaList;
import com.ab.hicarerun.network.models.KarmaModel.KarmaHistoryData;

/**
 * Created by Arjun Bhatt on 10/13/2020.
 */
public class KarmaProgressViewModel implements Parcelable {
    private String resourceId;
    private int LifeLineIndex;
    private boolean IsActiveLifeLine;
    private int TotalPointsDeducted;
    private int TotalPoints;
    private int TotalPointsPending;

    public KarmaProgressViewModel() {
        this.resourceId = "NA";
        this.LifeLineIndex = 0;
        this.IsActiveLifeLine = false;
        this.TotalPointsDeducted = 0;
        this.TotalPoints = 0;
        this.TotalPointsPending = 0;
    }


    protected KarmaProgressViewModel(Parcel in) {
        resourceId = in.readString();
        LifeLineIndex = in.readInt();
        IsActiveLifeLine = in.readByte() != 0;
        TotalPointsDeducted = in.readInt();
        TotalPoints = in.readInt();
        TotalPointsPending = in.readInt();
    }

    public static final Creator<KarmaProgressViewModel> CREATOR = new Creator<KarmaProgressViewModel>() {
        @Override
        public KarmaProgressViewModel createFromParcel(Parcel in) {
            return new KarmaProgressViewModel(in);
        }

        @Override
        public KarmaProgressViewModel[] newArray(int size) {
            return new KarmaProgressViewModel[size];
        }
    };

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public Integer getLifeLineIndex() {
        return LifeLineIndex;
    }

    public void setLifeLineIndex(Integer lifeLineIndex) {
        LifeLineIndex = lifeLineIndex;
    }

    public Boolean getIsActiveLifeLine() {
        return IsActiveLifeLine;
    }

    public void setIsActiveLifeLine(Boolean isActiveLifeLine) {
        IsActiveLifeLine = isActiveLifeLine;
    }

    public int getTotalPointsDeducted() {
        return TotalPointsDeducted;
    }

    public void setTotalPointsDeducted(int totalPointsDeducted) {
        TotalPointsDeducted = totalPointsDeducted;
    }

    public Integer getTotalPoints() {
        return TotalPoints;
    }

    public void setTotalPoints(Integer totalPoints) {
        TotalPoints = totalPoints;
    }

    public Integer getTotalPointsPending() {
        return TotalPointsPending;
    }

    public void setTotalPointsPending(Integer totalPointsPending) {
        TotalPointsPending = totalPointsPending;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(resourceId);
        dest.writeInt(LifeLineIndex);
        dest.writeByte((byte) (IsActiveLifeLine ? 1 : 0));
        dest.writeInt(TotalPointsDeducted);
        dest.writeInt(TotalPoints);
        dest.writeInt(TotalPointsPending);
    }


    public void clone(KarmaHistoryData data) {
        this.resourceId = data.getResourceId();
        this.LifeLineIndex = data.getLifeLineIndex();
        this.IsActiveLifeLine = data.getIsActiveLifeLine();
        this.TotalPointsDeducted = data.getTotalPointsDeducted();
        this.TotalPoints = data.getTotalPoints();
        this.TotalPointsPending = data.getTotalPointsPending();
    }
}
