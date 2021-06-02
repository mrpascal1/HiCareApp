package com.ab.hicarerun.network.models.OffersModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Arjun Bhatt on 3/26/2020.
 */
public class UpdateRewardScratchRequest {
    @SerializedName("ResourceId")
    @Expose
    private String resourceId;
    @SerializedName("Id")
    @Expose
    private String id;
    @SerializedName("IsRewardScratchDone")
    @Expose
    private Boolean isRewardScratchDone;

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getIsRewardScratchDone() {
        return isRewardScratchDone;
    }

    public void setIsRewardScratchDone(Boolean isRewardScratchDone) {
        this.isRewardScratchDone = isRewardScratchDone;
    }
}
