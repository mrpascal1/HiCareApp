package com.ab.hicarerun.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.ab.hicarerun.network.models.ReferralModel.ReferralList;
import com.ab.hicarerun.network.models.ReferralModel.ReferralSRData;
import com.ab.hicarerun.network.models.ReferralModel.ReferralService;

/**
 * Created by Arjun Bhatt on 7/9/2020.
 */
public class ReferralServiceViewModel implements Parcelable {
    private int Id;
    private String Name;
    private String DisplayName;

    public ReferralServiceViewModel() {
        this.Id = 0;
        this.Name = "";
        this.DisplayName = "";
    }

    protected ReferralServiceViewModel(Parcel in) {
        Id = in.readInt();
        Name = in.readString();
        DisplayName = in.readString();
    }

    public static final Creator<ReferralServiceViewModel> CREATOR = new Creator<ReferralServiceViewModel>() {
        @Override
        public ReferralServiceViewModel createFromParcel(Parcel in) {
            return new ReferralServiceViewModel(in);
        }

        @Override
        public ReferralServiceViewModel[] newArray(int size) {
            return new ReferralServiceViewModel[size];
        }
    };

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

    public void clone(ReferralService referralList) {
        this.Id = referralList.getId();
        this.DisplayName = referralList.getDisplayName();
        this.Name = referralList.getName();
    }

}
