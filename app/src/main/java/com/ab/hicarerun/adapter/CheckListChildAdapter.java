package com.ab.hicarerun.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.LayoutNewCompletionChildListBinding;
import com.ab.hicarerun.handler.OnListItemClickHandler;
import com.ab.hicarerun.network.models.technicianroutinemodel.ValueData;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arjun Bhatt on 8/18/2020.
 */
public class CheckListChildAdapter extends RecyclerView.Adapter<CheckListChildAdapter.ViewHolder> {
    private OnListItemClickHandler onItemClickHandler;
    private final Context mContext;
    private List<ValueData> items = null;
    private int selectedPos = -1;
    private OnCheckChanged onCheckChanged;
    private String optionType;

    public CheckListChildAdapter(Context mContext, OnCheckChanged onCheckChanged) {
        if (items == null) {
            items = new ArrayList<>();
        }
        this.onCheckChanged = onCheckChanged;
        this.mContext = mContext;
    }

    @NotNull
    @Override
    public CheckListChildAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        LayoutNewCompletionChildListBinding mLayoutNewCompletionChildListBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.layout_new_completion_child_list, parent, false);
        return new CheckListChildAdapter.ViewHolder(mLayoutNewCompletionChildListBinding);
    }

    @Override
    public void onBindViewHolder(@NotNull CheckListChildAdapter.ViewHolder holder, final int position) {

        try {
            holder.mLayoutNewCompletionChildListBinding.rbAnswers.setText(items.get(position).getOptionValueDisplayText());

            if (optionType.equalsIgnoreCase("Single Select")) {
                holder.mLayoutNewCompletionChildListBinding.rbAnswers.setVisibility(View.VISIBLE);
                holder.mLayoutNewCompletionChildListBinding.chkAnswers.setVisibility(View.GONE);

                holder.mLayoutNewCompletionChildListBinding.rbAnswers.setChecked(items.get(position).getIsSelected());


                holder.mLayoutNewCompletionChildListBinding.rbAnswers.setOnClickListener(v -> {
                    selectedPos = position;
                    holder.mLayoutNewCompletionChildListBinding.rbAnswers.setChecked(true);
                    for (int i = 0; i < items.size(); i++) {
                        if (selectedPos != i) {
                            holder.mLayoutNewCompletionChildListBinding.rbAnswers.setChecked(false);
                            items.get(i).setIsSelected(false);
                        }
                    }
                    onItemClickHandler.onItemClick(position);
                    notifyDataSetChanged();
                });
            } else if (optionType.equalsIgnoreCase("Multi Select")) {
                holder.mLayoutNewCompletionChildListBinding.chkAnswers.setText(items.get(position).getOptionValueDisplayText());
                holder.mLayoutNewCompletionChildListBinding.chkAnswers.setChecked(items.get(position).getIsSelected());
                holder.mLayoutNewCompletionChildListBinding.rbAnswers.setVisibility(View.GONE);
                holder.mLayoutNewCompletionChildListBinding.chkAnswers.setVisibility(View.VISIBLE);
                holder.mLayoutNewCompletionChildListBinding.chkAnswers.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    onCheckChanged.onChecked(position, isChecked, items.get(position).getValue());
                    onItemClickHandler.onItemClick(position);
                });
            } else {
                holder.mLayoutNewCompletionChildListBinding.chkAnswers.setVisibility(View.GONE);
                holder.mLayoutNewCompletionChildListBinding.rbAnswers.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        try {

            holder.mLayoutNewCompletionChildListBinding.rbAnswers.setChecked(items.get(position).getIsSelected());
            Log.i("Sel_Pos " + position, String.valueOf(items.get(position).getIsSelected()));

            holder.mLayoutNewCompletionChildListBinding.rbAnswers.setText(items.get(position).getOptionValueDisplayText());
            holder.mLayoutNewCompletionChildListBinding.rbAnswers.setOnClickListener(v -> {
                selectedPos = position;
                holder.mLayoutNewCompletionChildListBinding.rbAnswers.setChecked(true);
                for (int i = 0; i < items.size(); i++) {
                    Log.i("Sel_OnClick", String.valueOf(i));
                    Log.i("Sel_PosClick", String.valueOf(selectedPos));
                    if (selectedPos != i) {
                        holder.mLayoutNewCompletionChildListBinding.rbAnswers.setChecked(false);
                        items.get(i).setIsSelected(false);
                    }
                }

                onItemClickHandler.onItemClick(position);
                notifyDataSetChanged();
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setOnItemClickHandler(OnListItemClickHandler onItemClickHandler) {
        this.onItemClickHandler = onItemClickHandler;
    }

    public ValueData getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    //
    public void addData(List<ValueData> data, String optionType) {
        items.clear();
        items.addAll(data);
        this.optionType = optionType;
        Log.i("Sel_Add ", "called");
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final LayoutNewCompletionChildListBinding mLayoutNewCompletionChildListBinding;

        public ViewHolder(LayoutNewCompletionChildListBinding mLayoutNewCompletionChildListBinding) {
            super(mLayoutNewCompletionChildListBinding.getRoot());
            this.mLayoutNewCompletionChildListBinding = mLayoutNewCompletionChildListBinding;
        }
    }

    public interface OnCheckChanged {
        void onChecked(int position, boolean isChecked, String value);
    }
}
