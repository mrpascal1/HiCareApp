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
import com.ab.hicarerun.handler.OnListItemClickHandler;
import com.ab.hicarerun.network.models.chemicalmodel.Chemicals;
import com.ab.hicarerun.network.models.generalmodel.GeneralData;
import com.ab.hicarerun.viewmodel.ChemicalViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.realm.RealmResults;

import static com.ab.hicarerun.BaseApplication.getRealm;

public class ChemicalRecycleAdapter extends RecyclerView.Adapter<ChemicalRecycleAdapter.ViewHolder> {

    private OnListItemClickHandler onItemClickHandler;
    private final Context mContext;
    private static List<ChemicalViewModel> items = null;
    private static HashMap<Integer, String> map = new HashMap();
    private Boolean isVerified = false;
    private OnEditTextChanged onEditTextChanged;
    private int ChemicalNo = 0;
    private int standardChem = 0;
    private Boolean isCombined = false;
    private Boolean showStadardChemicals = false;
    private String type = "";


    public ChemicalRecycleAdapter(Context context, Boolean isCombined, Boolean showStandardChemicals, String type, OnEditTextChanged onEditTextChanged) {
        if (items == null) {
            items = new ArrayList<>();
        }
        this.mContext = context;
        this.onEditTextChanged = onEditTextChanged;
        this.isCombined = isCombined;
        this.showStadardChemicals = showStandardChemicals;
        this.type = type;
    }


    @NotNull
    @Override
    public ChemicalRecycleAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        ChemicalRecycleRowBinding mChemicalRecycleRowBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.chemical_recycle_row, parent, false);
        return new ChemicalRecycleAdapter.ViewHolder(mChemicalRecycleRowBinding);
    }

    @Override
    public void onBindViewHolder(@NotNull final ChemicalRecycleAdapter.ViewHolder holder, final int position) {
        try {
            final ChemicalViewModel model = items.get(position);
            holder.mChemicalRecycleRowBinding.chemName.setText(model.getName());
            holder.mChemicalRecycleRowBinding.chemConsumption.setText(model.getConsumption().toLowerCase());
            String unit = model.getConsumption().toLowerCase();
            holder.mChemicalRecycleRowBinding.chemStandard.setText(model.getStandard() + " " + unit);
            holder.mChemicalRecycleRowBinding.chemConsumption.setVisibility(View.GONE);
//            if (showStadardChemicals) {
//                holder.mChemicalRecycleRowBinding.chemStandard.setVisibility(View.VISIBLE);
//            } else {
//                holder.mChemicalRecycleRowBinding.chemStandard.setVisibility(View.GONE);
//            }


            if (type.equals("Standard")) {
                holder.mChemicalRecycleRowBinding.lnrActual.setVisibility(View.GONE);
                holder.mChemicalRecycleRowBinding.chemStandard.setVisibility(View.VISIBLE);
                holder.mChemicalRecycleRowBinding.lnrType.setVisibility(View.GONE);
                holder.mChemicalRecycleRowBinding.chemType.setText(model.getChemType());
                holder.mChemicalRecycleRowBinding.serviceArea.setText("(" + model.getServiceArea() + ")");
                holder.mChemicalRecycleRowBinding.serviceArea.setVisibility(View.GONE);
            } else {
                holder.mChemicalRecycleRowBinding.lnrActual.setVisibility(View.VISIBLE);
                holder.mChemicalRecycleRowBinding.chemStandard.setVisibility(View.VISIBLE);
                holder.mChemicalRecycleRowBinding.chemType.setText(model.getChemType());
                holder.mChemicalRecycleRowBinding.lnrType.setVisibility(View.VISIBLE);
                if (isCombined) {
                    holder.mChemicalRecycleRowBinding.serviceArea.setVisibility(View.VISIBLE);
                    holder.mChemicalRecycleRowBinding.serviceArea.setText("(" + model.getServiceArea() + ")");
                }
            }

            RealmResults<GeneralData> mGeneralRealmData =
                    getRealm().where(GeneralData.class).findAll();
            if (mGeneralRealmData != null && mGeneralRealmData.size() > 0) {
                assert mGeneralRealmData.get(0) != null;
                isVerified = mGeneralRealmData.get(0).getAutoSubmitChemicals();
                assert mGeneralRealmData.get(0) != null;
                String status = mGeneralRealmData.get(0).getSchedulingStatus();

                if (isVerified || status.equals("Completed")) {
                    holder.mChemicalRecycleRowBinding.edtActual.setText(model.getEdtActual());
                    holder.mChemicalRecycleRowBinding.edtActual.setTextColor(Color.parseColor("#000000"));
                    holder.mChemicalRecycleRowBinding.edtActual.setEnabled(false);
                } else if (status.equals("Incomplete")) {
                    holder.mChemicalRecycleRowBinding.edtActual.setText("-");
                    holder.mChemicalRecycleRowBinding.edtActual.setTextColor(Color.parseColor("#808080"));
                    holder.mChemicalRecycleRowBinding.edtActual.setEnabled(false);
                } else if (status.equals("Dispatched")) {
                    holder.mChemicalRecycleRowBinding.edtActual.setEnabled(false);
                    holder.mChemicalRecycleRowBinding.edtActual.setBackgroundResource(R.drawable.disable_edit_borders);
                } else {
//                    if (model.getActual() != null) {
//                        holder.mChemicalRecycleRowBinding.edtActual.setText(model.getEdtActual());
//                    } else {
//                        holder.mChemicalRecycleRowBinding.edtActual.setText("");
//                    }

                    holder.mChemicalRecycleRowBinding.edtActual.requestFocus();
                    holder.mChemicalRecycleRowBinding.edtActual.setOnFocusChangeListener((view, b) -> {
                        try {
                            onEditTextChanged.onTextChanged(position, holder.mChemicalRecycleRowBinding.edtActual.getText().toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });

                    holder.mChemicalRecycleRowBinding.edtActual.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                        try {
//                            onEditTextChanged.onTextChanged(position, s.toString());
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            try {
                                onEditTextChanged.onTextChanged(position, s.toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    });

                    if (holder.mChemicalRecycleRowBinding.edtActual.getText().toString().length() != 0) {
                        try {
                            onEditTextChanged.onTextChanged(position, holder.mChemicalRecycleRowBinding.edtActual.getText().toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (Exception e) {
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

        private final ChemicalRecycleRowBinding mChemicalRecycleRowBinding;

        public ViewHolder(ChemicalRecycleRowBinding mChemicalRecycleRowBinding) {
            super(mChemicalRecycleRowBinding.getRoot());
            this.mChemicalRecycleRowBinding = mChemicalRecycleRowBinding;
        }
    }

    public interface OnEditTextChanged {
        void onTextChanged(int position, String charSeq);
    }
}

