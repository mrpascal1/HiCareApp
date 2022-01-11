package com.ab.hicarerun.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.SlotsAdapterBinding;
import com.ab.hicarerun.handler.OnListItemClickHandler;
import com.ab.hicarerun.network.models.slotmodel.TimeSlot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arjun Bhatt on 10/16/2020.
 */
public class SlotsAdapter extends RecyclerView.Adapter<SlotsAdapter.ViewHolder> {
    private OnListItemClickHandler onItemClickHandler;
    private final Context mContext;
    private List<TimeSlot> items = null;
    private int selectedPos = -1;

    public SlotsAdapter(Context context) {
        if (items == null) {
            items = new ArrayList<>();
        }
        this.mContext = context;
    }

    @NotNull
    @Override
    public SlotsAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        SlotsAdapterBinding mSlotsAdapterBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.slots_adapter, parent, false);
        return new SlotsAdapter.ViewHolder(mSlotsAdapterBinding);
    }


    @Override
    public void onBindViewHolder(@NotNull SlotsAdapter.ViewHolder holder, final int position) {
        try {
            holder.mSlotsAdapterBinding.txtSlots.setText(items.get(position).getStartTime() +" — "+items.get(position).getFinishTime());

            if(selectedPos == position){
                holder.mSlotsAdapterBinding.radioSlots.setChecked(true);
            }else {
                holder.mSlotsAdapterBinding.radioSlots.setChecked(false);
            }
            holder.mSlotsAdapterBinding.radioSlots.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedPos = position;
                    notifyDataSetChanged();
                    onItemClickHandler.onItemClick(position);
                }
            });
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

    public TimeSlot getItem(int position) {
        return items.get(position);
    }


    public void setData(List<TimeSlot> data) {
        items.clear();
        items.addAll(data);
    }

    //
    public void addData(List<TimeSlot> data) {
        items.clear();
        items.addAll(data);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private final SlotsAdapterBinding mSlotsAdapterBinding;

        public ViewHolder(SlotsAdapterBinding mSlotsAdapterBinding) {
            super(mSlotsAdapterBinding.getRoot());
            this.mSlotsAdapterBinding = mSlotsAdapterBinding;

        }
    }
}

