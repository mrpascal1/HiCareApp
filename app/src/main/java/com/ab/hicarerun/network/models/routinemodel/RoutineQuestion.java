package com.ab.hicarerun.network.models.routinemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Arjun Bhatt on 8/21/2020.
 */
public class RoutineQuestion {

    @SerializedName("QuestionTitle")
    @Expose
    private String questionTitle;
    @SerializedName("Question_Display_Title")
    @Expose
    private String Question_Display_Title;
    @SerializedName("QuestionType")
    @Expose
    private String questionType;
    @SerializedName("Values")
    @Expose
    private List<RoutineOption> values = null;
    @SerializedName("Yes")
    @Expose
    private ValueYes yes;
    @SerializedName("No")
    @Expose
    private ValueNo no;
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

    public List<RoutineOption> getValues() {
        return values;
    }

    public void setValues(List<RoutineOption> values) {
        this.values = values;
    }

    public ValueYes getYes() {
        return yes;
    }

    public void setYes(ValueYes yes) {
        this.yes = yes;
    }

    public ValueNo getNo() {
        return no;
    }

    public void setNo(ValueNo no) {
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

    public String getQuestion_Display_Title() {
        return Question_Display_Title;
    }

    public void setQuestion_Display_Title(String question_Display_Title) {
        Question_Display_Title = question_Display_Title;
    }
}
