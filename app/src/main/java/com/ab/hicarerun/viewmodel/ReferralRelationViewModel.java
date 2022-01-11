package com.ab.hicarerun.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.ab.hicarerun.network.models.referralmodel.ReferralRelation;

/**
 * Created by Arjun Bhatt on 7/9/2020.
 */
public class ReferralRelationViewModel implements Parcelable {
    private int Id;
    private String Name;
    private String DisplayName;

    public ReferralRelationViewModel() {
        this.Id = 0;
        this.Name = "";
        this.DisplayName = "";
    }

    protected ReferralRelationViewModel(Parcel in) {
        Id = in.readInt();
        Name = in.readString();
        DisplayName = in.readString();
    }

    public static final Creator<ReferralRelationViewModel> CREATOR = new Creator<ReferralRelationViewModel>() {
        @Override
        public ReferralRelationViewModel createFromParcel(Parcel in) {
            return new ReferralRelationViewModel(in);
        }

        @Override
        public ReferralRelationViewModel[] newArray(int size) {
            return new ReferralRelationViewModel[size];
        }
    };

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDisplayName() {
        return DisplayName;
    }

    public void setDisplayName(String displayName) {
        DisplayName = displayName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Id);
        dest.writeString(Name);
        dest.writeString(DisplayName);
    }

    public void clone(ReferralRelation referralList) {
        this.Id = referralList.getId();
        this.DisplayName = referralList.getDisplayName();
        this.Name = referralList.getName();
    }
}


