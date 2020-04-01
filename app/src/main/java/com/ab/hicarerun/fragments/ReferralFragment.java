package com.ab.hicarerun.fragments;


import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ab.hicarerun.BaseApplication;
import com.ab.hicarerun.BaseFragment;
import com.ab.hicarerun.R;
import com.ab.hicarerun.activities.NewTaskDetailsActivity;
import com.ab.hicarerun.adapter.ReferralListAdapter;
import com.ab.hicarerun.databinding.FragmentReferralBinding;
import com.ab.hicarerun.handler.OnDeleteListItemClickHandler;
import com.ab.hicarerun.handler.UserReferralClickHandler;
import com.ab.hicarerun.network.NetworkCallController;
import com.ab.hicarerun.network.NetworkResponseListner;
import com.ab.hicarerun.network.models.GeneralModel.GeneralData;
import com.ab.hicarerun.network.models.LoginResponse;
import com.ab.hicarerun.network.models.ReferralModel.ReferralDeleteRequest;
import com.ab.hicarerun.network.models.ReferralModel.ReferralList;
import com.ab.hicarerun.network.models.ReferralModel.ReferralRequest;
import com.ab.hicarerun.network.models.ReferralModel.ReferralResponse;
import com.ab.hicarerun.utils.AppUtils;
import com.ab.hicarerun.utils.SwipeToDeleteCallBack;
import com.ab.hicarerun.viewmodel.ReferralViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import es.dmoral.toasty.Toasty;
import io.realm.RealmResults;

public class ReferralFragment extends BaseFragment implements UserReferralClickHandler, OnDeleteListItemClickHandler, NetworkResponseListner {

    FragmentReferralBinding mfragmentReferralBinding;
    private static final int POST_REFERRAL_REQUEST = 1000;
    private static final int GET_REFERRAL_REQUEST = 2000;
    private static final int DELETE_REFERRAL_REQUEST = 3000;
    private static final String ARG_TASK = "ARG_TASK";
    private static final String ARGS_MOBILE = "ARGS_MOBILE";
    private String taskId = "";
    private String technicianMobileNo = "";
    ReferralListAdapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    private Integer pageNumber = 1;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    RealmResults<GeneralData> mGeneralRealmModel;

    public ReferralFragment() {
        // Required empty public constructor
    }

