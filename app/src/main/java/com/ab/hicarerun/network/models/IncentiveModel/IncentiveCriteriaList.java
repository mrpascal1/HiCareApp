package com.ab.hicarerun.network.models.IncentiveModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Arjun Bhatt on 7/10/2020.
 */
public class IncentiveCriteriaList {
    @SerializedName("Criteria")
    @Expose
    private String criteria;
    @SerializedName("Amount")
    @Expose
    private String amount;

    public String getCriteria() {
        return criteria;
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
