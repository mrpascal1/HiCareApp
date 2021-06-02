package com.ab.hicarerun.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.IncentiveMatrixAdapterBinding;
import com.ab.hicarerun.databinding.LayoutIncentiveListAdapterBinding;
import com.ab.hicarerun.network.models.IncentiveModel.IncentiiveDetailList;
import com.ab.hicarerun.network.models.IncentiveModel.IncentiveCriteriaList;
import com.ab.hicarerun.viewmodel.IncentiveDetailViewModel;
import com.ab.hicarerun.viewmodel.IncentiveMatrixViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arjun Bhatt on 7/10/2020.
 */
public class IncentiveDetailsAdapter extends RecyclerView.Adapter<IncentiveDetailsAdapter.ViewHolder> {


    private List<IncentiveDetailViewModel> items = null;
    private int progress = 0;
    private Context mContext;

    public IncentiveDetailsAdapter(Context mContext) {
        if (items == null) {
            items = new ArrayList<>();
        }
        this.mContext = mContext;
    }


    @NotNull
    @Override
    public IncentiveDetailsAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        LayoutIncentiveListAdapterBinding mLayoutIncentiveListAdapterBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.layout_incentive_list_adapter, parent, false);
        return new IncentiveDetailsAdapter.ViewHolder(mLayoutIncentiveListAdapterBinding);
    }


    @Override
    public void onBindViewHolder(@NotNull IncentiveDetailsAdapter.ViewHolder holder, final int position) {
        try {
            holder.mLayoutIncentiveListAdapterBinding.txtDescription.setText(items.get(position).getDescription());
            holder.mLayoutIncentiveListAdapterBinding.txtAmount.setText(String.valueOf(items.get(position).getAmount())+" Rs.");
            holder.mLayoutIncentiveListAdapterBinding.txtPoints.setText(String.valueOf(items.get(position).getPoints())+" Pts.");
            holder.mLayoutIncentiveListAdapterBinding.progressBar.setMax(100);
            progress = items.get(position).getPoints();
            holder.mLayoutIncentiveListAdapterBinding.progressBar.setProgress(progress);

            if (progress >= 100) {
                holder.mLayoutIncentiveListAdapterBinding.progressBar.getProgressDrawable().setColorFilter(mContext.getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
            } else if (progress >= 50) {
                holder.mLayoutIncentiveListAdapterBinding.progressBar.getProgressDrawable().setColorFilter(mContext.getResources().getColor(R.color.yelold), PorterDuff.Mode.SRC_IN);
            } else {
                holder.mLayoutIncentiveListAdapterBinding.progressBar.getProgressDrawable().setColorFilter(mContext.getResources().getColor(R.color.lightRed), PorterDuff.Mode.SRC_IN);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setData(List<IncentiiveDetailList> data) {
        items.clear();
        for (int index = 0; index < data.size(); index++) {
            IncentiveDetailViewModel incentiveViewModel = new IncentiveDetailViewModel();
            incentiveViewModel.clone(data.get(index));
            items.add(incentiveViewModel);
        }
    }

    public void addData(List<IncentiiveDetailList> data) {
        items.clear();
        for (int index = 0; index < data.size(); index++) {
            IncentiveDetailViewModel incentiveViewModel = new IncentiveDetailViewModel();
            incentiveViewModel.clone(data.get(index));
            items.add(incentiveViewModel);
        }
    }

    public void removeAll() {
        items.removeAll(items);
        notifyDataSetChanged();
    }

    public IncentiveDetailViewModel getItem(int position) {
        return items.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final LayoutIncentiveListAdapterBinding mLayoutIncentiveListAdapterBinding;

        public ViewHolder(LayoutIncentiveListAdapterBinding mLayoutIncentiveListAdapterBinding) {
            super(mLayoutIncentiveListAdapterBinding.getRoot());
            this.mLayoutIncentiveListAdapterBinding = mLayoutIncentiveListAdapterBinding;
        }
    }
}
