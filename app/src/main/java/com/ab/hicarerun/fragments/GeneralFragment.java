package com.ab.hicarerun.fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;

import com.ab.hicarerun.BaseFragment;
import com.ab.hicarerun.R;
import com.ab.hicarerun.activities.TaskDetailsActivity;
import com.ab.hicarerun.databinding.FragmentGeneralBinding;
import com.ab.hicarerun.handler.OnSaveEventHandler;
import com.ab.hicarerun.handler.UserGeneralClickHandler;
import com.ab.hicarerun.network.models.GeneralModel.GeneralData;
import com.ab.hicarerun.network.models.GeneralModel.GeneralTaskStatus;
import com.ab.hicarerun.network.models.GeneralModel.IncompleteReason;
import com.ab.hicarerun.utils.AppUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.realm.RealmResults;


public class GeneralFragment extends BaseFragment implements UserGeneralClickHandler {
    FragmentGeneralBinding mFragmentGeneralBinding;
    RealmResults<GeneralData> mGeneralRealmModel;
    private String selectedStatus = "";
    private OnSaveEventHandler mCallback;
    private boolean isState = false;
    AlertDialog mAlertDialog = null;
    private String[] arrayReason = null;
    private String[] arrayStatus = null;
    private RealmResults<GeneralTaskStatus> generalTaskRealmModel;
    private RealmResults<IncompleteReason> ReasonRealmModel = null;
    private String Selection = "";
    private int radiopos = 0;
    private String status = "";
    private Boolean isFeedback = false;
    private String mode = "";
    private String OnSiteOtp = "";
    private String ScOtp = "";


