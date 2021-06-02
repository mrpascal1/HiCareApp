package com.ab.hicarerun.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.LayoutConsulationChildAdapterBinding;
import com.ab.hicarerun.databinding.LayoutConsulationParentAdapterBinding;
import com.ab.hicarerun.handler.OnListItemClickHandler;
import com.ab.hicarerun.network.models.ConsulationModel.Data;
import com.ab.hicarerun.network.models.ConsulationModel.Optionlist;
import com.ab.hicarerun.viewmodel.ConsulationViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arjun Bhatt on 7/18/2020.
 */
public class ConsulationChildAdapter extends RecyclerView.Adapter<ConsulationChildAdapter.ViewHolder> {
    private OnListItemClickHandler onItemClickHandler;
    private final Context mContext;
    private List<Optionlist> items = null;
    private int selectedPos = -1;
    private String QuestionType = "";
    private OnCheckChanged onCheckChanged;
    private boolean isInspectionDone = false;
    private boolean isAnswerSelected = false;
    private String type = "";

    public ConsulationChildAdapter(Context mContext, boolean isInspectionDone, OnCheckChanged onCheckChanged) {
        if (items == null) {
            items = new ArrayList<>();
        }
        this.onCheckChanged = onCheckChanged;
        this.mContext = mContext;
        this.isInspectionDone = isInspectionDone;
    }

    @NotNull
    @Override
    public ConsulationChildAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        LayoutConsulationChildAdapterBinding mLayoutConsulationChildAdapterBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.layout_consulation_child_adapter, parent, false);
        return new ConsulationChildAdapter.ViewHolder(mLayoutConsulationChildAdapterBinding);
    }

    @Override
    public void onBindViewHolder(@NotNull ConsulationChildAdapter.ViewHolder holder, final int position) {
        try {
            holder.mLayoutConsulationChildAdapterBinding.rbAnswers.setText(items.get(position).getOptionValueDisplayText());

            if (QuestionType.equals("Single Select")) {
                holder.mLayoutConsulationChildAdapterBinding.rbAnswers.setVisibility(View.VISIBLE);
                holder.mLayoutConsulationChildAdapterBinding.chkAnswers.setVisibility(View.GONE);
                holder.mLayoutConsulationChildAdapterBinding.rbAnswers.setChecked(items.get(position).getIsselected());

                if (isInspectionDone) {
                    holder.mLayoutConsulationChildAdapterBinding.rbAnswers.setEnabled(false);
                } else {
                    if (isAnswerSelected) {
                        holder.mLayoutConsulationChildAdapterBinding.rbAnswers.setEnabled(false);
                    } else {
                        holder.mLayoutConsulationChildAdapterBinding.rbAnswers.setEnabled(true);
                    }
                }


                holder.mLayoutConsulationChildAdapterBinding.rbAnswers.setOnClickListener(v -> {
                    selectedPos = position;
                    holder.mLayoutConsulationChildAdapterBinding.rbAnswers.setChecked(true);

                    for (int i = 0; i < items.size(); i++) {
                        if (selectedPos != i) {
                            holder.mLayoutConsulationChildAdapterBinding.rbAnswers.setChecked(false);
                            items.get(i).setIsselected(false);
                        }
                    }
                    onItemClickHandler.onItemClick(position);
                    notifyDataSetChanged();
                });
            } else {
                if (isInspectionDone) {
                    holder.mLayoutConsulationChildAdapterBinding.chkAnswers.setEnabled(false);
                } else {
                    if (isAnswerSelected) {
                        holder.mLayoutConsulationChildAdapterBinding.chkAnswers.setEnabled(false);
                    } else {
                        if(items.get(position).isSelectedAndDisabled()){
                            holder.mLayoutConsulationChildAdapterBinding.chkAnswers.setEnabled(false);
                            items.get(position).setIsselected(true);
                            onCheckChanged.onChecked(position, true, items.get(position).isSelectedAndDisabled());
                            holder.mLayoutConsulationChildAdapterBinding.chkAnswers.setChecked(true);
                        }else {
                            holder.mLayoutConsulationChildAdapterBinding.chkAnswers.setEnabled(true);
                        }
                    }
                }
                holder.mLayoutConsulationChildAdapterBinding.chkAnswers.setText(items.get(position).getOptionValueDisplayText());
                holder.mLayoutConsulationChildAdapterBinding.chkAnswers.setChecked(items.get(position).getIsselected());
                holder.mLayoutConsulationChildAdapterBinding.rbAnswers.setVisibility(View.GONE);
                holder.mLayoutConsulationChildAdapterBinding.chkAnswers.setVisibility(View.VISIBLE);
                holder.mLayoutConsulationChildAdapterBinding.chkAnswers.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    onCheckChanged.onChecked(position, isChecked, items.get(position).isSelectedAndDisabled());
                    onItemClickHandler.onItemClick(position);
                    if(isChecked){
                        items.get(position).setIsselected(true);
                    }else {
                        items.get(position).setIsselected(false);
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setOnItemClickHandler(OnListItemClickHandler onItemClickHandler) {
        this.onItemClickHandler = onItemClickHandler;
    }

    public Optionlist getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addData(List<Optionlist> data, String questionType, Boolean answerSelected, String type) {
        items.clear();
        items.addAll(data);
        this.QuestionType = questionType;
        this.isAnswerSelected = answerSelected;
        this.type = type;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final LayoutConsulationChildAdapterBinding mLayoutConsulationChildAdapterBinding;

        public ViewHolder(LayoutConsulationChildAdapterBinding mLayoutConsulationChildAdapterBinding) {
            super(mLayoutConsulationChildAdapterBinding.getRoot());
            this.mLayoutConsulationChildAdapterBinding = mLayoutConsulationChildAdapterBinding;
        }
    }

    public interface OnCheckChanged {
        void onChecked(int position, boolean isChecked, boolean isSelectedAndDisable);
    }
}
