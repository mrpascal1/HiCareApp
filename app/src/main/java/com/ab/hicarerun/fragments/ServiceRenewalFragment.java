package com.ab.hicarerun.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.ab.hicarerun.BaseFragment;
import com.ab.hicarerun.R;
import com.ab.hicarerun.activities.HomeActivity;
import com.ab.hicarerun.activities.NewTaskDetailsActivity;
import com.ab.hicarerun.adapter.AssessmentReportAdapter;
import com.ab.hicarerun.adapter.ServiceRenewalAdapter;
import com.ab.hicarerun.databinding.FragmentServiceRenewalBinding;
import com.ab.hicarerun.network.NetworkCallController;
import com.ab.hicarerun.network.NetworkResponseListner;
import com.ab.hicarerun.network.models.BasicResponse;
import com.ab.hicarerun.network.models.JeopardyModel.JeopardyReasonsList;
import com.ab.hicarerun.network.models.ServicePlanModel.NotRenewalReasons;
import com.ab.hicarerun.network.models.ServicePlanModel.PlanData;
import com.ab.hicarerun.network.models.WalletModel.WalletBase;
import com.ab.hicarerun.utils.AppUtils;
import com.ab.hicarerun.utils.LocaleHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class ServiceRenewalFragment extends BaseFragment {
    FragmentServiceRenewalBinding mFragmentServiceRenewalBinding;
    private static final int SERVICE_REQ = 1000;
    private static final int NOT_RENEW_REQ = 2000;

    private static final String ARG_TASK = "ARG_TASK";
    private String taskId;
    private ServiceRenewalAdapter mAdapter;
    private Integer pageNumber = 1;
    private Double mDiscount = 0.0;
    private List<NotRenewalReasons> notRenewalReasonsList = new ArrayList<>();
    private Context mContext;
    private double walletPoints = 0;


    public ServiceRenewalFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public static ServiceRenewalFragment newInstance(String taskId) {
        Bundle args = new Bundle();
        args.putString(ARG_TASK, taskId);
        ServiceRenewalFragment fragment = new ServiceRenewalFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle oldInstanceState) {
        super.onSaveInstanceState(oldInstanceState);
        oldInstanceState.clear();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            taskId = getArguments().getString(ARG_TASK);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFragmentServiceRenewalBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_service_renewal, container, false);
        getActivity().setTitle("Service Plans");
        return mFragmentServiceRenewalBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getActivity());
        mFragmentServiceRenewalBinding.recycleView.setLayoutManager(lm);
        mFragmentServiceRenewalBinding.recycleView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new ServiceRenewalAdapter(getActivity());
        mFragmentServiceRenewalBinding.recycleView.setAdapter(mAdapter);
