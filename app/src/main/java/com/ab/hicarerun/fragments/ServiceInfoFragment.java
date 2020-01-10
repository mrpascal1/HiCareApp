package com.ab.hicarerun.fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.hicarerun.BaseApplication;
import com.ab.hicarerun.BaseFragment;
import com.ab.hicarerun.R;
import com.ab.hicarerun.activities.NewTaskDetailsActivity;
import com.ab.hicarerun.adapter.BankSearchAdapter;
import com.ab.hicarerun.databinding.FragmentServiceInfoBinding;
import com.ab.hicarerun.handler.OnSaveEventHandler;
import com.ab.hicarerun.handler.UserServiceInfoClickHandler;
import com.ab.hicarerun.network.NetworkCallController;
import com.ab.hicarerun.network.NetworkResponseListner;
import com.ab.hicarerun.network.models.BasicResponse;
import com.ab.hicarerun.network.models.GeneralModel.GeneralData;
import com.ab.hicarerun.network.models.GeneralModel.GeneralPaymentMode;
import com.ab.hicarerun.network.models.GeneralModel.GeneralTaskStatus;
import com.ab.hicarerun.network.models.GeneralModel.IncompleteReason;
import com.ab.hicarerun.network.models.GeneralModel.OnSiteOtpResponse;
import com.ab.hicarerun.network.models.LoginResponse;
import com.ab.hicarerun.network.models.PayementModel.PaymentLinkRequest;
import com.ab.hicarerun.network.models.PayementModel.PaymentLinkResponse;
import com.ab.hicarerun.network.models.TaskModel.Tasks;
import com.ab.hicarerun.utils.AppUtils;
import com.ab.hicarerun.utils.MyDividerItemDecoration;
import com.bumptech.glide.Glide;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import es.dmoral.toasty.Toasty;
import io.realm.RealmResults;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.view.View.GONE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServiceInfoFragment extends BaseFragment implements UserServiceInfoClickHandler, FragmentDatePicker.onDatePickerListener {

    FragmentServiceInfoBinding mFragmentServiceInfoBinding;
    private static final int POST_PAYMENT_LINK = 1000;
    private static final String ARG_TASK = "ARG_TASK";
    private static final int ONSITE_REQUEST = 1000;
    private static final int REQUEST_BANK = 2000;
    private static final int COMPLETION_REQUEST = 3000;
    private String selectedStatus = "";
    private String status = "";
    private String OnSiteOtp = "";
    private String ScOtp = "";
    private String[] arrayReason = null;
    private String[] arrayStatus = null;
    private Boolean isFeedback = false;
    private Boolean isChequeRequired = false;
    private Boolean isTrue = false;
    private int radiopos = 0;
    private String Selection = "";
    private String chequeImg = "";
    private String selectedImagePath = "";
    private Bitmap bitmap;
    private BankSearchAdapter mAdapter;
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
    private Tasks model;
    private RealmResults<GeneralData> mTaskDetailsData = null;
    private RealmResults<GeneralTaskStatus> generalTaskRealmModel;
    private RealmResults<IncompleteReason> ReasonRealmModel = null;
    private RealmResults<GeneralPaymentMode> mPaymentRealmModel;
    private AlertDialog mAlertDialog = null;
    private OnSaveEventHandler mCallback;

    public static ServiceInfoFragment newInstance(Tasks taskId) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_TASK, taskId);
        ServiceInfoFragment fragment = new ServiceInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public ServiceInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
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
            model = getArguments().getParcelable(ARG_TASK);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
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
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        bankList = new ArrayList<>();
        getServiceDetail();
        getPaymentData();

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

        mFragmentServiceInfoBinding.btnOnsiteOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCommercialDialog();
            }
        });
    }

    private void getCommercialDialog() {

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
                                        Toasty.success(getActivity(), "OTP send successfully").show();
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
                            controller.getOnSiteOTP(ONSITE_REQUEST, resourceId, model.getTaskId(), edtName.getText().toString(), edtmobile.getText().toString());
                        } else {
                            Toasty.error(getActivity(), "Invalid mobile no.").show();
                        }
                    }
                });
                btn_cancel.setOnClickListener(v -> alertDialog.dismiss());
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();
            }
        }
    }


    private boolean isOnSiteValidate(EditText edtmobile, EditText edtName) {
        if (edtName.getText().toString().length() == 0) {
            edtName.setError("This field is required");
            return false;
        } else if (edtmobile.getText().toString().length() == 0) {
            edtmobile.setError("This field is required");
            return false;
        } else if (edtmobile.getText().toString().length() < 10) {
            edtmobile.setError("Invalid mobile no.");
            return false;
        } else {
            return true;
        }
    }


    private void getPaymentData() {
        mTaskDetailsData =
                getRealm().where(GeneralData.class).findAll();
        if (mTaskDetailsData != null && mTaskDetailsData.size() > 0) {
            mobile = mTaskDetailsData.get(0).getMobileNumber();
            Email = mTaskDetailsData.get(0).getEmail();
            try {
                mask = mobile.replaceAll("\\w(?=\\w{4})", "*");
            } catch (Exception e) {
                e.printStackTrace();
            }
            chequeImg = mTaskDetailsData.get(0).getChequeImageUrl();
            AmountCollected = mTaskDetailsData.get(0).getAmountCollected();
            AmountToCollect = Integer.parseInt(mTaskDetailsData.get(0).getAmountToCollect());
            mFragmentServiceInfoBinding.txtAmountToCollect.setText(AmountToCollect + " " + "\u20B9");
            mPaymentRealmModel = getRealm().where(GeneralPaymentMode.class).findAll().sort("Value");
            type = new ArrayList<>();
            type.clear();
            for (GeneralPaymentMode generalPaymentMode : mPaymentRealmModel) {
                type.add(generalPaymentMode.getValue());
            }

            isTrue = mTaskDetailsData.get(0).getPaymentValidation();
            isChequeRequired = mTaskDetailsData.get(0).getChequeRequired();
            type.add(0, "None");
            Log.i("type", String.valueOf(type.size()));
            arrayMode = new String[type.size()];
            arrayMode = type.toArray(arrayMode);
            Log.i("payment", Arrays.toString(arrayMode));
            final String status = mTaskDetailsData.get(0).getSchedulingStatus();
            sta = mTaskDetailsData.get(0).getSchedulingStatus();
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
            ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(getActivity(),
                    R.layout.spinner_layout_new, arrayMode);
            statusAdapter.setDropDownViewResource(R.layout.spinner_popup);
            mFragmentServiceInfoBinding.spnPaymentMode.setAdapter(statusAdapter);

            mFragmentServiceInfoBinding.spnPaymentMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        mFragmentServiceInfoBinding.btnSendPaymentLink.setVisibility(GONE);
                        Mode = mFragmentServiceInfoBinding.spnPaymentMode.getSelectedItem().toString();
                        if (Mode.equals("None")) {
                            mCallback.mode("");
                        } else {
                            mCallback.mode(Mode);
                        }
                        mCallback.amountToCollect(String.valueOf(AmountToCollect));

                        if (mFragmentServiceInfoBinding.spnPaymentMode.getSelectedItem().toString().equals("Cheque")) {
                            mFragmentServiceInfoBinding.lnrCheque.setVisibility(View.VISIBLE);
                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
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
                        if (mFragmentServiceInfoBinding.spnPaymentMode.getSelectedItem().toString().equals("PayTm")) {
                            mFragmentServiceInfoBinding.cardScanner.setVisibility(View.VISIBLE);
                            getValidated(AmountToCollect);
                            Glide.with(getActivity()).load("http://52.74.65.15/MobileApi/images/PayTm.png").into(mFragmentServiceInfoBinding.imgPayscanner);
                        } else if (mFragmentServiceInfoBinding.spnPaymentMode.getSelectedItem().toString().equals("Google Pay")) {
                            mFragmentServiceInfoBinding.cardScanner.setVisibility(View.VISIBLE);
                            getValidated(AmountToCollect);
                            Glide.with(getActivity()).load("http://52.74.65.15/MobileApi/images/gpay.png").into(mFragmentServiceInfoBinding.imgPayscanner);
                        } else if (mFragmentServiceInfoBinding.spnPaymentMode.getSelectedItem().toString().equals("PhonePe")) {
                            mFragmentServiceInfoBinding.cardScanner.setVisibility(View.VISIBLE);
                            getValidated(AmountToCollect);
                            Glide.with(getActivity()).load("http://52.74.65.15/MobileApi/images/PhonePay.png").into(mFragmentServiceInfoBinding.imgPayscanner);
                        } else {
                            mFragmentServiceInfoBinding.cardScanner.setVisibility(View.GONE);
                        }
                        if (mFragmentServiceInfoBinding.spnPaymentMode.getSelectedItem().toString().equals("None")) {
                            getValidated(AmountToCollect);
                            mFragmentServiceInfoBinding.txtCollected.setEnabled(false);
//                            mFragmentServiceInfoBinding.lnrCollected.setVisibility(GONE);
                        }
                        if (mFragmentServiceInfoBinding.spnPaymentMode.getSelectedItem().toString().equals("Online Payment Link")) {
                            getValidated(AmountToCollect);
                            mFragmentServiceInfoBinding.txtCollected.setText("");
                            mFragmentServiceInfoBinding.txtCollected.setEnabled(false);
                            mFragmentServiceInfoBinding.lnrCollected.setVisibility(View.GONE);
                        }
                        if (mFragmentServiceInfoBinding.spnPaymentMode.getSelectedItem().toString().equals("Cash")) {
                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                            imm.showSoftInput(mFragmentServiceInfoBinding.txtCollected, InputMethodManager.SHOW_IMPLICIT);
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
    }

    private void getServiceDetail() {
        try {
            if ((NewTaskDetailsActivity) getActivity() != null) {
                mTaskDetailsData = getRealm().where(GeneralData.class).findAll();
                if (mTaskDetailsData != null && mTaskDetailsData.size() > 0) {
                    status = mTaskDetailsData.get(0).getSchedulingStatus();
                    String order = mTaskDetailsData.get(0).getOrderNumber();
                    String duration = mTaskDetailsData.get(0).getDuration();
                    String start = mTaskDetailsData.get(0).getTaskAssignmentStartTime();
                    if (model.getCombinedTask()) {
                        String type = model.getCombinedServiceType();
                        mFragmentServiceInfoBinding.txtType.setText(type);
                    } else {
                        String type = mTaskDetailsData.get(0).getServiceType();
                        mFragmentServiceInfoBinding.txtType.setText(type);
                    }

                    String[] split_sDuration = duration.split(":");
                    String hr_duration = split_sDuration[0];
                    String mn_duration = split_sDuration[1];

                    if (hr_duration.equals("00")) {
                        mFragmentServiceInfoBinding.txtDuration.setText(duration + " min");
                    } else {
                        mFragmentServiceInfoBinding.txtDuration.setText(duration + " hr");
                    }
                    mFragmentServiceInfoBinding.txtOrder.setText("Order# " + order);
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
        selectedStatus = mTaskDetailsData.get(0).getSchedulingStatus();
        generalTaskRealmModel = getRealm().where(GeneralTaskStatus.class).findAll().sort("Status");
        final ArrayList<String> type = new ArrayList<>();

        for (GeneralTaskStatus generalTaskStatus : generalTaskRealmModel) {
            type.add(generalTaskStatus.getStatus());
        }
        arrayStatus = new String[type.size()];
        arrayStatus = type.toArray(arrayStatus);

        ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_layout_new, arrayStatus);
        statusAdapter.setDropDownViewResource(R.layout.spinner_popup);
        mFragmentServiceInfoBinding.spnStatus.setAdapter(statusAdapter);

        mFragmentServiceInfoBinding.spnStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    try {
                        AppUtils.statusCheck(getActivity());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Mode = mFragmentServiceInfoBinding.spnStatus.getSelectedItem().toString();
                    isFeedback = mTaskDetailsData.get(0).getFeedBack();
                    try {
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
            if (selectedStatus.equals(generalTaskRealmModel.get(i).getStatus())) {
                mFragmentServiceInfoBinding.spnStatus.setSelection(i);
            }
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
                    builder.setSingleChoiceItems(arrayReason, radiopos, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
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
                        }
                    });
                    mAlertDialog = builder.create();
                    mAlertDialog.show();
                }

            }
        }
    }

    private void showBankDialog() {
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
                        mAdapter.onBankSelected(new BankSearchAdapter.BankAdapterListener() {
                            @Override
                            public void onSelected(String item, int position) {
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
                            }
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
                        mAdapter.onBankSelected(new BankSearchAdapter.BankAdapterListener() {
                            @Override
                            public void onSelected(String item, int position) {
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
                            }

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
        txt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlertDialog.dismiss();
            }
        });

        dialogBuilder.setCancelable(false);
        mAlertDialog = dialogBuilder.create();
        mAlertDialog.setCanceledOnTouchOutside(true);
        mAlertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mAlertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        mAlertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        mAlertDialog.show();

    }

    private void sendPaymentLink() {
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


        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTaskDetailsData =
                        getRealm().where(GeneralData.class).findAll();

                if (mTaskDetailsData != null && mTaskDetailsData.size() > 0) {

                    PaymentLinkRequest request = new PaymentLinkRequest();
                    request.setAmount(mTaskDetailsData.get(0).getAmountToCollect());
                    request.setCustomerName(mTaskDetailsData.get(0).getCustName());
                    request.setEmail(mTaskDetailsData.get(0).getEmail());
                    request.setMobileNo(mTaskDetailsData.get(0).getMobileNumber());
                    request.setOrderNo(mTaskDetailsData.get(0).getOrderNumber());
                    request.setTaskId(model.getTaskId());

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
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Mode.equalsIgnoreCase("Online Payment Link")) {
                    alertDialog.dismiss();
                    mFragmentServiceInfoBinding.btnSendPaymentLink.setVisibility(View.VISIBLE);
                } else {
                    mFragmentServiceInfoBinding.btnSendPaymentLink.setVisibility(GONE);
                }
            }
        });
        alertDialog.setCancelable(false);
        alertDialog.setIcon(R.mipmap.logo);
        alertDialog.show();
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

    private void uploadChequeImage() {
        if (images.size() < 1) {
            PickImageDialog.build(new PickSetup()).setOnPickResult(new IPickResult() {
                @Override
                public void onPickResult(PickResult pickResult) {
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

                }
            }).show(getActivity());
        } else {
            Toast.makeText(getContext(), "You have already selected an Image", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDatePicker() {
        FragmentDatePicker mFragDatePicker = new FragmentDatePicker();
        mFragDatePicker.setmDatePickerListener(this);
        mFragDatePicker.show(getActivity().getSupportFragmentManager(), "datepicker");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
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
    }

    private void getValidated(int amounttocollect) {
        try {
            if (isTrue) {
                if (amounttocollect > 0) {
                    Log.i("paymentMode", Mode);
                    mFragmentServiceInfoBinding.txtCollected.setEnabled(true);
                    mFragmentServiceInfoBinding.lnrCollected.setVisibility(View.VISIBLE);

                    if (mFragmentServiceInfoBinding.txtCollected.getText().toString().trim().length() == 0) {
                        mCallback.isPaymentChanged(true);
                        if (Mode.equals("Online Payment Link")) {
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
                                    mCallback.isPaymentChanged(true);
                                    mCallback.isAmountCollectedRequired(true);
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
                                mCallback.isAmountCollectedRequired(true);
                            }
                        }

                    }
                } else {
                    Log.i("paymentMode", Mode);
                    mFragmentServiceInfoBinding.txtCollected.setEnabled(false);
                    if (Mode.equalsIgnoreCase("Online Payment Link")) {
                        mCallback.isAmountCollectedRequired(false);
                    } else {
                        mCallback.isAmountCollectedRequired(true);
                    }
                }

            } else {
                if (amounttocollect > 0) {
                    Log.i("paymentMode", Mode);
                    mFragmentServiceInfoBinding.txtCollected.setEnabled(true);
                    mFragmentServiceInfoBinding.lnrCollected.setVisibility(View.VISIBLE);
                    if (mFragmentServiceInfoBinding.txtCollected.getText().toString().trim().length() == 0) {
                        mCallback.isPaymentChanged(true);
                        if (Mode.equalsIgnoreCase("Online Payment Link")) {
                            mCallback.isAmountCollectedRequired(false);
                        } else {
                            mCallback.isAmountCollectedRequired(true);
                        }
                    } else {
                        mCallback.isPaymentChanged(false);
                        mCallback.isAmountCollectedRequired(false);

                    }
                } else {
                    mFragmentServiceInfoBinding.txtCollected.setEnabled(false);
                    mFragmentServiceInfoBinding.lnrCollected.setVisibility(GONE);
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
                                    } else {
                                        mCallback.isPaymentChanged(true);
                                        mCallback.isAmountCollectedRequired(true);
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
                Log.i("isFeedback", String.valueOf(isFeedback));

                if (isFeedback && mTaskDetailsData.get(0).getOnsite_OTP() != null && !mTaskDetailsData.get(0).getOnsite_OTP().equals("")) {
                    if (status.equals("Dispatched")) {
                        mFragmentServiceInfoBinding.layoutOtp.setVisibility(View.VISIBLE);
                        mFragmentServiceInfoBinding.lnrNoCustomer.setVisibility(View.VISIBLE);

                    } else {
                        mFragmentServiceInfoBinding.layoutOtp.setVisibility(View.GONE);
                        mFragmentServiceInfoBinding.lnrNoCustomer.setVisibility(View.GONE);
                    }
                    OnSiteOtp = mTaskDetailsData.get(0).getOnsite_OTP();
                    ScOtp = mTaskDetailsData.get(0).getSc_OTP();
                    String otp = mFragmentServiceInfoBinding.edtOnsiteOtp.getText().toString();
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

            }

            if (Mode.equals("Completed")) {
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
                    controller.getValidateCompletionTime(COMPLETION_REQUEST, mTaskDetailsData.get(0).getActualCompletionDateTime(), model.getTaskId());

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
