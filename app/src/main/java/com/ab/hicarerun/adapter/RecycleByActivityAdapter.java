package com.ab.hicarerun.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.ItemChemicalAreaChildAdapterBinding;
import com.ab.hicarerun.databinding.ItemRecycleActivityUnitBinding;
import com.ab.hicarerun.handler.OnAddActivityClickHandler;
import com.ab.hicarerun.network.models.ActivityModel.ServiceActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arjun Bhatt on 7/5/2021.
 */
public class RecycleByActivityAdapter extends RecyclerView.Adapter<RecycleByActivityAdapter.ViewHolder> {
    private OnAddActivityClickHandler onItemClickHandler;
    private final Context mContext;
    private List<ServiceActivity> items = null;

    public RecycleByActivityAdapter(Context mContext) {
        this.mContext = mContext;
        if (items == null) {
            items = new ArrayList<>();
        }
    }

    @NotNull
    @Override
    public RecycleByActivityAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        ItemRecycleActivityUnitBinding mItemRecycleActivityUnitBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_recycle_activity_unit, parent, false);
        return new RecycleByActivityAdapter.ViewHolder(mItemRecycleActivityUnitBinding);
    }

    @Override
    public void onBindViewHolder(@NotNull RecycleByActivityAdapter.ViewHolder holder, final int position) {
        try {
            holder.mItemRecycleActivityUnitBinding.txtActivity.setText(items.get(position).getChemical_Name() + " - " + items.get(position).getServiceActivityName());
            holder.mItemRecycleActivityUnitBinding.txtQty.setText(items.get(position).getChemical_Qty() + " " + items.get(position).getChemical_Unit().toLowerCase());

            if (items.get(position).getStatus() != null) {
                holder.mItemRecycleActivityUnitBinding.isBarcodeVerified.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_check_circle_green));
            } else {
                holder.mItemRecycleActivityUnitBinding.isBarcodeVerified.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_check_circle_black));
            }

            holder.mItemRecycleActivityUnitBinding.txtActivity.setTypeface(holder.mItemRecycleActivityUnitBinding.txtActivity.getTypeface(), Typeface.BOLD);
            holder.mItemRecycleActivityUnitBinding.lnrUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickHandler.onAddActivityClick(position);
                }
            });

            holder.mItemRecycleActivityUnitBinding.lnrNotAvailable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickHandler.onNotDoneClick(position);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setOnItemClickHandler(OnAddActivityClickHandler onItemClickHandler) {
        this.onItemClickHandler = onItemClickHandler;
    }

    public void addData(List<ServiceActivity> data) {
        items.clear();
        items.addAll(data);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public ServiceActivity getItem(int position) {
        return items.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemRecycleActivityUnitBinding mItemRecycleActivityUnitBinding;

        public ViewHolder(ItemRecycleActivityUnitBinding mItemRecycleActivityUnitBinding) {
            super(mItemRecycleActivityUnitBinding.getRoot());
            this.mItemRecycleActivityUnitBinding = mItemRecycleActivityUnitBinding;
        }
    }

}
