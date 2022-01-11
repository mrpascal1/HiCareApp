package com.ab.hicarerun.network.models.chemicalcountmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Arjun Bhatt on 1/20/2020.
 */
public class ChemicalCount {
    @SerializedName("Header")
    @Expose
    private List<String> header = null;
    @SerializedName("Data")
    @Expose
    private List<Object> data = null;

    public List<String> getHeader() {
        return header;
    }

    public void setHeader(List<String> header) {
        this.header = header;
    }

    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }
}
