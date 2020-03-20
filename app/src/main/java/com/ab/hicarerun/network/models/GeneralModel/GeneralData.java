package com.ab.hicarerun.network.models.GeneralModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class GeneralData implements RealmModel {
    @SerializedName("SchedulingStatus")
    @Expose
    private String SchedulingStatus;

    @SerializedName("PaymentMode")
    @Expose
    private String PaymentMode;

    @SerializedName("AmountToCollect")
    @Expose
    private String AmountToCollect;

    @SerializedName("IncompleteReason")
    @Expose
    private String IncompleteReason;

    @SerializedName("AmountCollected")
    @Expose
    private String AmountCollected;

    @SerializedName("MobileNumber")
    @Expose
    private String MobileNumber;

    @SerializedName("AlternateMobileNumber")
    @Expose
    private String AlternateMobileNumber;

    @SerializedName("CustomerName")
    @Expose
    private String CustName;

    @SerializedName("Email")
    @Expose
    private String Email;

    @SerializedName("Duration")
    @Expose
    private String Duration;
    @PrimaryKey
    @SerializedName("OrderNumber")
    @Expose
    private String OrderNumber;

    @SerializedName("NumberOfBhk")
    @Expose
    private String NumberOfBhk;

    @SerializedName("Technician_OTP")
    @Expose
    private String TechnicianOTP;

    @SerializedName("Sc_OTP")
    @Expose
    private String Sc_OTP;

    @SerializedName("Onsite_OTP")
    @Expose
    private String Onsite_OTP;

    @SerializedName("Customer_OTP")
    @Expose
    private String Customer_OTP;

    @SerializedName("PaymentOtp")
    @Expose
    private String PaymentOtp;

    @SerializedName("CustomerSign")
    @Expose
    private String Signatory;

    @SerializedName("SignatureUrl")
    @Expose
    private String SignatureUrl;

    @SerializedName("ServicePlan")
    @Expose
    private String ServicePlan;


    @SerializedName("ServiceType")
    @Expose
    private String ServiceType;

    @SerializedName("TaskAssignmentStartTime")
    @Expose
    private String TaskAssignmentStartTime;

    @SerializedName("IsFeedBack")
    @Expose
    private Boolean IsFeedBack;

    @SerializedName("IsPaymentValidation")
    @Expose
    private Boolean IsPaymentValidation;

    @SerializedName("IsJobCardRequired")
    @Expose
    private Boolean IsJobCardRequired;

    @SerializedName("AutoSubmitChemicals")
    @Expose
    private Boolean IsAutoSubmitChemicals;

    @SerializedName("IsChequeRequired")
    @Expose
    private Boolean IsChequeRequired;

    @SerializedName("TaskAssignmentEndTime")
    @Expose
    private String TaskAssignmentEndTime;

    @SerializedName("StandardPropertySize")
    @Expose
    private String StandardPropertySize;

    @SerializedName("ActualPropertySize")
    @Expose
    private String ActualPropertySize;

    @SerializedName("IsTechnicianFeedbackRequired")
    @Expose
    private Boolean IsTechnicianFeedbackRequired;

    @SerializedName("TechnicianRating")
    @Expose
    private String TechnicianRating;

    @SerializedName("IsIncentiveEnable")
    @Expose
    private Boolean IsIncentiveEnable;

    @SerializedName("IncentivePoint")
    @Expose
    private String IncentivePoint;
    @SerializedName("BankName")
    @Expose
    private String BankName;

    @SerializedName("ChequeNo")
    @Expose
    private String ChequeNo;

    @SerializedName("AccountType")
    @Expose
    private String AccountType;

    @SerializedName("ChequeDate")
    @Expose
    private String ChequeDate;

    @SerializedName("ChequeImageUrl")
    @Expose
    private String ChequeImageUrl;

    @SerializedName("ReferralDiscount")
    @Expose
    private String ReferralDiscount;

    @SerializedName("SchedulingStatusList")
    @Expose
    private RealmList<GeneralTaskStatus> SchedulingStatusModel = null;

    @SerializedName("PaymentModeList")
    @Expose

    private RealmList<GeneralPaymentMode> PaymentModeList = null;
    @SerializedName("IncompleteReasonList")
    @Expose
    private RealmList<IncompleteReason> IncompleteReasonList = null;

    @SerializedName("RelatedTasks")
    @Expose
    private RealmList<MSTTasks> MSTList = null;

    @SerializedName("ActualCompletionDateTime")
    @Expose
    private String ActualCompletionDateTime;
    @SerializedName("Restrict_Early_Completion")
    @Expose
    private Boolean Restrict_Early_Completion;

    @SerializedName("IsFlushOutRequired")
    @Expose
    private Boolean IsFlushOutRequired;

    @SerializedName("Payment_Otp_Required")
    @Expose
    private Boolean Payment_Otp_Required;

    @SerializedName("Payment_Jeopardy_Raised")
    @Expose
    private Boolean Payment_Jeopardy_Raised;

    @SerializedName("CustomerLatitude")
    @Expose
    private Double CustomerLatitude;

    @SerializedName("CustomerLongitude")
    @Expose
    private Double CustomerLongitude;
    @SerializedName("TechnicianMobileNo")
    @Expose
    private String TechnicianMobileNo;

    @SerializedName("Show_Standard_Chemicals")
    @Expose
    private Boolean Show_Standard_Chemicals;
    public GeneralData() {
        SchedulingStatus = "NA";
        PaymentMode = "NA";
        AmountToCollect = "NA";
        IncompleteReason = "NA";
        AmountCollected = "NA";
        MobileNumber = "NA";
        CustName = "NA";
        Email = "NA";
        Duration = "NA";
        OrderNumber = "NA";
        NumberOfBhk = "NA";
        TechnicianOTP = "NA";
        Sc_OTP = "NA";
        Onsite_OTP = "NA";
        Customer_OTP = "NA";
        AccountType = "NA";
        Signatory = "NA";
        SignatureUrl = "NA";
        ServicePlan = "NA";
        ServiceType = "NA";
        TaskAssignmentStartTime = "NA";
        IsFeedBack = false;
        IsPaymentValidation = false;
        IsJobCardRequired = false;
        IsAutoSubmitChemicals = false;
        IsChequeRequired = false;
        TaskAssignmentEndTime = "NA";
        StandardPropertySize = "NA";
        ActualPropertySize = "NA";
        IsTechnicianFeedbackRequired = false;
        TechnicianRating = "NA";
        IsIncentiveEnable = false;
        IncentivePoint = "NA";
        BankName = "NA";
        ChequeNo = "NA";
        ChequeDate = "NA";
        ChequeImageUrl = "NA";
        ActualCompletionDateTime = "NA";
        Restrict_Early_Completion = false;
        ReferralDiscount = "0";
        IsFlushOutRequired = false;
        Payment_Otp_Required = false;
        PaymentOtp = "";
        CustomerLatitude = 0.0;
        CustomerLongitude = 0.0;
        TechnicianMobileNo = "";
        Payment_Jeopardy_Raised = false;
        Show_Standard_Chemicals = false;
    }

    public String getOrderNumber() {
        return OrderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        OrderNumber = orderNumber;
    }

    public String getTaskAssignmentStartTime() {
        return TaskAssignmentStartTime;
    }

    public void setTaskAssignmentStartTime(String taskAssignmentStartTime) {
        TaskAssignmentStartTime = taskAssignmentStartTime;
    }

    public String getTaskAssignmentEndTime() {
        return TaskAssignmentEndTime;
    }

    public void setTaskAssignmentEndTime(String taskAssignmentEndTime) {
        TaskAssignmentEndTime = taskAssignmentEndTime;
    }

    public String getSchedulingStatus() {
        return SchedulingStatus;
    }

    public void setSchedulingStatus(String schedulingStatus) {
        SchedulingStatus = schedulingStatus;
    }

    public String getPaymentMode() {
        return PaymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        PaymentMode = paymentMode;
    }

    public String getAmountToCollect() {
        return AmountToCollect;
    }

    public void setAmountToCollect(String amountToCollect) {
        AmountToCollect = amountToCollect;
    }

    public String getAmountCollected() {
        return AmountCollected;
    }

    public void setAmountCollected(String amountCollected) {
        AmountCollected = amountCollected;
    }

    public String getMobileNumber() {
        return MobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        MobileNumber = mobileNumber;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getDuration() {
        return Duration;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }

    public RealmList<GeneralTaskStatus> getSchedulingStatusModel() {
        return SchedulingStatusModel;
    }

    public void setSchedulingStatusModel(RealmList<GeneralTaskStatus> schedulingStatusModel) {
        SchedulingStatusModel = schedulingStatusModel;
    }

    public String getServicePlan() {
        return ServicePlan;
    }

    public void setServicePlan(String servicePlan) {
        ServicePlan = servicePlan;
    }

    public String getServiceType() {
        return ServiceType;
    }

    public void setServiceType(String serviceType) {
        ServiceType = serviceType;
    }

    public String getNumberOfBhk() {
        return NumberOfBhk;
    }

    public void setNumberOfBhk(String numberOfBhk) {
        NumberOfBhk = numberOfBhk;
    }

    public String getTechnicianOTP() {
        return TechnicianOTP;
    }

    public void setTechnicianOTP(String technicianOTP) {
        TechnicianOTP = technicianOTP;
    }

    public String getSignatureUrl() {
        return SignatureUrl;
    }

    public void setSignatureUrl(String signatureUrl) {
        SignatureUrl = signatureUrl;
    }

    public RealmList<GeneralPaymentMode> getPaymentModeList() {
        return PaymentModeList;
    }

    public void setPaymentModeList(RealmList<GeneralPaymentMode> paymentModeList) {
        PaymentModeList = paymentModeList;
    }

    public String getSignatory() {
        return Signatory;
    }

    public void setSignatory(String signatory) {
        Signatory = signatory;
    }

    public Boolean getFeedBack() {
        return IsFeedBack;
    }

    public void setFeedBack(Boolean feedBack) {
        IsFeedBack = feedBack;
    }

    public Boolean getPaymentValidation() {
        return IsPaymentValidation;
    }

    public void setPaymentValidation(Boolean paymentValidation) {
        IsPaymentValidation = paymentValidation;
    }

    public Boolean getJobCardRequired() {
        return IsJobCardRequired;
    }

    public void setJobCardRequired(Boolean jobCardRequired) {
        IsJobCardRequired = jobCardRequired;
    }

    public Boolean getAutoSubmitChemicals() {
        return IsAutoSubmitChemicals;
    }

    public void setAutoSubmitChemicals(Boolean autoSubmitChemicals) {
        IsAutoSubmitChemicals = autoSubmitChemicals;
    }

    public Boolean getChequeRequired() {
        return IsChequeRequired;
    }

    public void setChequeRequired(Boolean chequeRequired) {
        IsChequeRequired = chequeRequired;
    }

    public String getCustName() {
        return CustName;
    }

    public void setCustName(String custName) {
        CustName = custName;
    }

    public String getStandardPropertySize() {
        return StandardPropertySize;
    }

    public void setStandardPropertySize(String standardPropertySize) {
        StandardPropertySize = standardPropertySize;
    }

    public String getActualPropertySize() {
        return ActualPropertySize;
    }

    public void setActualPropertySize(String actualPropertySize) {
        ActualPropertySize = actualPropertySize;
    }

    public String getSc_OTP() {
        return Sc_OTP;
    }

    public void setSc_OTP(String sc_OTP) {
        Sc_OTP = sc_OTP;
    }

    public String getCustomer_OTP() {
        return Customer_OTP;
    }

    public void setCustomer_OTP(String customer_OTP) {
        Customer_OTP = customer_OTP;
    }

    public RealmList<IncompleteReason> getIncompleteReasonList() {
        return IncompleteReasonList;
    }

    public void setIncompleteReasonList(RealmList<IncompleteReason> incompleteReasonList) {
        IncompleteReasonList = incompleteReasonList;
    }

    public Boolean getTechnicianFeedbackRequired() {
        return IsTechnicianFeedbackRequired;
    }

    public void setTechnicianFeedbackRequired(Boolean technicianFeedbackRequired) {
        IsTechnicianFeedbackRequired = technicianFeedbackRequired;
    }

    public String getTechnicianRating() {
        return TechnicianRating;
    }

    public void setTechnicianRating(String technicianRating) {
        TechnicianRating = technicianRating;
    }

    public Boolean getIncentiveEnable() {
        return IsIncentiveEnable;
    }

    public void setIncentiveEnable(Boolean incentiveEnable) {
        IsIncentiveEnable = incentiveEnable;
    }

    public String getIncentivePoint() {
        return IncentivePoint;
    }

    public void setIncentivePoint(String incentivePoint) {
        IncentivePoint = incentivePoint;
    }

    public String getChequeImageUrl() {
        return ChequeImageUrl;
    }

    public void setChequeImageUrl(String chequeImageUrl) {
        ChequeImageUrl = chequeImageUrl;
    }

    public String getIncompleteReason() {
        return IncompleteReason;
    }

    public void setIncompleteReason(String incompleteReason) {
        IncompleteReason = incompleteReason;
    }

    public String getBankName() {
        return BankName;
    }

    public void setBankName(String bankName) {
        BankName = bankName;
    }

    public String getChequeNo() {
        return ChequeNo;
    }

    public void setChequeNo(String chequeNo) {
        ChequeNo = chequeNo;
    }

    public String getChequeDate() {
        return ChequeDate;
    }

    public void setChequeDate(String chequeDate) {
        ChequeDate = chequeDate;
    }

    public String getOnsite_OTP() {
        return Onsite_OTP;
    }

    public void setOnsite_OTP(String onsite_OTP) {
        Onsite_OTP = onsite_OTP;
    }

    public String getActualCompletionDateTime() {
        return ActualCompletionDateTime;
    }

    public void setActualCompletionDateTime(String actualCompletionDateTime) {
        ActualCompletionDateTime = actualCompletionDateTime;
    }

    public Boolean getRestrict_Early_Completion() {
        return Restrict_Early_Completion;
    }

    public void setRestrict_Early_Completion(Boolean restrict_Early_Completion) {
        Restrict_Early_Completion = restrict_Early_Completion;
    }

    public String getAccountType() {
        return AccountType;
    }

    public void setAccountType(String accountType) {
        AccountType = accountType;
    }

    public String getAlternateMobileNumber() {
        return AlternateMobileNumber;
    }

    public void setAlternateMobileNumber(String alternateMobileNumber) {
        AlternateMobileNumber = alternateMobileNumber;
    }

    public RealmList<MSTTasks> getMSTList() {
        return MSTList;
    }

    public void setMSTList(RealmList<MSTTasks> MSTList) {
        this.MSTList = MSTList;
    }

    public String getReferralDiscount() {
        return ReferralDiscount;
    }

    public void setReferralDiscount(String referralDiscount) {
        ReferralDiscount = referralDiscount;
    }

    public Boolean getFlushOutRequired() {
        return IsFlushOutRequired;
    }

    public void setFlushOutRequired(Boolean flushOutRequired) {
        IsFlushOutRequired = flushOutRequired;
    }

    public Boolean getPayment_Otp_Required() {
        return Payment_Otp_Required;
    }

    public void setPayment_Otp_Required(Boolean payment_Otp_Required) {
        Payment_Otp_Required = payment_Otp_Required;
    }

    public String getPaymentOtp() {
        return PaymentOtp;
    }

    public void setPaymentOtp(String paymentOtp) {
        PaymentOtp = paymentOtp;
    }

    public Double getCustomerLatitude() {
        return CustomerLatitude;
    }

    public void setCustomerLatitude(Double customerLatitude) {
        CustomerLatitude = customerLatitude;
    }

    public Double getCustomerLongitude() {
        return CustomerLongitude;
    }

    public void setCustomerLongitude(Double customerLongitude) {
        CustomerLongitude = customerLongitude;
    }

    public String getTechnicianMobileNo() {
        return TechnicianMobileNo;
    }

    public void setTechnicianMobileNo(String technicianMobileNo) {
        TechnicianMobileNo = technicianMobileNo;
    }

    public Boolean getPayment_Jeopardy_Raised() {
        return Payment_Jeopardy_Raised;
    }

    public void setPayment_Jeopardy_Raised(Boolean payment_Jeopardy_Raised) {
        Payment_Jeopardy_Raised = payment_Jeopardy_Raised;
    }

    public Boolean getShow_Standard_Chemicals() {
        return Show_Standard_Chemicals;
    }

    public void setShow_Standard_Chemicals(Boolean show_Standard_Chemicals) {
        Show_Standard_Chemicals = show_Standard_Chemicals;
    }
}
