package com.ab.hicarerun.network.models.referralmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Arjun Bhatt on 7/9/2020.
 */
public class ReferralSRData {
    @SerializedName("Services")
    @Expose
    private List<ReferralService> services = null;
    @SerializedName("Relation")
    @Expose
    private List<ReferralRelation> relation = null;

    public List<ReferralService> getServices() {
        return services;
    }

    public void setServices(List<ReferralService> services) {
        this.services = services;
    }

    public List<ReferralRelation> getRelation() {
        return relation;
    }

    public void setRelation(List<ReferralRelation> relation) {
        this.relation = relation;
    }
}
