package com.ab.hicarerun.network.models.QuizModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Arjun Bhatt on 2/11/2021.
 */
public class QuizData {
    @SerializedName("PuzzleId")
    @Expose
    private Integer puzzleId;
    @SerializedName("PuzzleQuestionType")
    @Expose
    private String puzzleQuestionType;
    @SerializedName("PuzzleQuestionId")
    @Expose
    private Integer puzzleQuestionId;
    @SerializedName("PuzzleQuestionSelectionType")
    @Expose
    private String puzzleQuestionSelectionType;
    @SerializedName("PuzzleQuestionTitle")
    @Expose
    private String puzzleQuestionTitle;
    @SerializedName("PuzzleQuestionDescription")
    @Expose
    private String puzzleQuestionDescription;
    @SerializedName("PuzzleQuestionURL")
    @Expose
    private String puzzleQuestionURL;
    @SerializedName("Points")
    @Expose
    private Integer points;
    @SerializedName("ParentQuestionId")
    @Expose
    private Integer parentQuestionId;
    @SerializedName("isDependentQuestionExist")
    @Expose
    private Boolean isDependentQuestionExist;
    @SerializedName("Options")
    @Expose
    private List<QuizOption> options = null;
    @SerializedName("CorrectAnswers")
    @Expose
    private List<QuizAnswer> correctAnswers;
    @SerializedName("CorrectAnswerIds")
    @Expose
    private String correctAnswerIds;
    @SerializedName("DependentQuestionList")
    @Expose
    private List<VideoDependentQuest> dependentQuestionList = null;

    public Integer getPuzzleId() {
        return puzzleId;
    }

    public void setPuzzleId(Integer puzzleId) {
        this.puzzleId = puzzleId;
    }

    public String getPuzzleQuestionType() {
        return puzzleQuestionType;
    }

    public void setPuzzleQuestionType(String puzzleQuestionType) {
        this.puzzleQuestionType = puzzleQuestionType;
    }

    public Integer getPuzzleQuestionId() {
        return puzzleQuestionId;
    }

    public void setPuzzleQuestionId(Integer puzzleQuestionId) {
        this.puzzleQuestionId = puzzleQuestionId;
    }

    public String getPuzzleQuestionSelectionType() {
        return puzzleQuestionSelectionType;
    }

    public void setPuzzleQuestionSelectionType(String puzzleQuestionSelectionType) {
        this.puzzleQuestionSelectionType = puzzleQuestionSelectionType;
    }

    public String getPuzzleQuestionTitle() {
        return puzzleQuestionTitle;
    }

    public void setPuzzleQuestionTitle(String puzzleQuestionTitle) {
        this.puzzleQuestionTitle = puzzleQuestionTitle;
    }

    public String getPuzzleQuestionDescription() {
        return puzzleQuestionDescription;
    }

    public void setPuzzleQuestionDescription(String puzzleQuestionDescription) {
        this.puzzleQuestionDescription = puzzleQuestionDescription;
    }

    public String getPuzzleQuestionURL() {
        return puzzleQuestionURL;
    }

    public void setPuzzleQuestionURL(String puzzleQuestionURL) {
        this.puzzleQuestionURL = puzzleQuestionURL;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getParentQuestionId() {
        return parentQuestionId;
    }

    public void setParentQuestionId(Integer parentQuestionId) {
        this.parentQuestionId = parentQuestionId;
    }

    public Boolean getIsDependentQuestionExist() {
        return isDependentQuestionExist;
    }

    public void setIsDependentQuestionExist(Boolean isDependentQuestionExist) {
        this.isDependentQuestionExist = isDependentQuestionExist;
    }

    public List<QuizOption> getOptions() {
        return options;
    }

    public void setOptions(List<QuizOption> options) {
        this.options = options;
    }

    public List<QuizAnswer> getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(List<QuizAnswer> correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public String getCorrectAnswerIds() {
        return correctAnswerIds;
    }

    public void setCorrectAnswerIds(String correctAnswerIds) {
        this.correctAnswerIds = correctAnswerIds;
    }

    public List<VideoDependentQuest> getDependentQuestionList() {
        return dependentQuestionList;
    }

    public void setDependentQuestionList(List<VideoDependentQuest> dependentQuestionList) {
        this.dependentQuestionList = dependentQuestionList;
    }
}
