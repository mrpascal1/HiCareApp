package com.ab.hicarerun.fragments;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ab.hicarerun.BaseApplication;
import com.ab.hicarerun.BaseFragment;
import com.ab.hicarerun.R;
import com.ab.hicarerun.adapter.AttachmentListAdapter;
import com.ab.hicarerun.adapter.TaskListAdapter;
import com.ab.hicarerun.databinding.FragmentAttachmentBinding;
import com.ab.hicarerun.handler.OnJobCardEventHandler;
import com.ab.hicarerun.handler.OnSaveEventHandler;
import com.ab.hicarerun.handler.UserAttachmentClickHandler;
import com.ab.hicarerun.network.NetworkCallController;
import com.ab.hicarerun.network.NetworkResponseListner;
import com.ab.hicarerun.network.models.AttachmentModel.AttachmentDeleteRequest;
import com.ab.hicarerun.network.models.AttachmentModel.GetAttachmentList;
import com.ab.hicarerun.network.models.AttachmentModel.PostAttachmentRequest;
import com.ab.hicarerun.network.models.AttachmentModel.PostAttachmentResponse;
import com.ab.hicarerun.network.models.GeneralModel.GeneralData;
import com.ab.hicarerun.network.models.LoginResponse;
import com.ab.hicarerun.utils.AppUtils;
import com.ab.hicarerun.utils.SharedPreferencesUtility;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import io.realm.RealmResults;

import static android.view.View.GONE;

public class AttachmentFragment extends BaseFragment implements UserAttachmentClickHandler, NetworkResponseListner {
    FragmentAttachmentBinding mFragmentAttachmentBinding;
    public static final String ARGS_TASKS = "ARGS_TASKS";
    private static final int POST_ATTACHMENT_REQ = 2000;
    private static final int GET_ATTACHMENT_REQ = 3000;
    private static final int DELETE_ATTACHMENT_REQ = 4000;
    private String UserId = "";
    private String taskId = "";
    private File imgFile;
    private ArrayList<String> images = new ArrayList<>();
    private Integer pageNumber = 1;
    AttachmentListAdapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    private boolean isCardRequired = false;
    private OnJobCardEventHandler mCallback;
    private String selectedImagePath = "";
    private Bitmap bitmap;


    public AttachmentFragment() {
        // Required empty public constructor
    }

