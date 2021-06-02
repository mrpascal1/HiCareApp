package com.ab.hicarerun.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.ab.hicarerun.network.models.ReferralModel.ReferralList;
import com.ab.hicarerun.network.models.SelfAssessModel.ResourceCheckList;

/**
 * Created by Arjun Bhatt on 5/20/2020.
 */
public class SelfAccessViewModel implements Parcelable {
    private Integer Id;
    private String Title;
    private String DisplayTitle;
    private String Description;
    private String CType;
    private Boolean ShowText;
    private Boolean IsActive;
    private String Created_By;

    public SelfAccessViewModel() {
        Id = 0;
        Title = "NA";
        DisplayTitle = "NA";
        Description = "NA";
        CType = "NA";
        ShowText = false;
        IsActive = false;
        Created_By = "NA";
    }


    protected SelfAccessViewModel(Parcel in) {
        Id = in.readInt();
        Title = in.readString();
        DisplayTitle = in.readString();
        Description = in.readString();
        CType = in.readString();
        byte tmpShowText = in.readByte();
        ShowText = tmpShowText == 0 ? null : tmpShowText == 1;
        byte tmpIsActive = in.readByte();
        IsActive = tmpIsActive == 0 ? null : tmpIsActive == 1;
        Created_By = in.readString();
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

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getCType() {
        return CType;
    }

    public void setCType(String CType) {
        this.CType = CType;
    }

    public Boolean getShowText() {
        return ShowText;
    }

    public void setShowText(Boolean showText) {
        ShowText = showText;
    }

    public Boolean getActive() {
        return IsActive;
    }

    public void setActive(Boolean active) {
        IsActive = active;
    }

    public String getCreated_By() {
        return Created_By;
    }

    public void setCreated_By(String created_By) {
        Created_By = created_By;
    }

    public static final Creator<SelfAccessViewModel> CREATOR = new Creator<SelfAccessViewModel>() {
        @Override
        public SelfAccessViewModel createFromParcel(Parcel in) {
            return new SelfAccessViewModel(in);
        }

        @Override
        public SelfAccessViewModel[] newArray(int size) {
            return new SelfAccessViewModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Id);
        dest.writeString(Title);
        dest.writeString(DisplayTitle);
        dest.writeString(Description);
        dest.writeString(CType);
        dest.writeByte((byte) (ShowText == null ? 0 : ShowText ? 1 : 2));
        dest.writeByte((byte) (IsActive == null ? 0 : IsActive ? 1 : 2));
        dest.writeString(Created_By);
    }

    public void clone(ResourceCheckList checkList) {
        this.Id = checkList.getId();
        this.Title = checkList.getTitle();
        this.DisplayTitle = checkList.getDisplayTitle();
//        this.Description = checkList.getDescription();
        this.CType = checkList.getCType();
        this.ShowText= checkList.getShowText();
        this.IsActive = checkList.getIsActive();
//        this.Created_By = checkList.getCreatedByIdUser();

    }
}


