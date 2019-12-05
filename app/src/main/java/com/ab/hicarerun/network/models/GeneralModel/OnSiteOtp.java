package com.ab.hicarerun.network.models.GeneralModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Arjun Bhatt on 11/19/2019.
 */
public class OnSiteOtp {
    @SerializedName("ResourceId")
    @Expose
    private Object resourceId;
    @SerializedName("TaskId")
    @Expose
    private Object taskId;
    @SerializedName("CustName")
    @Expose
    private Object custName;
    @SerializedName("SchedulingStatus")
    @Expose
    private String schedulingStatus;
    @SerializedName("ScheduledDateTime")
    @Expose
    private String scheduledDateTime;
    @SerializedName("IncompleteReason")
    @Expose
    private Object incompleteReason;
    @SerializedName("PaymentMode")
    @Expose
    private Object paymentMode;
    @SerializedName("PaymentOtp")
    @Expose
    private Object paymentOtp;
    @SerializedName("AmountToCollect")
    @Expose
    private String amountToCollect;
    @SerializedName("AmountCollected")
    @Expose
    private Object amountCollected;
    @SerializedName("BankName")
    @Expose
    private Object bankName;
    @SerializedName("ChequeNo")
    @Expose
    private Object chequeNo;
    @SerializedName("ChequeDate")
    @Expose
    private Object chequeDate;
    @SerializedName("MobileNumber")
    @Expose
    private String mobileNumber;
    @SerializedName("CustomerName")
    @Expose
    private String customerName;
    @SerializedName("MobileNo")
    @Expose
    private Object mobileNo;
    @SerializedName("AlternateMobileNo")
    @Expose
    private Object alternateMobileNo;
    @SerializedName("InterestedService")
    @Expose
    private Object interestedService;
    @SerializedName("ActualPropertySize")
    @Expose
    private Object actualPropertySize;
    @SerializedName("StandardPropertySize")
    @Expose
    private Object standardPropertySize;
    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("NumberOfBhk")
    @Expose
    private Integer numberOfBhk;
    @SerializedName("FeedbackCode")
    @Expose
    private Integer feedbackCode;
    @SerializedName("CustomerSign")
    @Expose
    private Object customerSign;
    @SerializedName("Comment_Val")
    @Expose
    private Object commentVal;
    @SerializedName("SchedulingStatusList")
    @Expose
    private Object schedulingStatusList;
    @SerializedName("Duration")
    @Expose
    private String duration;
    @SerializedName("DurationInSeconds")
    @Expose
    private Double durationInSeconds;
    @SerializedName("ServicePlan")
    @Expose
    private String servicePlan;
    @SerializedName("ServiceType")
    @Expose
    private String serviceType;
    @SerializedName("TypeName")
    @Expose
    private String typeName;
    @SerializedName("PaymentModeList")
    @Expose
    private Object paymentModeList;
    @SerializedName("SignatureUrl")
    @Expose
    private Object signatureUrl;
    @SerializedName("IsFeedBack")
    @Expose
    private Boolean isFeedBack;
    @SerializedName("IsJobCardRequired")
    @Expose
    private Boolean isJobCardRequired;
    @SerializedName("IsPaymentValidation")
    @Expose
    private Boolean isPaymentValidation;
    @SerializedName("Sc_OTP")
    @Expose
    private String scOTP;
    @SerializedName("Technician_OTP")
    @Expose
    private Object technicianOTP;
    @SerializedName("Customer_OTP")
    @Expose
    private String customerOTP;
    @SerializedName("Onsite_OTP")
    @Expose
    private String onsiteOTP;
    @SerializedName("AutoSubmitChemicals")
    @Expose
    private Boolean autoSubmitChemicals;
    @SerializedName("IsChequeRequired")
    @Expose
    private Boolean isChequeRequired;
    @SerializedName("ChequeImageUrl")
    @Expose
    private Object chequeImageUrl;
    @SerializedName("IsTechnicianFeedbackRequired")
    @Expose
    private Boolean isTechnicianFeedbackRequired;
    @SerializedName("TechnicianRating")
    @Expose
    private Integer technicianRating;
    @SerializedName("IsIncentiveEnable")
    @Expose
    private Boolean isIncentiveEnable;
    @SerializedName("IncentivePoint")
    @Expose
    private Integer incentivePoint;
    @SerializedName("IncompleteReasonList")
    @Expose
    private Object incompleteReasonList;
    @SerializedName("ActualCompletionDateTime")
    @Expose
    private Object actualCompletionDateTime;
    @SerializedName("Restrict_Early_Completion")
    @Expose
    private Boolean restrictEarlyCompletion;
    @SerializedName("IsMegaTask")
    @Expose
    private Boolean isMegaTask;
    @SerializedName("OrderNumber")
    @Expose
    private String orderNumber;
    @SerializedName("TaskAssignmentStartDate")
    @Expose
    private String taskAssignmentStartDate;
    @SerializedName("TaskAssignmentStartTime")
    @Expose
    private String taskAssignmentStartTime;
    @SerializedName("TaskAssignmentEndDate")
    @Expose
    private String taskAssignmentEndDate;
    @SerializedName("TaskAssignmentEndTime")
    @Expose
    private String taskAssignmentEndTime;
    @SerializedName("AssignmentStartDate")
    @Expose
    private String assignmentStartDate;
    @SerializedName("AssignmentEndDate")
    @Expose
    private String assignmentEndDate;
    @SerializedName("AccountId")
    @Expose
    private String accountId;
    @SerializedName("AccountType")
    @Expose
    private String accountType;
    @SerializedName("AccountLat")
    @Expose
    private String accountLat;
    @SerializedName("AccountLong")
    @Expose
    private String accountLong;
    @SerializedName("OnsiteLat")
    @Expose
    private Object onsiteLat;
    @SerializedName("OnsiteLong")
    @Expose
    private Object onsiteLong;
    @SerializedName("OnsiteDateTime")
    @Expose
    private Object onsiteDateTime;
    @SerializedName("CompletedLat")
    @Expose
    private Object completedLat;
    @SerializedName("CompletedLong")
    @Expose
    private Object completedLong;
    @SerializedName("CompletedDateTime")
    @Expose
    private Object completedDateTime;
    @SerializedName("PlayerId")
    @Expose
    private Object playerId;
    @SerializedName("TaskAppointmentStartTime")
    @Expose
    private Object taskAppointmentStartTime;
    @SerializedName("TaskAppointmentEndTime")
    @Expose
    private Object taskAppointmentEndTime;