    public static AttachmentFragment newInstance(String taskId) {
        Bundle args = new Bundle();
        args.putString(ARGS_TASKS, taskId);
        AttachmentFragment fragment = new AttachmentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            taskId = getArguments().getString(ARGS_TASKS);
        }
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnJobCardEventHandler) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement FragmentToActivity");
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentAttachmentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_attachment, container, false);
        mFragmentAttachmentBinding.setHandler(this);
        return mFragmentAttachmentBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Upload Job Card");
        mFragmentAttachmentBinding.recycleView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        mFragmentAttachmentBinding.recycleView.setLayoutManager(layoutManager);
        mAdapter = new AttachmentListAdapter(getActivity(), mFragmentAttachmentBinding.imgSelect, mFragmentAttachmentBinding.txtDelcount);
        mFragmentAttachmentBinding.recycleView.setAdapter(mAdapter);
        getAttachmentList();

    }


    private void getAttachmentList() {
        try {
            RealmResults<LoginResponse> LoginRealmModels =
                    BaseApplication.getRealm().where(LoginResponse.class).findAll();
            if (LoginRealmModels != null && LoginRealmModels.size() > 0) {
                assert LoginRealmModels.get(0) != null;
                UserId = LoginRealmModels.get(0).getUserID();
                NetworkCallController controller = new NetworkCallController(this);
                controller.setListner(this);
                controller.getAttachments(GET_ATTACHMENT_REQ, taskId, UserId);
            }
        } catch (Exception e) {
            RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
            if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                assert mLoginRealmModels.get(0) != null;
                String userName = "TECHNICIAN NAME : "+mLoginRealmModels.get(0).getUserName();
                String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                String DeviceName = "DEVICE_NAME : "+ Build.DEVICE+", DEVICE_VERSION : "+ Build.VERSION.SDK_INT;
                AppUtils.sendErrorLogs(e.getMessage(), getClass().getSimpleName(), "getAttachmentList", lineNo,userName,DeviceName);
            }
        }

    }

    @Override
    public void onAddImageClicked(View view) {
        try {
            RealmResults<GeneralData> mGeneralRealmData =
                    getRealm().where(GeneralData.class).findAll();

            if (mGeneralRealmData != null && mGeneralRealmData.size() > 0) {
                assert mGeneralRealmData.get(0) != null;
                isCardRequired = mGeneralRealmData.get(0).getJobCardRequired();
                if (isCardRequired) {
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
                                    assert LoginRealmModels.get(0) != null;
                                    UserId = LoginRealmModels.get(0).getUserID();
                                    NetworkCallController controller = new NetworkCallController(AttachmentFragment.this);
                                    PostAttachmentRequest request = new PostAttachmentRequest();
                                    request.setFile(encodedImage);
                                    request.setResourceId(UserId);
                                    request.setTaskId(taskId);
                                    controller.setListner(new NetworkResponseListner() {
                                        @Override
                                        public void onResponse(int requestCode, Object response) {
                                            PostAttachmentResponse postResponse = (PostAttachmentResponse) response;
                                            if (postResponse.getSuccess()) {
//                                                    Toast.makeText(getActivity(), "Post Successfully.", Toast.LENGTH_LONG).show();
                                                Toasty.success(Objects.requireNonNull(getActivity()), "Job card uploaded successfully.", Toast.LENGTH_LONG).show();
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
                    }).show(Objects.requireNonNull(getActivity()));

                } else {
                    mCallback.isJobCardEnable(false);
                    Toast.makeText(getActivity(), "Disable", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
            if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                assert mLoginRealmModels.get(0) != null;
                String userName = "TECHNICIAN NAME : "+mLoginRealmModels.get(0).getUserName();
                String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                String DeviceName = "DEVICE_NAME : "+ Build.DEVICE+", DEVICE_VERSION : "+ Build.VERSION.SDK_INT;
                AppUtils.sendErrorLogs(e.getMessage(), getClass().getSimpleName(), "onAddImageClicked", lineNo,userName,DeviceName);
            }
        }

    }

    @Override
    public void onSelectImageClicked(View view) {
    }

    @Override
    public void onDeleteImageClicked(View view) {
        try {
            AttachmentDeleteRequest request;
            List<AttachmentDeleteRequest> model = null;
            model = new ArrayList<>();
            int count = mAdapter.getItemCount();
            for (int i = 0; i < count; i++) {
                if (mAdapter.getItem(i).getChecked()) {
                    request = new AttachmentDeleteRequest();
                    try {
                        request.setId(mAdapter.getItem(i).getId());
                        request.setTaskId(mAdapter.getItem(i).getTaskId());
                        request.setResourceId(mAdapter.getItem(i).getResourceId());
                        request.setCreated_On(mAdapter.getItem(i).getCreated_On());
                        request.setFileName(mAdapter.getItem(i).getFileName());
                        request.setFilePath(mAdapter.getItem(i).getFilePath());
                        request.setFile(mAdapter.getItem(i).getFile());
                        model.add(request);
                    } catch (Exception e) {
                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                            String userName = "TECHNICIAN NAME : "+mLoginRealmModels.get(0).getUserName();
                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                            String DeviceName = "DEVICE_NAME : "+ Build.DEVICE+", DEVICE_VERSION : "+ Build.VERSION.SDK_INT;
                            AppUtils.sendErrorLogs(e.getMessage(), getClass().getSimpleName(), "onDeleteImageClicked", lineNo,userName,DeviceName);
                        }
                    }
                }
            }
            NetworkCallController controller = new NetworkCallController(AttachmentFragment.this);
            controller.setListner(this);
            controller.getDeleteAttachments(DELETE_ATTACHMENT_REQ, model);
        }catch (Exception e){
            RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
            if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                assert mLoginRealmModels.get(0) != null;
                String userName = "TECHNICIAN NAME : "+mLoginRealmModels.get(0).getUserName();
                String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                String DeviceName = "DEVICE_NAME : "+ Build.DEVICE+", DEVICE_VERSION : "+ Build.VERSION.SDK_INT;
                AppUtils.sendErrorLogs(e.getMessage(), getClass().getSimpleName(), "onDeleteImageClicked", lineNo,userName,DeviceName);
            }
        }

    }

    @Override
    public void onResponse(int requestCode, Object data) {
        switch (requestCode) {
            case GET_ATTACHMENT_REQ:
                List<GetAttachmentList> items = (List<GetAttachmentList>) data;
                if (items != null) {
                    if (pageNumber == 1 && items.size() > 0) {
                        mFragmentAttachmentBinding.txtData.setVisibility(View.GONE);
                        mAdapter.setData(items);
                        mAdapter.notifyDataSetChanged();
                        mFragmentAttachmentBinding.imgSelect.setEnabled(true);
                        if (isCardRequired) {
                            mCallback.isJobCardEnable(true);
                        } else {
                            mCallback.isJobCardEnable(false);
                        }
                        mCallback.AttachmentList(items);
                    } else if (items.size() > 0) {
                        mFragmentAttachmentBinding.txtData.setVisibility(View.GONE);
                        mAdapter.addData(items);
                        mAdapter.notifyDataSetChanged();
                        mFragmentAttachmentBinding.imgSelect.setEnabled(true);
                        if (isCardRequired) {
                            mCallback.isJobCardEnable(true);

                        } else {
                            mCallback.isJobCardEnable(false);
                        }
                        mCallback.AttachmentList(items);
                    } else {
                        pageNumber--;
                    }
                } else {
                    mFragmentAttachmentBinding.txtData.setVisibility(View.VISIBLE);
                    mFragmentAttachmentBinding.imgSelect.setEnabled(false);
                    mCallback.isJobCardEnable(false);
                    mCallback.AttachmentList(items);
                }

                break;

            case DELETE_ATTACHMENT_REQ:
                PostAttachmentResponse deleteResponse = (PostAttachmentResponse) data;

                if (deleteResponse.getSuccess()) {
                    mAdapter.removeAll();
                    if (mAdapter.getItemCount() == 0) {
                        mFragmentAttachmentBinding.txtData.setVisibility(View.VISIBLE);
                    } else {
                        mFragmentAttachmentBinding.txtData.setVisibility(GONE);
                    }


                    for (int i = 0; i < mAdapter.getItemCount(); i++) {
                        if (mAdapter.getItem(i).getChecked()) {
                            mAdapter.getItem(i).setChecked(false);
                        }
                    }

                    getAttachmentList();
                    Toasty.success(getActivity(), "Deleted successfully.", Toasty.LENGTH_SHORT).show();
                    mAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity(), "Failed.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }

    @Override
    public void onFailure(int requestCode) {

    }
}
