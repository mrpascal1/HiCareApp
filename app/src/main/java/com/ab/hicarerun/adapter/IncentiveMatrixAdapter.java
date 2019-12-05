package com.ab.hicarerun.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.IncentiveMatrixAdapterBinding;
import com.ab.hicarerun.databinding.OnJobTechniciansListBinding;
import com.ab.hicarerun.handler.OnCaptureListItemClickHandler;
import com.ab.hicarerun.network.models.IncentiveModel.Matrix;
import com.ab.hicarerun.network.models.TechnicianGroomingModel.TechGroom;
import com.ab.hicarerun.viewmodel.GroomingViewModel;
import com.ab.hicarerun.viewmodel.IncentiveMatrixViewModel;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arjun Bhatt on 10/16/2019.
 */
public class IncentiveMatrixAdapter extends RecyclerView.Adapter<IncentiveMatrixAdapter.ViewHolder> {


    private List<IncentiveMatrixViewModel> items = null;

    public IncentiveMatrixAdapter() {
        if (items == null) {
            items = new ArrayList<>();
        }
    }


    @Override
    public IncentiveMatrixAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        IncentiveMatrixAdapterBinding mIncentiveMatrixAdapterBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.incentive_matrix_adapter, parent, false);
        return new IncentiveMatrixAdapter.ViewHolder(mIncentiveMatrixAdapterBinding);
    }


    @Override
    public void onBindViewHolder(IncentiveMatrixAdapter.ViewHolder holder, final int position) {
        holder.mIncentiveMatrixAdapterBinding.txtMatrix.setText(items.get(position).getMatrix());
        holder.mIncentiveMatrixAdapterBinding.txtIncentive.setText(items.get(position).getIncentive());
    }


    @Override
    public int getItemCount() {
        return items.size();

    }

    public void setData(List<Matrix> data) {
        items.clear();
        for (int index = 0; index < data.size(); index++) {
            IncentiveMatrixViewModel incentiveViewModel = new IncentiveMatrixViewModel();
            incentiveViewModel.clone(data.get(index));
            items.add(incentiveViewModel);
        }
    }

    public void addData(List<Matrix> data) {
        items.clear();
        for (int index = 0; index < data.size(); index++) {
            IncentiveMatrixViewModel incentiveViewModel = new IncentiveMatrixViewModel();
            incentiveViewModel.clone(data.get(index));
            items.add(incentiveViewModel);
        }
    }

    public void removeAll() {
        items.removeAll(items);
        notifyDataSetChanged();
    }

    public IncentiveMatrixViewModel getItem(int position) {
        return items.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final IncentiveMatrixAdapterBinding mIncentiveMatrixAdapterBinding;

        public ViewHolder(IncentiveMatrixAdapterBinding mIncentiveMatrixAdapterBinding) {
            super(mIncentiveMatrixAdapterBinding.getRoot());
            this.mIncentiveMatrixAdapterBinding = mIncentiveMatrixAdapterBinding;
        }
    }
}
