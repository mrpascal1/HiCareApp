package com.ab.hicarerun.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.LayoutMissedDetailAdapterBinding;
import com.ab.hicarerun.databinding.LayoutScratchDetailAdapterBinding;
import com.ab.hicarerun.network.models.NewRewardsModel.MissedRewardDetailSummary;
import com.ab.hicarerun.network.models.NewRewardsModel.NewRewardDetailSummary;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arjun Bhatt on 7/2/2020.
 */
public class RewardMissedAdapter extends RecyclerView.Adapter<RewardMissedAdapter.ViewHolder> {
    private int progress = 0;
    private final Context mContext;
    private List<MissedRewardDetailSummary> items = null;

    public RewardMissedAdapter(Context context, List<MissedRewardDetailSummary> rewardSummaryList) {
        if (items == null) {
            items = new ArrayList<>();
        }
        this.mContext = context;
    }

    @NotNull
    @Override
    public RewardMissedAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        LayoutMissedDetailAdapterBinding layoutMissedDetailAdapterBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.layout_missed_detail_adapter, parent, false);
        return new RewardMissedAdapter.ViewHolder(layoutMissedDetailAdapterBinding);
    }


    @Override
    public void onBindViewHolder(@NotNull RewardMissedAdapter.ViewHolder holder, final int position) {
        try {
            holder.layoutMissedDetailAdapterBinding.txtPoints.setText(" "+String.valueOf(items.get(position).getPointsEarned())+" ");
            holder.layoutMissedDetailAdapterBinding.txtTitle.setText(items.get(position).getEventName());
            holder.layoutMissedDetailAdapterBinding.txtDescription.setText(items.get(position).getScoreDescription());
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setData(List<MissedRewardDetailSummary> data) {
        items.clear();
        items.addAll(data);
    }

    //
    public void addData(List<MissedRewardDetailSummary> data) {
        items.clear();
        items.addAll(data);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private final LayoutMissedDetailAdapterBinding layoutMissedDetailAdapterBinding;

        public ViewHolder(LayoutMissedDetailAdapterBinding layoutMissedDetailAdapterBinding) {
            super(layoutMissedDetailAdapterBinding.getRoot());
            this.layoutMissedDetailAdapterBinding = layoutMissedDetailAdapterBinding;
        }
    }
}
