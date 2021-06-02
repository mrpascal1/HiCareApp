package com.ab.hicarerun.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.LayoutCheckListAdapterBinding;
import com.ab.hicarerun.databinding.LayoutRoutineCheckAdapterBinding;
import com.ab.hicarerun.handler.OnConsultationClickHandler;
import com.ab.hicarerun.network.models.RoutineModel.RoutineOption;
import com.ab.hicarerun.network.models.RoutineModel.RoutineQuestion;
import com.ab.hicarerun.network.models.SelfAssessModel.ResourceCheckList;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arjun Bhatt on 12/8/2020.
 */
public class CovidCheckListParentAdapter extends RecyclerView.Adapter<CovidCheckListParentAdapter.ViewHolder> {
    private OnConsultationClickHandler onItemClickHandler;
    private final Context mContext;
    private List<ResourceCheckList> items = null;
    private List<RoutineOption> valueList = new ArrayList<>();
    private OnOptionClicked onOptionClicked;
    private ArrayList<String> multileItems = null;
    private String strAnswer = "";


    public CovidCheckListParentAdapter(Context context, OnOptionClicked onOptionClicked) {
        if (items == null) {
            items = new ArrayList<>();
        }
        if (multileItems == null) {
            multileItems = new ArrayList<>();
        }
        this.onOptionClicked = onOptionClicked;
        this.mContext = context;
    }

    @NotNull
    @Override
    public CovidCheckListParentAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        LayoutCheckListAdapterBinding mLayoutCheckListAdapterBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.layout_check_list_adapter, parent, false);
        return new CovidCheckListParentAdapter.ViewHolder(mLayoutCheckListAdapterBinding);
    }

    @Override
    public void onBindViewHolder(@NotNull CovidCheckListParentAdapter.ViewHolder holder, final int position) {
        try {
            holder.mLayoutCheckListAdapterBinding.txtName.setText(items.get(position).getDisplayTitle());
            if (items.get(position).getDescription() != null) {
                holder.mLayoutCheckListAdapterBinding.edtTemperature.setHint(items.get(position).getDescription());
            }
            holder.mLayoutCheckListAdapterBinding.txtName.setTypeface(holder.mLayoutCheckListAdapterBinding.txtName.getTypeface(), Typeface.BOLD);
            holder.mLayoutCheckListAdapterBinding.chkSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    onOptionClicked.onClicked(holder.getAdapterPosition(), holder.mLayoutCheckListAdapterBinding.edtTemperature.getText().toString(), isChecked);
                    if (isChecked && items.get(position).getOptionList() != null && items.get(position).getOptionList().size() > 0) {
                        holder.mLayoutCheckListAdapterBinding.recycleView.setVisibility(View.VISIBLE);
                        holder.mLayoutCheckListAdapterBinding.lnrTemperature.setVisibility(View.GONE);
                    } else {
                        holder.mLayoutCheckListAdapterBinding.recycleView.setVisibility(View.GONE);
                        if (items.get(position).getShowText()) {
                            holder.mLayoutCheckListAdapterBinding.lnrTemperature.setVisibility(View.VISIBLE);
                        } else {
                            holder.mLayoutCheckListAdapterBinding.lnrTemperature.setVisibility(View.GONE);
                        }
                    }
                }
            });

            holder.mLayoutCheckListAdapterBinding.edtTemperature.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
//                    if (!s.toString().equals("")) {
//                        holder.mLayoutCheckListAdapterBinding.textSuffix.setVisibility(View.VISIBLE);
//                    } else {
//                        holder.mLayoutCheckListAdapterBinding.textSuffix.setVisibility(View.GONE);
//                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    onOptionClicked.onClicked(holder.getAdapterPosition(), s.toString(), true);
                }
            });

            CovidCheckListChildAdapter childAdapter = new CovidCheckListChildAdapter(mContext, (positionChild, isClicked, value) -> {
                items.get(position).setOptions(value.trim());
                onOptionClicked.onClicked(holder.getAdapterPosition(), value.trim(), true);

                if (isClicked) {
                    multileItems.add(items.get(position).getOptionList().get(positionChild).getValue());
                } else {
                    multileItems.remove(items.get(position).getOptionList().get(positionChild).getValue());
                }

                if (multileItems.size() > 0) {
                    StringBuilder sbString = new StringBuilder("");
                    //iterate through ArrayList
                    for (String service : multileItems) {
                        //append ArrayList element followed by comma
                        sbString.append(service).append(",");
                    }
                    //convert StringBuffer to String
                    strAnswer = sbString.toString();
                    //remove last comma from String if you want
                    if (strAnswer.length() > 0) {
                        strAnswer = strAnswer.substring(0, strAnswer.length() - 1);
                    }
                    items.get(position).setOptions(strAnswer);
                    onOptionClicked.onClicked(holder.getAdapterPosition(), strAnswer, true);

                } else {
                    strAnswer = "";
                }
            });

            holder.mLayoutCheckListAdapterBinding.recycleView.setLayoutManager(new LinearLayoutManager(mContext));
            holder.mLayoutCheckListAdapterBinding.recycleView.setHasFixedSize(true);
            holder.mLayoutCheckListAdapterBinding.recycleView.setClipToPadding(false);
            holder.mLayoutCheckListAdapterBinding.recycleView.setAdapter(childAdapter);
            if (items.get(position).getOptionList() != null) {
                childAdapter.addData(items.get(position).getOptionList(), items.get(position).getOptionType());
                childAdapter.setOnItemClickHandler(positionChild -> {
                    childAdapter.getItem(positionChild).setIsSelected(true);
                    childAdapter.notifyItemChanged(positionChild);
                    onOptionClicked.onClicked(holder.getAdapterPosition(), items.get(position).getOptionList().get(positionChild).getValue(), true);
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

    public ResourceCheckList getItem(int position) {
        return items.get(position);
    }

    public List<ResourceCheckList> getList() {
        return items;
    }

    public void addData(List<ResourceCheckList> data) {
        items.clear();
        items.addAll(data);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final LayoutCheckListAdapterBinding mLayoutCheckListAdapterBinding;

        public ViewHolder(LayoutCheckListAdapterBinding mLayoutCheckListAdapterBinding) {
            super(mLayoutCheckListAdapterBinding.getRoot());
            this.mLayoutCheckListAdapterBinding = mLayoutCheckListAdapterBinding;
        }
    }

    public interface OnOptionClicked {
        void onClicked(int position, String option, boolean isChecked);
    }
}
