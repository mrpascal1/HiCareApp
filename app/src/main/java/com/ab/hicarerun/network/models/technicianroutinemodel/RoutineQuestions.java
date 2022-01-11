package com.ab.hicarerun.network.models.technicianroutinemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Arjun Bhatt on 8/13/2020.
 */
public class RoutineQuestions {
    @SerializedName("QuestionTitle")
    @Expose
    private String questionTitle;
    @SerializedName("QuestionType")
    @Expose
    private String questionType;
    @SerializedName("Values")
    @Expose
    private List<RoutineValue> RoutineValue = null;
    @SerializedName("Yes")
    @Expose
    private RoutineValueYes yes;
    @SerializedName("No")
    @Expose
    private RoutineValueNo no;
    @SerializedName("PrimarySelection")
    @Expose
    private String primarySelection;
    @SerializedName("SecondarySelection")
    @Expose
    private String secondarySelection;

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

    public List<RoutineValue> getRoutineValue() {
        return RoutineValue;
    }

    public void setRoutineValue(List<RoutineValue> routineValue) {
        RoutineValue = routineValue;
    }

    public RoutineValueYes getYes() {
        return yes;
    }

    public void setYes(RoutineValueYes yes) {
        this.yes = yes;
    }

    public RoutineValueNo getNo() {
        return no;
    }

    public void setNo(RoutineValueNo no) {
        this.no = no;
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

}
