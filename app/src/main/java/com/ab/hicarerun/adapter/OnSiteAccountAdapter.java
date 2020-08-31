package com.ab.hicarerun.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.OnsiteAccountAdapterBinding;
import com.ab.hicarerun.databinding.ReferralListAdapterBinding;
import com.ab.hicarerun.handler.OnAccountOnsiteClickHandler;
import com.ab.hicarerun.handler.OnDeleteListItemClickHandler;
import com.ab.hicarerun.handler.OnListItemClickHandler;
import com.ab.hicarerun.network.models.OnSiteModel.OnSiteAccounts;
import com.ab.hicarerun.network.models.ReferralModel.ReferralList;
import com.ab.hicarerun.viewmodel.OnSiteAccountViewModel;
import com.ab.hicarerun.viewmodel.ReferralListViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arjun Bhatt on 12/18/2019.
 */
public class OnSiteAccountAdapter extends RecyclerView.Adapter<OnSiteAccountAdapter.ViewHolder> {


    private OnAccountOnsiteClickHandler onOnSiteClickHandler;
    private OnListItemClickHandler onItemClickHandler;
    private final Context mContext;
    private List<OnSiteAccountViewModel> items = null;
    private String Flat = "";
    private String Street = "";

    public OnSiteAccountAdapter(Context context) {
        if (items == null) {
            items = new ArrayList<>();
        }
        this.mContext = context;
    }


    @NotNull
    @Override
    public OnSiteAccountAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        OnsiteAccountAdapterBinding mOnsiteAccountAdapterBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.onsite_account_adapter, parent, false);
        return new OnSiteAccountAdapter.ViewHolder(mOnsiteAccountAdapterBinding);
    }


    @Override
    public void onBindViewHolder(@NotNull OnSiteAccountAdapter.ViewHolder holder, final int position) {
        try {
            int[] attrs = new int[]{R.attr.selectableItemBackground};
            TypedArray typedArray = mContext.obtainStyledAttributes(attrs);
            int backgroundResource = typedArray.getResourceId(0, 0);
            holder.mOnsiteAccountAdapterBinding.lnrAccount.setBackgroundResource(backgroundResource);
            holder.mOnsiteAccountAdapterBinding.txtName.setText(items.get(position).getAccountName());
            holder.mOnsiteAccountAdapterBinding.txtPostcode.setText(items.get(position).getPostalCode());
            if (items.get(position).getBuildingName() != null && items.get(position).getBuildingName().trim().length() != 0) {
                holder.mOnsiteAccountAdapterBinding.lnrAddress.setVisibility(View.VISIBLE);
                if (items.get(position).getFlatNumber() != null && !items.get(position).getFlatNumber().equals("")) {
                    Flat = items.get(position).getFlatNumber() + ", ";
                }
                if (items.get(position).getBillingStreet() != null) {
                    Street = items.get(position).getBillingStreet();
                }
                holder.mOnsiteAccountAdapterBinding.txtAddress.setText(Flat
                        + items.get(position).getBuildingName() + ", "
                        + items.get(position).getLocality() + ", "
                        + Street);
            } else {
                holder.mOnsiteAccountAdapterBinding.lnrAddress.setVisibility(View.GONE);
            }
            holder.mOnsiteAccountAdapterBinding.lnrLocation.setOnClickListener(v -> onOnSiteClickHandler.onTrackLocationIconClicked(position));

            holder.mOnsiteAccountAdapterBinding.mobilePrimary.setOnClickListener(view -> onOnSiteClickHandler.onPrimaryMobileClicked(position));

            holder.mOnsiteAccountAdapterBinding.mobileSecondary.setOnClickListener(view -> onOnSiteClickHandler.onAlternateMobileClicked(position));

            holder.mOnsiteAccountAdapterBinding.oldPhone.setOnClickListener(view -> onOnSiteClickHandler.onTelePhoneClicked(position));
            holder.itemView.setOnClickListener(v -> onItemClickHandler.onItemClick(position));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setOnItemClickHandler(OnAccountOnsiteClickHandler onItemClickHandler) {
        this.onOnSiteClickHandler = onItemClickHandler;
    }

    public void setOnItemClickHandler(OnListItemClickHandler onItemClickHandler) {
        this.onItemClickHandler = onItemClickHandler;
    }

    @Override
    public int getItemCount() {
        return items.size();

    }

    public void setData(List<OnSiteAccounts> data) {
        items.clear();
        for (int index = 0; index < data.size(); index++) {
            OnSiteAccountViewModel onSiteAccountViewModel = new OnSiteAccountViewModel();
            onSiteAccountViewModel.clone(data.get(index));
            items.add(onSiteAccountViewModel);
        }
    }

    public void addData(List<OnSiteAccounts> data) {
        items.clear();
        for (int index = 0; index < data.size(); index++) {
            OnSiteAccountViewModel onSiteAccountViewModel = new OnSiteAccountViewModel();
            onSiteAccountViewModel.clone(data.get(index));
            items.add(onSiteAccountViewModel);
        }
    }

    public void removeAll() {
        items.removeAll(items);
        notifyDataSetChanged();
    }

    public OnSiteAccountViewModel getItem(int position) {
        return items.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final OnsiteAccountAdapterBinding mOnsiteAccountAdapterBinding;

        public ViewHolder(OnsiteAccountAdapterBinding mOnsiteAccountAdapterBinding) {
            super(mOnsiteAccountAdapterBinding.getRoot());
            this.mOnsiteAccountAdapterBinding = mOnsiteAccountAdapterBinding;
        }
    }
}
