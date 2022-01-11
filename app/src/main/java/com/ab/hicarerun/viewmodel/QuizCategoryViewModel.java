package com.ab.hicarerun.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.ab.hicarerun.network.models.quizmodel.QuizCategoryData;

/**
 * Created by Arjun Bhatt on 2/11/2021.
 */
public class QuizCategoryViewModel implements Parcelable {
    private int puzzleId;
    private String puzzleName;
    private String puzzleTitle;
    private String puzzleTitleDisplay;
    private String puzzleDescription;
    private String puzzleUrl;
    private Integer completionPecentage;
    private boolean isCompleted;
    private boolean isPuzzleSubmitted;
    private String completionDateTime;

    public QuizCategoryViewModel() {
        this.puzzleId = 0;
        this.puzzleName = "NA";
        this.puzzleTitle = "NA";
        this.puzzleTitleDisplay = "NA";
        this.puzzleDescription = "NA";
        this.puzzleUrl = "NA";
        this.isPuzzleSubmitted = false;
        this.completionDateTime = "NA";
        this.completionPecentage = 0;
        this.isCompleted = false;
    }

    protected QuizCategoryViewModel(Parcel in) {
        puzzleId = in.readInt();
        puzzleName = in.readString();
        puzzleTitle = in.readString();
        puzzleTitleDisplay = in.readString();
        puzzleDescription = in.readString();
        puzzleUrl = in.readString();
        isPuzzleSubmitted = in.readByte() != 0;
        completionDateTime = in.readString();
        completionPecentage = in.readInt();
        isCompleted = in.readByte() != 0;
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

    public Integer getCompletionPecentage() {
        return completionPecentage;
    }

    public void setCompletionPecentage(Integer completionPecentage) {
        this.completionPecentage = completionPecentage;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

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

    public String getPuzzleTitleDisplay() {
        return puzzleTitleDisplay;
    }

    public void setPuzzleTitleDisplay(String puzzleTitleDisplay) {
        this.puzzleTitleDisplay = puzzleTitleDisplay;
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
        parcel.writeString(puzzleTitleDisplay);
        parcel.writeString(puzzleDescription);
        parcel.writeString(puzzleUrl);
        parcel.writeByte((byte) (isPuzzleSubmitted ? 1 : 0));
        parcel.writeString(completionDateTime);
        parcel.writeInt(completionPecentage);
        parcel.writeByte((byte) (isCompleted ? 1 : 0));
    }

    public void clone(QuizCategoryData data) {
        this.puzzleId = data.getPuzzleId();
        this.puzzleName = data.getPuzzleName();
        this.puzzleTitle = data.getPuzzleTitle();
        this.puzzleTitleDisplay = data.getPuzzleTitleDisplay();
        this.puzzleDescription = data.getPuzzleDescription();
        this.puzzleUrl = data.getPuzzleImageUrl();
        this.isPuzzleSubmitted = data.getIsPuzzleSubmitted();
        this.completionDateTime = data.getPuzzleDescription();
        this.completionPecentage = data.getCompletionPecentage();
        this.isCompleted = data.getCompleted();
    }
}
