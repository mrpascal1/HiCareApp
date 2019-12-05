package com.ab.hicarerun.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.ab.hicarerun.network.models.IncentiveModel.Matrix;

/**
 * Created by Arjun Bhatt on 10/16/2019.
 */
public class IncentiveMatrixViewModel implements Parcelable {
    private String matrix;
    private String incentive;

    public IncentiveMatrixViewModel() {
        matrix = "NA";
        incentive = "NA";
    }

    protected IncentiveMatrixViewModel(Parcel in) {
        matrix = in.readString();
        incentive = in.readString();
    }

    public static final Creator<IncentiveMatrixViewModel> CREATOR = new Creator<IncentiveMatrixViewModel>() {
        @Override
        public IncentiveMatrixViewModel createFromParcel(Parcel in) {
            return new IncentiveMatrixViewModel(in);
        }

        @Override
        public IncentiveMatrixViewModel[] newArray(int size) {
            return new IncentiveMatrixViewModel[size];
        }
    };

    public String getMatrix() {
        return matrix;
    }

    public void setMatrix(String matrix) {
        this.matrix = matrix;
    }

    public String getIncentive() {
        return incentive;
    }

    public void setIncentive(String incentive) {
        this.incentive = incentive;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(matrix);
        parcel.writeString(incentive);
    }

    public void clone(Matrix matrix) {
        this.matrix = matrix.getMatrix();
        this.incentive = matrix.getIncentiveAmount();
    }
}
