package com.ab.hicarerun.network.models.voucher;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VoucherRequest {
    @SerializedName("UserId")
    @Expose
    private String UserId;
    @SerializedName("OrderNo")
    @Expose
    private String OrderNo;
    @SerializedName("MobileNo")
    @Expose
    private String MobileNo;

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String orderNo) {
        OrderNo = orderNo;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }
}
