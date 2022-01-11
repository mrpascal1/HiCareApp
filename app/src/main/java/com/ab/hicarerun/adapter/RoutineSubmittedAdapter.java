package com.ab.hicarerun.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.LayoutRoutineSubmittedAdapterBinding;
import com.ab.hicarerun.handler.OnConsultationClickHandler;
import com.ab.hicarerun.network.models.routinemodel.RoutineQuestion;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arjun Bhatt on 8/27/2020.
 */
public class RoutineSubmittedAdapter extends RecyclerView.Adapter<RoutineSubmittedAdapter.ViewHolder> {
    private OnConsultationClickHandler onItemClickHandler;
    private final Context mContext;
    private List<RoutineQuestion> items = null;


    public RoutineSubmittedAdapter(Context context) {
        if (items == null) {
            items = new ArrayList<>();
        }
        this.mContext = context;
    }

    @NotNull
    @Override
    public RoutineSubmittedAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        LayoutRoutineSubmittedAdapterBinding mLayoutRoutineSubmittedAdapterBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.layout_routine_submitted_adapter, parent, false);
        return new RoutineSubmittedAdapter.ViewHolder(mLayoutRoutineSubmittedAdapterBinding);
    }

    @Override
    public void onBindViewHolder(@NotNull RoutineSubmittedAdapter.ViewHolder holder, final int position) {
        try {

            holder.mLayoutRoutineSubmittedAdapterBinding.txtQuest.setText(items.get(position).getQuestion_Display_Title());
            if(!items.get(position).getPrimarySelection().equals("") && !items.get(position).getSecondarySelection().equals("")){
                holder.mLayoutRoutineSubmittedAdapterBinding.txtAnswers.setText("Ans : " + items.get(position).getPrimarySelection()+" - "+ items.get(position).getSecondarySelection());
            }else {
                holder.mLayoutRoutineSubmittedAdapterBinding.txtAnswers.setText("Ans : " + items.get(position).getPrimarySelection());
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

    public  RoutineQuestion getItem(int position) {
        return items.get(position);
    }

    public void addData(List<RoutineQuestion> data) {
        items.clear();
        items.addAll(data);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final LayoutRoutineSubmittedAdapterBinding mLayoutRoutineSubmittedAdapterBinding;

        public ViewHolder(LayoutRoutineSubmittedAdapterBinding mLayoutRoutineSubmittedAdapterBinding) {
            super(mLayoutRoutineSubmittedAdapterBinding.getRoot());
            this.mLayoutRoutineSubmittedAdapterBinding = mLayoutRoutineSubmittedAdapterBinding;
        }
    }

    public interface OnOptionClicked {
        void onClicked(int position, String primary, String secondary);
    }
}
