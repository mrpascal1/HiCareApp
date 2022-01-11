package com.ab.hicarerun.fragments;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.hicarerun.BaseApplication;
import com.ab.hicarerun.BaseFragment;
import com.ab.hicarerun.BuildConfig;
import com.ab.hicarerun.R;
import com.ab.hicarerun.activities.NewTaskDetailsActivity;
import com.ab.hicarerun.activities.ServiceRenewalActivity;
import com.ab.hicarerun.adapter.NewAttachmentListAdapter;
import com.ab.hicarerun.adapter.SlotsAdapter;
import com.ab.hicarerun.databinding.FragmentSignatureInfoBinding;
import com.ab.hicarerun.handler.OnDeleteListItemClickHandler;
import com.ab.hicarerun.handler.OnSaveEventHandler;
import com.ab.hicarerun.handler.UserSignatureClickHandler;
import com.ab.hicarerun.network.NetworkCallController;
import com.ab.hicarerun.network.NetworkResponseListner;
import com.ab.hicarerun.network.models.attachmentmodel.AttachmentDeleteRequest;
import com.ab.hicarerun.network.models.attachmentmodel.GetAttachmentList;
import com.ab.hicarerun.network.models.attachmentmodel.PostAttachmentRequest;
import com.ab.hicarerun.network.models.attachmentmodel.PostAttachmentResponse;
import com.ab.hicarerun.network.models.BasicResponse;
import com.ab.hicarerun.network.models.feedbackmodel.FeedbackRequest;
import com.ab.hicarerun.network.models.feedbackmodel.FeedbackResponse;
import com.ab.hicarerun.network.models.generalmodel.GeneralData;
import com.ab.hicarerun.network.models.LoginResponse;
import com.ab.hicarerun.network.models.slotmodel.TimeSlot;
import com.ab.hicarerun.network.models.tsscannermodel.BaseResponse;
import com.ab.hicarerun.network.models.tsscannermodel.counts.CountsResponse;
import com.ab.hicarerun.utils.AppUtils;
import com.ab.hicarerun.utils.SwipeToDeleteCallBack;
import com.bumptech.glide.Glide;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;
import io.realm.RealmResults;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignatureInfoFragment extends BaseFragment implements UserSignatureClickHandler, OnDeleteListItemClickHandler, FragmentScheduleDatePicker.onDatePickerListener, NewTaskDetailsActivity.OnAboutDataReceivedListener {
    FragmentSignatureInfoBinding mFragmentSignatureInfoBinding;
    private static final int POST_FEEDBACK_LINK = 1000;
    private static final int POST_ATTACHMENT_REQ = 2000;
    private static final int GET_ATTACHMENT_REQ = 3000;
    private static final int DELETE_ATTACHMENT_REQ = 4000;
    private static final int COMPLETION_REQUEST = 5000;
    private static final int SLOT_REQUEST = 6000;

    private static final String ARG_TASK = "ARG_TASK";
    private static final String ARG_VAR = "ARG_VAR";
    private static final String RENEWAL_TYPE = "RENEWAL_TYPE";
    private static final String RENEWAL_ORDER = "RENEWAL_ORDER";
    private String status = "";
    static String mFeedback = "";
    //    private boolean isAttachment = false;
    private OnSaveEventHandler mCallback;
    private DrawingView dv;
    private Paint mPaint;
    private Bitmap bmp = null;
    private String Email = "", mobile = "", Order_Number = "", Service_Name = "", mask = "", name = "", code = "";
    private String UserId = "";
    private String actual_property = "", feedback_code = "", signatory = "", signature = "";
    private Boolean isJobcardEnable = false;
    private Boolean isFeedBack = false;
    private String accountType = "";
    private RealmResults<GeneralData> mGeneralRealmData = null;
    private ArrayList<String> images = new ArrayList<>();
    private File imgFile;
    private String selectedImagePath = "";
    private Bitmap bitmap;
    private NewAttachmentListAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Integer pageNumber = 1;
    private String taskId = "";
    private String Renew_Type = "";
    private String Renew_Order = "";
    private String OTP = "";
    private String appointmentDate = "";
    private String assignmentStartTime = "";
    private String assignmentEndTime = "";
    private SlotsAdapter mSlotsAdapter;
    private int source = 0;
    private NewTaskDetailsActivity mActivity = null;
    CountDownTimer timer;
    private static final String FORMAT = "%02d:%02d";
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_GALLERY_PHOTO = 2;
    File mPhotoFile;
    private Bitmap selectedBmp = null;
    String uid = "";

//    private Tasks model;

    public SignatureInfoFragment() {
        // Required empty public constructor
    }

    public static SignatureInfoFragment newInstance(String taskId, String renewal_Type, String renewal_Order_No) {
        Bundle args = new Bundle();
        args.putString(ARG_TASK, taskId);
        args.putString(RENEWAL_TYPE, renewal_Type);
        args.putString(RENEWAL_ORDER, renewal_Order_No);
        SignatureInfoFragment fragment = new SignatureInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            taskId = getArguments().getString(ARG_TASK);
            Renew_Type = getArguments().getString(RENEWAL_TYPE);
            Renew_Order = getArguments().getString(RENEWAL_ORDER);
        }
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
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFragmentSignatureInfoBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_signature_info, container, false);
        feedback_code = Objects.requireNonNull(mFragmentSignatureInfoBinding.txtFeedback.getText()).toString();
        signatory = Objects.requireNonNull(mFragmentSignatureInfoBinding.edtSignatory.getText()).toString();
        mActivity = (NewTaskDetailsActivity) getActivity();
        Objects.requireNonNull(mActivity).setAboutDataListener(this);
        assert getArguments() != null;
        mFeedback = getArguments().getString(ARG_VAR);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(8);
        mFragmentSignatureInfoBinding.setHandler(this);
        return mFragmentSignatureInfoBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        try {
            getSignature();
            mFragmentSignatureInfoBinding.txtRenewTitle.setTypeface(Typeface.DEFAULT, Typeface.BOLD_ITALIC);
            mFragmentSignatureInfoBinding.txtNew.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            mFragmentSignatureInfoBinding.txtFeedback.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    getValidate();
                }
            });


            mFragmentSignatureInfoBinding.edtSignatory.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    getValidate();
                }
            });
            mFragmentSignatureInfoBinding.rgCMS.setOnCheckedChangeListener((radioGroup, i) -> getValidate());
            mFragmentSignatureInfoBinding.btnRenew.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ServiceRenewalActivity.class);
                    intent.putExtra(ServiceRenewalActivity.ARGS_TASKS, taskId);
                    startActivity(intent);
                }
            });
            mFragmentSignatureInfoBinding.referBtn.setOnClickListener(v -> {
                getReferral();
            });

            mFragmentSignatureInfoBinding.recycleView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(getActivity());
            mFragmentSignatureInfoBinding.recycleView.setLayoutManager(layoutManager);
            assert mGeneralRealmData.get(0) != null;
            mAdapter = new NewAttachmentListAdapter(getActivity(), mGeneralRealmData.get(0).getSchedulingStatus());
            mFragmentSignatureInfoBinding.recycleView.setAdapter(mAdapter);
            mAdapter.setOnItemClickHandler(this);
            getAttachmentList();
            resetTimer();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //getBarcodeCount(Order_Number, uid);
    }

    @Override
    public void onResume() {
        super.onResume();
        getBarcodeCount(Order_Number, uid);
    }

    private void enableSwipeToDeleteAndUndo() {

        SwipeToDeleteCallBack swipeToDeleteCallback = new SwipeToDeleteCallBack(getActivity()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                final int position = viewHolder.getAdapterPosition();
                getJobCardDeleted(position);
            }
        };
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(mFragmentSignatureInfoBinding.recycleView);
    }

    private void getJobCardDeleted(int position) {
        try {
            AttachmentDeleteRequest request = new AttachmentDeleteRequest();
            List<AttachmentDeleteRequest> model = new ArrayList<>();
            request.setId(mAdapter.getItem(position).getId());
            request.setTaskId(mAdapter.getItem(position).getTaskId());
            request.setResourceId(mAdapter.getItem(position).getResourceId());
            request.setCreated_On(mAdapter.getItem(position).getCreated_On());
            request.setFileName(mAdapter.getItem(position).getFileName());
            request.setFilePath(mAdapter.getItem(position).getFilePath());
            request.setFile(mAdapter.getItem(position).getFile());
            model.add(request);
            NetworkCallController controller = new NetworkCallController(SignatureInfoFragment.this);
            controller.setListner(new NetworkResponseListner() {
                @Override
                public void onResponse(int requestCode, Object data) {
                    PostAttachmentResponse deleteResponse = (PostAttachmentResponse) data;
                    if (deleteResponse.getSuccess()) {
                        mAdapter.removeAll();
                        if (mAdapter.getItemCount() == 0) {
                            mFragmentSignatureInfoBinding.txtData.setVisibility(View.VISIBLE);
                        } else {
                            mFragmentSignatureInfoBinding.txtData.setVisibility(GONE);
                        }
                        getAttachmentList();
                        Toasty.success(getActivity(), "Deleted successfully.", Toasty.LENGTH_SHORT).show();
                        mAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getActivity(), "Failed.", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(int requestCode) {

                }
            });
            controller.getDeleteAttachments(DELETE_ATTACHMENT_REQ, model);
        } catch (Exception e) {
            RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
            if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                assert mLoginRealmModels.get(0) != null;
                String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                AppUtils.sendErrorLogs(e.getMessage(), getClass().getSimpleName(), "onDeleteImageClicked", lineNo, userName, DeviceName);
            }
        }
    }


    private void getSignature() {
        try {

            mGeneralRealmData =
                    getRealm().where(GeneralData.class).findAll();

            if (mGeneralRealmData != null && mGeneralRealmData.size() > 0) {
                isJobcardEnable = mGeneralRealmData.get(0) != null ? mGeneralRealmData.get(0).getJobCardRequired() : false;

                isFeedBack = mGeneralRealmData.get(0) != null ? mGeneralRealmData.get(0).getFeedBack() : false;
                accountType = mGeneralRealmData.get(0) != null ? mGeneralRealmData.get(0).getAccountType() : "NA";
                status = mGeneralRealmData.get(0) != null ? mGeneralRealmData.get(0).getSchedulingStatus() : "NA";
                if (mGeneralRealmData.get(0).getNext_SR_Planned_Start_Date() != null) {
                    try {
                        mFragmentSignatureInfoBinding.txtPlannedDate.setText(AppUtils.reFormatDateAndTime(mGeneralRealmData.get(0).getNext_SR_Planned_Start_Date(), "dd-MMM-yyyy"));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    mFragmentSignatureInfoBinding.txtPlannedDate.setText("NA");
                }
                mFragmentSignatureInfoBinding.txtPlannedDate.setTypeface(mFragmentSignatureInfoBinding.txtPlannedDate.getTypeface(), Typeface.BOLD);

                if (mGeneralRealmData.get(0).getShowNextServiceAppointment() && status.equals("On-Site")) {
                    mFragmentSignatureInfoBinding.lnrServiceDate.setVisibility(View.VISIBLE);
                    mFragmentSignatureInfoBinding.lnrPlannedServiceDate.setVisibility(View.VISIBLE);
                    source = 0;
                } else if ((mGeneralRealmData.get(0).getFlushoutRequired() || mGeneralRealmData.get(0).getGelTreatmentRequired()) && status.equals("On-Site") && mGeneralRealmData.get(0).getTag() != null) {
                    if (mGeneralRealmData.get(0).getTag().equalsIgnoreCase("complaint") && AppUtils.infestationLevel.equalsIgnoreCase("high infestation")) {
                        mFragmentSignatureInfoBinding.lnrBook.setText("SCHEDULE FLUSH OUT SERVICE");
                        mFragmentSignatureInfoBinding.lnrServiceDate.setVisibility(View.VISIBLE);
                        mFragmentSignatureInfoBinding.lnrPlannedServiceDate.setVisibility(GONE);
                        source = 1;
                    } else if (mGeneralRealmData.get(0).getTag().equalsIgnoreCase("flushout") || mGeneralRealmData.get(0).getTag().equalsIgnoreCase("incomplete flushout")) {
                        mFragmentSignatureInfoBinding.lnrBook.setText("SCHEDULE GEL TREATMENT");
                        mFragmentSignatureInfoBinding.lnrServiceDate.setVisibility(View.VISIBLE);
                        mFragmentSignatureInfoBinding.lnrPlannedServiceDate.setVisibility(GONE);
                        source = 2;
                    } else if (mGeneralRealmData.get(0).getTag().equalsIgnoreCase("gel") || mGeneralRealmData.get(0).getTag().equalsIgnoreCase("incomplete gel")) {
                        mFragmentSignatureInfoBinding.lnrServiceDate.setVisibility(GONE);
                        mFragmentSignatureInfoBinding.lnrPlannedServiceDate.setVisibility(GONE);
                    } else {
                        mFragmentSignatureInfoBinding.lnrServiceDate.setVisibility(GONE);
                        mFragmentSignatureInfoBinding.lnrPlannedServiceDate.setVisibility(GONE);
                    }
                } else {
                    if (mGeneralRealmData.get(0).getTag() != null && status.equalsIgnoreCase("completed") && (mGeneralRealmData.get(0).getTag().equalsIgnoreCase("complaint") || mGeneralRealmData.get(0).getTag().equalsIgnoreCase("flushout") || mGeneralRealmData.get(0).getTag().equalsIgnoreCase("incomplete flushout"))) {
                        mFragmentSignatureInfoBinding.lnrPlannedServiceDate.setVisibility(View.VISIBLE);
                    } else {
                        mFragmentSignatureInfoBinding.lnrPlannedServiceDate.setVisibility(GONE);
                    }
                    mFragmentSignatureInfoBinding.lnrServiceDate.setVisibility(GONE);

                }

                if (isJobcardEnable) {
                    mFragmentSignatureInfoBinding.lnrJobCard.setVisibility(View.VISIBLE);
                } else {
                    mFragmentSignatureInfoBinding.lnrJobCard.setVisibility(View.GONE);
                }

                if (Renew_Type != null && Renew_Type.equals("Renewal")) {
                    if (mGeneralRealmData.get(0).getNo_Renewal_Reason() != null && !mGeneralRealmData.get(0).getNo_Renewal_Reason().equals("")) {
                        mFragmentSignatureInfoBinding.lnrRenew.setVisibility(View.VISIBLE);
                        mFragmentSignatureInfoBinding.btnRenew.setVisibility(GONE);
                        mFragmentSignatureInfoBinding.txtRenewTitle.setText("Renewal Not Interested");
                        mFragmentSignatureInfoBinding.txtRenewDes.setText(mGeneralRealmData.get(0).getNo_Renewal_Reason());
                        mFragmentSignatureInfoBinding.txtRenewTitle.setTextSize(15f);
                        mFragmentSignatureInfoBinding.txtRenewDes.setTextSize(15f);
                        mFragmentSignatureInfoBinding.lnrRenew.setWeightSum(8);

                    } else {
                        mFragmentSignatureInfoBinding.lnrRenew.setVisibility(View.VISIBLE);
                        mFragmentSignatureInfoBinding.txtRenewTitle.setText(R.string.service_renewal);
                        mFragmentSignatureInfoBinding.lnrRenew.setWeightSum(10);
                    }
//                    mFragmentSignatureInfoBinding.lnrRenew.setVisibility(View.VISIBLE);
                } else {
                    mFragmentSignatureInfoBinding.lnrRenew.setVisibility(GONE);
                }

                if (Renew_Order != null && !Renew_Order.equals("")) {
                    mFragmentSignatureInfoBinding.lnrRenew.setVisibility(View.VISIBLE);
                    mFragmentSignatureInfoBinding.btnRenew.setVisibility(GONE);
                    mFragmentSignatureInfoBinding.lnrRenew.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    mFragmentSignatureInfoBinding.txtRenewTitle.setText("Order# " + Renew_Order + " Renewed Successfully");
                    mFragmentSignatureInfoBinding.txtRenewDes.setText(R.string.you_did_a_great_job);
                    mFragmentSignatureInfoBinding.txtRenewTitle.setTextSize(15f);
                    mFragmentSignatureInfoBinding.txtRenewDes.setTextSize(15f);
                    mFragmentSignatureInfoBinding.lnrRenew.setWeightSum(8);
                }
                if (mGeneralRealmData.get(0).getShowSignature() != null && mGeneralRealmData.get(0).getShowSignature()) {
                    mFragmentSignatureInfoBinding.signatureTitle.setVisibility(View.VISIBLE);
                    mFragmentSignatureInfoBinding.signatureBox.setVisibility(View.VISIBLE);
                    mFragmentSignatureInfoBinding.lnrSignatory.setVisibility(View.VISIBLE);
                } else {
                    mCallback.isSignatureChanged(false);
                    mCallback.isSignatureValidated(false);
                    mFragmentSignatureInfoBinding.signatureTitle.setVisibility(GONE);
                    mFragmentSignatureInfoBinding.signatureBox.setVisibility(GONE);
                    mFragmentSignatureInfoBinding.lnrSignatory.setVisibility(GONE);
                }
                if (status.equals("Completed") || status.equals("Incomplete")) {
                    mFragmentSignatureInfoBinding.edtSignatory.setEnabled(false);
                    mFragmentSignatureInfoBinding.edtSignatory.setText(mGeneralRealmData.get(0) != null ? mGeneralRealmData.get(0).getSignatory() : "NA");
                    mFragmentSignatureInfoBinding.txtFeedback.setText(mGeneralRealmData.get(0) != null ? mGeneralRealmData.get(0).getTechnicianOTP() : "NA");
                    mFragmentSignatureInfoBinding.txtHint.setVisibility(GONE);
                    mFragmentSignatureInfoBinding.txtFeedback.setEnabled(false);
                    mFragmentSignatureInfoBinding.lnrBottom.setVisibility(GONE);
//                    mFragmentSignatureInfoBinding.btnUpload.setVisibility(GONE);
                    mFragmentSignatureInfoBinding.lnrJobCard.setVisibility(GONE);
                    mFragmentSignatureInfoBinding.imgSign.setVisibility(View.VISIBLE);
                    if (mGeneralRealmData.get(0).getAmountCollected() != null && !mGeneralRealmData.get(0).getAmountCollected().equals("0")) {
                        mFragmentSignatureInfoBinding.lnrOrder.setVisibility(View.VISIBLE);
                        mFragmentSignatureInfoBinding.txtAmount.setText("₹ " + mGeneralRealmData.get(0).getAmountCollected());
                    } else {
                        mFragmentSignatureInfoBinding.lnrOrder.setVisibility(View.GONE);
                    }

                    if (mGeneralRealmData.get(0).getPaymentMode() != null && !mGeneralRealmData.get(0).getPaymentMode().equals("") && !mGeneralRealmData.get(0).getPaymentMode().equalsIgnoreCase("none")) {
                        mFragmentSignatureInfoBinding.lnrType.setVisibility(View.VISIBLE);
                        if(mGeneralRealmData.get(0).getPaymentMode()!=null){
                            mFragmentSignatureInfoBinding.txtType.setText(mGeneralRealmData.get(0).getPaymentMode());
                        }
                    } else {
                        mFragmentSignatureInfoBinding.lnrType.setVisibility(View.GONE);
                    }

                } else if (status.equals("Dispatched")) {
                    mFragmentSignatureInfoBinding.edtSignatory.setBackgroundResource(R.drawable.disable_edit_borders);
                    mFragmentSignatureInfoBinding.imgSign.setVisibility(GONE);
                    mFragmentSignatureInfoBinding.edtSignatory.setEnabled(false);
                    mFragmentSignatureInfoBinding.txtHint.setVisibility(GONE);
                    mFragmentSignatureInfoBinding.txtFeedback.setEnabled(false);
                    mFragmentSignatureInfoBinding.lnrBottom.setVisibility(GONE);
//                    mFragmentSignatureInfoBinding.btnUpload.setVisibility(GONE);
                    mFragmentSignatureInfoBinding.lnrJobCard.setVisibility(GONE);
                } else {
                    mFragmentSignatureInfoBinding.edtSignatory.setEnabled(true);
                    mFragmentSignatureInfoBinding.imgSign.setEnabled(true);
                    mFragmentSignatureInfoBinding.txtHint.setVisibility(View.VISIBLE);
                    mFragmentSignatureInfoBinding.txtAmount.setText("₹" + " " + 0);
                }

                if (mGeneralRealmData.get(0).getIsFlushOutRequired() != null && mGeneralRealmData.get(0).getIsFlushOutRequired()) {
                    mFragmentSignatureInfoBinding.lnrCMSReason.setVisibility(View.VISIBLE);
                } else {
                    mFragmentSignatureInfoBinding.lnrCMSReason.setVisibility(GONE);
                }

                mobile = mGeneralRealmData.get(0) != null ? mGeneralRealmData.get(0).getMobileNumber() : "NA";
                Order_Number = mGeneralRealmData.get(0) != null ? mGeneralRealmData.get(0).getOrderNumber() : "NA";
                Service_Name = mGeneralRealmData.get(0) != null ? mGeneralRealmData.get(0).getServicePlan() : "NA";
                name = mGeneralRealmData.get(0) != null ? mGeneralRealmData.get(0).getCustName() : "NA";
                code = mGeneralRealmData.get(0) != null ? mGeneralRealmData.get(0).getTechnicianOTP() : "NA";
                Email = mGeneralRealmData.get(0) != null ? mGeneralRealmData.get(0).getEmail() : "NA";
//                feedback_code = mFragmentSignatureInfoBinding.txtFeedback.getText().toString();
//                signatory = mFragmentSignatureInfoBinding.edtSignatory.getText().toString();

                try {
                    if (mGeneralRealmData.get(0).getSignatureUrl() != null && !mGeneralRealmData.get(0).getSignatureUrl().equals("")) {
                        mFragmentSignatureInfoBinding.txtHint.setVisibility(GONE);
                        Glide.with(getActivity())
                                .load(mGeneralRealmData.get(0).getSignatureUrl())
                                .error(android.R.drawable.stat_notify_error)
                                .into(mFragmentSignatureInfoBinding.imgSign);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
//
                try {
                    if (mobile != null && mobile.length() > 0)
                        mask = mobile.replaceAll("\\w(?=\\w{4})", "*");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                getValidate();
            }
        } catch (Exception e) {
            RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
            if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                AppUtils.sendErrorLogs(e.getMessage(), getClass().getSimpleName(), "getSignature", lineNo, userName, DeviceName);
            }
        }
    }

    private void getSignatureDialog() {
        try {
            if (mFragmentSignatureInfoBinding.imgSign.getDrawable() == null) {
                LayoutInflater li = LayoutInflater.from(getActivity());
                View promptsView = li.inflate(R.layout.signature_dialog, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setView(promptsView);
                final AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialogBuilder.setTitle("Signature");
                final RelativeLayout lnr_screen =
                        promptsView.findViewById(R.id.lnr_screen);
                final AppCompatImageView img_right =
                        promptsView.findViewById(R.id.img_right);
                final AppCompatImageView img_wrong =
                        promptsView.findViewById(R.id.img_wrong);
                final AppCompatButton btn_close =
                        promptsView.findViewById(R.id.btn_close);
                final AppCompatTextView txt_hint =
                        promptsView.findViewById(R.id.txt_hint);
                img_right.setEnabled(false);
                dv = new DrawingView(getActivity(), txt_hint, img_right);
                lnr_screen.addView(dv);
                img_right.setOnClickListener(v -> {
                    View view = lnr_screen;
                    view.setDrawingCacheEnabled(true);
                    bmp = Bitmap.createBitmap(view.getDrawingCache());
                    view.setDrawingCacheEnabled(false);
                    onCallBack(bmp);
                    alertDialog.dismiss();
                });

                img_wrong.setOnClickListener(v -> {
                    lnr_screen.removeAllViews();
                    dv = new DrawingView(getActivity(), txt_hint, img_right);
                    mPaint = new Paint();
                    mPaint.setAntiAlias(true);
                    mPaint.setDither(true);
                    mPaint.setColor(Color.BLACK);
                    mPaint.setStyle(Paint.Style.STROKE);
                    mPaint.setStrokeJoin(Paint.Join.ROUND);
                    mPaint.setStrokeCap(Paint.Cap.ROUND);
                    mPaint.setStrokeWidth(8);
                    lnr_screen.addView(dv);
                    txt_hint.setVisibility(View.VISIBLE);
                    getValidate();
                });

                btn_close.setOnClickListener(v -> {
                    alertDialog.dismiss();
                    getValidate();

                });
                alertDialog.show();
                alertDialog.setIcon(R.mipmap.logo);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getBarcodeCount(String orderNo, String userId){
        Log.d("TAG", "User "+userId+" order "+orderNo);
        NetworkCallController controller = new NetworkCallController();
        controller.setListner(new NetworkResponseListner<CountsResponse>() {
            @Override
            public void onResponse(int requestCode, CountsResponse response) {
                if (response != null) {
                    if (response.isSuccess()) {
                        if (response.getData() != null) {
                            if (response.getData().getDeployed() > 0) {
                                mFragmentSignatureInfoBinding.countTv.setText(response.getData().getTotalScanned() + " / " + response.getData().getDeployed());
                                mFragmentSignatureInfoBinding.lnrBarcodeCount.setVisibility(View.VISIBLE);
                            } else {
                                mFragmentSignatureInfoBinding.lnrBarcodeCount.setVisibility(GONE);
                            }
                        }else {
                            mFragmentSignatureInfoBinding.lnrBarcodeCount.setVisibility(GONE);
                        }
                    }
                }else {
                    mFragmentSignatureInfoBinding.lnrBarcodeCount.setVisibility(GONE);
                }
            }

            @Override
            public void onFailure(int requestCode) {
                Log.d("TAG", ""+requestCode);
            }
        });
        controller.getBarcodeSummaryCount(202108, orderNo, userId);
    }

    @Override
    public void onSignatureClicked(View view) {
        if (status.equals("Completed") || status.equals("Incomplete")) {
            Log.v("state", status);
        } else {
            Log.v("state", status);
            getSignatureDialog();
        }
    }

    void resetTimer() {
        timer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mFragmentSignatureInfoBinding.feedBackTimer.setText("Resend in " + String.format(FORMAT,
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
//                mFragmentSignatureInfoBinding.feedBackTimer.setText(String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                mFragmentSignatureInfoBinding.btnSendlink.setAlpha(1.0f);
                mFragmentSignatureInfoBinding.feedBackTimer.setVisibility(View.GONE);
                mFragmentSignatureInfoBinding.btnSendlink.setEnabled(true);

            }
        };
    }

    @Override
    public void onSendLinkClicked(View view) {
        sendFeedbackLink();
    }

    private void sendFeedbackLink() {
        try {
            if (isFeedBack && status.equals("On-Site") && mGeneralRealmData.get(0).getRestrict_Early_Completion()) {
                NetworkCallController controller = new NetworkCallController(this);
                controller.setListner(new NetworkResponseListner() {
                    @Override
                    public void onResponse(int requestCode, Object data) {
                        BasicResponse response = (BasicResponse) data;
                        if (response.getSuccess()) {
                            sendFeedback();

                        } else {
                            Toasty.error(getActivity(),
                                    getString(R.string.spent_adequate_time),
                                    Toasty.LENGTH_LONG).show();
                            getValidate();
                        }
                    }

                    @Override
                    public void onFailure(int requestCode) {
                    }
                });
                controller.getValidateCompletionTime(COMPLETION_REQUEST, mGeneralRealmData.get(0).getActualCompletionDateTime(), taskId);
            } else {
                sendFeedback();
                getValidate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void sendFeedback() {

        try {
            LayoutInflater li = LayoutInflater.from(getActivity());
            View promptsView = li.inflate(R.layout.link_confirm_dialog, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
            alertDialogBuilder.setView(promptsView);
            alertDialogBuilder.setTitle("Feedback Link");
            final AlertDialog alertDialog = alertDialogBuilder.create();
            final AppCompatEditText edtmobile =
                    promptsView.findViewById(R.id.edtmobile);
            final AppCompatEditText edtemail =
                    promptsView.findViewById(R.id.edtemail);
            final AppCompatButton btn_send =
                    promptsView.findViewById(R.id.btn_send);
            final AppCompatButton btn_cancel =
                    promptsView.findViewById(R.id.btn_cancel);
            edtemail.setEnabled(false);
            edtmobile.setEnabled(false);
            edtemail.setText(Email);
            edtmobile.setText(mask);
            btn_send.setOnClickListener(v -> {

                String customer_otp = mGeneralRealmData.get(0).getCustomer_OTP();
                FeedbackRequest request = new FeedbackRequest();
                request.setName(name);
                request.setTask_id(taskId);
                request.setFeedback_code(customer_otp);
                request.setOrder_number(Order_Number);
                request.setService_name(Service_Name);
                NetworkCallController controller = new NetworkCallController(SignatureInfoFragment.this);
                controller.setListner(new NetworkResponseListner() {
                    @Override
                    public void onResponse(int requestCode, Object response) {
                        FeedbackResponse refResponse = (FeedbackResponse) response;
                        if (refResponse.getSuccess()) {
                            mFragmentSignatureInfoBinding.btnSendlink.setAlpha(0.5f);
                            mFragmentSignatureInfoBinding.feedBackTimer.setVisibility(View.VISIBLE);
                            mFragmentSignatureInfoBinding.btnSendlink.setEnabled(false);
                            if (timer != null)
                                timer.cancel();

                            timer.start();
                            Toasty.success(getActivity(), getString(R.string.feedback_link_spent_successfully), Toasty.LENGTH_LONG).show();
                            getValidate();
                        }
                    }

                    @Override
                    public void onFailure(int requestCode) {

                    }
                });
                controller.postFeedbackLink(POST_FEEDBACK_LINK, request);

                alertDialog.dismiss();
            });
            btn_cancel.setOnClickListener(v -> alertDialog.dismiss());
            alertDialog.setIcon(R.mipmap.logo);
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void onCallBack(Bitmap bmp) {

        try {
            if (bmp != null) {
                mFragmentSignatureInfoBinding.txtHint.setVisibility(GONE);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] b = baos.toByteArray();
                String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                signature = encodedImage;
                mCallback.signature(encodedImage);
                mCallback.signatory(mFragmentSignatureInfoBinding.edtSignatory.getText().toString());
                mFragmentSignatureInfoBinding.imgSign.setImageBitmap(bmp);
                mFragmentSignatureInfoBinding.imgSign.setVisibility(View.VISIBLE);
                getValidate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUploadAttachmentClicked(View view) {
//        try {
//            RealmResults<GeneralData> mGeneralRealmData =
//                    getRealm().where(GeneralData.class).findAll();
//            if (mGeneralRealmData != null && mGeneralRealmData.size() > 0) {
//                isJobcardEnable = mGeneralRealmData.get(0).getJobCardRequired();
//                if (isJobcardEnable) {
//                    PickImageDialog.build(new PickSetup()).setOnPickResult(pickResult -> {
//                        if (pickResult.getError() == null) {
//                            images.add(pickResult.getPath());
////                            imgFile = new File(pickResult.getPath());
////                            selectedImagePath = pickResult.getPath();
////                            if (selectedImagePath != null) {
////                                Bitmap bit = new BitmapDrawable(getActivity().getResources(),
////                                        selectedImagePath).getBitmap();
////                                int i = (int) (bit.getHeight() * (1024.0 / bit.getWidth()));
////                                bitmap = Bitmap.createScaledBitmap(bit, 1024, i, true);
////                            }
//
//                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                            pickResult.getBitmap().compress(Bitmap.CompressFormat.JPEG, 100, baos);
//                            byte[] b = baos.toByteArray();
//                            String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
//
//                            RealmResults<LoginResponse> LoginRealmModels =
//                                    BaseApplication.getRealm().where(LoginResponse.class).findAll();
//                            if (pickResult.getPath() != null) {
//                                if (LoginRealmModels != null && LoginRealmModels.size() > 0) {
//                                    UserId = LoginRealmModels.get(0).getUserID();
//                                    NetworkCallController controller = new NetworkCallController(SignatureInfoFragment.this);
//                                    PostAttachmentRequest request = new PostAttachmentRequest();
//                                    request.setFile(encodedImage);
//                                    request.setResourceId(UserId);
//                                    request.setTaskId(taskId);
//                                    controller.setListner(new NetworkResponseListner() {
//                                        @Override
//                                        public void onResponse(int requestCode, Object response) {
//                                            PostAttachmentResponse postResponse = (PostAttachmentResponse) response;
//                                            if (postResponse.getSuccess()) {
//                                                Toasty.success(getActivity(), getString(R.string.jobcard_uploaded_successfully), Toast.LENGTH_LONG).show();
//                                                getAttachmentList();
//                                            } else {
//                                                Toasty.error(getActivity(), getString(R.string.uploading_failed), Toast.LENGTH_LONG).show();
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onFailure(int requestCode) {
//                                        }
//                                    });
//
//                                    controller.postAttachments(POST_ATTACHMENT_REQ, request);
//                                }
//                            }
//                        }
//                    }).show(getActivity());
//                }
//            }
//        } catch (Exception e) {
//            RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
//            if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
//                String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
//                String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
//                String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
//                AppUtils.sendErrorLogs(e.getMessage(), getClass().getSimpleName(), "onAddImageClicked", lineNo, userName, DeviceName);
//            }
//        }
        getAddJobCard();
    }

    @Override
    public void onScheduleDateClicked(View view) {
        showDatePicker();
    }

    @Override
    public void onBookAppointmentClicked(View view) {
        try {
            LayoutInflater li = LayoutInflater.from(getActivity());
            View promptsView = li.inflate(R.layout.next_service_appointment_layout, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
            alertDialogBuilder.setView(promptsView);
            final AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            final RecyclerView recyclerView =
                    promptsView.findViewById(R.id.recycleSlots);
            final CalendarView mCalendarView = promptsView.findViewById(R.id.calendarView);
            final AppCompatButton btnBook = promptsView.findViewById(R.id.btnBook);
            final AppCompatButton btnCancel = promptsView.findViewById(R.id.btnCancel);
            final TextView txtDateHead = promptsView.findViewById(R.id.txtDateHead);
            final TextView txtSlotHead = promptsView.findViewById(R.id.txtSlotHead);
            final TextView txtNoSlots = promptsView.findViewById(R.id.txtNoSlots);
            final TextView txtSelectDate = promptsView.findViewById(R.id.txtSelectDate);
            txtDateHead.setTypeface(txtDateHead.getTypeface(), Typeface.BOLD);
            txtSlotHead.setTypeface(txtSlotHead.getTypeface(), Typeface.BOLD);
            String startDate = "";
            String endDate = "";

            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            mSlotsAdapter = new SlotsAdapter(getActivity());
            recyclerView.setAdapter(mSlotsAdapter);


            if (mGeneralRealmData.get(0).getTag() != null && (mGeneralRealmData.get(0).getTag().equalsIgnoreCase("complaint"))) {
                startDate = AppUtils.reFormatDateAndTime(mGeneralRealmData.get(0).getFlushout_Start_Date(), "yyyy-MM-dd");
                endDate = AppUtils.reFormatDateAndTime(mGeneralRealmData.get(0).getFlushout_End_Date(), "yyyy-MM-dd");
            } else if (mGeneralRealmData.get(0).getTag() != null && (mGeneralRealmData.get(0).getTag().equalsIgnoreCase("flushout") || mGeneralRealmData.get(0).getTag().equalsIgnoreCase("incomplete flushout"))) {
                startDate = AppUtils.reFormatDateAndTime(mGeneralRealmData.get(0).getGelTreatment_Start_Date(), "yyyy-MM-dd");
                endDate = AppUtils.reFormatDateAndTime(mGeneralRealmData.get(0).getGelTreatment_End_Date(), "yyyy-MM-dd");
            } else {
                startDate = AppUtils.reFormatDateAndTime(mGeneralRealmData.get(0).getNext_SR_Planned_Start_Date(), "yyyy-MM-dd");
                endDate = AppUtils.reFormatDateAndTime(mGeneralRealmData.get(0).getNext_SR_Planned_End_Date(), "yyyy-MM-dd");
            }

            String sParts[] = startDate.split("-");
            String eParts[] = endDate.split("-");

            int sYear = Integer.parseInt(sParts[0]);
            int sMonth = Integer.parseInt(sParts[1]);
            int sDay = Integer.parseInt(sParts[2]);

            int eYear = Integer.parseInt(eParts[0]);
            int eMonth = Integer.parseInt(eParts[1]);
            int eDay = Integer.parseInt(eParts[2]);

            Calendar sCalendar = Calendar.getInstance();
            sCalendar.set(Calendar.YEAR, sYear);
            sCalendar.set(Calendar.MONTH, sMonth - 1);
            sCalendar.set(Calendar.DAY_OF_MONTH, sDay);
            long startTime = sCalendar.getTimeInMillis();

            Calendar eCalendar = Calendar.getInstance();
            eCalendar.set(Calendar.YEAR, eYear);
            eCalendar.set(Calendar.MONTH, eMonth - 1);
            eCalendar.set(Calendar.DAY_OF_MONTH, eDay);
            long endTime = eCalendar.getTimeInMillis();
            mCalendarView.setMinDate(startTime);
            mCalendarView.setMaxDate(endTime);


            if (!appointmentDate.equals("")) {
                String selectedDate = appointmentDate;
                String cParts[] = selectedDate.split("-");
                int cYear = Integer.parseInt(cParts[0]);
                int cMonth = Integer.parseInt(cParts[1]);
                int cDay = Integer.parseInt(cParts[2]);
                Calendar cCalendar = Calendar.getInstance();
                cCalendar.set(Calendar.YEAR, cYear);
                cCalendar.set(Calendar.MONTH, cMonth - 1);
                cCalendar.set(Calendar.DAY_OF_MONTH, cDay);
                long selectedTime = cCalendar.getTimeInMillis();
                mCalendarView.setDate(selectedTime);
            } else {
                mCalendarView.setDate(startTime);
            }


            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            appointmentDate = sdf.format(new Date(mCalendarView.getDate()));
            AppUtils.appointmentDate = appointmentDate;

//            getSlots(startDate);

            mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                    appointmentDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                    AppUtils.appointmentDate = appointmentDate;
                    getSlots(appointmentDate, txtNoSlots, txtSelectDate, recyclerView);
                }
            });

            btnBook.setOnClickListener(v -> {
                try {
                    if (!assignmentStartTime.equals("") && !assignmentEndTime.equals("")) {
                        mCallback.assignmentStartDate(appointmentDate);
                        mCallback.assignmentStartTime(assignmentStartTime);
                        mCallback.assignmentEndTime(assignmentEndTime);
                        Log.i("DATE", appointmentDate);
                        String slotDate = AppUtils.getFormatted(appointmentDate, "MMM dd, yyyy", "yyyy-MM-dd");
                        AppUtils.appointmentDate = appointmentDate;
                        mFragmentSignatureInfoBinding.lnrSelectedDate.setVisibility(View.VISIBLE);
                        mFragmentSignatureInfoBinding.txtAppointmentTitle.setTypeface(mFragmentSignatureInfoBinding.txtAppointmentTitle.getTypeface(), Typeface.BOLD);
                        mFragmentSignatureInfoBinding.txtSelectdSlot.setText(slotDate + " | " + assignmentStartTime + " - " + assignmentEndTime);
                        mFragmentSignatureInfoBinding.lnrBook.setText("CHANGE YOUR SLOT");
                        assignmentEndTime = "";
                        assignmentStartTime = "";
                        Toasty.success(getActivity(), "Appointment booked successfully.", Toasty.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    } else {
                        Toasty.error(getActivity(), "Please select time slot.", Toasty.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });

            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setCancelable(false);
            alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getSlots(String date, TextView txtNoSlots, TextView txtSelectDate, RecyclerView recyclerView) {
        NetworkCallController controller = new NetworkCallController(this);
        controller.setListner(new NetworkResponseListner<List<TimeSlot>>() {

            @Override
            public void onResponse(int requestCode, List<TimeSlot> items) {
                txtSelectDate.setVisibility(View.GONE);
                if (items != null && items.size() > 0) {
                    mSlotsAdapter.setData(items);
                    mSlotsAdapter.notifyDataSetChanged();
                    txtNoSlots.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    mSlotsAdapter.setOnItemClickHandler(position -> {
                        assignmentStartTime = mSlotsAdapter.getItem(position).getStartTime();
                        AppUtils.appointmentStartTime = assignmentStartTime;
                        assignmentEndTime = mSlotsAdapter.getItem(position).getFinishTime();
                        AppUtils.appointmentEndTime = assignmentEndTime;
                    });
                } else {
                    recyclerView.setVisibility(GONE);
                    txtNoSlots.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(int requestCode) {

            }
        });
        controller.getAppointmentSlots(SLOT_REQUEST, mGeneralRealmData.get(0).getTaskId(), date, date, source);
    }

    private Calendar dateToCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    private void showDatePicker() {
        try {
            FragmentScheduleDatePicker mFragDatePicker = new FragmentScheduleDatePicker();
            mFragDatePicker.setmDatePickerListener(this);
            mFragDatePicker.show(getActivity().getSupportFragmentManager(), "datepicker");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getAttachmentList() {
        try {
            RealmResults<LoginResponse> LoginRealmModels =
                    BaseApplication.getRealm().where(LoginResponse.class).findAll();
            if (LoginRealmModels != null && LoginRealmModels.size() > 0) {
                UserId = LoginRealmModels.get(0).getUserID();
                uid = LoginRealmModels.get(0).getId();
                NetworkCallController controller = new NetworkCallController(this);
                controller.setListner(new NetworkResponseListner<List<GetAttachmentList>>() {

                    @Override
                    public void onResponse(int requestCode, List<GetAttachmentList> items) {
                        if (items != null) {
                            if (items.size() > 0) {
                                mFragmentSignatureInfoBinding.txtData.setVisibility(View.GONE);
                                mAdapter.addData(items);
                                mAdapter.notifyDataSetChanged();
                                if (isJobcardEnable)
                                    mCallback.isJobCardEnable(false);
                                enableSwipeToDeleteAndUndo();
                            } else {
                                if (isJobcardEnable)
                                    mCallback.isJobCardEnable(true);
                                pageNumber--;
                            }
                        } else {
                            if (isJobcardEnable)
                                mCallback.isJobCardEnable(true);
                            mFragmentSignatureInfoBinding.txtData.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(int requestCode) {

                    }
                });
                controller.getAttachments(GET_ATTACHMENT_REQ, taskId, UserId);
            }
        } catch (Exception e) {
            RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
            if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                AppUtils.sendErrorLogs(e.getMessage(), getClass().getSimpleName(), "getAttachmentList", lineNo, userName, DeviceName);
            }
        }
    }

    private void getValidate() {
        try {
            if (mGeneralRealmData.get(0).getIsFlushOutRequired() != null && mGeneralRealmData.get(0).getIsFlushOutRequired() && mFragmentSignatureInfoBinding.rgCMS.getCheckedRadioButtonId() == -1) {
                mCallback.isWorkTypeNotChecked(true);
            } else {
                mCallback.isWorkTypeNotChecked(false);
                if (mFragmentSignatureInfoBinding.rgCMS.getCheckedRadioButtonId() == 0) {
                    mCallback.FlushOutReason(mFragmentSignatureInfoBinding.rbFlushOut.getText().toString());
                } else {
                    mCallback.FlushOutReason(mFragmentSignatureInfoBinding.rbComplete.getText().toString());
                }
            }
            if (isFeedBack && accountType.equals("Individual")) {
                mFragmentSignatureInfoBinding.lnrOtp.setVisibility(View.VISIBLE);
                if (mFragmentSignatureInfoBinding.txtFeedback.getText().toString().length() > 0) {
                    OTP = mFragmentSignatureInfoBinding.txtFeedback.getText().toString().trim();
                }
                String sc_otp = mGeneralRealmData.get(0) != null ? mGeneralRealmData.get(0).getSc_OTP() : "";
                String customer_otp = mGeneralRealmData.get(0) != null ? mGeneralRealmData.get(0).getCustomer_OTP() : "";
//                if (status.equals("Completed")) {
//                    mFragmentSignatureInfoBinding.lnrAdd.setVisibility(View.GONE);
//                } else {
//                    mFragmentSignatureInfoBinding.lnrAdd.setVisibility(View.VISIBLE);
//                }
                if (status.equals("Completed") || status.equals("Incomplete")) {
                    mFragmentSignatureInfoBinding.txtFeedback.setEnabled(false);
                    mFragmentSignatureInfoBinding.lnrBottom.setVisibility(View.GONE);
                } else {
                    mFragmentSignatureInfoBinding.txtFeedback.setEnabled(true);
                    mFragmentSignatureInfoBinding.lnrBottom.setVisibility(View.VISIBLE);
                    if (OTP.length() != 0) {
                        mCallback.isOTPRequired(false);
                        if (OTP.equals(sc_otp) || OTP.equals(customer_otp)) {
                            mCallback.feedbackCode(OTP);
                            mCallback.isOTPValidated(false);
                        } else {
                            mCallback.isOTPValidated(true);
                        }
                    } else {
                        mCallback.isOTPRequired(true);
                    }
                    if (mGeneralRealmData.get(0).getShowSignature() != null && mGeneralRealmData.get(0).getShowSignature()) {
                        if (mFragmentSignatureInfoBinding.edtSignatory.getText().toString().length() == 0) {
                            mCallback.isSignatureChanged(true);
                        } else {
                            mCallback.isSignatureChanged(false);
                        }
                        if (mFragmentSignatureInfoBinding.imgSign.getDrawable() == null) {
                            mCallback.isSignatureValidated(true);
                        } else {
                            mCallback.isSignatureValidated(false);
                        }
                    } else {
                        mCallback.isSignatureValidated(false);
                        mCallback.isSignatureChanged(false);
                    }

                }
            } else {
                mFragmentSignatureInfoBinding.txtFeedback.setEnabled(false);
                mFragmentSignatureInfoBinding.lnrOtp.setVisibility(GONE);
                mFragmentSignatureInfoBinding.lnrBottom.setVisibility(View.GONE);
                if (mGeneralRealmData.get(0).getShowSignature() != null && mGeneralRealmData.get(0).getShowSignature()) {
                    if (mFragmentSignatureInfoBinding.edtSignatory.getText().toString().length() == 0) {
                        mCallback.isSignatureChanged(true);
                    } else {
                        mCallback.isSignatureChanged(false);
                    }
                    if (mFragmentSignatureInfoBinding.imgSign.getDrawable() == null) {
                        mCallback.isSignatureValidated(true);
                    } else {
                        mCallback.isSignatureValidated(false);
                    }
                } else {
                    mCallback.isSignatureChanged(false);
                    mCallback.isSignatureValidated(false);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDeleteItemClicked(int position) {
        getJobCardDeleted(position);
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {

    }

    @Override
    public void onDataReceived(String amount, String type) {

        if (type.equalsIgnoreCase("upi") || type.equalsIgnoreCase("paytm") || type.equalsIgnoreCase("amazon pay")) {
            mFragmentSignatureInfoBinding.txtType.setText(type);
            mFragmentSignatureInfoBinding.lnrOrder.setVisibility(View.GONE);
            mFragmentSignatureInfoBinding.lnrType.setVisibility(View.VISIBLE);
        } else if (type.equalsIgnoreCase("none")) {
            mFragmentSignatureInfoBinding.lnrType.setVisibility(View.GONE);
            mFragmentSignatureInfoBinding.lnrOrder.setVisibility(View.GONE);
        } else if (type.equalsIgnoreCase("cash") || type.equalsIgnoreCase("cheque")) {
            if (amount.equals("0") || amount.equals("")) {
                mFragmentSignatureInfoBinding.lnrType.setVisibility(View.GONE);
                mFragmentSignatureInfoBinding.lnrOrder.setVisibility(View.GONE);
            } else {
                mFragmentSignatureInfoBinding.txtAmount.setText("₹" + " " + amount);
                mFragmentSignatureInfoBinding.txtType.setText(type);
                mFragmentSignatureInfoBinding.lnrType.setVisibility(View.VISIBLE);
                mFragmentSignatureInfoBinding.lnrOrder.setVisibility(View.VISIBLE);
            }
        }
    }

    public class DrawingView extends View {

        public int width;
        public int height;
        private Bitmap mBitmap;
        private Canvas mCanvas;
        private Path mPath;
        private Paint mBitmapPaint;
        Context context;
        private Paint circlePaint;
        private Path circlePath;
        AppCompatTextView txt_hint;
        AppCompatImageView img_right;

        public DrawingView(Context c, AppCompatTextView txt_hint, AppCompatImageView img_right) {
            super(c);
            context = c;
            this.txt_hint = txt_hint;
            this.img_right = img_right;
            mPath = new Path();
            mBitmapPaint = new Paint(Paint.DITHER_FLAG);
            circlePaint = new Paint();
            circlePath = new Path();
            circlePaint.setAntiAlias(true);
            circlePaint.setColor(Color.BLACK);
            circlePaint.setStyle(Paint.Style.STROKE);
            circlePaint.setStrokeJoin(Paint.Join.MITER);
            circlePaint.setStrokeWidth(2f);
            img_right.setEnabled(false);
            txt_hint.setVisibility(VISIBLE);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
            canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
            canvas.drawPath(mPath, mPaint);
            canvas.drawPath(circlePath, circlePaint);
        }

        private float mX, mY;
        private static final float TOUCH_TOLERANCE = 4;

        private void touch_start(float x, float y) {
            mPath.reset();
            mPath.moveTo(x, y);
            mX = x;
            mY = y;
        }

        private void touch_move(float x, float y) {
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);
            if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
                mX = x;
                mY = y;

                circlePath.reset();
                circlePath.addCircle(mX, mY, 30, Path.Direction.CW);
            }
        }

        private void touch_up() {
            mPath.lineTo(mX, mY);
            circlePath.reset();
            // commit the path to our offscreen
            mCanvas.drawPath(mPath, mPaint);
            // kill this so we don't double draw
            mPath.reset();
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touch_start(x, y);
                    txt_hint.setVisibility(GONE);
                    img_right.setEnabled(true);
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    touch_move(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    touch_up();
                    invalidate();
                    break;
            }
            return true;
        }

    }

    private void getAddJobCard() {
        try {
            RealmResults<GeneralData> mGeneralRealmData =
                    getRealm().where(GeneralData.class).findAll();
            if (mGeneralRealmData != null && mGeneralRealmData.size() > 0) {

                LayoutInflater li = LayoutInflater.from(getActivity());

                View promptsView = li.inflate(R.layout.layout_choose_gallery_dialog, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

                alertDialogBuilder.setView(promptsView);
                final AlertDialog alertDialog = alertDialogBuilder.create();

                final ImageView selectedImg =
                        promptsView.findViewById(R.id.selectedImg);
                final LinearLayout lnrCamera =
                        promptsView.findViewById(R.id.lnrCamera);
                final LinearLayout lnrGallery =
                        promptsView.findViewById(R.id.lnrGallery);
                final TextView btn_cancel =
                        promptsView.findViewById(R.id.btnCancel);
                final LinearLayout cardSelected = promptsView.findViewById(R.id.cardSelected);

                int[] attrs = new int[]{R.attr.selectableItemBackground};
                TypedArray typedArray = getActivity().obtainStyledAttributes(attrs);
                int backgroundResource = typedArray.getResourceId(0, 0);
                lnrCamera.setBackgroundResource(backgroundResource);
                lnrGallery.setBackgroundResource(backgroundResource);

                if (selectedBmp != null) {
                    cardSelected.setVisibility(View.VISIBLE);
                    Glide.with(getActivity())
                            .load(selectedBmp)
                            .error(android.R.drawable.stat_notify_error)
                            .into(selectedImg);
                } else {
                    cardSelected.setVisibility(GONE);
                }

                cardSelected.setOnClickListener(view -> {
                    if (selectedBmp != null) {
                        alertDialog.dismiss();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        selectedBmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] b = baos.toByteArray();
                        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                        getUploadJobCard(encodedImage);
                    }
                });
                lnrCamera.setOnClickListener(view -> {
                    requestStoragePermission(true);
                    alertDialog.dismiss();
                });

                lnrGallery.setOnClickListener(view -> {
                    requestStoragePermission(false);
                    alertDialog.dismiss();
                });

                btn_cancel.setOnClickListener(v -> alertDialog.dismiss());
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void dispatchGalleryIntent() {
        try {
            Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(pickPhoto, REQUEST_GALLERY_PHOTO);
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
                                } else {
                                    dispatchGalleryIntent();
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
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
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
                    images.add(mPhotoFile.getPath());
                    imgFile = new File(mPhotoFile.getPath());
                    selectedImagePath = mPhotoFile.getPath();
                    if (selectedImagePath != null) {
                        Bitmap bit = new BitmapDrawable(getActivity().getResources(),
                                selectedImagePath).getBitmap();
                        int i = (int) (bit.getHeight() * (1024.0 / bit.getWidth()));
                        bitmap = Bitmap.createScaledBitmap(bit, 1024, i, true);
                        selectedBmp = bitmap;
                    }
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] b = baos.toByteArray();
                    String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                    getUploadJobCard(encodedImage);

                } else if (requestCode == REQUEST_GALLERY_PHOTO) {
                    Uri selectedImage = data.getData();
                    mPhotoFile = new File(getRealPathFromUri(selectedImage));
                    selectedImagePath = mPhotoFile.getPath();
                    if (selectedImagePath != null) {
                        Bitmap bit = new BitmapDrawable(getActivity().getResources(),
                                selectedImagePath).getBitmap();
                        int i = (int) (bit.getHeight() * (1024.0 / bit.getWidth()));
                        bitmap = Bitmap.createScaledBitmap(bit, 1024, i, true);
                        selectedBmp = bitmap;
                    }
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] b = baos.toByteArray();
                    String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                    getUploadJobCard(encodedImage);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get real file path from URI
     */
    private String getRealPathFromUri(Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
            assert cursor != null;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void getUploadJobCard(String encodedImage) {
        try {
            RealmResults<LoginResponse> LoginRealmModels =
                    BaseApplication.getRealm().where(LoginResponse.class).findAll();
            if (LoginRealmModels != null && LoginRealmModels.size() > 0) {
                UserId = LoginRealmModels.get(0).getUserID();
                NetworkCallController controller = new NetworkCallController(this);
                PostAttachmentRequest request = new PostAttachmentRequest();
                request.setFile(encodedImage);
                request.setResourceId(UserId);
                request.setTaskId(taskId);
                controller.setListner(new NetworkResponseListner() {
                    @Override
                    public void onResponse(int requestCode, Object response) {
                        PostAttachmentResponse postResponse = (PostAttachmentResponse) response;
                        if (postResponse.getSuccess()) {
                            Toasty.success(getActivity(), getString(R.string.jobcard_uploaded_successfully), Toast.LENGTH_LONG).show();
                            getAttachmentList();
                        } else {
                            Toast.makeText(getActivity(), getString(R.string.uploading_failed), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(int requestCode) {
                    }
                });

                controller.postAttachments(POST_ATTACHMENT_REQ, request);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getReferral(){
        if ((NewTaskDetailsActivity) getActivity() != null) {
            mGeneralRealmData = getRealm().where(GeneralData.class).findAll();
            if (mGeneralRealmData != null && mGeneralRealmData.size() > 0) {
                NetworkCallController controller = new NetworkCallController(this);
                controller.setListner(new NetworkResponseListner<BaseResponse>() {
                    @Override
                    public void onResponse(int requestCode, BaseResponse response) {
                        if(response.isSuccess()){
                            Toasty.success(requireContext(), "Sent successfully.", Toasty.LENGTH_SHORT).show();
                        }else {
                            Toasty.error(requireContext(), "Please try again!", Toasty.LENGTH_SHORT);
                        }
                    }

                    @Override
                    public void onFailure(int requestCode) {

                    }
                });
                controller.sendReferralMessage(1611, mGeneralRealmData.get(0).getResourceId(), mGeneralRealmData.get(0).getTaskId() );
            }
        }
    }
}
