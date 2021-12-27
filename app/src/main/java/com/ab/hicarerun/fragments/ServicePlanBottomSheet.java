package com.ab.hicarerun.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.hicarerun.BaseApplication;
import com.ab.hicarerun.BaseFragment;
import com.ab.hicarerun.R;
import com.ab.hicarerun.activities.HomeActivity;
import com.ab.hicarerun.activities.NewTaskDetailsActivity;
import com.ab.hicarerun.adapter.BankSearchAdapter;
import com.ab.hicarerun.adapter.ServicePaymentAdapter;
import com.ab.hicarerun.databinding.FragmentServicePlanBottomSheetBinding;
import com.ab.hicarerun.network.NetworkCallController;
import com.ab.hicarerun.network.NetworkResponseListner;
import com.ab.hicarerun.network.models.GeneralModel.GeneralData;
import com.ab.hicarerun.network.models.LoginResponse;
import com.ab.hicarerun.network.models.ModelQRCode.CheckStatus;
import com.ab.hicarerun.network.models.ModelQRCode.QRCode;
import com.ab.hicarerun.network.models.ServicePlanModel.PaymentMode;
import com.ab.hicarerun.network.models.ServicePlanModel.PlanData;
import com.ab.hicarerun.network.models.ServicePlanModel.RenewOrder;
import com.ab.hicarerun.network.models.ServicePlanModel.RenewOrderRequest;
import com.ab.hicarerun.network.models.ServicePlanModel.RenewalOTPResponse;
import com.ab.hicarerun.network.models.ServicePlanModel.RenewalServicePlan;
import com.ab.hicarerun.network.models.WalletModel.WalletBase;
import com.ab.hicarerun.utils.AppUtils;
import com.ab.hicarerun.utils.GifDrawableImageViewTarget;
import com.ab.hicarerun.utils.MyDividerItemDecoration;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import io.realm.RealmResults;

