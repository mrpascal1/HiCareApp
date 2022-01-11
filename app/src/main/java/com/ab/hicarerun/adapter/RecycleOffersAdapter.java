package com.ab.hicarerun.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.LayoutOffersAdapterBinding;
import com.ab.hicarerun.handler.OnListItemClickHandler;
import com.ab.hicarerun.handler.OnRewardtemClickHandler;
import com.ab.hicarerun.network.models.offersmodel.RewardList;
import com.ab.hicarerun.viewmodel.RewardsViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arjun Bhatt on 2/20/2020.
 */
public class RecycleOffersAdapter extends RecyclerView.Adapter<RecycleOffersAdapter.ViewHolder> {
    private OnListItemClickHandler onItemClickHandler;
    private OnRewardtemClickHandler onRewardClickItemClickHandler;
    private final Context mContext;
    private List<RewardsViewModel> items = null;

    public RecycleOffersAdapter(Context context) {
        if (items == null) {
            items = new ArrayList<>();
        }
        this.mContext = context;
    }

    @NotNull
    @Override
    public RecycleOffersAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        LayoutOffersAdapterBinding mLayoutOffersAdapterBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.layout_offers_adapter, parent, false);
        return new RecycleOffersAdapter.ViewHolder(mLayoutOffersAdapterBinding);
    }


    @Override
    public void onBindViewHolder(@NotNull RecycleOffersAdapter.ViewHolder holder, final int position) {

        try {
            if (items.get(position).getRewardScratchDone()) {
                holder.mLayoutOffersAdapterBinding.relCover.setVisibility(View.GONE);
            } else {
                holder.mLayoutOffersAdapterBinding.relCover.setVisibility(View.VISIBLE);
            }

            if (items.get(position).getTotalPointsEarned() != null) {
                holder.mLayoutOffersAdapterBinding.txtWinningPoint.setText(String.valueOf(items.get(position).getTotalPointsEarned()));
            }

            holder.mLayoutOffersAdapterBinding.cardReward.setOnClickListener(view -> onRewardClickItemClickHandler.onRewardClicked(position));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setOnRewardClickListener(OnRewardtemClickHandler onRewardClickItemClickHandler) {
        this.onRewardClickItemClickHandler = onRewardClickItemClickHandler;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setData(List<RewardList> data) {
        items.clear();
        for (int index = 0; index < data.size(); index++) {
            RewardsViewModel rewardsViewModel = new RewardsViewModel();
            rewardsViewModel.clone(data.get(index));
            items.add(rewardsViewModel);
        }
    }

    //
    public void addData(List<RewardList> data) {
        items.clear();
        for (int index = 0; index < data.size(); index++) {
            RewardsViewModel rewardsViewModel = new RewardsViewModel();
            rewardsViewModel.clone(data.get(index));
            items.add(rewardsViewModel);
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private final LayoutOffersAdapterBinding mLayoutOffersAdapterBinding;

        public ViewHolder(LayoutOffersAdapterBinding mLayoutOffersAdapterBinding) {
            super(mLayoutOffersAdapterBinding.getRoot());
            this.mLayoutOffersAdapterBinding = mLayoutOffersAdapterBinding;
        }
    }
}
