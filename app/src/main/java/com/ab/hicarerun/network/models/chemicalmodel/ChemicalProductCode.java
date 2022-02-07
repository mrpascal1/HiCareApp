package com.ab.hicarerun.network.models.chemicalmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChemicalProductCode {
    @SerializedName("Product_Code__c") @Expose
    private String ProductCode;

    public ChemicalProductCode(){
        ProductCode = "NA";
    }

    public String getProductCode() {
        return ProductCode;
    }

    public void setProductCode(String productCode) {
        ProductCode = productCode;
    }
}
