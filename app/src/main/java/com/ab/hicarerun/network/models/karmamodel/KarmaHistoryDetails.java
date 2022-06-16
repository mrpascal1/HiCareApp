package com.ab.hicarerun.network.models.karmamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Arjun Bhatt on 10/13/2020.
 */
public class KarmaHistoryDetails {
    @SerializedName("EventName")
    @Expose
    private String eventName;
    @SerializedName("ScoreDescription")
    @Expose
    private String scoreDescription;
    @SerializedName("ScoreName")
    @Expose
    private String scoreName;
    @SerializedName("PointsDeducted")
    @Expose
    private Integer pointsDeducted;
    @SerializedName("LifeIndex")
    @Expose
    private Integer lifeIndex;
    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("VideoId")
    @Expose
    private Integer videoId;
    @SerializedName("VideoURL")
    @Expose
    private String videoURL;
    @SerializedName("CreatedOnDate")
    @Expose
    private String createdOnDate;
    @SerializedName("CreatedOnDateDisplay")
    @Expose
    private String createdOnDateDisplay;

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getScoreDescription() {
        return scoreDescription;
    }

    public void setScoreDescription(String scoreDescription) {
        this.scoreDescription = scoreDescription;
    }

    public String getScoreName() {
        return scoreName;
    }

    public void setScoreName(String scoreName) {
        this.scoreName = scoreName;
    }

    public Integer getPointsDeducted() {
        return pointsDeducted;
    }

    public void setPointsDeducted(Integer pointsDeducted) {
        this.pointsDeducted = pointsDeducted;
    }

    public Integer getLifeIndex() {
        return lifeIndex;
    }

    public void setLifeIndex(Integer lifeIndex) {
        this.lifeIndex = lifeIndex;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getVideoId() {
        return videoId;
    }

    public void setVideoId(Integer videoId) {
        this.videoId = videoId;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getCreatedOnDate() {
        return createdOnDate;
    }

    public void setCreatedOnDate(String createdOnDate) {
        this.createdOnDate = createdOnDate;
    }

    public String getCreatedOnDateDisplay() {
        return createdOnDateDisplay;
    }

    public void setCreatedOnDateDisplay(String createdOnDateDisplay) {
        this.createdOnDateDisplay = createdOnDateDisplay;
    }
}
