package com.ab.hicarerun.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.ab.hicarerun.network.models.routinemodel.RoutineOption;
import com.ab.hicarerun.network.models.routinemodel.RoutineQuestion;
import com.ab.hicarerun.network.models.routinemodel.ValueNo;
import com.ab.hicarerun.network.models.routinemodel.ValueYes;

import java.util.List;

/**
 * Created by Arjun Bhatt on 8/13/2020.
 */
public class RoutineViewModel implements Parcelable {
    private String questionTitle;
    private String questionType;
    private List<RoutineOption> valueList;
    private ValueYes valueYes;
    private ValueNo valueNo;
    private String primarySelection;
    private String secondarySelection;

    public RoutineViewModel() {
        this.questionTitle = "NA";
        this.questionType = "NA";
        this.primarySelection = "NA";
        this.secondarySelection = "NA";
    }

    protected RoutineViewModel(Parcel in) {
        questionTitle = in.readString();
        questionType = in.readString();
        valueYes = in.readParcelable(ValueYes.class.getClassLoader());
        valueNo = in.readParcelable(ValueNo.class.getClassLoader());
        primarySelection = in.readString();
        secondarySelection = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(questionTitle);
        dest.writeString(questionType);
        dest.writeString(primarySelection);
        dest.writeString(secondarySelection);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RoutineViewModel> CREATOR = new Creator<RoutineViewModel>() {
        @Override
        public RoutineViewModel createFromParcel(Parcel in) {
            return new RoutineViewModel(in);
        }

        @Override
        public RoutineViewModel[] newArray(int size) {
            return new RoutineViewModel[size];
        }
    };

    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public List<RoutineOption> getValueList() {
        return valueList;
    }

    public void setValueList(List<RoutineOption> valueList) {
        this.valueList = valueList;
    }

    public ValueYes getValueYes() {
        return valueYes;
    }

    public void setValueYes(ValueYes valueYes) {
        this.valueYes = valueYes;
    }

    public ValueNo getValueNo() {
        return valueNo;
    }

    public void setValueNo(ValueNo valueNo) {
        this.valueNo = valueNo;
    }

    public String getPrimarySelection() {
        return primarySelection;
    }

    public void setPrimarySelection(String primarySelection) {
        this.primarySelection = primarySelection;
    }

    public String getSecondarySelection() {
        return secondarySelection;
    }

    public void setSecondarySelection(String secondarySelection) {
        this.secondarySelection = secondarySelection;
    }

    public static Creator<RoutineViewModel> getCREATOR() {
        return CREATOR;
    }

    public void clone(RoutineQuestion data){
        this.questionTitle = data.getQuestionTitle();
        this.questionType = data.getQuestionType();
        this.valueYes = data.getYes();
        this.valueNo = data.getNo();
        this.valueList = data.getValues();
        this.primarySelection = data.getPrimarySelection();
        this.secondarySelection = data.getSecondarySelection();
    }


}
