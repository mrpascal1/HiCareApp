package com.ab.hicarerun.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.ab.hicarerun.network.models.LeaderBoardModel.RewardLeaders;

/**
 * Created by Arjun Bhatt on 4/9/2020.
 */
public class RewardLeadersViewModel implements Parcelable {
    private String RecourceId;
    private String RecourceName;;
    private String ResourceImage;
    private Integer TotalPoints;
    private Integer MissedPoints;
    private String ServiceCentreName;
    private String BadgeName;
    private Integer Rank;
    private Boolean isSameResource;

    public RewardLeadersViewModel() {
        this.RecourceId = "NA";
        this.RecourceName = "NA";
        this.ResourceImage = "NA";
        this.TotalPoints = 0;
        this.MissedPoints = 0;
        this.ServiceCentreName = "NA";
        this.BadgeName = "NA";
        this.Rank = 0;
        this.isSameResource = false;
    }


    protected RewardLeadersViewModel(Parcel in) {
        RecourceId = in.readString();
        RecourceName = in.readString();
        ResourceImage = in.readString();
        if (in.readByte() == 0) {
            TotalPoints = null;
        } else {
            TotalPoints = in.readInt();
        }
        if (in.readByte() == 0) {
            MissedPoints = null;
        } else {
            MissedPoints = in.readInt();
        }
        ServiceCentreName = in.readString();
        BadgeName = in.readString();
        if (in.readByte() == 0) {
            Rank = null;
        } else {
            Rank = in.readInt();
        }
        byte tmpIsSameResource = in.readByte();
        isSameResource = tmpIsSameResource == 0 ? null : tmpIsSameResource == 1;
    }

    public static final Creator<RewardLeadersViewModel> CREATOR = new Creator<RewardLeadersViewModel>() {
        @Override
        public RewardLeadersViewModel createFromParcel(Parcel in) {
            return new RewardLeadersViewModel(in);
        }

        @Override
        public RewardLeadersViewModel[] newArray(int size) {
            return new RewardLeadersViewModel[size];
        }
    };

    public void clone(RewardLeaders rewards) {
        this.RecourceId = rewards.getResourceId();
        this.RecourceName = rewards.getResourceName();
        this.ResourceImage = rewards.getResourceImage();
        this.ServiceCentreName = rewards.getServiceCenterName();
        this.TotalPoints = rewards.getTotalPoints();
        this.MissedPoints = rewards.getTotalMissedPoints();
        this.BadgeName = rewards.getBadgeName();
        this.Rank = rewards.getRank();
        this.isSameResource = rewards.getSameResource();
    }

    public String getRecourceId() {
        return RecourceId;
    }

    public void setRecourceId(String recourceId) {
        RecourceId = recourceId;
    }

    public String getRecourceName() {
        return RecourceName;
    }

    public void setRecourceName(String recourceName) {
        RecourceName = recourceName;
    }

    public String getResourceImage() {
        return ResourceImage;
    }

    public void setResourceImage(String resourceImage) {
        ResourceImage = resourceImage;
    }

    public Integer getTotalPoints() {
        return TotalPoints;
    }

    public void setTotalPoints(Integer totalPoints) {
        TotalPoints = totalPoints;
    }

    public String getServiceCentreName() {
        return ServiceCentreName;
    }

    public void setServiceCentreName(String serviceCentreName) {
        ServiceCentreName = serviceCentreName;
    }

    public String getBadgeName() {
        return BadgeName;
    }

    public void setBadgeName(String badgeName) {
        BadgeName = badgeName;
    }

    public Integer getRank() {
        return Rank;
    }

    public void setRank(Integer rank) {
        Rank = rank;
    }

    public Boolean getSameResource() {
        return isSameResource;
    }

    public void setSameResource(Boolean sameResource) {
        isSameResource = sameResource;
    }

    public Integer getMissedPoints() {
        return MissedPoints;
    }

    public void setMissedPoints(Integer missedPoints) {
        MissedPoints = missedPoints;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(RecourceId);
        dest.writeString(RecourceName);
        dest.writeString(ResourceImage);
        if (TotalPoints == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(TotalPoints);
        }
        if (MissedPoints == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(MissedPoints);
        }
        dest.writeString(ServiceCentreName);
        dest.writeString(BadgeName);
        if (Rank == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(Rank);
        }
        dest.writeByte((byte) (isSameResource == null ? 0 : isSameResource ? 1 : 2));
    }
}
