package com.ab.hicarerun.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.ChemicalRecycleRowBinding;
import com.ab.hicarerun.databinding.LayoutChemicalsDialogAdapterBinding;
import com.ab.hicarerun.handler.OnListItemClickHandler;
import com.ab.hicarerun.network.models.ChemicalModel.Chemicals;
import com.ab.hicarerun.network.models.GeneralModel.GeneralData;
import com.ab.hicarerun.viewmodel.ChemicalViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.realm.RealmResults;

import static com.ab.hicarerun.BaseApplication.getRealm;

/**
 * Created by Arjun Bhatt on 3/12/2020.
 */
public class ChemicalDialogAdapter extends RecyclerView.Adapter<ChemicalDialogAdapter.ViewHolder> {

    private OnListItemClickHandler onItemClickHandler;
    private final Context mContext;
    private static List<ChemicalViewModel> items = null;
    private Boolean isCombined = false;


    public ChemicalDialogAdapter(Context context, Boolean isCombined) {
        if (items == null) {
            items = new ArrayList<>();
        }
        this.mContext = context;
        this.isCombined = isCombined;
    }


    @NotNull
    @Override
    public ChemicalDialogAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        LayoutChemicalsDialogAdapterBinding mChemicalRecycleRowBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.layout_chemicals_dialog_adapter, parent, false);
        return new ChemicalDialogAdapter.ViewHolder(mChemicalRecycleRowBinding);
    }

    @Override
    public void onBindViewHolder(@NotNull final ChemicalDialogAdapter.ViewHolder holder, final int position) {
        try {
            final ChemicalViewModel model = items.get(position);
            holder.mChemicalRecycleRowBinding.chemName.setText(model.getName());
            holder.mChemicalRecycleRowBinding.chemConsumption.setText(model.getConsumption());
            holder.mChemicalRecycleRowBinding.chemStandard.setText(model.getStandard());
            if (isCombined) {
                holder.mChemicalRecycleRowBinding.lnrType.setVisibility(View.VISIBLE);
                holder.mChemicalRecycleRowBinding.chemType.setText(model.getChemType());
                holder.mChemicalRecycleRowBinding.serviceArea.setText("(" + model.getServiceArea() + ")");
            } else {
                holder.mChemicalRecycleRowBinding.lnrType.setVisibility(View.GONE);
            }
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


    public void setData(List<Chemicals> data) {
        items.clear();
        for (int index = 0; index < data.size(); index++) {
            ChemicalViewModel chemicalViewModel = new ChemicalViewModel();
            chemicalViewModel.clone(data.get(index));
            items.add(chemicalViewModel);
        }

    }

    public void addData(List<Chemicals> data) {
        items.clear();
        for (int index = 0; index < data.size(); index++) {
            ChemicalViewModel chemicalViewModel = new ChemicalViewModel();
            chemicalViewModel.clone(data.get(index));
            items.add(chemicalViewModel);
        }
    }

    public ChemicalViewModel getItem(int position) {
        return items.get(position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private final LayoutChemicalsDialogAdapterBinding mChemicalRecycleRowBinding;

        public ViewHolder(LayoutChemicalsDialogAdapterBinding mChemicalRecycleRowBinding) {
            super(mChemicalRecycleRowBinding.getRoot());
            this.mChemicalRecycleRowBinding = mChemicalRecycleRowBinding;
        }
    }

}

