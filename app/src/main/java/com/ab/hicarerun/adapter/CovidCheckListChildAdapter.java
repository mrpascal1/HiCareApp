package com.ab.hicarerun.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.LayoutCheckListChildAdapterBinding;
import com.ab.hicarerun.databinding.LayoutRoutineCheckChildAdapterBinding;
import com.ab.hicarerun.handler.OnListItemClickHandler;
import com.ab.hicarerun.network.models.RoutineModel.RoutineNoValue;
import com.ab.hicarerun.network.models.RoutineModel.RoutineOption;
import com.ab.hicarerun.network.models.RoutineModel.ValueNo;
import com.ab.hicarerun.network.models.RoutineModel.ValueYes;
import com.ab.hicarerun.network.models.SelfAssessModel.ResourceOptionList;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arjun Bhatt on 12/8/2020.
 */
public class CovidCheckListChildAdapter extends RecyclerView.Adapter<CovidCheckListChildAdapter.ViewHolder> {
    private OnListItemClickHandler onItemClickHandler;
    private final Context mContext;
    private List<ResourceOptionList> items = null;
    private int selectedPos = -1;
    private OnCheckChanged onCheckChanged;
    private  String type = "";


    public CovidCheckListChildAdapter(Context mContext,OnCheckChanged onCheckChanged) {
        if (items == null) {
            items = new ArrayList<>();
        }
        this.mContext = mContext;
        this.onCheckChanged = onCheckChanged;
    }

    @NotNull
    @Override
    public CovidCheckListChildAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        LayoutCheckListChildAdapterBinding mLayoutCheckListChildAdapterBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.layout_check_list_child_adapter, parent, false);
        return new CovidCheckListChildAdapter.ViewHolder(mLayoutCheckListChildAdapterBinding);
    }


    @Override
    public void onBindViewHolder(@NotNull CovidCheckListChildAdapter.ViewHolder holder, final int position) {
        try {
            if(type.equalsIgnoreCase("CheckBox")){
                holder.mLayoutCheckListChildAdapterBinding.rbAnswers.setVisibility(View.GONE);
                holder.mLayoutCheckListChildAdapterBinding.lnrTemperature.setVisibility(View.GONE);
                holder.mLayoutCheckListChildAdapterBinding.chkAnswers.setVisibility(View.VISIBLE);
                holder.mLayoutCheckListChildAdapterBinding.chkAnswers.setText(items.get(position).getValue());
                holder.mLayoutCheckListChildAdapterBinding.chkAnswers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        holder.mLayoutCheckListChildAdapterBinding.chkAnswers.setChecked(b);
                        onCheckChanged.onChecked(position, true, items.get(position).getValue());
                    }
                });
            }else if(type.equalsIgnoreCase("Radio")){
                holder.mLayoutCheckListChildAdapterBinding.rbAnswers.setChecked(items.get(position).getIsSelected());
                holder.mLayoutCheckListChildAdapterBinding.chkAnswers.setVisibility(View.GONE);
                holder.mLayoutCheckListChildAdapterBinding.lnrTemperature.setVisibility(View.GONE);
                holder.mLayoutCheckListChildAdapterBinding.rbAnswers.setVisibility(View.VISIBLE);
                holder.mLayoutCheckListChildAdapterBinding.rbAnswers.setText(items.get(position).getValue());
                holder.mLayoutCheckListChildAdapterBinding.rbAnswers.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectedPos = position;
//                        onCheckChanged.onChecked(position, true, items.get(position).getValue());
                        for (int i = 0; i < items.size(); i++) {
                            if (selectedPos != i) {
                                holder.mLayoutCheckListChildAdapterBinding.rbAnswers.setChecked(false);
                                items.get(i).setIsSelected(false);
                            }
                        }

                        onItemClickHandler.onItemClick(position);
                        notifyDataSetChanged();
                    }
                });

            }else if(type.equalsIgnoreCase("TextBox")){
                holder.mLayoutCheckListChildAdapterBinding.rbAnswers.setVisibility(View.GONE);
                holder.mLayoutCheckListChildAdapterBinding.chkAnswers.setVisibility(View.GONE);
                holder.mLayoutCheckListChildAdapterBinding.lnrTemperature.setVisibility(View.VISIBLE);
                holder.mLayoutCheckListChildAdapterBinding.edtTemperature.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable value) {
                        onCheckChanged.onChecked(position, true, value.toString());

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

    public ResourceOptionList getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    //
    public void addData(List<ResourceOptionList> data, String type) {
        items.clear();
        items.addAll(data);
        this.type = type;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final LayoutCheckListChildAdapterBinding mLayoutCheckListChildAdapterBinding;

        public ViewHolder(LayoutCheckListChildAdapterBinding mLayoutCheckListChildAdapterBinding) {
            super(mLayoutCheckListChildAdapterBinding.getRoot());
            this.mLayoutCheckListChildAdapterBinding = mLayoutCheckListChildAdapterBinding;
        }
    }

    public interface OnCheckChanged {
        void onChecked(int position, boolean isChecked, String value);
    }
}
