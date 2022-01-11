package com.ab.hicarerun.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.LayoutReferralRelationAdapterBinding;
import com.ab.hicarerun.handler.OnListItemClickHandler;
import com.ab.hicarerun.network.models.referralmodel.ReferralRelation;
import com.ab.hicarerun.viewmodel.ReferralRelationViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arjun Bhatt on 7/9/2020.
 */
public class ReferralRelationAdapter extends RecyclerView.Adapter<ReferralRelationAdapter.ViewHolder> {

    private final Context mContext;
    private OnListItemClickHandler onItemClickHandler;
    private List<ReferralRelationViewModel> items = null;
    private int selectedPos = -1;

    public ReferralRelationAdapter(Context context) {
        if (items == null) {
            items = new ArrayList<>();
        }

        this.mContext = context;
    }


    @NotNull
    @Override
    public ReferralRelationAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        LayoutReferralRelationAdapterBinding mReferralListAdapterBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.layout_referral_relation_adapter, parent, false);
        return new ReferralRelationAdapter.ViewHolder(mReferralListAdapterBinding);
    }


    @Override
    public void onBindViewHolder(ReferralRelationAdapter.ViewHolder holder, final int position) {
        try {
            if (selectedPos == position) {
                holder.mReferralListAdapterBinding.chkSelected.setChecked(true);
            } else {
                holder.mReferralListAdapterBinding.chkSelected.setChecked(false);
            }
            holder.mReferralListAdapterBinding.chkSelected.setOnClickListener(v -> {
                selectedPos = position;
                onItemClickHandler.onItemClick(position);
                notifyDataSetChanged();
            });

            holder.mReferralListAdapterBinding.txtName.setText(items.get(position).getDisplayName());
        }catch (Exception e){
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

    public void setData(List<ReferralRelation> data) {
        items.clear();
        for (int index = 0; index < data.size(); index++) {
            ReferralRelationViewModel referralListViewModel = new ReferralRelationViewModel();
            referralListViewModel.clone(data.get(index));
            items.add(referralListViewModel);
        }
    }

    public void addData(List<ReferralRelation> data) {
        items.clear();
        for (int index = 0; index < data.size(); index++) {
            ReferralRelationViewModel referralListViewModel = new ReferralRelationViewModel();
            referralListViewModel.clone(data.get(index));
            items.add(referralListViewModel);
        }
    }

    public void removeAll() {
        items.removeAll(items);
        notifyDataSetChanged();
    }

    public ReferralRelationViewModel getItem(int position) {
        return items.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final LayoutReferralRelationAdapterBinding mReferralListAdapterBinding;

        public ViewHolder(LayoutReferralRelationAdapterBinding mReferralListAdapterBinding) {
            super(mReferralListAdapterBinding.getRoot());
            this.mReferralListAdapterBinding = mReferralListAdapterBinding;
        }
    }
}
