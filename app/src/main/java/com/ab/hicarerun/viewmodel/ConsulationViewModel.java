package com.ab.hicarerun.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.ab.hicarerun.network.models.ConsulationModel.Data;
import com.ab.hicarerun.network.models.ConsulationModel.Optionlist;
import com.ab.hicarerun.network.models.OnSiteModel.OnSiteRecent;
import com.ab.hicarerun.network.models.OnSiteModel.RecentActivityDetails;

import java.util.List;

/**
 * Created by Arjun Bhatt on 7/18/2020.
 */
public class ConsulationViewModel implements Parcelable {
    private String Id;
    private String Name;
    private String QuestionTitle;
    private String QuestionDisplayTitle;
    private String QuestionType;
    private String QuestionCategory;
    private Boolean isPictureRequired;
    private Boolean isAnswerSelected;
    private Boolean ShowQuestion;
    private Boolean isNoSelected;
    private String PictureURL;
    private String Options;
    private String AnswerText;
    private String SRId;
    private String AccountId;
    private String OrderId;
    private String TaskId;
    private List<Optionlist> optionlists = null;

    public ConsulationViewModel() {
        this.Id = "NA";
        this.Name = "NA";
        this.QuestionTitle = "NA";
        this.QuestionDisplayTitle = "NA";
        this.QuestionType = "NA";
        this.QuestionCategory = "NA";
        this.isPictureRequired = false;
        this.isAnswerSelected = false;
        this.ShowQuestion = false;
        this.isNoSelected = false;
        this.PictureURL = "NA";
        this.Options = "NA";
        this.AnswerText = "NA";
        this.SRId = "NA";
        this.AccountId = "NA";
        this.OrderId = "NA";
        this.TaskId = "NA";
        this.optionlists = null;
    }


    protected ConsulationViewModel(Parcel in) {
        Id = in.readString();
        Name = in.readString();
        QuestionTitle = in.readString();
        QuestionDisplayTitle = in.readString();
        QuestionType = in.readString();
        QuestionCategory = in.readString();
        byte tmpIsPictureRequired = in.readByte();
        isPictureRequired = tmpIsPictureRequired == 0 ? null : tmpIsPictureRequired == 1;
        byte tmpIsAnswerSelected = in.readByte();
        isAnswerSelected = tmpIsAnswerSelected == 0 ? null : tmpIsAnswerSelected == 1;
        byte tmpShowQuestion = in.readByte();
        ShowQuestion = tmpShowQuestion == 0 ? null : tmpShowQuestion == 1;
        byte tmpIsNoSelected = in.readByte();
        isNoSelected = tmpIsNoSelected == 0 ? null : tmpIsNoSelected == 1;
        PictureURL = in.readString();
        Options = in.readString();
        AnswerText = in.readString();
        SRId = in.readString();
        AccountId = in.readString();
        OrderId = in.readString();
        TaskId = in.readString();
    }

    public static final Creator<ConsulationViewModel> CREATOR = new Creator<ConsulationViewModel>() {
        @Override
        public ConsulationViewModel createFromParcel(Parcel in) {
            return new ConsulationViewModel(in);
        }

        @Override
        public ConsulationViewModel[] newArray(int size) {
            return new ConsulationViewModel[size];
        }
    };

    public Boolean getNoSelected() {
        return isNoSelected;
    }

    public void setNoSelected(Boolean noSelected) {
        isNoSelected = noSelected;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getQuestionTitle() {
        return QuestionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        QuestionTitle = questionTitle;
    }

    public String getQuestionType() {
        return QuestionType;
    }

    public void setQuestionType(String questionType) {
        QuestionType = questionType;
    }

    public String getQuestionCategory() {
        return QuestionCategory;
    }

    public void setQuestionCategory(String questionCategory) {
        QuestionCategory = questionCategory;
    }

    public Boolean getPictureRequired() {
        return isPictureRequired;
    }

    public void setPictureRequired(Boolean pictureRequired) {
        isPictureRequired = pictureRequired;
    }

    public String getPictureURL() {
        return PictureURL;
    }

    public void setPictureURL(String pictureURL) {
        PictureURL = pictureURL;
    }

    public String getOptions() {
        return Options;
    }

    public void setOptions(String options) {
        Options = options;
    }

    public String getAnswerText() {
        return AnswerText;
    }

    public void setAnswerText(String answerText) {
        AnswerText = answerText;
    }

    public String getSRId() {
        return SRId;
    }

    public void setSRId(String SRId) {
        this.SRId = SRId;
    }

    public String getAccountId() {
        return AccountId;
    }

    public void setAccountId(String accountId) {
        AccountId = accountId;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public String getTaskId() {
        return TaskId;
    }

    public void setTaskId(String taskId) {
        TaskId = taskId;
    }

    public List<Optionlist> getOptionlists() {
        return optionlists;
    }

    public void setOptionlists(List<Optionlist> optionlists) {
        this.optionlists = optionlists;
    }

    public Boolean getAnswerSelected() {
        return isAnswerSelected;
    }

    public void setAnswerSelected(Boolean answerSelected) {
        isAnswerSelected = answerSelected;
    }

    public Boolean getShowQuestion() {
        return ShowQuestion;
    }

    public void setShowQuestion(Boolean showQuestion) {
        ShowQuestion = showQuestion;
    }

    public String getQuestionDisplayTitle() {
        return QuestionDisplayTitle;
    }

    public void setQuestionDisplayTitle(String questionDisplayTitle) {
        QuestionDisplayTitle = questionDisplayTitle;
    }

    public void clone(Data account) {
        this.Id = account.getId();
        this.Name = account.getName();
        this.QuestionTitle = account.getQuestiontitle();
        this.QuestionDisplayTitle = account.getQuestionTitleDisplayText();
        this.QuestionType = account.getQuestiontype();
        this.QuestionCategory = account.getQuestioncategory();
        this.isPictureRequired = account.getIspicturerequired();
        this.isAnswerSelected = account.isAnswerSelected();
        this.PictureURL = account.getPictureUrl();
        this.Options = account.getOptions();
        this.AnswerText = account.getAnswertext();
        this.SRId = account.getSrid();
        this.AccountId = account.getAccountid();
        this.OrderId = account.getOrderid();
        this.TaskId = account.getTaskid();
        this.optionlists = account.getOptionlist();
        this.ShowQuestion = account.isShowQuestion();
        this.isNoSelected = account.isNoSelected();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Id);
        dest.writeString(Name);
        dest.writeString(QuestionTitle);
        dest.writeString(QuestionDisplayTitle);
        dest.writeString(QuestionType);
        dest.writeString(QuestionCategory);
        dest.writeByte((byte) (isPictureRequired == null ? 0 : isPictureRequired ? 1 : 2));
        dest.writeByte((byte) (isAnswerSelected == null ? 0 : isAnswerSelected ? 1 : 2));
        dest.writeByte((byte) (ShowQuestion == null ? 0 : ShowQuestion ? 1 : 2));
        dest.writeByte((byte) (isNoSelected == null ? 0 : isNoSelected ? 1 : 2));
        dest.writeString(PictureURL);
        dest.writeString(Options);
        dest.writeString(AnswerText);
        dest.writeString(SRId);
        dest.writeString(AccountId);
        dest.writeString(OrderId);
        dest.writeString(TaskId);
    }
}
