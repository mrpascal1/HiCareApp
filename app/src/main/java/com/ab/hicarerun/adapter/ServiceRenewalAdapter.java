package com.ab.hicarerun.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.LayoutCheckListAdapterBinding;
import com.ab.hicarerun.databinding.RenewalServicePlansAdapterBinding;
import com.ab.hicarerun.handler.OnCheckListItemClickHandler;
import com.ab.hicarerun.handler.OnListItemClickHandler;
import com.ab.hicarerun.network.models.SelfAssessModel.ResourceCheckList;
import com.ab.hicarerun.network.models.ServicePlanModel.RenewalServicePlan;
import com.ab.hicarerun.viewmodel.SelfAccessViewModel;
import com.ab.hicarerun.viewmodel.ServicePlanViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arjun Bhatt on 5/27/2020.
 */
public class ServiceRenewalAdapter extends RecyclerView.Adapter<ServiceRenewalAdapter.ViewHolder> {

    private OnListItemClickHandler onItemClickHandler;
    private final Context mContext;
    private static List<ServicePlanViewModel> items = null;
    public int selectedPosition = -1;
    private Double offerDiscount = 0.0;

    public ServiceRenewalAdapter(Context context) {
        if (items == null) {
            items = new ArrayList<>();
        }
        this.mContext = context;
    }


