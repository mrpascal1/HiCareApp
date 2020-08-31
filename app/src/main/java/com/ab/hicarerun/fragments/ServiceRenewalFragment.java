package com.ab.hicarerun.fragments;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ab.hicarerun.BaseFragment;
import com.ab.hicarerun.R;
import com.ab.hicarerun.adapter.AssessmentReportAdapter;
import com.ab.hicarerun.adapter.ServiceRenewalAdapter;
import com.ab.hicarerun.databinding.FragmentServiceRenewalBinding;
import com.ab.hicarerun.network.NetworkCallController;
import com.ab.hicarerun.network.NetworkResponseListner;
import com.ab.hicarerun.network.models.ServicePlanModel.PlanData;

import java.util.Objects;

public class ServiceRenewalFragment extends BaseFragment  {
    FragmentServiceRenewalBinding mFragmentServiceRenewalBinding;
    private static final int SERVICE_REQ = 1000;
    private static final String ARG_TASK = "ARG_TASK";
    private String taskId;
    private ServiceRenewalAdapter mAdapter;
    private Integer pageNumber = 1;
    private Double mDiscount = 0.0;

    public ServiceRenewalFragment() {
        // Required empty public constructor
    }

    public static ServiceRenewalFragment newInstance(String taskId) {
        Bundle args = new Bundle();
        args.putString(ARG_TASK, taskId);
        ServiceRenewalFragment fragment = new ServiceRenewalFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle oldInstanceState)
    {
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
    }

    private void getServicePlans() {
        try {
            NetworkCallController controller = new NetworkCallController(this);
            controller.setListner(new NetworkResponseListner<PlanData>() {
                @Override
                public void onResponse(int requestCode, PlanData response) {
                    mFragmentServiceRenewalBinding.txtCurrentTitle.setText(response.getPlanName());
                    mFragmentServiceRenewalBinding.txtCurrentDesc.setText(response.getServiceDescription());
                    mFragmentServiceRenewalBinding.txtCurrentAmount.setText("\u20B9"+" "+ response.getDiscountedOrderAmount());
                    mFragmentServiceRenewalBinding.txtCurrentDisAmount.setText("\u20B9"+" "+ response.getActualOrderAmount());
                    mFragmentServiceRenewalBinding.txtCurrentDiscount.setText(response.getDiscount()+"%"+" OFF");
                    mFragmentServiceRenewalBinding.txtCurrentDiscount.setTypeface(mFragmentServiceRenewalBinding.txtCurrentDiscount.getTypeface(), Typeface.BOLD);
                    mFragmentServiceRenewalBinding.txtCurrentAmount.setTypeface(mFragmentServiceRenewalBinding.txtCurrentAmount.getTypeface(), Typeface.BOLD);
                    mFragmentServiceRenewalBinding.txtCurrentAmount.setTypeface(mFragmentServiceRenewalBinding.txtCurrentAmount.getTypeface(), Typeface.BOLD);
                    mFragmentServiceRenewalBinding.txtNow.setTypeface(mFragmentServiceRenewalBinding.txtNow.getTypeface(), Typeface.BOLD);
                    mFragmentServiceRenewalBinding.txtHeaderOther.setTypeface(mFragmentServiceRenewalBinding.txtHeaderOther.getTypeface(), Typeface.BOLD);
//                    mFragmentServiceRenewalBinding.txtCurrentInstant.setText("Get instant "+response.getDiscount()+"% OFF"+ " discount");
                    mFragmentServiceRenewalBinding.txtCurrentDisAmount.setPaintFlags(mFragmentServiceRenewalBinding.txtCurrentDisAmount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

//                    if(response.getDiscount()!=null){
//                        mDiscount = Double.parseDouble(response.getDiscount());
//                    }

                    if(response.getDiscount()!=null && !response.getDiscount().equals("0.00")){
                        mFragmentServiceRenewalBinding.txtCurrentDiscount.setVisibility(View.VISIBLE);
                        mFragmentServiceRenewalBinding.txtNow.setVisibility(View.VISIBLE);
                        mFragmentServiceRenewalBinding.txtCurrentDisAmount.setVisibility(View.VISIBLE);
                    }else {
                        mFragmentServiceRenewalBinding.txtCurrentDiscount.setVisibility(View.GONE);
                        mFragmentServiceRenewalBinding.txtNow.setVisibility(View.GONE);
                        mFragmentServiceRenewalBinding.txtCurrentDisAmount.setVisibility(View.GONE);
                    }


                    mFragmentServiceRenewalBinding.lnrCurrentContinue.setOnClickListener(v -> {
                        ServicePlanBottomSheet bottomSheet = new ServicePlanBottomSheet(response);
                        bottomSheet.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), bottomSheet.getTag());
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
                            ServicePlanBottomSheet bottomSheet = new ServicePlanBottomSheet(response.getRenewalServicePlans().get(position));
                            bottomSheet.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), bottomSheet.getTag());
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


}
