package com.ab.hicarerun.network.models.RewardsModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Arjun Bhatt on 3/2/2020.
 */
public class SaveRedeemRequest {
    @SerializedName("OfferId")
    @Expose
    private Integer offerId;
    @SerializedName("ResourceId")
    @Expose
    private String resourceId;
    @SerializedName("PointsUsed")
    @Expose
    private Integer pointsUsed;

    public Integer getOfferId() {
        return offerId;
    }

    public void setOfferId(Integer offerId) {
        this.offerId = offerId;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public Integer getPointsUsed() {
        return pointsUsed;
    }

    public void setPointsUsed(Integer pointsUsed) {
        this.pointsUsed = pointsUsed;
    }
}
