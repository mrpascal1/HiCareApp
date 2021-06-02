package com.ab.hicarerun.network.models.ModelQRCode;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Arjun Bhatt on 8/26/2020.
 */
public class PhonePeQRCode {
    @SerializedName("TransactionId")
    @Expose
    private String transactionId;
    @SerializedName("OrderNo")
    @Expose
    private String orderNo;
    @SerializedName("TaskId")
    @Expose
    private String taskId;
    @SerializedName("QRImageUrl")
    @Expose
    private String qRImageUrl;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getQRImageUrl() {
        return qRImageUrl;
    }

    public void setQRImageUrl(String qRImageUrl) {
        this.qRImageUrl = qRImageUrl;
    }
}
