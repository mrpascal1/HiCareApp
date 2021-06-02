package com.ab.hicarerun.network.models.KarmaModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Arjun Bhatt on 11/2/2020.
 */
public class SaveKarmaRequest {
    @SerializedName("ResourceId")
    @Expose
    private String resourceId;
    @SerializedName("LifeLineId")
    @Expose
    private Integer LifeLineId;
    @SerializedName("VideoId")
    @Expose
    private Integer VideoId;

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public Integer getLifeLineId() {
        return LifeLineId;
    }

    public void setLifeLineId(Integer lifeLineId) {
        LifeLineId = lifeLineId;
    }

    public Integer getVideoId() {
        return VideoId;
    }

    public void setVideoId(Integer videoId) {
        VideoId = videoId;
    }
}
