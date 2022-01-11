package com.ab.hicarerun.network.models.serviceplanmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Arjun Bhatt on 6/1/2020.
 */
public class PaymentMode {
    @SerializedName("Text")
    @Expose
    private String text;
    @SerializedName("Value")
    @Expose
    private String value;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
