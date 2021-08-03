package com.ab.hicarerun.network.models.QuizModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QuizSaveAnswers {
    @SerializedName("PuzzleId")
    @Expose
    private Integer puzzleId;
    @SerializedName("PuzzleQuestionId")
    @Expose
    private Integer puzzleQuestionId;
    @SerializedName("CorrectAnswerIds")
    @Expose
    private String correctAnswerIds;
    @SerializedName("ResourceGivenAnswerIds")
    @Expose
    private String resourceGivenAnswerIds;
    @SerializedName("ResourceId")
    @Expose
    private String resourceId;
    @SerializedName("Points")
    @Expose
    private Integer points;

    public QuizSaveAnswers() {
    }

    public QuizSaveAnswers(Integer puzzleId, Integer puzzleQuestionId, String correctAnswerIds, String resourceGivenAnswerIds, String resourceId, Integer points) {
        this.puzzleId = puzzleId;
        this.puzzleQuestionId = puzzleQuestionId;
        this.correctAnswerIds = correctAnswerIds;
        this.resourceGivenAnswerIds = resourceGivenAnswerIds;
        this.resourceId = resourceId;
        this.points = points;
    }

    public Integer getPuzzleId() {
        return puzzleId;
    }

    public void setPuzzleId(Integer puzzleId) {
        this.puzzleId = puzzleId;
    }

    public Integer getPuzzleQuestionId() {
        return puzzleQuestionId;
    }

    public void setPuzzleQuestionId(Integer puzzleQuestionId) {
        this.puzzleQuestionId = puzzleQuestionId;
    }

    public String getCorrectAnswerIds() {
        return correctAnswerIds;
    }

    public void setCorrectAnswerIds(String correctAnswerIds) {
        this.correctAnswerIds = correctAnswerIds;
    }

    public String getResourceGivenAnswerIds() {
        return resourceGivenAnswerIds;
    }

    public void setResourceGivenAnswerIds(String resourceGivenAnswerIds) {
        this.resourceGivenAnswerIds = resourceGivenAnswerIds;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "QuizSaveAnswers{" +
                "puzzleId=" + puzzleId +
                ", puzzleQuestionId=" + puzzleQuestionId +
                ", correctAnswerIds='" + correctAnswerIds + '\'' +
                ", resourceGivenAnswerIds='" + resourceGivenAnswerIds + '\'' +
                ", resourceId='" + resourceId + '\'' +
                ", points=" + points +
                '}';
    }
}
