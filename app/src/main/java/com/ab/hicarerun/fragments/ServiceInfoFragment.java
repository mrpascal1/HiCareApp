package com.ab.hicarerun.fragments;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.hicarerun.BaseApplication;
import com.ab.hicarerun.BaseFragment;
import com.ab.hicarerun.BuildConfig;
import com.ab.hicarerun.R;
import com.ab.hicarerun.activities.NewTaskDetailsActivity;
import com.ab.hicarerun.adapter.BankSearchAdapter;
import com.ab.hicarerun.adapter.ChemicalDialogAdapter;
import com.ab.hicarerun.adapter.ChemicalRecycleAdapter;
import com.ab.hicarerun.databinding.FragmentServiceInfoBinding;
import com.ab.hicarerun.handler.OnSaveEventHandler;
import com.ab.hicarerun.handler.UserServiceInfoClickHandler;
import com.ab.hicarerun.network.NetworkCallController;
import com.ab.hicarerun.network.NetworkResponseListner;
import com.ab.hicarerun.network.models.BasicResponse;
import com.ab.hicarerun.network.models.ChemicalModel.Chemicals;
import com.ab.hicarerun.network.models.CovidModel.CovidRequest;
import com.ab.hicarerun.network.models.CovidModel.CovidResponse;
import com.ab.hicarerun.network.models.GeneralModel.GeneralData;
import com.ab.hicarerun.network.models.GeneralModel.GeneralPaymentMode;
import com.ab.hicarerun.network.models.GeneralModel.GeneralTaskStatus;
import com.ab.hicarerun.network.models.GeneralModel.IncompleteReason;
import com.ab.hicarerun.network.models.GeneralModel.OnSiteOtpResponse;
import com.ab.hicarerun.network.models.JeopardyModel.CWFJeopardyRequest;
import com.ab.hicarerun.network.models.JeopardyModel.CWFJeopardyResponse;
import com.ab.hicarerun.network.models.LoginResponse;
import com.ab.hicarerun.network.models.ModelQRCode.CheckCodeResponse;
import com.ab.hicarerun.network.models.ModelQRCode.CheckStatus;
import com.ab.hicarerun.network.models.ModelQRCode.PhonePeData;
import com.ab.hicarerun.network.models.ModelQRCode.PhonePeQRCode;
import com.ab.hicarerun.network.models.ModelQRCode.QRCode;
import com.ab.hicarerun.network.models.ModelQRCode.QRCodeResponse;
import com.ab.hicarerun.network.models.PayementModel.PaymentLinkRequest;
import com.ab.hicarerun.network.models.PayementModel.PaymentLinkResponse;
import com.ab.hicarerun.utils.AppUtils;
import com.ab.hicarerun.utils.GPSUtils;
import com.ab.hicarerun.utils.MyDividerItemDecoration;
import com.bumptech.glide.Glide;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import es.dmoral.toasty.Toasty;
import io.realm.RealmResults;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.view.View.GONE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServiceInfoFragment extends BaseFragment implements UserServiceInfoClickHandler, FragmentDatePicker.onDatePickerListener {

    FragmentServiceInfoBinding mFragmentServiceInfoBinding;
    private static final int POST_PAYMENT_LINK = 1000;
    public static final String ARGS_TASKS = "ARGS_TASKS";
    public static final String ARGS_COMBINED_TASKS = "ARGS_COMBINED_TASKS";
    public static final String ARGS_COMBINED_TYPE = "ARGS_COMBINED_TYPE";
    public static final String ARGS_COMBINED_ORDER = "ARGS_COMBINED_ORDER";
    public static final String ARGS_COMBINED_ID = "ARGS_COMBINED_ID";
    private static final int ONSITE_REQUEST = 1000;
    private static final int REQUEST_BANK = 2000;
    private static final int COMPLETION_REQUEST = 3000;
    private static final int CHEMICAL_REQ = 4000;
    private static final int LESS_PAYMENT_REQ = 5000;
    private static final int QR_CODE_REQ = 6000;
    private static final int UPLOAD_REQ = 7000;
    private static final int CHECK_REQ = 8000;

    private AlertDialog alertDialog;
    private Integer pageNumber = 1;
    private String selectedStatus = "";
    private String status = "";
    private boolean showSandardChemicals = false;
    private boolean isUploadOnsiteImage = false;
    private String OnSiteOtp = "";
    private String PaymentOtp = "";
    private String ScOtp = "";
    private String[] arrayReason = null;
    private String[] arrayStatus = null;
    private Boolean isFeedback = false;
    private Boolean isChequeRequired = false;
    private Boolean isPaymentValidation = false;
    private Boolean isPaymentOtpRequired = false;
    private Boolean isRaiseJeopardyClicked = false;
    private Boolean isPaymentJeopardyRaised = false;
    private int radiopos = 0;
    private String Selection = "";
    private String onsiteTechImage = "";
    private String chequeImg = "";
    private String selectedImagePath = "";
    private Bitmap bitmap;
    private BankSearchAdapter mAdapter;
    private ChemicalDialogAdapter mChemicalAdapter;
    //    private OnSaveEventHandler mCallback;
    private String[] bankNames;
    private List<String> bankList;
    private String[] arrayMode = null;
    private ArrayList<String> type = null;
    private ArrayList<String> images = new ArrayList<>();
    private String mobile = "";
    private String Mode = "";
    private String Email = "";
    private String mask = "";
    private String AmountCollected = "";
    private int AmountToCollect = 0;
    private String sta = "";
    private String taskId = "";
    private String combiedTaskOrders = "";
    private String combinedTypes = "";
    private boolean isCombinedTask = false;
    private RealmResults<GeneralData> mTaskDetailsData = null;
    private RealmResults<GeneralTaskStatus> generalTaskRealmModel;
    private RealmResults<IncompleteReason> ReasonRealmModel = null;
    private RealmResults<GeneralPaymentMode> mPaymentRealmModel;
    private AlertDialog mAlertDialog = null;
    private OnSaveEventHandler mCallback;
    private String combinedTaskId = "";
    File mPhotoFile;
    static final int REQUEST_TAKE_PHOTO = 1;
    private Timer timer = new Timer();
    private boolean hasStarted = false;
    private String UpiTransactionId = "";

    public static ServiceInfoFragment newInstance(String taskId, String combinedTaskId, boolean isCombinedTasks, String combinedTypes, String combinedOrders) {
        Bundle args = new Bundle();
        args.putString(ARGS_TASKS, taskId);
        args.putBoolean(ARGS_COMBINED_TASKS, isCombinedTasks);
        args.putString(ARGS_COMBINED_ORDER, combinedOrders);
        args.putString(ARGS_COMBINED_TYPE, combinedTypes);
        args.putString(ARGS_COMBINED_ID, combinedTaskId);
        ServiceInfoFragment fragment = new ServiceInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public ServiceInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onSaveInstanceState(Bundle oldInstanceState) {
        super.onSaveInstanceState(oldInstanceState);
        oldInstanceState.clear();
    }


    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnSaveEventHandler) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement FragmentToActivity");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            taskId = getArguments().getString(ARGS_TASKS);
            combinedTaskId = getArguments().getString(ARGS_COMBINED_ID);
            combiedTaskOrders = getArguments().getString(ARGS_COMBINED_ORDER);
            combinedTypes = getArguments().getString(ARGS_COMBINED_TYPE);
            isCombinedTask = getArguments().getBoolean(ARGS_COMBINED_TASKS);
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFragmentServiceInfoBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_service_info, container, false);
        mFragmentServiceInfoBinding.setHandler(this);
        return mFragmentServiceInfoBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        bankList = new ArrayList<>();
        getServiceDetail();
        getPaymentData();
        mTaskDetailsData =
                getRealm().where(GeneralData.class).findAll();
        if (mTaskDetailsData != null && mTaskDetailsData.size() > 0) {
            AmountToCollect = Integer.parseInt(mTaskDetailsData.get(0).getAmountToCollect());
        }

        mFragmentServiceInfoBinding.edtOnsiteOtp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                getServiceValidate();
            }
        });

        mFragmentServiceInfoBinding.txtCollected.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mCallback.amountCollected(s.toString());
                getValidated(AmountToCollect);
            }
        });

        mFragmentServiceInfoBinding.txtChequeNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                getValidated(AmountToCollect);
                mCallback.chequeNumber(s.toString());
            }
        });

        mFragmentServiceInfoBinding.edtPaymentOTP.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                getValidated(AmountToCollect);
            }
        });

        mFragmentServiceInfoBinding.btnOnsiteOtp.setOnClickListener(view1 -> getCommercialDialog());


    }

    private void getCommercialDialog() {

        try {
            if (getActivity() != null) {
                RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {

                    LayoutInflater li = LayoutInflater.from(getActivity());

                    View promptsView = li.inflate(R.layout.layout_commercial_dialog, null);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

                    alertDialogBuilder.setView(promptsView);
                    final AlertDialog alertDialog = alertDialogBuilder.create();

                    final EditText edtmobile =
                            promptsView.findViewById(R.id.edtMobile);
                    final EditText edtName =
                            promptsView.findViewById(R.id.edtusername);
                    final AppCompatButton btn_send =
                            promptsView.findViewById(R.id.btn_send);
                    final AppCompatButton btn_cancel =
                            promptsView.findViewById(R.id.btn_cancel);
                    final String resourceId = mLoginRealmModels.get(0).getUserID();
                    final String mobileNo = mLoginRealmModels.get(0).getPhoneNumber();

                    btn_send.setOnClickListener(v -> {
                        if (isOnSiteValidate(edtmobile, edtName)) {
                            if (!mobileNo.equalsIgnoreCase(edtmobile.getText().toString())) {
                                NetworkCallController controller = new NetworkCallController(ServiceInfoFragment.this);
                                controller.setListner(new NetworkResponseListner() {
                                    @Override
                                    public void onResponse(int requestCode, Object data) {
                                        OnSiteOtpResponse response = (OnSiteOtpResponse) data;
                                        if (response.getSuccess()) {
                                            Toasty.success(getActivity(), getString(R.string.on_site_otp_is_sent_successfully)).show();
                                            alertDialog.dismiss();
                                        } else {
                                            Toasty.error(getActivity(), response.getErrorMessage()).show();
                                            alertDialog.dismiss();
                                        }
                                    }

                                    @Override
                                    public void onFailure(int requestCode) {
                                    }
                                });
                                controller.getOnSiteOTP(ONSITE_REQUEST, resourceId, taskId, edtName.getText().toString(), edtmobile.getText().toString());
                            } else {
                                Toasty.error(getActivity(), getString(R.string.mobile_number_is_invalid)).show();
                            }
                        }
                    });
                    btn_cancel.setOnClickListener(v -> alertDialog.dismiss());
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private boolean isOnSiteValidate(EditText edtmobile, EditText edtName) {
        if (edtName.getText().toString().length() == 0) {
            edtName.setError(getString(R.string.this_field_is_required));
            return false;
        } else if (edtmobile.getText().toString().length() == 0) {
            edtmobile.setError(getString(R.string.this_field_is_required));
            return false;
        } else if (edtmobile.getText().toString().length() < 10) {
            edtmobile.setError(getString(R.string.mobile_number_is_invalid));
            return false;
        } else {
            return true;
        }
    }


    private void getPaymentData() {
        try {
            mTaskDetailsData =
                    getRealm().where(GeneralData.class).findAll();
            if (mTaskDetailsData != null && mTaskDetailsData.size() > 0) {
                mobile = mTaskDetailsData.get(0).getMobileNumber();
                assert mTaskDetailsData.get(0) != null;
                Email = mTaskDetailsData.get(0).getEmail();
                try {
                    if (mobile != null && mobile.length() > 0)
                        mask = mobile.replaceAll("\\w(?=\\w{4})", "*");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                chequeImg = mTaskDetailsData.get(0).getChequeImageUrl();
                AmountCollected = mTaskDetailsData.get(0).getAmountCollected();
                AmountToCollect = Integer.parseInt(mTaskDetailsData.get(0).getAmountToCollect());
                mFragmentServiceInfoBinding.txtAmountToCollect.setText("₹ " + AmountToCollect);
                mPaymentRealmModel = getRealm().where(GeneralPaymentMode.class).findAll().sort("Value");
                type = new ArrayList<>();
                type.clear();
                for (GeneralPaymentMode generalPaymentMode : mPaymentRealmModel) {
                    type.add(generalPaymentMode.getValue());
                }

                isPaymentValidation = mTaskDetailsData.get(0).getPaymentValidation();
                isPaymentOtpRequired = mTaskDetailsData.get(0).getPayment_Otp_Required();
                isPaymentJeopardyRaised = mTaskDetailsData.get(0).getPayment_Jeopardy_Raised();

                isChequeRequired = mTaskDetailsData.get(0).getChequeRequired();
                type.add(0, "None");
                Log.i("type", String.valueOf(type.size()));
                arrayMode = new String[type.size()];
                arrayMode = type.toArray(arrayMode);
                Log.i("payment", Arrays.toString(arrayMode));
                final String status = mTaskDetailsData.get(0).getSchedulingStatus();
                sta = mTaskDetailsData.get(0).getSchedulingStatus();


                if (isPaymentOtpRequired && sta.equals("On-Site") && AmountToCollect != 0) {
                    mFragmentServiceInfoBinding.btnPaymentJeopardy.setVisibility(View.VISIBLE);
                } else {
                    mFragmentServiceInfoBinding.btnPaymentJeopardy.setVisibility(GONE);
                    mFragmentServiceInfoBinding.lnrPaymentOTP.setVisibility(GONE);
                }

                if (isPaymentJeopardyRaised && sta.equals("On-Site")) {
                    getValidated(AmountToCollect);
                }
                mFragmentServiceInfoBinding.btnPaymentJeopardy.setOnClickListener(view -> {

//                    Mode = type.get(0);
                    getValidated(AmountToCollect);
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Raise Payment Jeopardy")
                            .setMessage("Do you really want to raise payment jeopardy?")
                            .setIcon(R.drawable.ic_caution)
                            .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> getLessPaymentJeopardy())
                            .setNegativeButton(android.R.string.no, null).show();
                });

                try {
                    if (mTaskDetailsData.get(0).getChequeImageUrl() != null && mTaskDetailsData.get(0).getChequeImageUrl().length() != 0) {
                        mFragmentServiceInfoBinding.lnrUpload.setVisibility(View.GONE);
                        mFragmentServiceInfoBinding.cardUpload.setVisibility(View.VISIBLE);
                        Glide.with(getActivity())
                                .load(mTaskDetailsData.get(0).getChequeImageUrl())
                                .error(android.R.drawable.stat_notify_error)
                                .into(mFragmentServiceInfoBinding.imgChequeUploaded);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (status.equalsIgnoreCase("Dispatched")) {
                    mFragmentServiceInfoBinding.lnrPayment.setVisibility(View.GONE);
                    mFragmentServiceInfoBinding.lnrCollected.setVisibility(View.GONE);
                    mFragmentServiceInfoBinding.lnrCheque.setVisibility(View.GONE);
                    mFragmentServiceInfoBinding.btnSendPaymentLink.setVisibility(GONE);
                } else {
                    mFragmentServiceInfoBinding.lnrPayment.setVisibility(View.VISIBLE);
                    mFragmentServiceInfoBinding.lnrCollected.setVisibility(View.VISIBLE);
                    mFragmentServiceInfoBinding.lnrCheque.setVisibility(View.VISIBLE);
                    mFragmentServiceInfoBinding.btnSendPaymentLink.setVisibility(View.VISIBLE);
                }

                if (AmountToCollect == 0) {
                    mFragmentServiceInfoBinding.txtCollected.setEnabled(false);
                    mFragmentServiceInfoBinding.lnrCollected.setVisibility(GONE);
                    type.clear();
                    type.add(0, "None");
                    arrayMode = new String[type.size()];
                    arrayMode = type.toArray(arrayMode);
                } else if (status.equals("Completed")) {
                    try {
                        mFragmentServiceInfoBinding.txtCollected.setEnabled(false);
                        mFragmentServiceInfoBinding.lnrBank.setEnabled(false);
                        mFragmentServiceInfoBinding.lnrDate.setEnabled(false);
                        mFragmentServiceInfoBinding.lnrCollected.setVisibility(View.VISIBLE);
                        mFragmentServiceInfoBinding.txtChequeNo.setEnabled(false);
                        mFragmentServiceInfoBinding.txtCollected.setText(AmountCollected);
                        mFragmentServiceInfoBinding.txtChequeNo.setText(mTaskDetailsData.get(0).getChequeNo());
                        mFragmentServiceInfoBinding.txtBankname.setText(mTaskDetailsData.get(0).getBankName());
                        if (mTaskDetailsData.get(0).getChequeDate() != null) {
                            try {
                                mFragmentServiceInfoBinding.txtDate.setText(AppUtils.reFormatDateTime(mTaskDetailsData.get(0).getChequeDate(), "dd-MMM-yyyy"));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        type.clear();
                        type.add(0, mTaskDetailsData.get(0).getPaymentMode());
                        arrayMode = new String[type.size()];
                        arrayMode = type.toArray(arrayMode);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (status.equals("Incomplete")) {
                    mFragmentServiceInfoBinding.txtCollected.setEnabled(false);
                    mFragmentServiceInfoBinding.lnrCollected.setVisibility(View.VISIBLE);
                    mFragmentServiceInfoBinding.lnrBank.setEnabled(false);
                    mFragmentServiceInfoBinding.lnrDate.setEnabled(false);
                    mFragmentServiceInfoBinding.txtChequeNo.setEnabled(false);
                    type.clear();
                    type.add(0, "None");
                    arrayMode = new String[type.size()];
                    arrayMode = type.toArray(arrayMode);
                }
                try {
                    ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(getActivity(),
                            R.layout.spinner_layout_new, arrayMode);
                    statusAdapter.setDropDownViewResource(R.layout.spinner_popup);
                    mFragmentServiceInfoBinding.spnPaymentMode.setAdapter(statusAdapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                mFragmentServiceInfoBinding.spnPaymentMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        try {
                            mFragmentServiceInfoBinding.btnSendPaymentLink.setVisibility(GONE);
                            Mode = mFragmentServiceInfoBinding.spnPaymentMode.getSelectedItem().toString();
                            if (Mode.equals("None")) {
                                mCallback.mode("");
                            } else {
                                InputMethodManager imm = (InputMethodManager) Objects.requireNonNull(getActivity()).getSystemService(getActivity().INPUT_METHOD_SERVICE);
                                assert imm != null;
                                imm.showSoftInput(mFragmentServiceInfoBinding.edtPaymentOTP, InputMethodManager.SHOW_IMPLICIT);
                                mFragmentServiceInfoBinding.edtPaymentOTP.requestFocus();
                                mCallback.mode(Mode);
                            }
                            mCallback.amountToCollect(String.valueOf(AmountToCollect));

                            if (mFragmentServiceInfoBinding.spnPaymentMode.getSelectedItem().toString().equals("Cheque")) {
                                mFragmentServiceInfoBinding.lnrCheque.setVisibility(View.VISIBLE);
                                mFragmentServiceInfoBinding.lnrCollected.setVisibility(View.VISIBLE);
                                InputMethodManager imm = (InputMethodManager) Objects.requireNonNull(getActivity()).getSystemService(getActivity().INPUT_METHOD_SERVICE);
                                assert imm != null;
                                imm.showSoftInput(mFragmentServiceInfoBinding.edtPaymentOTP, InputMethodManager.SHOW_IMPLICIT);
                                mFragmentServiceInfoBinding.edtPaymentOTP.requestFocus();
                                imm.showSoftInput(mFragmentServiceInfoBinding.txtCollected, InputMethodManager.SHOW_IMPLICIT);
                                mFragmentServiceInfoBinding.txtCollected.requestFocus();
                                getValidated(AmountToCollect);
                            } else {
                                mFragmentServiceInfoBinding.lnrCheque.setVisibility(View.GONE);
                            }

                            if (mFragmentServiceInfoBinding.spnPaymentMode.getSelectedItem().toString().equalsIgnoreCase("Online Payment Link")) {
                                if (sta.equals("On-Site")) {
                                    sendPaymentLink();
                                }
                            }
                            if (mFragmentServiceInfoBinding.spnPaymentMode.getSelectedItem().toString().equalsIgnoreCase("paytm")) {
                                mFragmentServiceInfoBinding.txtPaymentStatus.setText("Pending...");
                                mFragmentServiceInfoBinding.imgStatus.setImageResource(R.drawable.ic_processing);
                                mFragmentServiceInfoBinding.txtPaymentStatus.setTextColor(getResources().getColor(R.color.orange));
                                loadQRCOde();
                            } else if (mFragmentServiceInfoBinding.spnPaymentMode.getSelectedItem().toString().equalsIgnoreCase("phonepay")) {
                                mFragmentServiceInfoBinding.txtPaymentStatus.setText("Pending...");
                                mFragmentServiceInfoBinding.imgStatus.setImageResource(R.drawable.ic_processing);
                                mFragmentServiceInfoBinding.txtPaymentStatus.setTextColor(getResources().getColor(R.color.orange));
                                loadPhonePeCode();
                            } else {
                                if (hasStarted) {
                                    timer.cancel();
                                }
                                hasStarted = false;
                                mFragmentServiceInfoBinding.lnrQrCode.setVisibility(GONE);
                            }

//                            if (mFragmentServiceInfoBinding.spnPaymentMode.getSelectedItem().toString().equals("PayTm")) {
//                                mFragmentServiceInfoBinding.lnrQrCode.setVisibility(View.VISIBLE);
//                                getValidated(AmountToCollect);
//                                Picasso.get().load("http://52.74.65.15/MobileApi/images/PayTm.png").into(mFragmentServiceInfoBinding.imgPayscanner);
//                            } else if (mFragmentServiceInfoBinding.spnPaymentMode.getSelectedItem().toString().equals("Google Pay")) {
//                                mFragmentServiceInfoBinding.lnrQrCode.setVisibility(View.VISIBLE);
//                                getValidated(AmountToCollect);
//                                Picasso.get().load("http://52.74.65.15/MobileApi/images/gpay.png").into(mFragmentServiceInfoBinding.imgPayscanner);
//                            } else if (mFragmentServiceInfoBinding.spnPaymentMode.getSelectedItem().toString().equals("PhonePe")) {
//                                mFragmentServiceInfoBinding.lnrQrCode.setVisibility(View.VISIBLE);
//                                getValidated(AmountToCollect);
//                                Picasso.get().load("http://52.74.65.15/MobileApi/images/PhonePay.png").into(mFragmentServiceInfoBinding.imgPayscanner);
//                            } else {
//                                mFragmentServiceInfoBinding.lnrQrCode.setVisibility(View.GONE);
//                            }
                            if (mFragmentServiceInfoBinding.spnPaymentMode.getSelectedItem().toString().equals("None")) {
                                getValidated(AmountToCollect);
                                mFragmentServiceInfoBinding.txtCollected.setText("");
                                mFragmentServiceInfoBinding.txtCollected.setEnabled(false);
                                mFragmentServiceInfoBinding.lnrCollected.setVisibility(GONE);
                            }
                            if (mFragmentServiceInfoBinding.spnPaymentMode.getSelectedItem().toString().equals("Online Payment Link")) {
                                getValidated(AmountToCollect);
                                mFragmentServiceInfoBinding.txtCollected.setText("");
                                mFragmentServiceInfoBinding.txtCollected.setEnabled(false);
                                mFragmentServiceInfoBinding.lnrCollected.setVisibility(View.GONE);
                            }
                            if (mFragmentServiceInfoBinding.spnPaymentMode.getSelectedItem().toString().equals("Cash")) {
                                InputMethodManager imm = (InputMethodManager) Objects.requireNonNull(getActivity()).getSystemService(getActivity().INPUT_METHOD_SERVICE);
                                assert imm != null;
                                imm.showSoftInput(mFragmentServiceInfoBinding.edtPaymentOTP, InputMethodManager.SHOW_IMPLICIT);
                                mFragmentServiceInfoBinding.edtPaymentOTP.requestFocus();
                                imm.showSoftInput(mFragmentServiceInfoBinding.txtCollected, InputMethodManager.SHOW_IMPLICIT);
                                mFragmentServiceInfoBinding.lnrCollected.setVisibility(View.VISIBLE);
                                mFragmentServiceInfoBinding.txtCollected.requestFocus();
                                getValidated(AmountToCollect);
                            }
                        } catch (Exception e) {
                            RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                            if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                AppUtils.sendErrorLogs(e.getMessage(), getClass().getSimpleName(), "getPaymentData", lineNo, userName, DeviceName);
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadPhonePeCode() {
        if ((NewTaskDetailsActivity) getActivity() != null) {
            mTaskDetailsData = getRealm().where(GeneralData.class).findAll();
            if (mTaskDetailsData != null && mTaskDetailsData.size() > 0) {
                String accountNo = mTaskDetailsData.get(0).getAccountId();
                String amount = "10";
                String orderNo = mTaskDetailsData.get(0).getOrderNumber();
                String source = "HICARERUN";
                NetworkCallController controller = new NetworkCallController(ServiceInfoFragment.this);
                controller.setListner(new NetworkResponseListner<PhonePeQRCode>() {

                    @Override
                    public void onResponse(int requestCode, PhonePeQRCode response) {
                        mFragmentServiceInfoBinding.lnrQrCode.setVisibility(View.VISIBLE);
                        mFragmentServiceInfoBinding.lnrCollected.setVisibility(GONE);
                        Picasso.get().load(response.getQRImageUrl()).into(mFragmentServiceInfoBinding.imgPayscanner);
                        checkPhonePeStatus(response.getTransactionId(), response.getOrderNo());
                        getValidated(AmountToCollect);
                    }

                    @Override
                    public void onFailure(int requestCode) {
                    }
                });
                controller.getPhonePayCode(QR_CODE_REQ, taskId, accountNo, orderNo, amount, source);
            }
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

    private void checkPhonePeStatus(String transactionId, String orderId) {
        try {
            hasStarted = true;
            timer.scheduleAtFixedRate(new TimerTask() {
                                          @Override
                                          public void run() {
                                              checkPhoneStatus(transactionId, orderId, timer);
                                          }
                                      },
                    0, 15000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkPhoneStatus(String transactionId, String orderId, Timer timer) {
        try {
            NetworkCallController controller = new NetworkCallController();
            controller.setListner(new NetworkResponseListner<PhonePeData>() {
                @Override
                public void onResponse(int requestCode, PhonePeData response) {
                    if (response != null) {
                        if (response.getPaymentStatus().equals("TXN_SUCCESS")) {
                            mCallback.isUPIPaymentDone(true);
                            mFragmentServiceInfoBinding.txtPaymentStatus.setText("Payment Successfull");
                            mFragmentServiceInfoBinding.imgStatus.setImageResource(R.drawable.ic_routine_done);
                            mFragmentServiceInfoBinding.txtPaymentStatus.setTextColor(getResources().getColor(R.color.colorPrimary));
                            timer.cancel();
                        } else {
                            mFragmentServiceInfoBinding.txtPaymentStatus.setText("Pending...");
                            mFragmentServiceInfoBinding.imgStatus.setImageResource(R.drawable.ic_processing);
                            mFragmentServiceInfoBinding.txtPaymentStatus.setTextColor(getResources().getColor(R.color.yellow2));
                        }
                    }

                }

                @Override
                public void onFailure(int requestCode) {

                }
            });
            controller.checkPhonePeStatus(CHECK_REQ, taskId, transactionId, orderId);
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
                    if (response != null) {
                        if (response.getSTATUS().equals("TXN_SUCCESS")) {
                            mFragmentServiceInfoBinding.txtPaymentStatus.setText("Payment Successfull");
                            mCallback.isUPIPaymentDone(true);
                            mFragmentServiceInfoBinding.imgStatus.setImageResource(R.drawable.ic_routine_done);
                            mFragmentServiceInfoBinding.txtPaymentStatus.setTextColor(getResources().getColor(R.color.colorPrimary));
                            timer.cancel();
                        } else {
                            mFragmentServiceInfoBinding.txtPaymentStatus.setText("Pending...");
                            mFragmentServiceInfoBinding.imgStatus.setImageResource(R.drawable.ic_processing);
                            mFragmentServiceInfoBinding.txtPaymentStatus.setTextColor(getResources().getColor(R.color.yellow2));
                        }
                    }

                }

                @Override
                public void onFailure(int requestCode) {

                }
            });
            controller.checkPaymentStatus(CHECK_REQ, orderId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadQRCOde() {
        if ((NewTaskDetailsActivity) getActivity() != null) {
            mTaskDetailsData = getRealm().where(GeneralData.class).findAll();
            if (mTaskDetailsData != null && mTaskDetailsData.size() > 0) {
                String accountNo = mTaskDetailsData.get(0).getAccountId();
                String amount = mTaskDetailsData.get(0).getAmountToCollect();
                String orderNo = mTaskDetailsData.get(0).getOrderNumber();
                String source = "HICARERUN";
                NetworkCallController controller = new NetworkCallController(ServiceInfoFragment.this);
                controller.setListner(new NetworkResponseListner<QRCode>() {

                    @Override
                    public void onResponse(int requestCode, QRCode response) {
                        mFragmentServiceInfoBinding.lnrQrCode.setVisibility(View.VISIBLE);
                        mFragmentServiceInfoBinding.lnrCollected.setVisibility(GONE);
                        Picasso.get().load(response.getUrl()).into(mFragmentServiceInfoBinding.imgPayscanner);
                        checkPaymentStatus(response.getOrderId());
                        getValidated(AmountToCollect);
                    }

                    @Override
                    public void onFailure(int requestCode) {
                    }
                });
                controller.getUPICode(QR_CODE_REQ, taskId, accountNo, orderNo, amount, source);
            }
        }
    }


    private void getLessPaymentJeopardy() {
        NetworkCallController controller = new NetworkCallController(this);
        CWFJeopardyRequest request = new CWFJeopardyRequest();
        request.setTaskId(taskId);
        request.setJeopardyText("Less Payment Jeopardy");
        request.setBatchName("Less_Payment_Jeopardy");
        request.setRemark("Less Payment Jeopardy");
        controller.setListner(new NetworkResponseListner<CWFJeopardyResponse>() {
            @Override
            public void onResponse(int requestCode, CWFJeopardyResponse response) {
                if (response.getSuccess()) {
                    isRaiseJeopardyClicked = true;
                    mFragmentServiceInfoBinding.lnrPaymentOTP.setVisibility(View.VISIBLE);
                    getValidated(AmountToCollect);
                    Toast.makeText(getActivity(), response.getResponseMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), response.getResponseMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int requestCode) {

            }
        });
        controller.insertLessPaymentJeopardy(LESS_PAYMENT_REQ, request);
    }

    private void getServiceDetail() {
        try {
            if ((NewTaskDetailsActivity) getActivity() != null) {
                mTaskDetailsData = getRealm().where(GeneralData.class).findAll();
                if (mTaskDetailsData != null && mTaskDetailsData.size() > 0) {
                    status = mTaskDetailsData.get(0).getSchedulingStatus();
                    showSandardChemicals = mTaskDetailsData.get(0).getShow_Standard_Chemicals();
                    isUploadOnsiteImage = mTaskDetailsData.get(0).getOnsite_Image_Required();
                    onsiteTechImage = mTaskDetailsData.get(0).getOnsite_Image_Path();
                    String order = mTaskDetailsData.get(0).getOrderNumber();
                    String duration = mTaskDetailsData.get(0).getDuration();
                    String start = mTaskDetailsData.get(0).getTaskAssignmentStartTime();
                    mFragmentServiceInfoBinding.txtConsIns.setTypeface(mFragmentServiceInfoBinding.txtConsIns.getTypeface(), Typeface.BOLD);
                    mFragmentServiceInfoBinding.txtView.setTypeface(mFragmentServiceInfoBinding.txtView.getTypeface(), Typeface.BOLD);

                    if (mTaskDetailsData.get(0).getConsultationInspectionRequired() && status.equals("On-Site")) {
                        mFragmentServiceInfoBinding.lnrConsIns.setVisibility(View.VISIBLE);
                    } else {
                        mFragmentServiceInfoBinding.lnrConsIns.setVisibility(View.GONE);
                    }

                    if (isCombinedTask) {
                        String type = combinedTypes.replace(",", ", ");
                        mFragmentServiceInfoBinding.txtType.setText(type);
                    } else {
                        String type = mTaskDetailsData.get(0).getServiceType();
                        mFragmentServiceInfoBinding.txtType.setText(type);
                    }

                    String[] split_sDuration = duration.split(":");
                    String hr_duration = split_sDuration[0];
                    String mn_duration = split_sDuration[1];

                    if (isUploadOnsiteImage && onsiteTechImage != null && !onsiteTechImage.equals("")) {
                        mFragmentServiceInfoBinding.imgUser.setVisibility(View.VISIBLE);
                        Picasso.get().load(onsiteTechImage).into(mFragmentServiceInfoBinding.imgUser);
                    } else {
                        mFragmentServiceInfoBinding.imgUser.setVisibility(GONE);
                    }
                    if (hr_duration.equals("00")) {
                        mFragmentServiceInfoBinding.txtDuration.setText(duration + " min");
                    } else {
                        mFragmentServiceInfoBinding.txtDuration.setText(duration + " hr");
                    }
                    if (isCombinedTask) {
                        String service = combiedTaskOrders.replace(",", ", ");
                        mFragmentServiceInfoBinding.txtOrder.setText(getString(R.string.service_order) + "# " + service);
                    } else {
                        mFragmentServiceInfoBinding.txtOrder.setText(getString(R.string.service_order) + "# " + order);
                    }

                    mFragmentServiceInfoBinding.txtStartTime.setText(start);

                    getStatus();
                    setDefaultReason();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getStatus() {
        try {
            assert mTaskDetailsData.get(0) != null;
            selectedStatus = mTaskDetailsData.get(0).getSchedulingStatus();
            generalTaskRealmModel = getRealm().where(GeneralTaskStatus.class).findAll().sort("Status");
            final ArrayList<String> type = new ArrayList<>();

            for (GeneralTaskStatus generalTaskStatus : generalTaskRealmModel) {
                type.add(generalTaskStatus.getStatus());
            }
            arrayStatus = new String[type.size()];
            arrayStatus = type.toArray(arrayStatus);

            try {
                ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(Objects.requireNonNull(getActivity()),
                        R.layout.spinner_layout_new, arrayStatus);
                statusAdapter.setDropDownViewResource(R.layout.spinner_popup);
                mFragmentServiceInfoBinding.spnStatus.setAdapter(statusAdapter);
            } catch (Exception e) {
                e.printStackTrace();
            }

            mFragmentServiceInfoBinding.spnStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        Mode = mFragmentServiceInfoBinding.spnStatus.getSelectedItem().toString();

                        assert mTaskDetailsData.get(0) != null;
                        isFeedback = mTaskDetailsData.get(0).getFeedBack();
                        try {
                            assert generalTaskRealmModel.get(position) != null;
                            mCallback.status(generalTaskRealmModel.get(position).getStatus());
                            if (generalTaskRealmModel.get(position).getStatus().equals("Incomplete")) {
                                mFragmentServiceInfoBinding.lnrIncomplete.setVisibility(View.VISIBLE);
                                if (mFragmentServiceInfoBinding.txtReason.getText().toString().equals("Select Reason")) {
                                    mCallback.isIncompleteReason(true);
                                    mCallback.getIncompleteReason("");

                                } else {
                                    mCallback.isIncompleteReason(false);
                                    mCallback.getIncompleteReason(mFragmentServiceInfoBinding.txtReason.getText().toString());
                                }
                            } else {
                                mFragmentServiceInfoBinding.lnrIncomplete.setVisibility(View.GONE);
                            }
                            mCallback.duration(mTaskDetailsData.get(0).getDuration());
                            if (selectedStatus.equals(generalTaskRealmModel.get(position).getStatus())) {
                                mCallback.isGeneralChanged(true);
                                mCallback.status(generalTaskRealmModel.get(position).getStatus());
                            } else {
                                mCallback.isGeneralChanged(false);
                                mCallback.status(generalTaskRealmModel.get(position).getStatus());
                            }

                            if (Mode.equals("On-Site") && status.equals("Dispatched")) {
                                if (isUploadOnsiteImage && (onsiteTechImage == null || onsiteTechImage.equals(""))) {
                                    captureTechImage();
                                } else if (showSandardChemicals) {
                                    showChemicalsDialog();
                                }

                            }

                            if (Mode.equals("On-Site") && status.equals("Dispatched") && showSandardChemicals) {
                                showChemicalsDialog();
                            }


                            getServiceValidate();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            for (int i = 0; i < generalTaskRealmModel.size(); i++) {
                assert generalTaskRealmModel.get(i) != null;
                if (selectedStatus.equals(generalTaskRealmModel.get(i).getStatus())) {
                    mFragmentServiceInfoBinding.spnStatus.setSelection(i);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void captureTechImage() {
        try {

            LayoutInflater li = LayoutInflater.from(getActivity());

            View promptsView = li.inflate(R.layout.layout_covid_mask_dialog, null);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

            alertDialogBuilder.setView(promptsView);
            alertDialog = alertDialogBuilder.create();

            final TextView txtCapture =
                    promptsView.findViewById(R.id.txtCapture);

            txtCapture.setTypeface(null, Typeface.BOLD);

            int[] attrs = new int[]{R.attr.selectableItemBackground};
            TypedArray typedArray = getActivity().obtainStyledAttributes(attrs);
            int backgroundResource = typedArray.getResourceId(0, 0);
            txtCapture.setBackgroundResource(backgroundResource);
            txtCapture.setOnClickListener(view -> {
                requestStoragePermission(true);
            });
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setCancelable(false);
            alertDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void requestStoragePermission(boolean isCamera) {
        try {
            Dexter.withActivity(getActivity())
                    .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            // check if all permissions are granted
                            if (report.areAllPermissionsGranted()) {
                                if (isCamera) {
                                    dispatchTakePictureIntent();
                                }
                            }
                            // check for permanent denial of any permission
                            if (report.isAnyPermissionPermanentlyDenied()) {
                                // show alert dialog navigating to Settings
                                showSettingsDialog();
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions,
                                                                       PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    })
                    .withErrorListener(
                            error -> Toast.makeText(getActivity(), "Error occurred! ", Toast.LENGTH_SHORT)
                                    .show())
                    .onSameThread()
                    .check();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void dispatchTakePictureIntent() {
        try {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    // Error occurred while creating the File
                }
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(getActivity(),
                            BuildConfig.APPLICATION_ID + ".provider",
                            photoFile);
                    mPhotoFile = photoFile;
                    takePictureIntent.putExtra("android.intent.extras.CAMERA_FACING", Camera.CameraInfo.CAMERA_FACING_FRONT);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void uploadOnsiteImage(String base64) {
        RealmResults<LoginResponse> LoginRealmModels =
                BaseApplication.getRealm().where(LoginResponse.class).findAll();
        if (LoginRealmModels != null && LoginRealmModels.size() > 0) {
            String UserId = LoginRealmModels.get(0).getUserID();
            CovidRequest request = new CovidRequest();
            request.setResourceId(UserId);
            request.setTaskId(taskId);
            request.setFile(base64);
            NetworkCallController controller = new NetworkCallController(this);
            controller.setListner(new NetworkResponseListner<CovidResponse>() {
                @Override
                public void onResponse(int requestCode, CovidResponse response) {
                    if (response.getIsSuccess()) {
                        mFragmentServiceInfoBinding.imgUser.setVisibility(View.VISIBLE);
                        Picasso.get().load(response.getData()).into(mFragmentServiceInfoBinding.imgUser);
                    } else {
                        mFragmentServiceInfoBinding.imgUser.setVisibility(GONE);
                        Toasty.error(getActivity(), response.getErrorMessage(), Toasty.LENGTH_SHORT).show();
                        captureTechImage();
                    }
                }

                @Override
                public void onFailure(int requestCode) {

                }
            });
            controller.uploadOnsiteImage(UPLOAD_REQ, request);

        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String mFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File mFile = File.createTempFile(mFileName, ".jpg", storageDir);
        return mFile;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (resultCode == RESULT_OK) {
                if (requestCode == REQUEST_TAKE_PHOTO) {
                    selectedImagePath = mPhotoFile.getPath();
                    if (selectedImagePath != null || !selectedImagePath.equals("")) {
                        Bitmap bit = new BitmapDrawable(getResources(),
                                selectedImagePath).getBitmap();
                        int i = (int) (bit.getHeight() * (1024.0 / bit.getWidth()));
                        bitmap = Bitmap.createScaledBitmap(bit, 1024, i, true);
                    }

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] b = baos.toByteArray();
                    String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                    alertDialog.dismiss();
                    uploadOnsiteImage(encodedImage);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void showSettingsDialog() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Need Permissions");
            builder.setMessage(
                    "This app needs permission to use this feature. You can grant them in app settings.");
            builder.setPositiveButton("GOTO SETTINGS", (dialog, which) -> {
                dialog.cancel();
                openSettings();
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // navigating user to app settings
    private void openSettings() {
        try {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
            intent.setData(uri);
            startActivityForResult(intent, 101);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showChemicalsDialog() {
        try {
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.layout_chemicals_dialog, null);
            dialogBuilder.setView(dialogView);
            final AlertDialog alertDialog = dialogBuilder.create();
            final RecyclerView recycleView = (RecyclerView) dialogView.findViewById(R.id.recycleView);
            final Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
            final TextView txtType = (TextView) dialogView.findViewById(R.id.txtType);

            RecyclerView.LayoutManager lm = new LinearLayoutManager(getActivity());
            recycleView.setLayoutManager(lm);
            recycleView.setItemAnimator(new DefaultItemAnimator());
            mChemicalAdapter = new ChemicalDialogAdapter(getActivity(), isCombinedTask);
            recycleView.setAdapter(mChemicalAdapter);
            if (isCombinedTask) {
                txtType.setVisibility(View.VISIBLE);
            } else {
                txtType.setVisibility(View.GONE);
            }

            NetworkCallController controller = new NetworkCallController(ServiceInfoFragment.this);
            controller.setListner(new NetworkResponseListner<List<Chemicals>>() {
                @Override
                public void onResponse(int requestCode, List<Chemicals> items) {
                    try {
                        if (items != null) {
                            if (pageNumber == 1 && items.size() > 0) {
                                mChemicalAdapter.setData(items);
                                mChemicalAdapter.notifyDataSetChanged();
                                alertDialog.show();
                            } else if (items.size() > 0) {
                                mChemicalAdapter.addData(items);
                                mChemicalAdapter.notifyDataSetChanged();
                                alertDialog.show();
                            } else {
                                pageNumber--;
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
            if (isCombinedTask) {
                controller.getMSTChemicals(CHEMICAL_REQ, combinedTaskId);
            } else {
                controller.getChemicals(CHEMICAL_REQ, taskId);
            }
            btnOk.setOnClickListener(v -> alertDialog.dismiss());

            dialogBuilder.setCancelable(false);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            alertDialog.setCanceledOnTouchOutside(false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setDefaultReason() {
        try {
            ReasonRealmModel = getRealm().where(IncompleteReason.class).findAll().sort("reason");
            String res = mTaskDetailsData.get(0).getIncompleteReason();
            if (res == null || res.length() == 0) {
                mFragmentServiceInfoBinding.txtReason.setText("Select Reason");
            } else {
                mFragmentServiceInfoBinding.txtReason.setText(res);
            }
            if (mFragmentServiceInfoBinding.txtReason.getText().toString().equals("Select Reason")) {
                mCallback.getIncompleteReason("");
                mCallback.isIncompleteReason(true);

            } else {
                mCallback.getIncompleteReason(mFragmentServiceInfoBinding.txtReason.getText().toString());
                mCallback.isIncompleteReason(false);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onIncompleteReasonClicked(View view) {
        try {
            if (!status.equalsIgnoreCase("Incomplete")) {
                if ((NewTaskDetailsActivity) getActivity() != null) {
                    mTaskDetailsData = getRealm().where(GeneralData.class).findAll();
                    if (mTaskDetailsData != null && mTaskDetailsData.size() > 0) {
                        final ArrayList<String> type = new ArrayList<>();
                        type.add("Select Reason");
                        for (IncompleteReason incompleteReason : ReasonRealmModel) {
                            type.add(incompleteReason.getReason());
                        }
                        arrayReason = new String[type.size()];
                        arrayReason = type.toArray(arrayReason);
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                        builder.setTitle("Incomplete Reason");
                        builder.setIcon(R.mipmap.logo);
                        builder.setSingleChoiceItems(arrayReason, radiopos, (dialog, which) -> {
                            radiopos = which;
                            Selection = arrayReason[which];
                            mFragmentServiceInfoBinding.txtReason.setText(Selection);
                            if (mFragmentServiceInfoBinding.txtReason.getText().toString().equals("Select Reason")) {
                                mCallback.getIncompleteReason("");
                                mCallback.isIncompleteReason(true);
                            } else {
                                mCallback.getIncompleteReason(mFragmentServiceInfoBinding.txtReason.getText().toString());
                                mCallback.isIncompleteReason(false);
                            }
                            mAlertDialog.dismiss();
                        });
                        mAlertDialog = builder.create();
                        mAlertDialog.show();
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showBankDialog() {

        try {
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.bank_custom_dialog, null);
            dialogBuilder.setView(dialogView);
            final RecyclerView recycle = (RecyclerView) dialogView.findViewById(R.id.recycle);
            final TextView txt_close = (TextView) dialogView.findViewById(R.id.txt_close);
            final SearchView search = (SearchView) dialogView.findViewById(R.id.search);

            if (bankList == null || bankList.size() == 0) {
                NetworkCallController controller = new NetworkCallController(ServiceInfoFragment.this);
                controller.setListner(new NetworkResponseListner() {
                    @Override
                    public void onResponse(int requestCode, Object data) {
                        List<String> items = (List<String>) data;

                        if (items != null) {
                            RecyclerView.LayoutManager lm = new LinearLayoutManager(getActivity());
                            recycle.setLayoutManager(lm);
                            recycle.setItemAnimator(new DefaultItemAnimator());
                            mAdapter = new BankSearchAdapter(getActivity(), items);
                            recycle.setAdapter(mAdapter);
                            recycle.addItemDecoration(new MyDividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL, 10));
                            mAdapter.onBankSelected((item, position) -> {
                                mFragmentServiceInfoBinding.txtBankname.setText(item);
                                mCallback.bankName(item);
                                mTaskDetailsData =
                                        getRealm().where(GeneralData.class).findAll();

                                if (mTaskDetailsData != null && mTaskDetailsData.size() > 0) {
                                    try {
                                        String amountToCollect = mTaskDetailsData.get(0).getAmountToCollect();
                                        int amount = Integer.parseInt(amountToCollect);
                                        getValidated(amount);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                mAlertDialog.dismiss();
                            });
                            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                @Override
                                public boolean onQueryTextSubmit(String query) {
                                    mAdapter.getFilter().filter(query);
                                    return false;
                                }

                                @Override
                                public boolean onQueryTextChange(String query) {
                                    mAdapter.getFilter().filter(query);
                                    return false;
                                }
                            });
                        } else {
                            RecyclerView.LayoutManager lm = new LinearLayoutManager(getActivity());
                            recycle.setLayoutManager(lm);
                            recycle.setItemAnimator(new DefaultItemAnimator());
                            mAdapter = new BankSearchAdapter(getActivity(), bankList);
                            recycle.setAdapter(mAdapter);
                            recycle.addItemDecoration(new MyDividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL, 10));
                            mAdapter.onBankSelected((item, position) -> {
                                mFragmentServiceInfoBinding.txtBankname.setText(item);
                                mCallback.bankName(item);
                                mTaskDetailsData =
                                        getRealm().where(GeneralData.class).findAll();

                                if (mTaskDetailsData != null && mTaskDetailsData.size() > 0) {
                                    try {
                                        String amountToCollect = mTaskDetailsData.get(0).getAmountToCollect();
                                        int amount = Integer.parseInt(amountToCollect);
                                        getValidated(amount);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                mAlertDialog.dismiss();
                            });
                            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                @Override
                                public boolean onQueryTextSubmit(String query) {
                                    mAdapter.getFilter().filter(query);
                                    return false;
                                }


                                @Override
                                public boolean onQueryTextChange(String query) {
                                    mAdapter.getFilter().filter(query);
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

    private void sendPaymentLink() {

        try {
            LayoutInflater li = LayoutInflater.from(getActivity());

            View promptsView = li.inflate(R.layout.payment_link_dialog, null);

            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(getActivity());

            alertDialogBuilder.setView(promptsView);

            alertDialogBuilder.setTitle("Payment Link");

            // create alert dialog
            final android.app.AlertDialog alertDialog = alertDialogBuilder.create();

            final AppCompatEditText edtmobile =
                    (AppCompatEditText) promptsView.findViewById(R.id.edtmobile);
            final AppCompatEditText edtemail =
                    (AppCompatEditText) promptsView.findViewById(R.id.edtemail);
            final AppCompatButton btn_send =
                    (AppCompatButton) promptsView.findViewById(R.id.btn_send);
            final AppCompatButton btn_cancel =
                    (AppCompatButton) promptsView.findViewById(R.id.btn_cancel);
            edtemail.setEnabled(false);
            edtmobile.setEnabled(false);

            try {
                edtmobile.setText(mask);
                edtemail.setText(Email);
            } catch (Exception e) {
                e.printStackTrace();
            }


            btn_send.setOnClickListener(v -> {
                mTaskDetailsData =
                        getRealm().where(GeneralData.class).findAll();

                if (mTaskDetailsData != null && mTaskDetailsData.size() > 0) {

                    PaymentLinkRequest request = new PaymentLinkRequest();
                    request.setAmount(mTaskDetailsData.get(0).getAmountToCollect());
                    request.setCustomerName(mTaskDetailsData.get(0).getCustName());
                    request.setEmail(mTaskDetailsData.get(0).getEmail());
                    request.setMobileNo(mTaskDetailsData.get(0).getMobileNumber());
                    request.setOrderNo(mTaskDetailsData.get(0).getOrderNumber());
                    request.setTaskId(taskId);

                    NetworkCallController controller = new NetworkCallController(ServiceInfoFragment.this);
                    controller.setListner(new NetworkResponseListner() {
                        @Override
                        public void onResponse(int requestCode, Object response) {
                            PaymentLinkResponse refResponse = (PaymentLinkResponse) response;
                            if (refResponse.getIsSuccess()) {
                                Toasty.success(getActivity(), "Payment link sent successfully", Toasty.LENGTH_LONG).show();
                            }
                            mFragmentServiceInfoBinding.btnSendPaymentLink.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onFailure(int requestCode) {

                        }
                    });
                    controller.sendPaymentLink(POST_PAYMENT_LINK, request);
                }

                alertDialog.dismiss();
            });

            btn_cancel.setOnClickListener(v -> {
                if (Mode.equalsIgnoreCase("Online Payment Link")) {
                    alertDialog.dismiss();
                    mFragmentServiceInfoBinding.btnSendPaymentLink.setVisibility(View.VISIBLE);
                } else {
                    mFragmentServiceInfoBinding.btnSendPaymentLink.setVisibility(GONE);
                }
            });
            alertDialog.setCancelable(false);
            alertDialog.setIcon(R.mipmap.logo);
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onCalendarClicked(View view) {
        showDatePicker();
    }

    @Override
    public void onPaymentLinkClicked(View view) {
        sendPaymentLink();
    }

    @Override
    public void onUploadChequeClicked(View view) {
        uploadChequeImage();
    }


    @Override
    public void onImageDeleteClicked(View view) {
        mFragmentServiceInfoBinding.lnrImage.setVisibility(GONE);
        mFragmentServiceInfoBinding.lnrUpload.setVisibility(View.VISIBLE);
        images.clear();
    }

    @Override
    public void onBankNameClicked(View view) {
        if (!sta.equalsIgnoreCase("Completed")) {
            showBankDialog();
        }
    }

    @Override
    public void onViewBottonClicked(View view) {
        try {
            if (mTaskDetailsData.get(0).getTaskTypeName().equals("Termite for Post")) {
                InspectionFragment alert = InspectionFragment.newInstance();
                alert.show(getActivity().getSupportFragmentManager(), "Alert");
            } else {
                ConsultationFragment alert = ConsultationFragment.newInstance();
                alert.show(getActivity().getSupportFragmentManager(), "Alert");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void uploadChequeImage() {
        try {
            if (images.size() < 1) {
                PickImageDialog.build(new PickSetup()).setOnPickResult(pickResult -> {
                    try {
                        if (pickResult.getError() == null) {
                            images.add(pickResult.getPath());
                            mFragmentServiceInfoBinding.lnrUpload.setVisibility(View.VISIBLE);

                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            selectedImagePath = pickResult.getPath();
                            if (selectedImagePath != null) {
                                Bitmap bit = new BitmapDrawable(getActivity().getResources(),
                                        selectedImagePath).getBitmap();
                                int i = (int) (bit.getHeight() * (512.0 / bit.getWidth()));
                                bitmap = Bitmap.createScaledBitmap(bit, 512, i, true);
                            }
                            mFragmentServiceInfoBinding.imgUploadedCheque.setImageBitmap(bitmap);
                            mFragmentServiceInfoBinding.lnrImage.setVisibility(View.VISIBLE);

                            if (mFragmentServiceInfoBinding.imgUploadedCheque.getDrawable() != null) {
                                mFragmentServiceInfoBinding.lnrUpload.setVisibility(View.GONE);
                            }


                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            byte[] b = baos.toByteArray();
                            String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                            if (images.size() == 0) {
                                mCallback.isPaymentChanged(true);
                            } else {
                                mCallback.isPaymentChanged(false);
                                mCallback.chequeImage(encodedImage);
                                getValidated(AmountToCollect);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }).show(getActivity());
            } else {
                Toast.makeText(getContext(), "You have already selected an Image", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showDatePicker() {
        try {
            FragmentDatePicker mFragDatePicker = new FragmentDatePicker();
            mFragDatePicker.setmDatePickerListener(this);
            mFragDatePicker.show(getActivity().getSupportFragmentManager(), "datepicker");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        try {
            month++;
            mFragmentServiceInfoBinding.txtDate.setText("" + day + "-" + month + "-" + year);
            String date = mFragmentServiceInfoBinding.txtDate.getText().toString();
            mCallback.chequeDate("" + month + "-" + day + "-" + year);
            mTaskDetailsData =
                    getRealm().where(GeneralData.class).findAll();

            if (mTaskDetailsData != null && mTaskDetailsData.size() > 0) {
                try {
                    String amountToCollect = mTaskDetailsData.get(0).getAmountToCollect();
                    int amount = Integer.parseInt(amountToCollect);
                    getValidated(amount);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getValidated(int amounttocollect) {
        try {
            if (Mode.equals("None") && amounttocollect != 0) {
                mCallback.isPaymentModeNotChanged(true);
            } else {
                mCallback.isPaymentModeNotChanged(false);
            }
            if (isPaymentValidation) {
                if (amounttocollect > 0) {
                    if ((isRaiseJeopardyClicked || isPaymentJeopardyRaised) && isPaymentOtpRequired) {
//                        mFragmentServiceInfoBinding.lnrPaymentOTP.setVisibility(View.VISIBLE);
                        if (mFragmentServiceInfoBinding.txtCollected.getText().toString().trim().length() != 0) {
                            int amount = 0;
                            amount = Integer.parseInt(mFragmentServiceInfoBinding.txtCollected.getText().toString());
//                            if (amounttocollect == amount) {
////                                mCallback.isPaymentOtpRequired(false);
////                                mCallback.isPaymentOtpvalidated(false);
//                            } else {
                            if (PaymentOtp.length() > 0 && PaymentOtp != null) {
                                mFragmentServiceInfoBinding.lnrPaymentOTP.setVisibility(View.VISIBLE);
                                String otp = mFragmentServiceInfoBinding.edtPaymentOTP.getText().toString();
                                if (otp.length() != 0) {
                                    mCallback.isPaymentOtpRequired(false);
                                    if (otp.equals(PaymentOtp)) {
//                                    mCallback.onSiteOtp(otp);
                                        mCallback.isPaymentOtpvalidated(false);
                                    } else {
                                        mCallback.isPaymentOtpvalidated(true);
                                    }
                                } else {
                                    mCallback.isPaymentOtpRequired(true);
                                }
//                                    mCallback.isPaymentOtpRequired(true);
                                mFragmentServiceInfoBinding.txtCollected.setEnabled(true);
//                                    mFragmentServiceInfoBinding.lnrCollected.setVisibility(View.VISIBLE);
                                if (mFragmentServiceInfoBinding.txtCollected.getText().toString().trim().length() == 0) {
                                    mCallback.isPaymentChanged(true);
                                    if (Mode.equalsIgnoreCase("Online Payment Link")) {
                                        mCallback.isAmountCollectedRequired(false);
                                    } else {
                                        mCallback.isAmountCollectedRequired(true);
                                    }
                                    if (Mode.equals("UPI")) {
                                        mCallback.isAmountCollectedRequired(false);
                                    } else if (Mode.equalsIgnoreCase("PayTM")) {
                                        mCallback.isAmountCollectedRequired(false);
                                    } else if (Mode.equalsIgnoreCase("PhonePay")) {
                                        mCallback.isAmountCollectedRequired(false);
                                    } else {
                                        mCallback.isAmountCollectedRequired(true);
                                    }
                                } else {
                                    mCallback.isPaymentChanged(false);
                                    mCallback.isAmountCollectedRequired(false);
                                }
                            } else {
                                mCallback.isPaymentOtpRequired(false);
                                mCallback.isPaymentOtpvalidated(false);
                            }
//                            }
                        } else {
                            if (PaymentOtp.length() > 0 && PaymentOtp != null) {
                                mFragmentServiceInfoBinding.lnrPaymentOTP.setVisibility(View.VISIBLE);
                                String otp = mFragmentServiceInfoBinding.edtPaymentOTP.getText().toString();
                                if (otp.length() != 0) {
                                    mCallback.isPaymentOtpRequired(false);
                                    if (otp.equals(PaymentOtp)) {
//                                    mCallback.onSiteOtp(otp);
                                        mCallback.isPaymentOtpvalidated(false);
                                    } else {
                                        mCallback.isPaymentOtpvalidated(true);
                                    }
                                } else {
                                    mCallback.isPaymentOtpRequired(true);
                                }

                                if (mFragmentServiceInfoBinding.txtCollected.getText().toString().trim().length() == 0) {
                                    mCallback.isPaymentChanged(true);
                                    if (Mode.equalsIgnoreCase("Online Payment Link")) {
                                        mCallback.isAmountCollectedRequired(false);
                                    } else {
                                        mCallback.isAmountCollectedRequired(true);
                                    }
                                    if (Mode.equals("UPI")) {
                                        mCallback.isAmountCollectedRequired(false);
                                    } else if (Mode.equalsIgnoreCase("PayTM")) {
                                        mCallback.isAmountCollectedRequired(false);
                                    } else if (Mode.equalsIgnoreCase("PhonePay")) {
                                        mCallback.isAmountCollectedRequired(false);
                                    } else {
                                        mCallback.isAmountCollectedRequired(true);
                                    }
                                } else {
                                    mCallback.isPaymentChanged(false);
                                    mCallback.isAmountCollectedRequired(false);
                                }
                            } else {
                                mCallback.isPaymentOtpRequired(false);
                                mCallback.isPaymentOtpvalidated(false);
                            }
                        }

                    } else {
                        mCallback.isPaymentOtpRequired(false);
                        if (mFragmentServiceInfoBinding.txtCollected.getText().toString().trim().length() == 0) {
                            mCallback.isPaymentChanged(true);
                            if (Mode.equals("Online Payment Link")) {
                                mCallback.isAmountCollectedRequired(false);
                            } else {
                                mCallback.isAmountCollectedRequired(true);
                            }
                            if (Mode.equals("UPI")) {
                                mCallback.isAmountCollectedRequired(false);
                            } else if (Mode.equalsIgnoreCase("PayTM")) {
                                mCallback.isAmountCollectedRequired(false);
                            } else if (Mode.equalsIgnoreCase("PhonePay")) {
                                mCallback.isAmountCollectedRequired(false);
                            } else {
                                mCallback.isAmountCollectedRequired(true);
                            }
                        } else {
                            if (mFragmentServiceInfoBinding.txtCollected.getText().toString().trim().length() != 0) {
                                int amount = 0;
                                Log.i("amount", String.valueOf(amount));
                                try {
                                    amount = Integer.parseInt(mFragmentServiceInfoBinding.txtCollected.getText().toString());
                                    if (amounttocollect == amount) {
                                        mCallback.isPaymentChanged(false);
                                        mCallback.isAmountCollectedRequired(false);
                                        mCallback.isACEquals(false);
                                    } else {
                                        mCallback.isPaymentChanged(false);
                                        mCallback.isAmountCollectedRequired(false);
                                        mCallback.isACEquals(true);
                                    }

                                } catch (Exception e) {
                                    e.getMessage();
                                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                mCallback.isPaymentChanged(true);
                                if (Mode.equals("Online Payment Link")) {
                                    mCallback.isAmountCollectedRequired(false);
                                } else {
                                    if (mFragmentServiceInfoBinding.txtCollected.getText().toString().trim().length() == 0) {
                                        mCallback.isAmountCollectedRequired(true);
                                    }
                                }
                                if (Mode.equals("UPI")) {
                                    mCallback.isAmountCollectedRequired(false);
                                } else if (Mode.equalsIgnoreCase("PayTM")) {
                                    mCallback.isAmountCollectedRequired(false);
                                } else if (Mode.equalsIgnoreCase("PhonePay")) {
                                    mCallback.isAmountCollectedRequired(false);
                                } else {
                                    mCallback.isAmountCollectedRequired(true);
                                }
                            }
                        }
                    }
                    Log.i("paymentMode", Mode);
                    mFragmentServiceInfoBinding.txtCollected.setEnabled(true);
//                    mFragmentServiceInfoBinding.lnrCollected.setVisibility(View.VISIBLE);
                } else {
                    Log.i("paymentMode", Mode);
                    mFragmentServiceInfoBinding.txtCollected.setEnabled(false);
                    if (Mode.equalsIgnoreCase("Online Payment Link")) {
                        mCallback.isAmountCollectedRequired(false);
                    } else {
                        mCallback.isAmountCollectedRequired(true);
                    }
                    if (Mode.equals("UPI")) {
                        mCallback.isAmountCollectedRequired(false);
                    } else if (Mode.equalsIgnoreCase("PayTM")) {
                        mCallback.isAmountCollectedRequired(false);
                    } else if (Mode.equalsIgnoreCase("PhonePay")) {
                        mCallback.isAmountCollectedRequired(false);
                    } else {
                        mCallback.isAmountCollectedRequired(true);
                    }
                }

            } else {

                if (amounttocollect > 0) {
                    if ((isRaiseJeopardyClicked || isPaymentJeopardyRaised) && isPaymentOtpRequired) {
//                        mFragmentServiceInfoBinding.lnrPaymentOTP.setVisibility(View.VISIBLE);
                        mFragmentServiceInfoBinding.txtCollected.setEnabled(true);
                        if (mFragmentServiceInfoBinding.txtCollected.getText().toString().trim().length() != 0) {
                            int amount = 0;
                            amount = Integer.parseInt(mFragmentServiceInfoBinding.txtCollected.getText().toString());
//                            if (amounttocollect == amount) {
//                                mCallback.isPaymentOtpRequired(false);
//                                mCallback.isPaymentOtpvalidated(false);
//                            } else {
                            if (PaymentOtp.length() > 0 && PaymentOtp != null) {
                                mFragmentServiceInfoBinding.lnrPaymentOTP.setVisibility(View.VISIBLE);
                                String otp = mFragmentServiceInfoBinding.edtPaymentOTP.getText().toString();
                                if (otp.length() != 0) {
                                    mCallback.isPaymentOtpRequired(false);
                                    if (otp.equals(PaymentOtp)) {
//                                    mCallback.onSiteOtp(otp);
                                        mCallback.isPaymentOtpvalidated(false);
                                    } else {
                                        mCallback.isPaymentOtpvalidated(true);
                                    }
                                } else {
                                    mCallback.isPaymentOtpRequired(true);
                                }

                                if (mFragmentServiceInfoBinding.txtCollected.getText().toString().trim().length() == 0) {
                                    mCallback.isPaymentChanged(true);
                                    if (Mode.equalsIgnoreCase("Online Payment Link")) {
                                        mCallback.isAmountCollectedRequired(false);
                                    } else {
                                        mCallback.isAmountCollectedRequired(true);
                                    }
                                    if (Mode.equals("UPI")) {
                                        mCallback.isAmountCollectedRequired(false);
                                    } else if (Mode.equalsIgnoreCase("PayTM")) {
                                        mCallback.isAmountCollectedRequired(false);
                                    } else if (Mode.equalsIgnoreCase("PhonePay")) {
                                        mCallback.isAmountCollectedRequired(false);
                                    } else {
                                        mCallback.isAmountCollectedRequired(true);
                                    }
                                } else {
                                    mCallback.isPaymentChanged(false);
                                    mCallback.isAmountCollectedRequired(false);
                                }
                            } else {
                                mCallback.isPaymentOtpRequired(false);
                                mCallback.isPaymentOtpvalidated(false);
                                mCallback.isAmountCollectedRequired(true);
                            }
//                            }
                        } else {
                            if (PaymentOtp.length() > 0 && PaymentOtp != null) {
                                mFragmentServiceInfoBinding.lnrPaymentOTP.setVisibility(View.VISIBLE);
                                String otp = mFragmentServiceInfoBinding.edtPaymentOTP.getText().toString();
                                if (otp.length() != 0) {
                                    mCallback.isPaymentOtpRequired(false);
                                    if (otp.equals(PaymentOtp)) {
//                                    mCallback.onSiteOtp(otp);
                                        mCallback.isPaymentOtpvalidated(false);
                                    } else {
                                        mCallback.isPaymentOtpvalidated(true);
                                    }
                                } else {
                                    mCallback.isPaymentOtpRequired(true);
                                }

                                if (mFragmentServiceInfoBinding.txtCollected.getText().toString().trim().length() == 0) {
                                    mCallback.isPaymentChanged(true);
                                    if (Mode.equalsIgnoreCase("Online Payment Link")) {
                                        mCallback.isAmountCollectedRequired(false);
                                    } else {
                                        mCallback.isAmountCollectedRequired(true);
                                    }
                                    if (Mode.equals("UPI")) {
                                        mCallback.isAmountCollectedRequired(false);
                                    } else if (Mode.equalsIgnoreCase("PayTM")) {
                                        mCallback.isAmountCollectedRequired(false);
                                    } else if (Mode.equalsIgnoreCase("PhonePay")) {
                                        mCallback.isAmountCollectedRequired(false);
                                    } else {
                                        mCallback.isAmountCollectedRequired(true);
                                    }
                                } else {
                                    mCallback.isPaymentChanged(false);
                                    mCallback.isAmountCollectedRequired(false);
                                }
                            } else {
                                mCallback.isPaymentOtpRequired(false);
                                mCallback.isPaymentOtpvalidated(false);
                                mCallback.isAmountCollectedRequired(true);
                            }
                        }

                    } else {
                        Log.i("paymentMode", Mode);
                        mFragmentServiceInfoBinding.txtCollected.setEnabled(true);
//                        mFragmentServiceInfoBinding.lnrCollected.setVisibility(View.VISIBLE);
                        if (mFragmentServiceInfoBinding.txtCollected.getText().toString().trim().length() == 0) {
                            mCallback.isPaymentChanged(true);
                            if (Mode.equalsIgnoreCase("Online Payment Link")) {
                                mCallback.isAmountCollectedRequired(false);
                            } else {
                                mCallback.isAmountCollectedRequired(true);
                            }
                            if (Mode.equals("UPI")) {
                                mCallback.isAmountCollectedRequired(false);
                            } else if (Mode.equalsIgnoreCase("PayTM")) {
                                mCallback.isAmountCollectedRequired(false);
                            } else if (Mode.equalsIgnoreCase("PhonePay")) {
                                mCallback.isAmountCollectedRequired(false);
                            } else {
                                mCallback.isAmountCollectedRequired(true);
                            }
                        } else {
                            mCallback.isPaymentChanged(false);
                            mCallback.isAmountCollectedRequired(false);
                        }
                    }

                } else {
                    mFragmentServiceInfoBinding.txtCollected.setEnabled(false);
//                    mFragmentServiceInfoBinding.lnrCollected.setVisibility(GONE);
                    mCallback.isPaymentChanged(false);
                    mCallback.isAmountCollectedRequired(false);
                }
            }


            if (mFragmentServiceInfoBinding.spnPaymentMode.getSelectedItem().toString().equals("Cheque")) {
                mFragmentServiceInfoBinding.lnrCheque.setVisibility(View.VISIBLE);
                if (isChequeRequired) {
                    mFragmentServiceInfoBinding.lnrUploadChq.setVisibility(View.VISIBLE);
                    if (mFragmentServiceInfoBinding.txtChequeNo.getText().length() == 6) {
                        mCallback.isInvalidChequeNumber(false);
                        if (images.size() == 0) {
                            mCallback.isPaymentChanged(true);
                            mCallback.isChequeImageRequired(true);
                        } else {
                            mCallback.isChequeImageRequired(false);
                            if (mFragmentServiceInfoBinding.txtCollected.getText().toString().trim().length() != 0) {
                                int amount = 0;
                                Log.i("amount", String.valueOf(amount));
                                try {
                                    amount = Integer.parseInt(mFragmentServiceInfoBinding.txtCollected.getText().toString());
                                    if (amounttocollect == amount) {
                                        mCallback.isPaymentChanged(false);
                                        mCallback.isAmountCollectedRequired(false);
                                        mCallback.isPaymentOtpvalidated(false);
                                        mCallback.isPaymentModeNotChanged(false);
                                    } else {
                                        mCallback.isPaymentChanged(true);
                                        mCallback.isAmountCollectedRequired(true);
                                        if ((isRaiseJeopardyClicked || isPaymentJeopardyRaised) && isPaymentOtpRequired) {
                                            mCallback.isPaymentOtpvalidated(true);
                                            mCallback.isPaymentModeNotChanged(true);
                                        }

                                    }

                                } catch (Exception e) {
                                    e.getMessage();
                                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                mCallback.isPaymentChanged(true);
                                mCallback.isAmountCollectedRequired(true);
                            }

                        }
                    } else {
                        mCallback.isPaymentChanged(true);
                        mCallback.isChequeNumberRequired(true);
                    }
                } else {
                    mFragmentServiceInfoBinding.lnrUploadChq.setVisibility(View.GONE);
                }
                if (mFragmentServiceInfoBinding.txtChequeNo.getText().toString().length() == 0) {
                    mCallback.isChequeNumberRequired(true);
                } else {
                    mCallback.isChequeNumberRequired(false);
                }

                if (mFragmentServiceInfoBinding.txtChequeNo.getText().toString().length() < 6) {
                    mCallback.isInvalidChequeNumber(true);
                } else {
                    mCallback.isInvalidChequeNumber(false);
                }

                if (mFragmentServiceInfoBinding.txtBankname.getText().equals("Select Bank")) {
                    mCallback.isBankNameRequired(true);
                } else {
                    mCallback.isBankNameRequired(false);
                }

                if (mFragmentServiceInfoBinding.txtDate.getText().equals("Select Date")) {
                    mCallback.isChequeDateRequired(true);
                } else {
                    mCallback.isChequeDateRequired(false);
                }

            } else {
                mCallback.isBankNameRequired(false);
                mCallback.isChequeDateRequired(false);
                mCallback.isInvalidChequeNumber(false);
                mCallback.isChequeNumberRequired(false);

            }
            if (sta != null) {
                if (sta.equals("Completed") || sta.equals("Incompleted")) {
                    mFragmentServiceInfoBinding.txtCollected.setEnabled(false);
                    mFragmentServiceInfoBinding.lnrBank.setEnabled(false);
                    mFragmentServiceInfoBinding.lnrDate.setEnabled(false);
                    mFragmentServiceInfoBinding.txtChequeNo.setEnabled(false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getServiceValidate() {
        try {
            if (Mode.equals("On-Site")) {
                assert mTaskDetailsData.get(0) != null;
                if (isFeedback && mTaskDetailsData.get(0).getOnsite_OTP() != null && !mTaskDetailsData.get(0).getOnsite_OTP().equals("")) {
                    if (status.equals("Dispatched")) {
                        mFragmentServiceInfoBinding.layoutOtp.setVisibility(View.VISIBLE);
                        mFragmentServiceInfoBinding.lnrNoCustomer.setVisibility(View.VISIBLE);
                        mFragmentServiceInfoBinding.lnrIncomplete.setVisibility(GONE);
                    } else {
                        mFragmentServiceInfoBinding.layoutOtp.setVisibility(View.GONE);
                        mFragmentServiceInfoBinding.lnrNoCustomer.setVisibility(View.GONE);
                    }
                    assert mTaskDetailsData.get(0) != null;
                    OnSiteOtp = mTaskDetailsData.get(0).getOnsite_OTP();
                    assert mTaskDetailsData.get(0) != null;
                    ScOtp = mTaskDetailsData.get(0).getSc_OTP();
                    assert mTaskDetailsData.get(0) != null;
                    PaymentOtp = mTaskDetailsData.get(0).getPaymentOtp();
                    String otp = Objects.requireNonNull(mFragmentServiceInfoBinding.edtOnsiteOtp.getText()).toString();
                    if (otp.length() != 0) {
                        mCallback.isEmptyOnsiteOtp(false);
                        if (otp.equals(OnSiteOtp) || otp.equals(ScOtp)) {
                            mCallback.onSiteOtp(otp);
                            mCallback.isOnsiteOtp(false);
                        } else {
                            mCallback.isOnsiteOtp(true);
                        }
                    } else {
                        mCallback.isEmptyOnsiteOtp(true);
                    }
                } else {
                    mCallback.isEmptyOnsiteOtp(false);
                    mCallback.isOnsiteOtp(false);
                    mFragmentServiceInfoBinding.layoutOtp.setVisibility(View.GONE);
                }

            } else if (Mode.equals("Completed")) {
                mFragmentServiceInfoBinding.lnrIncomplete.setVisibility(GONE);
                if (mTaskDetailsData.get(0).getRestrict_Early_Completion()) {
                    NetworkCallController controller = new NetworkCallController(this);
                    controller.setListner(new NetworkResponseListner() {
                        @Override
                        public void onResponse(int requestCode, Object data) {
                            BasicResponse response = (BasicResponse) data;
                            if (response.getSuccess()) {
                                mCallback.isEarlyCompletion(false);
                            } else {
                                mCallback.isEarlyCompletion(true);
                            }
                        }

                        @Override
                        public void onFailure(int requestCode) {

                        }
                    });
                    controller.getValidateCompletionTime(COMPLETION_REQUEST, mTaskDetailsData.get(0).getActualCompletionDateTime(), taskId);
                }
            } else if (Mode.equals("Dispatched")) {
                mFragmentServiceInfoBinding.lnrIncomplete.setVisibility(GONE);
                mFragmentServiceInfoBinding.layoutOtp.setVisibility(GONE);
            } else if (Mode.equals("Incomplete")) {
                mFragmentServiceInfoBinding.lnrIncomplete.setVisibility(View.VISIBLE);
                mFragmentServiceInfoBinding.layoutOtp.setVisibility(GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


