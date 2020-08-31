package com.ab.hicarerun.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.ab.hicarerun.network.models.IncentiveModel.IncentiveCriteriaList;
import com.ab.hicarerun.network.models.IncentiveModel.Matrix;

/**
 * Created by Arjun Bhatt on 10/16/2019.
 */
public class IncentiveMatrixViewModel implements Parcelable {
    private String Criteria;
    private String Amount;

    public IncentiveMatrixViewModel() {
        Criteria = "NA";
        Amount = "NA";
    }

    protected IncentiveMatrixViewModel(Parcel in) {
        Criteria = in.readString();
        Amount = in.readString();
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
        return Criteria;
    }

    public void setMatrix(String Criteria) {
        this.Criteria = Criteria;
    }

    public String getIncentive() {
        return Amount;
    }

    public void setIncentive(String Amount) {
        this.Amount = Amount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(Criteria);
        parcel.writeString(Amount);
    }

    public void clone(IncentiveCriteriaList Criteria) {
        this.Criteria = Criteria.getCriteria();
        this.Amount = Criteria.getAmount();
    }
}
