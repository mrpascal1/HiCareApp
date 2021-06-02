package com.ab.hicarerun.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.LayoutCheckListAdapterBinding;
import com.ab.hicarerun.databinding.LayoutCompletionListAdapterBinding;
import com.ab.hicarerun.handler.OnCheckListItemClickHandler;
import com.ab.hicarerun.network.models.ChemicalModel.Chemicals;
import com.ab.hicarerun.network.models.GeneralModel.TaskCheckList;
import com.ab.hicarerun.network.models.SelfAssessModel.ResourceCheckList;
import com.ab.hicarerun.viewmodel.ReferralListViewModel;
import com.ab.hicarerun.viewmodel.SelfAccessViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arjun Bhatt on 5/20/2020.
 */
public class ResourceCheckListAdapter extends RecyclerView.Adapter<ResourceCheckListAdapter.ViewHolder> {

    private OnCheckListItemClickHandler onItemClickHandler;
    private final Context mContext;
    private static List<SelfAccessViewModel> items = null;
    private OnCheckChanged onCheckChanged;


    public ResourceCheckListAdapter(Context context, OnCheckChanged onCheckChanged) {
        if (items == null) {
            items = new ArrayList<>();
        }
        this.mContext = context;
        this.onCheckChanged = onCheckChanged;
    }


    @NotNull
    @Override
    public ResourceCheckListAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        LayoutCheckListAdapterBinding mLayoutCheckListAdapterBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.layout_check_list_adapter, parent, false);
        return new ResourceCheckListAdapter.ViewHolder(mLayoutCheckListAdapterBinding);
    }

    @Override
    public void onBindViewHolder(@NotNull final ResourceCheckListAdapter.ViewHolder holder, final int position) {
        try {
            holder.mLayoutCheckListAdapterBinding.txtName.setText(items.get(position).getDisplayTitle());
            holder.mLayoutCheckListAdapterBinding.chkSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    try {
                        holder.mLayoutCheckListAdapterBinding.edtTemperature.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                if (!s.toString().equals("")) {
                                    holder.mLayoutCheckListAdapterBinding.textSuffix.setVisibility(View.VISIBLE);
                                } else {
                                    holder.mLayoutCheckListAdapterBinding.textSuffix.setVisibility(View.GONE);
                                }

                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                    onCheckChanged.onChecked(holder.getAdapterPosition(), isChecked, holder.mLayoutCheckListAdapterBinding.edtTemperature.getText().toString());
                            }
                        });
                        onCheckChanged.onChecked(holder.getAdapterPosition(), isChecked, holder.mLayoutCheckListAdapterBinding.edtTemperature.getText().toString());

                        if (isChecked && items.get(position).getShowText()) {
                            holder.mLayoutCheckListAdapterBinding.lnrTemperature.setVisibility(View.VISIBLE);
                        } else {
                            holder.mLayoutCheckListAdapterBinding.lnrTemperature.setVisibility(View.GONE);
                        }

                        if (holder.mLayoutCheckListAdapterBinding.edtTemperature.getText().toString().length() != 0) {
                            try {
                                onCheckChanged.onChecked(holder.getAdapterPosition(), isChecked, holder.mLayoutCheckListAdapterBinding.edtTemperature.getText().toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String PerfectDecimal(String str, int MAX_BEFORE_POINT, int MAX_DECIMAL) {
        if (str.charAt(0) == '.') str = "0" + str;
        int max = str.length();
        String rFinal = "";
        boolean after = false;
        int i = 0, up = 0, decimal = 0;
        char t;
        while (i < max) {
            t = str.charAt(i);
            if (t != '.' && after == false) {
                up++;
                if (up > MAX_BEFORE_POINT) return rFinal;
            } else if (t == '.') {
                after = true;
            } else {
                decimal++;
                if (decimal > MAX_DECIMAL) return rFinal;
            }
            rFinal = rFinal + t;
            i++;
        }
        return rFinal;
    }

    @Override
    public int getItemCount() {
        return items.size();

    }

    public void setOnItemClickHandler(OnCheckListItemClickHandler onItemClickHandler) {
        this.onItemClickHandler = onItemClickHandler;
    }


    public void setData(List<ResourceCheckList> data) {
        items.clear();
        for (int index = 0; index < data.size(); index++) {
            SelfAccessViewModel selfAccessViewModel = new SelfAccessViewModel();
            selfAccessViewModel.clone(data.get(index));
            items.add(selfAccessViewModel);
        }
    }

    public void addData(List<ResourceCheckList> data) {
        items.clear();
        for (int index = 0; index < data.size(); index++) {
            SelfAccessViewModel selfAccessViewModel = new SelfAccessViewModel();
            selfAccessViewModel.clone(data.get(index));
            items.add(selfAccessViewModel);
        }
    }

    public SelfAccessViewModel getItem(int position) {
        return items.get(position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private final LayoutCheckListAdapterBinding mLayoutCheckListAdapterBinding;

        public ViewHolder(LayoutCheckListAdapterBinding mLayoutCheckListAdapterBinding) {
            super(mLayoutCheckListAdapterBinding.getRoot());
            this.mLayoutCheckListAdapterBinding = mLayoutCheckListAdapterBinding;
        }
    }

    public interface OnCheckChanged {
        void onChecked(int position, boolean isChecked, String temperature);
    }
}
