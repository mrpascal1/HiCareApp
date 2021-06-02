package com.ab.hicarerun.network.models.ServicePlanModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Arjun Bhatt on 5/30/2020.
 */
public class RenewOrderRequest {

    @SerializedName("AccountId")
    @Expose
    private String accountId;
    @SerializedName("OrderNo")
    @Expose
    private String orderNo;
    @SerializedName("TaskId")
    @Expose
    private String taskId;
    @SerializedName("Comment")
    @Expose
    private String comment;
    @SerializedName("DiscountOffered")
    @Expose
    private Double discountOffered;
    @SerializedName("OrderValue")
    @Expose
    private Double orderValue;
    @SerializedName("StandardValue")
    @Expose
    private Double standardValue;
    @SerializedName("StartDate")
    @Expose
    private String startDate;
    @SerializedName("PaymentType")
    @Expose
    private String paymentType;
    @SerializedName("OrderGeneratedFrom")
    @Expose
    private String orderGeneratedFrom;
    @SerializedName("AppointmentStartDate")
    @Expose
    private String appointmentStartDate;
    @SerializedName("AppointmentEndDate")
    @Expose
    private String appointmentEndDate;
    @SerializedName("SPCode")
    @Expose
    private String sPCode;
    @SerializedName("OrderSource")
    @Expose
    private String orderSource;
    @SerializedName("OrderType")
    @Expose
    private String orderType;
    @SerializedName("PaymentMode")
    @Expose
    private String paymentMode;
    @SerializedName("BankName")
    @Expose
    private String bankName;
    @SerializedName("ChequeNumber")
    @Expose
    private String chequeNumber;
    @SerializedName("ChequeDate")
    @Expose
    private String chequeDate;
    @SerializedName("CollectedByID")
    @Expose
    private String collectedByID;
    @SerializedName("CollectedByName")
    @Expose
    private String collectedByName;
    @SerializedName("RenewalOtp")
    @Expose
    private String RenewalOtp;
    @SerializedName("TransactionId")
    @Expose
    private String TransactionId;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Double getDiscountOffered() {
        return discountOffered;
    }

    public void setDiscountOffered(Double discountOffered) {
        this.discountOffered = discountOffered;
    }

    public Double getOrderValue() {
        return orderValue;
    }

    public void setOrderValue(Double orderValue) {
        this.orderValue = orderValue;
    }

    public Double getStandardValue() {
        return standardValue;
    }

    public void setStandardValue(Double standardValue) {
        this.standardValue = standardValue;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getOrderGeneratedFrom() {
        return orderGeneratedFrom;
    }

    public void setOrderGeneratedFrom(String orderGeneratedFrom) {
        this.orderGeneratedFrom = orderGeneratedFrom;
    }

    public String getAppointmentStartDate() {
        return appointmentStartDate;
    }

    public void setAppointmentStartDate(String appointmentStartDate) {
        this.appointmentStartDate = appointmentStartDate;
    }

    public String getAppointmentEndDate() {
        return appointmentEndDate;
    }

    public void setAppointmentEndDate(String appointmentEndDate) {
        this.appointmentEndDate = appointmentEndDate;
    }

    public String getSPCode() {
        return sPCode;
    }

    public void setSPCode(String sPCode) {
        this.sPCode = sPCode;
    }

    public String getOrderSource() {
        return orderSource;
    }

    public void setOrderSource(String orderSource) {
        this.orderSource = orderSource;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getChequeNumber() {
        return chequeNumber;
    }

    public void setChequeNumber(String chequeNumber) {
        this.chequeNumber = chequeNumber;
    }

    public String getChequeDate() {
        return chequeDate;
    }

    public void setChequeDate(String chequeDate) {
        this.chequeDate = chequeDate;
    }

    public String getCollectedByID() {
        return collectedByID;
    }

    public void setCollectedByID(String collectedByID) {
        this.collectedByID = collectedByID;
    }

    public String getCollectedByName() {
        return collectedByName;
    }

    public void setCollectedByName(String collectedByName) {
        this.collectedByName = collectedByName;
    }

    public String getRenewalOtp() {
        return RenewalOtp;
    }

    public void setRenewalOtp(String renewalOtp) {
        RenewalOtp = renewalOtp;
    }

    public String getTransactionId() {
        return TransactionId;
    }

    public void setTransactionId(String transactionId) {
        TransactionId = transactionId;
    }
}
