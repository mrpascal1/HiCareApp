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
import com.ab.hicarerun.databinding.ItemRecycleAreaUnitBinding;
import com.ab.hicarerun.handler.OnAddActivityClickHandler;
import com.ab.hicarerun.handler.OnSelectServiceClickHandler;
import com.ab.hicarerun.network.models.ActivityModel.AreaActivity;
import com.ab.hicarerun.network.models.ActivityModel.ServiceActivity;
import com.ab.hicarerun.network.models.ChemicalModel.AreaData;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arjun Bhatt on 7/5/2021.
 */
public class ActivityAreaUnitAdapter extends RecyclerView.Adapter<ActivityAreaUnitAdapter.ViewHolder> {
    private OnAddActivityClickHandler onItemClickHandler;
    private final Context mContext;
    List<String> isCheckedList = new ArrayList<>();
    List<AreaActivity> areaList;
    private onRadioClickChanged mRadioClickChanged;
    private OnSelectServiceClickHandler serviceClickHandler;

    public ActivityAreaUnitAdapter(Context context, List<AreaActivity> areaList, onRadioClickChanged mRadioClickChanged) {
        this.areaList = areaList;
        this.mContext = context;
        this.mRadioClickChanged = mRadioClickChanged;
    }

    @NotNull
    @Override
    public ActivityAreaUnitAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        ItemRecycleAreaUnitBinding itemChemicalAreaChildAdapterBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_recycle_area_unit, parent, false);
        return new ActivityAreaUnitAdapter.ViewHolder(itemChemicalAreaChildAdapterBinding);
    }

    @Override
    public void onBindViewHolder(@NotNull ActivityAreaUnitAdapter.ViewHolder holder, final int position) {


        if (areaList.get(position).getStatus() != null) {
            if (areaList.get(position).getStatus().equalsIgnoreCase("yes")) {
                holder.itemChemicalAreaChildAdapterBinding.on.setChecked(true);
                holder.itemChemicalAreaChildAdapterBinding.off.setChecked(false);
            } else if (areaList.get(position).getStatus().equalsIgnoreCase("No")) {
                holder.itemChemicalAreaChildAdapterBinding.on.setChecked(false);
                holder.itemChemicalAreaChildAdapterBinding.off.setChecked(true);
            }
        } else {
            holder.itemChemicalAreaChildAdapterBinding.on.setChecked(true);
            holder.itemChemicalAreaChildAdapterBinding.off.setChecked(false);
        }
        holder.itemChemicalAreaChildAdapterBinding.off.setOnClickListener(view -> {
            holder.itemChemicalAreaChildAdapterBinding.on.setChecked(false);
            holder.itemChemicalAreaChildAdapterBinding.off.setChecked(true);
//            serviceClickHandler.onRadioNoClicked(position);
            mRadioClickChanged.onClickChanged(position, "No");
        });
        holder.itemChemicalAreaChildAdapterBinding.on.setOnClickListener(view -> {
            holder.itemChemicalAreaChildAdapterBinding.on.setChecked(true);
            holder.itemChemicalAreaChildAdapterBinding.off.setChecked(false);
//            serviceClickHandler.onRadioYesClicked(position);
            mRadioClickChanged.onClickChanged(position, "Yes");
        });

        if (holder.itemChemicalAreaChildAdapterBinding.on.isChecked()) {
            mRadioClickChanged.onClickChanged(position, "Yes");
        } else {
            mRadioClickChanged.onClickChanged(position, "No");
        }
        holder.itemChemicalAreaChildAdapterBinding.txtArea.setText(areaList.get(position).getAreaName());
    }

    public void setOnItemClickHandler(OnAddActivityClickHandler onItemClickHandler) {
        this.onItemClickHandler = onItemClickHandler;
    }

    public void setOnSelectServiceClickHandler(OnSelectServiceClickHandler serviceClickHandler) {
        this.serviceClickHandler = serviceClickHandler;
    }

    public void addData(List<AreaActivity> data) {
        areaList.clear();
        areaList.addAll(data);
    }

    @Override
    public int getItemCount() {
        return areaList.size();
    }

    public AreaActivity getItem(int position) {
        return areaList.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemRecycleAreaUnitBinding itemChemicalAreaChildAdapterBinding;

        public ViewHolder(ItemRecycleAreaUnitBinding itemChemicalAreaChildAdapterBinding) {
            super(itemChemicalAreaChildAdapterBinding.getRoot());
            this.itemChemicalAreaChildAdapterBinding = itemChemicalAreaChildAdapterBinding;
        }
    }

    public interface onRadioClickChanged {
        void onClickChanged(int position, String value);
    }
}
