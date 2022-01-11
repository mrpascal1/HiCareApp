package com.ab.hicarerun.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.HicareProductListAdapterBinding;
import com.ab.hicarerun.handler.OnListItemClickHandler;
import com.ab.hicarerun.network.models.productmodel.ProductData;
import com.ab.hicarerun.viewmodel.ProductViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arjun Bhatt on 5/28/2020.
 */
public class HicareProductAdapter extends RecyclerView.Adapter<HicareProductAdapter.ViewHolder> {

    private OnListItemClickHandler onItemClickHandler;
    private final Context mContext;
    private static List<ProductViewModel> items = null;

    public HicareProductAdapter(Context context) {
        if (items == null) {
            items = new ArrayList<>();
        }
        this.mContext = context;
    }

    @NotNull
    @Override
    public HicareProductAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        HicareProductListAdapterBinding mHicareProductListAdapterBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.hicare_product_list_adapter, parent, false);
        return new HicareProductAdapter.ViewHolder(mHicareProductListAdapterBinding);
    }

    @Override
    public void onBindViewHolder(@NotNull final HicareProductAdapter.ViewHolder holder, final int position) {
        try {
            holder.mHicareProductListAdapterBinding.txtActualAmount.setText("\u20B9" + " " + items.get(position).getServicePlanUnits().get(0).getPrice());
            holder.mHicareProductListAdapterBinding.txtTitle.setText(items.get(position).getPlan_Name());
            holder.mHicareProductListAdapterBinding.txtDescription.setText(items.get(position).getService_Description());
            holder.mHicareProductListAdapterBinding.txtDiscount.setText("Save " + "\u20B9" + " " + items.get(position).getServicePlanUnits().get(0).getTotalDiscount());
            holder.mHicareProductListAdapterBinding.txtUnit.setText(items.get(position).getServicePlanUnits().get(0).getUnit());
            holder.mHicareProductListAdapterBinding.txtDiscountedAmount.setText("\u20B9" + " " + items.get(position).getServicePlanUnits().get(0).getDiscountedPrice());
            holder.mHicareProductListAdapterBinding.txtActualAmount.setPaintFlags(holder.mHicareProductListAdapterBinding.txtActualAmount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickHandler.onItemClick(position);
                }
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


    public void setData(List<ProductData> data) {
        items.clear();
        for (int index = 0; index < data.size(); index++) {
            ProductViewModel productViewModel = new ProductViewModel();
            productViewModel.clone(data.get(index));
            items.add(productViewModel);
        }
    }

    public void addData(List<ProductData> data) {
        items.clear();
        for (int index = 0; index < data.size(); index++) {
            ProductViewModel productViewModel = new ProductViewModel();
            productViewModel.clone(data.get(index));
            items.add(productViewModel);
        }
    }

    public ProductViewModel getItem(int position) {
        return items.get(position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private final HicareProductListAdapterBinding mHicareProductListAdapterBinding;

        public ViewHolder(HicareProductListAdapterBinding mHicareProductListAdapterBinding) {
            super(mHicareProductListAdapterBinding.getRoot());
            this.mHicareProductListAdapterBinding = mHicareProductListAdapterBinding;
        }
    }

}

