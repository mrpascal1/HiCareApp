package com.ab.hicarerun.network.models.karmamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Arjun Bhatt on 10/8/2020.
 */
public class KarmaDetailList {
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
}
