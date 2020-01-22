package com.ab.hicarerun.fragments;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ab.hicarerun.BaseApplication;
import com.ab.hicarerun.BaseFragment;
import com.ab.hicarerun.R;
import com.ab.hicarerun.adapter.NewAttachmentListAdapter;
import com.ab.hicarerun.databinding.FragmentSignatureInfoBinding;
import com.ab.hicarerun.handler.OnDeleteListItemClickHandler;
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
import com.ab.hicarerun.utils.MyDividerItemDecoration;
import com.ab.hicarerun.utils.SharedPreferencesUtility;
import com.ab.hicarerun.utils.SwipeToDeleteCallBack;
import com.ab.hicarerun.viewmodel.AttachmentListViewModel;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import io.realm.RealmResults;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.view.View.GONE;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignatureInfoFragment extends BaseFragment implements UserSignatureClickHandler, OnDeleteListItemClickHandler {
    FragmentSignatureInfoBinding mFragmentSignatureInfoBinding;
    private static final int POST_FEEDBACK_LINK = 1000;
    private static final int POST_ATTACHMENT_REQ = 2000;
    private static final int GET_ATTACHMENT_REQ = 3000;
    private static final int DELETE_ATTACHMENT_REQ = 4000;
    private static final int COMPLETION_REQUEST = 5000;

    private static final String ARG_TASK = "ARG_TASK";
    private static final String ARG_VAR = "ARG_VAR";
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
    private Tasks model;

    public SignatureInfoFragment() {
        // Required empty public constructor
    }

    public static SignatureInfoFragment newInstance(Tasks taskId) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_TASK, taskId);
        SignatureInfoFragment fragment = new SignatureInfoFragment();
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
        mFragmentSignatureInfoBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_signature_info, container, false);
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
//        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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

        mFragmentSignatureInfoBinding.recycleView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        mFragmentSignatureInfoBinding.recycleView.setLayoutManager(layoutManager);
        mAdapter = new NewAttachmentListAdapter(getActivity(), mGeneralRealmData.get(0).getSchedulingStatus());
        mFragmentSignatureInfoBinding.recycleView.setAdapter(mAdapter);
        mAdapter.setOnItemClickHandler(this);
        getAttachmentList();
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
//                    mFragmentSignatureInfoBinding.btnUpload.setVisibility(GONE);
                    mFragmentSignatureInfoBinding.lnrJobCard.setVisibility(GONE);
                    mFragmentSignatureInfoBinding.imgSign.setVisibility(View.VISIBLE);

                } else if (status.equals("Dispatched")) {
                    mFragmentSignatureInfoBinding.edtSignatory.setBackgroundResource(R.drawable.disable_edit_borders);
                    mFragmentSignatureInfoBinding.imgSign.setVisibility(GONE);
                    mFragmentSignatureInfoBinding.edtSignatory.setEnabled(false);
                    mFragmentSignatureInfoBinding.txtHint.setVisibility(GONE);
                    mFragmentSignatureInfoBinding.txtFeedback.setEnabled(false);
                    mFragmentSignatureInfoBinding.btnSendlink.setVisibility(GONE);
//                    mFragmentSignatureInfoBinding.btnUpload.setVisibility(GONE);
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
//                    mFragmentSignatureInfoBinding.btnUpload.setVisibility(View.VISIBLE);
                    mFragmentSignatureInfoBinding.lnrJobCard.setVisibility(View.VISIBLE);
                } else {
//                    mFragmentSignatureInfoBinding.btnUpload.setVisibility(View.GONE);
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

    public void sendFeedback() {

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
                NetworkCallController controller = new NetworkCallController(SignatureInfoFragment.this);
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
                                    NetworkCallController controller = new NetworkCallController(SignatureInfoFragment.this);
                                    PostAttachmentRequest request = new PostAttachmentRequest();
                                    request.setFile(encodedImage);
                                    request.setResourceId(UserId);
                                    request.setTaskId(model.getTaskId());
                                    controller.setListner(new NetworkResponseListner() {
                                        @Override
                                        public void onResponse(int requestCode, Object response) {
                                            PostAttachmentResponse postResponse = (PostAttachmentResponse) response;
                                            if (postResponse.getSuccess()) {
                                                Toasty.success(getActivity(), "Job card uploaded successfully", Toast.LENGTH_LONG).show();
                                                getAttachmentList();
                                            } else {
                                                Toasty.error(getActivity(), "Posting Failed", Toast.LENGTH_LONG).show();
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
                        List<GetAttachmentList> items = (List<GetAttachmentList>) data;
                        if (items != null) {
                            if (pageNumber == 1 && items.size() > 0) {
                                mFragmentSignatureInfoBinding.txtData.setVisibility(View.GONE);
                                mAdapter.setData(items);
                                mAdapter.notifyDataSetChanged();
                                if (isJobcardEnable)
                                    mCallback.isJobCardEnable(false);
                                enableSwipeToDeleteAndUndo();
                            } else if (items.size() > 0) {
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
                controller.getAttachments(GET_ATTACHMENT_REQ, model.getTaskId(), UserId);
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
                if (status.equals("Completed")) {
                    mFragmentSignatureInfoBinding.lnrAdd.setVisibility(View.GONE);
                } else {
                    mFragmentSignatureInfoBinding.lnrAdd.setVisibility(View.VISIBLE);
                }
                if (status.equals("Completed") || status.equals("Incomplete")) {
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

    @Override
    public void onDeleteItemClicked(int position) {
        getJobCardDeleted(position);
    }

    @Override
    public void onItemClick(int position) {

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
