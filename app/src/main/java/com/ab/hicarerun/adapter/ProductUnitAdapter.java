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
import com.ab.hicarerun.databinding.ProductUnitsAdapterBinding;
import com.ab.hicarerun.handler.OnBazaarItemClickHandler;
import com.ab.hicarerun.handler.OnListItemClickHandler;
import com.ab.hicarerun.network.models.ProductModel.ServicePlanUnits;
import com.ab.hicarerun.network.models.RewardsModel.AvailableOffer;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arjun Bhatt on 5/28/2020.
 */
public class ProductUnitAdapter extends RecyclerView.Adapter<ProductUnitAdapter.ViewHolder> {
    private OnListItemClickHandler onItemClickHandler;
    private final Context mContext;
    private List<ServicePlanUnits> items = null;
    private int selectedPos = 0;

    public ProductUnitAdapter(Context context) {
        if (items == null) {
            items = new ArrayList<>();
        }
        this.mContext = context;
    }

    @NotNull
    @Override
    public ProductUnitAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        ProductUnitsAdapterBinding mProductUnitsAdapterBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.product_units_adapter, parent, false);
        return new ProductUnitAdapter.ViewHolder(mProductUnitsAdapterBinding);
    }


    @Override
    public void onBindViewHolder(@NotNull ProductUnitAdapter.ViewHolder holder, final int position) {
        try {
            if(selectedPos == position){
                holder.mProductUnitsAdapterBinding.cardUnits.setCardBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
                holder.mProductUnitsAdapterBinding.lnrUnits.setBackground(mContext.getResources().getDrawable(R.drawable.white_round_border));
                holder.mProductUnitsAdapterBinding.txtUnits.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
            }else {
                holder.mProductUnitsAdapterBinding.cardUnits.setCardBackgroundColor(mContext.getResources().getColor(R.color.smoke_gray));
                holder.mProductUnitsAdapterBinding.lnrUnits.setBackgroundColor(mContext.getResources().getColor(R.color.smoke_gray));
                holder.mProductUnitsAdapterBinding.txtUnits.setTextColor(mContext.getResources().getColor(R.color.black));
            }
            holder.mProductUnitsAdapterBinding.txtUnits.setText(items.get(position).getUnit());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickHandler.onItemClick(position);
                    selectedPos = position;
                    notifyDataSetChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setOnItemClickHandler(OnListItemClickHandler onItemClickHandler) {
        this.onItemClickHandler = onItemClickHandler;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setData(List<ServicePlanUnits> data) {
        items.clear();
        items.addAll(data);
    }
//
//    public void removeAll() {
//        items.removeAll(items);
//        notifyDataSetChanged();
//    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ProductUnitsAdapterBinding mProductUnitsAdapterBinding;

        public ViewHolder(ProductUnitsAdapterBinding mProductUnitsAdapterBinding) {
            super(mProductUnitsAdapterBinding.getRoot());
            this.mProductUnitsAdapterBinding = mProductUnitsAdapterBinding;
        }
    }
}
