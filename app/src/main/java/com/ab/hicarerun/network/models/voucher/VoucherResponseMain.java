package com.ab.hicarerun.network.models.voucher;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VoucherResponseMain {

    @SerializedName("IsSuccess") @Expose
    private Boolean IsSuccess;
    @SerializedName("Message") @Expose
    private String message = null;
    @SerializedName("com.ab.hicarerun.network.models.TSScannerModel.Data") @Expose
    private VoucherResponse data = null;

    public Boolean getSuccess() {
        return IsSuccess;
    }

    public void setSuccess(Boolean success) {
        IsSuccess = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public VoucherResponse getData() {
        return data;
    }

    public void setData(VoucherResponse data) {
        this.data = data;
    }
}
