package com.ab.hicarerun.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.LayoutProductCartAdapterBinding;
import com.ab.hicarerun.databinding.LayoutRecommendationsAdapterBinding;
import com.ab.hicarerun.handler.OnCartItemClickHandler;
import com.ab.hicarerun.handler.OnListItemClickHandler;
import com.ab.hicarerun.network.models.ConsulationModel.Recommendations;
import com.ab.hicarerun.network.models.ProductCartModel.ProductCart;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.ab.hicarerun.BaseApplication.getRealm;

/**
 * Created by Arjun Bhatt on 7/23/2020.
 */
public class RecommendationsAdapter extends RecyclerView.Adapter<RecommendationsAdapter.ViewHolder> {

    private OnListItemClickHandler onItemClickHandler;
    private final Context mContext;
    private List<Recommendations> items = null;
    private String type = "";

    public RecommendationsAdapter(Context context, String type) {
        if (items == null) {
            items = new ArrayList<>();
        }
        this.mContext = context;
        this.type = type;
    }

    @NotNull
    @Override
    public RecommendationsAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        LayoutRecommendationsAdapterBinding layoutRecommendationsAdapterBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.layout_recommendations_adapter, parent, false);
        return new RecommendationsAdapter.ViewHolder(layoutRecommendationsAdapterBinding);
    }

    @Override
    public void onBindViewHolder(@NotNull RecommendationsAdapter.ViewHolder holder, final int position) {
        try {
            holder.layoutRecommendationsAdapterBinding.txtTitle.setText(items.get(position).getRecommendationTitle());
            if(items.get(position).getRecommendationDescription()!=null && !items.get(position).getRecommendationDescription().equals("")){
               if(type.equals("TMS")){
                   holder.layoutRecommendationsAdapterBinding.txtDefaultArea.setTypeface(holder.layoutRecommendationsAdapterBinding.txtDefaultArea.getTypeface(), Typeface.BOLD);
                   holder.layoutRecommendationsAdapterBinding.txtDescLabel.setVisibility(View.GONE);
                   holder.layoutRecommendationsAdapterBinding.txtColon.setVisibility(View.GONE);
               }
                holder.layoutRecommendationsAdapterBinding.lnrDescription.setVisibility(View.VISIBLE);
                holder.layoutRecommendationsAdapterBinding.txtDescription.setText(Html.fromHtml(items.get(position).getRecommendationDescription()));
            }else {
                holder.layoutRecommendationsAdapterBinding.lnrDescription.setVisibility(View.GONE);
            }

            if (items.get(position).getChemicalValue() != null && !items.get(position).getChemicalValue().equals("")) {
                holder.layoutRecommendationsAdapterBinding.lnrChemicalValue.setVisibility(View.VISIBLE);
                holder.layoutRecommendationsAdapterBinding.txtChemValue.setText(items.get(position).getChemicalValue());
                holder.layoutRecommendationsAdapterBinding.txtChemValue.setTypeface(holder.layoutRecommendationsAdapterBinding.txtChemValue.getTypeface(), Typeface.BOLD);
            } else {
                holder.layoutRecommendationsAdapterBinding.lnrChemicalValue.setVisibility(View.GONE);
            }

            if(items.get(position).getDefaultArea()!=null && !items.get(position).getDefaultArea().equals("")){
                holder.layoutRecommendationsAdapterBinding.lnrDefaultArea.setVisibility(View.VISIBLE);
                holder.layoutRecommendationsAdapterBinding.txtDefaultArea.setText(items.get(position).getDefaultArea());
            }else {
                holder.layoutRecommendationsAdapterBinding.lnrDefaultArea.setVisibility(View.GONE);
            }

            if(items.get(position).getExtraArea()!=null && !items.get(position).getExtraArea().equals("")){
                holder.layoutRecommendationsAdapterBinding.lnrExtra.setVisibility(View.VISIBLE);
                holder.layoutRecommendationsAdapterBinding.txtExtra.setText(items.get(position).getExtraArea());
            }else {
                holder.layoutRecommendationsAdapterBinding.lnrExtra.setVisibility(View.GONE);
            }

            if(items.get(position).getDuration()!=null && !items.get(position).getDuration().equals("")){
                holder.layoutRecommendationsAdapterBinding.lnrDuration.setVisibility(View.VISIBLE);
                holder.layoutRecommendationsAdapterBinding.txtDuration.setText(items.get(position).getDuration());
            }else {
                holder.layoutRecommendationsAdapterBinding.lnrDuration.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public Recommendations getItem(int position) {
        return items.get(position);
    }

    public void setData(List<Recommendations> data) {
        items.clear();
        items.addAll(data);
    }

    public void removeAll() {
        items.removeAll(items);
        notifyDataSetChanged();
    }

    public void remove(int position) {
        items.remove(items.get(position));
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final LayoutRecommendationsAdapterBinding layoutRecommendationsAdapterBinding;

        public ViewHolder(LayoutRecommendationsAdapterBinding layoutRecommendationsAdapterBinding) {
            super(layoutRecommendationsAdapterBinding.getRoot());
            this.layoutRecommendationsAdapterBinding = layoutRecommendationsAdapterBinding;
        }
    }
}
