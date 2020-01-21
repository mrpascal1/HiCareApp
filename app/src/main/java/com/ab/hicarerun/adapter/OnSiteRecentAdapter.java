package com.ab.hicarerun.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.OnsiteListAdapterBinding;
import com.ab.hicarerun.databinding.OnsiteRecentAdapterBinding;
import com.ab.hicarerun.handler.OnAddActivityClickHandler;
import com.ab.hicarerun.handler.OnDeleteListItemClickHandler;
import com.ab.hicarerun.handler.OnRecentTaskClickHandler;
import com.ab.hicarerun.network.models.OnSiteModel.OnSiteArea;
import com.ab.hicarerun.network.models.OnSiteModel.OnSiteRecent;
import com.ab.hicarerun.utils.AppUtils;
import com.ab.hicarerun.viewmodel.AccountAreaViewModel;
import com.ab.hicarerun.viewmodel.RecentActivityViewModel;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arjun Bhatt on 12/23/2019.
 */
public class OnSiteRecentAdapter extends RecyclerView.Adapter<OnSiteRecentAdapter.ViewHolder> {
    private OnRecentTaskClickHandler onItemClickHandler;
    private final Context mContext;
    private List<RecentActivityViewModel> items = null;
    private Boolean isSwiped = false;

    public OnSiteRecentAdapter(Context context) {
        if (items == null) {
            items = new ArrayList<>();
        }
        this.mContext = context;
    }

    @Override
    public OnSiteRecentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        OnsiteRecentAdapterBinding mOnsiteRecentAdapterBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.onsite_recent_adapter, parent, false);
        return new OnSiteRecentAdapter.ViewHolder(mOnsiteRecentAdapterBinding);
    }

    @Override
    public void onBindViewHolder(final OnSiteRecentAdapter.ViewHolder holder, final int position) {
        int[] attrs = new int[]{R.attr.selectableItemBackground};
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs);
        int backgroundResource = typedArray.getResourceId(0, 0);
        holder.mOnsiteRecentAdapterBinding.swipemain.setBackgroundResource(backgroundResource);
        ((GradientDrawable) holder.mOnsiteRecentAdapterBinding.innerAdd.getBackground()).setColor(mContext.getResources().getColor(R.color.outerBlue));
        ((GradientDrawable) holder.mOnsiteRecentAdapterBinding.outerAdd.getBackground()).setColor(mContext.getResources().getColor(R.color.innerBlue));
        ((GradientDrawable) holder.mOnsiteRecentAdapterBinding.innerNotDone.getBackground()).setColor(mContext.getResources().getColor(R.color.taskPink));
        ((GradientDrawable) holder.mOnsiteRecentAdapterBinding.outerNotdone.getBackground()).setColor(mContext.getResources().getColor(R.color.red));
        if (items.get(position).getActivityDetails() != null && items.get(position).getActivityDetails().size() > 0) {
            try {
                String dt = items.get(position).getActivityDetails().get(0).getStartTime();
                String time = AppUtils.formatTime(dt, "hh:mm aa");
                holder.mOnsiteRecentAdapterBinding.txtTime.setText(time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        String service = items.get(position).getServiceType().replace(",", ", ");
        holder.mOnsiteRecentAdapterBinding.txtTitle.setText(items.get(position).getAreaSubType());
        holder.mOnsiteRecentAdapterBinding.txtArea.setText(items.get(position).getAreaType());
        holder.mOnsiteRecentAdapterBinding.txtServiceType.setText(service);
        if (items.get(position).getServiceType().equalsIgnoreCase("Not Done")) {
            holder.mOnsiteRecentAdapterBinding.bgView.setBackgroundColor(mContext.getResources().getColor(R.color.red));
        } else {
            holder.mOnsiteRecentAdapterBinding.bgView.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
        }

    }

    public void setOnItemClickHandler(OnRecentTaskClickHandler onItemClickHandler) {
        this.onItemClickHandler = onItemClickHandler;
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setData(List<OnSiteRecent> data) {
        items.clear();
        for (int index = 0; index < data.size(); index++) {
            RecentActivityViewModel recentActivityViewModel = new RecentActivityViewModel();
            recentActivityViewModel.clone(data.get(index));
            items.add(recentActivityViewModel);
        }
    }

    public void addData(List<OnSiteRecent> data) {
        items.clear();
        for (int index = 0; index < data.size(); index++) {
            RecentActivityViewModel recentActivityViewModel = new RecentActivityViewModel();
            recentActivityViewModel.clone(data.get(index));
            items.add(recentActivityViewModel);
        }
    }

    public void removeAll() {
        items.removeAll(items);
        notifyDataSetChanged();
    }

    public RecentActivityViewModel getItem(int position) {
        return items.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final OnsiteRecentAdapterBinding mOnsiteRecentAdapterBinding;

        public ViewHolder(OnsiteRecentAdapterBinding mOnsiteRecentAdapterBinding) {
            super(mOnsiteRecentAdapterBinding.getRoot());
            this.mOnsiteRecentAdapterBinding = mOnsiteRecentAdapterBinding;
        }
    }
}
