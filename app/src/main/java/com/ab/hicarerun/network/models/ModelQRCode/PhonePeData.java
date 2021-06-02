package com.ab.hicarerun.network.models.ModelQRCode;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Arjun Bhatt on 8/26/2020.
 */
public class PhonePeData {
    @SerializedName("TransactionId")
    @Expose
    private String transactionId;
    @SerializedName("Amount")
    @Expose
    private Integer amount;
    @SerializedName("OrderNo")
    @Expose
    private String orderNo;
    @SerializedName("PaymentStatus")
    @Expose
    private String paymentStatus;
    @SerializedName("UserMessage")
    @Expose
    private String UserMessage;
    @SerializedName("UserCode")
    @Expose
    private String UserCode;
    @SerializedName("TaskId")
    @Expose
    private String taskId;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getUserMessage() {
        return UserMessage;
    }

    public void setUserMessage(String userMessage) {
        UserMessage = userMessage;
    }

    public String getUserCode() {
        return UserCode;
    }

    public void setUserCode(String userCode) {
        UserCode = userCode;
    }
}
