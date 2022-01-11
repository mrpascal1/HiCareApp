package com.ab.hicarerun.network.models.technicianroutinemodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Arjun Bhatt on 8/13/2020.
 */
public class RoutineValueYes implements Parcelable {
    @SerializedName("Type")
    @Expose
    private String type;
    @SerializedName("Values")
    @Expose
    private String value;

    protected RoutineValueYes(Parcel in) {
        type = in.readString();
        value = in.readString();
    }

    public static final Creator<RoutineValueYes> CREATOR = new Creator<RoutineValueYes>() {
        @Override
        public RoutineValueYes createFromParcel(Parcel in) {
            return new RoutineValueYes(in);
        }

        @Override
        public RoutineValueYes[] newArray(int size) {
            return new RoutineValueYes[size];
        }
    };

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeString(value);
    }
}
