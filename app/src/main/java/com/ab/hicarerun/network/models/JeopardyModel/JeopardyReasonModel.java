package com.ab.hicarerun.network.models.JeopardyModel;

import com.ab.hicarerun.network.models.AttachmentModel.GetAttachmentList;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class JeopardyReasonModel {
    @SerializedName("IsSuccess") @Expose
    private Boolean IsSuccess;

    @SerializedName("com.ab.hicarerun.network.models.TSScannerModel.Data") @Expose
    private List<JeopardyReasonsList> data = null;

    @SerializedName("ResponseMessage") @Expose
    private String ResponseMessage;

    public Boolean getSuccess() {
        return IsSuccess;
    }

    public void setSuccess(Boolean success) {
        IsSuccess = success;
    }

    public List<JeopardyReasonsList> getData() {
        return data;
    }

    public void setData(List<JeopardyReasonsList> data) {
        this.data = data;
    }

    public String getResponseMessage() {
        return ResponseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        ResponseMessage = responseMessage;
    }
}
