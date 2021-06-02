package com.ab.hicarerun.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.LayoutBazaarAdapterBinding;
import com.ab.hicarerun.databinding.LayoutScratchDetailAdapterBinding;
import com.ab.hicarerun.databinding.OffersHistoryFooterAdapterBinding;
import com.ab.hicarerun.handler.OnBazaarItemClickHandler;
import com.ab.hicarerun.handler.OnListItemClickHandler;
import com.ab.hicarerun.network.models.NewRewardsModel.NewRewardDetailSummary;
import com.ab.hicarerun.network.models.OffersModel.RewardDetailSummary;
import com.ab.hicarerun.network.models.RewardsModel.AvailableOffer;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arjun Bhatt on 3/26/2020.
 */
public class RewardsSummaryAdapter extends RecyclerView.Adapter<RewardsSummaryAdapter.ViewHolder> {
    private int progress = 0;
    private final Context mContext;
    private List<RewardDetailSummary> items = null;

    public RewardsSummaryAdapter(Context context, List<RewardDetailSummary> rewardSummaryList) {
        if (items == null) {
            items = new ArrayList<>();
        }
        this.mContext = context;
    }

    @NotNull
    @Override
    public RewardsSummaryAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        LayoutScratchDetailAdapterBinding mLayoutScratchDetailAdapterBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.layout_scratch_detail_adapter, parent, false);
        return new RewardsSummaryAdapter.ViewHolder(mLayoutScratchDetailAdapterBinding);
    }


    @Override
    public void onBindViewHolder(@NotNull RewardsSummaryAdapter.ViewHolder holder, final int position) {
        try {

            if (items.get(position).getScoreName().equals("Receiving Good Feedback from the Customer")) {
                if (items.get(position).getMissedEvent()) {
                    holder.mLayoutScratchDetailAdapterBinding.imgBadge.setImageResource(R.drawable.ic_feedback_badge);
                } else {
                    holder.mLayoutScratchDetailAdapterBinding.imgBadge.setImageResource(R.drawable.ic_feedback_green);
                }
            } else if (items.get(position).getScoreName().equals("Reporting On-Time (before 8:30 am) at the Service Center")) {
                if (items.get(position).getMissedEvent()) {
                    holder.mLayoutScratchDetailAdapterBinding.imgBadge.setImageResource(R.drawable.ic_clock_badge);
                } else {
                    holder.mLayoutScratchDetailAdapterBinding.imgBadge.setImageResource(R.drawable.ic_clock_green);
                }
            } else if (items.get(position).getScoreName().equals("Delivering the Jobs On-Time on the basis of Assignment Time")) {
                if (items.get(position).getMissedEvent()) {
                    holder.mLayoutScratchDetailAdapterBinding.imgBadge.setImageResource(R.drawable.ic_clock_badge);
                } else {
                    holder.mLayoutScratchDetailAdapterBinding.imgBadge.setImageResource(R.drawable.ic_clock_green);
                }
            }else {
                if (items.get(position).getMissedEvent()) {
                    holder.mLayoutScratchDetailAdapterBinding.imgBadge.setImageResource(R.drawable.ic_rewards_badge);
                } else {
                    holder.mLayoutScratchDetailAdapterBinding.imgBadge.setImageResource(R.drawable.ic_rewards_green);
                }
            }

            if (items.get(position).getMissedEvent()) {
                holder.mLayoutScratchDetailAdapterBinding.txtProb.setText("-");
                holder.mLayoutScratchDetailAdapterBinding.txtProb.setTextColor(mContext.getResources().getColor(R.color.lightRed));
                holder.mLayoutScratchDetailAdapterBinding.txtPoints.setTextColor(mContext.getResources().getColor(R.color.lightRed));
                holder.mLayoutScratchDetailAdapterBinding.txtPtsLabel.setTextColor(mContext.getResources().getColor(R.color.lightRed));
            } else {
                holder.mLayoutScratchDetailAdapterBinding.txtProb.setText("+");
                holder.mLayoutScratchDetailAdapterBinding.txtProb.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
                holder.mLayoutScratchDetailAdapterBinding.txtPtsLabel.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
                holder.mLayoutScratchDetailAdapterBinding.txtPoints.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
            }
            holder.mLayoutScratchDetailAdapterBinding.txtPoints.setText(" " + String.valueOf(items.get(position).getPointsEarned()) + " ");
            holder.mLayoutScratchDetailAdapterBinding.txtTitle.setText(items.get(position).getEventName());
            holder.mLayoutScratchDetailAdapterBinding.txtDescription.setText(items.get(position).getScoreDescription());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setData(List<RewardDetailSummary> data) {
        items.clear();
        items.addAll(data);
    }

    //
    public void addData(List<RewardDetailSummary> data) {
        items.clear();
        items.addAll(data);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private final LayoutScratchDetailAdapterBinding mLayoutScratchDetailAdapterBinding;

        public ViewHolder(LayoutScratchDetailAdapterBinding mLayoutScratchDetailAdapterBinding) {
            super(mLayoutScratchDetailAdapterBinding.getRoot());
            this.mLayoutScratchDetailAdapterBinding = mLayoutScratchDetailAdapterBinding;
        }
    }
}
