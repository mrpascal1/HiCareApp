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
import android.graphics.drawable.BitmapDrawable;
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
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.hicarerun.BaseApplication;
import com.ab.hicarerun.BaseFragment;
import com.ab.hicarerun.BuildConfig;
import com.ab.hicarerun.R;
import com.ab.hicarerun.adapter.JobCardMSTAdapter;
import com.ab.hicarerun.adapter.NewAttachmentListAdapter;
import com.ab.hicarerun.databinding.FragmentSignatureMstinfoBinding;
import com.ab.hicarerun.handler.OnAddJobCardClickHandler;
import com.ab.hicarerun.handler.OnJobCardDeleteClickHandler;
import com.ab.hicarerun.handler.OnSaveEventHandler;
import com.ab.hicarerun.handler.UserSignatureClickHandler;
import com.ab.hicarerun.network.NetworkCallController;
import com.ab.hicarerun.network.NetworkResponseListner;
import com.ab.hicarerun.network.models.AttachmentModel.AttachmentDeleteRequest;
import com.ab.hicarerun.network.models.AttachmentModel.GetAttachmentList;
import com.ab.hicarerun.network.models.AttachmentModel.MSTAttachment;
import com.ab.hicarerun.network.models.AttachmentModel.PostAttachmentRequest;
import com.ab.hicarerun.network.models.AttachmentModel.PostAttachmentResponse;
import com.ab.hicarerun.network.models.BasicResponse;
import com.ab.hicarerun.network.models.FeedbackModel.FeedbackRequest;
import com.ab.hicarerun.network.models.FeedbackModel.FeedbackResponse;
import com.ab.hicarerun.network.models.GeneralModel.GeneralData;
import com.ab.hicarerun.network.models.LoginResponse;
import com.ab.hicarerun.network.models.TaskModel.Tasks;
import com.ab.hicarerun.utils.AppUtils;
import com.ab.hicarerun.utils.SharedPreferencesUtility;
import com.ab.hicarerun.utils.SwipeToDeleteCallBack;
import com.bumptech.glide.Glide;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;
import io.realm.RealmResults;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignatureMSTInfoFragment extends BaseFragment implements UserSignatureClickHandler, OnJobCardDeleteClickHandler, OnAddJobCardClickHandler {
    FragmentSignatureMstinfoBinding mFragmentSignatureInfoBinding;
    private static final int POST_FEEDBACK_LINK = 1000;
    private static final int POST_ATTACHMENT_REQ = 2000;
    private static final int GET_ATTACHMENT_REQ = 3000;
    private static final int DELETE_ATTACHMENT_REQ = 4000;
    private static final int COMPLETION_REQUEST = 5000;
    private String jobCardId = "";
    private Bitmap selectedBmp = null;
    private static final String ARG_TASK = "ARG_TASK";
    private static final String ARG_VAR = "ARG_VAR";
    private String status = "";
    static String mFeedback = "";
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
    private ArrayList<GetAttachmentList> attachmentList = null;
    private HashMap<String, List<GetAttachmentList>> hashJob = new HashMap<>();
    private File imgFile;
    private String selectedImagePath = "";
    private Bitmap bitmap;
    private NewAttachmentListAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Integer pageNumber = 1;
    private Tasks model;
    List<String> expandableListTitle;
    List<MSTAttachment> listTitle;
    List<MSTAttachment> mstAttachments;
    HashMap<String, List<GetAttachmentList>> expandableListDetail;
    List<MSTAttachment> items = null;
    JobCardMSTAdapter mJobCardAdapter;
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_GALLERY_PHOTO = 2;
    File mPhotoFile;

    public SignatureMSTInfoFragment() {
        // Required empty public constructor
    }

    public static SignatureMSTInfoFragment newInstance(Tasks taskId) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_TASK, taskId);
        SignatureMSTInfoFragment fragment = new SignatureMSTInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            model = getArguments().getParcelable(ARG_TASK);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getValidate();
        try {
            AppUtils.statusCheck(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFragmentSignatureInfoBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_signature_mstinfo, container, false);
        feedback_code = mFragmentSignatureInfoBinding.txtFeedback.getText().toString();
        signatory = mFragmentSignatureInfoBinding.edtSignatory.getText().toString();
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
        getSignature();
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

        getAttachmentList();

        mFragmentSignatureInfoBinding.expandableListView.setOnGroupClickListener((parent, v, groupPosition, id) -> {
            return true; // This way the expander cannot be collapsed
        });
    }


    private void getJobCardDeleted(int parent, int child) {
        try {
            AttachmentDeleteRequest request = new AttachmentDeleteRequest();
            List<AttachmentDeleteRequest> model = new ArrayList<>();
            request.setId(expandableListDetail.get(expandableListTitle.get(parent)).get(child).getId());
            request.setTaskId(expandableListDetail.get(expandableListTitle.get(parent)).get(child).getTaskId());
            request.setResourceId(expandableListDetail.get(expandableListTitle.get(parent)).get(child).getResourceId());
            request.setCreated_On(expandableListDetail.get(expandableListTitle.get(parent)).get(child).getCreated_On());
            request.setFileName(expandableListDetail.get(expandableListTitle.get(parent)).get(child).getFileName());
            request.setFilePath(expandableListDetail.get(expandableListTitle.get(parent)).get(child).getFilePath());
            request.setFile(expandableListDetail.get(expandableListTitle.get(parent)).get(child).getFile());
            model.add(request);
            NetworkCallController controller = new NetworkCallController(SignatureMSTInfoFragment.this);
            controller.setListner(new NetworkResponseListner() {
                @Override
                public void onResponse(int requestCode, Object data) {
                    PostAttachmentResponse deleteResponse = (PostAttachmentResponse) data;
                    if (deleteResponse.getSuccess()) {
                        getAttachmentList();
                        Toasty.success(getActivity(), "Deleted successfully.", Toasty.LENGTH_SHORT).show();
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
                status = mGeneralRealmData.get(0).getSchedulingStatus();
                if (status.equals("Completed") || status.equals("Incomplete")) {
                    mFragmentSignatureInfoBinding.edtSignatory.setEnabled(false);
                    mFragmentSignatureInfoBinding.edtSignatory.setText(mGeneralRealmData.get(0).getSignatory());
                    mFragmentSignatureInfoBinding.txtFeedback.setText(mGeneralRealmData.get(0).getTechnicianOTP());
                    mFragmentSignatureInfoBinding.txtHint.setVisibility(GONE);
                    mFragmentSignatureInfoBinding.txtFeedback.setEnabled(false);
                    mFragmentSignatureInfoBinding.btnSendlink.setVisibility(GONE);
                    mFragmentSignatureInfoBinding.lnrJobCard.setVisibility(GONE);
                    mFragmentSignatureInfoBinding.imgSign.setVisibility(View.VISIBLE);

                } else if (status.equals("Dispatched")) {
                    mFragmentSignatureInfoBinding.edtSignatory.setBackgroundResource(R.drawable.disable_edit_borders);
                    mFragmentSignatureInfoBinding.imgSign.setVisibility(GONE);
                    mFragmentSignatureInfoBinding.edtSignatory.setEnabled(false);
                    mFragmentSignatureInfoBinding.txtHint.setVisibility(GONE);
                    mFragmentSignatureInfoBinding.txtFeedback.setEnabled(false);
                    mFragmentSignatureInfoBinding.btnSendlink.setVisibility(GONE);
                    mFragmentSignatureInfoBinding.lnrJobCard.setVisibility(GONE);
                } else {
                    mFragmentSignatureInfoBinding.edtSignatory.setEnabled(true);
                    mFragmentSignatureInfoBinding.imgSign.setEnabled(true);
                    mFragmentSignatureInfoBinding.txtHint.setVisibility(View.VISIBLE);
                }
                String amount = mGeneralRealmData.get(0).getAmountToCollect();
                mobile = mGeneralRealmData.get(0).getMobileNumber();
                Order_Number = mGeneralRealmData.get(0).getOrderNumber();
                Service_Name = mGeneralRealmData.get(0).getServicePlan();
                name = mGeneralRealmData.get(0).getCustName();
                code = mGeneralRealmData.get(0).getTechnicianOTP();
                try {
                    if (mGeneralRealmData.get(0).getSignatureUrl() != null || !mGeneralRealmData.get(0).getSignatureUrl().equals("")) {
                        mFragmentSignatureInfoBinding.txtHint.setVisibility(GONE);
                        Glide.with(getActivity())
                                .load(mGeneralRealmData.get(0).getSignatureUrl())
                                .error(android.R.drawable.stat_notify_error)
                                .into(mFragmentSignatureInfoBinding.imgSign);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    mask = mobile.replaceAll("\\w(?=\\w{4})", "*");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Email = mGeneralRealmData.get(0).getEmail();
                mFragmentSignatureInfoBinding.txtAmount.setText(amount + " " + "\u20B9");
                feedback_code = mFragmentSignatureInfoBinding.txtFeedback.getText().toString();
                signatory = mFragmentSignatureInfoBinding.edtSignatory.getText().toString();
                isJobcardEnable = mGeneralRealmData.get(0).getJobCardRequired();
                isFeedBack = mGeneralRealmData.get(0).getFeedBack();
                accountType = mGeneralRealmData.get(0).getAccountType();
                if (isJobcardEnable) {
                    mFragmentSignatureInfoBinding.lnrJobCard.setVisibility(View.VISIBLE);
                } else {
                    mFragmentSignatureInfoBinding.lnrJobCard.setVisibility(View.GONE);
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
        if (mFragmentSignatureInfoBinding.imgSign.getDrawable() == null) {
            LayoutInflater li = LayoutInflater.from(getActivity());
            View promptsView = li.inflate(R.layout.signature_dialog, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
            alertDialogBuilder.setView(promptsView);

            final AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialogBuilder.setTitle("Signature");
            final RelativeLayout lnr_screen =
                    (RelativeLayout) promptsView.findViewById(R.id.lnr_screen);
            final AppCompatImageView img_right =
                    (AppCompatImageView) promptsView.findViewById(R.id.img_right);
            final AppCompatImageView img_wrong =
                    (AppCompatImageView) promptsView.findViewById(R.id.img_wrong);
            final AppCompatButton btn_close =
                    (AppCompatButton) promptsView.findViewById(R.id.btn_close);
            final AppCompatTextView txt_hint =
                    (AppCompatTextView) promptsView.findViewById(R.id.txt_hint);
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
                                    "You are not allowed to send feedback link as you have not spent adequate time. Please follow the correct procedure and deliver the job properly",
                                    Toasty.LENGTH_LONG).show();
                            getValidate();
                        }
                    }

                    @Override
                    public void onFailure(int requestCode) {
                    }
                });
                controller.getValidateCompletionTime(COMPLETION_REQUEST, mGeneralRealmData.get(0).getActualCompletionDateTime(), model.getTaskId());
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
                request.setTask_id(model.getTaskId());
                request.setFeedback_code(customer_otp);
                request.setOrder_number(Order_Number);
                request.setService_name(Service_Name);
                NetworkCallController controller = new NetworkCallController(SignatureMSTInfoFragment.this);
                controller.setListner(new NetworkResponseListner() {
                    @Override
                    public void onResponse(int requestCode, Object response) {
                        FeedbackResponse refResponse = (FeedbackResponse) response;
                        if (refResponse.getSuccess()) {
                            Toasty.success(getActivity(), "Feedback link sent successfully.", Toasty.LENGTH_LONG).show();
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
        try {
            RealmResults<GeneralData> mGeneralRealmData =
                    getRealm().where(GeneralData.class).findAll();
            if (mGeneralRealmData != null && mGeneralRealmData.size() > 0) {
                isJobcardEnable = mGeneralRealmData.get(0).getJobCardRequired();
                if (isJobcardEnable) {
                    PickImageDialog.build(new PickSetup()).setOnPickResult(pickResult -> {
                        if (pickResult.getError() == null) {
                            images.add(pickResult.getPath());
                            imgFile = new File(pickResult.getPath());
                            selectedImagePath = pickResult.getPath();
                            if (selectedImagePath != null) {
                                Bitmap bit = new BitmapDrawable(getActivity().getResources(),
                                        selectedImagePath).getBitmap();
                                int i = (int) (bit.getHeight() * (1024.0 / bit.getWidth()));
                                bitmap = Bitmap.createScaledBitmap(bit, 1024, i, true);
                            }
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            byte[] b = baos.toByteArray();
                            String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                            RealmResults<LoginResponse> LoginRealmModels =
                                    BaseApplication.getRealm().where(LoginResponse.class).findAll();
                            if (pickResult.getPath() != null) {
                                if (LoginRealmModels != null && LoginRealmModels.size() > 0) {
                                    UserId = LoginRealmModels.get(0).getUserID();
                                    NetworkCallController controller = new NetworkCallController(SignatureMSTInfoFragment.this);
                                    PostAttachmentRequest request = new PostAttachmentRequest();
                                    request.setFile(encodedImage);
                                    request.setResourceId(UserId);
                                    request.setTaskId(model.getTaskId());
                                    controller.setListner(new NetworkResponseListner() {
                                        @Override
                                        public void onResponse(int requestCode, Object response) {
                                            PostAttachmentResponse postResponse = (PostAttachmentResponse) response;
                                            if (postResponse.getSuccess()) {
                                                Toasty.success(getActivity(), "Job card uploaded successfully.", Toast.LENGTH_LONG).show();
                                                getAttachmentList();
                                            } else {
                                                Toast.makeText(getActivity(), "Posting Failed.", Toast.LENGTH_LONG).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(int requestCode) {
                                        }
                                    });

                                    controller.postAttachments(POST_ATTACHMENT_REQ, request);
                                }
                            }
                        }
                    }).show(getActivity());

                } else {
                    Toast.makeText(getActivity(), "Disable", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
            if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                AppUtils.sendErrorLogs(e.getMessage(), getClass().getSimpleName(), "onAddImageClicked", lineNo, userName, DeviceName);
            }
        }
    }

    private void getAttachmentList() {
        try {
            RealmResults<LoginResponse> LoginRealmModels =
                    BaseApplication.getRealm().where(LoginResponse.class).findAll();
            if (LoginRealmModels != null && LoginRealmModels.size() > 0) {
                UserId = LoginRealmModels.get(0).getUserID();
                NetworkCallController controller = new NetworkCallController(this);
                controller.setListner(new NetworkResponseListner() {
                    @Override
                    public void onResponse(int requestCode, Object data) {
                        items = (List<MSTAttachment>) data;
                        expandableListDetail = new HashMap<>();
                        expandableListTitle = new ArrayList<>(expandableListDetail.keySet());
                        listTitle = new ArrayList<>();
                        mstAttachments = new ArrayList<>();
                        if (isJobcardEnable) {
                            if (isListContainJobCard(items)) {
                                mCallback.isJobCardEnable(false);
                            } else {
                                mCallback.isJobCardEnable(true);
                            }
                        } else {
                            mCallback.isJobCardEnable(false);
                        }


                        if (items.size() > 0) {
                            for (int i = 0; i < items.size(); i++) {
                                MSTAttachment request = new MSTAttachment();
                                request.setTaskType(items.get(i).getTaskType());
                                request.setTaskNo(items.get(i).getTaskNo());
                                request.setAttachmentList(items.get(i).getAttachmentList());
                                listTitle.add(request);
                                expandableListTitle.add(items.get(i).getTaskNo());
                                expandableListDetail.put(items.get(i).getTaskNo(), items.get(i).getAttachmentList());
                                hashJob.put(items.get(i).getTaskNo(), items.get(i).getAttachmentList());
                                mstAttachments = items;

//                                SubItems = expandableListDetail.get(expandableListTitle);
                            }
                            mJobCardAdapter = new JobCardMSTAdapter(getActivity(), expandableListTitle, expandableListDetail, mFragmentSignatureInfoBinding.expandableListView, status, listTitle);
                            mFragmentSignatureInfoBinding.expandableListView.setAdapter(mJobCardAdapter);
                            mJobCardAdapter.setOnItemClickHandler(SignatureMSTInfoFragment.this);
                            mJobCardAdapter.setOnDeleteItemClickHandler(SignatureMSTInfoFragment.this);
                            for (int i = 0; i < mFragmentSignatureInfoBinding.expandableListView.getExpandableListAdapter().getGroupCount(); i++) {
                                //Expand group
                                mFragmentSignatureInfoBinding.expandableListView.expandGroup(i);
                            }
                            getValidate();
                        }
                    }

                    @Override
                    public void onFailure(int requestCode) {

                    }
                });
                controller.getMSTAttachments(GET_ATTACHMENT_REQ, UserId, model.getCombinedTaskId(), model.getCombinedServiceType());
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
            if (isFeedBack && accountType.equals("Individual")) {
                mFragmentSignatureInfoBinding.lnrOtp.setVisibility(View.VISIBLE);
                String otp = mFragmentSignatureInfoBinding.txtFeedback.getText().toString().trim();
                String sc_otp = mGeneralRealmData.get(0).getSc_OTP();
                String customer_otp = mGeneralRealmData.get(0).getCustomer_OTP();
                if (status.equals("Completed") || status.equals("Incomplete")) {
//                mCallback.isFeedbackRequired(false);
                    mFragmentSignatureInfoBinding.txtFeedback.setEnabled(false);
                    mFragmentSignatureInfoBinding.btnSendlink.setVisibility(View.GONE);
                } else {
                    mFragmentSignatureInfoBinding.txtFeedback.setEnabled(true);
                    mFragmentSignatureInfoBinding.btnSendlink.setVisibility(View.VISIBLE);
                    if (otp.length() != 0) {
                        mCallback.isOTPRequired(false);
                        if (otp.equals(sc_otp) || otp.equals(customer_otp)) {
                            mCallback.feedbackCode(otp);
                            mCallback.isOTPValidated(false);
                        } else {
                            mCallback.isOTPValidated(true);
                        }
                    } else {
                        mCallback.isOTPRequired(true);
                    }

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
                }
            } else {
                mFragmentSignatureInfoBinding.txtFeedback.setEnabled(false);
                mFragmentSignatureInfoBinding.lnrOtp.setVisibility(GONE);
                mFragmentSignatureInfoBinding.btnSendlink.setVisibility(View.GONE);
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
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isListContainJobCard(List<MSTAttachment> arraylist) {
        for (MSTAttachment str : arraylist) {
            if (str.getAttachmentList().size() == 0) {
                return false;
            }
        }
        return true;
    }


    @Override
    public void onAddJobCardClicked(int parent) {
        getAddJobCard(parent);
    }

    private void getAddJobCard(int parent) {
        try {
            jobCardId = "";
            RealmResults<GeneralData> mGeneralRealmData =
                    getRealm().where(GeneralData.class).findAll();
            if (mGeneralRealmData != null && mGeneralRealmData.size() > 0) {
                jobCardId = mstAttachments.get(parent).getTaskNo();

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
        }catch (Exception e){
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
        }catch (Exception e){
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
        }catch (Exception e){
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
        }catch (Exception e){
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
        }catch (Exception e){
            e.printStackTrace();
        }

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

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getUploadJobCard(String encodedImage) {
        try {
            RealmResults<LoginResponse> LoginRealmModels =
                    BaseApplication.getRealm().where(LoginResponse.class).findAll();
            if (LoginRealmModels != null && LoginRealmModels.size() > 0) {
                UserId = LoginRealmModels.get(0).getUserID();
                NetworkCallController controller = new NetworkCallController(SignatureMSTInfoFragment.this);
                PostAttachmentRequest request = new PostAttachmentRequest();
                request.setFile(encodedImage);
                request.setResourceId(UserId);
                request.setTaskId(jobCardId);
                controller.setListner(new NetworkResponseListner() {
                    @Override
                    public void onResponse(int requestCode, Object response) {
                        PostAttachmentResponse postResponse = (PostAttachmentResponse) response;
                        if (postResponse.getSuccess()) {
                            Toasty.success(getActivity(), "Job card uploaded successfully.", Toast.LENGTH_LONG).show();
                            getAttachmentList();
                        } else {
                            Toast.makeText(getActivity(), "Posting Failed.", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(int requestCode) {
                    }
                });

                controller.postAttachments(POST_ATTACHMENT_REQ, request);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    @Override
    public void onItemClick(int parent, int child) {

    }

    @Override
    public void onDeleteJobCard(int parent, int child) {
        getJobCardDeleted(parent, child);
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
}