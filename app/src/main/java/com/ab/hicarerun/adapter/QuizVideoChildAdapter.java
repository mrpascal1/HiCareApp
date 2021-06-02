package com.ab.hicarerun.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.LayoutConsulationChildAdapterBinding;
import com.ab.hicarerun.databinding.LayoutOptionAdapterBinding;
import com.ab.hicarerun.handler.OnListItemClickHandler;
import com.ab.hicarerun.network.models.ConsulationModel.Optionlist;
import com.ab.hicarerun.network.models.QuizModel.QuizOption;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arjun Bhatt on 3/3/2021.
 */
public class QuizVideoChildAdapter extends RecyclerView.Adapter<QuizVideoChildAdapter.ViewHolder> {
    private OnListItemClickHandler onItemClickHandler;
    private final Context mContext;
    private List<QuizOption> items = null;
    private int selectedPos = -1;
    private String QuestionType = "";
    private OnCheckChanged onCheckChanged;
    private String type = "";

    public QuizVideoChildAdapter(Context mContext, QuizVideoChildAdapter.OnCheckChanged onCheckChanged) {
        if (items == null) {
            items = new ArrayList<>();
        }
        this.onCheckChanged = onCheckChanged;
        this.mContext = mContext;
    }

    @NotNull
    @Override
    public QuizVideoChildAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        LayoutOptionAdapterBinding mLayoutOptionAdapterBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.layout_option_adapter, parent, false);
        return new QuizVideoChildAdapter.ViewHolder(mLayoutOptionAdapterBinding);
    }

    @Override
    public void onBindViewHolder(@NotNull QuizVideoChildAdapter.ViewHolder holder, final int position) {
        try {
            if (type.equals("Radio")) {
                holder.mLayoutOptionAdapterBinding.txtRadioOption.setText(items.get(position).getOptionTitle());
                holder.mLayoutOptionAdapterBinding.lnrRadio.setVisibility(View.VISIBLE);
                holder.mLayoutOptionAdapterBinding.lnrCheck.setVisibility(View.GONE);
                holder.mLayoutOptionAdapterBinding.lnrImgOption.setVisibility(View.GONE);
                holder.mLayoutOptionAdapterBinding.radioOption.setChecked(items.get(position).getIsSelected());

                holder.mLayoutOptionAdapterBinding.radioOption.setOnClickListener(v -> {
                    selectedPos = position;
                    holder.mLayoutOptionAdapterBinding.radioOption.setChecked(true);

                    for (int i = 0; i < items.size(); i++) {
                        if (selectedPos != i) {
                            holder.mLayoutOptionAdapterBinding.radioOption.setChecked(false);
                            items.get(i).setIsSelected(false);
                        }
                    }
                    onItemClickHandler.onItemClick(position);
                    notifyDataSetChanged();
                });
            } else {
                holder.mLayoutOptionAdapterBinding.txtCheckOption.setText(items.get(position).getOptionTitle());
                holder.mLayoutOptionAdapterBinding.checkOption.setChecked(items.get(position).getIsSelected());
                holder.mLayoutOptionAdapterBinding.checkOption.setVisibility(View.GONE);
                holder.mLayoutOptionAdapterBinding.checkOption.setVisibility(View.VISIBLE);
                holder.mLayoutOptionAdapterBinding.checkOption.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    onCheckChanged.onChecked(position, isChecked);
                    onItemClickHandler.onItemClick(position);
                    if(isChecked){
                        items.get(position).setIsSelected(true);
                    }else {
                        items.get(position).setIsSelected(false);
                    }
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setOnItemClickHandler(OnListItemClickHandler onItemClickHandler) {
        this.onItemClickHandler = onItemClickHandler;
    }

    public QuizOption getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addData(List<QuizOption> data, String type) {
        items.clear();
        items.addAll(data);
        this.type = type;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final LayoutOptionAdapterBinding mLayoutOptionAdapterBinding;

        public ViewHolder(LayoutOptionAdapterBinding mLayoutOptionAdapterBinding) {
            super(mLayoutOptionAdapterBinding.getRoot());
            this.mLayoutOptionAdapterBinding = mLayoutOptionAdapterBinding;
        }
    }

    public interface OnCheckChanged {
        void onChecked(int position, boolean isChecked);
    }
}
