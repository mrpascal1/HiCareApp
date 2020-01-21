package com.ab.hicarerun.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
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
import com.ab.hicarerun.databinding.ReferralListAdapterBinding;
import com.ab.hicarerun.handler.OnAddActivityClickHandler;
import com.ab.hicarerun.handler.OnDeleteListItemClickHandler;
import com.ab.hicarerun.network.models.OnSiteModel.OnSiteArea;
import com.ab.hicarerun.network.models.ReferralModel.ReferralList;
import com.ab.hicarerun.utils.AppUtils;
import com.ab.hicarerun.viewmodel.AccountAreaViewModel;
import com.ab.hicarerun.viewmodel.ReferralListViewModel;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arjun Bhatt on 12/16/2019.
 */
public class OnSiteTasksAdapter extends RecyclerView.Adapter<OnSiteTasksAdapter.ViewHolder> {

    private OnAddActivityClickHandler onItemClickHandler;
    private final Context mContext;
    private List<AccountAreaViewModel> items = null;

    public OnSiteTasksAdapter(Context context) {
        if (items == null) {
            items = new ArrayList<>();
        }
        this.mContext = context;
    }


    @Override
    public OnSiteTasksAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        OnsiteListAdapterBinding mOnsiteListAdapterBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.onsite_list_adapter, parent, false);
        return new OnSiteTasksAdapter.ViewHolder(mOnsiteListAdapterBinding);
    }

    @Override
    public void onBindViewHolder(OnSiteTasksAdapter.ViewHolder holder, final int position) {
        int[] attrs = new int[]{R.attr.selectableItemBackground};
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs);
        int backgroundResource = typedArray.getResourceId(0, 0);
        holder.mOnsiteListAdapterBinding.swipemain.setBackgroundResource(backgroundResource);
        holder.mOnsiteListAdapterBinding.txtAdd.setBackgroundResource(backgroundResource);
        holder.mOnsiteListAdapterBinding.txtND.setBackgroundResource(backgroundResource);
//        ((GradientDrawable) holder.mOnsiteListAdapterBinding.innerAdd.getBackground()).setColor(mContext.getResources().getColor(R.color.taskOuter));
//        ((GradientDrawable) holder.mOnsiteListAdapterBinding.outerAdd.getBackground()).setColor(mContext.getResources().getColor(R.color.taskInner));
//        ((GradientDrawable) holder.mOnsiteListAdapterBinding.innerNotDone.getBackground()).setColor(mContext.getResources().getColor(R.color.taskPink));
//        ((GradientDrawable) holder.mOnsiteListAdapterBinding.outerNotdone.getBackground()).setColor(mContext.getResources().getColor(R.color.red));
        holder.mOnsiteListAdapterBinding.lnrAdd.setOnClickListener(v -> onItemClickHandler.onAddActivityClick(position));
        holder.mOnsiteListAdapterBinding.lnrNotDone.setOnClickListener(view -> onItemClickHandler.onNotDoneClick(position));
        String service = items.get(position).getServiceName().replace(";",", ");
        holder.mOnsiteListAdapterBinding.txtServiceType.setText(service);
        holder.mOnsiteListAdapterBinding.txtArea.setText(items.get(position).getArea());
        holder.mOnsiteListAdapterBinding.txtSubArea.setText(items.get(position).getSubArea());
        if(items.get(position).getLastActivity()!=null && !items.get(position).getLastActivity().equals("")){
            try {
                String mDate = AppUtils.reFormatDateAndTime(items.get(position).getLastActivity(),"dd MMM, yyyy hh:mm aa");
                holder.mOnsiteListAdapterBinding.txtLastActivity.setText(items.get(position).getLastActivity());

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else {
            holder.mOnsiteListAdapterBinding.txtLastActivity.setText("NA");        }
    }

    public void setOnItemClickHandler(OnAddActivityClickHandler onItemClickHandler) {
        this.onItemClickHandler = onItemClickHandler;
    }


    @Override
    public int getItemCount() {
        return items.size();

    }

    public void setData(List<OnSiteArea> data) {
        items.clear();
        for (int index = 0; index < data.size(); index++) {
            AccountAreaViewModel accountAreaViewModel = new AccountAreaViewModel();
            accountAreaViewModel.clone(data.get(index));
            items.add(accountAreaViewModel);
        }
    }

    public void addData(List<OnSiteArea> data) {
        items.clear();
        for (int index = 0; index < data.size(); index++) {
            AccountAreaViewModel accountAreaViewModel = new AccountAreaViewModel();
            accountAreaViewModel.clone(data.get(index));
            items.add(accountAreaViewModel);
        }
    }

    public void removeAll() {
        items.removeAll(items);
        notifyDataSetChanged();
    }

    public AccountAreaViewModel getItem(int position) {
        return items.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final OnsiteListAdapterBinding mOnsiteListAdapterBinding;

        public ViewHolder(OnsiteListAdapterBinding mOnsiteListAdapterBinding) {
            super(mOnsiteListAdapterBinding.getRoot());
            this.mOnsiteListAdapterBinding = mOnsiteListAdapterBinding;
        }
    }
}
