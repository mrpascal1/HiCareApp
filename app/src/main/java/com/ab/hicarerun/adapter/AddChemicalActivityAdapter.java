package com.ab.hicarerun.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.AddChemicalActivityAdapterBinding;
import com.ab.hicarerun.handler.OnAddActivityClickHandler;
import com.ab.hicarerun.handler.OnSelectServiceClickHandler;
import com.ab.hicarerun.network.models.chemicalmodel.ActivityData;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arjun Bhatt on 6/27/2021.
 */
public class AddChemicalActivityAdapter extends RecyclerView.Adapter<AddChemicalActivityAdapter.ViewHolder> {
    private OnAddActivityClickHandler onItemClickHandler;
    private OnSelectServiceClickHandler serviceClickHandler;
    private final Context mContext;
    private List<ActivityData> items = null;
    private onRadioClickChanged mRadioClickChanged;
    List<String> isCheckedList = new ArrayList<>();

    public AddChemicalActivityAdapter(Context context, List<ActivityData> items, onRadioClickChanged mRadioClickChanged) {
        if (items == null) {
            items = new ArrayList<>();
        }
        this.items = items;
        this.mContext = context;
        this.mRadioClickChanged = mRadioClickChanged;
    }

    @NotNull
    @Override
    public AddChemicalActivityAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        AddChemicalActivityAdapterBinding mAddActivityAdapterBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.add_chemical_activity_adapter, parent, false);
        return new AddChemicalActivityAdapter.ViewHolder(mAddActivityAdapterBinding);
    }

    @Override
    public void onBindViewHolder(@NotNull final AddChemicalActivityAdapter.ViewHolder holder, final int position) {
        try {
            if (isCheckedList.isEmpty()) {
                for (int i = 0; i < items.size(); i++) {
                    isCheckedList.add("false");
                }
            }

            if (items.get(position).getStatus() != null && items.get(position).getStatus().equals("Yes")) {
                holder.mAddActivityAdapterBinding.radioYes.setChecked(true);
                holder.mAddActivityAdapterBinding.radioYes.setEnabled(false);
            } else if(items.get(position).getStatus() != null && items.get(position).getStatus().equals("No")){
                holder.mAddActivityAdapterBinding.radioNo.setChecked(true);
                holder.mAddActivityAdapterBinding.radioYes.setEnabled(false);
            }

            holder.mAddActivityAdapterBinding.radioYes.setOnClickListener(view -> {
                isCheckedList.set(position, "true");
                holder.mAddActivityAdapterBinding.radioNo.setChecked(false);
                holder.mAddActivityAdapterBinding.radioYes.setChecked(true);
                serviceClickHandler.onRadioYesClicked(position);
                mRadioClickChanged.onClickChanged(position, isCheckedList);
            });
            holder.mAddActivityAdapterBinding.radioNo.setOnClickListener(view -> {
                isCheckedList.set(position, "false");
                holder.mAddActivityAdapterBinding.radioNo.setChecked(true);
                holder.mAddActivityAdapterBinding.radioYes.setChecked(false);
                serviceClickHandler.onRadioNoClicked(position);
                mRadioClickChanged.onClickChanged(position, isCheckedList);
            });


            if (holder.mAddActivityAdapterBinding.radioYes.isChecked()) {
                isCheckedList.set(position, "true");
                mRadioClickChanged.onClickChanged(position, isCheckedList);
            } else {
                isCheckedList.set(position, "false");
                mRadioClickChanged.onClickChanged(position, isCheckedList);
            }

            holder.mAddActivityAdapterBinding.txtServiceType.setText(items.get(position).getServiceCode());
            holder.mAddActivityAdapterBinding.txtActivity.setText(items.get(position).getServiceActivity());
            holder.mAddActivityAdapterBinding.txtChemicalUsed.setText("(" + items.get(position).getChemicalName() + ")");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setOnItemClickHandler(OnAddActivityClickHandler onItemClickHandler) {
        this.onItemClickHandler = onItemClickHandler;
    }

    public void setOnSelectServiceClickHandler(OnSelectServiceClickHandler serviceClickHandler) {
        this.serviceClickHandler = serviceClickHandler;
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public void removeAll() {
        items.removeAll(items);
        notifyDataSetChanged();
    }

    public ActivityData getItem(int position) {
        return items.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final AddChemicalActivityAdapterBinding mAddActivityAdapterBinding;

        public ViewHolder(AddChemicalActivityAdapterBinding mAddActivityAdapterBinding) {
            super(mAddActivityAdapterBinding.getRoot());
            this.mAddActivityAdapterBinding = mAddActivityAdapterBinding;
        }
    }

    public interface onRadioClickChanged {
        void onClickChanged(int position, List<String> isCheckedList);
    }
}