    public GeneralFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        AppUtils.statusCheck(getActivity());
    }

    public static GeneralFragment newInstance(String taskId, String status) {
        Bundle args = new Bundle();
        GeneralFragment fragment = new GeneralFragment();
        fragment.setArguments(args);
        return fragment;
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

        mFragmentGeneralBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_general, container, false);
        return mFragmentGeneralBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFragmentGeneralBinding.setHandler(this);

        mFragmentGeneralBinding.spnStatus.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        mFragmentGeneralBinding.txtOtp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                getValidated();
            }
        });
        getGeneralData();
    }


    private void getGeneralData() {
        try {
            if ((TaskDetailsActivity) getActivity() != null) {
                mGeneralRealmModel = getRealm().where(GeneralData.class).findAll();
                if (mGeneralRealmModel != null && mGeneralRealmModel.size() > 0) {
                    status = mGeneralRealmModel.get(0).getSchedulingStatus();
                    String order = mGeneralRealmModel.get(0).getOrderNumber();
                    String duration = mGeneralRealmModel.get(0).getDuration();
                    String start = mGeneralRealmModel.get(0).getTaskAssignmentStartTime();
                    String finish = mGeneralRealmModel.get(0).getTaskAssignmentEndTime();
                    String serviceType = mGeneralRealmModel.get(0).getServiceType();

                    String[] split_sDuration = duration.split(":");
                    String hr_duration = split_sDuration[0];
                    String mn_duration = split_sDuration[1];

                    if (hr_duration.equals("00")) {
                        mFragmentGeneralBinding.txtDuration.setText(duration + " min");
                    } else {
                        mFragmentGeneralBinding.txtDuration.setText(duration + " hr");
                    }


                    mFragmentGeneralBinding.txtOrder.setText(order);
                    mFragmentGeneralBinding.txtStart.setText(start);
                    mFragmentGeneralBinding.txtFinish.setText(finish);
                    mFragmentGeneralBinding.txtType.setText(serviceType);

                    getStatus();

                    setDefaultReason();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setDefaultReason() {
        try {
            ReasonRealmModel = getRealm().where(IncompleteReason.class).findAll().sort("reason");
            String res = mGeneralRealmModel.get(0).getIncompleteReason();
            if (res == null || res.length() == 0) {
                mFragmentGeneralBinding.txtReason.setText("Select Reason");
            } else {
                mFragmentGeneralBinding.txtReason.setText(res);
            }
            if (mFragmentGeneralBinding.txtReason.getText().toString().equals("Select Reason")) {
                mCallback.getIncompleteReason("");
                mCallback.isIncompleteReason(true);

            } else {
                mCallback.getIncompleteReason(mFragmentGeneralBinding.txtReason.getText().toString());
                mCallback.isIncompleteReason(false);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getStatus() {
        selectedStatus = mGeneralRealmModel.get(0).getSchedulingStatus();
        generalTaskRealmModel = getRealm().where(GeneralTaskStatus.class).findAll().sort("Status");
        final ArrayList<String> type = new ArrayList<>();

        for (GeneralTaskStatus generalTaskStatus : generalTaskRealmModel) {
            type.add(generalTaskStatus.getStatus());
        }

        arrayStatus = new String[type.size()];
        arrayStatus = type.toArray(arrayStatus);

        ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_layout, arrayStatus);
        statusAdapter.setDropDownViewResource(R.layout.spinner_popup);
        mFragmentGeneralBinding.spnStatus.setAdapter(statusAdapter);

        mFragmentGeneralBinding.spnStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AppUtils.statusCheck(getActivity());
                mode = mFragmentGeneralBinding.spnStatus.getSelectedItem().toString();
                isFeedback = mGeneralRealmModel.get(0).getFeedBack();

                try {
                    mCallback.status(generalTaskRealmModel.get(position).getStatus());
                    if (generalTaskRealmModel.get(position).getStatus().equals("Incomplete")) {
                        mFragmentGeneralBinding.cardReason.setVisibility(View.VISIBLE);
                        if (mFragmentGeneralBinding.txtReason.getText().toString().equals("Select Reason")) {
                            mCallback.isIncompleteReason(true);
                            mCallback.getIncompleteReason("");

                        } else {
                            mCallback.isIncompleteReason(false);
                            mCallback.getIncompleteReason(mFragmentGeneralBinding.txtReason.getText().toString());
                        }
                    } else {
                        mFragmentGeneralBinding.cardReason.setVisibility(View.GONE);
                    }


                    mCallback.duration(mGeneralRealmModel.get(0).getDuration());
                    if (selectedStatus.equals(generalTaskRealmModel.get(position).getStatus())) {
                        mCallback.isGeneralChanged(true);
                        mCallback.status(generalTaskRealmModel.get(position).getStatus());
                    } else {
                        mCallback.isGeneralChanged(false);
                        mCallback.status(generalTaskRealmModel.get(position).getStatus());
                    }
                    getValidated();
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
                mFragmentGeneralBinding.spnStatus.setSelection(i);
            }
        }
    }


    @Override
    public void onReasonClicked(View view) {
        if (!status.equalsIgnoreCase("Incomplete")) {
            if ((TaskDetailsActivity) getActivity() != null) {

                mGeneralRealmModel = getRealm().where(GeneralData.class).findAll();
                if (mGeneralRealmModel != null && mGeneralRealmModel.size() > 0) {
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
                            mFragmentGeneralBinding.txtReason.setText(Selection);
                            if (mFragmentGeneralBinding.txtReason.getText().toString().equals("Select Reason")) {
                                mCallback.getIncompleteReason("");
                                mCallback.isIncompleteReason(true);
                            } else {
                                mCallback.getIncompleteReason(mFragmentGeneralBinding.txtReason.getText().toString());
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

    private void getValidated() {
        if (mode.equals("On-Site")) {
            Log.i("isFeedback", String.valueOf(isFeedback));
            if (isFeedback && mGeneralRealmModel.get(0).getOnsite_OTP() != null && !mGeneralRealmModel.get(0).getOnsite_OTP().equals("")) {
                if (status.equals("Dispatched")) {
                    mFragmentGeneralBinding.cardOtp.setVisibility(View.VISIBLE);
                } else {
                    mFragmentGeneralBinding.cardOtp.setVisibility(View.GONE);
                }
                OnSiteOtp = mGeneralRealmModel.get(0).getOnsite_OTP();
                ScOtp = mGeneralRealmModel.get(0).getSc_OTP();
                String otp = mFragmentGeneralBinding.txtOtp.getText().toString();
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
            }
        } else {
            mCallback.isEmptyOnsiteOtp(false);
            mCallback.isOnsiteOtp(false);
            mFragmentGeneralBinding.cardOtp.setVisibility(View.GONE);
        }

        if (mode.equals("Completed")) {
            if (mGeneralRealmModel.get(0).getRestrict_Early_Completion()) {
                String Duration = mGeneralRealmModel.get(0).getActualCompletionDateTime();
//                String oldFormat= "yyyy-MM-dd hh:mm aa";
                String newFormat= "yyyy-MM-dd HH:mm:ss";
//
//                String formatedDate = "";
//                SimpleDateFormat dateFormat = new SimpleDateFormat(oldFormat);
//                Date myDate = null;
//                try {
//                    myDate = dateFormat.parse(Duration);
//                } catch (java.text.ParseException e) {
//                    e.printStackTrace();
//                }

                try {
                    String DurationDate = AppUtils.reFormatDurationTime(Duration, newFormat);
                    String isStartDate = AppUtils.compareDates(AppUtils.currentDateTime(), DurationDate);
                    Log.i("isEarlyDuration",isStartDate);
                    if (isStartDate.equals("afterdate") || isStartDate.equals("equalsdate")) {
                        mCallback.isEarlyCompletion(false);
                    }else {
                        mCallback.isEarlyCompletion(true);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
//
//                SimpleDateFormat timeFormat = new SimpleDateFormat(newFormat);
//                formatedDate = timeFormat.format(myDate);


            }
        }
    }

}

