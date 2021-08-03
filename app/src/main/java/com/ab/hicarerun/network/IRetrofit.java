package com.ab.hicarerun.network;

import com.ab.hicarerun.network.models.ActivityModel.ActivityResponse;
import com.ab.hicarerun.network.models.ActivityModel.SaveServiceActivity;
import com.ab.hicarerun.network.models.AttachmentModel.AttachmentDeleteRequest;
import com.ab.hicarerun.network.models.AttachmentModel.AttachmentMSTResponse;
import com.ab.hicarerun.network.models.AttachmentModel.GetAttachmentResponse;
import com.ab.hicarerun.network.models.AttachmentModel.PostAttachmentRequest;
import com.ab.hicarerun.network.models.AttachmentModel.PostAttachmentResponse;
import com.ab.hicarerun.network.models.AttendanceModel.AttendanceDetailResponse;
import com.ab.hicarerun.network.models.AttendanceModel.AttendanceRequest;
import com.ab.hicarerun.network.models.AttendanceModel.ProfilePicRequest;
import com.ab.hicarerun.network.models.BasicResponse;
import com.ab.hicarerun.network.models.CheckListModel.CheckListResponse;
import com.ab.hicarerun.network.models.CheckListModel.UploadCheckListRequest;
import com.ab.hicarerun.network.models.CheckListModel.UploadCheckListResponse;
import com.ab.hicarerun.network.models.ChemicalCountModel.ChemicalCountResponse;
import com.ab.hicarerun.network.models.ChemicalModel.ChemicalResponse;
import com.ab.hicarerun.network.models.ChemicalModel.SaveActivityRequest;
import com.ab.hicarerun.network.models.ChemicalModel.ServiceAreaChemicalResponse;
import com.ab.hicarerun.network.models.ChemicalModel.ServiceChemicalData;
import com.ab.hicarerun.network.models.ConsulationModel.ConsulationResponse;
import com.ab.hicarerun.network.models.ConsulationModel.Data;
import com.ab.hicarerun.network.models.ConsulationModel.RecommendationResponse;
import com.ab.hicarerun.network.models.ConsulationModel.SaveConsulationResponse;
import com.ab.hicarerun.network.models.CovidModel.CovidRequest;
import com.ab.hicarerun.network.models.CovidModel.CovidResponse;
import com.ab.hicarerun.network.models.DialingModel.DialingResponse;
import com.ab.hicarerun.network.models.ExotelModel.ExotelResponse;
import com.ab.hicarerun.network.models.FeedbackModel.FeedbackRequest;
import com.ab.hicarerun.network.models.FeedbackModel.FeedbackResponse;
import com.ab.hicarerun.network.models.GeneralModel.GeneralResponse;
import com.ab.hicarerun.network.models.GeneralModel.OnSiteOtpResponse;
import com.ab.hicarerun.network.models.GeneralModel.TaskCheckList;
import com.ab.hicarerun.network.models.HandShakeModel.ContinueHandShakeRequest;
import com.ab.hicarerun.network.models.HandShakeModel.ContinueHandShakeResponse;
import com.ab.hicarerun.network.models.HandShakeModel.HandShakeResponse;
import com.ab.hicarerun.network.models.IncentiveModel.IncentiveResponse;
import com.ab.hicarerun.network.models.JeopardyModel.CWFJeopardyRequest;
import com.ab.hicarerun.network.models.JeopardyModel.CWFJeopardyResponse;
import com.ab.hicarerun.network.models.JeopardyModel.JeopardyReasonModel;
import com.ab.hicarerun.network.models.KarmaModel.KarmaHistoryResponse;
import com.ab.hicarerun.network.models.KarmaModel.KarmaResponse;
import com.ab.hicarerun.network.models.KarmaModel.SaveKarmaRequest;
import com.ab.hicarerun.network.models.KarmaModel.SaveKarmaResponse;
import com.ab.hicarerun.network.models.KycModel.KycDocumentResponse;
import com.ab.hicarerun.network.models.KycModel.KycTypeResponse;
import com.ab.hicarerun.network.models.KycModel.SaveKycRequest;
import com.ab.hicarerun.network.models.KycModel.SaveKycResponse;
import com.ab.hicarerun.network.models.LeaderBoardModel.RewardLeadersResponse;
import com.ab.hicarerun.network.models.LoggerModel.ErrorLoggerModel;
import com.ab.hicarerun.network.models.LoginResponse;
import com.ab.hicarerun.network.models.LogoutResponse;
import com.ab.hicarerun.network.models.MenuModel.MenuResponse;
import com.ab.hicarerun.network.models.ModelQRCode.CheckCodeResponse;
import com.ab.hicarerun.network.models.ModelQRCode.CheckPhonePeResponse;
import com.ab.hicarerun.network.models.ModelQRCode.PhonePeQRCodeResponse;
import com.ab.hicarerun.network.models.ModelQRCode.QRCodeResponse;
import com.ab.hicarerun.network.models.NPSModel.NPSDataResponse;
import com.ab.hicarerun.network.models.OffersModel.OffersHistoryResponse;
import com.ab.hicarerun.network.models.OffersModel.OffersResponse;
import com.ab.hicarerun.network.models.OffersModel.UpdateRewardScratchRequest;
import com.ab.hicarerun.network.models.OffersModel.UpdateRewardScratchResponse;
import com.ab.hicarerun.network.models.OnSiteModel.OnSiteAccountResponse;
import com.ab.hicarerun.network.models.OnSiteModel.OnSiteAreaResponse;
import com.ab.hicarerun.network.models.OnSiteModel.OnSiteRecentResponse;
import com.ab.hicarerun.network.models.OnSiteModel.SaveAccountAreaRequest;
import com.ab.hicarerun.network.models.OnSiteModel.SaveAccountAreaResponse;
import com.ab.hicarerun.network.models.OtpModel.SendOtpResponse;
import com.ab.hicarerun.network.models.PayementModel.BankResponse;
import com.ab.hicarerun.network.models.PayementModel.PaymentLinkRequest;
import com.ab.hicarerun.network.models.PayementModel.PaymentLinkResponse;
import com.ab.hicarerun.network.models.ProductModel.ProductResponse;
import com.ab.hicarerun.network.models.ProfileModel.TechnicianProfileDetails;
import com.ab.hicarerun.network.models.QuizModel.QuizCategoryResponse;
import com.ab.hicarerun.network.models.QuizModel.QuizResponse;
import com.ab.hicarerun.network.models.ReferralModel.ReferralDeleteRequest;
import com.ab.hicarerun.network.models.ReferralModel.ReferralListResponse;
import com.ab.hicarerun.network.models.ReferralModel.ReferralRequest;
import com.ab.hicarerun.network.models.ReferralModel.ReferralResponse;
import com.ab.hicarerun.network.models.ReferralModel.ReferralSRResponse;
import com.ab.hicarerun.network.models.RewardsModel.RewardsResponse;
import com.ab.hicarerun.network.models.RewardsModel.SaveRedeemRequest;
import com.ab.hicarerun.network.models.RewardsModel.SaveRedeemResponse;
import com.ab.hicarerun.network.models.RoutineModel.RoutineResponse;
import com.ab.hicarerun.network.models.RoutineModel.SaveRoutineResponse;
import com.ab.hicarerun.network.models.RoutineModel.TechRoutineData;
import com.ab.hicarerun.network.models.SelfAssessModel.AssessmentReportResponse;
import com.ab.hicarerun.network.models.SelfAssessModel.ResourceCheckListResponse;
import com.ab.hicarerun.network.models.SelfAssessModel.SelfAssessmentRequest;
import com.ab.hicarerun.network.models.SelfAssessModel.SelfAssessmentResponse;
import com.ab.hicarerun.network.models.ServicePlanModel.RenewOrderRequest;
import com.ab.hicarerun.network.models.ServicePlanModel.RenewOrderResponse;
import com.ab.hicarerun.network.models.ServicePlanModel.RenewalOTPResponse;
import com.ab.hicarerun.network.models.ServicePlanModel.ServicePlanResponse;
import com.ab.hicarerun.network.models.SlotModel.SlotResponse;
import com.ab.hicarerun.network.models.TSScannerModel.BarcodeDetailsResponse;
import com.ab.hicarerun.network.models.TSScannerModel.BarcodeList;
import com.ab.hicarerun.network.models.TSScannerModel.BaseResponse;
import com.ab.hicarerun.network.models.TSScannerModel.OrderDetails;
import com.ab.hicarerun.network.models.TaskModel.TaskListResponse;
import com.ab.hicarerun.network.models.TaskModel.UpdateTaskResponse;
import com.ab.hicarerun.network.models.TaskModel.UpdateTasksRequest;
import com.ab.hicarerun.network.models.TechnicianGroomingModel.TechGroomingRequest;
import com.ab.hicarerun.network.models.TechnicianGroomingModel.TechGroomingResponse;
import com.ab.hicarerun.network.models.TechnicianRoutineModel.TechnicianRoutineResponse;
import com.ab.hicarerun.network.models.TrainingModel.TrainingResponse;
import com.ab.hicarerun.network.models.TrainingModel.WelcomeVideoResponse;
import com.ab.hicarerun.network.models.UpdateAppModel.UpdateResponse;
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
    String B2B_URL = "http://connect.hicare.in/b2bwow/api/";

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
    Call<QuizCategoryResponse> getQuizCategory(@Query("resourceId") String resourceId);

    /*[puzzle/GetPuzzleQuestionListById]*/

    @GET("puzzle/GetPuzzleQuestionListById")
    Call<QuizResponse> getQuizQuestions(@Query("resourceId") String resourceId, @Query("puzzleId") int puzzleId);

    @GET("Barcode/GetOrderDetails")
    Call<OrderDetails> getOrderDetails(@Query("orderNo") String orderNo, @Query("userId") String userId);

    @GET("Barcode/GetBarcodeOrderDetails")
    Call<BarcodeDetailsResponse> getBarcodeOrderDetails(@Query("orderNo") String orderNo, @Query("userId") String userId);

    @POST("Barcode/SaveBarcodeDetails")
    Call<BaseResponse> saveBarcode(@Body ArrayList<BarcodeList> barcodeList);

    @POST("Barcode/VerifyBarcodeDetails")
    Call<BaseResponse> verifyBarcode(@Body HashMap<String, Object> verification);

    @POST("Barcode/DeleteBarcodeDetails")
    Call<BaseResponse> deleteBarcode(@Body HashMap<String, Object> details);

    @GET("Integration/GetOrderServiceAreaChemical")
    Call<ServiceAreaChemicalResponse> getServiceAreaChemical(@Query("orderNo") String activityId, @Query("serviceNo") int serviceNo, @Query("serviceType") String seviceType, @Query("showAllService") boolean showAllService);

    @POST("Integration/UpdateActivityServiceStatus")
    Call<BaseResponse> updateActivityStatus(@Body List<SaveActivityRequest> requests);

    @GET("Integration/GetOrderActivityChemical")
    Call<ActivityResponse> getServiceActivityChemical(@Query("orderNo") String activityId, @Query("serviceNo") int serviceNo, @Query("serviceType") String seviceType, @Query("showAllService") boolean showAllService);

    @POST("Integration/UpdateServiceActivityStatus")
    Call<BaseResponse> updateActivityServiceStatus(@Body List<SaveServiceActivity> requests);

    @GET("resourceactivity/GetResourceMenu")
    Call<MenuResponse> getResourceMenu(@Query("resourceId") String resourceId);

}