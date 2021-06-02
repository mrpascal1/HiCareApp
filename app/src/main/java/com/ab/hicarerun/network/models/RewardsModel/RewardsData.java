package com.ab.hicarerun.network.models.RewardsModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Arjun Bhatt on 3/2/2020.
 */
public class RewardsData {
    @SerializedName("ResourceId")
    @Expose
    private String resourceId;
    @SerializedName("TotalPointsEarned")
    @Expose
    private Integer totalPointsEarned;
    @SerializedName("TotalPointsUsed")
    @Expose
    private Integer totalPointsUsed;
    @SerializedName("TotalPendingPoints")
    @Expose
    private Integer totalPendingPoints;
    @SerializedName("AvailableOffers")
    @Expose
    private List<AvailableOffer> availableOffers = null;
    @SerializedName("RedeemedData")
    @Expose
    private List<RedeemedOffer> redeemedData = null;

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public Integer getTotalPointsEarned() {
        return totalPointsEarned;
    }

    public void setTotalPointsEarned(Integer totalPointsEarned) {
        this.totalPointsEarned = totalPointsEarned;
    }

    public Integer getTotalPointsUsed() {
        return totalPointsUsed;
    }

    public void setTotalPointsUsed(Integer totalPointsUsed) {
        this.totalPointsUsed = totalPointsUsed;
    }

    public Integer getTotalPendingPoints() {
        return totalPendingPoints;
    }

    public void setTotalPendingPoints(Integer totalPendingPoints) {
        this.totalPendingPoints = totalPendingPoints;
    }

    public List<AvailableOffer> getAvailableOffers() {
        return availableOffers;
    }

    public void setAvailableOffers(List<AvailableOffer> availableOffers) {
        this.availableOffers = availableOffers;
    }

    public List<RedeemedOffer> getRedeemedData() {
        return redeemedData;
    }

    public void setRedeemedData(List<RedeemedOffer> redeemedData) {
        this.redeemedData = redeemedData;
    }
}
