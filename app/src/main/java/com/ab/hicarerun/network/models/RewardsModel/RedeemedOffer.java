package com.ab.hicarerun.network.models.RewardsModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Arjun Bhatt on 3/2/2020.
 */
public class RedeemedOffer {

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
    @SerializedName("PointsUsed")
    @Expose
    private Integer pointsUsed;
    @SerializedName("OfferRedeemedDate")
    @Expose
    private String offerRedeemedDate;
    @SerializedName("OfferRedeemedDateString")
    @Expose
    private String offerRedeemedDateString;

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

    public Integer getPointsUsed() {
        return pointsUsed;
    }

    public void setPointsUsed(Integer pointsUsed) {
        this.pointsUsed = pointsUsed;
    }

    public String getOfferRedeemedDate() {
        return offerRedeemedDate;
    }

    public void setOfferRedeemedDate(String offerRedeemedDate) {
        this.offerRedeemedDate = offerRedeemedDate;
    }

    public String getOfferRedeemedDateString() {
        return offerRedeemedDateString;
    }

    public void setOfferRedeemedDateString(String offerRedeemedDateString) {
        this.offerRedeemedDateString = offerRedeemedDateString;
    }

}