    public static ReferralFragment newInstance(String taskId, String technicianMobileNo) {
        Bundle args = new Bundle();
        args.putString(ARG_TASK, taskId);
        args.putString(ARGS_MOBILE, technicianMobileNo);
        ReferralFragment fragment = new ReferralFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            taskId = getArguments().getString(ARG_TASK);
            technicianMobileNo = getArguments().getString(ARGS_MOBILE);
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mfragmentReferralBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_referral, container, false);
        mfragmentReferralBinding.setModel(new ReferralViewModel());
        mfragmentReferralBinding.setHandler(this);
        return mfragmentReferralBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        getActivity().setTitle("Customer Referrals");
        mfragmentReferralBinding.swipeRefreshLayout.setOnRefreshListener(
                this::getReferralList);
        mfragmentReferralBinding.recycleView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        mfragmentReferralBinding.recycleView.setLayoutManager(layoutManager);
        mfragmentReferralBinding.swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_dark, android.R.color.holo_blue_light,
                android.R.color.holo_red_dark, android.R.color.holo_red_light,
                android.R.color.holo_green_dark, android.R.color.holo_green_light,
                android.R.color.holo_red_dark, android.R.color.holo_red_light);
        mAdapter = new ReferralListAdapter(getActivity());
        mAdapter.setOnItemClickHandler(this);
        mfragmentReferralBinding.recycleView.setAdapter(mAdapter);
        getReferralList();
        mfragmentReferralBinding.swipeRefreshLayout.setRefreshing(true);

    }

    private void enableSwipeToDeleteAndUndo() {
        try {
            SwipeToDeleteCallBack swipeToDeleteCallback = new SwipeToDeleteCallBack(getActivity()) {
                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                    final int position = viewHolder.getAdapterPosition();
                    getReferralDeleted(position);

                }
            };
            ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
            itemTouchhelper.attachToRecyclerView(mfragmentReferralBinding.recycleView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getReferralDeleted(int position) {
        try {
            ReferralDeleteRequest request = new ReferralDeleteRequest();
            request.setId(mAdapter.getItem(position).getId());
            request.setTaskId(mAdapter.getItem(position).getTaskId());
            request.setFirstName(mAdapter.getItem(position).getFirstName());
            request.setLastName(mAdapter.getItem(position).getLastName());
            request.setMobileNo(mAdapter.getItem(position).getMobileNo());
            request.setAlternateMobileNo(mAdapter.getItem(position).getAlternateMobileNo());
            request.setEmail(mAdapter.getItem(position).getEmail());
            request.setInterestedService(mAdapter.getItem(position).getInterestedService());
            NetworkCallController controller = new NetworkCallController(this);
            controller.setListner(this);
            controller.getDeleteReferrals(DELETE_REFERRAL_REQUEST, request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void getReferralList() {
        try {
            NetworkCallController controller = new NetworkCallController(this);
            controller.setListner(this);
            controller.getReferrals(GET_REFERRAL_REQUEST, taskId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onAddReferralClicked(View view) {
        try {
            if ((NewTaskDetailsActivity) getActivity() != null) {
                mGeneralRealmModel = getRealm().where(GeneralData.class).findAll();
                if (mGeneralRealmModel != null && mGeneralRealmModel.size() > 0) {
                    LayoutInflater li = LayoutInflater.from(getActivity());

                    View promptsView = li.inflate(R.layout.add_referral_dialog, null);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

                    alertDialogBuilder.setView(promptsView);

                    alertDialogBuilder.setTitle(getString(R.string.add_referral));
                    final AlertDialog alertDialog = alertDialogBuilder.create();
                    final AppCompatEditText edt_fname =
                            (AppCompatEditText) promptsView.findViewById(R.id.edt_firstname);
                    final AppCompatEditText edt_lname =
                            (AppCompatEditText) promptsView.findViewById(R.id.edt_lastname);
                    final AppCompatEditText edt_contact =
                            (AppCompatEditText) promptsView.findViewById(R.id.edtmobile);
                    final AppCompatEditText edt_alt_contact =
                            (AppCompatEditText) promptsView.findViewById(R.id.edt_alt_mobile);
                    final AppCompatEditText edt_interested =
                            (AppCompatEditText) promptsView.findViewById(R.id.edt_interested);
                    final AppCompatEditText edt_email =
                            (AppCompatEditText) promptsView.findViewById(R.id.edtemail);
                    final AppCompatButton btn_send =
                            (AppCompatButton) promptsView.findViewById(R.id.btn_send);
                    final AppCompatButton btn_cancel =
                            (AppCompatButton) promptsView.findViewById(R.id.btn_cancel);

                    btn_send.setOnClickListener(v -> {
                        String mobile = "";
                        String Alt_Mobile = "";
                        String Technicain_Mobile = "";
                        if (mGeneralRealmModel.get(0).getMobileNumber() != null) {
                            mobile = mGeneralRealmModel.get(0).getMobileNumber();
                        }
                        if (mGeneralRealmModel.get(0).getAlternateMobileNumber() != null) {
                            Alt_Mobile = mGeneralRealmModel.get(0).getAlternateMobileNumber();
                        }
                        if (technicianMobileNo != null && technicianMobileNo.length() > 0) {
                            Technicain_Mobile = technicianMobileNo;
                        }

                        if (validateSaveReferral(edt_fname, edt_lname, edt_contact, edt_email, mobile, Alt_Mobile, Technicain_Mobile)) {

                            NetworkCallController controller = new NetworkCallController(ReferralFragment.this);
                            ReferralRequest request = new ReferralRequest();
                            request.setTaskId(taskId);
                            request.setFirstName(edt_fname.getText().toString());
                            request.setLastName("");
                            request.setMobileNo(edt_contact.getText().toString());
                            request.setAlternateMobileNo("");
                            request.setEmail("");
                            request.setInterestedService("");

                            controller.setListner(new NetworkResponseListner() {
                                @Override
                                public void onResponse(int requestCode, Object response) {
                                    ReferralResponse refResponse = (ReferralResponse) response;
                                    if (refResponse.getSuccess()) {
                                        mAdapter.notifyDataSetChanged();
                                        Toasty.success(getActivity(), getResources().getString(R.string.referral_added_successfully), Toast.LENGTH_SHORT).show();
                                        getReferralList();
                                    }
                                }

                                @Override
                                public void onFailure(int requestCode) {

                                }
                            });
                            controller.postReferrals(POST_REFERRAL_REQUEST, request);
                            alertDialog.dismiss();
                            mAdapter.notifyDataSetChanged();

                        }
                    });


                    btn_cancel.setOnClickListener(v -> alertDialog.dismiss());
                    alertDialog.setIcon(R.mipmap.logo);

                    alertDialog.show();
                }
            }

        } catch (Exception e) {
            RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
            if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                AppUtils.sendErrorLogs(e.getMessage(), getClass().getSimpleName(), "onAddReferralClicked", lineNo, userName, DeviceName);
            }
        }

    }

    @Override
    public void onDeleteItemClicked(int position) {
        try {
            ReferralDeleteRequest request = new ReferralDeleteRequest();
            request.setId(mAdapter.getItem(position).getId());
            request.setTaskId(mAdapter.getItem(position).getTaskId());
            request.setFirstName(mAdapter.getItem(position).getFirstName());
            request.setLastName(mAdapter.getItem(position).getLastName());
            request.setMobileNo(mAdapter.getItem(position).getMobileNo());
            request.setAlternateMobileNo(mAdapter.getItem(position).getAlternateMobileNo());
            request.setEmail(mAdapter.getItem(position).getEmail());
            request.setInterestedService(mAdapter.getItem(position).getInterestedService());
            NetworkCallController controller = new NetworkCallController(this);
            controller.setListner(this);
            controller.getDeleteReferrals(DELETE_REFERRAL_REQUEST, request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onItemClick(int positon) {

    }

    @Override
    public void onResponse(int requestCode, Object data) {
        switch (requestCode) {
            case GET_REFERRAL_REQUEST:
                try {
                    List<ReferralList> items = (List<ReferralList>) data;
                    mfragmentReferralBinding.swipeRefreshLayout.setRefreshing(false);
                    if (items != null) {
                        if (pageNumber == 1 && items.size() > 0) {
                            mfragmentReferralBinding.emptyTask.setVisibility(View.GONE);
                            mAdapter.setData(items);
                            mAdapter.notifyDataSetChanged();
                            enableSwipeToDeleteAndUndo();
                        } else if (items.size() > 0) {
                            mfragmentReferralBinding.emptyTask.setVisibility(View.GONE);
                            mAdapter.addData(items);
                            mAdapter.notifyDataSetChanged();
                            enableSwipeToDeleteAndUndo();
                        } else {
                            pageNumber--;
                        }
                    } else {
                        mfragmentReferralBinding.emptyTask.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

            case DELETE_REFERRAL_REQUEST:
                try {
                    ReferralResponse DeleteResponse = (ReferralResponse) data;
                    if (DeleteResponse.getSuccess()) {
                        mAdapter.removeAll();
                        getReferralList();
//                    Toast.makeText(getActivity(), "Deleted Successfully.", Toast.LENGTH_LONG).show();
                        Toasty.success(getActivity(), getResources().getString(R.string.deleted_successfully), Toast.LENGTH_SHORT).show();

                        mAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.failed), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onFailure(int requestCode) {
        switch (requestCode) {
            case GET_REFERRAL_REQUEST:
                mfragmentReferralBinding.swipeRefreshLayout.setRefreshing(false);
                break;
        }
    }

    private boolean validateSaveReferral(AppCompatEditText edt_fname, AppCompatEditText edt_lname, AppCompatEditText edt_contact, AppCompatEditText edtEmail, String mobile, String alt_Mobile, String technicain_Mobile) {
        if (edt_fname.getText().toString().trim().length() == 0) {
            edt_fname.setError(getString(R.string.name_is_required));
            edt_fname.requestFocus();
            return false;
        } /*else if (edt_lname.getText().toString().trim().length() == 0) {
            edt_lname.setError("Last name is required!");
            edt_lname.requestFocus();
            return false;
        }*/ else if (edt_contact.getText().toString().trim().length() == 0) {
            edt_contact.setError(getString(R.string.mobile_number_is_required));
            edt_contact.requestFocus();
            return false;
        } else if (edt_contact.getText().toString().trim().length() < 10) {
            edt_contact.setError(getString(R.string.mobile_number_is_invalid));
            edt_contact.requestFocus();
            return false;
        } else if (edt_contact.getText().toString().equals(mobile)) {
            edt_contact.setError(getString(R.string.mobile_number_is_invalid));
            edt_contact.requestFocus();
            return false;
        } else if (edt_contact.getText().toString().equals(alt_Mobile)) {
            edt_contact.setError(getString(R.string.mobile_number_is_invalid));
            edt_contact.requestFocus();
            return false;
        } else if (edt_contact.getText().toString().equals(technicain_Mobile)) {
            edt_contact.setError(getString(R.string.mobile_number_is_invalid));
            edt_contact.requestFocus();
            return false;
        } else {
            return true;
        }
    }

}
