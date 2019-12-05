package com.ab.hicarerun.network.models.PayementModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Arjun Bhatt on 12/4/2019.
 */
public class BankNames {
    @SerializedName("names")
    @Expose
    private String BankNames;


    public String getBankNames() {
        return BankNames;
    }

    public void setBankNames(String bankNames) {
        BankNames = bankNames;
    }
}
