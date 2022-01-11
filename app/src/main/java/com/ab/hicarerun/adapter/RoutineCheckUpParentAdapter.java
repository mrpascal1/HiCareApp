package com.ab.hicarerun.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.LayoutRoutineCheckAdapterBinding;
import com.ab.hicarerun.handler.OnConsultationClickHandler;
import com.ab.hicarerun.network.models.routinemodel.RoutineOption;
import com.ab.hicarerun.network.models.routinemodel.RoutineQuestion;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arjun Bhatt on 8/18/2020.
 */
public class RoutineCheckUpParentAdapter extends RecyclerView.Adapter<RoutineCheckUpParentAdapter.ViewHolder> {
    private OnConsultationClickHandler onItemClickHandler;
    private final Context mContext;
    private List<RoutineQuestion> items = null;
    private List<RoutineOption> valueList = new ArrayList<>();
    private OnOptionClicked onOptionClicked;


    public RoutineCheckUpParentAdapter(Context context, OnOptionClicked onOptionClicked) {
        if (items == null) {
            items = new ArrayList<>();
        }
        this.onOptionClicked = onOptionClicked;
        this.mContext = context;
    }

    @NotNull
    @Override
    public RoutineCheckUpParentAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        LayoutRoutineCheckAdapterBinding mLayoutRoutineCheckAdapterBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.layout_routine_check_adapter, parent, false);
        return new RoutineCheckUpParentAdapter.ViewHolder(mLayoutRoutineCheckAdapterBinding);
    }

    @Override
    public void onBindViewHolder(@NotNull RoutineCheckUpParentAdapter.ViewHolder holder, final int position) {
        try {
            holder.mLayoutRoutineCheckAdapterBinding.txtQuest.setText(items.get(position).getQuestion_Display_Title());
            holder.mLayoutRoutineCheckAdapterBinding.txtQuest.setTypeface(holder.mLayoutRoutineCheckAdapterBinding.txtQuest.getTypeface(), Typeface.BOLD);
            RoutineCheckUpChildAdapter childAdapter = new RoutineCheckUpChildAdapter(mContext, (positionChild, isChecked, value) -> {
                items.get(position).setSecondarySelection(value.trim());
                items.get(position).setPrimarySelection(items.get(position).getValues().get(positionChild).getValue());

                if(items.get(position).getYes()!=null){
                    onOptionClicked.onClicked(position, items.get(position).getValues().get(positionChild).getValue(), value.trim());
                }else {
                    onOptionClicked.onClicked(position, items.get(position).getValues().get(positionChild).getValue(), "NA");
                }
            });
            holder.mLayoutRoutineCheckAdapterBinding.recycleView.setLayoutManager(new LinearLayoutManager(mContext));
            holder.mLayoutRoutineCheckAdapterBinding.recycleView.setHasFixedSize(true);
            holder.mLayoutRoutineCheckAdapterBinding.recycleView.setClipToPadding(false);
            holder.mLayoutRoutineCheckAdapterBinding.recycleView.setAdapter(childAdapter);
            if (items.get(position).getValues() != null && items.get(position).getValues().size() > 0) {
                childAdapter.addData(items.get(position).getValues(), items.get(position).getYes(), items.get(position).getNo());
                childAdapter.setOnItemClickHandler(positionChild -> {
                    childAdapter.getItem(positionChild).setIsSelected(true);
                    childAdapter.notifyItemChanged(positionChild);
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setOnItemClickHandler(OnConsultationClickHandler onItemClickHandler) {
        this.onItemClickHandler = onItemClickHandler;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public RoutineQuestion getItem(int position) {
        return items.get(position);
    }

    public void addData(List<RoutineQuestion> data) {
        items.clear();
        items.addAll(data);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final LayoutRoutineCheckAdapterBinding mLayoutRoutineCheckAdapterBinding;

        public ViewHolder(LayoutRoutineCheckAdapterBinding mLayoutRoutineCheckAdapterBinding) {
            super(mLayoutRoutineCheckAdapterBinding.getRoot());
            this.mLayoutRoutineCheckAdapterBinding = mLayoutRoutineCheckAdapterBinding;
        }
    }

    public interface OnOptionClicked {
        void onClicked(int position, String primary, String secondary);
    }
}