    public Object getResourceId() {
        return resourceId;
    }

    public void setResourceId(Object resourceId) {
        this.resourceId = resourceId;
    }

    public Object getTaskId() {
        return taskId;
    }

    public void setTaskId(Object taskId) {
        this.taskId = taskId;
    }

    public Object getCustName() {
        return custName;
    }

    public void setCustName(Object custName) {
        this.custName = custName;
    }

    public String getSchedulingStatus() {
        return schedulingStatus;
    }

    public void setSchedulingStatus(String schedulingStatus) {
        this.schedulingStatus = schedulingStatus;
    }

    public String getScheduledDateTime() {
        return scheduledDateTime;
    }

    public void setScheduledDateTime(String scheduledDateTime) {
        this.scheduledDateTime = scheduledDateTime;
    }

    public Object getIncompleteReason() {
        return incompleteReason;
    }

    public void setIncompleteReason(Object incompleteReason) {
        this.incompleteReason = incompleteReason;
    }

    public Object getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(Object paymentMode) {
        this.paymentMode = paymentMode;
    }

    public Object getPaymentOtp() {
        return paymentOtp;
    }

    public void setPaymentOtp(Object paymentOtp) {
        this.paymentOtp = paymentOtp;
    }

    public String getAmountToCollect() {
        return amountToCollect;
    }

    public void setAmountToCollect(String amountToCollect) {
        this.amountToCollect = amountToCollect;
    }

    public Object getAmountCollected() {
        return amountCollected;
    }

    public void setAmountCollected(Object amountCollected) {
        this.amountCollected = amountCollected;
    }

    public Object getBankName() {
        return bankName;
    }

    public void setBankName(Object bankName) {
        this.bankName = bankName;
    }

    public Object getChequeNo() {
        return chequeNo;
    }

    public void setChequeNo(Object chequeNo) {
        this.chequeNo = chequeNo;
    }

    public Object getChequeDate() {
        return chequeDate;
    }

