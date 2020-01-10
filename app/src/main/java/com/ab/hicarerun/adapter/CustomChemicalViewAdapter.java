package com.ab.hicarerun.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.ab.hicarerun.R;
import com.ab.hicarerun.network.models.ChemicalModel.Chemicals;
import com.ab.hicarerun.network.models.ChemicalModel.MSTChemicals;
import com.ab.hicarerun.network.models.GeneralModel.GeneralData;
import com.ab.hicarerun.viewmodel.ChemicalMSTViewModel;
import com.ab.hicarerun.viewmodel.ChemicalViewModel;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;

import static com.ab.hicarerun.BaseApplication.getRealm;

/**
 * Created by Arjun Bhatt on 1/8/2020.
 */
public class CustomChemicalViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = CustomChemicalViewAdapter.class.getSimpleName();
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static List<ChemicalMSTViewModel> items = null;
    private List<MSTChemicals> itemObjects;
    private OnEditTextChanged onEditTextChanged;
    private final Context mContext;
    private Boolean isVerified = false;

    public CustomChemicalViewAdapter(Context context, OnEditTextChanged onEditTextChanged) {
        if (items == null) {
            items = new ArrayList<>();
        }
        this.mContext = context;
        this.onEditTextChanged = onEditTextChanged;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_chemical_header, parent, false);
            return new HeaderViewHolder(layoutView);
        } else if (viewType == TYPE_ITEM) {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chemical_recycle_row, parent, false);
            return new ChemicalViewHolder(layoutView);
        }
        throw new RuntimeException("No match for " + viewType + ".");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ChemicalMSTViewModel model = items.get(position);
        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).headerTitle.setText(model.getTaskType());
        } else if (holder instanceof ChemicalViewHolder) {
            RealmResults<GeneralData> mGeneralRealmData =
                    getRealm().where(GeneralData.class).findAll();
            for (int i = 0; i < model.getChemicalsList().size(); i++) {
                ((ChemicalViewHolder) holder).chemName.setText(model.getChemicalsList().get(i).getCWFProductName());
                ((ChemicalViewHolder) holder).chemConsumption.setText(model.getChemicalsList().get(i).getConsumption());

                if (mGeneralRealmData != null && mGeneralRealmData.size() > 0) {
                    isVerified = mGeneralRealmData.get(0).getAutoSubmitChemicals();
                    String status = mGeneralRealmData.get(0).getSchedulingStatus();

                    if (isVerified || status.equals("Completed")) {
                        ((ChemicalViewHolder) holder).edtActual.setText(model.getChemicalsList().get(i).getActual_Usage());
                        ((ChemicalViewHolder) holder).edtActual.setTextColor(Color.parseColor("#000000"));
                        ((ChemicalViewHolder) holder).edtActual.setEnabled(false);
                    } else if (status.equals("Incomplete")) {
                        ((ChemicalViewHolder) holder).edtActual.setText("-");
                        ((ChemicalViewHolder) holder).edtActual.setTextColor(Color.parseColor("#808080"));
                        ((ChemicalViewHolder) holder).edtActual.setEnabled(false);
                    } else if (status.equals("Dispatched")) {
                        ((ChemicalViewHolder) holder).edtActual.setEnabled(false);
                        ((ChemicalViewHolder) holder).edtActual.setBackgroundResource(R.drawable.disable_edit_borders);
                    } else {
                        ((ChemicalViewHolder) holder).edtActual.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

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
                    }
                }

            }
        }
    }

    public void setData(List<MSTChemicals> data) {
        items.clear();
        for (int index = 0; index < data.size(); index++) {
            ChemicalMSTViewModel chemicalViewModel = new ChemicalMSTViewModel();
            chemicalViewModel.clone(data.get(index));
            items.add(chemicalViewModel);
        }
    }

    public void addData(List<MSTChemicals> data) {
        for (int index = 0; index < data.size(); index++) {
            ChemicalMSTViewModel chemicalViewModel = new ChemicalMSTViewModel();
            chemicalViewModel.clone(data.get(index));
            items.add(chemicalViewModel);
        }
    }
//    private MSTChemicals getItem(int position) {
//        return itemObjects.get(position);
//    }

    public ChemicalMSTViewModel getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getItemCount() {
        return items.get(0).getChemicalsList().size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    public interface OnEditTextChanged {
        void onTextChanged(int position, String charSeq);
    }
}