//        mFragmentServiceRenewalBinding.waveHeader.stop();
        getServicePlans();
        mFragmentServiceRenewalBinding.btnNotInterested.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNotInterestedReasons();
            }
        });
        getWalletBalance(taskId);
    }

    private void showNotInterestedReasons() {
        try {

            if (notRenewalReasonsList != null && notRenewalReasonsList.size() > 0) {
                try {

                    final AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                    LayoutInflater inflater = LayoutInflater.from(getActivity());
                    final View v = inflater.inflate(R.layout.jeopardy_reasons_layout, null, false);
                    final RadioGroup radioGroup = (RadioGroup) v.findViewById(R.id.radiogrp);

                    for (int i = 0; i < notRenewalReasonsList.size(); i++) {
                        final RadioButton rbn = new RadioButton(getActivity());
                        rbn.setId(i);
                        rbn.setText(notRenewalReasonsList.get(i).getValue());
                        rbn.setTextSize(15);
                        RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
                        params.setMargins(10, 10, 2, 1);
                        radioGroup.addView(rbn, params);
                    }
                    builder.setView(v);
                    builder.setCancelable(false);
                    builder.setPositiveButton(getResources().getString(R.string.submit_helpline), (dialogInterface, i) -> {
                        RadioButton radioButton = (RadioButton) v.findViewById(radioGroup.getCheckedRadioButtonId());
                        if (radioGroup.getCheckedRadioButtonId() == -1) {
                            Toast.makeText(getActivity(), getResources().getString(R.string.please_select_atleast_one_reason), Toast.LENGTH_SHORT).show();
                            builder.setCancelable(false);
                        } else {
                            NetworkCallController controller = new NetworkCallController(this);
                            controller.setListner(new NetworkResponseListner() {
                                @Override
                                public void onResponse(int requestCode, Object data) {

                                    BasicResponse response = (BasicResponse) data;
                                    if (response.getSuccess()) {
                                        AppUtils.NOT_RENEWAL_DONE = false;
                                        dialogInterface.dismiss();
                                        Intent intent = new Intent(mContext, HomeActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        mContext.startActivity(intent);
                                    } else {
                                        Toasty.error(getActivity(),
                                                "Please try again!",
                                                Toasty.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onFailure(int requestCode) {

                                }
                            });
                            controller.updateNoRenewalReason(NOT_RENEW_REQ, taskId, radioButton.getText().toString());
                        }
                    });
                    builder.setNegativeButton(getResources().getString(R.string.cancel_helpline), (dialogInterface, i) -> dialogInterface.cancel());
                    final AlertDialog dialog = builder.create();
                    Objects.requireNonNull(dialog.getWindow()).getAttributes().windowAnimations = R.style.DialogAnimation_2;
                    dialog.show();

                } catch (Exception e) {
                    e.printStackTrace();
                    dismissProgressDialog();
                }
            } else {
                Toasty.info(requireActivity(), getResources().getString(R.string.complete_first_job), Toasty.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getServicePlans() {
        try {
            NetworkCallController controller = new NetworkCallController(this);
            controller.setListner(new NetworkResponseListner<PlanData>() {
                @Override
                public void onResponse(int requestCode, PlanData response) {
                    if (response.getRenewalReasonsList() != null && response.getRenewalReasonsList().size() > 0) {
                        notRenewalReasonsList = response.getRenewalReasonsList();
                    }
                    if (response.getTechScript() != null && !response.getTechScript().equals("")) {
//                        mFragmentServiceRenewalBinding.relNotInterested.setVisibility(View.VISIBLE);
                        mFragmentServiceRenewalBinding.txtScript.setText(response.getTechScript());
                        mFragmentServiceRenewalBinding.txtScript.setVisibility(View.VISIBLE);
                    } else {
                        mFragmentServiceRenewalBinding.txtScript.setVisibility(View.GONE);
//                        mFragmentServiceRenewalBinding.relNotInterested.setVisibility(View.GONE);
                    }

                    if (response.getAverageRating() != null && response.getAverageRating() != 0) {
                        mFragmentServiceRenewalBinding.txtRating.setText(String.valueOf(response.getAverageRating()));
                        mFragmentServiceRenewalBinding.ratingBar.setVisibility(View.VISIBLE);
                        mFragmentServiceRenewalBinding.txtRating.setTextSize(28f);
                        mFragmentServiceRenewalBinding.ratingBar.setRating(response.getAverageRating());

                    } else {
                        mFragmentServiceRenewalBinding.txtRating.setText("N/A");
                        mFragmentServiceRenewalBinding.txtRating.setTextSize(16f);
                        mFragmentServiceRenewalBinding.ratingBar.setVisibility(View.GONE);
                    }

                    if (response.getComplaints() != null) {
                        mFragmentServiceRenewalBinding.txtComplaint.setText(String.valueOf(response.getComplaints()));
                    } else {
                        mFragmentServiceRenewalBinding.txtComplaint.setText("0");
                    }

                    if (response.getCompletedServices() != null) {
                        mFragmentServiceRenewalBinding.txtCompletedServices.setText(String.valueOf(response.getCompletedServices() + "/" + String.valueOf(response.getTotalServices())));
                        mFragmentServiceRenewalBinding.lnrCompletedServices.setVisibility(View.VISIBLE);
                    } else {
                        mFragmentServiceRenewalBinding.lnrCompletedServices.setVisibility(View.GONE);
                    }


                    mFragmentServiceRenewalBinding.txtCurrentTitle.setText(response.getPlanName());
                    mFragmentServiceRenewalBinding.txtCurrentDesc.setText(response.getServiceDescription());
                    mFragmentServiceRenewalBinding.txtCurrentAmount.setText("\u20B9" + " " + response.getDiscountedOrderAmount());
                    mFragmentServiceRenewalBinding.txtCurrentDisAmount.setText("\u20B9" + " " + response.getActualOrderAmount());
                    mFragmentServiceRenewalBinding.txtCurrentDiscount.setText(response.getDiscount() + "%" + " OFF");
                    mFragmentServiceRenewalBinding.txtSaving.setText("You save " + "\u20B9 " + response.getDiscountAmount());
                    mFragmentServiceRenewalBinding.txtCurrentDiscount.setTypeface(mFragmentServiceRenewalBinding.txtCurrentDiscount.getTypeface(), Typeface.BOLD);
                    mFragmentServiceRenewalBinding.txtRating.setTypeface(mFragmentServiceRenewalBinding.txtRating.getTypeface(), Typeface.BOLD);
                    mFragmentServiceRenewalBinding.txtCurrentAmount.setTypeface(mFragmentServiceRenewalBinding.txtCurrentAmount.getTypeface(), Typeface.BOLD);
                    mFragmentServiceRenewalBinding.txtCurrentAmount.setTypeface(mFragmentServiceRenewalBinding.txtCurrentAmount.getTypeface(), Typeface.BOLD);
                    mFragmentServiceRenewalBinding.txtNow.setTypeface(mFragmentServiceRenewalBinding.txtNow.getTypeface(), Typeface.BOLD);
                    mFragmentServiceRenewalBinding.txtTitle.setTypeface(mFragmentServiceRenewalBinding.txtTitle.getTypeface(), Typeface.BOLD);
                    mFragmentServiceRenewalBinding.txtCompletedServices.setTypeface(mFragmentServiceRenewalBinding.txtCompletedServices.getTypeface(), Typeface.BOLD);
                    mFragmentServiceRenewalBinding.txtComplaint.setTypeface(mFragmentServiceRenewalBinding.txtComplaint.getTypeface(), Typeface.BOLD);
                    mFragmentServiceRenewalBinding.txtHeaderOther.setTypeface(mFragmentServiceRenewalBinding.txtHeaderOther.getTypeface(), Typeface.BOLD);
//                    mFragmentServiceRenewalBinding.txtCurrentInstant.setText("Get instant "+response.getDiscount()+"% OFF"+ " discount");
                    mFragmentServiceRenewalBinding.txtCurrentDisAmount.setPaintFlags(mFragmentServiceRenewalBinding.txtCurrentDisAmount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

//                    if(response.getDiscount()!=null){
//                        mDiscount = Double.parseDouble(response.getDiscount());
//                    }

                    if (response.getDiscount() != null && !response.getDiscount().equals("0.00") && !response.getDiscount().equals("0")) {
                        mFragmentServiceRenewalBinding.txtCurrentDiscount.setVisibility(View.VISIBLE);
                        mFragmentServiceRenewalBinding.txtNow.setVisibility(View.VISIBLE);
                        mFragmentServiceRenewalBinding.txtCurrentDisAmount.setVisibility(View.VISIBLE);
                    } else {
                        mFragmentServiceRenewalBinding.txtCurrentDiscount.setVisibility(View.GONE);
                        mFragmentServiceRenewalBinding.txtNow.setVisibility(View.GONE);
                        mFragmentServiceRenewalBinding.txtCurrentDisAmount.setVisibility(View.GONE);
                    }
                    mFragmentServiceRenewalBinding.lnrCurrentContinue.setOnClickListener(v -> {
                        ServicePlanBottomSheet bottomSheet = new ServicePlanBottomSheet(response, walletPoints);
                        bottomSheet.show(requireActivity().getSupportFragmentManager(), bottomSheet.getTag());
                    });

                    if (response.getRenewalServicePlans() != null) {
                        if (pageNumber == 1 && response.getRenewalServicePlans().size() > 0) {
                            mAdapter.setData(response.getRenewalServicePlans());
                            mAdapter.notifyDataSetChanged();
                        } else if (response.getRenewalServicePlans().size() > 0) {
                            mAdapter.addData(response.getRenewalServicePlans());
                            mAdapter.notifyDataSetChanged();
                        }
                        mAdapter.setOnItemClickHandler(position -> {
                            ServicePlanBottomSheet bottomSheet = new ServicePlanBottomSheet(response.getRenewalServicePlans().get(position), walletPoints);
                            bottomSheet.show(requireActivity().getSupportFragmentManager(), bottomSheet.getTag());
                        });
                    }
                }

                @Override
                public void onFailure(int requestCode) {
                }
            });
            controller.getServicePlans(SERVICE_REQ, taskId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getWalletBalance(String taskId){
        NetworkCallController controller = new NetworkCallController(this);
        controller.setListner(new NetworkResponseListner<WalletBase>() {
            @Override
            public void onResponse(int requestCode, WalletBase response) {
                if (response != null){
                    if (response.isSuccess()){
                        if (response.getData() != null) {
                            Log.d("TAG", "" + response);
                            walletPoints  = response.getData().getTotalRedeemablePointsInWallet();
                            mFragmentServiceRenewalBinding.walletPointsTitleTv.setText("Hygiene Points");
                            mFragmentServiceRenewalBinding.walletPointsTv.setText(" " + walletPoints);
                            mFragmentServiceRenewalBinding.walletPointsTitleTv.setVisibility(View.VISIBLE);
                            mFragmentServiceRenewalBinding.walletPointLayout.setVisibility(View.VISIBLE);
                        }else {
                            mFragmentServiceRenewalBinding.walletPointsTitleTv.setVisibility(View.GONE);
                            mFragmentServiceRenewalBinding.walletPointLayout.setVisibility(View.GONE);
                        }
                    }else {
                        mFragmentServiceRenewalBinding.walletPointsTitleTv.setVisibility(View.GONE);
                        mFragmentServiceRenewalBinding.walletPointLayout.setVisibility(View.GONE);
                    }
                }else {
                    mFragmentServiceRenewalBinding.walletPointsTitleTv.setVisibility(View.GONE);
                    mFragmentServiceRenewalBinding.walletPointLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(int requestCode) {
            }
        });
        controller.getWalletBalance(14122021, taskId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
