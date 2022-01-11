package com.ab.hicarerun.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.AddActivityAdapterBinding;
import com.ab.hicarerun.network.models.onsitemodel.RecentActivityDetails;
import com.ab.hicarerun.viewmodel.AccountAreaViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arjun Bhatt on 12/24/2019.
 */
public class ViewActivityAdapter extends RecyclerView.Adapter<ViewActivityAdapter.ViewHolder> {
    private final Context mContext;
    private List<AccountAreaViewModel> items = null;
    List<RecentActivityDetails> serviceList = null;

    public ViewActivityAdapter(Context context, List<RecentActivityDetails> serviceList) {
        if (items == null) {
            items = new ArrayList<>();
        }
        this.serviceList = serviceList;
        this.mContext = context;
    }

    @NotNull
    @Override
    public ViewActivityAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        AddActivityAdapterBinding mAddActivityAdapterBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.add_activity_adapter, parent, false);
        return new ViewActivityAdapter.ViewHolder(mAddActivityAdapterBinding);
    }

    @Override
    public void onBindViewHolder(@NotNull final ViewActivityAdapter.ViewHolder holder, final int position) {
        holder.mAddActivityAdapterBinding.txtServiceType.setText(serviceList.get(position).getServiceType());
        holder.mAddActivityAdapterBinding.radioNo.setEnabled(false);
        holder.mAddActivityAdapterBinding.radioYes.setEnabled(false);
        if (serviceList.get(position).getIsServiceDone()) {
            holder.mAddActivityAdapterBinding.radioYes.setChecked(true);
            holder.mAddActivityAdapterBinding.radioNo.setChecked(false);

        } else {
            holder.mAddActivityAdapterBinding.radioYes.setChecked(false);
            holder.mAddActivityAdapterBinding.radioNo.setChecked(true);
        }
    }


    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public void removeAll() {
        serviceList.removeAll(items);
        notifyDataSetChanged();
    }

    public AccountAreaViewModel getItem(int position) {
        return items.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final AddActivityAdapterBinding mAddActivityAdapterBinding;

        public ViewHolder(AddActivityAdapterBinding mAddActivityAdapterBinding) {
            super(mAddActivityAdapterBinding.getRoot());
            this.mAddActivityAdapterBinding = mAddActivityAdapterBinding;
        }
    }
}
