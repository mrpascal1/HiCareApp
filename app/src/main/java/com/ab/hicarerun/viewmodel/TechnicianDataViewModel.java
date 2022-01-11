package com.ab.hicarerun.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.ab.hicarerun.network.models.technicianroutinemodel.TechnicianData;

/**
 * Created by Arjun Bhatt on 8/13/2020.
 */
public class TechnicianDataViewModel implements Parcelable {
    private String technicianId;
    private String technicianName;
    private boolean isRoutineCheckListSubmited;


    public TechnicianDataViewModel() {
        this.technicianId = "NA";
        this.technicianName = "NA";
        this.isRoutineCheckListSubmited = false;
    }

    protected TechnicianDataViewModel(Parcel in) {
        technicianId = in.readString();
        technicianName = in.readString();
        isRoutineCheckListSubmited = in.readByte() != 0;
    }

    public static final Creator<TechnicianDataViewModel> CREATOR = new Creator<TechnicianDataViewModel>() {
        @Override
        public TechnicianDataViewModel createFromParcel(Parcel in) {
            return new TechnicianDataViewModel(in);
        }

        @Override
        public TechnicianDataViewModel[] newArray(int size) {
            return new TechnicianDataViewModel[size];
        }
    };

    public String getTechnicianId() {
        return technicianId;
    }

    public void setTechnicianId(String technicianId) {
        this.technicianId = technicianId;
    }

    public String getTechnicianName() {
        return technicianName;
    }

    public void setTechnicianName(String technicianName) {
        this.technicianName = technicianName;
    }

    public boolean isRoutineCheckListSubmited() {
        return isRoutineCheckListSubmited;
    }

    public void setRoutineCheckListSubmited(boolean routineCheckListSubmited) {
        isRoutineCheckListSubmited = routineCheckListSubmited;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(technicianId);
        dest.writeString(technicianName);
        dest.writeByte((byte) (isRoutineCheckListSubmited ? 1 : 0));
    }
    public void clone(TechnicianData data){
        this.technicianId = data.getTechnicianId();
        this.technicianName = data.getTechnicianName();
        this.isRoutineCheckListSubmited = data.getRoutineChecklistSubmitted();
    }
}
