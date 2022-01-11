package com.ab.hicarerun.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.LayoutKarmaAdapterBinding;
import com.ab.hicarerun.handler.OnListItemClickHandler;
import com.ab.hicarerun.network.models.karmamodel.KarmaHistoryDetails;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arjun Bhatt on 10/12/2020.
 */
public class KarmaAdapter extends RecyclerView.Adapter<KarmaAdapter.ViewHolder> {
    private OnListItemClickHandler onItemClickHandler;
    private final Context mContext;
    private List<KarmaHistoryDetails> items = null;

    public KarmaAdapter(Context context) {
        if (items == null) {
            items = new ArrayList<>();
        }
        this.mContext = context;
    }

    @NotNull
    @Override
    public KarmaAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        LayoutKarmaAdapterBinding mLayoutKarmaAdapterBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.layout_karma_adapter, parent, false);
        return new KarmaAdapter.ViewHolder(mLayoutKarmaAdapterBinding);
    }


    @Override
    public void onBindViewHolder(@NotNull KarmaAdapter.ViewHolder holder, final int position) {
        try {
            holder.mLayoutKarmaAdapterBinding.txtPoints.setText(String.valueOf(items.get(position).getPointsDeducted()));
            holder.mLayoutKarmaAdapterBinding.txtDescription.setText(items.get(position).getScoreDescription());
            holder.mLayoutKarmaAdapterBinding.txtTitle.setText(items.get(position).getScoreName());
            holder.mLayoutKarmaAdapterBinding.txtTitle.setTypeface(holder.mLayoutKarmaAdapterBinding.txtTitle.getTypeface(), Typeface.BOLD);
            holder.mLayoutKarmaAdapterBinding.txtProgress.setTypeface(holder.mLayoutKarmaAdapterBinding.txtProgress.getTypeface(), Typeface.BOLD);
            holder.mLayoutKarmaAdapterBinding.progressBar.setProgress(items.get(position).getPointsDeducted());
            holder.mLayoutKarmaAdapterBinding.txtProgress.setText(String.valueOf(items.get(position).getPointsDeducted()));
            holder.mLayoutKarmaAdapterBinding.txtPoints.setTextColor(mContext.getResources().getColor(R.color.red));
            holder.mLayoutKarmaAdapterBinding.txtPtsLabel.setTextColor(mContext.getResources().getColor(R.color.red));
            if(items.get(position).getVideoURL()!=null && !items.get(position).getVideoURL().equals("")){
                holder.mLayoutKarmaAdapterBinding.thumbnail.setVisibility(View.VISIBLE);
            }else {
                holder.mLayoutKarmaAdapterBinding.thumbnail.setVisibility(View.GONE);
            }
            holder.mLayoutKarmaAdapterBinding.thumbnail.setOnClickListener(v -> {
                try {
                    onItemClickHandler.onItemClick(position);
                }catch (Exception e){
                    e.printStackTrace();
                }

            });
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

    public KarmaHistoryDetails getItem(int position) {
        return items.get(position);
    }


    public void setData(List<KarmaHistoryDetails> data) {
        items.clear();
        items.addAll(data);
    }

    //
    public void addData(List<KarmaHistoryDetails> data) {
        items.clear();
        items.addAll(data);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private final LayoutKarmaAdapterBinding mLayoutKarmaAdapterBinding;

        public ViewHolder(LayoutKarmaAdapterBinding mLayoutKarmaAdapterBinding) {
            super(mLayoutKarmaAdapterBinding.getRoot());
            this.mLayoutKarmaAdapterBinding = mLayoutKarmaAdapterBinding;
        }
    }
}

