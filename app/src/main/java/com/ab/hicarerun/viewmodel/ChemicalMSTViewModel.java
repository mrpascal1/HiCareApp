package com.ab.hicarerun.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.ab.hicarerun.network.models.chemicalmodel.Chemicals;
import com.ab.hicarerun.network.models.chemicalmodel.MSTChemicals;

import java.util.List;

/**
 * Created by Arjun Bhatt on 1/7/2020.
 */
public class ChemicalMSTViewModel implements Parcelable {
    private String taskNo;
    private String taskType;
    private List<Chemicals> chemicalsList = null;

    public ChemicalMSTViewModel() {
        this.taskNo = "NA";
        this.taskNo = "NA";
    }


    protected ChemicalMSTViewModel(Parcel in) {
        taskNo = in.readString();
        taskType = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(taskNo);
        dest.writeString(taskType);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ChemicalMSTViewModel> CREATOR = new Creator<ChemicalMSTViewModel>() {
        @Override
        public ChemicalMSTViewModel createFromParcel(Parcel in) {
            return new ChemicalMSTViewModel(in);
        }

        @Override
        public ChemicalMSTViewModel[] newArray(int size) {
            return new ChemicalMSTViewModel[size];
        }
    };

    public String getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public List<Chemicals> getChemicalsList() {
        return chemicalsList;
    }

    public void setChemicalsList(List<Chemicals> chemicalsList) {
        this.chemicalsList = chemicalsList;
    }

    public static Creator<ChemicalMSTViewModel> getCREATOR() {
        return CREATOR;
    }



    public void clone(MSTChemicals chemicals) {
        this.taskNo = chemicals.getTaskNo();
        this.taskType = chemicals.getTaskType();
        this.chemicalsList = chemicals.getData();
    }
}
