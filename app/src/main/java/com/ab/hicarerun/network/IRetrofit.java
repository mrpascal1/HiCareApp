package com.ab.hicarerun.network;

import com.ab.hicarerun.network.models.activitymodel.ActivityResponse;
import com.ab.hicarerun.network.models.activitymodel.SaveServiceActivity;
import com.ab.hicarerun.network.models.attachmentmodel.AttachmentDeleteRequest;
import com.ab.hicarerun.network.models.attachmentmodel.AttachmentMSTResponse;
import com.ab.hicarerun.network.models.attachmentmodel.GetAttachmentResponse;
import com.ab.hicarerun.network.models.attachmentmodel.PostAttachmentRequest;
import com.ab.hicarerun.network.models.attachmentmodel.PostAttachmentResponse;
import com.ab.hicarerun.network.models.attendancemodel.AttendanceDetailResponse;
import com.ab.hicarerun.network.models.attendancemodel.AttendanceRequest;
import com.ab.hicarerun.network.models.attendancemodel.ProfilePicRequest;
import com.ab.hicarerun.network.models.BasicResponse;
import com.ab.hicarerun.network.models.checklistmodel.CheckListResponse;
import com.ab.hicarerun.network.models.checklistmodel.UploadCheckListRequest;
import com.ab.hicarerun.network.models.checklistmodel.UploadCheckListResponse;
import com.ab.hicarerun.network.models.chemicalcmodel.ChemicalConsumption;
import com.ab.hicarerun.network.models.chemicalcountmodel.ChemicalCountResponse;
import com.ab.hicarerun.network.models.chemicalmodel.ChemicalResponse;
import com.ab.hicarerun.network.models.chemicalmodel.SaveActivityRequest;
import com.ab.hicarerun.network.models.chemicalmodel.ServiceAreaChemicalResponse;
import com.ab.hicarerun.network.models.consulationmodel.ConsulationResponse;
import com.ab.hicarerun.network.models.consulationmodel.Data;
import com.ab.hicarerun.network.models.consulationmodel.RecommendationResponse;
import com.ab.hicarerun.network.models.consulationmodel.SaveConsulationResponse;
import com.ab.hicarerun.network.models.covidmodel.CovidRequest;
import com.ab.hicarerun.network.models.covidmodel.CovidResponse;
import com.ab.hicarerun.network.models.dialingmodel.DialingResponse;
import com.ab.hicarerun.network.models.exotelmodel.ExotelResponse;
import com.ab.hicarerun.network.models.feedbackmodel.FeedbackRequest;
import com.ab.hicarerun.network.models.feedbackmodel.FeedbackResponse;
import com.ab.hicarerun.network.models.generalmodel.GeneralResponse;
import com.ab.hicarerun.network.models.generalmodel.OnSiteOtpResponse;
import com.ab.hicarerun.network.models.generalmodel.TaskCheckList;
import com.ab.hicarerun.network.models.handshakemodel.ContinueHandShakeRequest;
import com.ab.hicarerun.network.models.handshakemodel.ContinueHandShakeResponse;
import com.ab.hicarerun.network.models.handshakemodel.HandShakeResponse;
import com.ab.hicarerun.network.models.incentivemodel.IncentiveResponse;
import com.ab.hicarerun.network.models.inventorymodel.AddInventoryResult;
import com.ab.hicarerun.network.models.inventorymodel.historymodel.InventoryHistoryBase;
import com.ab.hicarerun.network.models.inventorymodel.inventorylistmodel.InventoryListResult;
import com.ab.hicarerun.network.models.jeopardymodel.CWFJeopardyRequest;
import com.ab.hicarerun.network.models.jeopardymodel.CWFJeopardyResponse;
import com.ab.hicarerun.network.models.jeopardymodel.JeopardyReasonModel;
import com.ab.hicarerun.network.models.karmamodel.KarmaHistoryResponse;
import com.ab.hicarerun.network.models.karmamodel.KarmaResponse;
import com.ab.hicarerun.network.models.karmamodel.SaveKarmaRequest;
import com.ab.hicarerun.network.models.karmamodel.SaveKarmaResponse;
import com.ab.hicarerun.network.models.kycmodel.KycDocumentResponse;
import com.ab.hicarerun.network.models.kycmodel.KycTypeResponse;
import com.ab.hicarerun.network.models.kycmodel.SaveKycRequest;
import com.ab.hicarerun.network.models.kycmodel.SaveKycResponse;
import com.ab.hicarerun.network.models.leaderboardmodel.RewardLeadersResponse;
import com.ab.hicarerun.network.models.loggermodel.ErrorLoggerModel;
import com.ab.hicarerun.network.models.LoginResponse;
import com.ab.hicarerun.network.models.LogoutResponse;
import com.ab.hicarerun.network.models.menumodel.MenuResponse;
import com.ab.hicarerun.network.models.pulsemodel.PulseData;
import com.ab.hicarerun.network.models.pulsemodel.PulseResponse;
import com.ab.hicarerun.network.models.qrcodemodel.CheckCodeResponse;
import com.ab.hicarerun.network.models.qrcodemodel.CheckPhonePeResponse;
import com.ab.hicarerun.network.models.qrcodemodel.PhonePeQRCodeResponse;
import com.ab.hicarerun.network.models.qrcodemodel.QRCodeResponse;
import com.ab.hicarerun.network.models.npsmodel.NPSDataResponse;
import com.ab.hicarerun.network.models.offersmodel.OffersHistoryResponse;
import com.ab.hicarerun.network.models.offersmodel.OffersResponse;
import com.ab.hicarerun.network.models.offersmodel.UpdateRewardScratchRequest;
import com.ab.hicarerun.network.models.offersmodel.UpdateRewardScratchResponse;
import com.ab.hicarerun.network.models.onsitemodel.OnSiteAccountResponse;
import com.ab.hicarerun.network.models.onsitemodel.OnSiteAreaResponse;
import com.ab.hicarerun.network.models.onsitemodel.OnSiteRecentResponse;
import com.ab.hicarerun.network.models.onsitemodel.SaveAccountAreaRequest;
import com.ab.hicarerun.network.models.onsitemodel.SaveAccountAreaResponse;
import com.ab.hicarerun.network.models.otpmodel.SendOtpResponse;
import com.ab.hicarerun.network.models.payementmodel.BankResponse;
import com.ab.hicarerun.network.models.payementmodel.PaymentLinkRequest;
import com.ab.hicarerun.network.models.payementmodel.PaymentLinkResponse;
import com.ab.hicarerun.network.models.productmodel.ProductResponse;
import com.ab.hicarerun.network.models.profilemodel.TechnicianProfileDetails;
import com.ab.hicarerun.network.models.quizleaderboardmodel.QuizLeaderBoardBase;
import com.ab.hicarerun.network.models.quizlevelmodel.QuizLevelModelBase;
import com.ab.hicarerun.network.models.quizmodel.QuizCategoryResponse;
import com.ab.hicarerun.network.models.quizmodel.QuizPuzzleStats;
import com.ab.hicarerun.network.models.quizmodel.QuizResponse;
import com.ab.hicarerun.network.models.quizmodel.QuizSaveAnswers;
import com.ab.hicarerun.network.models.quizsavemodel.QuizSaveResponseBase;
import com.ab.hicarerun.network.models.referralmodel.ReferralDeleteRequest;
import com.ab.hicarerun.network.models.referralmodel.ReferralListResponse;
import com.ab.hicarerun.network.models.referralmodel.ReferralRequest;
import com.ab.hicarerun.network.models.referralmodel.ReferralResponse;
import com.ab.hicarerun.network.models.referralmodel.ReferralSRResponse;
import com.ab.hicarerun.network.models.rewardsmodel.RewardsResponse;
import com.ab.hicarerun.network.models.rewardsmodel.SaveRedeemRequest;
import com.ab.hicarerun.network.models.rewardsmodel.SaveRedeemResponse;
import com.ab.hicarerun.network.models.roachmodel.roachlistmodel.RoachBase;
import com.ab.hicarerun.network.models.roachmodel.saveroachmodel.RoachSaveBase;
import com.ab.hicarerun.network.models.routinemodel.RoutineResponse;
import com.ab.hicarerun.network.models.routinemodel.SaveRoutineResponse;
import com.ab.hicarerun.network.models.routinemodel.TechRoutineData;
import com.ab.hicarerun.network.models.selfassessmodel.AssessmentReportResponse;
import com.ab.hicarerun.network.models.selfassessmodel.ResourceCheckListResponse;
import com.ab.hicarerun.network.models.selfassessmodel.SelfAssessmentRequest;
import com.ab.hicarerun.network.models.selfassessmodel.SelfAssessmentResponse;
import com.ab.hicarerun.network.models.serviceplanmodel.RenewOrderRequest;
import com.ab.hicarerun.network.models.serviceplanmodel.RenewOrderResponse;
import com.ab.hicarerun.network.models.serviceplanmodel.RenewalOTPResponse;
import com.ab.hicarerun.network.models.serviceplanmodel.ServicePlanResponse;
import com.ab.hicarerun.network.models.slotmodel.SlotResponse;
import com.ab.hicarerun.network.models.tsscannermodel.BarcodeDetailsResponse;
import com.ab.hicarerun.network.models.tsscannermodel.BarcodeList;
import com.ab.hicarerun.network.models.tsscannermodel.BaseResponse;
import com.ab.hicarerun.network.models.tsscannermodel.OrderDetails;
import com.ab.hicarerun.network.models.tsscannermodel.counts.CountsResponse;
import com.ab.hicarerun.network.models.taskmodel.TaskListResponse;
import com.ab.hicarerun.network.models.taskmodel.UpdateTaskResponse;
import com.ab.hicarerun.network.models.taskmodel.UpdateTasksRequest;
import com.ab.hicarerun.network.models.techniciangroomingmodel.TechGroomingRequest;
import com.ab.hicarerun.network.models.techniciangroomingmodel.TechGroomingResponse;
import com.ab.hicarerun.network.models.technicianroutinemodel.TechnicianRoutineResponse;
import com.ab.hicarerun.network.models.tmsmodel.QuestionBase;
import com.ab.hicarerun.network.models.trainingmodel.TrainingResponse;
import com.ab.hicarerun.network.models.trainingmodel.WelcomeVideoResponse;
import com.ab.hicarerun.network.models.updateappmodel.UpdateResponse;
import com.ab.hicarerun.network.models.walletmodel.WalletBase;
import com.ab.hicarerun.network.models.voucher.VoucherResponseMain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface IRetrofit {
    //    String BASE_URL = "http://52.74.65.15/mobileapi/api/";
    //    String ERROR_LOG_URL = "http://52.74.65.15/logging/api/";
    //    http://apps.hicare.in/cwf/datasync/InsertRenewalAppJeopardy;
    String BASE_URL = "http://api.hicare.in/mobile/api/";
    String SCAN_URL = "http://api.hicare.in/taskservice/api/";
    String EXOTEL_URL = "http://apps.hicare.in/api/api/";
    String ERROR_LOG_URL = "http://api.hicare.in/logging/api/";
    String JEOPARDY_URL = "http://apps.hicare.in/cwf/";
    String SLOT_URL = "http://api.hicare.in/slot/api/";
    String UAT = "http://api.hicare.in/Mobile/api/";
    String B2B_URL = "http://connect.hicare.in/b2bwowuat/api/"; //b2bwow(production) & b2bwowuat(uat)
    String INVENTORY_URL = "http://connect.hicare.in/inventory_api/api/";
    String IOT_URL = "http://connect.hicare.in/iotapi/api/";

    /*[Verify User]*/
    @GET("userverification/VerifyUser")
    Call<SendOtpResponse> sendOtp(@Query("mobileno") String mobile, @Query("resendOtp") String isResend);

    /*[Login]*/
    @FormUrlEncoded
    @POST("Login")
    Call<LoginResponse> login(@Field("grant_type") String grantType,
                              @Field("UserName") String username,
                              @Field("Password") String password,
                              @Header("Content-Type") String content_type,
                              @Header("IMEINo") String imei,
                              @Header("AppVersion") String version,
                              @Header("DeviceInfo") String deviceinfo,
                              @Header("PlayerId") String mStrPlayerId);

    /*[Refresh Token]*/
    @FormUrlEncoded
    @POST("login")
    Call<LoginResponse> refreshToken(@Field("grant_type") String grantType,
                                     @Field("refresh_token") String refreshToken,
                                     @Header("Content-Type") String content_type,
                                     @Header("IMEINo") String imei,
                                     @Header("AppVersion") String version,
                                     @Header("DeviceInfo") String deviceinfo,
                                     @Header("PlayerId") String mStrPlayerId);

    /*[Task Details]*/
    @GET("Task/GetTaskList")
    Call<TaskListResponse> getTasksList(@Query("resourceId") String resourceId, @Query("deviceId") String IMEI);

    /*[Task Details By ID]*/
    @GET("Task/GetTaskDetailsById")
    Call<GeneralResponse> getTasksDetailById(@Query("resourceId") String resourceId, @Query("taskId") String taskId, @Query("IsCombinedTask") Boolean isCombinedTask, @Query("ln") String language);

    /*[GetReferralServiceAndRelation]*/
    @GET("CustomerReferral/GetReferralServiceAndRelation")
    Call<ReferralSRResponse> getReferralServiceAndRelation(@Query("taskId") String taskId, @Query("language") String lang);

    /*[Save Referral]*/
    @POST("CustomerReferral/SaveCustomerReferralDetails")
    Call<ReferralResponse> postReferrals(@Body ReferralRequest request);

    /*[Referral Details]*/
    @GET("CustomerReferral/GetReferralDetailsByTaskId")
    Call<ReferralListResponse> getReferrals(@Query("taskId") String taskId);

    /*[Delete Referral]*/
    @POST("CustomerReferral/DeleteCustomerReferralDetails")
    Call<ReferralResponse> getDeleteReferrals(@Body ReferralDeleteRequest request);

    /*[Send Feedback]*/
    @POST("Feedback/SendFeedbackLink")
    Call<FeedbackResponse> postFeedBackLink(@Body FeedbackRequest request);

    /*[Upload Attachment]*/
    @POST("Attachment/UploadAttachment")
    Call<PostAttachmentResponse> postAttachments(@Body PostAttachmentRequest request);

    /*[Delete Attachment]*/
    @POST("Attachment/DeleteAttachmentDetails")
    Call<PostAttachmentResponse> getDeleteAttachments(@Body List<AttachmentDeleteRequest> request);

    /*[Attachment Details]*/
    @GET("Attachment/GetAttachmentDetailsByTaskId")
    Call<GetAttachmentResponse> getAttachments(@Query("resourceId") String resourceId, @Query("taskId") String taskId);

    /*[Attachment MST Details*/
    @GET("Attachment/GetAttachmentDetailsByTaskIdForMST")
    Call<AttachmentMSTResponse> getMSTAttachments(@Query("resourceId") String resourceId, @Query("taskIds") String taskId,
                                                  @Query("serviceTypes") String serviceTypes);

    /*[Update Tasks]*/
    @POST("Task/UpdateTaskDetails")
    Call<UpdateTaskResponse> updateTasks(@Body UpdateTasksRequest request);

    /*[HandShake]*/
    @GET("ResourceActivity/InitializeActivityHandshake")
    Call<HandShakeResponse> getHandShake();

    /*[Continue HandShake]*/
    @POST("ResourceActivity/PostResourceActivity")
    Call<ContinueHandShakeResponse> getContinueHandShake(@Body ContinueHandShakeRequest request);

    /*[Chemicals Details]*/
    @GET("ChemicalConsumption/GetChemimcalDetails")
    Call<ChemicalResponse> getChemicals(@Query("taskId") String taskId);

    /*[Chemicals Details]*/
    @GET("ChemicalConsumption/GetChemimcalDetailsForMST")
    Call<ChemicalResponse> getMSTChemicals(@Query("taskId") String taskId);

    /*[Logout]*/
    @POST("ResourceActivity/LogOut")
    Call<LogoutResponse> getLogout(@Query("resourceId") String UserId);

    /*[Dial Customer]*/
    @GET("applicationlogic/DialExotelNumber")
    Call<ExotelResponse> getCallExotel(@Query("customerNo") String customerNo, @Query("techNo") String techNo);

    /*[Mark Attendance]*/
    @POST("ResourceActivity/PostResourceAttendance")
    Call<SelfAssessmentResponse> getTechAttendance(@Body AttendanceRequest request);

    /*[Register Profile]*/
    @POST("ResourceActivity/PostResourceProfilePic")
    Call<HandShakeResponse> getProfilePic(@Body ProfilePicRequest request);

    /*[Training Videos]*/
    @GET("VideoUploader/GettrainingVideoDetails")
    Call<TrainingResponse> getTrainingVideos();

    /*[Error Log]*/
    @POST("Log/Publish")
    Call<String> sendErrorLog(@Body ErrorLoggerModel request);

    /*[Update APP api]*/
    @GET("ResourceActivity/VersionCheck")
    Call<UpdateResponse> getUpdateApp();

    /*[Send Payment Link]*/

    @POST("Payment/SendPaymentLink")
    Call<PaymentLinkResponse> sendPaymentLink(@Body PaymentLinkRequest request);

    /*[Get Technician Grooming]*/

    @GET("TechnicianGrooming/GetDetails")
    Call<TechGroomingResponse> getGroomingTechnicians(@Query("resourceId") String customerNo);

    /*[Post Grooming Image]*/

    @POST("TechnicianGrooming/UploadImage")
    Call<BasicResponse> postGroomingImage(@Body TechGroomingRequest request);

    /*[Resource Profile]*/

    @GET("ResourceActivity/GetResourceProfileDetails")
    Call<TechnicianProfileDetails> getTechnicianProfile(@Query("resourceId") String customerNo);

    /*[Get Jeopardy Reasons]*/

    @GET("Jeopardy/GetHelpLineReasons")
    Call<JeopardyReasonModel> getJeopardyReasons(@Query("taskId") String taskId,
                                                 @Query("language") String language);
    /*[CWF JEOPARDY ]*/

    @POST("Jeopardy/InsertHelpLineJeopardy")
    Call<CWFJeopardyResponse> postCWFJeopardy(@Body CWFJeopardyRequest request);

    /*[Resource Incentive]*/

    @GET("ResourceActivity/GetResourceIncentive")
    Call<IncentiveResponse> getTechnicianIncentive(@Query("resourceId") String customerNo);

    /*[GetIncentiveDataForResource]*/

    @GET("ResourceActivity/GetIncentiveDataForResource")
    Call<IncentiveResponse> getResourceIncentive(@Query("resourceId") String customerNo);

    /*[getAttendanceDetail]*/

    @GET("ResourceActivity/GetResourceAttendenceStatistics")
    Call<AttendanceDetailResponse> getAttendanceDetail(@Query("resourceId") String resourceId);

    /*[Voucher Code]*/

    @GET("ResourceActivity/GetTechnicianReferralCode")
    Call<VoucherResponseMain> getTechnicianReferralCode(@Query("resourceId") String resourceId);


    /*[GetWelcomeVideo]*/
    @GET("VideoUploader/GetWelcomeVideo")
    Call<WelcomeVideoResponse> getStartingVideos(@Query("resourceId") String resourceId);


    /*[ResendOnsiteOTP]*/
    @GET("Task/ResendOnsiteOTP")
    Call<OnSiteOtpResponse> getOnsiteOTP(@Query("resourceId") String resourceId,
                                         @Query("taskId") String taskId,
                                         @Query("customername") String customername,
                                         @Query("customermobile") String customermobile);


    @GET("Task/ResendCompletionOTP")
    Call<OnSiteOtpResponse> getCompletionOTP(@Query("resourceId") String resourceId,
                                         @Query("taskId") String taskId,
                                         @Query("customername") String customername,
                                         @Query("customermobile") String customermobile);

    /*[Task/ValidateCompletionTime]*/

    @GET("Task/ValidateCompletionTime")
    Call<BasicResponse> getValidateCompletionTime(@Query("completionDateTime") String resourceId,
                                                  @Query("taskId") String taskId);

    /*[Payment/GetBanksName]*/
    @GET("Payment/GetBankList")
    Call<BankResponse> getBanksName();

    /*[AreaActivity/GetOnSiteAccount]*/
    @GET("AreaActivity/GetOnsiteAccounts")
    Call<OnSiteAccountResponse> getOnsiteAccounts(@Query("resourceId") String resourceId);

    /*[AreaActivity/GetAccountAreaActivity]*/
    @GET("AreaActivity/GetAccountAreaActivity")
    Call<OnSiteAreaResponse> getAccountAreaActivity(@Query("accountId") String accountId,
                                                    @Query("resourceId") String resourceId);
    /*[AreaActivity/SaveAccountAreaActivity]*/

    @POST("AreaActivity/SaveAccountAreaActivity")
    Call<SaveAccountAreaResponse> getSaveAccountAreaActivity(@Body SaveAccountAreaRequest request);

    /*[AreaActivity/GetAccountAreaActivity]*/
    @GET("AreaActivity/GetRecentAccountAreaActivity")
    Call<OnSiteRecentResponse> getRecentAccountAreaActivity(@Query("accountId") String accountId,
                                                            @Query("resourceId") String resourceId,
                                                            @Query("isGrouped") Boolean isGrouped);

    /*[Payment/GetBanksName]*/
    @GET("AreaActivity/GetNotDoneReasons")
    Call<BankResponse> getNotDoneReasons();

    /*[AreaActivity/DeleteAccountAreaActivity]*/

    @GET("AreaActivity/DeleteAccountAreaActivity")
    Call<SaveAccountAreaResponse> getDeleteOnSiteTasks(@Query("activityId") Integer activityId);

    /*[TechnicianGrooming/GetTechnicianJobSummary]*/

    @GET("TechnicianGrooming/GetTechnicianJobSummary")
    Call<ChemicalCountResponse> getTechnicianJobSummary(@Query("resourceId") String customerNo);

    /*[TechnicianGrooming/GetTechnicianJobSummary]*/

    @GET("ResourceActivity/GetResourceProfilePicture")
    Call<String> getResourceProfilePicture(@Query("resourceId") String resourceId);

    @GET("ResourceActivity/GetResourceRedeemedData")
    Call<RewardsResponse> getResourceRedeemedData(@Query("resourceId") String resourceId);

    @POST("ResourceActivity/SaveResourceRedeemData")
    Call<SaveRedeemResponse> getSaveResourceRedeemData(@Body SaveRedeemRequest request);

    @GET("ResourceActivity/GetResourceNPSData")
    Call<NPSDataResponse> getNPSData(@Query("resourceId") String resourceId);

    @POST("Jeopardy/InsertLessPaymentJeopardy")
    Call<CWFJeopardyResponse> insertLessPaymentJeopardy(@Body CWFJeopardyRequest request);

    @GET("ResourceActivity/GetRewards")
    Call<OffersResponse> getAllRewards(@Query("resourceId") String resourceId);

    @GET("ResourceActivity/GetRewardsWithMissedDetails")
    Call<OffersResponse> getRewardsWithMissedData(@Query("resourceId") String resourceId);

    @POST("ResourceActivity/UpdateRewardScratch")
    Call<UpdateRewardScratchResponse> updateRewardScratch(@Body UpdateRewardScratchRequest request);

    @POST("Task/UploadOnsiteImage")
    Call<CovidResponse> uploadOnsiteImage(@Body CovidRequest request);

    @GET("ResourceActivity/GetRewardHistory")
    Call<OffersHistoryResponse> getAllRewardsHistory(@Query("resourceId") String resourceId);

    @GET("ResourceActivity/GetRewardHistoryWithMissedDetails")
    Call<OffersHistoryResponse> getRewardHistoryWithMissedDetails(@Query("resourceId") String resourceId);

    @GET("payment/GenerateTaskQRCode")
    Call<QRCodeResponse> getTaskQRCode(@Query("taskNo") String taskNo);

    @GET("payment/GenerateUPICode")
    Call<QRCodeResponse> getUPICode(@Query("taskId") String taskId, @Query("accountNo") String accountNo, @Query("orderNo") String orderNo, @Query("amount") String amount,
                                    @Query("source") String source);

    @GET("payment/GenerateRenewalUPICode")
    Call<QRCodeResponse> getGenerateUPICode(@Query("taskId") String taskId, @Query("accountNo") String accountNo, @Query("orderNo") String orderNo, @Query("amount") String amount,
                                            @Query("source") String source);

    @GET("phonepepayment/GeneratePhonePeQRCode")
    Call<PhonePeQRCodeResponse> getPhonePeCode(@Query("taskid") String taskkId, @Query("accountNo") String accountNo, @Query("orderNo") String orderNo,
                                               @Query("amount") String amount, @Query("source") String source);

    @GET("payment/CheckUPIPaymentStatus")
    Call<CheckCodeResponse> checkUPIPaymentStatus(@Query("orderNo") String orderNo);

    @GET("payment/CheckRenewalUPIPaymentStatus")
    Call<CheckCodeResponse> checkUPIRenewalPaymentStatus(@Query("orderNo") String orderNo);

    @GET("phonepepayment/CheckPhonePEPaymentStatus")
    Call<CheckPhonePeResponse> checkPhonePeStatus(@Query("taskid") String taskId, @Query("transactionId") String transactionId, @Query("orderNo") String orderNo);

    @POST("Task/SaveCheckList")
    Call<CheckListResponse> saveCheckList(@Body List<TaskCheckList> request);

    /*ResourceActivity/GetRewardLeaders*/
    @GET("ResourceActivity/GetRewardLeaders")
    Call<RewardLeadersResponse> getRewardLeaders(@Query("resourceId") String userId);

    /*api/ApplicationLogic/DialNumber*/
    @GET("ApplicationLogic/DialNumber")
    Call<DialingResponse> getDialNumber(@Query("customerNo") String custNo, @Query("techNo") String techNo);

    /*ResourceActivity/GetResourceChecklist*/
    @GET("ResourceActivity/GetResourceChecklist")
    Call<ResourceCheckListResponse> getResourceCheckList(@Query("resourceId") String mobile, @Query("ln") String isResend);

    /*ResourceActivity/SaveResourceChecklistResponse*/
    @POST("ResourceActivity/SaveResourceChecklistResponse")
    Call<SelfAssessmentResponse> saveResourceCheckList(@Body List<SelfAssessmentRequest> request);

    /*ResourceActivity/GetResourceChecklistResponse*/
    @GET("ResourceActivity/GetResourceChecklistResponse")
    Call<AssessmentReportResponse> getAssessmentResponse(@Query("resourceId") String mobile, @Query("ln") String lang);

    @GET("Renewal/GetRenewalDetails")
    Call<ServicePlanResponse> getServicePlans(@Query("taskid") String taskid);

    @GET("Renewal/GetProducts")
    Call<ProductResponse> getProducts();

    /*renewal/RenewOrder*/
    @POST("Renewal/RenewOrder")
    Call<RenewOrderResponse> renewOrder(@Body RenewOrderRequest request);

    @GET("Renewal/ResendRenewalOTP")
    Call<RenewalOTPResponse> getRenewalOTP(@Query("resourceId") String resourceId, @Query("taskid") String taskid);

    @GET("Renewal/UpdateNoRenewalReason")
    Call<BasicResponse> updateNoRenewalReason(@Query("taskid") String taskid, @Query("reason") String reason);

    /*renewal/RenewOrder*/
    @POST("Consultation/UploadAttachment")
    Call<UploadCheckListResponse> uploadCheckListAttachment(@Body UploadCheckListRequest request);

    /*Consultation/GetConsultationAndInspectionQuestions*/
    @GET("Consultation/GetConsultationAndInspectionQuestions")
    Call<ConsulationResponse> getConsulation(@Query("resourceId") String resourceId, @Query("taskId") String taskid, @Query("lan") String lan);

    /*Consultation/GetConsultationAndInspectionQuestions*/
    @POST("Consultation/SaveConsultationAndInspectionAnswers")
    Call<SaveConsulationResponse> saveConsultationNdInspection(@Body List<Data> request);

    /*Consultation/GetRecommendations*/
    @GET("Consultation/GetRecommendations")
    Call<RecommendationResponse> getRecommendations(@Query("resourceId") String resourceId, @Query("taskId") String taskid, @Query("lan") String lan);

    /*TechnicianGrooming/GetTechnicianForRoutineCheckList*/
    @GET("TechnicianGrooming/GetTechnicianForRoutineCheckList")
    Call<TechnicianRoutineResponse> getTechniciansForRoutineCheckList(@Query("resourceid") String resourceId);

    /*TechnicianGrooming/GetRoutineCheckList*/
    @GET("TechnicianGrooming/GetRoutineCheckList")
    Call<RoutineResponse> getRoutineData(@Query("resourceid") String resourceId, @Query("language") String lang);

    @POST("TechnicianGrooming/SaveRoutineCheckList")
    Call<SaveRoutineResponse> saveRoutineCheckList(@Body TechRoutineData request);

    /*ResourceActivity/GetKYCDocuments*/
    @GET("ResourceActivity/GetKYCDocuments")
    Call<KycDocumentResponse> getKYCDocuments(@Query("resourceid") String resourceId, @Query("language") String lang);

    /*ResourceActivity/GetKYCTypes*/
    @GET("ResourceActivity/GetKYCTypes")
    Call<KycTypeResponse> getKYCTypes(@Query("resourceid") String resourceId, @Query("language") String lang);

    @POST("ResourceActivity/SaveKYCDocuments")
    Call<SaveKycResponse> saveKYCDocument(@Body SaveKycRequest request);

    /*ResourceActivity/GetKarmaForResource*/
    @GET("ResourceActivity/GetKarmaForResource")
    Call<KarmaResponse> getKarmaForResource(@Query("resourceId") String userId);

    /*ResourceActivity/GetKarmaForResource*/
    @GET("ResourceActivity/GetKarmaHistoryForResource")
    Call<KarmaHistoryResponse> getKarmaHistoryForResource(@Query("resourceId") String userId);

    /*slot/GetAvailableSlotForMobile*/
    @GET("slot/GetAvailableSlotForMobile")
    Call<SlotResponse> getAppointmentSlots(@Query("taskId") String taskId, @Query("slotStartDate") String slotStartDate, @Query("slotEndDate") String slotEndDate, @Query("source") int source);

    @POST("ResourceActivity/SaveKarmaVideoDetails")
    Call<SaveKarmaResponse> saveKarmaDetails(@Body SaveKarmaRequest request);

    @GET("payment/GenerateRenewalUPICode")
    Call<QRCodeResponse> getRenewalUPICode(@Query("taskId") String taskId, @Query("accountNo") String accountNo, @Query("orderNo") String orderNo, @Query("amount") String amount,
                                           @Query("source") String source);
    /*[puzzle/getpuzzlelist]*/

    @GET("puzzle/getpuzzlelist")
    Call<QuizCategoryResponse> getQuizCategory(@Query("resourceId") String resourceId, @Query("lan") String lan);

    /*[puzzle/GetPuzzleQuestionListById]*/

    @GET("puzzle/GetPuzzleQuestionListById")
    Call<QuizResponse> getQuizQuestions(@Query("resourceId") String resourceId, @Query("puzzleId") int puzzleId, @Query("lan") String lan);

    @GET("Barcode/GetOrderDetails")
    Call<OrderDetails> getOrderDetails(@Query("orderNo") String orderNo, @Query("userId") String userId);

    @GET("Barcode/GetBarcodeOrderDetails")
    Call<BarcodeDetailsResponse> getBarcodeOrderDetails(@Query("orderNo") String orderNo, @Query("userId") String userId, @Query("barcodeType") String barcodeType);

    @POST("Barcode/SaveBarcodeDetails")
    Call<BaseResponse> saveBarcode(@Body ArrayList<BarcodeList> barcodeList);

    @POST("Barcode/VerifyBarcodeDetails")
    Call<BaseResponse> verifyBarcode(@Body HashMap<String, Object> verification);

    @POST("Barcode/DeleteBarcodeDetails")
    Call<BaseResponse> deleteBarcode(@Body HashMap<String, Object> details);

    @GET("Barcode/GetBarcodeOrderSummaryCount")
    Call<CountsResponse> getBarcodeSummaryCount(@Query("orderNo") String orderNo, @Query("userId") String userId);

    @POST("Barcode/UploadBoxImage")
    Call<BaseResponse> uploadBoxImage(@Body HashMap<String, String> imageDetails);

    @GET("Integration/GetOrderServiceAreaChemical")
    Call<ServiceAreaChemicalResponse> getServiceAreaChemical(@Query("orderNo") String activityId, @Query("serviceNo") int serviceNo, @Query("serviceType") String seviceType, @Query("showAllService") boolean showAllService);

    @POST("Integration/UpdateActivityServiceStatus")
    Call<BaseResponse> updateActivityStatus(@Body List<SaveActivityRequest> requests);

    @GET("Integration/GetOrderActivityChemical")
    Call<ActivityResponse> getServiceActivityChemical(@Query("orderNo") String activityId, @Query("serviceNo") int serviceNo, @Query("serviceType") String seviceType, @Query("showAllService") boolean showAllService);

    @POST("Integration/SaveChemicalConsumptionByServiceActivity")
    Call<BaseResponse> saveChemicalConsumptionByServiceActivity(@Body List<HashMap<Object, Object>> body);

    @GET("Integration/GetChemicalConsumptionForAllServiceActivity")
    Call<ChemicalConsumption> getChemicalConsumptionForAllServiceActivity(@Query("orderNo") String orderNo, @Query("serviceSequenceNo") String serviceSequenceNo);

    @POST("Integration/UpdateServiceActivityStatus")
    Call<BaseResponse> updateActivityServiceStatus(@Body List<SaveServiceActivity> requests);

    @GET("resourceactivity/GetResourceMenu")
    Call<MenuResponse> getResourceMenu(@Query("resourceId") String resourceId);

    @GET("Puzzle/GetPuzzleStatsForResource")
    Call<QuizPuzzleStats> getPuzzleStatsForResources(@Query("resourceId") String resourceId, @Query("lan") String lan);

    @GET("Puzzle/GetPuzzleLeaderBoard")
    Call<QuizLeaderBoardBase> getPuzzleLeaderBoard(@Query("resourceId") String resourceId, @Query("lan") String lan);

    @GET("Puzzle/GetPuzzleLevelModel")
    Call<QuizLevelModelBase> getPuzzleLevelModel(@Query("resourceId") String resourceId, @Query("lan") String lan);

    @POST("Puzzle/SavePuzzleAnswers")
    Call<QuizSaveResponseBase> savePuzzleAnswers(@Body List<QuizSaveAnswers> quizSaveAnswers);

    @GET("CustomerReferral/SendReferralMessage")
    Call<BaseResponse> sendReferralMessage(@Query("resourceId") String resourceId, @Query("taskId") String taskId);

    @GET("TMSDigitisation/GetTMSQuestions")
    Call<QuestionBase> getTmsQuestions(@Query("taskId") String taskId, @Query("lan") String lan);

    @POST("TMSDigitisation/UpdateTMSQuestions")
    Call<BaseResponse> saveTmsQuestions(@Body List<HashMap<String, Object>> data);

    @GET("TMSDigitisation/GetServiceDeliveryQuestions")
    Call<QuestionBase> getServiceDeliveryQuestions(@Query("taskId") String taskId, @Query("lan") String lan);

    @POST("TMSDigitisation/UpdateServiceDelivery")
    Call<CheckListResponse> saveServiceDelivery(@Body List<HashMap<String, Object>> data);

    @GET("Renewal/GetWalletBalance")
    Call<WalletBase> getWalletBalance(@Query("taskId") String taskId);

    @POST("Inventory/AddInventory")
    Call<AddInventoryResult> addInventory(@Body HashMap<String, Object> data);

    @POST("Inventory/UpdateInventory")
    Call<BaseResponse> updateInventory(@Body HashMap<String, Object> data);

    @GET("Inventory/GetInventoryList")
    Call<InventoryListResult> getInventoryList(@Query("userId") String userId);

    @GET("Inventory/GetInventoryListByOrderNo")
    Call<InventoryListResult> getInventoryListByOrderNo(@Query("orderno") String orderNo);

    @GET("Inventory/GetInventoryHistory")
    Call<InventoryHistoryBase> getInventoryHistory(@Query("itemCode") String itemCode);

    @GET("B2BInspection/GetB2bInspectionQuestions")
    Call<PulseResponse> getPulseB2bInspectionQuestions(@Query("taskId") String taskId, @Query("resourceId") String resourceId, @Query("lan") String lan);

    @POST("B2BInspection/UpdateB2BInspection")
    Call<BaseResponse> updateB2BInspection(@Body List<PulseData> data);

    @POST("DeviceRegistration/SaveDeviceRegistrationForApp")
    Call<RoachSaveBase> saveDeviceRegistrationForApp(@Body HashMap<String, Object> data);

    @GET("Device/GetAllDeviceByAccount")
    Call<RoachBase> getAllDeviceByAccount(@Query("AccountNo") String accountNo);

    @POST("DeviceRegistration/DeleteDevice")
    Call<BaseResponse> deleteDevice(@Body HashMap<String, Object> data);

    @POST("IOTImage/UpdateImageForApp")
    Call<BaseResponse> updateImageForApp(@Body HashMap<String, Object> data);
}