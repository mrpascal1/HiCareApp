package com.ab.hicarerun.network.models.AttachmentModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Arjun Bhatt on 1/6/2020.
 */
public class AttachmentMSTResponse {
    @SerializedName("IsSuccess")
    @Expose
    private Boolean isSuccess;
    @SerializedName("com.ab.hicarerun.network.models.TSScannerModel.Data")
    @Expose
    private List<MSTAttachment> data = null;

    public Boolean getSuccess() {
        return isSuccess;
    }

    public void setSuccess(Boolean success) {
        isSuccess = success;
    }

    public List<MSTAttachment> getData() {
        return data;
    }

    public void setData(List<MSTAttachment> data) {
        this.data = data;
    }
}
