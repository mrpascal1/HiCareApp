package com.ab.hicarerun.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.ab.hicarerun.network.models.SelfAssessModel.AssessmentReport;
import com.ab.hicarerun.network.models.SelfAssessModel.ResourceCheckList;

/**
 * Created by Arjun Bhatt on 5/21/2020.
 */
public class AssessmentReportViewModel implements Parcelable {
    private Integer Id;
    private String Title;
    private String DisplayTitle;
    private String OptionText;
    private Boolean IsSelected;
    private String Created_On;
    private String Created_On_Text;

    public AssessmentReportViewModel() {
        Id = 0;
        Title = "NA";
        DisplayTitle = "NA";
        OptionText = "NA";
        Created_On = "NA";
        IsSelected = false;
        Created_On_Text = "NA";
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDisplayTitle() {
        return DisplayTitle;
    }

    public void setDisplayTitle(String displayTitle) {
        DisplayTitle = displayTitle;
    }

    public String getOptionText() {
        return OptionText;
    }

    public void setOptionText(String optionText) {
        OptionText = optionText;
    }

    public Boolean getSelected() {
        return IsSelected;
    }

    public void setSelected(Boolean selected) {
        IsSelected = selected;
    }

    public String getCreated_On() {
        return Created_On;
    }

    public void setCreated_On(String created_On) {
        Created_On = created_On;
    }

    public String getCreated_On_Text() {
        return Created_On_Text;
    }

    public void setCreated_On_Text(String created_On_Text) {
        Created_On_Text = created_On_Text;
    }

    protected AssessmentReportViewModel(Parcel in) {
        if (in.readByte() == 0) {
            Id = null;
        } else {
            Id = in.readInt();
        }
        Title = in.readString();
        DisplayTitle = in.readString();
        OptionText = in.readString();
        byte tmpIsSelected = in.readByte();
        IsSelected = tmpIsSelected == 0 ? null : tmpIsSelected == 1;
        Created_On = in.readString();
        Created_On_Text = in.readString();
    }

    public static final Creator<AssessmentReportViewModel> CREATOR = new Creator<AssessmentReportViewModel>() {
        @Override
        public AssessmentReportViewModel createFromParcel(Parcel in) {
            return new AssessmentReportViewModel(in);
        }

        @Override
        public AssessmentReportViewModel[] newArray(int size) {
            return new AssessmentReportViewModel[size];
        }
    };

    public void clone(AssessmentReport checkList) {
        this.Id = checkList.getOptionId();
        this.Title = checkList.getOptionTitle();
        this.DisplayTitle = checkList.getDisplayOptionTitle();
        this.OptionText = checkList.getOptionText();
        this.Created_On = checkList.getCreatedOn();
        this.IsSelected = checkList.getIsSelected();
        this.Created_On_Text = checkList.getCreatedOnText();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (Id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(Id);
        }
        dest.writeString(Title);
        dest.writeString(DisplayTitle);
        dest.writeString(OptionText);
        dest.writeByte((byte) (IsSelected == null ? 0 : IsSelected ? 1 : 2));
        dest.writeString(Created_On);
        dest.writeString(Created_On_Text);
    }
}