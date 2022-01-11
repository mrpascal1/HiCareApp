package com.ab.hicarerun.network.models.attachmentmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetAttachmentResponse {
    @SerializedName("IsSuccess")
    @Expose
    private Boolean isSuccess;
    @SerializedName("Data")
    @Expose
    private List<GetAttachmentList> data = null;

    public Boolean getSuccess() {
        return isSuccess;
    }

    public void setSuccess(Boolean success) {
        isSuccess = success;
    }

    public List<GetAttachmentList> getData() {
        return data;
    }

    public void setData(List<GetAttachmentList> data) {
        this.data = data;
    }
}
