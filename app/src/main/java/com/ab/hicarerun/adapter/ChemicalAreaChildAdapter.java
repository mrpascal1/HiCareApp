package com.ab.hicarerun.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.ItemChemicalAreaChildAdapterBinding;
import com.ab.hicarerun.handler.OnAddChemicalActivity;
import com.ab.hicarerun.network.models.chemicalmodel.AreaData;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arjun Bhatt on 6/27/2021.
 */
public class ChemicalAreaChildAdapter extends RecyclerView.Adapter<ChemicalAreaChildAdapter.ViewHolder> {
    private OnAddChemicalActivity mOnAddChemicalActivity;
    private final Context mContext;
    private List<AreaData> items = null;
    private int parentPos = 0;

    public ChemicalAreaChildAdapter(Context mContext, List<AreaData> items, int position) {
        this.mContext = mContext;
        if (items == null) {
            items = new ArrayList<>();
        }
        this.items = items;
        parentPos = position;
    }

    @NotNull
    @Override
    public ChemicalAreaChildAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        ItemChemicalAreaChildAdapterBinding itemChemicalAreaChildAdapterBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_chemical_area_child_adapter, parent, false);
        return new ChemicalAreaChildAdapter.ViewHolder(itemChemicalAreaChildAdapterBinding);
    }

    @Override
    public void onBindViewHolder(@NotNull ChemicalAreaChildAdapter.ViewHolder holder, final int position) {

    }

    public void setmOnAddChemicalActivity(OnAddChemicalActivity mOnAreaClickedHandler) {
        this.mOnAddChemicalActivity = mOnAreaClickedHandler;
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
