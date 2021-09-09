package com.ab.hicarerun.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.LayoutBazaarAdapterBinding;
import com.ab.hicarerun.databinding.LayoutOffersAdapterBinding;
import com.ab.hicarerun.handler.OnBazaarItemClickHandler;
import com.ab.hicarerun.handler.OnListItemClickHandler;
import com.ab.hicarerun.network.models.RewardsModel.AvailableOffer;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arjun Bhatt on 3/25/2020.
 */
public class RecycleBazaarAdapter extends RecyclerView.Adapter<RecycleBazaarAdapter.ViewHolder> {
    private OnListItemClickHandler onItemClickHandler;
    private OnBazaarItemClickHandler onBazaarItemClickHandler;
    private int progress = 0;
    private final Context mContext;
    private List<AvailableOffer> items = null;

    public RecycleBazaarAdapter(Context context) {
        if (items == null) {
            items = new ArrayList<>();
        }
        this.mContext = context;
    }

    @NotNull
    @Override
    public RecycleBazaarAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        LayoutBazaarAdapterBinding mLayoutBazaarAdapterBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.layout_bazaar_adapter, parent, false);
        return new RecycleBazaarAdapter.ViewHolder(mLayoutBazaarAdapterBinding);
    }


    @Override
    public void onBindViewHolder(@NotNull RecycleBazaarAdapter.ViewHolder holder, final int position) {
        try {
            Picasso.get().load(items.get(position).getImageURL()).into(holder.mLayoutBazaarAdapterBinding.imgOffer);
            holder.mLayoutBazaarAdapterBinding.txtTitle.setText(items.get(position).getTitle());
            holder.mLayoutBazaarAdapterBinding.txtDescription.setText(items.get(position).getDescription());
            holder.mLayoutBazaarAdapterBinding.btnRedeem.setOnClickListener(view -> onBazaarItemClickHandler.onOfferRedeemClicked(position));
            holder.mLayoutBazaarAdapterBinding.txtRequiredPoints.setText(items.get(position).getPointsRequired() + " Pts.");
            holder.mLayoutBazaarAdapterBinding.progressBar.setMax(items.get(position).getPointsRequired());
            holder.mLayoutBazaarAdapterBinding.progressBar.setProgress(progress);
            if (items.get(position).isLocked()) {
                holder.mLayoutBazaarAdapterBinding.btnLevelName.setEnabled(false);
                holder.mLayoutBazaarAdapterBinding.btnRedeem.setVisibility(View.GONE);
                holder.mLayoutBazaarAdapterBinding.btnLevelName.setText("" + items.get(position).getUnlocksAtLevel());
            }else {
                holder.mLayoutBazaarAdapterBinding.lockedBtnLayout.setVisibility(View.GONE);
                holder.mLayoutBazaarAdapterBinding.btnRedeem.setVisibility(View.VISIBLE);
                if (progress >= items.get(position).getPointsRequired()) {
                    holder.mLayoutBazaarAdapterBinding.btnRedeem.setEnabled(true);
                    holder.mLayoutBazaarAdapterBinding.btnRedeem.setText("Collect this offer");
                    holder.mLayoutBazaarAdapterBinding.btnRedeem.setAlpha(1);
//            holder.mLayoutBazaarAdapterBinding.txtInvalid.setVisibility(View.GONE);
                    holder.mLayoutBazaarAdapterBinding.progressBar.getProgressDrawable().setColorFilter(mContext.getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
                } else if (progress >= items.get(position).getPointsRequired() / 2) {
                    int points = items.get(position).getPointsRequired() - progress;
                    holder.mLayoutBazaarAdapterBinding.btnRedeem.setEnabled(false);
                    DecimalFormat formatter = new DecimalFormat("#,###,###");
                    String formattedPoints = formatter.format(points);
                    holder.mLayoutBazaarAdapterBinding.btnRedeem.setText("Insufficient Points" + " (" + String.valueOf(formattedPoints) + ")");
                    holder.mLayoutBazaarAdapterBinding.progressBar.getProgressDrawable().setColorFilter(mContext.getResources().getColor(R.color.yelold), PorterDuff.Mode.SRC_IN);
                    holder.mLayoutBazaarAdapterBinding.btnRedeem.setAlpha(0.7f);
                } else {
                    int points = items.get(position).getPointsRequired() - progress;
                    holder.mLayoutBazaarAdapterBinding.btnRedeem.setEnabled(false);
                    holder.mLayoutBazaarAdapterBinding.btnRedeem.setAlpha(0.7f);
                    DecimalFormat formatter = new DecimalFormat("#,###,###");
                    String formattedPoints = formatter.format(points);
                    holder.mLayoutBazaarAdapterBinding.btnRedeem.setText("Insufficient Points" + " (" + String.valueOf(formattedPoints) + ")");
                    holder.mLayoutBazaarAdapterBinding.progressBar.getProgressDrawable().setColorFilter(mContext.getResources().getColor(R.color.red), PorterDuff.Mode.SRC_IN);

                }
            }
            /*if (items.get(position).isLocked()){
                holder.mLayoutBazaarAdapterBinding.btnLevelName.setEnabled(false);
                holder.mLayoutBazaarAdapterBinding.btnLevelName.setText(""+items.get(position).getUnlocksAtLevel());
            }else {
                holder.mLayoutBazaarAdapterBinding.btnLevelName.setVisibility(View.GONE);
            }*/
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public void setOnRedeemClickHandler(OnBazaarItemClickHandler onBazaarItemClickHandler) {
        this.onBazaarItemClickHandler = onBazaarItemClickHandler;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setData(List<AvailableOffer> data, Integer totalPendingPoints) {
        items.clear();
        progress = totalPendingPoints;
        items.addAll(data);
    }

    //
    public void addData(List<AvailableOffer> data, Integer totalPendingPoints) {
        items.clear();
        progress = totalPendingPoints;
        items.addAll(data);
    }
//
//    public void removeAll() {
//        items.removeAll(items);
//        notifyDataSetChanged();
//    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final LayoutBazaarAdapterBinding mLayoutBazaarAdapterBinding;

        public ViewHolder(LayoutBazaarAdapterBinding mLayoutBazaarAdapterBinding) {
            super(mLayoutBazaarAdapterBinding.getRoot());
            this.mLayoutBazaarAdapterBinding = mLayoutBazaarAdapterBinding;
        }
    }
}