import static android.view.View.GONE;
import static com.ab.hicarerun.BaseApplication.getRealm;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServicePlanBottomSheet extends BottomSheetDialogFragment {
    FragmentServicePlanBottomSheetBinding mFragmentServicePlanBottomSheetBinding;
    private RenewalServicePlan renewalServicePlans;
    private PlanData planData;
    private ServicePaymentAdapter mAdapter;
    private BankSearchAdapter mBankAdapter;
    private List<String> bankList = new ArrayList<>();
    private static final int REQUEST_BANK = 1000;
    private static final int REQUEST_RENEW = 2000;
    private static final int REQUEST_OTP = 3000;
    private static final int QR_CODE_REQ = 4000;
    private static final int CHECK_REQ = 5000;
    private AlertDialog mAlertDialog = null;
    private RealmResults<GeneralData> mTaskDetailsData = null;
    private Timer timer = new Timer();
    private boolean hasStarted = false;
    private String bankName = "";
    private String chequeDate = "";
    private List<PaymentMode> checkModes = new ArrayList<>();
    private String mode = "";
    private String taskId = "";
    private String accountNo = "";
    private String amount = "";
    private String orderNo = "";
    private String orderId = "";
    private final String source = "HICARERUN";
    private String OTP = "";
    private String OTP_1 = "";
    private String OTP_2 = "";
    private String edt_OTP = "";
    private int redeemablePoints = 0;
    private double utilizedPoints = 0;
    private String actualAmount = "";
    private int actualRedeemPoints = 0;
    private ProgressDialog progress;
    private Context mContext;


    public ServicePlanBottomSheet(RenewalServicePlan renewalServicePlans, int redeemablePoints) {
        this.renewalServicePlans = renewalServicePlans;
        this.redeemablePoints = redeemablePoints;
        actualRedeemPoints = redeemablePoints;
    }

    public ServicePlanBottomSheet(PlanData planData, int redeemablePoints) {
        this.planData = planData;
        this.redeemablePoints = redeemablePoints;
        actualRedeemPoints = redeemablePoints;
    }

    @Override
    public void onSaveInstanceState(Bundle oldInstanceState) {
        super.onSaveInstanceState(oldInstanceState);
        oldInstanceState.clear();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        bottomSheetDialog.setOnShowListener(dia -> {
            BottomSheetDialog dialog = (BottomSheetDialog) dia;
            FrameLayout bottomSheet = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
            BottomSheetBehavior.from(bottomSheet).setSkipCollapsed(true);
            BottomSheetBehavior.from(bottomSheet).setHideable(true);
        });

        return bottomSheetDialog;

    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFragmentServicePlanBottomSheetBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_service_plan_bottom_sheet, container, false);
        return mFragmentServicePlanBottomSheetBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progress = new ProgressDialog(getActivity(), R.style.TransparentProgressDialog);
        progress.setCancelable(false);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getActivity());
        mFragmentServicePlanBottomSheetBinding.recycleView.setLayoutManager(lm);
        mFragmentServicePlanBottomSheetBinding.recycleView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new ServicePaymentAdapter(getActivity());
        mFragmentServicePlanBottomSheetBinding.recycleView.setAdapter(mAdapter);
        setPaymentModes();

        if (planData != null) {
            taskId = planData.getTaskId();
            accountNo = planData.getAccountId();
            orderNo = planData.getOrderNo();
            amount = planData.getDiscountedOrderAmount();
            actualAmount = planData.getDiscountedOrderAmount();
            mFragmentServicePlanBottomSheetBinding.priceTv.setText("\u20B9" + " " + planData.getDiscountedOrderAmount());
            mFragmentServicePlanBottomSheetBinding.txtPlanAmount.setText("\u20B9" + " " + planData.getDiscountedOrderAmount());
            mFragmentServicePlanBottomSheetBinding.txtPlanAmount.setTypeface(mFragmentServicePlanBottomSheetBinding.txtPlanAmount.getTypeface(), Typeface.BOLD);
            mFragmentServicePlanBottomSheetBinding.txtPlanDis.setText("\u20B9" + " " + planData.getActualOrderAmount());
            mFragmentServicePlanBottomSheetBinding.txtPlanName.setText(planData.getPlanName());
            mFragmentServicePlanBottomSheetBinding.txtDiscountPercent.setText(planData.getDiscount() + "% OFF");
            mFragmentServicePlanBottomSheetBinding.txtDiscountPercent.setTypeface(mFragmentServicePlanBottomSheetBinding.txtDiscountPercent.getTypeface(), Typeface.BOLD);
            mFragmentServicePlanBottomSheetBinding.txtPlanDetail.setText(planData.getServiceDescription());
            if (planData.getOfferText() != null && !planData.getOfferText().equals("")) {
                mFragmentServicePlanBottomSheetBinding.txtPlanDes.setText(planData.getOfferText());
                mFragmentServicePlanBottomSheetBinding.txtPlanDes.setVisibility(View.VISIBLE);
            } else {
                mFragmentServicePlanBottomSheetBinding.txtPlanDes.setVisibility(View.GONE);
            }
            if (planData.getDiscount() != null && !planData.getDiscount().equals("0.00")) {
                mFragmentServicePlanBottomSheetBinding.txtDiscountPercent.setVisibility(View.VISIBLE);
                mFragmentServicePlanBottomSheetBinding.txtPlanDis.setVisibility(View.VISIBLE);
            } else {
                mFragmentServicePlanBottomSheetBinding.txtDiscountPercent.setVisibility(View.GONE);
                mFragmentServicePlanBottomSheetBinding.txtPlanDis.setVisibility(View.GONE);
            }

        } else {
            taskId = renewalServicePlans.getTaskId();
            accountNo = renewalServicePlans.getAccountId();
            orderNo = renewalServicePlans.getOrderNo();
            amount = renewalServicePlans.getDiscountedOrderAmount();
            actualAmount = renewalServicePlans.getDiscountedOrderAmount();
            mFragmentServicePlanBottomSheetBinding.priceTv.setText("\u20B9" + " " + renewalServicePlans.getDiscountedOrderAmount());
            mFragmentServicePlanBottomSheetBinding.txtPlanAmount.setText("\u20B9" + " " + renewalServicePlans.getDiscountedOrderAmount());
            mFragmentServicePlanBottomSheetBinding.txtPlanAmount.setTypeface(mFragmentServicePlanBottomSheetBinding.txtPlanAmount.getTypeface(), Typeface.BOLD);
            mFragmentServicePlanBottomSheetBinding.txtPlanDis.setText("\u20B9" + " " + renewalServicePlans.getActualOrderAmount());
            mFragmentServicePlanBottomSheetBinding.txtPlanName.setText(renewalServicePlans.getPlanName());
            mFragmentServicePlanBottomSheetBinding.txtDiscountPercent.setText(renewalServicePlans.getDiscount() + "% OFF");
            mFragmentServicePlanBottomSheetBinding.txtDiscountPercent.setTypeface(mFragmentServicePlanBottomSheetBinding.txtDiscountPercent.getTypeface(), Typeface.BOLD);
            mFragmentServicePlanBottomSheetBinding.txtPlanDetail.setText(renewalServicePlans.getServiceDescription());
            if (renewalServicePlans.getOfferText() != null && !renewalServicePlans.getOfferText().equals("")) {
                mFragmentServicePlanBottomSheetBinding.txtPlanDes.setText(renewalServicePlans.getOfferText());
                mFragmentServicePlanBottomSheetBinding.txtPlanDes.setVisibility(View.VISIBLE);
            } else {
                mFragmentServicePlanBottomSheetBinding.txtPlanDes.setVisibility(View.GONE);
            }
            if (renewalServicePlans.getDiscount() != null && !renewalServicePlans.getDiscount().equals("0.00")) {
                mFragmentServicePlanBottomSheetBinding.txtDiscountPercent.setVisibility(View.VISIBLE);
                mFragmentServicePlanBottomSheetBinding.txtPlanDis.setVisibility(View.VISIBLE);
            } else {
                mFragmentServicePlanBottomSheetBinding.txtDiscountPercent.setVisibility(View.GONE);
                mFragmentServicePlanBottomSheetBinding.txtPlanDis.setVisibility(View.GONE);
            }
        }

        setHygienePoints();
        mFragmentServicePlanBottomSheetBinding.txtPlanDis.setPaintFlags(mFragmentServicePlanBottomSheetBinding.txtPlanDis.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        mFragmentServicePlanBottomSheetBinding.btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Double.parseDouble(amount) > 0) {
                    if (mode.length() > 0) {
                        if (mode.equalsIgnoreCase("paytm") || mode.equalsIgnoreCase("upi")) {
//                        if (timer != null) {
//                            timer.cancel();
//                        }
                            showQRCodeDialog();
                        } else {
                            if (timer != null) {
                                timer.cancel();
                            }
                            getRenewOtp(false);
                        }

                    }
                }else {
                    getRenewOtp(false);
                }
            }
        });
        mFragmentServicePlanBottomSheetBinding.redeemChk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    if (redeemablePoints > Double.parseDouble(amount)){
                        utilizedPoints = Double.parseDouble(actualAmount);
                        redeemablePoints = Integer.parseInt(String.valueOf(Math.round(redeemablePoints - Double.parseDouble(amount))));
                        amount = String.valueOf(0);
                        mAdapter.setMode();
                    }else if (Double.parseDouble(amount) >= redeemablePoints){
                        utilizedPoints = actualRedeemPoints;
                        amount = String.valueOf(Double.parseDouble(amount) - redeemablePoints);
                        //redeemablePoints = Math.round(Float.parseFloat(actualAmount)) - redeemablePoints;
                        redeemablePoints = 0;
                    }
                    //mFragmentServicePlanBottomSheetBinding.redeemablePointsTv.setPaintFlags(mFragmentServicePlanBottomSheetBinding.redeemablePointsTv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    //mFragmentServicePlanBottomSheetBinding.remainingPointsTv.setText(" " + redeemablePoints);
                    mFragmentServicePlanBottomSheetBinding.remainingPointsTv.setVisibility(GONE);
                }else {
                    mAdapter.resetMode();
                    redeemablePoints = actualRedeemPoints;
                    amount = actualAmount;
                    utilizedPoints = 0;
                    mFragmentServicePlanBottomSheetBinding.remainingPointsTv.setVisibility(GONE);
                    //mFragmentServicePlanBottomSheetBinding.redeemablePointsTv.setPaintFlags(mFragmentServicePlanBottomSheetBinding.redeemablePointsTv.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                }
                mFragmentServicePlanBottomSheetBinding.redeemablePointsTv.setText(" " + actualRedeemPoints);
                mFragmentServicePlanBottomSheetBinding.priceTv.setText("\u20B9" + " " + amount);
            }
        });
    }

    private void showQRCodeDialog() {
        try {
            if (getActivity() != null) {
//                mTaskDetailsData = getRealm().where(GeneralData.class).findAll();
//                if (mTaskDetailsData != null && mTaskDetailsData.size() > 0) {
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.qr_code_dialog_layout, null);
                dialogBuilder.setView(dialogView);
                final AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
                progress.show();
                final TextView txtTitle = (TextView) dialogView.findViewById(R.id.txtTitle);
                final TextView txtStatus = (TextView) dialogView.findViewById(R.id.txtPaymentStatus);
                final TextView txtScan = (TextView) dialogView.findViewById(R.id.txtScan);
                final TextView txtApp = (TextView) dialogView.findViewById(R.id.txtApp);
                final ImageView imgClose = (ImageView) dialogView.findViewById(R.id.imgClose);
                final ImageView imgCode = (ImageView) dialogView.findViewById(R.id.img_payscanner);
                final LinearLayout lnrCheckStatus = (LinearLayout) dialogView.findViewById(R.id.lnrQrCode);
                txtTitle.setText("\u20B9" + " " + amount);
                txtScan.setTypeface(txtScan.getTypeface(), Typeface.BOLD);
                txtApp.setTypeface(txtApp.getTypeface(), Typeface.BOLD);
                NetworkCallController controller = new NetworkCallController();
                controller.setListner(new NetworkResponseListner<QRCode>() {
                    @Override
                    public void onResponse(int requestCode, QRCode response) {
                        Picasso.get().load(response.getUrl()).into(imgCode);
                        checkPaymentStatus(response.getOrderId());
                        orderId = response.getOrderId();
                        progress.dismiss();
                    }

                    @Override
                    public void onFailure(int requestCode) {
                    }
                });
                controller.getGenerateUPICode(QR_CODE_REQ, taskId, accountNo, orderNo, amount, source);
                imgClose.setOnClickListener(view -> {
                    alertDialog.dismiss();
                });

                lnrCheckStatus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        checkPaymentStatus(orderId);
                    }
                });
                alertDialog.setCancelable(false);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                alertDialog.setCanceledOnTouchOutside(false);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkPaymentStatus(String orderId) {
        try {
            hasStarted = true;
            timer.scheduleAtFixedRate(new TimerTask() {
                                          @Override
                                          public void run() {
                                              checkStatus(orderId, timer);
                                          }
                                      },
                    0, 15000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void checkStatus(String orderId, Timer timer) {
        try {
            NetworkCallController controller = new NetworkCallController();
            controller.setListner(new NetworkResponseListner<CheckStatus>() {
                @Override
                public void onResponse(int requestCode, CheckStatus response) {
                    try {
                        if (response != null) {
                            if (response.getSTATUS().equals("TXN_SUCCESS")) {
                                if (timer != null) {
                                    timer.cancel();
                                }
                                saveRenewalData(mode, response.getTXNID(), "", "", "", "");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int requestCode) {
                }
            });
            controller.checkRenewalPaymentStatus(CHECK_REQ, orderId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getRenewOtp(boolean isResend) {
        try {
            progress.show();
            RealmResults<LoginResponse> LoginRealmModels =
                    getRealm().where(LoginResponse.class).findAll();
            if (LoginRealmModels != null && LoginRealmModels.size() > 0) {
                NetworkCallController controller = new NetworkCallController();
                controller.setListner(new NetworkResponseListner<RenewalOTPResponse>() {
                    @Override
                    public void onResponse(int requestCode, RenewalOTPResponse response) {
                        try {
                            if (response.getIsSuccess()) {
                                OTP = response.getData();
                                if (OTP.indexOf("|") > -1) {
                                    String[] arrayString = OTP.split("\\|", -1);
                                    OTP_1 = arrayString[0];
                                    OTP_2 = arrayString[1];
                                    Log.i("OTP_1", OTP_1);
                                    Log.i("OTP_2", OTP_2);
                                    if (!isResend) {
                                        showConfirmationDialog(mode);
                                    }
                                } else {
                                    OTP_1 = response.getData();
                                    OTP_2 = response.getData();
                                    if (!isResend) {
                                        showConfirmationDialog(mode);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        progress.dismiss();
                    }

                    @Override
                    public void onFailure(int requestCode) {
                    }
                });
                controller.getRenewalOTP(REQUEST_OTP, LoginRealmModels.get(0).getUserID(), taskId);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setPaymentModes() {
        try {
            if (planData != null) {
                mAdapter.setData(planData.getPaymentModeList());
                mAdapter.notifyDataSetChanged();
            } else {
                mAdapter.setData(renewalServicePlans.getPaymentModeList());
                mAdapter.notifyDataSetChanged();
            }
            mode = mAdapter.getItem(0).getValue();
            mAdapter.setOnItemClickHandler(position -> {
                mode = mAdapter.getItem(position).getValue();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showConfirmationDialog(String mode) {
        try {
            if (getActivity() != null) {
                RealmResults<LoginResponse> LoginRealmModels =
                        getRealm().where(LoginResponse.class).findAll();
                if (LoginRealmModels != null && LoginRealmModels.size() > 0) {
                    progress.show();
                    final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                    LayoutInflater inflater = getLayoutInflater();
                    final View dialogView = inflater.inflate(R.layout.layout_cheque_details, null);
                    dialogBuilder.setView(dialogView);
                    final AlertDialog alertDialog = dialogBuilder.create();
                    final Button btnOk = (Button) dialogView.findViewById(R.id.btnSubmit);
                    final RelativeLayout relBankName = (RelativeLayout) dialogView.findViewById(R.id.relBankName);
                    final RelativeLayout relChequeDate = (RelativeLayout) dialogView.findViewById(R.id.relChequeDate);
                    final LinearLayout lnrChequeNumber = (LinearLayout) dialogView.findViewById(R.id.lnrChequeNumber);
                    final LinearLayout lnrCheque = (LinearLayout) dialogView.findViewById(R.id.lnrCheque);
                    final Button btnCancel = (Button) dialogView.findViewById(R.id.btnCancel);
                    final EditText edtChequeDate = (EditText) dialogView.findViewById(R.id.edtChequeDate);
                    final EditText edtChequeNo = (EditText) dialogView.findViewById(R.id.edtChequeNo);
                    final EditText edtBankName = (EditText) dialogView.findViewById(R.id.edtBankName);
                    final EditText edtOtp = (EditText) dialogView.findViewById(R.id.edtOtp);
                    final TextView txtResend = (TextView) dialogView.findViewById(R.id.txtResend);
                    final TextView txtTitle = (TextView) dialogView.findViewById(R.id.txtTitle);

                    if (mode.equals("Cheque")) {
                        lnrCheque.setVisibility(View.VISIBLE);
                    } else {
                        lnrCheque.setVisibility(View.GONE);
                    }

                    txtResend.setPaintFlags(txtResend.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

                    txtResend.setOnClickListener(v -> getRenewOtp(true));

                    relBankName.setOnClickListener(v -> {
                        showBankDialog(edtBankName);
                        isValidated(mode, edtBankName, edtChequeDate, edtChequeNo, edtOtp);
                    });

                    relChequeDate.setOnClickListener(v -> {
                        showDatePicker(edtChequeDate);
                        isValidated(mode, edtBankName, edtChequeDate, edtChequeNo, edtOtp);
                    });

                    edtOtp.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            isValidated(mode, edtBankName, edtChequeDate, edtChequeNo, edtOtp);
                        }
                    });

                    edtChequeNo.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            isValidated(mode, edtBankName, edtChequeDate, edtChequeNo, edtOtp);
                            edt_OTP = edtOtp.getText().toString();
                        }
                    });

                    btnOk.setOnClickListener(v -> {
                        if (isValidated(mode, edtBankName, edtChequeDate, edtChequeNo, edtOtp)) {
                            saveRenewalData(mode, "", edtBankName.getText().toString(), edtChequeDate.getText().toString(), edtChequeNo.getText().toString(), edtOtp.getText().toString());
                            alertDialog.dismiss();
                        }
                    });

                    btnCancel.setOnClickListener(v -> {
                        progress.dismiss();
                        alertDialog.dismiss();
                    });
                    if (alertDialog.isShowing()) {
                        progress.dismiss();
                    }

                    alertDialog.show();
                    alertDialog.setCancelable(false);
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    alertDialog.setCanceledOnTouchOutside(false);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isValidated(String mode, EditText edtBankName, EditText edtChequeDate, EditText edtChequeNo, EditText edtOtp) {

        if (mode.equals("Cheque")) {
            if (edtOtp.getText().toString().length() == 0) {
                edtOtp.setError("This field is required!");
                return false;
            } else if (edtOtp.getText().toString().length() < 6) {
                edtOtp.setError("Incorrect OTP!");
                return false;
            } else if (!OTP_1.equals(edtOtp.getText().toString()) && !OTP_2.equals(edtOtp.getText().toString())) {
                edtOtp.setError("Incorrect OTP!");
                return false;
            } else if (edtBankName.getText().toString().length() == 0) {
                edtBankName.setError("Please select bank name!");
                return false;
            } else if (edtChequeDate.getText().toString().length() == 0) {
                edtBankName.setError(null);
                edtChequeDate.setError("Please select cheque date!");
                return false;
            } else if (edtChequeNo.getText().toString().length() == 0) {
                edtBankName.setError(null);
                edtChequeDate.setError(null);
                edtChequeNo.setError("This field is required!");
                return false;
            } else if (edtChequeNo.getText().toString().length() < 6) {
                edtBankName.setError(null);
                edtChequeDate.setError(null);
                edtChequeNo.setError("Incorrect cheque number!");
                return false;
            } else {
                edtOtp.setError(null);
                edtBankName.setError(null);
                edtChequeDate.setError(null);
                edtChequeNo.setError(null);
                return true;
            }
        } else {
            if (edtOtp.getText().toString().length() == 0) {
                edtOtp.setError("This field is required!");
                return false;
            } else if (edtOtp.getText().toString().length() < 6) {
                edtOtp.setError("Incorrect OTP!");
                return false;
            } else if (!OTP_1.equals(edtOtp.getText().toString()) && !OTP_2.equals(edtOtp.getText().toString())) {
                edtOtp.setError("Incorrect OTP!");
                return false;
            } else {
                edtOtp.setError(null);
                edtOtp.setError(null);
                return true;
            }
        }
    }

    private void saveRenewalData(String mode, String txnId, String name, String date, String number, String otp) {
        progress.show();
        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
            try {
                NetworkCallController controller = new NetworkCallController();
                RenewOrderRequest request = new RenewOrderRequest();
                request.setPaymentMode(mode);
                request.setOrderGeneratedFrom("Renewal App");
                request.setComment("");
                request.setOrderSource("Renewal");
                request.setOrderType("Renewal");
                request.setStartDate("");
                request.setAppointmentStartDate("");
                request.setAppointmentEndDate("");
                request.setBankName(name);
                request.setRenewalOtp(otp);
                request.setTransactionId(txnId);
                if (date.length() > 0) {
                    request.setChequeDate(AppUtils.getFormatted(date, "yyyy-MM-dd", "dd-MM-yyyy"));
                } else {
                    request.setChequeDate(date);
                }
                request.setChequeNumber(number);
                request.setCollectedByName(mLoginRealmModels.get(0).getUserName());
                request.setCollectedByID(mLoginRealmModels.get(0).getUserID());
                if (mode.equals("Online Payment Link")) {
                    request.setPaymentType("COD");
                } else if (mode.equalsIgnoreCase("paytm") || mode.equalsIgnoreCase("upi")) {
                    request.setPaymentType("UPI");
                } else {
                    request.setPaymentType(mode);
                }
                if (planData != null) {
                    request.setAccountId(planData.getAccountId());
                    request.setTaskId(planData.getTaskId());
                    request.setOrderNo(planData.getOrderNo());
                    request.setDiscountOffered(Double.parseDouble(planData.getDiscount()));
                    request.setOrderValue(Double.parseDouble(amount));
                    //request.setOrderValue(Double.parseDouble(planData.getDiscountedOrderAmount()));
                    request.setStandardValue(Double.parseDouble(planData.getActualOrderAmount()));
                    request.setSPCode(planData.getSpCode());
                    request.setWallet_Point(utilizedPoints);
                } else {
                    request.setAccountId(renewalServicePlans.getAccountId());
                    request.setTaskId(renewalServicePlans.getTaskId());
                    request.setOrderNo(renewalServicePlans.getOrderNo());
                    request.setDiscountOffered(Double.parseDouble(renewalServicePlans.getDiscount()));
                    request.setOrderValue(Double.parseDouble(amount));
                    //request.setOrderValue(Double.parseDouble(renewalServicePlans.getDiscountedOrderAmount()));
                    request.setStandardValue(Double.parseDouble(renewalServicePlans.getActualOrderAmount()));
                    request.setSPCode(renewalServicePlans.getSpCode());
                    request.setWallet_Point(utilizedPoints);
                }
                controller.setListner(new NetworkResponseListner<List<RenewOrder>>() {

                    @Override
                    public void onResponse(int requestCode, List<RenewOrder> items) {
                        progress.dismiss();
                        if (items != null && items.size() > 0) {
                            if (timer != null) {
                                timer.cancel();
                            }
                            dismiss();
                            showSuccessDialog(items);
                        }
                    }

                    @Override
                    public void onFailure(int requestCode) {
                    }
                });
                controller.renewOrder(REQUEST_RENEW, request);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void showSuccessDialog(List<RenewOrder> items) {
        try {
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.layout_payment_success, null);
            dialogBuilder.setView(dialogView);
            final AlertDialog alertDialog = dialogBuilder.create();
            final Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
            final TextView txtOrderNo = (TextView) dialogView.findViewById(R.id.txtOrderNo);
            final TextView txtUnit = (TextView) dialogView.findViewById(R.id.txtUnit);
            final TextView txtType = (TextView) dialogView.findViewById(R.id.txtType);
            final TextView txtQuantity = (TextView) dialogView.findViewById(R.id.txtQuantity);
            final TextView txtAmount = (TextView) dialogView.findViewById(R.id.txtAmount);
            final TextView txtDate = (TextView) dialogView.findViewById(R.id.txtDate);
            final TextView txtStatus = (TextView) dialogView.findViewById(R.id.txtStatus);
            final ImageView imgSuccess = (ImageView) dialogView.findViewById(R.id.imgSuccess);

            Glide.with(getActivity())
                    .load(R.drawable.tick_marks)
                    .into(new GifDrawableImageViewTarget(imgSuccess, 1));

            txtOrderNo.setText(items.get(0).getOrderNumberC());
            txtUnit.setText(items.get(0).getUnit1C());
            txtType.setText(items.get(0).getSubType1C());
            txtQuantity.setText(items.get(0).getQuantityC());
            txtAmount.setText("\u20B9" + " " + items.get(0).getOrderValueNumericC());
            txtDate.setText(AppUtils.getFormatted(items.get(0).getStartDateC(), "MMM dd, yyyy", "yyyy-MM-dd"));
            txtStatus.setText(items.get(0).getStatusC());

            btnOk.setOnClickListener(v -> {
                try {
                    alertDialog.dismiss();
                    Intent intent = new Intent(mContext, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mContext.startActivity(intent);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            });
            alertDialog.show();
            alertDialog.setCancelable(false);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            alertDialog.setCanceledOnTouchOutside(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showDatePicker(EditText edtChequeDate) {
        try {
            FragmentDatePicker mFragDatePicker = new FragmentDatePicker();
            mFragDatePicker.setmDatePickerListener((view, year, month, day) -> {
                month++;
                chequeDate = "" + day + "-" + month + "-" + year;
                edtChequeDate.setText(chequeDate);
            });
            mFragDatePicker.show(getActivity().getSupportFragmentManager(), "datepicker");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showBankDialog(EditText edtBankName) {
        try {
            progress.show();
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.bank_custom_dialog, null, false);
            dialogBuilder.setView(dialogView);
            final RecyclerView recycle = (RecyclerView) dialogView.findViewById(R.id.recycle);
            final TextView txt_close = (TextView) dialogView.findViewById(R.id.txt_close);
            final SearchView search = (SearchView) dialogView.findViewById(R.id.search);

            if (bankList == null || bankList.size() == 0) {
                NetworkCallController controller = new NetworkCallController();
                controller.setListner(new NetworkResponseListner() {
                    @Override
                    public void onResponse(int requestCode, Object data) {
                        progress.dismiss();
                        List<String> items = (List<String>) data;

                        if (items != null) {
                            RecyclerView.LayoutManager lm = new LinearLayoutManager(getActivity());
                            recycle.setLayoutManager(lm);
                            recycle.setItemAnimator(new DefaultItemAnimator());
                            mBankAdapter = new BankSearchAdapter(getActivity(), items);
                            recycle.setAdapter(mBankAdapter);
                            recycle.addItemDecoration(new MyDividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL, 10));
                            mBankAdapter.onBankSelected((item, position) -> {
                                bankName = item;
                                edtBankName.setText(item);
                                mAlertDialog.dismiss();
                            });
                            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                @Override
                                public boolean onQueryTextSubmit(String query) {
                                    mBankAdapter.getFilter().filter(query);
                                    return false;
                                }

                                @Override
                                public boolean onQueryTextChange(String query) {
                                    mBankAdapter.getFilter().filter(query);
                                    return false;
                                }
                            });
                        } else {
                            RecyclerView.LayoutManager lm = new LinearLayoutManager(getActivity());
                            recycle.setLayoutManager(lm);
                            recycle.setItemAnimator(new DefaultItemAnimator());
                            mBankAdapter = new BankSearchAdapter(getActivity(), bankList);
                            recycle.setAdapter(mBankAdapter);
                            recycle.addItemDecoration(new MyDividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL, 10));
                            mBankAdapter.onBankSelected((item, position) -> {
                                bankName = item;
                                mAdapter.notifyDataSetChanged();
                                mAlertDialog.dismiss();
                            });
                            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                @Override
                                public boolean onQueryTextSubmit(String query) {
                                    mBankAdapter.getFilter().filter(query);
                                    return false;
                                }

                                @Override
                                public boolean onQueryTextChange(String query) {
                                    mBankAdapter.getFilter().filter(query);
                                    return false;
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(int requestCode) {
                    }
                });
                controller.getBanksName(REQUEST_BANK);
            }
            txt_close.setOnClickListener(v -> mAlertDialog.dismiss());

            dialogBuilder.setCancelable(false);
            mAlertDialog = dialogBuilder.create();
            mAlertDialog.setCanceledOnTouchOutside(true);
            Objects.requireNonNull(mAlertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            mAlertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            mAlertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            mAlertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setHygienePoints(){
        mFragmentServicePlanBottomSheetBinding.redeemablePointsTv.setTypeface(mFragmentServicePlanBottomSheetBinding.redeemablePointsTv.getTypeface(), Typeface.BOLD);
        //mFragmentServicePlanBottomSheetBinding.grandTotalTitleTv.setTypeface(mFragmentServicePlanBottomSheetBinding.grandTotalTitleTv.getTypeface(), Typeface.BOLD);
        mFragmentServicePlanBottomSheetBinding.priceTv.setTypeface(mFragmentServicePlanBottomSheetBinding.priceTv.getTypeface(), Typeface.BOLD);
        if (actualRedeemPoints > 0) {
            mFragmentServicePlanBottomSheetBinding.grandTotalLayout.setVisibility(View.VISIBLE);
            mFragmentServicePlanBottomSheetBinding.hygienePointsLayout.setVisibility(View.VISIBLE);
            mFragmentServicePlanBottomSheetBinding.redeemablePointsTitleTv.setText("Hygiene Points");
            mFragmentServicePlanBottomSheetBinding.redeemablePointsTv.setText(" " + actualRedeemPoints);
        }else {
            mFragmentServicePlanBottomSheetBinding.hygienePointsLayout.setVisibility(GONE);
            mFragmentServicePlanBottomSheetBinding.grandTotalLayout.setVisibility(GONE);
        }
    }

    private void getWalletBalance(String taskId){
        progress.show();
        NetworkCallController controller = new NetworkCallController();
        controller.setListner(new NetworkResponseListner<WalletBase>() {
            @Override
            public void onResponse(int requestCode, WalletBase response) {
                progress.dismiss();
                if (response != null){
                    if (response.isSuccess()){
                        if (response.getData() != null) {
                            Log.d("TAG", "" + response);
                            double pointsInWallet = response.getData().getPointsInWallet();
                            double totalRPoints = response.getData().getTotalRedeemablePointsInWallet();
                            int pointsEarned = response.getData().getPointsEarned();
                            redeemablePoints = 3000;
                            mFragmentServicePlanBottomSheetBinding.redeemablePointsTitleTv.setText("Available Hygiene Points");
                            mFragmentServicePlanBottomSheetBinding.redeemablePointsTv.setText("\u20B9 "+redeemablePoints);
                            mFragmentServicePlanBottomSheetBinding.redeemablePointsTitleTv.setVisibility(View.VISIBLE);
                            mFragmentServicePlanBottomSheetBinding.redeemablePointsTv.setVisibility(View.VISIBLE);
                        }else {
                            mFragmentServicePlanBottomSheetBinding.redeemablePointsTitleTv.setVisibility(View.VISIBLE);
                            mFragmentServicePlanBottomSheetBinding.redeemablePointsTv.setVisibility(View.VISIBLE);
                        }
                    }else {

                        mFragmentServicePlanBottomSheetBinding.redeemablePointsTitleTv.setVisibility(View.VISIBLE);
                        mFragmentServicePlanBottomSheetBinding.redeemablePointsTv.setVisibility(View.VISIBLE);
                    }
                }else {

                    mFragmentServicePlanBottomSheetBinding.redeemablePointsTitleTv.setVisibility(View.VISIBLE);
                    mFragmentServicePlanBottomSheetBinding.redeemablePointsTv.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(int requestCode) {
            }
        });
        controller.getWalletBalance(14122021, taskId);
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (timer != null) {
            timer.cancel();
        }
    }
}
