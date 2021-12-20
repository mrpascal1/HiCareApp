package com.ab.hicarerun.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.LayoutPaymentModesBinding;
import com.ab.hicarerun.handler.OnBankClickHandler;
import com.ab.hicarerun.handler.OnListItemClickHandler;
import com.ab.hicarerun.network.models.OnSiteModel.OnSiteArea;
import com.ab.hicarerun.network.models.ServicePlanModel.PaymentMode;
import com.ab.hicarerun.viewmodel.ServicePlanViewModel;

//import net.igenius.customcheckbox.CustomCheckBox;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arjun Bhatt on 5/27/2020.
 */
public class ServicePaymentAdapter extends RecyclerView.Adapter<ServicePaymentAdapter.ViewHolder> {

    private OnListItemClickHandler onItemClickHandler;
    private final Context mContext;
    private List<PaymentMode> items = null;
    private int selectedPos = 0;
    private boolean cashTrue = false;

    public ServicePaymentAdapter(Context context) {
        if (items == null) {
            items = new ArrayList<>();
        }
        this.mContext = context;
    }


    @NotNull
    @Override
    public ServicePaymentAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        LayoutPaymentModesBinding layoutPaymentModesBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.layout_payment_modes, parent, false);
        return new ServicePaymentAdapter.ViewHolder(layoutPaymentModesBinding);
    }

    @Override
    public void onBindViewHolder(@NotNull final ServicePaymentAdapter.ViewHolder holder, final int position) {
        try {
            if (selectedPos == position) {
                holder.layoutPaymentModesBinding.checkMode.setChecked(true);
            } else {
                holder.layoutPaymentModesBinding.checkMode.setChecked(false);
            }
            holder.layoutPaymentModesBinding.txtModes.setText(items.get(position).getValue());

            holder.layoutPaymentModesBinding.checkMode.setOnClickListener(v -> {
                selectedPos = position;
                notifyDataSetChanged();
                onItemClickHandler.onItemClick(position);
            });

            holder.itemView.setOnClickListener(v -> {
                selectedPos = position;
                notifyDataSetChanged();
                onItemClickHandler.onItemClick(position);
            });

            /*if (cashTrue) {
                if (items.get(position).getValue().contains("cash")) {
                    selectedPos = position;
                    cashTrue = false;
                    onItemClickHandler.onItemClick(position);
                    notifyDataSetChanged();
                }
            }*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setMode(){
        cashTrue = true;
        notifyDataSetChanged();
    }

    public void setOnItemClickHandler(OnListItemClickHandler onItemClickHandler) {
        this.onItemClickHandler = onItemClickHandler;
    }


    public void setData(List<PaymentMode> data) {
        items.clear();
        items.addAll(data);
    }
//
//    public void addData(List<RenewalServicePlan> data) {
//        items.clear();
//        for (int index = 0; index < data.size(); index++) {
//            ServicePlanViewModel servicePlanViewModel = new ServicePlanViewModel();
//            servicePlanViewModel.clone(data.get(index));
//            items.add(servicePlanViewModel);
//        }
//    }

    public PaymentMode getItem(int position) {
        return items.get(position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private final LayoutPaymentModesBinding layoutPaymentModesBinding;

        public ViewHolder(LayoutPaymentModesBinding layoutPaymentModesBinding) {
            super(layoutPaymentModesBinding.getRoot());
            this.layoutPaymentModesBinding = layoutPaymentModesBinding;
        }
    }

}
