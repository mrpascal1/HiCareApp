package com.ab.hicarerun.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.TimeUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.LayoutOfferHistoryAdapterBinding;
import com.ab.hicarerun.databinding.LayoutOffersAdapterBinding;
import com.ab.hicarerun.handler.OnListItemClickHandler;
import com.ab.hicarerun.network.models.RewardsModel.AvailableOffer;
import com.ab.hicarerun.network.models.RewardsModel.RedeemedOffer;
import com.ab.hicarerun.utils.AppUtils;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arjun Bhatt on 3/3/2020.
 */
public class RecycleOfferHistoryAdapter extends RecyclerView.Adapter<RecycleOfferHistoryAdapter.ViewHolder> {
    private OnListItemClickHandler onItemClickHandler;
    private int progress = 0;
    private final Context mContext;
    private List<RedeemedOffer> items = null;

    public RecycleOfferHistoryAdapter(Context context) {
        if (items == null) {
            items = new ArrayList<>();
        }
        this.mContext = context;
    }


    @NotNull
    @Override
    public RecycleOfferHistoryAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        LayoutOfferHistoryAdapterBinding mLayoutOffersAdapterBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.layout_offer_history_adapter, parent, false);
        return new RecycleOfferHistoryAdapter.ViewHolder(mLayoutOffersAdapterBinding);
    }


    @Override
    public void onBindViewHolder(@NotNull RecycleOfferHistoryAdapter.ViewHolder holder, final int position) {
        try {
            Picasso.get().load(items.get(position).getImageURL()).into(holder.mLayoutOffersAdapterBinding.imgOffer);
            holder.mLayoutOffersAdapterBinding.txtTitle.setText(items.get(position).getTitle());
            holder.mLayoutOffersAdapterBinding.txtDescription.setText(items.get(position).getDescription());
            holder.mLayoutOffersAdapterBinding.txtRedeemedPoints.setText("Redeemed Pts: " + items.get(position).getPointsUsed());
            try {
                holder.mLayoutOffersAdapterBinding.txtRedeemedOn.setText("Redeemed On: " + AppUtils.reFormatRedeemedDate(items.get(position).getOfferRedeemedDateString(), "dd/MM/yyyy"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setData(List<RedeemedOffer> data) {
        items.clear();
        items.addAll(data);
    }

    //
    public void addData(List<RedeemedOffer> data) {
        items.clear();
        items.addAll(data);
    }
//
//    public void removeAll() {
//        items.removeAll(items);
//        notifyDataSetChanged();
//    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final LayoutOfferHistoryAdapterBinding mLayoutOffersAdapterBinding;

        public ViewHolder(LayoutOfferHistoryAdapterBinding mLayoutOffersAdapterBinding) {
            super(mLayoutOffersAdapterBinding.getRoot());
            this.mLayoutOffersAdapterBinding = mLayoutOffersAdapterBinding;
        }
    }
}
