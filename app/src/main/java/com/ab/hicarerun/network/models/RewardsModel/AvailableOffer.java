package com.ab.hicarerun.network.models.RewardsModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Arjun Bhatt on 3/2/2020.
 */
public class AvailableOffer {

    @SerializedName("OfferId")
    @Expose
    private Integer offerId;
    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("ImageURL")
    @Expose
    private String imageURL;
    @SerializedName("PointsRequired")
    @Expose
    private Integer pointsRequired;
    @SerializedName("IsLocked")
    @Expose
    private boolean isLocked;
    @SerializedName("UnlocksAtLevel")
    @Expose
    private String unlocksAtLevel;

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public String getUnlocksAtLevel() {
        return unlocksAtLevel;
    }

    public void setUnlocksAtLevel(String unlocksAtLevel) {
        this.unlocksAtLevel = unlocksAtLevel;
    }

    public Integer getOfferId() {
        return offerId;
    }

    public void setOfferId(Integer offerId) {
        this.offerId = offerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Integer getPointsRequired() {
        return pointsRequired;
    }

    public void setPointsRequired(Integer pointsRequired) {
        this.pointsRequired = pointsRequired;
    }

}
