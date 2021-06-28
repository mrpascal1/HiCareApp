package com.ab.hicarerun.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.ItemChemicalAreaParentAdapterBinding;
import com.ab.hicarerun.handler.OnAddChemicalActivity;
import com.ab.hicarerun.network.models.ChemicalModel.ActivityData;
import com.ab.hicarerun.network.models.ChemicalModel.ServiceData;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.VISIBLE;

/**
 * Created by Arjun Bhatt on 6/27/2021.
 */
public class ChemicalAreaParentAdapter extends RecyclerView.Adapter<ChemicalAreaParentAdapter.ViewHolder> implements OnAddChemicalActivity {
    private OnAddChemicalActivity onAddChemicalActivity;
    private final Context mContext;
    private List<ServiceData> items = null;

    public ChemicalAreaParentAdapter(Context context) {
        this.mContext = context;
        if (items == null) {
            items = new ArrayList<>();
        }
    }

    @NotNull
    @Override
    public ChemicalAreaParentAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        ItemChemicalAreaParentAdapterBinding mItemChemicalAreaParentAdapterBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_chemical_area_parent_adapter, parent, false);
        return new ChemicalAreaParentAdapter.ViewHolder(mItemChemicalAreaParentAdapterBinding);
    }

    @Override
    public void onBindViewHolder(@NotNull ChemicalAreaParentAdapter.ViewHolder holder, final int position) {
        try {
            holder.mItemChemicalAreaParentAdapterBinding.txtHeader.setText(items.get(position).getService());
            holder.mItemChemicalAreaParentAdapterBinding.txtHeader.setTypeface(holder.mItemChemicalAreaParentAdapterBinding.txtHeader.getTypeface(), Typeface.BOLD);
            ChemicalAreaChildAdapter childAdapter = new ChemicalAreaChildAdapter(mContext, items.get(position).getArea(), position);
            holder.mItemChemicalAreaParentAdapterBinding.recyclerChild.setLayoutManager(new LinearLayoutManager(mContext));
            holder.mItemChemicalAreaParentAdapterBinding.recyclerChild.setHasFixedSize(true);
            holder.mItemChemicalAreaParentAdapterBinding.recyclerChild.setClipToPadding(false);
            holder.mItemChemicalAreaParentAdapterBinding.recyclerChild.setAdapter(childAdapter);
            childAdapter.setmOnAddChemicalActivity(this);
            childAdapter.notifyDataSetChanged();

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.mItemChemicalAreaParentAdapterBinding.recyclerChild.getVisibility() == VISIBLE) {
                        holder.mItemChemicalAreaParentAdapterBinding.recyclerChild.setVisibility(View.GONE);
                        holder.mItemChemicalAreaParentAdapterBinding.imgArrow.setRotation(180);
                    } else {
                        holder.mItemChemicalAreaParentAdapterBinding.recyclerChild.setVisibility(VISIBLE);
                        holder.mItemChemicalAreaParentAdapterBinding.imgArrow.setRotation(360);
                    }
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setOnAddChemicalActivity(OnAddChemicalActivity onAddChemicalActivity) {
        this.onAddChemicalActivity = onAddChemicalActivity;
    }

    public void addData(List<ServiceData> data) {
        items.clear();
        items.addAll(data);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public ServiceData getItem(int position) {
        return items.get(position);
    }

    @Override
    public void onAddActivityClicked(int parentPosition, int childPosition, List<ActivityData> activity) {
        onAddChemicalActivity.onAddActivityClicked(parentPosition, childPosition, activity);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemChemicalAreaParentAdapterBinding mItemChemicalAreaParentAdapterBinding;

        public ViewHolder(ItemChemicalAreaParentAdapterBinding mItemChemicalAreaParentAdapterBinding) {
            super(mItemChemicalAreaParentAdapterBinding.getRoot());
            this.mItemChemicalAreaParentAdapterBinding = mItemChemicalAreaParentAdapterBinding;
        }
    }


}
