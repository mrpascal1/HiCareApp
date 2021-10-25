package com.ab.hicarerun.network.models.ConsulationModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Arjun Bhatt on 7/23/2020.
 */
public class Recommendations {
    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("RecommendationTitle")
    @Expose
    private String recommendationTitle;
    @SerializedName("OverallInfestationLevel")
    @Expose
    private String OverallInfestationLevel;
    @SerializedName("RecommendationDescription")
    @Expose
    private String recommendationDescription;
    @SerializedName("ChemicalValue")
    @Expose
    private String chemicalValue;
    @SerializedName("DefaultArea")
    @Expose
    private String defaultArea;
    @SerializedName("ExtraArea")
    @Expose
    private String extraArea;
    @SerializedName("Duration")
    @Expose
    private String duration;

    @SerializedName("RecommendationImageUrl")
    @Expose
    private String recommendationImageUrl;

    @SerializedName("RecommendationAudioUrl")
    @Expose
    private String recommendationAudioUrl;

    @SerializedName("IsAudioEnabled")
    @Expose
    private boolean isAudioEnabled;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRecommendationTitle() {
        return recommendationTitle;
    }

    public void setRecommendationTitle(String recommendationTitle) {
        this.recommendationTitle = recommendationTitle;
    }

    public String getRecommendationDescription() {
        return recommendationDescription;
    }

    public void setRecommendationDescription(String recommendationDescription) {
        this.recommendationDescription = recommendationDescription;
    }

    public String getChemicalValue() {
        return chemicalValue;
    }

    public void setChemicalValue(String chemicalValue) {
        this.chemicalValue = chemicalValue;
    }

    public String getDefaultArea() {
        return defaultArea;
    }

    public void setDefaultArea(String defaultArea) {
        this.defaultArea = defaultArea;
    }

    public String getExtraArea() {
        return extraArea;
    }

    public void setExtraArea(String extraArea) {
        this.extraArea = extraArea;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getOverallInfestationLevel() {
        return OverallInfestationLevel;
    }

    public void setOverallInfestationLevel(String overallInfestationLevel) {
        OverallInfestationLevel = overallInfestationLevel;
    }

    public String getRecommendationImageUrl() {
        return recommendationImageUrl;
    }

    public void setRecommendationImageUrl(String recommendationImageUrl) {
        this.recommendationImageUrl = recommendationImageUrl;
    }

    public String getRecommendationAudioUrl() {
        return recommendationAudioUrl;
    }

    public void setRecommendationAudioUrl(String recommendationAudioUrl) {
        this.recommendationAudioUrl = recommendationAudioUrl;
    }

    public boolean isAudioEnabled() {
        return isAudioEnabled;
    }

    public void setAudioEnabled(boolean audioEnabled) {
        isAudioEnabled = audioEnabled;
    }
}
