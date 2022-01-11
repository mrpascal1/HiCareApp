package com.ab.hicarerun.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.LayoutLifelineProgressAdapterBinding;
import com.ab.hicarerun.handler.OnListItemClickHandler;
import com.ab.hicarerun.handler.OnRewardtemClickHandler;
import com.ab.hicarerun.network.models.karmamodel.KarmaHistoryData;
import com.ab.hicarerun.viewmodel.KarmaProgressViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arjun Bhatt on 10/13/2020.
 */
public class KarmaProgressAdapter extends RecyclerView.Adapter<KarmaProgressAdapter.ViewHolder> {
    private OnListItemClickHandler onItemClickHandler;
    private OnRewardtemClickHandler onRewardClickItemClickHandler;
    private final Context mContext;
    private List<KarmaProgressViewModel> items = null;

    public KarmaProgressAdapter(Context context) {
        if (items == null) {
            items = new ArrayList<>();
        }
        this.mContext = context;
    }

    @NotNull
    @Override
    public KarmaProgressAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        LayoutLifelineProgressAdapterBinding mLayoutKarmaProgressAdapterBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.layout_lifeline_progress_adapter, parent, false);
        return new KarmaProgressAdapter.ViewHolder(mLayoutKarmaProgressAdapterBinding);
    }


    @Override
    public void onBindViewHolder(@NotNull KarmaProgressAdapter.ViewHolder holder, final int position) {

        try {
            Drawable unwrappedDrawable = AppCompatResources.getDrawable(mContext, R.drawable.ic_heart);
            Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
            holder.mLayoutKarmaProgressAdapterBinding.txtLife.setText(String.valueOf("Life "+items.get(position).getLifeLineIndex()));

            if(items.get(position).getTotalPointsPending() >= 80){
                holder.mLayoutKarmaProgressAdapterBinding.imgPerson.setColorFilter(mContext.getResources().getColor(R.color.karmaGreen));
            }else if(items.get(position).getTotalPointsPending()>= 50 && items.get(position).getTotalPointsPending()<80){
                holder.mLayoutKarmaProgressAdapterBinding.imgPerson.setColorFilter(mContext.getResources().getColor(R.color.amber));
            }
//            else if(!items.get(position).getIsActiveLifeLine() && items.get(position).getTotalPointsPending() == 0){
//                holder.mLayoutKarmaProgressAdapterBinding.imgPerson.setColorFilter(mContext.getResources().getColor(R.color.greyclose));
//            }
            else {
                holder.mLayoutKarmaProgressAdapterBinding.imgPerson.setColorFilter(mContext.getResources().getColor(R.color.red));
            }
            holder.mLayoutKarmaProgressAdapterBinding.txtLife.setTypeface(holder.mLayoutKarmaProgressAdapterBinding.txtLife.getTypeface(), Typeface.BOLD);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickHandler.onItemClick(position);
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setOnItemClickHandler(OnListItemClickHandler onItemClickHandler) {
        this.onItemClickHandler = onItemClickHandler;
    }

    public void setData(List<KarmaHistoryData> data) {
        items.clear();
        for (int index = 0; index < data.size(); index++) {
            KarmaProgressViewModel rewardsViewModel = new KarmaProgressViewModel();
            rewardsViewModel.clone(data.get(index));
            items.add(rewardsViewModel);
        }
    }

    //
    public void addData(List<KarmaHistoryData> data) {
        items.clear();
        for (int index = 0; index < data.size(); index++) {
            KarmaProgressViewModel rewardsViewModel = new KarmaProgressViewModel();
            rewardsViewModel.clone(data.get(index));
            items.add(rewardsViewModel);
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private final LayoutLifelineProgressAdapterBinding mLayoutKarmaProgressAdapterBinding;

        public ViewHolder(LayoutLifelineProgressAdapterBinding mLayoutKarmaProgressAdapterBinding) {
            super(mLayoutKarmaProgressAdapterBinding.getRoot());
            this.mLayoutKarmaProgressAdapterBinding = mLayoutKarmaProgressAdapterBinding;
        }
    }
}

