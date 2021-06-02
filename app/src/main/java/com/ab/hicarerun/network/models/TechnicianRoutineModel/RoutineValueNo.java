package com.ab.hicarerun.network.models.TechnicianRoutineModel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Arjun Bhatt on 8/13/2020.
 */
public class RoutineValueNo implements Parcelable {
    @SerializedName("Type")
    @Expose
    private String type;
    @SerializedName("Values")
    @Expose
    private List<String> value;

    protected RoutineValueNo(Parcel in) {
        type = in.readString();
        value = in.createStringArrayList();
    }

    public static final Creator<RoutineValueNo> CREATOR = new Creator<RoutineValueNo>() {
        @Override
        public RoutineValueNo createFromParcel(Parcel in) {
            return new RoutineValueNo(in);
        }

        @Override
        public RoutineValueNo[] newArray(int size) {
            return new RoutineValueNo[size];
        }
    };

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getValue() {
        return value;
    }

    public void setValue(List<String> value) {
        this.value = value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeStringList(value);
    }
}
