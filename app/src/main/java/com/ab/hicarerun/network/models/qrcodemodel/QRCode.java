package com.ab.hicarerun.network.models.qrcodemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Arjun Bhatt on 4/1/2020.
 */
public class QRCode {
    @SerializedName("Code")
    @Expose
    private String code;
    @SerializedName("URL")
    @Expose
    private String url;
    @SerializedName("OrderId")
    @Expose
    private String OrderId;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }
}
