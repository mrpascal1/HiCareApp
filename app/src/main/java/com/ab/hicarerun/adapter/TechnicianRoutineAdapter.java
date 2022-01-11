package com.ab.hicarerun.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.TechnicianRoutineAdapterBinding;
import com.ab.hicarerun.handler.OnListItemClickHandler;
import com.ab.hicarerun.network.models.technicianroutinemodel.TechnicianData;
import com.ab.hicarerun.viewmodel.TechnicianDataViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arjun Bhatt on 8/13/2020.
 */
public class TechnicianRoutineAdapter extends RecyclerView.Adapter<TechnicianRoutineAdapter.ViewHolder> {
    private OnListItemClickHandler onItemClickHandler;
    private final Context mContext;
    private List<TechnicianDataViewModel> items = null;
    private static final int RESOURCE_REQ = 1000;


    public TechnicianRoutineAdapter(Context context) {
        if (items == null) {
            items = new ArrayList<>();
        }
        this.mContext = context;
    }

    @NotNull
    @Override
    public TechnicianRoutineAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        TechnicianRoutineAdapterBinding mLayoutTechnicianRoutineAdapterBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.technician_routine_adapter, parent, false);
        return new TechnicianRoutineAdapter.ViewHolder(mLayoutTechnicianRoutineAdapterBinding);
    }

    @Override
    public void onBindViewHolder(@NotNull TechnicianRoutineAdapter.ViewHolder holder, final int position) {
        try {
            holder.mLayoutTechnicianRoutineAdapterBinding.txtName.setText(items.get(position).getTechnicianName());
            if (items.get(position).isRoutineCheckListSubmited()) {
                holder.mLayoutTechnicianRoutineAdapterBinding.imgChecked.setImageResource(R.drawable.ic_routine_done);
            } else {
                holder.mLayoutTechnicianRoutineAdapterBinding.imgChecked.setImageResource(R.drawable.ic_routine_not_done);
            }

            holder.itemView.setOnClickListener(v -> onItemClickHandler.onItemClick(position));

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

    public TechnicianDataViewModel getItem(int position) {
        return items.get(position);
    }


    //
    public void addData(List<TechnicianData> data) {
        items.clear();
        for (int index = 0; index < data.size(); index++) {
            TechnicianDataViewModel technicianDataViewModel = new TechnicianDataViewModel();
            technicianDataViewModel.clone(data.get(index));
            items.add(technicianDataViewModel);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TechnicianRoutineAdapterBinding mLayoutTechnicianRoutineAdapterBinding;

        public ViewHolder(TechnicianRoutineAdapterBinding mLayoutTechnicianRoutineAdapterBinding) {
            super(mLayoutTechnicianRoutineAdapterBinding.getRoot());
            this.mLayoutTechnicianRoutineAdapterBinding = mLayoutTechnicianRoutineAdapterBinding;
        }
    }

}
