package com.ab.hicarerun.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.LayoutReferralInterestAdapterBinding;
import com.ab.hicarerun.databinding.ReferralListAdapterBinding;
import com.ab.hicarerun.handler.OnDeleteListItemClickHandler;
import com.ab.hicarerun.handler.OnListItemClickHandler;
import com.ab.hicarerun.network.models.ReferralModel.ReferralList;
import com.ab.hicarerun.network.models.ReferralModel.ReferralService;
import com.ab.hicarerun.viewmodel.ReferralListViewModel;
import com.ab.hicarerun.viewmodel.ReferralServiceViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arjun Bhatt on 7/9/2020.
 */
public class ReferralServiceAdapter extends RecyclerView.Adapter<ReferralServiceAdapter.ViewHolder> {

    private final Context mContext;
    private OnListItemClickHandler onItemClickHandler;
    private List<ReferralServiceViewModel> items = null;
    private OnCheckChanged onCheckChanged;

    public ReferralServiceAdapter(Context context, OnCheckChanged onCheckChanged) {
        if (items == null) {
            items = new ArrayList<>();
        }
        this.mContext = context;
        this.onCheckChanged = onCheckChanged;
    }


    @NotNull
    @Override
    public ReferralServiceAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        LayoutReferralInterestAdapterBinding mReferralListAdapterBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.layout_referral_interest_adapter, parent, false);
        return new ReferralServiceAdapter.ViewHolder(mReferralListAdapterBinding);
    }


    @Override
    public void onBindViewHolder(ReferralServiceAdapter.ViewHolder holder, final int position) {
        try {
            holder.mReferralListAdapterBinding.txtName.setText(items.get(position).getDisplayName());

            holder.mReferralListAdapterBinding.chkSelected.setOnCheckedChangeListener((buttonView, isChecked) -> onCheckChanged.onChecked(position, isChecked));

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

    public void setData(List<ReferralService> data) {
        items.clear();
        for (int index = 0; index < data.size(); index++) {
            ReferralServiceViewModel referralListViewModel = new ReferralServiceViewModel();
            referralListViewModel.clone(data.get(index));
            items.add(referralListViewModel);
        }
    }

    public void addData(List<ReferralService> data) {
        items.clear();
        for (int index = 0; index < data.size(); index++) {
            ReferralServiceViewModel referralListViewModel = new ReferralServiceViewModel();
            referralListViewModel.clone(data.get(index));
            items.add(referralListViewModel);
        }
    }

    public void removeAll() {
        items.removeAll(items);
        notifyDataSetChanged();
    }

    public ReferralServiceViewModel getItem(int position) {
        return items.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final LayoutReferralInterestAdapterBinding mReferralListAdapterBinding;

        public ViewHolder(LayoutReferralInterestAdapterBinding mReferralListAdapterBinding) {
            super(mReferralListAdapterBinding.getRoot());
            this.mReferralListAdapterBinding = mReferralListAdapterBinding;
        }
    }

    public interface OnCheckChanged {
        void onChecked(int position, boolean isChecked);
    }
}
