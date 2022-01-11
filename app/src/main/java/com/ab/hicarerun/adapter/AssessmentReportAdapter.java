package com.ab.hicarerun.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.LayoutAssessReportAdapterBinding;
import com.ab.hicarerun.handler.OnCheckListItemClickHandler;
import com.ab.hicarerun.network.models.selfassessmodel.AssessmentReport;
import com.ab.hicarerun.viewmodel.AssessmentReportViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arjun Bhatt on 5/21/2020.
 */
public class AssessmentReportAdapter extends RecyclerView.Adapter<AssessmentReportAdapter.ViewHolder> {

    private OnCheckListItemClickHandler onItemClickHandler;
    private final Context mContext;
    private static List<AssessmentReportViewModel> items = null;

    public AssessmentReportAdapter(Context context) {
        if (items == null) {
            items = new ArrayList<>();
        }
        this.mContext = context;
    }


    @NotNull
    @Override
    public AssessmentReportAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        LayoutAssessReportAdapterBinding mLayoutAssessReportAdapterBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.layout_assess_report_adapter, parent, false);
        return new AssessmentReportAdapter.ViewHolder(mLayoutAssessReportAdapterBinding);
    }

    @Override
    public void onBindViewHolder(@NotNull final AssessmentReportAdapter.ViewHolder holder, final int position) {
        try {
            if(!items.get(position).getOptionText().equals("")){
                holder.mLayoutAssessReportAdapterBinding.optionText.setVisibility(View.VISIBLE);
//                double optText = Double.parseDouble(items.get(position).getOptionText());
//                double roundedOpt = Math.round(optText * 10) / 10.0;
//                holder.mLayoutAssessReportAdapterBinding.optionText.setText(String.valueOf(roundedOpt)+"\u00B0"+"F");
                holder.mLayoutAssessReportAdapterBinding.optionText.setText(items.get(position).getOptionText());
            }else {
                holder.mLayoutAssessReportAdapterBinding.optionText.setVisibility(View.GONE);
            }
            holder.mLayoutAssessReportAdapterBinding.txtTitle.setText(items.get(position).getDisplayTitle());
            if(items.get(position).getSelected()){
                holder.mLayoutAssessReportAdapterBinding.imgChecked.setImageResource(R.drawable.ic_completion_tick);
            }else {
                holder.mLayoutAssessReportAdapterBinding.imgChecked.setImageResource(R.drawable.ic_cancel_nps);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return items.size();

    }

    public void setOnItemClickHandler(OnCheckListItemClickHandler onItemClickHandler) {
        this.onItemClickHandler = onItemClickHandler;
    }


    public void setData(List<AssessmentReport> data) {
        items.clear();
        for (int index = 0; index < data.size(); index++) {
            AssessmentReportViewModel assessmentReportViewModel = new AssessmentReportViewModel();
            assessmentReportViewModel.clone(data.get(index));
            items.add(assessmentReportViewModel);
        }
    }

    public void addData(List<AssessmentReport> data) {
        items.clear();
        for (int index = 0; index < data.size(); index++) {
            AssessmentReportViewModel assessmentReportViewModel = new AssessmentReportViewModel();
            assessmentReportViewModel.clone(data.get(index));
            items.add(assessmentReportViewModel);
        }
    }

    public AssessmentReportViewModel getItem(int position) {
        return items.get(position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private final LayoutAssessReportAdapterBinding mLayoutAssessReportAdapterBinding;

        public ViewHolder(LayoutAssessReportAdapterBinding mLayoutAssessReportAdapterBinding) {
            super(mLayoutAssessReportAdapterBinding.getRoot());
            this.mLayoutAssessReportAdapterBinding = mLayoutAssessReportAdapterBinding;
        }
    }
    public interface OnCheckChanged {
        void onChecked(int position, boolean isChecked, String temperature);
    }
}
