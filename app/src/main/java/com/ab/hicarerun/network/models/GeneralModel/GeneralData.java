package com.ab.hicarerun.network.models.GeneralModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class GeneralData implements RealmModel {
    @SerializedName("ResourceId")
    @Expose
    private String ResourceId;
    @SerializedName("TaskId")
    @Expose
    private String TaskId;
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

    @SerializedName("Service_Sequence_Number")
    @Expose
    private String Service_Sequence_Number;

    @SerializedName("ServiceActivityRequired")
    @Expose
    private boolean ServiceActivityRequired;

    @SerializedName("AmountCollected")
    @Expose
    private String AmountCollected;

    @SerializedName("UpiTransactionId")
    @Expose
    private String UpiTransactionId;

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

    @SerializedName("No_Renewal_Reason")
    @Expose
    private String No_Renewal_Reason;

    @PrimaryKey
    @SerializedName("OrderNumber")
    @Expose
    private String OrderNumber;
    @SerializedName("TypeName")
    @Expose
    private String TaskTypeName;
    @SerializedName("AccountId")
    @Expose
    private String AccountId;

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

    @SerializedName("Tag")
    @Expose
    private String Tag;

    @SerializedName("SignatureUrl")
    @Expose
    private String SignatureUrl;

    @SerializedName("ServicePlan")
    @Expose
    private String ServicePlan;

    @SerializedName("ShowBarcode")
    @Expose
    private Boolean ShowBarcode;

    @SerializedName("RefferalQuestion")
    @Expose
    private String RefferalQuestion;

    @SerializedName("IsCustomerInterestedToGiveRefferals")
    @Expose
    private Boolean IsCustomerInterestedToGiveRefferals;

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

    @SerializedName("FlushoutRequired")
    @Expose
    private Boolean FlushoutRequired;

    @SerializedName("GelTreatmentRequired")
    @Expose
    private Boolean GelTreatmentRequired;

    @SerializedName("TaskAssignmentEndTime")
    @Expose
    private String TaskAssignmentEndTime;

    @SerializedName("InspectionInfestationLevel")
    @Expose
    private String InspectionInfestationLevel;

    @SerializedName("ConsultationInfestationLevel")
    @Expose
    private String ConsultationInfestationLevel;

    @SerializedName("Flushout_Start_Date")
    @Expose
    private String Flushout_Start_Date;

    @SerializedName("Flushout_End_Date")
    @Expose
    private String Flushout_End_Date;

    @SerializedName("GelTreatment_Start_Date")
    @Expose
    private String GelTreatment_Start_Date;

    @SerializedName("GelTreatment_End_Date")
    @Expose
    private String GelTreatment_End_Date;

    @SerializedName("ShowSignature")
    @Expose
    private Boolean showSignature;

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

    @SerializedName("Onsite_Image_Required")
    @Expose
    private Boolean Onsite_Image_Required;

    @SerializedName("IsConsultationInspectionRequired")
    @Expose
    private Boolean IsConsultationInspectionRequired;

    @SerializedName("IsConsultationInspectionDone")
    @Expose
    private Boolean IsConsultationInspectionDone;

    @SerializedName("Onsite_Image_Path")
    @Expose
    private String Onsite_Image_Path;

    @SerializedName("Customer_Instructions")
    @Expose
    private String Customer_Instructions;

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

    @SerializedName("PostJob_Checklist_Done")
    @Expose
    private Boolean PostJob_Checklist_Done;

    @SerializedName("ActualAmountToCollect")
    @Expose
    private String ActualAmountToCollect;

    @SerializedName("TaskCheckList")
    @Expose
    private RealmList<TaskCheckList> TaskCheckList = null;

    @SerializedName("Renewal_Type")
    @Expose
    private String Renewal_Type;

    @SerializedName("Renewal_Order_No")
    @Expose
    private String Renewal_Order_No;

    @SerializedName("ShowNextServiceAppointment")
    @Expose
    private Boolean ShowNextServiceAppointment;

    @SerializedName("Next_SR_Service_Date")
    @Expose
    private String Next_SR_Service_Date;

    @SerializedName("Next_SR_Service_Start_Time")
    @Expose
    private String Next_SR_Service_Start_Time;

    @SerializedName("Next_SR_Service_End_Time")
    @Expose
    private String Next_SR_Service_End_Time;

    @SerializedName("SR_Date")
    @Expose
    private String SR_Date;

    @SerializedName("Next_SR_Planned_Start_Date")
    @Expose
    private String Next_SR_Planned_Start_Date;

    @SerializedName("Next_SR_Planned_End_Date")
    @Expose
    private String Next_SR_Planned_End_Date;

    @SerializedName("TaskAssignmentStartDate")
    @Expose
    private String TaskAssignmentStartDate;

    @SerializedName("TaskAssignmentEndDate")
    @Expose
    private String TaskAssignmentEndDate;

    @SerializedName("CustomerRefferalAlert")
    @Expose
    private String CustomerRefferalAlert;

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
        ActualAmountToCollect = "NA";
        Onsite_Image_Required = false;
        ShowNextServiceAppointment = false;
        Next_SR_Service_Date = "NA";
        Next_SR_Service_Start_Time = "NA";
        Next_SR_Service_End_Time = "NA";

        SR_Date = "NA";
        Next_SR_Planned_Start_Date = "NA";
        Next_SR_Planned_End_Date = "NA";
        TaskAssignmentStartDate = "NA";
        TaskAssignmentEndDate = "NA";

        RefferalQuestion = "NA";
        IsCustomerInterestedToGiveRefferals = false;

        CustomerRefferalAlert = "NA";
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

    public Boolean getIsFlushOutRequired() {
        return IsFlushOutRequired;
    }

    public void setIsFlushOutRequired(Boolean flushOutRequired) {
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

    public String getActualAmountToCollect() {
        return ActualAmountToCollect;
    }

    public void setActualAmountToCollect(String actualAmountToCollect) {
        ActualAmountToCollect = actualAmountToCollect;
    }

    public RealmList<TaskCheckList> getTaskCheckList() {
        return TaskCheckList;
    }

    public void setTaskCheckList(RealmList<TaskCheckList> taskCheckList) {
        TaskCheckList = taskCheckList;
    }

    public Boolean getOnsite_Image_Required() {
        return Onsite_Image_Required;
    }

    public void setOnsite_Image_Required(Boolean onsite_Image_Required) {
        Onsite_Image_Required = onsite_Image_Required;
    }

    public String getTaskTypeName() {
        return TaskTypeName;
    }

    public void setTaskTypeName(String taskTypeName) {
        TaskTypeName = taskTypeName;
    }

    public String getOnsite_Image_Path() {
        return Onsite_Image_Path;
    }

    public void setOnsite_Image_Path(String onsite_Image_Path) {
        Onsite_Image_Path = onsite_Image_Path;
    }

    public Boolean getShowSignature() {
        return showSignature;
    }

    public void setShowSignature(Boolean showSignature) {
        this.showSignature = showSignature;
    }

    public String getRenewal_Type() {
        return Renewal_Type;
    }

    public void setRenewal_Type(String renewal_Type) {
        Renewal_Type = renewal_Type;
    }

    public String getRenewal_Order_No() {
        return Renewal_Order_No;
    }

    public void setRenewal_Order_No(String renewal_Order_No) {
        Renewal_Order_No = renewal_Order_No;
    }

    public String getAccountId() {
        return AccountId;
    }

    public void setAccountId(String accountId) {
        AccountId = accountId;
    }

    public Boolean getConsultationInspectionRequired() {
        return IsConsultationInspectionRequired;
    }

    public void setConsultationInspectionRequired(Boolean consultationInspectionRequired) {
        IsConsultationInspectionRequired = consultationInspectionRequired;
    }

    public Boolean getConsultationInspectionDone() {
        return IsConsultationInspectionDone;
    }

    public void setConsultationInspectionDone(Boolean consultationInspectionDone) {
        IsConsultationInspectionDone = consultationInspectionDone;
    }

    public String getResourceId() {
        return ResourceId;
    }

    public void setResourceId(String resourceId) {
        ResourceId = resourceId;
    }

    public String getTaskId() {
        return TaskId;
    }

    public void setTaskId(String taskId) {
        TaskId = taskId;
    }

    public String getUpiTransactionId() {
        return UpiTransactionId;
    }

    public void setUpiTransactionId(String upiTransactionId) {
        UpiTransactionId = upiTransactionId;
    }

    public Boolean getPostJob_Checklist_Done() {
        return PostJob_Checklist_Done;
    }

    public void setPostJob_Checklist_Done(Boolean postJob_Checklist_Done) {
        PostJob_Checklist_Done = postJob_Checklist_Done;
    }

    public Boolean getShowNextServiceAppointment() {
        return ShowNextServiceAppointment;
    }

    public void setShowNextServiceAppointment(Boolean showNextServiceAppointment) {
        ShowNextServiceAppointment = showNextServiceAppointment;
    }

    public String getNext_SR_Service_Date() {
        return Next_SR_Service_Date;
    }

    public void setNext_SR_Service_Date(String next_SR_Service_Date) {
        Next_SR_Service_Date = next_SR_Service_Date;
    }

    public String getNext_SR_Service_Start_Time() {
        return Next_SR_Service_Start_Time;
    }

    public void setNext_SR_Service_Start_Time(String next_SR_Service_Start_Time) {
        Next_SR_Service_Start_Time = next_SR_Service_Start_Time;
    }

    public String getNext_SR_Service_End_Time() {
        return Next_SR_Service_End_Time;
    }

    public void setNext_SR_Service_End_Time(String next_SR_Service_End_Time) {
        Next_SR_Service_End_Time = next_SR_Service_End_Time;
    }

    public String getSR_Date() {
        return SR_Date;
    }

    public void setSR_Date(String SR_Date) {
        this.SR_Date = SR_Date;
    }

    public String getNext_SR_Planned_Start_Date() {
        return Next_SR_Planned_Start_Date;
    }

    public void setNext_SR_Planned_Start_Date(String next_SR_Planned_Start_Date) {
        Next_SR_Planned_Start_Date = next_SR_Planned_Start_Date;
    }

    public String getNext_SR_Planned_End_Date() {
        return Next_SR_Planned_End_Date;
    }

    public void setNext_SR_Planned_End_Date(String next_SR_Planned_End_Date) {
        Next_SR_Planned_End_Date = next_SR_Planned_End_Date;
    }

    public String getTaskAssignmentStartDate() {
        return TaskAssignmentStartDate;
    }

    public void setTaskAssignmentStartDate(String taskAssignmentStartDate) {
        TaskAssignmentStartDate = taskAssignmentStartDate;
    }

    public String getTaskAssignmentEndDate() {
        return TaskAssignmentEndDate;
    }

    public void setTaskAssignmentEndDate(String taskAssignmentEndDate) {
        TaskAssignmentEndDate = taskAssignmentEndDate;
    }

    public String getTag() {
        return Tag;
    }

    public void setTag(String tag) {
        Tag = tag;
    }


    public Boolean getFlushoutRequired() {
        return FlushoutRequired;
    }

    public void setFlushoutRequired(Boolean flushoutRequired) {
        FlushoutRequired = flushoutRequired;
    }

    public Boolean getGelTreatmentRequired() {
        return GelTreatmentRequired;
    }

    public void setGelTreatmentRequired(Boolean gelTreatmentRequired) {
        GelTreatmentRequired = gelTreatmentRequired;
    }



    public String getConsultationInfestationLevel() {
        return ConsultationInfestationLevel;
    }

    public void setConsultationInfestationLevel(String consultationInfestationLevel) {
        ConsultationInfestationLevel = consultationInfestationLevel;
    }

    public String getFlushout_Start_Date() {
        return Flushout_Start_Date;
    }

    public void setFlushout_Start_Date(String flushout_Start_Date) {
        Flushout_Start_Date = flushout_Start_Date;
    }

    public String getFlushout_End_Date() {
        return Flushout_End_Date;
    }

    public void setFlushout_End_Date(String flushout_End_Date) {
        Flushout_End_Date = flushout_End_Date;
    }

    public String getGelTreatment_Start_Date() {
        return GelTreatment_Start_Date;
    }

    public void setGelTreatment_Start_Date(String gelTreatment_Start_Date) {
        GelTreatment_Start_Date = gelTreatment_Start_Date;
    }

    public String getGelTreatment_End_Date() {
        return GelTreatment_End_Date;
    }

    public void setGelTreatment_End_Date(String gelTreatment_End_Date) {
        GelTreatment_End_Date = gelTreatment_End_Date;
    }

    public String getInspectionInfestationLevel() {
        return InspectionInfestationLevel;
    }

    public void setInspectionInfestationLevel(String inspectionInfestationLevel) {
        InspectionInfestationLevel = inspectionInfestationLevel;
    }

    public String getNo_Renewal_Reason() {
        return No_Renewal_Reason;
    }

    public void setNo_Renewal_Reason(String no_Renewal_Reason) {
        No_Renewal_Reason = no_Renewal_Reason;
    }

    public String getService_Sequence_Number() {
        return Service_Sequence_Number;
    }

    public void setService_Sequence_Number(String service_Sequence_Number) {
        Service_Sequence_Number = service_Sequence_Number;
    }

    public boolean getServiceActivityRequired() {
        return ServiceActivityRequired;
    }

    public void setServiceActivityRequired(boolean serviceActivityRequired) {
        ServiceActivityRequired = serviceActivityRequired;
    }

    public Boolean getShowBarcode() {
        return ShowBarcode;
    }

    public void setShowBarcode(Boolean showBarcode) {
        ShowBarcode = showBarcode;
    }

    public String getCustomer_Instructions() {
        return Customer_Instructions;
    }

    public void setCustomer_Instructions(String customer_Instructions) {
        Customer_Instructions = customer_Instructions;
    }

    public String getRefferalQuestion() {
        return RefferalQuestion;
    }

    public void setRefferalQuestion(String refferalQuestion) {
        RefferalQuestion = refferalQuestion;
    }

    public Boolean getCustomerInterestedToGiveRefferals() {
        return IsCustomerInterestedToGiveRefferals;
    }

    public void setCustomerInterestedToGiveRefferals(Boolean customerInterestedToGiveRefferals) {
        IsCustomerInterestedToGiveRefferals = customerInterestedToGiveRefferals;
    }

    public String getCustomerRefferalAlert() {
        return CustomerRefferalAlert;
    }

    public void setCustomerRefferalAlert(String customerRefferalAlert) {
        CustomerRefferalAlert = customerRefferalAlert;
    }
}
