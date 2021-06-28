package com.ab.hicarerun.network.models.ChemicalModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Arjun Bhatt on 6/25/2021.
 */
public class ActivityChemicalData {
    @SerializedName("Chemical_Name")
    @Expose
    private String chemicalName;
    @SerializedName("Quantity")
    @Expose
    private Double quantity;

    public String getChemicalName() {
        return chemicalName;
    }

    public void setChemicalName(String chemicalName) {
        this.chemicalName = chemicalName;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

}