    public void setChequeDate(Object chequeDate) {
        this.chequeDate = chequeDate;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Object getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(Object mobileNo) {
        this.mobileNo = mobileNo;
    }

    public Object getAlternateMobileNo() {
        return alternateMobileNo;
    }

    public void setAlternateMobileNo(Object alternateMobileNo) {
        this.alternateMobileNo = alternateMobileNo;
    }

    public Object getInterestedService() {
        return interestedService;
    }

    public void setInterestedService(Object interestedService) {
        this.interestedService = interestedService;
    }

    public Object getActualPropertySize() {
        return actualPropertySize;
    }

    public void setActualPropertySize(Object actualPropertySize) {
        this.actualPropertySize = actualPropertySize;
    }

    public Object getStandardPropertySize() {
        return standardPropertySize;
    }

    public void setStandardPropertySize(Object standardPropertySize) {
        this.standardPropertySize = standardPropertySize;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getNumberOfBhk() {
        return numberOfBhk;
    }

    public void setNumberOfBhk(Integer numberOfBhk) {
        this.numberOfBhk = numberOfBhk;
    }

    public Integer getFeedbackCode() {
        return feedbackCode;
    }

    public void setFeedbackCode(Integer feedbackCode) {
        this.feedbackCode = feedbackCode;
    }

    public Object getCustomerSign() {
        return customerSign;
    }

    public void setCustomerSign(Object customerSign) {
        this.customerSign = customerSign;
    }

    public Object getCommentVal() {
        return commentVal;
    }

    public void setCommentVal(Object commentVal) {
        this.commentVal = commentVal;
    }

    public Object getSchedulingStatusList() {
        return schedulingStatusList;
    }

    public void setSchedulingStatusList(Object schedulingStatusList) {
        this.schedulingStatusList = schedulingStatusList;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Double getDurationInSeconds() {
        return durationInSeconds;
    }

    public void setDurationInSeconds(Double durationInSeconds) {
        this.durationInSeconds = durationInSeconds;
    }

    public String getServicePlan() {
        return servicePlan;
    }

    public void setServicePlan(String servicePlan) {
        this.servicePlan = servicePlan;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Object getPaymentModeList() {
        return paymentModeList;
    }

    public void setPaymentModeList(Object paymentModeList) {
        this.paymentModeList = paymentModeList;
    }

    public Object getSignatureUrl() {
        return signatureUrl;
    }

    public void setSignatureUrl(Object signatureUrl) {
        this.signatureUrl = signatureUrl;
    }

    public Boolean getIsFeedBack() {
        return isFeedBack;
    }

    public void setIsFeedBack(Boolean isFeedBack) {
        this.isFeedBack = isFeedBack;
    }

    public Boolean getIsJobCardRequired() {
        return isJobCardRequired;
    }

    public void setIsJobCardRequired(Boolean isJobCardRequired) {
        this.isJobCardRequired = isJobCardRequired;
    }

    public Boolean getIsPaymentValidation() {
        return isPaymentValidation;
    }

    public void setIsPaymentValidation(Boolean isPaymentValidation) {
        this.isPaymentValidation = isPaymentValidation;
    }

    public String getScOTP() {
        return scOTP;
    }

    public void setScOTP(String scOTP) {
        this.scOTP = scOTP;
    }

    public Object getTechnicianOTP() {
        return technicianOTP;
    }

    public void setTechnicianOTP(Object technicianOTP) {
        this.technicianOTP = technicianOTP;
    }

    public String getCustomerOTP() {
        return customerOTP;
    }

    public void setCustomerOTP(String customerOTP) {
        this.customerOTP = customerOTP;
    }

    public String getOnsiteOTP() {
        return onsiteOTP;
    }

    public void setOnsiteOTP(String onsiteOTP) {
        this.onsiteOTP = onsiteOTP;
    }

    public Boolean getAutoSubmitChemicals() {
        return autoSubmitChemicals;
    }

    public void setAutoSubmitChemicals(Boolean autoSubmitChemicals) {
        this.autoSubmitChemicals = autoSubmitChemicals;
    }

    public Boolean getIsChequeRequired() {
        return isChequeRequired;
    }

    public void setIsChequeRequired(Boolean isChequeRequired) {
        this.isChequeRequired = isChequeRequired;
    }

    public Object getChequeImageUrl() {
        return chequeImageUrl;
    }

    public void setChequeImageUrl(Object chequeImageUrl) {
        this.chequeImageUrl = chequeImageUrl;
    }

    public Boolean getIsTechnicianFeedbackRequired() {
        return isTechnicianFeedbackRequired;
    }

    public void setIsTechnicianFeedbackRequired(Boolean isTechnicianFeedbackRequired) {
        this.isTechnicianFeedbackRequired = isTechnicianFeedbackRequired;
    }

    public Integer getTechnicianRating() {
        return technicianRating;
    }

    public void setTechnicianRating(Integer technicianRating) {
        this.technicianRating = technicianRating;
    }

    public Boolean getIsIncentiveEnable() {
        return isIncentiveEnable;
    }

    public void setIsIncentiveEnable(Boolean isIncentiveEnable) {
        this.isIncentiveEnable = isIncentiveEnable;
    }

    public Integer getIncentivePoint() {
        return incentivePoint;
    }

    public void setIncentivePoint(Integer incentivePoint) {
        this.incentivePoint = incentivePoint;
    }

    public Object getIncompleteReasonList() {
        return incompleteReasonList;
    }

    public void setIncompleteReasonList(Object incompleteReasonList) {
        this.incompleteReasonList = incompleteReasonList;
    }

    public Object getActualCompletionDateTime() {
        return actualCompletionDateTime;
    }

    public void setActualCompletionDateTime(Object actualCompletionDateTime) {
        this.actualCompletionDateTime = actualCompletionDateTime;
    }

    public Boolean getRestrictEarlyCompletion() {
        return restrictEarlyCompletion;
    }

    public void setRestrictEarlyCompletion(Boolean restrictEarlyCompletion) {
        this.restrictEarlyCompletion = restrictEarlyCompletion;
    }

    public Boolean getIsMegaTask() {
        return isMegaTask;
    }

    public void setIsMegaTask(Boolean isMegaTask) {
        this.isMegaTask = isMegaTask;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getTaskAssignmentStartDate() {
        return taskAssignmentStartDate;
    }

    public void setTaskAssignmentStartDate(String taskAssignmentStartDate) {
        this.taskAssignmentStartDate = taskAssignmentStartDate;
    }

    public String getTaskAssignmentStartTime() {
        return taskAssignmentStartTime;
    }

    public void setTaskAssignmentStartTime(String taskAssignmentStartTime) {
        this.taskAssignmentStartTime = taskAssignmentStartTime;
    }

    public String getTaskAssignmentEndDate() {
        return taskAssignmentEndDate;
    }

    public void setTaskAssignmentEndDate(String taskAssignmentEndDate) {
        this.taskAssignmentEndDate = taskAssignmentEndDate;
    }

    public String getTaskAssignmentEndTime() {
        return taskAssignmentEndTime;
    }

    public void setTaskAssignmentEndTime(String taskAssignmentEndTime) {
        this.taskAssignmentEndTime = taskAssignmentEndTime;
    }

    public String getAssignmentStartDate() {
        return assignmentStartDate;
    }

    public void setAssignmentStartDate(String assignmentStartDate) {
        this.assignmentStartDate = assignmentStartDate;
    }

    public String getAssignmentEndDate() {
        return assignmentEndDate;
    }

    public void setAssignmentEndDate(String assignmentEndDate) {
        this.assignmentEndDate = assignmentEndDate;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountLat() {
        return accountLat;
    }

    public void setAccountLat(String accountLat) {
        this.accountLat = accountLat;
    }

    public String getAccountLong() {
        return accountLong;
    }

    public void setAccountLong(String accountLong) {
        this.accountLong = accountLong;
    }

    public Object getOnsiteLat() {
        return onsiteLat;
    }

    public void setOnsiteLat(Object onsiteLat) {
        this.onsiteLat = onsiteLat;
    }

    public Object getOnsiteLong() {
        return onsiteLong;
    }

    public void setOnsiteLong(Object onsiteLong) {
        this.onsiteLong = onsiteLong;
    }

    public Object getOnsiteDateTime() {
        return onsiteDateTime;
    }

    public void setOnsiteDateTime(Object onsiteDateTime) {
        this.onsiteDateTime = onsiteDateTime;
    }

    public Object getCompletedLat() {
        return completedLat;
    }

    public void setCompletedLat(Object completedLat) {
        this.completedLat = completedLat;
    }

    public Object getCompletedLong() {
        return completedLong;
    }

    public void setCompletedLong(Object completedLong) {
        this.completedLong = completedLong;
    }

    public Object getCompletedDateTime() {
        return completedDateTime;
    }

    public void setCompletedDateTime(Object completedDateTime) {
        this.completedDateTime = completedDateTime;
    }

    public Object getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Object playerId) {
        this.playerId = playerId;
    }

    public Object getTaskAppointmentStartTime() {
        return taskAppointmentStartTime;
    }

    public void setTaskAppointmentStartTime(Object taskAppointmentStartTime) {
        this.taskAppointmentStartTime = taskAppointmentStartTime;
    }

    public Object getTaskAppointmentEndTime() {
        return taskAppointmentEndTime;
    }

    public void setTaskAppointmentEndTime(Object taskAppointmentEndTime) {
        this.taskAppointmentEndTime = taskAppointmentEndTime;
    }
}
