package com.ab.hicarerun.network;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.ab.hicarerun.BaseApplication;
import com.ab.hicarerun.BaseFragment;
import com.ab.hicarerun.R;
import com.ab.hicarerun.activities.HomeActivity;
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
import com.ab.hicarerun.network.models.QuizLeaderBoardModel.QuizLeaderBoardBase;
import com.ab.hicarerun.network.models.QuizLevelModel.QuizLevelModelBase;
import com.ab.hicarerun.network.models.QuizModel.QuizCategoryResponse;
import com.ab.hicarerun.network.models.QuizModel.QuizPuzzleStats;
import com.ab.hicarerun.network.models.QuizModel.QuizResponse;
import com.ab.hicarerun.network.models.QuizModel.QuizSaveAnswers;
import com.ab.hicarerun.network.models.QuizSaveModel.QuizSaveResponseBase;
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
import com.ab.hicarerun.network.models.TaskModel.UpdateTasksRequest;
import com.ab.hicarerun.network.models.TaskModel.UpdateTaskResponse;
import com.ab.hicarerun.network.models.TechnicianGroomingModel.TechGroomingRequest;
import com.ab.hicarerun.network.models.TechnicianGroomingModel.TechGroomingResponse;
import com.ab.hicarerun.network.models.TechnicianRoutineModel.TechnicianRoutineResponse;
import com.ab.hicarerun.network.models.TmsModel.QuestionBase;
import com.ab.hicarerun.network.models.TmsModel.TmsData;
import com.ab.hicarerun.network.models.TrainingModel.TrainingResponse;
import com.ab.hicarerun.network.models.TrainingModel.WelcomeVideoResponse;
import com.ab.hicarerun.network.models.UpdateAppModel.UpdateResponse;
import com.ab.hicarerun.network.models.voucher.VoucherResponseMain;
import com.ab.hicarerun.utils.AppUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkCallController {

    private final BaseFragment mContext;
    private NetworkResponseListner mListner;

    public NetworkCallController(BaseFragment context) {
        this.mContext = context;
    }

    public NetworkCallController() {
        this.mContext = null;
    }

    public void setListner(NetworkResponseListner listner) {
        this.mListner = listner;
    }

    public void getHandShake(final int requestCode) {
        try {
            BaseApplication.getRetrofitAPI(true)
                    .getHandShake()
                    .enqueue(new Callback<HandShakeResponse>() {
                        @Override
                        public void onResponse(Call<HandShakeResponse> call, Response<HandShakeResponse> response) {
                            try {
                                if (response != null) {
                                    if (response.body() != null) {
                                        mListner.onResponse(requestCode, response.body().getData());
                                    } else if (response.errorBody() != null) {
                                        try {
                                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), "", "getHandShake", lineNo, "", DeviceName);

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(Call<HandShakeResponse> call, Throwable t) {

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getContinueHandShake(final int requestCode, final ContinueHandShakeRequest request) {
        try {
            BaseApplication.getRetrofitAPI(true)
                    .getContinueHandShake(request)
                    .enqueue(new Callback<ContinueHandShakeResponse>() {
                        @Override
                        public void onResponse(Call<ContinueHandShakeResponse> call, Response<ContinueHandShakeResponse> response) {
                            try {
                                if (response != null) {

                                    if (response.code() == 401) { // Unauthorised Access
                                        NetworkCallController controller = new NetworkCallController();
                                        controller.setListner(new NetworkResponseListner<LoginResponse>() {
                                            @Override
                                            public void onResponse(int reqCode, LoginResponse response) {
                                                try {
                                                    // delete all previous record
                                                    Realm.getDefaultInstance().beginTransaction();
                                                    Realm.getDefaultInstance().deleteAll();
                                                    Realm.getDefaultInstance().commitTransaction();

                                                    // add new record
                                                    Realm.getDefaultInstance().beginTransaction();
                                                    Realm.getDefaultInstance().copyToRealmOrUpdate(response);
                                                    Realm.getDefaultInstance().commitTransaction();
                                                    getContinueHandShake(requestCode, request);
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                            @Override
                                            public void onFailure(int requestCode) {

                                            }
                                        });
                                        controller.refreshToken(100, getRefreshToken());
                                    } else if (response.body() != null) {
                                        mListner.onResponse(requestCode, response.body());
                                    } else if (response.errorBody() != null) {
                                        try {
                                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            Log.e("Error Log", "Arjun Bhatt " + lineNo + DeviceName);

//                                    AppUtils.sendErrorLogs(response.errorBody().string(), "", "getContinueHandShake", lineNo, "", DeviceName);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<ContinueHandShakeResponse> call, Throwable t) {
                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                            Log.e("Error Log", "Arjun Bhatt " + lineNo + DeviceName);
//                        AppUtils.sendErrorLogs(t.getMessage(), "", "getContinueHandShake", lineNo, "", DeviceName);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void sendOtp(final int requestCode, final String mobile, final String isResend) {
        try {
            mContext.showProgressDialog();
            BaseApplication.getRetrofitAPI(false)
                    .sendOtp(mobile, isResend)
                    .enqueue(new Callback<SendOtpResponse>() {
                        @Override
                        public void onResponse(Call<SendOtpResponse> call,
                                               Response<SendOtpResponse> response) {
                            mContext.dismissProgressDialog();

                            if (response != null) {
                                if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body());

                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        mContext.showServerError(jObjError.getString("Message"));
                                        String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                        String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                        AppUtils.sendErrorLogs(response.errorBody().string(), "", "sendOtp", lineNo, "", DeviceName);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                mContext.showServerError();
                            }
                        }

                        @Override
                        public void onFailure(Call<SendOtpResponse> call, Throwable t) {
                            mContext.dismissProgressDialog();
                            mContext.showServerError(mContext.getString(R.string.something_went_wrong));

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void login(final int requestCode, String username, String password, String versionName, final String imei, final String device_info, String mStrPlayerId) {
        try {
            mContext.showProgressDialog();

            BaseApplication.getRetrofitAPI(false)
                    .login("password", username, password, "application/x-www-form-urlencoded", imei, versionName, device_info, mStrPlayerId)
                    .enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                            mContext.dismissProgressDialog();

                            if (response != null) {
                                if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body());
                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        mContext.showServerError("Invalid OTP!");
                                        String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                        String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                        AppUtils.sendErrorLogs(response.errorBody().string(), "", "login", lineNo, "", DeviceName);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                mContext.showServerError("Invalid OTP!");
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                            mContext.dismissProgressDialog();
                            mContext.showServerError(mContext.getString(R.string.something_went_wrong));

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refreshToken(final int requestCode, final String refreshToken) {
        try {
            BaseApplication.getRetrofitAPI(false)
                    .refreshToken("refresh_token", refreshToken, "text/plain", "", "", "", "")
                    .enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                            try {
                                if (response != null) {
                                    if (response.body() != null) {
                                        mListner.onResponse(requestCode, response.body());
                                    } else {
                                        mListner.onFailure(requestCode);
                                    }
                                } else {
                                    mListner.onFailure(requestCode);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                            mListner.onFailure(requestCode);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void getTasksList(final int requestCode, final String userId, final String IMEI) {
        try {
            mContext.showProgressDialog();
            BaseApplication.getRetrofitAPI(true)
                    .getTasksList(userId, IMEI)
                    .enqueue(new Callback<TaskListResponse>() {
                        @Override
                        public void onResponse(Call<TaskListResponse> call,
                                               Response<TaskListResponse> response) {
                            try {
                                mContext.dismissProgressDialog();
                                if (response != null) {
                                    if (response.code() == 401) { // Unauthorised Access
                                        NetworkCallController controller = new NetworkCallController();
                                        controller.setListner(new NetworkResponseListner<LoginResponse>() {
                                            @Override
                                            public void onResponse(int reqCode, LoginResponse response) {
                                                try {
                                                    // delete all previous record
                                                    Realm.getDefaultInstance().beginTransaction();
                                                    Realm.getDefaultInstance().deleteAll();
                                                    Realm.getDefaultInstance().commitTransaction();

                                                    // add new record
                                                    Realm.getDefaultInstance().beginTransaction();
                                                    Realm.getDefaultInstance().copyToRealmOrUpdate(response);
                                                    Realm.getDefaultInstance().commitTransaction();
                                                    getTasksList(requestCode, userId, IMEI);
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                            @Override
                                            public void onFailure(int requestCode) {

                                            }
                                        });
                                        controller.refreshToken(100, getRefreshToken());
                                    } else if (response.body() != null) {
                                        mListner.onResponse(requestCode, response.body());

                                    } else if (response.errorBody() != null) {
                                        try {
                                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                                            mContext.showServerError(jObjError.getString("Message"));
                                            RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                            if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                                String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                                String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                                String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                                AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getTasksList", lineNo, userName, DeviceName);
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } else {
                                    mContext.showServerError();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<TaskListResponse> call, Throwable t) {
                            mContext.dismissProgressDialog();
                            mContext.showServerError(mContext.getString(R.string.something_went_wrong));

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getTaskDetailById(final int requestCode, final String userId, final String taskId, final Boolean isCombinedTask, String language, final Activity context, ProgressDialog progress) {
        try {
            BaseApplication.getRetrofitAPI(true)
                    .getTasksDetailById(userId, taskId, isCombinedTask, language)
                    .enqueue(new Callback<GeneralResponse>() {
                        @Override
                        public void onResponse(Call<GeneralResponse> call,
                                               Response<GeneralResponse> response) {
                            try {
                                if (response != null) {
                                    try {
                                        progress.dismiss();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    if (response.code() == 401) { // Unauthorised Access
                                        NetworkCallController controller = new NetworkCallController();
                                        controller.setListner(new NetworkResponseListner<LoginResponse>() {
                                            @Override
                                            public void onResponse(int reqCode, LoginResponse response) {
                                                try {
                                                    // delete all previous record
                                                    Realm.getDefaultInstance().beginTransaction();
                                                    Realm.getDefaultInstance().deleteAll();
                                                    Realm.getDefaultInstance().commitTransaction();

                                                    // add new record
                                                    Realm.getDefaultInstance().beginTransaction();
                                                    Realm.getDefaultInstance().copyToRealmOrUpdate(response);
                                                    Realm.getDefaultInstance().commitTransaction();
                                                    getTaskDetailById(requestCode, userId, taskId, isCombinedTask, language, context, progress);
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                            @Override
                                            public void onFailure(int requestCode) {
                                                try {
                                                    progress.dismiss();

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                        controller.refreshToken(100, getRefreshToken());
                                    } else if (response.body() != null) {
                                        try {
                                            mListner.onResponse(requestCode, response.body());
                                            progress.dismiss();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    } else if (response.errorBody() != null) {
                                        try {
                                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                                            RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                            if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                                String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                                String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                                String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                                AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getTaskDetailById", lineNo, userName, DeviceName);
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(Call<GeneralResponse> call, Throwable t) {
                            try {
                                progress.dismiss();
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setMessage(mContext.getString(R.string.something_went_wrong));
                                builder.setNeutralButton("Dismiss", (dialogInterface, i) -> dialogInterface.dismiss()).create().show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void postReferrals(final int requestCode, ReferralRequest request) {
        try {
            mContext.showProgressDialog();
            BaseApplication.getRetrofitAPI(true)
                    .postReferrals(request)
                    .enqueue(new Callback<ReferralResponse>() {
                        @Override
                        public void onResponse(Call<ReferralResponse> call, Response<ReferralResponse> response) {
                            mContext.dismissProgressDialog();
                            if (response != null) {
                                if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body());
                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        mContext.showServerError(jObjError.getString("ErrorMessage"));
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "postReferrals", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                mContext.showServerError();
                            }
                        }

                        @Override
                        public void onFailure(Call<ReferralResponse> call, Throwable t) {
                            mContext.dismissProgressDialog();
                            mContext.showServerError(mContext.getString(R.string.something_went_wrong));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getReferralServiceAndRelation(final int requestCode, final String taskId, final String lang) {
        try {
            mContext.showProgressDialog();
            BaseApplication.getRetrofitAPI(true)
                    .getReferralServiceAndRelation(taskId, lang)
                    .enqueue(new Callback<ReferralSRResponse>() {
                        @Override
                        public void onResponse(Call<ReferralSRResponse> call,
                                               Response<ReferralSRResponse> response) {
                            mContext.dismissProgressDialog();
                            try {
                                if (response != null) {
                                    if (response.code() == 401) { // Unauthorised Access
                                        NetworkCallController controller = new NetworkCallController();
                                        controller.setListner(new NetworkResponseListner<LoginResponse>() {
                                            @Override
                                            public void onResponse(int reqCode, LoginResponse response) {
                                                try {
                                                    // delete all previous record
                                                    Realm.getDefaultInstance().beginTransaction();
                                                    Realm.getDefaultInstance().deleteAll();
                                                    Realm.getDefaultInstance().commitTransaction();

                                                    // add new record
                                                    Realm.getDefaultInstance().beginTransaction();
                                                    Realm.getDefaultInstance().copyToRealmOrUpdate(response);
                                                    Realm.getDefaultInstance().commitTransaction();
                                                    getReferrals(requestCode, taskId);
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                            @Override
                                            public void onFailure(int requestCode) {

                                            }
                                        });
                                        controller.refreshToken(100, getRefreshToken());
                                    } else if (response.body() != null) {
                                        mListner.onResponse(requestCode, response.body().getData());

                                    } else if (response.errorBody() != null) {
                                        try {
                                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                                            mContext.showServerError(jObjError.getString("ErrorMessage"));
                                            RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                            if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                                String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                                String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                                String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                                AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getReferrals", lineNo, userName, DeviceName);
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } else {
                                    mContext.showServerError();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<ReferralSRResponse> call, Throwable t) {
                            mContext.dismissProgressDialog();
                            mContext.showServerError(mContext.getString(R.string.something_went_wrong));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void getReferrals(final int requestCode, final String taskId) {
        try {
            mContext.showProgressDialog();
            BaseApplication.getRetrofitAPI(true)
                    .getReferrals(taskId)
                    .enqueue(new Callback<ReferralListResponse>() {
                        @Override
                        public void onResponse(Call<ReferralListResponse> call,
                                               Response<ReferralListResponse> response) {
                            mContext.dismissProgressDialog();
                            if (response != null) {
                                if (response.code() == 401) { // Unauthorised Access
                                    NetworkCallController controller = new NetworkCallController();
                                    controller.setListner(new NetworkResponseListner<LoginResponse>() {
                                        @Override
                                        public void onResponse(int reqCode, LoginResponse response) {
                                            try {
                                                // delete all previous record
                                                Realm.getDefaultInstance().beginTransaction();
                                                Realm.getDefaultInstance().deleteAll();
                                                Realm.getDefaultInstance().commitTransaction();

                                                // add new record
                                                Realm.getDefaultInstance().beginTransaction();
                                                Realm.getDefaultInstance().copyToRealmOrUpdate(response);
                                                Realm.getDefaultInstance().commitTransaction();
                                                getReferrals(requestCode, taskId);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void onFailure(int requestCode) {

                                        }
                                    });
                                    controller.refreshToken(100, getRefreshToken());
                                } else if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body().getData());

                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        mContext.showServerError(jObjError.getString("ErrorMessage"));
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getReferrals", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                mContext.showServerError();
                            }
                        }

                        @Override
                        public void onFailure(Call<ReferralListResponse> call, Throwable t) {
                            mContext.dismissProgressDialog();
                            mContext.showServerError(mContext.getString(R.string.something_went_wrong));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getDeleteReferrals(final int requestCode, ReferralDeleteRequest request) {
        try {
            mContext.showProgressDialog();
            BaseApplication.getRetrofitAPI(true)
                    .getDeleteReferrals(request)
                    .enqueue(new Callback<ReferralResponse>() {
                        @Override
                        public void onResponse(Call<ReferralResponse> call, Response<ReferralResponse> response) {
                            mContext.dismissProgressDialog();
                            if (response != null) {
                                if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body());
                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        mContext.showServerError(jObjError.getString("ErrorMessage"));
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getDeleteReferrals", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                mContext.showServerError();
                            }
                        }

                        @Override
                        public void onFailure(Call<ReferralResponse> call, Throwable t) {
                            mContext.dismissProgressDialog();
                            mContext.showServerError(mContext.getString(R.string.something_went_wrong));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void postFeedbackLink(final int requestCode, FeedbackRequest request) {
        try {
            mContext.showProgressDialog();
            BaseApplication.getRetrofitAPI(true)
                    .postFeedBackLink(request)
                    .enqueue(new Callback<FeedbackResponse>() {
                        @Override
                        public void onResponse(Call<FeedbackResponse> call, Response<FeedbackResponse> response) {
                            mContext.dismissProgressDialog();
                            if (response != null) {
                                if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body());
                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "postFeedbackLink", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<FeedbackResponse> call, Throwable t) {
                            mContext.dismissProgressDialog();
                            mContext.showServerError(mContext.getString(R.string.something_went_wrong));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void postAttachments(final int requestCode, PostAttachmentRequest request) {
        try {
            mContext.showProgressDialog();
            BaseApplication.getRetrofitAPI(true)
                    .postAttachments(request)
                    .enqueue(new Callback<PostAttachmentResponse>() {
                        @Override
                        public void onResponse(Call<PostAttachmentResponse> call, Response<PostAttachmentResponse> response) {
                            mContext.dismissProgressDialog();
                            if (response != null) {
                                if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body());
                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "postAttachments", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                mContext.dismissProgressDialog();
                            }
                        }

                        @Override
                        public void onFailure(Call<PostAttachmentResponse> call, Throwable t) {
                            mContext.dismissProgressDialog();
                            mContext.showServerError(mContext.getString(R.string.something_went_wrong));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getAttachments(final int requestCode, final String taskId, final String userId) {
        try {
            mContext.showProgressDialog();
            BaseApplication.getRetrofitAPI(true)
                    .getAttachments(userId, taskId)
                    .enqueue(new Callback<GetAttachmentResponse>() {
                        @Override
                        public void onResponse(Call<GetAttachmentResponse> call,
                                               Response<GetAttachmentResponse> response) {
                            mContext.dismissProgressDialog();
                            if (response != null) {
                                if (response.code() == 401) { // Unauthorised Access
                                    NetworkCallController controller = new NetworkCallController();
                                    controller.setListner(new NetworkResponseListner<LoginResponse>() {
                                        @Override
                                        public void onResponse(int reqCode, LoginResponse response) {
                                            try {
                                                // delete all previous record
                                                Realm.getDefaultInstance().beginTransaction();
                                                Realm.getDefaultInstance().deleteAll();
                                                Realm.getDefaultInstance().commitTransaction();

                                                // add new record
                                                Realm.getDefaultInstance().beginTransaction();
                                                Realm.getDefaultInstance().copyToRealmOrUpdate(response);
                                                Realm.getDefaultInstance().commitTransaction();
                                                getAttachments(requestCode, userId, taskId);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void onFailure(int requestCode) {

                                        }
                                    });
                                    controller.refreshToken(100, getRefreshToken());
                                } else if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body().getData());

                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        mContext.showServerError(jObjError.getString("ErrorMessage"));
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getAttachments", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                mContext.showServerError();
                            }
                        }

                        @Override
                        public void onFailure(Call<GetAttachmentResponse> call, Throwable t) {
                            mContext.showServerError(mContext.getString(R.string.something_went_wrong));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getMSTAttachments(final int requestCode, final String userId, final String taskId, final String serviceType) {
        try {
            mContext.showProgressDialog();
            BaseApplication.getRetrofitAPI(true)
                    .getMSTAttachments(userId, taskId, serviceType)
                    .enqueue(new Callback<AttachmentMSTResponse>() {
                        @Override
                        public void onResponse(Call<AttachmentMSTResponse> call,
                                               Response<AttachmentMSTResponse> response) {
                            mContext.dismissProgressDialog();
                            if (response != null) {
                                if (response.code() == 401) { // Unauthorised Access
                                    NetworkCallController controller = new NetworkCallController();
                                    controller.setListner(new NetworkResponseListner<LoginResponse>() {
                                        @Override
                                        public void onResponse(int reqCode, LoginResponse response) {
                                            try {
                                                // delete all previous record
                                                Realm.getDefaultInstance().beginTransaction();
                                                Realm.getDefaultInstance().deleteAll();
                                                Realm.getDefaultInstance().commitTransaction();

                                                // add new record
                                                Realm.getDefaultInstance().beginTransaction();
                                                Realm.getDefaultInstance().copyToRealmOrUpdate(response);
                                                Realm.getDefaultInstance().commitTransaction();
                                                getAttachments(requestCode, userId, taskId);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void onFailure(int requestCode) {

                                        }
                                    });
                                    controller.refreshToken(100, getRefreshToken());
                                } else if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body().getData());

                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        mContext.showServerError(jObjError.getString("ErrorMessage"));
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getAttachments", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                mContext.showServerError();
                            }
                        }

                        @Override
                        public void onFailure(Call<AttachmentMSTResponse> call, Throwable t) {
                            mContext.showServerError(mContext.getString(R.string.something_went_wrong));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void getDeleteAttachments(final int requestCode, List<AttachmentDeleteRequest> request) {
        try {
            mContext.showProgressDialog();
            BaseApplication.getRetrofitAPI(true)
                    .getDeleteAttachments(request)
                    .enqueue(new Callback<PostAttachmentResponse>() {
                        @Override
                        public void onResponse(Call<PostAttachmentResponse> call, Response<PostAttachmentResponse> response) {
                            mContext.dismissProgressDialog();
                            if (response != null) {
                                if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body());
                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        mContext.showServerError(jObjError.getString("ErrorMessage"));
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getDeleteAttachments", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                mContext.showServerError();
                            }
                        }

                        @Override
                        public void onFailure(Call<PostAttachmentResponse> call, Throwable t) {
                            mContext.dismissProgressDialog();
                            mContext.showServerError(mContext.getString(R.string.something_went_wrong));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void updateTasks(final int requestCode, UpdateTasksRequest request, final Activity context, ProgressDialog progress) {
        try {
            BaseApplication.getRetrofitAPI(true)
                    .updateTasks(request)
                    .enqueue(new Callback<UpdateTaskResponse>() {
                        @Override
                        public void onResponse(Call<UpdateTaskResponse> call, Response<UpdateTaskResponse> response) {
                            try {
                                progress.dismiss();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (response != null) {
                                if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body());
                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "updateTasks", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<UpdateTaskResponse> call, Throwable t) {
                            try {

                                progress.dismiss();
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setMessage("Something went wrong, please try again!");
                                builder.setNeutralButton("Dismiss", (dialogInterface, i) -> dialogInterface.dismiss()).create().show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                            AppUtils.sendErrorLogs(t.getMessage(), "", "getContinueHandShake", lineNo, "", DeviceName);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void getExotelCalled(final int requestCode, String customerNo, String techNo) {
        try {
            BaseApplication.getExotelApi()
                    .getCallExotel(customerNo, techNo)
                    .enqueue(new Callback<ExotelResponse>() {
                        @Override
                        public void onResponse(Call<ExotelResponse> call, Response<ExotelResponse> response) {
                            if (response != null) {
                                if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body());
                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getExotelCalled", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ExotelResponse> call, Throwable t) {
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getExotelCalledV2(final int requestCode, String customerNo, String techNo) {
        try {
            BaseApplication.getExotelApi()
                    .getDialNumber(customerNo, techNo)
                    .enqueue(new Callback<DialingResponse>() {
                        @Override
                        public void onResponse(Call<DialingResponse> call, Response<DialingResponse> response) {
                            if (response != null) {
                                if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body());
                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getExotelCalled", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<DialingResponse> call, Throwable t) {
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void getChemicals(final int requestCode, final String taskId) {
        try {
            mContext.showProgressDialog();
            BaseApplication.getRetrofitAPI(true)
                    .getChemicals(taskId)
                    .enqueue(new Callback<ChemicalResponse>() {
                        @Override
                        public void onResponse(Call<ChemicalResponse> call, Response<ChemicalResponse> response) {
                            mContext.dismissProgressDialog();
                            if (response != null) {
                                if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body().getData());
                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        mContext.showServerError(jObjError.getString("ErrorMessage"));
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getChemicals", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                mContext.showServerError();
                            }
                        }

                        @Override
                        public void onFailure(Call<ChemicalResponse> call, Throwable t) {
                            mContext.dismissProgressDialog();
                            mContext.showServerError(mContext.getString(R.string.something_went_wrong));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void getMSTChemicals(final int requestCode, final String taskId) {
        try {
            mContext.showProgressDialog();
            BaseApplication.getRetrofitAPI(true)
                    .getMSTChemicals(taskId)
                    .enqueue(new Callback<ChemicalResponse>() {
                        @Override
                        public void onResponse(Call<ChemicalResponse> call, Response<ChemicalResponse> response) {
                            mContext.dismissProgressDialog();
                            if (response != null) {
                                if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body().getData());
                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        mContext.showServerError(jObjError.getString("ErrorMessage"));
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getChemicals", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                mContext.showServerError();
                            }
                        }

                        @Override
                        public void onFailure(Call<ChemicalResponse> call, Throwable t) {
                            mContext.dismissProgressDialog();
                            mContext.showServerError(mContext.getString(R.string.something_went_wrong));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void getLogout(final int requestCode, final String UserId, final HomeActivity context) {
        try {
            BaseApplication.getRetrofitAPI(true)
                    .getLogout(UserId)
                    .enqueue(new Callback<LogoutResponse>() {
                        @Override
                        public void onResponse(Call<LogoutResponse> call, Response<LogoutResponse> response) {
                            if (response != null) {
                                if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body());
                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getLogout", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<LogoutResponse> call, Throwable t) {
                            Toast.makeText(context, "Logout failed! please try again...", Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void postResourceProfilePic(final int requestCode, final ProfilePicRequest request) {
        try {
            mContext.showProgressDialog();
            BaseApplication.getRetrofitAPI(true)
                    .getProfilePic(request)
                    .enqueue(new Callback<HandShakeResponse>() {
                        @Override
                        public void onResponse(Call<HandShakeResponse> call,
                                               Response<HandShakeResponse> response) {
                            mContext.dismissProgressDialog();

                            if (response != null) {
                                if (response.code() == 401) { // Unauthorised Access
                                    NetworkCallController controller = new NetworkCallController();
                                    controller.setListner(new NetworkResponseListner<LoginResponse>() {
                                        @Override
                                        public void onResponse(int reqCode, LoginResponse response) {
                                            try {
                                                // delete all previous record
                                                Realm.getDefaultInstance().beginTransaction();
                                                Realm.getDefaultInstance().deleteAll();
                                                Realm.getDefaultInstance().commitTransaction();

                                                // add new record
                                                Realm.getDefaultInstance().beginTransaction();
                                                Realm.getDefaultInstance().copyToRealmOrUpdate(response);
                                                Realm.getDefaultInstance().commitTransaction();
                                                postResourceProfilePic(requestCode, request);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void onFailure(int requestCode) {

                                        }
                                    });
                                    controller.refreshToken(100, getRefreshToken());
                                } else if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body());

                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "postResourceProfilePic", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<HandShakeResponse> call, Throwable t) {
                            mContext.dismissProgressDialog();
                            mContext.showServerError(mContext.getString(R.string.something_went_wrong));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getTechAttendance(final int requestCode, final AttendanceRequest request) {
        try {
            mContext.showProgressDialog();
            BaseApplication.getRetrofitAPI(true)
                    .getTechAttendance(request)
                    .enqueue(new Callback<SelfAssessmentResponse>() {
                        @Override
                        public void onResponse(Call<SelfAssessmentResponse> call,
                                               Response<SelfAssessmentResponse> response) {
                            mContext.dismissProgressDialog();
                            if (response != null) {
                                if (response.code() == 401) { // Unauthorised Access
                                    NetworkCallController controller = new NetworkCallController();
                                    controller.setListner(new NetworkResponseListner<LoginResponse>() {
                                        @Override
                                        public void onResponse(int reqCode, LoginResponse response) {
                                            try {
                                                // delete all previous record
                                                Realm.getDefaultInstance().beginTransaction();
                                                Realm.getDefaultInstance().deleteAll();
                                                Realm.getDefaultInstance().commitTransaction();

                                                // add new record
                                                Realm.getDefaultInstance().beginTransaction();
                                                Realm.getDefaultInstance().copyToRealmOrUpdate(response);
                                                Realm.getDefaultInstance().commitTransaction();
                                                getTechAttendance(requestCode, request);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void onFailure(int requestCode) {

                                        }
                                    });
                                    controller.refreshToken(100, getRefreshToken());
                                } else if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body());

                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = " TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = " DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            Log.e("Error Log", userName + lineNo + DeviceName);
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getTechAttendance", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<SelfAssessmentResponse> call, Throwable t) {
                            mContext.dismissProgressDialog();
                            mContext.showServerError(mContext.getString(R.string.something_went_wrong));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getTrainingVideos(final int requestCode) {
        try {
            mContext.showProgressDialog();
            BaseApplication.getRetrofitAPI(true)
                    .getTrainingVideos()
                    .enqueue(new Callback<TrainingResponse>() {
                        @Override
                        public void onResponse(Call<TrainingResponse> call, Response<TrainingResponse> response) {
                            mContext.dismissProgressDialog();
                            if (response != null) {
                                if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body().getData());
                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getTrainingVideos", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                mContext.showServerError();
                            }
                        }

                        @Override
                        public void onFailure(Call<TrainingResponse> call, Throwable t) {
                            mContext.dismissProgressDialog();
                            mContext.showServerError(mContext.getString(R.string.something_went_wrong));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*[Error Log]*/

    public void sendErrorLog(final int requestCode, final ErrorLoggerModel request) {
        try {
            BaseApplication.getLoggerApi()
                    .sendErrorLog(request)
                    .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call,
                                               Response<String> response) {
                            if (response != null) {
                                if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body());
                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "sendErrorLog", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*[update APP]*/

    public void getUpdateApp(final int requestCode) {
        try {
            BaseApplication.getRetrofitAPI(true)
                    .getUpdateApp()
                    .enqueue(new Callback<UpdateResponse>() {
                        @Override
                        public void onResponse(Call<UpdateResponse> call, Response<UpdateResponse> response) {
                            if (response != null) {
                                if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body().getData());
                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getUpdateApp", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                            }
                        }

                        @Override
                        public void onFailure(Call<UpdateResponse> call, Throwable t) {

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*[Send Payment Link]*/

    public void sendPaymentLink(final int requestCode, final PaymentLinkRequest request) {
        try {
            mContext.showProgressDialog();
            BaseApplication.getRetrofitAPI(true)
                    .sendPaymentLink(request)
                    .enqueue(new Callback<PaymentLinkResponse>() {
                        @Override
                        public void onResponse(Call<PaymentLinkResponse> call,
                                               Response<PaymentLinkResponse> response) {
                            mContext.dismissProgressDialog();

                            if (response != null) {
                                if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body());
                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "sendPaymentLink", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                mContext.showServerError();
                            }
                        }

                        @Override
                        public void onFailure(Call<PaymentLinkResponse> call, Throwable t) {
                            mContext.dismissProgressDialog();
                            mContext.showServerError(mContext.getString(R.string.something_went_wrong));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getGroomingTechnicians(final int requestCode, final String userId) {
        try {
            mContext.showProgressDialog();
            BaseApplication.getRetrofitAPI(true)
                    .getGroomingTechnicians(userId)
                    .enqueue(new Callback<TechGroomingResponse>() {
                        @Override
                        public void onResponse(Call<TechGroomingResponse> call,
                                               Response<TechGroomingResponse> response) {
                            mContext.dismissProgressDialog();
                            if (response != null) {
                                if (response.code() == 401) { // Unauthorised Access
                                    NetworkCallController controller = new NetworkCallController();
                                    controller.setListner(new NetworkResponseListner<LoginResponse>() {
                                        @Override
                                        public void onResponse(int reqCode, LoginResponse response) {
                                            try {
                                                // delete all previous record
                                                Realm.getDefaultInstance().beginTransaction();
                                                Realm.getDefaultInstance().deleteAll();
                                                Realm.getDefaultInstance().commitTransaction();

                                                // add new record
                                                Realm.getDefaultInstance().beginTransaction();
                                                Realm.getDefaultInstance().copyToRealmOrUpdate(response);
                                                Realm.getDefaultInstance().commitTransaction();
                                                getGroomingTechnicians(requestCode, userId);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void onFailure(int requestCode) {

                                        }
                                    });
                                    controller.refreshToken(100, getRefreshToken());
                                } else if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body().getData());

                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        mContext.showServerError(jObjError.getString("ErrorMessage"));
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getGroomingTechnicians", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<TechGroomingResponse> call, Throwable t) {
                            mContext.dismissProgressDialog();
                            mContext.showServerError(mContext.getString(R.string.something_went_wrong));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*[Post Grooming Image]*/

    public void postGroomingImage(final int requestCode, final TechGroomingRequest request) {
        try {
            mContext.showProgressDialog();
            BaseApplication.getRetrofitAPI(true)
                    .postGroomingImage(request)
                    .enqueue(new Callback<BasicResponse>() {
                        @Override
                        public void onResponse(Call<BasicResponse> call,
                                               Response<BasicResponse> response) {
                            mContext.dismissProgressDialog();
                            if (response != null) {

                                if (response.code() == 401) { // Unauthorised Access
                                    NetworkCallController controller = new NetworkCallController();
                                    controller.setListner(new NetworkResponseListner<LoginResponse>() {
                                        @Override
                                        public void onResponse(int reqCode, LoginResponse response) {
                                            try {
                                                // delete all previous record
                                                Realm.getDefaultInstance().beginTransaction();
                                                Realm.getDefaultInstance().deleteAll();
                                                Realm.getDefaultInstance().commitTransaction();

                                                // add new record
                                                Realm.getDefaultInstance().beginTransaction();
                                                Realm.getDefaultInstance().copyToRealmOrUpdate(response);
                                                Realm.getDefaultInstance().commitTransaction();
                                                postGroomingImage(requestCode, request);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void onFailure(int requestCode) {

                                        }
                                    });
                                    controller.refreshToken(100, getRefreshToken());
                                } else if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body());
                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "postGroomingImage", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                mContext.showServerError();
                            }
                        }

                        @Override
                        public void onFailure(Call<BasicResponse> call, Throwable t) {
                            mContext.dismissProgressDialog();
                            mContext.showServerError(mContext.getString(R.string.something_went_wrong));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /*[Resource Profile]*/

    public void getTechnicianProfile(final int requestCode, final String userId) {
        try {
            if (mContext != null)
                mContext.showProgressDialog();
            BaseApplication.getRetrofitAPI(true)
                    .getTechnicianProfile(userId)
                    .enqueue(new Callback<TechnicianProfileDetails>() {
                        @Override
                        public void onResponse(Call<TechnicianProfileDetails> call,
                                               Response<TechnicianProfileDetails> response) {
                            if (mContext != null)
                                mContext.dismissProgressDialog();
                            if (response != null) {
                                if (response.code() == 401) { // Unauthorised Access
                                    NetworkCallController controller = new NetworkCallController();
                                    controller.setListner(new NetworkResponseListner<LoginResponse>() {
                                        @Override
                                        public void onResponse(int reqCode, LoginResponse response) {
                                            try {
                                                // delete all previous record
                                                Realm.getDefaultInstance().beginTransaction();
                                                Realm.getDefaultInstance().deleteAll();
                                                Realm.getDefaultInstance().commitTransaction();

                                                // add new record
                                                Realm.getDefaultInstance().beginTransaction();
                                                Realm.getDefaultInstance().copyToRealmOrUpdate(response);
                                                Realm.getDefaultInstance().commitTransaction();
                                                getTechnicianProfile(requestCode, userId);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void onFailure(int requestCode) {

                                        }
                                    });
                                    controller.refreshToken(100, getRefreshToken());
                                } else if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body().getData());

                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getTechnicianProfile", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<TechnicianProfileDetails> call, Throwable t) {
                            if (mContext != null) {
                                mContext.dismissProgressDialog();
                                mContext.showServerError(mContext.getString(R.string.something_went_wrong));
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /*[Resource Incentive]*/

    public void getTechnicianIncentive(final int requestCode, final String userId) {
        try {
            if (mContext != null)
                mContext.showProgressDialog();

            BaseApplication.getRetrofitAPI(true)
                    .getTechnicianIncentive(userId)
                    .enqueue(new Callback<IncentiveResponse>() {
                        @Override
                        public void onResponse(Call<IncentiveResponse> call,
                                               Response<IncentiveResponse> response) {
                            if (mContext != null)
                                mContext.dismissProgressDialog();
                            if (response != null) {
                                if (response.code() == 401) { // Unauthorised Access
                                    NetworkCallController controller = new NetworkCallController();
                                    controller.setListner(new NetworkResponseListner<LoginResponse>() {
                                        @Override
                                        public void onResponse(int reqCode, LoginResponse response) {
                                            // delete all previous record
                                            Realm.getDefaultInstance().beginTransaction();
                                            Realm.getDefaultInstance().deleteAll();
                                            Realm.getDefaultInstance().commitTransaction();

                                            // add new record
                                            Realm.getDefaultInstance().beginTransaction();
                                            Realm.getDefaultInstance().copyToRealmOrUpdate(response);
                                            Realm.getDefaultInstance().commitTransaction();
                                            getTechnicianProfile(requestCode, userId);
                                        }

                                        @Override
                                        public void onFailure(int requestCode) {

                                        }
                                    });
                                    controller.refreshToken(100, getRefreshToken());
                                } else if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body().getData());

                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getTechnicianProfile", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<IncentiveResponse> call, Throwable t) {
                            if (mContext != null) {
                                mContext.dismissProgressDialog();
                                mContext.showServerError(mContext.getString(R.string.something_went_wrong));
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*[Resource Incentive]*/

    public void getResourceIncentive(final int requestCode, final String userId) {
        try {
            if (mContext != null)
                mContext.showProgressDialog();

            BaseApplication.getRetrofitAPI(true)
                    .getResourceIncentive(userId)
                    .enqueue(new Callback<IncentiveResponse>() {
                        @Override
                        public void onResponse(Call<IncentiveResponse> call,
                                               Response<IncentiveResponse> response) {
                            if (mContext != null)
                                mContext.dismissProgressDialog();
                            if (response != null) {
                                if (response.code() == 401) { // Unauthorised Access
                                    NetworkCallController controller = new NetworkCallController();
                                    controller.setListner(new NetworkResponseListner<LoginResponse>() {
                                        @Override
                                        public void onResponse(int reqCode, LoginResponse response) {
                                            // delete all previous record
                                            Realm.getDefaultInstance().beginTransaction();
                                            Realm.getDefaultInstance().deleteAll();
                                            Realm.getDefaultInstance().commitTransaction();

                                            // add new record
                                            Realm.getDefaultInstance().beginTransaction();
                                            Realm.getDefaultInstance().copyToRealmOrUpdate(response);
                                            Realm.getDefaultInstance().commitTransaction();
                                            getTechnicianProfile(requestCode, userId);
                                        }

                                        @Override
                                        public void onFailure(int requestCode) {

                                        }
                                    });
                                    controller.refreshToken(100, getRefreshToken());
                                } else if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body().getData());

                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getTechnicianProfile", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<IncentiveResponse> call, Throwable t) {
                            if (mContext != null) {
                                mContext.dismissProgressDialog();
                                mContext.showServerError(mContext.getString(R.string.something_went_wrong));
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*[Get Jeopardy Reasons]*/

    public void getJeopardyReasons(final int requestCode, final String taskId, final String language) {
        try {
            mContext.showProgressDialog();
            BaseApplication.getRetrofitAPI(true)
                    .getJeopardyReasons(taskId, language)
                    .enqueue(new Callback<JeopardyReasonModel>() {
                        @Override
                        public void onResponse(Call<JeopardyReasonModel> call,
                                               Response<JeopardyReasonModel> response) {
                            mContext.dismissProgressDialog();
                            if (response != null) {
                                if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body().getData());

                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        mContext.showServerError(jObjError.getString("ErrorMessage"));
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getJeopardyReasons", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                mContext.showServerError();
                            }
                        }

                        @Override
                        public void onFailure(Call<JeopardyReasonModel> call, Throwable t) {
                            mContext.dismissProgressDialog();
                            mContext.showServerError(mContext.getString(R.string.something_went_wrong));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*[CWF JEOPARDY ]*/

    public void postCWFJepoardy(final int requestCode, final CWFJeopardyRequest request) {
        try {
            mContext.showProgressDialog();
            BaseApplication.getRetrofitAPI(true)
                    .postCWFJeopardy(request)
                    .enqueue(new Callback<CWFJeopardyResponse>() {
                        @Override
                        public void onResponse(Call<CWFJeopardyResponse> call,
                                               Response<CWFJeopardyResponse> response) {
                            mContext.dismissProgressDialog();
                            if (response != null) {
                                if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body());
                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "postCWFJepoardy", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                mContext.showServerError();
                            }
                        }

                        @Override
                        public void onFailure(Call<CWFJeopardyResponse> call, Throwable t) {
                            mContext.dismissProgressDialog();
                            mContext.showServerError(mContext.getString(R.string.something_went_wrong));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*[CWF JEOPARDY ]*/

    public void insertLessPaymentJeopardy(final int requestCode, final CWFJeopardyRequest request) {
        try {
            mContext.showProgressDialog();
            BaseApplication.getRetrofitAPI(true)
                    .insertLessPaymentJeopardy(request)
                    .enqueue(new Callback<CWFJeopardyResponse>() {
                        @Override
                        public void onResponse(Call<CWFJeopardyResponse> call,
                                               Response<CWFJeopardyResponse> response) {
                            mContext.dismissProgressDialog();
                            if (response != null) {
                                if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body());
                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "postCWFJepoardy", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                mContext.showServerError();
                            }
                        }

                        @Override
                        public void onFailure(Call<CWFJeopardyResponse> call, Throwable t) {
                            mContext.dismissProgressDialog();
                            mContext.showServerError(mContext.getString(R.string.something_went_wrong));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getAttendanceDetail(final int requestCode, final String resourceId) {
        try {
            mContext.showProgressDialog();
            BaseApplication.getRetrofitAPI(true)
                    .getAttendanceDetail(resourceId)
                    .enqueue(new Callback<AttendanceDetailResponse>() {
                        @Override
                        public void onResponse(Call<AttendanceDetailResponse> call, Response<AttendanceDetailResponse> response) {
                            mContext.dismissProgressDialog();
                            if (response != null) {
                                if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body().getData());
                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        mContext.showServerError(jObjError.getString("ErrorMessage"));
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getChemicals", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                mContext.showServerError();
                            }
                        }

                        @Override
                        public void onFailure(Call<AttendanceDetailResponse> call, Throwable t) {
                            mContext.dismissProgressDialog();
                            mContext.showServerError(mContext.getString(R.string.something_went_wrong));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void getVoucherCode(final int requestCode, final String resourceId) {
        try {
            mContext.showProgressDialog();

            BaseApplication.getRetrofitAPI(true)
                    .getTechnicianReferralCode(resourceId)
                    .enqueue(new Callback<VoucherResponseMain>() {
                        @Override
                        public void onResponse(Call<VoucherResponseMain> call,
                                               Response<VoucherResponseMain> response) {
                            mContext.dismissProgressDialog();
                            if (response != null) {
                                if (response.code() == 401) { // Unauthorised Access
                                    NetworkCallController controller = new NetworkCallController();
                                    controller.setListner(new NetworkResponseListner<LoginResponse>() {
                                        @Override
                                        public void onResponse(int reqCode, LoginResponse response) {
                                            // delete all previous record
                                            Realm.getDefaultInstance().beginTransaction();
                                            Realm.getDefaultInstance().deleteAll();
                                            Realm.getDefaultInstance().commitTransaction();

                                            // add new record
                                            Realm.getDefaultInstance().beginTransaction();
                                            Realm.getDefaultInstance().copyToRealmOrUpdate(response);
                                            Realm.getDefaultInstance().commitTransaction();
                                            getVoucherCode(requestCode, resourceId);
                                        }

                                        @Override
                                        public void onFailure(int requestCode) {

                                        }
                                    });
                                    controller.refreshToken(100, getRefreshToken());
                                } else if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body().getData());
                                } else {
                                    if (response.errorBody() != null) {
                                        try {
                                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                                            mContext.showServerError(jObjError.getString("Message"));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            } else {
                                mContext.showServerError("error");
                            }
                        }

                        @Override
                        public void onFailure(Call<VoucherResponseMain> call, Throwable t) {
                            mContext.dismissProgressDialog();
                            mContext.showServerError(mContext.getString(R.string.something_went_wrong));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void getStartingVideos(final int requestCode, final String userId) {
        try {
            mContext.showProgressDialog();
            BaseApplication.getRetrofitAPI(true)
                    .getStartingVideos(userId)
                    .enqueue(new Callback<WelcomeVideoResponse>() {
                        @Override
                        public void onResponse(Call<WelcomeVideoResponse> call, Response<WelcomeVideoResponse> response) {
                            mContext.dismissProgressDialog();
                            if (response != null) {
                                if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body().getData());
                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        mContext.showServerError(jObjError.getString("ErrorMessage"));
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getChemicals", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                mContext.showServerError();
                            }
                        }

                        @Override
                        public void onFailure(Call<WelcomeVideoResponse> call, Throwable t) {
                            mContext.dismissProgressDialog();
                            mContext.showServerError(mContext.getString(R.string.something_went_wrong));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getOnSiteOTP(final int requestCode, final String resourceId,
                             final String taskId, final String userName,
                             final String mobileNo) {
        try {
            mContext.showProgressDialog();
            BaseApplication.getRetrofitAPI(true)
                    .getOnsiteOTP(resourceId, taskId, userName, mobileNo)
                    .enqueue(new Callback<OnSiteOtpResponse>() {
                        @Override
                        public void onResponse(Call<OnSiteOtpResponse> call, Response<OnSiteOtpResponse> response) {
                            mContext.dismissProgressDialog();
                            if (response != null) {
                                if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body());
                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        mContext.showServerError(jObjError.getString("ErrorMessage"));
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getChemicals", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                mContext.showServerError();
                            }
                        }

                        @Override
                        public void onFailure(Call<OnSiteOtpResponse> call, Throwable t) {
                            mContext.dismissProgressDialog();
                            mContext.showServerError(mContext.getString(R.string.something_went_wrong));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getValidateCompletionTime(final int requestCode, final String date_time,
                                          final String taskId) {
        try {
            mContext.showProgressDialog();
            BaseApplication.getRetrofitAPI(true)
                    .getValidateCompletionTime(date_time, taskId)
                    .enqueue(new Callback<BasicResponse>() {
                        @Override
                        public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                            mContext.dismissProgressDialog();
                            if (response != null) {
                                if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body());
                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        mContext.showServerError(jObjError.getString("ErrorMessage"));
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getChemicals", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                mContext.showServerError();
                            }
                        }

                        @Override
                        public void onFailure(Call<BasicResponse> call, Throwable t) {
                            mContext.dismissProgressDialog();
                            mContext.showServerError(mContext.getString(R.string.something_went_wrong));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void getBanksName(final int requestCode) {
        try {
            if (mContext != null)
                mContext.showProgressDialog();
            BaseApplication.getRetrofitAPI(true)
                    .getBanksName()
                    .enqueue(new Callback<BankResponse>() {
                        @Override
                        public void onResponse(Call<BankResponse> call, Response<BankResponse> response) {
                            if (mContext != null)
                                mContext.dismissProgressDialog();
                            if (response != null) {
                                if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body().getData());
                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        if (mContext != null)
                                            mContext.showServerError(jObjError.getString("ErrorMessage"));
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getChemicals", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                if (mContext != null)
                                    mContext.showServerError();
                            }
                        }

                        @Override
                        public void onFailure(Call<BankResponse> call, Throwable t) {
                            if (mContext != null) {
                                mContext.dismissProgressDialog();
                                mContext.showServerError(mContext.getString(R.string.something_went_wrong));
                            }

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getOnSiteAccounts(final int requestCode, final String resourceId) {
        try {
            mContext.showProgressDialog();
            BaseApplication.getRetrofitAPI(true)
                    .getOnsiteAccounts(resourceId)
                    .enqueue(new Callback<OnSiteAccountResponse>() {
                        @Override
                        public void onResponse(Call<OnSiteAccountResponse> call, Response<OnSiteAccountResponse> response) {
                            mContext.dismissProgressDialog();
                            if (response != null) {
                                if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body().getData());
                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        mContext.showServerError(jObjError.getString("ErrorMessage"));
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getChemicals", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                mContext.showServerError();
                            }
                        }

                        @Override
                        public void onFailure(Call<OnSiteAccountResponse> call, Throwable t) {
                            mContext.dismissProgressDialog();
                            mContext.showServerError(mContext.getString(R.string.something_went_wrong));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getAccountAreaActivity(final int requestCode, final String accountId, final String resourceId) {
        try {
            mContext.showProgressDialog();
            BaseApplication.getRetrofitAPI(true)
                    .getAccountAreaActivity(accountId, resourceId)
                    .enqueue(new Callback<OnSiteAreaResponse>() {
                        @Override
                        public void onResponse(Call<OnSiteAreaResponse> call, Response<OnSiteAreaResponse> response) {
                            mContext.dismissProgressDialog();
                            if (response != null) {
                                if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body());
                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        mContext.showServerError(jObjError.getString("ErrorMessage"));
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getChemicals", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                mContext.showServerError();
                            }
                        }

                        @Override
                        public void onFailure(Call<OnSiteAreaResponse> call, Throwable t) {
                            mContext.dismissProgressDialog();
                            mContext.showServerError(mContext.getString(R.string.something_went_wrong));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getSaveAccountAreaActivity(final int requestCode, final SaveAccountAreaRequest request) {
        try {
            mContext.showProgressDialog();
            BaseApplication.getRetrofitAPI(true)
                    .getSaveAccountAreaActivity(request)
                    .enqueue(new Callback<SaveAccountAreaResponse>() {
                        @Override
                        public void onResponse(Call<SaveAccountAreaResponse> call, Response<SaveAccountAreaResponse> response) {
                            mContext.dismissProgressDialog();
                            if (response != null) {
                                if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body());
                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        mContext.showServerError(jObjError.getString("ErrorMessage"));
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getChemicals", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                mContext.showServerError();
                            }
                        }

                        @Override
                        public void onFailure(Call<SaveAccountAreaResponse> call, Throwable t) {
                            mContext.dismissProgressDialog();
                            mContext.showServerError(mContext.getString(R.string.something_went_wrong));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getRecentAccountAreaActivity(final int requestCode, final String accountId, final String resourceId, final Boolean isGrouped) {
        try {
            mContext.showProgressDialog();
            BaseApplication.getRetrofitAPI(true)
                    .getRecentAccountAreaActivity(accountId, resourceId, isGrouped)
                    .enqueue(new Callback<OnSiteRecentResponse>() {
                        @Override
                        public void onResponse(Call<OnSiteRecentResponse> call, Response<OnSiteRecentResponse> response) {
                            mContext.dismissProgressDialog();
                            if (response != null) {
                                if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body().getData());
                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        mContext.showServerError(jObjError.getString("ErrorMessage"));
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getChemicals", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                mContext.showServerError();
                            }
                        }

                        @Override
                        public void onFailure(Call<OnSiteRecentResponse> call, Throwable t) {
                            mContext.dismissProgressDialog();
                            mContext.showServerError(mContext.getString(R.string.something_went_wrong));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void getNotDoneReasons(final int requestCode) {
        try {
            mContext.showProgressDialog();
            BaseApplication.getRetrofitAPI(true)
                    .getNotDoneReasons()
                    .enqueue(new Callback<BankResponse>() {
                        @Override
                        public void onResponse(Call<BankResponse> call, Response<BankResponse> response) {
                            mContext.dismissProgressDialog();
                            if (response != null) {
                                if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body().getData());
                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        mContext.showServerError(jObjError.getString("ErrorMessage"));
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getChemicals", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                mContext.showServerError();
                            }
                        }

                        @Override
                        public void onFailure(Call<BankResponse> call, Throwable t) {
                            mContext.dismissProgressDialog();
                            mContext.showServerError(mContext.getString(R.string.something_went_wrong));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getDeleteOnSiteTasks(final int requestCode, final Integer activityId) {
        try {
            mContext.showProgressDialog();
            BaseApplication.getRetrofitAPI(true)
                    .getDeleteOnSiteTasks(activityId)
                    .enqueue(new Callback<SaveAccountAreaResponse>() {
                        @Override
                        public void onResponse(Call<SaveAccountAreaResponse> call, Response<SaveAccountAreaResponse> response) {
                            mContext.dismissProgressDialog();
                            if (response != null) {
                                if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body());
                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        mContext.showServerError(jObjError.getString("ErrorMessage"));
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getChemicals", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                mContext.showServerError();
                            }
                        }

                        @Override
                        public void onFailure(Call<SaveAccountAreaResponse> call, Throwable t) {
                            mContext.dismissProgressDialog();
                            mContext.showServerError(mContext.getString(R.string.something_went_wrong));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getTechnicianJobSummary(final int requestCode, final String userId) {
        try {
            mContext.showProgressDialog();
            BaseApplication.getRetrofitAPI(true)
                    .getTechnicianJobSummary(userId)
                    .enqueue(new Callback<ChemicalCountResponse>() {
                        @Override
                        public void onResponse(Call<ChemicalCountResponse> call,
                                               Response<ChemicalCountResponse> response) {
                            mContext.dismissProgressDialog();
                            if (response != null) {
                                if (response.code() == 401) { // Unauthorised Access
                                    NetworkCallController controller = new NetworkCallController();
                                    controller.setListner(new NetworkResponseListner<LoginResponse>() {
                                        @Override
                                        public void onResponse(int reqCode, LoginResponse response) {
                                            // delete all previous record
                                            Realm.getDefaultInstance().beginTransaction();
                                            Realm.getDefaultInstance().deleteAll();
                                            Realm.getDefaultInstance().commitTransaction();

                                            // add new record
                                            Realm.getDefaultInstance().beginTransaction();
                                            Realm.getDefaultInstance().copyToRealmOrUpdate(response);
                                            Realm.getDefaultInstance().commitTransaction();
                                            getTechnicianJobSummary(requestCode, userId);
                                        }

                                        @Override
                                        public void onFailure(int requestCode) {

                                        }
                                    });
                                    controller.refreshToken(100, getRefreshToken());
                                } else if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body().getData());

                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        mContext.showServerError(jObjError.getString("ErrorMessage"));
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getGroomingTechnicians", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ChemicalCountResponse> call, Throwable t) {
                            mContext.dismissProgressDialog();
                            mContext.showServerError(mContext.getString(R.string.something_went_wrong));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getResourceProfilePicture(final int requestCode, final String userId) {
        try {
            if (mContext != null)
                mContext.showProgressDialog();
            BaseApplication.getRetrofitAPI(true)
                    .getResourceProfilePicture(userId)
                    .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call,
                                               Response<String> response) {
                            if (mContext != null)
                                mContext.dismissProgressDialog();
                            if (response != null) {
                                if (response.code() == 401) { // Unauthorised Access
                                    NetworkCallController controller = new NetworkCallController();
                                    controller.setListner(new NetworkResponseListner<LoginResponse>() {
                                        @Override
                                        public void onResponse(int reqCode, LoginResponse response) {
                                            // delete all previous record
                                            Realm.getDefaultInstance().beginTransaction();
                                            Realm.getDefaultInstance().deleteAll();
                                            Realm.getDefaultInstance().commitTransaction();

                                            // add new record
                                            Realm.getDefaultInstance().beginTransaction();
                                            Realm.getDefaultInstance().copyToRealmOrUpdate(response);
                                            Realm.getDefaultInstance().commitTransaction();
                                            getTechnicianJobSummary(requestCode, userId);
                                        }

                                        @Override
                                        public void onFailure(int requestCode) {

                                        }
                                    });
                                    controller.refreshToken(100, getRefreshToken());
                                } else if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body());

                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        if (mContext != null)
                                            mContext.showServerError(jObjError.getString("ErrorMessage"));
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getGroomingTechnicians", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            if (mContext != null) {
                                mContext.dismissProgressDialog();
                                mContext.showServerError(mContext.getString(R.string.something_went_wrong));
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void getResourceRedeemedData(final int requestCode, final String userId) {
        try {
            if (mContext != null)
                mContext.showProgressDialog();
            BaseApplication.getRetrofitAPI(true)
                    .getResourceRedeemedData(userId)
                    .enqueue(new Callback<RewardsResponse>() {
                        @Override
                        public void onResponse(Call<RewardsResponse> call,
                                               Response<RewardsResponse> response) {
                            if (mContext != null)
                                mContext.dismissProgressDialog();
                            if (response != null) {
                                if (response.code() == 401) { // Unauthorised Access
                                    NetworkCallController controller = new NetworkCallController();
                                    controller.setListner(new NetworkResponseListner<LoginResponse>() {
                                        @Override
                                        public void onResponse(int reqCode, LoginResponse response) {
                                            // delete all previous record
                                            Realm.getDefaultInstance().beginTransaction();
                                            Realm.getDefaultInstance().deleteAll();
                                            Realm.getDefaultInstance().commitTransaction();

                                            // add new record
                                            Realm.getDefaultInstance().beginTransaction();
                                            Realm.getDefaultInstance().copyToRealmOrUpdate(response);
                                            Realm.getDefaultInstance().commitTransaction();
                                            getTechnicianJobSummary(requestCode, userId);
                                        }

                                        @Override
                                        public void onFailure(int requestCode) {

                                        }
                                    });
                                    controller.refreshToken(100, getRefreshToken());
                                } else if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body().getData());

                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        if (mContext != null)
                                            mContext.showServerError(jObjError.getString("ErrorMessage"));
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getGroomingTechnicians", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<RewardsResponse> call, Throwable t) {
                            if (mContext != null) {
                                mContext.dismissProgressDialog();
                                mContext.showServerError(mContext.getString(R.string.something_went_wrong));
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void getSaveRedeemOffer(final int requestCode, final SaveRedeemRequest request) {
        try {
            mContext.showProgressDialog();
            BaseApplication.getRetrofitAPI(true)
                    .getSaveResourceRedeemData(request)
                    .enqueue(new Callback<SaveRedeemResponse>() {
                        @Override
                        public void onResponse(Call<SaveRedeemResponse> call, Response<SaveRedeemResponse> response) {
                            mContext.dismissProgressDialog();
                            if (response != null) {
                                if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body());
                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        mContext.showServerError(jObjError.getString("ErrorMessage"));
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getChemicals", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                mContext.showServerError();
                            }
                        }

                        @Override
                        public void onFailure(Call<SaveRedeemResponse> call, Throwable t) {
                            mContext.dismissProgressDialog();
                            mContext.showServerError(mContext.getString(R.string.something_went_wrong));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getNPSData(final int requestCode, final String userId) {
        try {
            mContext.showProgressDialog();
            BaseApplication.getRetrofitAPI(true)
                    .getNPSData(userId)
                    .enqueue(new Callback<NPSDataResponse>() {
                        @Override
                        public void onResponse(Call<NPSDataResponse> call,
                                               Response<NPSDataResponse> response) {
                            mContext.dismissProgressDialog();
                            if (response != null) {
                                if (response.code() == 401) { // Unauthorised Access
                                    NetworkCallController controller = new NetworkCallController();
                                    controller.setListner(new NetworkResponseListner<LoginResponse>() {
                                        @Override
                                        public void onResponse(int reqCode, LoginResponse response) {
                                            // delete all previous record
                                            Realm.getDefaultInstance().beginTransaction();
                                            Realm.getDefaultInstance().deleteAll();
                                            Realm.getDefaultInstance().commitTransaction();

                                            // add new record
                                            Realm.getDefaultInstance().beginTransaction();
                                            Realm.getDefaultInstance().copyToRealmOrUpdate(response);
                                            Realm.getDefaultInstance().commitTransaction();
                                            getNPSData(requestCode, userId);
                                        }

                                        @Override
                                        public void onFailure(int requestCode) {

                                        }
                                    });
                                    controller.refreshToken(100, getRefreshToken());
                                } else if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body().getData());

                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        mContext.showServerError(jObjError.getString("ErrorMessage"));
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getGroomingTechnicians", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<NPSDataResponse> call, Throwable t) {
                            mContext.dismissProgressDialog();
                            mContext.showServerError(mContext.getString(R.string.something_went_wrong));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getAllRewards(final int requestCode, final String userId) {
        try {
            mContext.showProgressDialog();
            BaseApplication.getRetrofitAPI(true)
                    .getAllRewards(userId)
                    .enqueue(new Callback<OffersResponse>() {
                        @Override
                        public void onResponse(Call<OffersResponse> call,
                                               Response<OffersResponse> response) {
                            mContext.dismissProgressDialog();
                            if (response != null) {
                                if (response.code() == 401) { // Unauthorised Access
                                    NetworkCallController controller = new NetworkCallController();
                                    controller.setListner(new NetworkResponseListner<LoginResponse>() {
                                        @Override
                                        public void onResponse(int reqCode, LoginResponse response) {
                                            // delete all previous record
                                            Realm.getDefaultInstance().beginTransaction();
                                            Realm.getDefaultInstance().deleteAll();
                                            Realm.getDefaultInstance().commitTransaction();

                                            // add new record
                                            Realm.getDefaultInstance().beginTransaction();
                                            Realm.getDefaultInstance().copyToRealmOrUpdate(response);
                                            Realm.getDefaultInstance().commitTransaction();
                                            getNPSData(requestCode, userId);
                                        }

                                        @Override
                                        public void onFailure(int requestCode) {

                                        }
                                    });
                                    controller.refreshToken(100, getRefreshToken());
                                } else if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body().getData());

                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        mContext.showServerError(jObjError.getString("ErrorMessage"));
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getGroomingTechnicians", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<OffersResponse> call, Throwable t) {
                            mContext.dismissProgressDialog();
                            mContext.showServerError(mContext.getString(R.string.something_went_wrong));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getRewardsWithMissedData(final int requestCode, final String userId) {
        try {
            mContext.showProgressDialog();
            BaseApplication.getRetrofitAPI(true)
                    .getRewardsWithMissedData(userId)
                    .enqueue(new Callback<OffersResponse>() {
                        @Override
                        public void onResponse(Call<OffersResponse> call,
                                               Response<OffersResponse> response) {
                            mContext.dismissProgressDialog();
                            if (response != null) {
                                if (response.code() == 401) { // Unauthorised Access
                                    NetworkCallController controller = new NetworkCallController();
                                    controller.setListner(new NetworkResponseListner<LoginResponse>() {
                                        @Override
                                        public void onResponse(int reqCode, LoginResponse response) {
                                            // delete all previous record
                                            Realm.getDefaultInstance().beginTransaction();
                                            Realm.getDefaultInstance().deleteAll();
                                            Realm.getDefaultInstance().commitTransaction();

                                            // add new record
                                            Realm.getDefaultInstance().beginTransaction();
                                            Realm.getDefaultInstance().copyToRealmOrUpdate(response);
                                            Realm.getDefaultInstance().commitTransaction();
                                            getNPSData(requestCode, userId);
                                        }

                                        @Override
                                        public void onFailure(int requestCode) {

                                        }
                                    });
                                    controller.refreshToken(100, getRefreshToken());
                                } else if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body().getData());

                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        mContext.showServerError(jObjError.getString("ErrorMessage"));
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getGroomingTechnicians", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<OffersResponse> call, Throwable t) {
                            mContext.dismissProgressDialog();
                            mContext.showServerError(mContext.getString(R.string.something_went_wrong));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void updateRewardScratch(final int requestCode, final UpdateRewardScratchRequest request) {
        try {
//            mContext.showProgressDialog();
            BaseApplication.getRetrofitAPI(true)
                    .updateRewardScratch(request)
                    .enqueue(new Callback<UpdateRewardScratchResponse>() {
                        @Override
                        public void onResponse(Call<UpdateRewardScratchResponse> call, Response<UpdateRewardScratchResponse> response) {
//                            mContext.dismissProgressDialog();
                            if (response != null) {
                                if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body());
                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
//                                        mContext.showServerError(jObjError.getString("ErrorMessage"));
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getChemicals", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<UpdateRewardScratchResponse> call, Throwable t) {
//                            mContext.dismissProgressDialog();
//                            mContext.showServerError(mContext.getString(R.string.something_went_wrong));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void uploadOnsiteImage(final int requestCode, final CovidRequest request) {
        try {
            mContext.showProgressDialog();
            BaseApplication.getRetrofitAPI(true)
                    .uploadOnsiteImage(request)
                    .enqueue(new Callback<CovidResponse>() {
                        @Override
                        public void onResponse(Call<CovidResponse> call, Response<CovidResponse> response) {
                            mContext.dismissProgressDialog();
                            if (response != null) {
                                if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body());
                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
//                                        mContext.showServerError(jObjError.getString("ErrorMessage"));
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getChemicals", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<CovidResponse> call, Throwable t) {
                            mContext.dismissProgressDialog();
                            mContext.showServerError(mContext.getString(R.string.something_went_wrong));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void uploadCheckListAttachment(final int requestCode, final UploadCheckListRequest request) {
        try {
//            mContext.showProgressDialog();
            BaseApplication.getRetrofitAPI(true)
                    .uploadCheckListAttachment(request)
                    .enqueue(new Callback<UploadCheckListResponse>() {
                        @Override
                        public void onResponse(Call<UploadCheckListResponse> call, Response<UploadCheckListResponse> response) {
//                            mContext.dismissProgressDialog();
                            if (response != null) {
                                if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body().getData());
                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
//                                        mContext.showServerError(jObjError.getString("ErrorMessage"));
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getChemicals", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<UploadCheckListResponse> call, Throwable t) {
//                            mContext.dismissProgressDialog();
//                            mContext.showServerError(mContext.getString(R.string.something_went_wrong));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void getAllRewardsHistory(final int requestCode, final String userId) {
        try {
            mContext.showProgressDialog();
            BaseApplication.getRetrofitAPI(true)
                    .getRewardHistoryWithMissedDetails(userId)
                    .enqueue(new Callback<OffersHistoryResponse>() {
                        @Override
                        public void onResponse(Call<OffersHistoryResponse> call,
                                               Response<OffersHistoryResponse> response) {
                            mContext.dismissProgressDialog();
                            if (response != null) {
                                if (response.code() == 401) { // Unauthorised Access
                                    NetworkCallController controller = new NetworkCallController();
                                    controller.setListner(new NetworkResponseListner<LoginResponse>() {
                                        @Override
                                        public void onResponse(int reqCode, LoginResponse response) {
                                            // delete all previous record
                                            Realm.getDefaultInstance().beginTransaction();
                                            Realm.getDefaultInstance().deleteAll();
                                            Realm.getDefaultInstance().commitTransaction();

                                            // add new record
                                            Realm.getDefaultInstance().beginTransaction();
                                            Realm.getDefaultInstance().copyToRealmOrUpdate(response);
                                            Realm.getDefaultInstance().commitTransaction();
                                            getNPSData(requestCode, userId);
                                        }

                                        @Override
                                        public void onFailure(int requestCode) {

                                        }
                                    });
                                    controller.refreshToken(100, getRefreshToken());
                                } else if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body().getData());

                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        mContext.showServerError(jObjError.getString("ErrorMessage"));
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getGroomingTechnicians", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<OffersHistoryResponse> call, Throwable t) {
                            mContext.dismissProgressDialog();
                            mContext.showServerError("Something went wrong, please try again !!!");
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getTaskQRCode(final int requestCode, final String taskNo) {
        try {
            mContext.showProgressDialog();
            BaseApplication.getQRCodeApi()
                    .getTaskQRCode(taskNo)
                    .enqueue(new Callback<QRCodeResponse>() {
                        @Override
                        public void onResponse(Call<QRCodeResponse> call,
                                               Response<QRCodeResponse> response) {
                            mContext.dismissProgressDialog();
                            if (response != null) {
                                if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body().getData());
                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        mContext.showServerError(jObjError.getString("ErrorMessage"));
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getGroomingTechnicians", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<QRCodeResponse> call, Throwable t) {
                            mContext.dismissProgressDialog();
                            mContext.showServerError(mContext.getString(R.string.something_went_wrong));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getUPICode(final int requestCode, final String taskId, final String accountNo, final String orderNo, final String amount, final String source) {
        try {
//            mContext.showProgressDialog();
            BaseApplication.getQRCodeApi()
                    .getUPICode(taskId, accountNo, orderNo, amount, source)
                    .enqueue(new Callback<QRCodeResponse>() {
                        @Override
                        public void onResponse(Call<QRCodeResponse> call,
                                               Response<QRCodeResponse> response) {
//                            mContext.dismissProgressDialog();
                            if (response != null) {
                                if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body().getData());
                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
//                                        mContext.showServerError(jObjError.getString("ErrorMessage"));
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getGroomingTechnicians", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<QRCodeResponse> call, Throwable t) {
//                            mContext.dismissProgressDialog();
//                            mContext.showServerError(mContext.getString(R.string.something_went_wrong));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getGenerateUPICode(final int requestCode, final String taskId, final String accountNo, final String orderNo, final String amount, final String source) {
        try {
//            mContext.showProgressDialog();
            BaseApplication.getQRCodeApi()
                    .getGenerateUPICode(taskId, accountNo, orderNo, amount, source)
                    .enqueue(new Callback<QRCodeResponse>() {
                        @Override
                        public void onResponse(Call<QRCodeResponse> call,
                                               Response<QRCodeResponse> response) {
//                            mContext.dismissProgressDialog();
                            if (response != null) {
                                if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body().getData());
                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
//                                        mContext.showServerError(jObjError.getString("ErrorMessage"));
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getGroomingTechnicians", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<QRCodeResponse> call, Throwable t) {
//                            mContext.dismissProgressDialog();
//                            mContext.showServerError(mContext.getString(R.string.something_went_wrong));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void getPhonePayCode(final int requestCode, final String taskId, final String accountNo, final String orderNo, final String amount, final String source) {
        try {
            mContext.showProgressDialog();
            BaseApplication.getQRCodeApi()
                    .getPhonePeCode(taskId, accountNo, orderNo, amount, source)
                    .enqueue(new Callback<PhonePeQRCodeResponse>() {
                        @Override
                        public void onResponse(Call<PhonePeQRCodeResponse> call,
                                               Response<PhonePeQRCodeResponse> response) {
                            mContext.dismissProgressDialog();
                            if (response != null) {
                                if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body().getData());
                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        mContext.showServerError(jObjError.getString("ErrorMessage"));
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getGroomingTechnicians", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<PhonePeQRCodeResponse> call, Throwable t) {
                            mContext.dismissProgressDialog();
                            mContext.showServerError(mContext.getString(R.string.something_went_wrong));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void checkPaymentStatus(final int requestCode, final String orderNo) {
        try {
//            mContext.showProgressDialog();
            BaseApplication.getQRCodeApi()
                    .checkUPIPaymentStatus(orderNo)
                    .enqueue(new Callback<CheckCodeResponse>() {
                        @Override
                        public void onResponse(Call<CheckCodeResponse> call,
                                               Response<CheckCodeResponse> response) {
//                            mContext.dismissProgressDialog();
                            if (response != null) {
                                if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body().getData());
                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
//                                        mContext.showServerError(jObjError.getString("ErrorMessage"));
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getGroomingTechnicians", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<CheckCodeResponse> call, Throwable t) {
//                            mContext.dismissProgressDialog();
//                            mContext.showServerError(mContext.getString(R.string.something_went_wrong));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void checkRenewalPaymentStatus(final int requestCode, final String orderNo) {
        try {
//            mContext.showProgressDialog();
            BaseApplication.getQRCodeApi()
                    .checkUPIRenewalPaymentStatus(orderNo)
                    .enqueue(new Callback<CheckCodeResponse>() {
                        @Override
                        public void onResponse(Call<CheckCodeResponse> call,
                                               Response<CheckCodeResponse> response) {
//                            mContext.dismissProgressDialog();
                            if (response != null) {
                                if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body().getData());
                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
//                                        mContext.showServerError(jObjError.getString("ErrorMessage"));
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getGroomingTechnicians", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<CheckCodeResponse> call, Throwable t) {
//                            mContext.dismissProgressDialog();
//                            mContext.showServerError(mContext.getString(R.string.something_went_wrong));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void checkPhonePeStatus(final int requestCode, final String taskId, final String transactionId, final String orderNo) {
        try {
//            mContext.showProgressDialog();
            BaseApplication.getQRCodeApi()
                    .checkPhonePeStatus(taskId, transactionId, orderNo)
                    .enqueue(new Callback<CheckPhonePeResponse>() {
                        @Override
                        public void onResponse(Call<CheckPhonePeResponse> call,
                                               Response<CheckPhonePeResponse> response) {
//                            mContext.dismissProgressDialog();
                            if (response != null) {
                                if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body().getData());
                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
//                                        mContext.showServerError(jObjError.getString("ErrorMessage"));
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getGroomingTechnicians", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<CheckPhonePeResponse> call, Throwable t) {
//                            mContext.dismissProgressDialog();
//                            mContext.showServerError(mContext.getString(R.string.something_went_wrong));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void saveCheckList(final int requestCode, List<TaskCheckList> request) {
        try {
            BaseApplication.getRetrofitAPI(true)
                    .saveCheckList(request)
                    .enqueue(new Callback<CheckListResponse>() {
                        @Override
                        public void onResponse(Call<CheckListResponse> call,
                                               Response<CheckListResponse> response) {
                            if (response != null) {
                                if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body());
                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
//                                        mContext.showServerError(jObjError.getString("ErrorMessage"));
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getGroomingTechnicians", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<CheckListResponse> call, Throwable t) {
//                            mContext.showServerError(mContext.getString(R.string.something_went_wrong));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getRewardLeaders(final int requestCode, final String userId) {
        try {
            mContext.showProgressDialog();
            BaseApplication.getRetrofitAPI(true)
                    .getRewardLeaders(userId)
                    .enqueue(new Callback<RewardLeadersResponse>() {
                        @Override
                        public void onResponse(Call<RewardLeadersResponse> call, Response<RewardLeadersResponse> response) {
                            mContext.dismissProgressDialog();
                            if (response != null) {
                                if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body().getData());
                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        mContext.showServerError(jObjError.getString("ErrorMessage"));
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getChemicals", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                mContext.showServerError();
                            }
                        }

                        @Override
                        public void onFailure(Call<RewardLeadersResponse> call, Throwable t) {
                            mContext.dismissProgressDialog();
                            mContext.showServerError(mContext.getString(R.string.something_went_wrong));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getServicePlans(final int requestCode, final String taskId) {
        try {
            mContext.showProgressDialog();
            BaseApplication.getRetrofitAPI(true)
                    .getServicePlans(taskId)
                    .enqueue(new Callback<ServicePlanResponse>() {
                        @Override
                        public void onResponse(Call<ServicePlanResponse> call, Response<ServicePlanResponse> response) {
                            mContext.dismissProgressDialog();
                            if (response != null) {
                                if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body().getData());
                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        mContext.showServerError(jObjError.getString("ErrorMessage"));
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getChemicals", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                mContext.showServerError();
                            }
                        }

                        @Override
                        public void onFailure(Call<ServicePlanResponse> call, Throwable t) {
                            mContext.dismissProgressDialog();
                            mContext.showServerError(mContext.getString(R.string.something_went_wrong));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void getResourceCheckList(final int requestCode, String resourceId, String lang) {
        try {
            BaseApplication.getRetrofitAPI(true)
                    .getResourceCheckList(resourceId, lang)
                    .enqueue(new Callback<ResourceCheckListResponse>() {
                        @Override
                        public void onResponse(Call<ResourceCheckListResponse> call, Response<ResourceCheckListResponse> response) {
                            if (response != null) {
                                if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body().getData());
                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getExotelCalled", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResourceCheckListResponse> call, Throwable t) {
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void saveSelfAssessment(final int requestCode, List<SelfAssessmentRequest> request) {
        try {
            BaseApplication.getRetrofitAPI(true)
                    .saveResourceCheckList(request)
                    .enqueue(new Callback<SelfAssessmentResponse>() {
                        @Override
                        public void onResponse(Call<SelfAssessmentResponse> call,
                                               Response<SelfAssessmentResponse> response) {
                            if (response != null) {
                                if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body());
                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
//                                        mContext.showServerError(jObjError.getString("ErrorMessage"));
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getGroomingTechnicians", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<SelfAssessmentResponse> call, Throwable t) {
//                            mContext.showServerError(mContext.getString(R.string.something_went_wrong));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void getAssessmentResponse(final int requestCode, String resourceId, String lang) {
        try {
            BaseApplication.getRetrofitAPI(true)
                    .getAssessmentResponse(resourceId, lang)
                    .enqueue(new Callback<AssessmentReportResponse>() {
                        @Override
                        public void onResponse(Call<AssessmentReportResponse> call, Response<AssessmentReportResponse> response) {
                            if (response != null) {
                                if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body().getData());
                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getExotelCalled", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<AssessmentReportResponse> call, Throwable t) {
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getProducts(final int requestCode) {
        try {
            mContext.showProgressDialog();
            BaseApplication.getRetrofitAPI(true)
                    .getProducts()
                    .enqueue(new Callback<ProductResponse>() {
                        @Override
                        public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                            mContext.dismissProgressDialog();
                            if (response != null) {
                                if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body().getData());
                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        mContext.showServerError(jObjError.getString("ErrorMessage"));
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getChemicals", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                mContext.showServerError();
                            }
                        }

                        @Override
                        public void onFailure(Call<ProductResponse> call, Throwable t) {
                            mContext.dismissProgressDialog();
                            mContext.showServerError(mContext.getString(R.string.something_went_wrong));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void renewOrder(final int requestCode, RenewOrderRequest request) {
        try {
//            mContext.showProgressDialog();
            BaseApplication.getRetrofitAPI(true)
                    .renewOrder(request)
                    .enqueue(new Callback<RenewOrderResponse>() {
                        @Override
                        public void onResponse(Call<RenewOrderResponse> call, Response<RenewOrderResponse> response) {
//                            mContext.dismissProgressDialog();
                            if (response != null) {
                                if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body().getData());
                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
//                                        mContext.showServerError(jObjError.getString("ErrorMessage"));
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "postReferrals", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
//                                mContext.showServerError();
                            }
                        }

                        @Override
                        public void onFailure(Call<RenewOrderResponse> call, Throwable t) {
//                            mContext.dismissProgressDialog();
//                            mContext.showServerError(mContext.getString(R.string.something_went_wrong));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getRenewalOTP(final int requestCode, String resourceId, String taskId) {
        try {
            BaseApplication.getRetrofitAPI(true)
                    .getRenewalOTP(resourceId, taskId)
                    .enqueue(new Callback<RenewalOTPResponse>() {
                        @Override
                        public void onResponse(Call<RenewalOTPResponse> call, Response<RenewalOTPResponse> response) {
                            if (response != null) {
                                if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body());
                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getExotelCalled", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<RenewalOTPResponse> call, Throwable t) {
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getConsolution(final int requestCode, String resourceId, String taskId, String lan) {
        try {
            BaseApplication.getRetrofitAPI(true)
                    .getConsulation(resourceId, taskId, lan)
                    .enqueue(new Callback<ConsulationResponse>() {
                        @Override
                        public void onResponse(Call<ConsulationResponse> call, Response<ConsulationResponse> response) {
                            if (response != null) {
                                if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body().getData());
                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getExotelCalled", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ConsulationResponse> call, Throwable t) {
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getTmsQuestions(final int requestCode, String taskId) {
        try {
            BaseApplication.getRetrofitAPI(true)
                    .getTmsQuestions(taskId)
                    .enqueue(new Callback<QuestionBase>() {
                        @Override
                        public void onResponse(Call<QuestionBase> call, Response<QuestionBase> response) {
                            if (response != null) {
                                if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body().getData());
                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getExotelCalled", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<QuestionBase> call, Throwable t) {
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getServiceDeliveryQuestions(final int requestCode, String taskId) {
        try {
            BaseApplication.getRetrofitAPI(true)
                    .getServiceDeliveryQuestions(taskId)
                    .enqueue(new Callback<QuestionBase>() {
                        @Override
                        public void onResponse(Call<QuestionBase> call, Response<QuestionBase> response) {
                            if (response != null) {
                                if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body().getData());
                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getExotelCalled", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<QuestionBase> call, Throwable t) {
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void saveTmsQuestions(final int requestCode, List<HashMap<String, Object>> data) {
        try {
            BaseApplication.getRetrofitAPI(true)
                    .saveTmsQuestions(data)
                    .enqueue(new Callback<BaseResponse>() {
                        @Override
                        public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                            if (response != null) {
                                if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body());
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<BaseResponse> call, Throwable t) {
                            mListner.onFailure(requestCode);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void saveServiceDelivery(final int requestCode, List<HashMap<String, Object>> data) {
        try {
            BaseApplication.getRetrofitAPI(true)
                    .saveServiceDelivery(data)
                    .enqueue(new Callback<CheckListResponse>() {
                        @Override
                        public void onResponse(Call<CheckListResponse> call, Response<CheckListResponse> response) {
                            if (response != null) {
                                if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body());
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<CheckListResponse> call, Throwable t) {
                            mListner.onFailure(requestCode);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getRecommendations(final int requestCode, String resourceId, String taskId, String lan) {
        try {
            BaseApplication.getRetrofitAPI(true)
                    .getRecommendations(resourceId, taskId, lan)
                    .enqueue(new Callback<RecommendationResponse>() {
                        @Override
                        public void onResponse(Call<RecommendationResponse> call, Response<RecommendationResponse> response) {
                            if (response != null) {
                                if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body().getData());
                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getExotelCalled", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<RecommendationResponse> call, Throwable t) {
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void saveConsultationNdInspection(final int requestCode, List<Data> request) {
        try {
            BaseApplication.getRetrofitAPI(true)
                    .saveConsultationNdInspection(request)
                    .enqueue(new Callback<SaveConsulationResponse>() {
                        @Override
                        public void onResponse(Call<SaveConsulationResponse> call,
                                               Response<SaveConsulationResponse> response) {
                            if (response != null) {
                                if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body());
                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
//                                        mContext.showServerError(jObjError.getString("ErrorMessage"));
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getGroomingTechnicians", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<SaveConsulationResponse> call, Throwable t) {
//                            mContext.showServerError(mContext.getString(R.string.something_went_wrong));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getTechnicianRoutineChecklist(final int requestCode, String resourceId) {
        try {
            mContext.showProgressDialog();
            BaseApplication.getRetrofitAPI(true)
                    .getTechniciansForRoutineCheckList(resourceId)
                    .enqueue(new Callback<TechnicianRoutineResponse>() {
                        @Override
                        public void onResponse(Call<TechnicianRoutineResponse> call, Response<TechnicianRoutineResponse> response) {
                            mContext.dismissProgressDialog();
                            if (response != null) {
                                if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body().getData());
                                } else if (response.errorBody() != null) {
                                    try {

                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getExotelCalled", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<TechnicianRoutineResponse> call, Throwable t) {
                            mContext.dismissProgressDialog();
                            mContext.showServerError(mContext.getString(R.string.something_went_wrong));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getRoutineResponse(final int requestCode, String resourceId, final String lang) {
        try {
            BaseApplication.getRetrofitAPI(true)
                    .getRoutineData(resourceId, lang)
                    .enqueue(new Callback<RoutineResponse>() {
                        @Override
                        public void onResponse(Call<RoutineResponse> call, Response<RoutineResponse> response) {
                            Log.i("RoutineCheckUp", "response_null");
                            if (response != null) {
                                Log.i("RoutineCheckUp", "response_nn");
                                if (response.body() != null) {
                                    Log.i("RoutineCheckUp", "response");
                                    mListner.onResponse(requestCode, response.body().getData());
                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getExotelCalled", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Log.i("RoutineCheckUp", "res" + e.getMessage());

                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<RoutineResponse> call, Throwable t) {
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("RoutineCheckUp", "res" + e.getMessage());
        }

    }

    public void getKYCDocuments(final int requestCode, String resourceId, final String lang) {
        try {
            BaseApplication.getRetrofitAPI(true)
                    .getKYCDocuments(resourceId, lang)
                    .enqueue(new Callback<KycDocumentResponse>() {
                        @Override
                        public void onResponse(Call<KycDocumentResponse> call, Response<KycDocumentResponse> response) {
                            Log.i("RoutineCheckUp", "response_null");
                            if (response != null) {
                                Log.i("RoutineCheckUp", "response_nn");
                                if (response.body() != null) {
                                    Log.i("RoutineCheckUp", "response");
                                    mListner.onResponse(requestCode, response.body().getData());
                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getExotelCalled", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Log.i("RoutineCheckUp", "res" + e.getMessage());

                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<KycDocumentResponse> call, Throwable t) {
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("RoutineCheckUp", "res" + e.getMessage());
        }

    }

    public void getKYCTypes(final int requestCode, String resourceId, final String lang) {
        try {
            BaseApplication.getRetrofitAPI(true)
                    .getKYCTypes(resourceId, lang)
                    .enqueue(new Callback<KycTypeResponse>() {
                        @Override
                        public void onResponse(Call<KycTypeResponse> call, Response<KycTypeResponse> response) {
                            Log.i("RoutineCheckUp", "response_null");
                            if (response != null) {
                                Log.i("RoutineCheckUp", "response_nn");
                                if (response.body() != null) {
                                    Log.i("RoutineCheckUp", "response");
                                    mListner.onResponse(requestCode, response.body().getData());
                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getExotelCalled", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Log.i("RoutineCheckUp", "res" + e.getMessage());

                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<KycTypeResponse> call, Throwable t) {
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("RoutineCheckUp", "res" + e.getMessage());
        }

    }

    public void saveRoutineCheckList(final int requestCode, TechRoutineData request) {
        try {
            BaseApplication.getRetrofitAPI(true)
                    .saveRoutineCheckList(request)
                    .enqueue(new Callback<SaveRoutineResponse>() {
                        @Override
                        public void onResponse(Call<SaveRoutineResponse> call,
                                               Response<SaveRoutineResponse> response) {
                            if (response != null) {
                                if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body());
                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
//                                        mContext.showServerError(jObjError.getString("ErrorMessage"));
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getGroomingTechnicians", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<SaveRoutineResponse> call, Throwable t) {
//                            mContext.showServerError(mContext.getString(R.string.something_went_wrong));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void saveKYCDocument(final int requestCode, SaveKycRequest request) {
        try {
            BaseApplication.getRetrofitAPI(true)
                    .saveKYCDocument(request)
                    .enqueue(new Callback<SaveKycResponse>() {
                        @Override
                        public void onResponse(Call<SaveKycResponse> call,
                                               Response<SaveKycResponse> response) {
                            if (response != null) {
                                if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body());
                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
//                                        mContext.showServerError(jObjError.getString("ErrorMessage"));
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getGroomingTechnicians", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<SaveKycResponse> call, Throwable t) {
//                            mContext.showServerError(mContext.getString(R.string.something_went_wrong));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void getKarmaResources(final int requestCode, final String userId) {
        try {
            BaseApplication.getRetrofitAPI(true)
                    .getKarmaForResource(userId)
                    .enqueue(new Callback<KarmaResponse>() {
                        @Override
                        public void onResponse(Call<KarmaResponse> call, Response<KarmaResponse> response) {
                            if (response != null) {
                                if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body().getData());
                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getChemicals", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<KarmaResponse> call, Throwable t) {
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getKarmaHistoryResources(final int requestCode, final String userId) {
        try {
            BaseApplication.getRetrofitAPI(true)
                    .getKarmaHistoryForResource(userId)
                    .enqueue(new Callback<KarmaHistoryResponse>() {
                        @Override
                        public void onResponse(Call<KarmaHistoryResponse> call, Response<KarmaHistoryResponse> response) {
                            if (response != null) {
                                if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body().getData());
                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getChemicals", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<KarmaHistoryResponse> call, Throwable t) {
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getAppointmentSlots(final int requestCode, final String taskId, final String slotStartDate, final String slotEndDate, int source) {
        try {
            mContext.showProgressDialog();
            BaseApplication.getSlotApi()
                    .getAppointmentSlots(taskId, slotStartDate, slotEndDate, source)
                    .enqueue(new Callback<SlotResponse>() {
                        @Override
                        public void onResponse(Call<SlotResponse> call, Response<SlotResponse> response) {
                            mContext.dismissProgressDialog();
                            if (response != null) {
                                if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body().getTimeSlot());
                                } else if (response.errorBody() != null) {
                                    mContext.dismissProgressDialog();
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        mContext.showServerError(jObjError.getString("ErrorMessage"));
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getChemicals", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        mContext.dismissProgressDialog();
                                        mContext.showServerError(e.getMessage()
                                        );

                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<SlotResponse> call, Throwable t) {
                            mContext.dismissProgressDialog();
                            mContext.showServerError(mContext.getString(R.string.something_went_wrong));

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void saveKarmaDetails(final int requestCode, SaveKarmaRequest request) {
        try {
            BaseApplication.getRetrofitAPI(true)
                    .saveKarmaDetails(request)
                    .enqueue(new Callback<SaveKarmaResponse>() {
                        @Override
                        public void onResponse(Call<SaveKarmaResponse> call,
                                               Response<SaveKarmaResponse> response) {
                            if (response != null) {
                                if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body());
                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
//                                        mContext.showServerError(jObjError.getString("ErrorMessage"));
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getGroomingTechnicians", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<SaveKarmaResponse> call, Throwable t) {
//                            mContext.showServerError(mContext.getString(R.string.something_went_wrong));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getRenewalUPICode(final int requestCode, final String taskId, final String accountNo, final String orderNo, final String amount, final String source) {
        try {
            mContext.showProgressDialog();
            BaseApplication.getQRCodeApi()
                    .getRenewalUPICode(taskId, accountNo, orderNo, amount, source)
                    .enqueue(new Callback<QRCodeResponse>() {
                        @Override
                        public void onResponse(Call<QRCodeResponse> call,
                                               Response<QRCodeResponse> response) {
                            mContext.dismissProgressDialog();
                            if (response != null) {
                                if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body().getData());
                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        mContext.showServerError(jObjError.getString("ErrorMessage"));
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getGroomingTechnicians", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<QRCodeResponse> call, Throwable t) {
                            mContext.dismissProgressDialog();
                            mContext.showServerError(mContext.getString(R.string.something_went_wrong));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getQuizCategory(final int requestCode, final String resourceId, String lan) {
        try {
            BaseApplication.getRetrofitAPI(true)
                    .getQuizCategory(resourceId, lan)
                    .enqueue(new Callback<QuizCategoryResponse>() {
                        @Override
                        public void onResponse(Call<QuizCategoryResponse> call, Response<QuizCategoryResponse> response) {
                            if (response != null) {
                                if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body().getData());
                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getChemicals", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<QuizCategoryResponse> call, Throwable t) {
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void getQuizQuestions(final int requestCode, final String resourceId, final int puzzleId, String lan) {
        try {
            BaseApplication.getRetrofitAPI(true)
                    .getQuizQuestions(resourceId, puzzleId, lan)
                    .enqueue(new Callback<QuizResponse>() {
                        @Override
                        public void onResponse(Call<QuizResponse> call, Response<QuizResponse> response) {
                            if (response != null) {
                                if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body().getData());
                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getChemicals", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<QuizResponse> call, Throwable t) {
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void updateNoRenewalReason(final int requestCode, final String taskId,
                                      final String reason) {
        try {
            mContext.showProgressDialog();
            BaseApplication.getRetrofitAPI(true)
                    .updateNoRenewalReason(taskId, reason)
                    .enqueue(new Callback<BasicResponse>() {
                        @Override
                        public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                            mContext.dismissProgressDialog();
                            if (response != null) {
                                if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body());
                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        mContext.showServerError(jObjError.getString("ErrorMessage"));
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getChemicals", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                mContext.showServerError();
                            }
                        }

                        @Override
                        public void onFailure(Call<BasicResponse> call, Throwable t) {
                            mContext.dismissProgressDialog();
                            mContext.showServerError(mContext.getString(R.string.something_went_wrong));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getOrderNoDetails(String orderId, String userId) {
        BaseApplication.getRetrofitAPI(false)
                .getOrderDetails(orderId, userId)
                .enqueue(new Callback<OrderDetails>() {
                    @Override
                    public void onResponse(Call<OrderDetails> call, Response<OrderDetails> response) {
                        if (response.body() != null) {
                            String responseBody = response.body().toString();
                            mListner.onResponse(20211, response.body());
                            //Log.d("TAG-UAT", responseBody);
                        }
                    }

                    @Override
                    public void onFailure(Call<OrderDetails> call, Throwable t) {
                        Log.d("TAG-UAT-Error", t.getMessage());
                        mListner.onFailure(20211);
                    }
                });
    }

    public void getBarcodeOrderDetails(String orderId, String userId) {
        BaseApplication.getRetrofitAPI(false)
                .getBarcodeOrderDetails(orderId, userId)
                .enqueue(new Callback<BarcodeDetailsResponse>() {
                    @Override
                    public void onResponse(Call<BarcodeDetailsResponse> call, Response<BarcodeDetailsResponse> response) {
                        if (response.body() != null) {
                            String responseBody = response.body().toString();
                            mListner.onResponse(20211, response.body());
                            //Log.d("TAG-UAT", responseBody);
                        }
                    }

                    @Override
                    public void onFailure(Call<BarcodeDetailsResponse> call, Throwable t) {
                        Log.d("TAG-UAT-Error", t.getMessage());
                        mListner.onFailure(20211);
                    }
                });
    }

    public void saveBarcodeList(int requestCode, ArrayList<BarcodeList> barcodeList) {
        BaseApplication.getRetrofitAPI(false)
                .saveBarcode(barcodeList)
                .enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        if (response.body() != null) {
                            mListner.onResponse(requestCode, response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
                        mListner.onFailure(requestCode);
                    }
                });
    }

    public void verifyBarcodeDetails(int requestCode, HashMap<String, Object> verifyDetails) {
        BaseApplication.getRetrofitAPI(false)
                .verifyBarcode(verifyDetails)
                .enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        if (response.body() != null) {
                            mListner.onResponse(requestCode, response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
                        mListner.onFailure(requestCode);
                    }
                });
    }


    public void deleteBarcodeDetails(int requestCode, HashMap<String, Object> details) {
        BaseApplication.getRetrofitAPI(false)
                .deleteBarcode(details)
                .enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        if (response.body() != null) {
                            mListner.onResponse(requestCode, response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
                        mListner.onFailure(requestCode);
                    }
                });
    }

    public void uploadBoxImage(int requestCode, HashMap<String, String> imageDetails) {
        BaseApplication.getRetrofitAPI(false)
                .uploadBoxImage(imageDetails)
                .enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        if (response.body() != null) {
                            mListner.onResponse(requestCode, response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
                        mListner.onFailure(requestCode);
                    }
                });
    }

    public void getPuzzleStatsForRes(int requestCode, String resourceId, String lan) {
        BaseApplication.getRetrofitAPI(false)
                .getPuzzleStatsForResources(resourceId, lan)
                .enqueue(new Callback<QuizPuzzleStats>() {
                    @Override
                    public void onResponse(Call<QuizPuzzleStats> call, Response<QuizPuzzleStats> response) {
                        if (response.body() != null) {
                            mListner.onResponse(requestCode, response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<QuizPuzzleStats> call, Throwable t) {
                        mListner.onFailure(requestCode);
                    }
                });
    }

    public void getPuzzleLeaderBoard(int requestCode, String resourceId, String lan) {
        BaseApplication.getRetrofitAPI(false)
                .getPuzzleLeaderBoard(resourceId, lan)
                .enqueue(new Callback<QuizLeaderBoardBase>() {
                    @Override
                    public void onResponse(Call<QuizLeaderBoardBase> call, Response<QuizLeaderBoardBase> response) {
                        mListner.onResponse(requestCode, response.body());
                    }

                    @Override
                    public void onFailure(Call<QuizLeaderBoardBase> call, Throwable t) {
                        mListner.onFailure(requestCode);
                    }
                });
    }

    public void getPuzzleLevel(int requestCode, String resourceId, String lan) {
        BaseApplication.getRetrofitAPI(false)
                .getPuzzleLevelModel(resourceId, lan)
                .enqueue(new Callback<QuizLevelModelBase>(){
                    @Override
                    public void onResponse(Call<QuizLevelModelBase> call, Response<QuizLevelModelBase> response) {
                        mListner.onResponse(requestCode, response.body());
                    }

                    @Override
                    public void onFailure(Call<QuizLevelModelBase> call, Throwable t) {
                        mListner.onFailure(requestCode);
                    }
                });
    }

    public void savePuzzleAnswers(int requestCode, List<QuizSaveAnswers> quizSaveAnswers) {
        BaseApplication.getRetrofitAPI(false)
                .savePuzzleAnswers(quizSaveAnswers)
                .enqueue(new Callback<QuizSaveResponseBase>(){
                    @Override
                    public void onResponse(Call<QuizSaveResponseBase> call, Response<QuizSaveResponseBase> response) {
                        mListner.onResponse(requestCode, response.body());
                    }

                    @Override
                    public void onFailure(Call<QuizSaveResponseBase> call, Throwable t) {
                        mListner.onFailure(requestCode);
                    }
                });
    }


    public void getServiceAreaChemical(String orderId, int sequenceNo, String serviceType, boolean showAllServices) {
        BaseApplication.getB2BWoWApi()
                .getServiceAreaChemical(orderId, sequenceNo, serviceType, showAllServices)
                .enqueue(new Callback<ServiceAreaChemicalResponse>() {
                    @Override
                    public void onResponse(Call<ServiceAreaChemicalResponse> call, Response<ServiceAreaChemicalResponse> response) {
                        if (response.body() != null) {
                            String responseBody = response.body().toString();
                            mListner.onResponse(20211, response.body().getData());
                            //Log.d("TAG-UAT", responseBody);
                        }
                    }

                    @Override
                    public void onFailure(Call<ServiceAreaChemicalResponse> call, Throwable t) {
                        Log.d("TAG-UAT-Error", t.getMessage());
                        mListner.onFailure(20211);
                    }
                });
    }

    public void updateActivityServiceStatus(final int requestCode, List<SaveActivityRequest> request) {
        try {
            BaseApplication.getB2BWoWApi()
                    .updateActivityStatus(request)
                    .enqueue(new Callback<BaseResponse>() {
                        @Override
                        public void onResponse(Call<BaseResponse> call,
                                               Response<BaseResponse> response) {
                            if (response != null) {
                                if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body());
                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
//                                        mContext.showServerError(jObjError.getString("ErrorMessage"));
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getGroomingTechnicians", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<BaseResponse> call, Throwable t) {
//                            mContext.showServerError(mContext.getString(R.string.something_went_wrong));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void updateActivityStatus(final int requestCode, List<SaveServiceActivity> request) {
        try {
            BaseApplication.getB2BWoWApi()
                    .updateActivityServiceStatus(request)
                    .enqueue(new Callback<BaseResponse>() {
                        @Override
                        public void onResponse(Call<BaseResponse> call,
                                               Response<BaseResponse> response) {
                            if (response != null) {
                                if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body());
                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
//                                        mContext.showServerError(jObjError.getString("ErrorMessage"));
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getGroomingTechnicians", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<BaseResponse> call, Throwable t) {
//                            mContext.showServerError(mContext.getString(R.string.something_went_wrong));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void getServiceActivityChemical(String orderId, int sequenceNo, String serviceType, boolean showAllServices) {
        BaseApplication.getB2BWoWApi()
                .getServiceActivityChemical(orderId, sequenceNo, serviceType, showAllServices)
                .enqueue(new Callback<ActivityResponse>() {
                    @Override
                    public void onResponse(Call<ActivityResponse> call, Response<ActivityResponse> response) {
                        if (response.body() != null) {
                            String responseBody = response.body().toString();
                            mListner.onResponse(20211, response.body().getData());
                            //Log.d("TAG-UAT", responseBody);
                        }
                    }

                    @Override
                    public void onFailure(Call<ActivityResponse> call, Throwable t) {
                        Log.d("TAG-UAT-Error", t.getMessage());
                        mListner.onFailure(20211);
                    }
                });
    }

    public void getResourceMenu(String resourceId) {
        BaseApplication.getRetrofitAPI(false)
                .getResourceMenu(resourceId)
                .enqueue(new Callback<MenuResponse>() {
                    @Override
                    public void onResponse(Call<MenuResponse> call, Response<MenuResponse> response) {
                        if (response.body() != null) {
                            String responseBody = response.body().toString();
                            mListner.onResponse(20211, response.body().getData());
                            //Log.d("TAG-UAT", responseBody);
                        }
                    }

                    @Override
                    public void onFailure(Call<MenuResponse> call, Throwable t) {
                        Log.d("TAG-UAT-Error", t.getMessage());
                        mListner.onFailure(20211);
                    }
                });
    }

    public void sendReferralMessage(final int requestCode, final String resourceId, final String taskId) {
        try {
            if (mContext != null)
                mContext.showProgressDialog();
            BaseApplication.getRetrofitAPI(false)
                    .sendReferralMessage(resourceId, taskId)
                    .enqueue(new Callback<BaseResponse>() {
                        @Override
                        public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                            if (mContext != null)
                                mContext.dismissProgressDialog();
                            if (response != null) {
                                if (response.body() != null) {
                                    mListner.onResponse(requestCode, response.body());
                                } else if (response.errorBody() != null) {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        if (mContext != null)
                                            mContext.showServerError(jObjError.getString("ErrorMessage"));
                                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                            AppUtils.sendErrorLogs(response.errorBody().string(), getClass().getSimpleName(), "getChemicals", lineNo, userName, DeviceName);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                if (mContext != null)
                                    mContext.showServerError();
                            }
                        }

                        @Override
                        public void onFailure(Call<BaseResponse> call, Throwable t) {
                            if (mContext != null) {
                                mContext.dismissProgressDialog();
                                mContext.showServerError(mContext.getString(R.string.something_went_wrong));
                            }

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public String getRefreshToken() {
        String refreshToken = null;
        try {
            LoginResponse loginResponse =
                    Realm.getDefaultInstance().where(LoginResponse.class).findAll().get(0);
            refreshToken = loginResponse.getRefreshToken();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return refreshToken;
    }

}
