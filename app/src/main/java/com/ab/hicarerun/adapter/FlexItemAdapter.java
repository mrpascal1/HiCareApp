package com.ab.hicarerun.adapter;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.LayoutFlexItemBinding;
import com.ab.hicarerun.handler.OnAccountOnsiteClickHandler;
import com.ab.hicarerun.handler.OnListItemClickHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arjun Bhatt on 5/10/2020.
 */
public class FlexItemAdapter extends RecyclerView.Adapter<FlexItemAdapter.ViewHolder> {


    private OnListItemClickHandler onItemClickHandler;
    private final Context mContext;
    private List<String> items = null;

    public FlexItemAdapter(Context context) {
        if (items == null) {
            items = new ArrayList<>();
        }
        this.mContext = context;
    }


    @NotNull
    @Override
    public FlexItemAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        LayoutFlexItemBinding mLayoutFlexItemBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.layout_flex_item, parent, false);
        return new FlexItemAdapter.ViewHolder(mLayoutFlexItemBinding);
    }


    @Override
    public void onBindViewHolder(@NotNull FlexItemAdapter.ViewHolder holder, final int position) {
        holder.layoutFlexItemBinding.text.setText(items.get(position));
        holder.layoutFlexItemBinding.text.setTypeface(null, Typeface.BOLD);
        holder.itemView.setOnClickListener(v -> onItemClickHandler.onItemClick(position));

    }

    public void setOnItemClickHandler(OnListItemClickHandler onItemClickHandler) {
        this.onItemClickHandler = onItemClickHandler;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addData(List<String> data) {
        items.clear();
       items.addAll(data);
    }

    public void removeAll() {
        items.removeAll(items);
        notifyDataSetChanged();
    }

    public String getItem(int position) {
        return items.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final LayoutFlexItemBinding layoutFlexItemBinding;

        public ViewHolder(LayoutFlexItemBinding layoutFlexItemBinding) {
            super(layoutFlexItemBinding.getRoot());
            this.layoutFlexItemBinding = layoutFlexItemBinding;
        }
    }
}
