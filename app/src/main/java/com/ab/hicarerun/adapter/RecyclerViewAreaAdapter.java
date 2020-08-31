package com.ab.hicarerun.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.LayoutAreaAdapterBinding;
import com.ab.hicarerun.handler.OnListItemClickHandler;
import com.ab.hicarerun.network.models.OnSiteModel.OnSiteArea;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arjun Bhatt on 9/23/2019.
 */
public class RecyclerViewAreaAdapter extends RecyclerView.Adapter<RecyclerViewAreaAdapter.ViewHolder> {
    private OnListItemClickHandler onItemClickHandler;
    private final Context mContext;
    private List<OnSiteArea> items = null;
    private List<String> areaList = null;

    public RecyclerViewAreaAdapter(Context context) {
        if (items == null) {
            items = new ArrayList<>();
        }
        if (areaList == null) {
            areaList = new ArrayList<>();
        }
        this.mContext = context;
    }


    @NotNull
    @Override
    public RecyclerViewAreaAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        LayoutAreaAdapterBinding mLayoutAreaAdapterBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.layout_area_adapter, parent, false);
        return new RecyclerViewAreaAdapter.ViewHolder(mLayoutAreaAdapterBinding);
    }


    @Override
    public void onBindViewHolder(@NotNull RecyclerViewAreaAdapter.ViewHolder holder, final int position) {
        try {
            int[] attrs = new int[]{R.attr.selectableItemBackground};
            TypedArray typedArray = mContext.obtainStyledAttributes(attrs);
            int backgroundResource = typedArray.getResourceId(0, 0);
            holder.mLayoutAreaAdapterBinding.lnrloc.setBackgroundResource(backgroundResource);
            holder.mLayoutAreaAdapterBinding.txtBranch.setText(areaList.get(position));
            holder.itemView.setOnClickListener(v -> onItemClickHandler.onItemClick(position));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setOnItemClickHandler(OnListItemClickHandler onItemClickHandler) {
        this.onItemClickHandler = onItemClickHandler;
    }

    @Override
    public int getItemCount() {
        return areaList.size();
    }

    public void setData(List<OnSiteArea> data) {
        items.clear();
        items.addAll(data);
    }

    public void setArea(List<String> area) {
        areaList.clear();
        areaList.addAll(area);
    }

    public void removeAll() {
        items.removeAll(items);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final LayoutAreaAdapterBinding mLayoutAreaAdapterBinding;

        public ViewHolder(LayoutAreaAdapterBinding mLayoutAreaAdapterBinding) {
            super(mLayoutAreaAdapterBinding.getRoot());
            this.mLayoutAreaAdapterBinding = mLayoutAreaAdapterBinding;
        }
    }
}
