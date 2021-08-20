package com.ab.hicarerun.network.models.QuizModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Arjun Bhatt on 2/11/2021.
 */
public class QuizCategoryData {
    @SerializedName("PuzzleId")
    @Expose
    private Integer puzzleId;
    @SerializedName("PuzzleName")
    @Expose
    private String puzzleName;
    @SerializedName("PuzzleTitle")
    @Expose
    private String puzzleTitle;
    @SerializedName("PuzzleDescription")
    @Expose
    private String puzzleDescription;
    @SerializedName("PuzzleImageUrl")
    @Expose
    private String puzzleImageUrl;
    @SerializedName("IsTimeApplicable")
    @Expose
    private Integer isTimeApplicable;
    @SerializedName("TimerForEachQuestionInSeconds")
    @Expose
    private Integer timerForEachQuestionInSeconds;
    @SerializedName("IsPuzzleSubmitted")
    @Expose
    private Boolean isPuzzleSubmitted;
    @SerializedName("CompletionPecentage")
    @Expose
    private Integer completionPecentage;
    @SerializedName("IsCompleted")
    @Expose
    private Boolean isCompleted;
    @SerializedName("CompletionDateTime")
    @Expose
    private Object completionDateTime;

    public Boolean getPuzzleSubmitted() {
        return isPuzzleSubmitted;
    }

    public void setPuzzleSubmitted(Boolean puzzleSubmitted) {
        isPuzzleSubmitted = puzzleSubmitted;
    }

    public Integer getCompletionPecentage() {
        return completionPecentage;
    }

    public void setCompletionPecentage(Integer completionPecentage) {
        this.completionPecentage = completionPecentage;
    }

    public Boolean getCompleted() {
        return isCompleted;
    }

    public void setCompleted(Boolean completed) {
        isCompleted = completed;
    }

    public Integer getPuzzleId() {
        return puzzleId;
    }

    public void setPuzzleId(Integer puzzleId) {
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

    public String getPuzzleImageUrl() {
        return puzzleImageUrl;
    }

    public void setPuzzleImageUrl(String puzzleImageUrl) {
        this.puzzleImageUrl = puzzleImageUrl;
    }

    public Integer getIsTimeApplicable() {
        return isTimeApplicable;
    }

    public void setIsTimeApplicable(Integer isTimeApplicable) {
        this.isTimeApplicable = isTimeApplicable;
    }

    public Integer getTimerForEachQuestionInSeconds() {
        return timerForEachQuestionInSeconds;
    }

    public void setTimerForEachQuestionInSeconds(Integer timerForEachQuestionInSeconds) {
        this.timerForEachQuestionInSeconds = timerForEachQuestionInSeconds;
    }

    public Boolean getIsPuzzleSubmitted() {
        return isPuzzleSubmitted;
    }

    public void setIsPuzzleSubmitted(Boolean isPuzzleSubmitted) {
        this.isPuzzleSubmitted = isPuzzleSubmitted;
    }

    public Object getCompletionDateTime() {
        return completionDateTime;
    }

    public void setCompletionDateTime(Object completionDateTime) {
        this.completionDateTime = completionDateTime;
    }
}