    @NotNull
    @Override
    public ServiceRenewalAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        RenewalServicePlansAdapterBinding mRenewalServicePlansAdapterBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.renewal_service_plans_adapter, parent, false);
        return new ServiceRenewalAdapter.ViewHolder(mRenewalServicePlansAdapterBinding);
    }

    @Override
    public void onBindViewHolder(@NotNull final ServiceRenewalAdapter.ViewHolder holder, final int position) {
        try {
            if(items.get(position).getDiscount()!=null){
                offerDiscount = Double.parseDouble(items.get(position).getDiscount());
            }
//            if(offerDiscount <= 0.0){
//                holder.mRenewalServicePlansAdapterBinding.txtInstant.setVisibility(View.GONE);
//                holder.mRenewalServicePlansAdapterBinding.relOffer.setVisibility(View.GONE);
//            }else {
//                holder.mRenewalServicePlansAdapterBinding.txtInstant.setVisibility(View.VISIBLE);
//                holder.mRenewalServicePlansAdapterBinding.relOffer.setVisibility(View.GONE);
//            }
            if (selectedPosition == position) {
                holder.mRenewalServicePlansAdapterBinding.txtInstant.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
                holder.mRenewalServicePlansAdapterBinding.serviceCard.setCardBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
                holder.mRenewalServicePlansAdapterBinding.relInner.setBackground(mContext.getResources().getDrawable(R.drawable.white_round_border));
            } else {
                holder.mRenewalServicePlansAdapterBinding.txtInstant.setTextColor(mContext.getResources().getColor(R.color.grey));
                holder.mRenewalServicePlansAdapterBinding.serviceCard.setCardBackgroundColor(mContext.getResources().getColor(R.color.soft_grey));
                holder.mRenewalServicePlansAdapterBinding.relInner.setBackgroundColor(mContext.getResources().getColor(R.color.soft_grey));
            }

            if (items.get(position).getIs_Recommended()) {
                holder.mRenewalServicePlansAdapterBinding.lnrRecommended.setVisibility(View.VISIBLE);
            } else {
                holder.mRenewalServicePlansAdapterBinding.lnrRecommended.setVisibility(View.GONE);
            }
            holder.mRenewalServicePlansAdapterBinding.txtAmount.setText("\u20B9"+" "+items.get(position).getDiscountedOrderAmount());
            holder.mRenewalServicePlansAdapterBinding.txtDisAmount.setText("\u20B9"+" "+items.get(position).getActualOrderAmount());
            holder.mRenewalServicePlansAdapterBinding.txtDiscount.setText(items.get(position).getDiscount()+"%");
            holder.mRenewalServicePlansAdapterBinding.txtDisAmount.setPaintFlags(holder.mRenewalServicePlansAdapterBinding.txtDisAmount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.mRenewalServicePlansAdapterBinding.txtPlanTitle.setText(items.get(position).getPlan_Name());
            holder.mRenewalServicePlansAdapterBinding.txtPlanDes.setText(items.get(position).getService_Description());
            holder.mRenewalServicePlansAdapterBinding.txtDiscount.setTypeface(holder.mRenewalServicePlansAdapterBinding.txtDiscount.getTypeface(), Typeface.BOLD);
            holder.mRenewalServicePlansAdapterBinding.txtOFF.setTypeface(holder.mRenewalServicePlansAdapterBinding.txtOFF.getTypeface(), Typeface.BOLD);
            holder.mRenewalServicePlansAdapterBinding.txtAmount.setTypeface(holder.mRenewalServicePlansAdapterBinding.txtAmount.getTypeface(), Typeface.BOLD);
            if (!items.get(position).getDiscountAmount().equals("") && !items.get(position).getDiscountAmount().equals("0.00")
                    && !items.get(position).getDiscountAmount().equals("0")){
                holder.mRenewalServicePlansAdapterBinding.txtSaving.setText("You will save "+"\u20B9"+""+items.get(position).getDiscountAmount() + " on this service!");
                holder.mRenewalServicePlansAdapterBinding.txtSaving.setVisibility(View.VISIBLE);
            }else {
                holder.mRenewalServicePlansAdapterBinding.txtSaving.setVisibility(View.GONE);
            }

            if(items.get(position).getDiscount()!=null && !items.get(position).getDiscount().equals("0.00") && !items.get(position).getDiscount().equals("0") && !items.get(position).getDiscount().equals("")){
                holder.mRenewalServicePlansAdapterBinding.relOffer.setVisibility(View.VISIBLE);
                holder.mRenewalServicePlansAdapterBinding.txtDisAmount.setVisibility(View.VISIBLE);
                holder.mRenewalServicePlansAdapterBinding.txtInstant.setVisibility(View.VISIBLE);
            }else {
                //holder.mRenewalServicePlansAdapterBinding.relOffer.setVisibility(View.GONE);
                holder.mRenewalServicePlansAdapterBinding.txtDisAmount.setVisibility(View.GONE);
                holder.mRenewalServicePlansAdapterBinding.txtInstant.setVisibility(View.GONE);
            }

            holder.mRenewalServicePlansAdapterBinding.txtInstant.setText(items.get(position).getOfferText());
            holder.itemView.setOnClickListener(v -> {
                onItemClickHandler.onItemClick(position);
                selectedPosition=position;
                notifyDataSetChanged();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setOnItemClickHandler(OnListItemClickHandler onItemClickHandler) {
        this.onItemClickHandler = onItemClickHandler;
    }


    public void setData(List<RenewalServicePlan> data) {
        items.clear();
        for (int index = 0; index < data.size(); index++) {
            ServicePlanViewModel servicePlanViewModel = new ServicePlanViewModel();
            servicePlanViewModel.clone(data.get(index));
            items.add(servicePlanViewModel);
        }
    }

    public void addData(List<RenewalServicePlan> data) {
        items.clear();
        for (int index = 0; index < data.size(); index++) {
            ServicePlanViewModel servicePlanViewModel = new ServicePlanViewModel();
            servicePlanViewModel.clone(data.get(index));
            items.add(servicePlanViewModel);
        }
    }

    public ServicePlanViewModel getItem(int position) {
        return items.get(position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private final RenewalServicePlansAdapterBinding mRenewalServicePlansAdapterBinding;

        public ViewHolder(RenewalServicePlansAdapterBinding mRenewalServicePlansAdapterBinding) {
            super(mRenewalServicePlansAdapterBinding.getRoot());
            this.mRenewalServicePlansAdapterBinding = mRenewalServicePlansAdapterBinding;
        }
    }

}
