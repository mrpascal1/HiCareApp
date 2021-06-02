package com.ab.hicarerun.network.models.LeaderBoardModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Arjun Bhatt on 4/9/2020.
 */
public class RewardLeaders {

    @SerializedName("ResourceId")
    @Expose
    private String resourceId;
    @SerializedName("ResourceName")
    @Expose
    private String resourceName;
    @SerializedName("ResourceImage")
    @Expose
    private String resourceImage;
    @SerializedName("TotalPoints")
    @Expose
    private Integer totalPoints;
    @SerializedName("TotalMissedPoints")
    @Expose
    private Integer TotalMissedPoints;
    @SerializedName("ServiceCenterName")
    @Expose
    private String serviceCenterName;
    @SerializedName("BadgeName")
    @Expose
    private String badgeName;
    @SerializedName("Rank")
    @Expose
    private Integer rank;
    @SerializedName("IsSameResource")
    @Expose
    private Boolean IsSameResource;


    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getResourceImage() {
        return resourceImage;
    }

    public void setResourceImage(String resourceImage) {
        this.resourceImage = resourceImage;
    }

    public Integer getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(Integer totalPoints) {
        this.totalPoints = totalPoints;
    }

    public String getServiceCenterName() {
        return serviceCenterName;
    }

    public void setServiceCenterName(String serviceCenterName) {
        this.serviceCenterName = serviceCenterName;
    }

    public String getBadgeName() {
        return badgeName;
    }

    public void setBadgeName(String badgeName) {
        this.badgeName = badgeName;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Boolean getSameResource() {
        return IsSameResource;
    }

    public void setSameResource(Boolean sameResource) {
        IsSameResource = sameResource;
    }

    public Integer getTotalMissedPoints() {
        return TotalMissedPoints;
    }

    public void setTotalMissedPoints(Integer totalMissedPoints) {
        TotalMissedPoints = totalMissedPoints;
    }
}
