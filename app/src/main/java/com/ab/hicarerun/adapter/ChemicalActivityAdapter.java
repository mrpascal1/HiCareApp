package com.ab.hicarerun.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.ItemChemicalAreaChildAdapterBinding;
import com.ab.hicarerun.handler.OnAddActivityClickHandler;
import com.ab.hicarerun.handler.OnAddChemicalActivity;
import com.ab.hicarerun.network.models.ChemicalModel.AreaData;
import com.ab.hicarerun.network.models.ChemicalModel.ServiceChemicalData;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arjun Bhatt on 6/28/2021.
 */
public class ChemicalActivityAdapter extends RecyclerView.Adapter<ChemicalActivityAdapter.ViewHolder> {
    private OnAddActivityClickHandler onItemClickHandler;
    private final Context mContext;
    private List<AreaData> items = null;

    public ChemicalActivityAdapter(Context mContext) {
        this.mContext = mContext;
        if (items == null) {
            items = new ArrayList<>();
        }
    }

    @NotNull
    @Override
    public ChemicalActivityAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        ItemChemicalAreaChildAdapterBinding itemChemicalAreaChildAdapterBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_chemical_area_child_adapter, parent, false);
        return new ChemicalActivityAdapter.ViewHolder(itemChemicalAreaChildAdapterBinding);
    }

    @Override
    public void onBindViewHolder(@NotNull ChemicalActivityAdapter.ViewHolder holder, final int position) {
        holder.itemChemicalAreaChildAdapterBinding.txtSubArea.setText(items.get(position).getAreaName());
        holder.itemChemicalAreaChildAdapterBinding.txtSubArea.setTypeface(holder.itemChemicalAreaChildAdapterBinding.txtSubArea.getTypeface(), Typeface.BOLD);
        holder.itemChemicalAreaChildAdapterBinding.txtServiceType.setText(items.get(position).getServices());
        holder.itemChemicalAreaChildAdapterBinding.txtFloor.setText(String.valueOf(items.get(position).getFloorNo()));
        holder.itemChemicalAreaChildAdapterBinding.lnrUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickHandler.onAddActivityClick(position);
            }
        });

        holder.itemChemicalAreaChildAdapterBinding.lnrNotAvailable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickHandler.onNotDoneClick(position);
            }
        });

    }

    public void setOnItemClickHandler(OnAddActivityClickHandler onItemClickHandler) {
        this.onItemClickHandler = onItemClickHandler;
    }

    public void addData(List<AreaData> data) {
        items.clear();
        items.addAll(data);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public AreaData getItem(int position) {
        return items.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemChemicalAreaChildAdapterBinding itemChemicalAreaChildAdapterBinding;

        public ViewHolder(ItemChemicalAreaChildAdapterBinding itemChemicalAreaChildAdapterBinding) {
            super(itemChemicalAreaChildAdapterBinding.getRoot());
            this.itemChemicalAreaChildAdapterBinding = itemChemicalAreaChildAdapterBinding;
        }
    }

}
