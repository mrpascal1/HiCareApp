package com.ab.hicarerun.network.models.ConsulationModel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data  {
    @Expose
    @SerializedName("OptionList")
    private List<Optionlist> Optionlist;
    @Expose
    @SerializedName("TaskId")
    private String Taskid;
    @Expose
    @SerializedName("OrderId")
    private String Orderid;
    @Expose
    @SerializedName("AccountId")
    private String Accountid;
    @Expose
    @SerializedName("SRId")
    private String Srid;
    @Expose
    @SerializedName("AnswerText")
    private String Answertext;
    @Expose
    @SerializedName("Options")
    private String Options;
    @Expose
    @SerializedName("IsAnswerSelected")
    private boolean IsAnswerSelected;
    @Expose
    @SerializedName("ShowQuestion")
    private boolean ShowQuestion;
    @Expose
    @SerializedName("IsPictureRequired")
    private boolean Ispicturerequired;
    private boolean IsNoSelected;
    @Expose
    @SerializedName("PictureUrl")
    private String PictureUrl;
    @Expose
    @SerializedName("QuestionCategory")
    private String Questioncategory;
    @Expose
    @SerializedName("QuestionType")
    private String Questiontype;
    @Expose
    @SerializedName("QuestionTitle")
    private String Questiontitle;

    @Expose
    @SerializedName("QuestionTitleDisplayText")
    private String QuestionTitleDisplayText;

    @Expose
    @SerializedName("Name")
    private String Name;
    @Expose
    @SerializedName("Id")
    private String Id;
    private boolean isChecked;



    public List<Optionlist> getOptionlist() {
        return Optionlist;
    }

    public void setOptionlist(List<Optionlist> Optionlist) {
        this.Optionlist = Optionlist;
    }

    public String getTaskid() {
        return Taskid;
    }

    public void setTaskid(String Taskid) {
        this.Taskid = Taskid;
    }

    public String getOrderid() {
        return Orderid;
    }

    public void setOrderid(String Orderid) {
        this.Orderid = Orderid;
    }

    public String getAccountid() {
        return Accountid;
    }

    public void setAccountid(String Accountid) {
        this.Accountid = Accountid;
    }

    public String getSrid() {
        return Srid;
    }

    public void setSrid(String Srid) {
        this.Srid = Srid;
    }

    public String getAnswertext() {
        return Answertext;
    }

    public void setAnswertext(String Answertext) {
        this.Answertext = Answertext;
    }

    public String getOptions() {
        return Options;
    }

    public void setOptions(String Options) {
        this.Options = Options;
    }

    public boolean getIspicturerequired() {
        return Ispicturerequired;
    }

    public void setIspicturerequired(boolean Ispicturerequired) {
        this.Ispicturerequired = Ispicturerequired;
    }

    public String getQuestioncategory() {
        return Questioncategory;
    }

    public void setQuestioncategory(String Questioncategory) {
        this.Questioncategory = Questioncategory;
    }

    public String getQuestiontype() {
        return Questiontype;
    }

    public void setQuestiontype(String Questiontype) {
        this.Questiontype = Questiontype;
    }

    public String getQuestiontitle() {
        return Questiontitle;
    }

    public void setQuestiontitle(String Questiontitle) {
        this.Questiontitle = Questiontitle;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getPictureUrl() {
        return PictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        PictureUrl = pictureUrl;
    }

    public boolean isIspicturerequired() {
        return Ispicturerequired;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isAnswerSelected() {
        return IsAnswerSelected;
    }

    public void setAnswerSelected(boolean answerSelected) {
        IsAnswerSelected = answerSelected;
    }

    public boolean isShowQuestion() {
        return ShowQuestion;
    }

    public void setShowQuestion(boolean showQuestion) {
        ShowQuestion = showQuestion;
    }

    public boolean isNoSelected() {
        return IsNoSelected;
    }

    public void setNoSelected(boolean noSelected) {
        IsNoSelected = noSelected;
    }

    public String getQuestionTitleDisplayText() {
        return QuestionTitleDisplayText;
    }

    public void setQuestionTitleDisplayText(String questionTitleDisplayText) {
        QuestionTitleDisplayText = questionTitleDisplayText;
    }
}
