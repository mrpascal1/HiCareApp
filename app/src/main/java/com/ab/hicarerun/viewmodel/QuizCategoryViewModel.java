package com.ab.hicarerun.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.ab.hicarerun.network.models.ProductModel.ProductData;
import com.ab.hicarerun.network.models.QuizModel.QuizCategoryData;

/**
 * Created by Arjun Bhatt on 2/11/2021.
 */
public class QuizCategoryViewModel implements Parcelable {
    private int puzzleId;
    private String puzzleName;
    private String puzzleTitle;
    private String puzzleDescription;
    private String puzzleUrl;
    private boolean isPuzzleSubmitted;
    private String completionDateTime;

    public QuizCategoryViewModel() {
        this.puzzleId = 0;
        this.puzzleName = "NA";
        this.puzzleTitle = "NA";
        this.puzzleDescription = "NA";
        this.puzzleUrl = "NA";
        this.isPuzzleSubmitted = false;
        this.completionDateTime = "NA";
    }

    protected QuizCategoryViewModel(Parcel in) {
        puzzleId = in.readInt();
        puzzleName = in.readString();
        puzzleTitle = in.readString();
        puzzleDescription = in.readString();
        puzzleUrl = in.readString();
        isPuzzleSubmitted = in.readByte() != 0;
        completionDateTime = in.readString();
    }

    public static final Creator<QuizCategoryViewModel> CREATOR = new Creator<QuizCategoryViewModel>() {
        @Override
        public QuizCategoryViewModel createFromParcel(Parcel in) {
            return new QuizCategoryViewModel(in);
        }

        @Override
        public QuizCategoryViewModel[] newArray(int size) {
            return new QuizCategoryViewModel[size];
        }
    };

    public int getPuzzleId() {
        return puzzleId;
    }

    public void setPuzzleId(int puzzleId) {
        this.puzzleId = puzzleId;
    }

    public String getPuzzleName() {
        return puzzleName;
    }

    public void setPuzzleName(String puzzleName) {
        this.puzzleName = puzzleName;
    }

    public String getPuzzleTitle() {
        return puzzleTitle;
    }

    public void setPuzzleTitle(String puzzleTitle) {
        this.puzzleTitle = puzzleTitle;
    }

    public String getPuzzleDescription() {
        return puzzleDescription;
    }

    public void setPuzzleDescription(String puzzleDescription) {
        this.puzzleDescription = puzzleDescription;
    }

    public String getPuzzleUrl() {
        return puzzleUrl;
    }

    public void setPuzzleUrl(String puzzleUrl) {
        this.puzzleUrl = puzzleUrl;
    }

    public boolean isPuzzleSubmitted() {
        return isPuzzleSubmitted;
    }

    public void setPuzzleSubmitted(boolean puzzleSubmitted) {
        isPuzzleSubmitted = puzzleSubmitted;
    }

    public String getCompletionDateTime() {
        return completionDateTime;
    }

    public void setCompletionDateTime(String completionDateTime) {
        this.completionDateTime = completionDateTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(puzzleId);
        parcel.writeString(puzzleName);
        parcel.writeString(puzzleTitle);
        parcel.writeString(puzzleDescription);
        parcel.writeString(puzzleUrl);
        parcel.writeByte((byte) (isPuzzleSubmitted ? 1 : 0));
        parcel.writeString(completionDateTime);
    }

    public void clone(QuizCategoryData data) {
        this.puzzleId = data.getPuzzleId();
        this.puzzleName = data.getPuzzleName();
        this.puzzleTitle = data.getPuzzleTitle();
        this.puzzleDescription = data.getPuzzleDescription();
        this.puzzleUrl = data.getPuzzleImageUrl();
        this.isPuzzleSubmitted = data.getIsPuzzleSubmitted();
        this.completionDateTime = data.getPuzzleDescription();
    }
}
