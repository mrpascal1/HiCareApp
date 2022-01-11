package com.ab.hicarerun.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.ab.hicarerun.network.models.incentivemodel.IncentiiveDetailList;

/**
 * Created by Arjun Bhatt on 7/10/2020.
 */
public class IncentiveDetailViewModel implements Parcelable {
    private String Description;
    private int Points;
    private int Amount;

    public IncentiveDetailViewModel() {
        this.Description = "NA";
        this.Points = 0;
        this.Amount = 0;
    }

    protected IncentiveDetailViewModel(Parcel in) {
        Description = in.readString();
        Points = in.readInt();
        Amount = in.readInt();
    }

    public static final Creator<IncentiveDetailViewModel> CREATOR = new Creator<IncentiveDetailViewModel>() {
        @Override
        public IncentiveDetailViewModel createFromParcel(Parcel in) {
            return new IncentiveDetailViewModel(in);
        }

        @Override
        public IncentiveDetailViewModel[] newArray(int size) {
            return new IncentiveDetailViewModel[size];
        }
    };

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getPoints() {
        return Points;
    }

    public void setPoints(int points) {
        Points = points;
    }

    public int getAmount() {
        return Amount;
    }

    public void setAmount(int amount) {
        Amount = amount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Description);
        dest.writeInt(Points);
        dest.writeInt(Amount);
    }

    public void clone(IncentiiveDetailList detailList) {
        this.Description = detailList.getDescription();
        this.Amount = detailList.getAmount();
        this.Points = detailList.getPoints();
    }
}
