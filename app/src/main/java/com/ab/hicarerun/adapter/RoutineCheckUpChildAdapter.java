package com.ab.hicarerun.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.LayoutRoutineCheckChildAdapterBinding;
import com.ab.hicarerun.handler.OnListItemClickHandler;
import com.ab.hicarerun.network.models.routinemodel.RoutineNoValue;
import com.ab.hicarerun.network.models.routinemodel.RoutineOption;
import com.ab.hicarerun.network.models.routinemodel.ValueNo;
import com.ab.hicarerun.network.models.routinemodel.ValueYes;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arjun Bhatt on 8/18/2020.
 */
public class RoutineCheckUpChildAdapter extends RecyclerView.Adapter<RoutineCheckUpChildAdapter.ViewHolder> {
    private OnListItemClickHandler onItemClickHandler;
    private final Context mContext;
    private List<RoutineOption> items = null;
    private int selectedPos = -1;
    private ValueYes valueYes;
    private ValueNo valueNo;
    private OnCheckChanged onCheckChanged;


    public RoutineCheckUpChildAdapter(Context mContext, OnCheckChanged onCheckChanged) {
        if (items == null) {
            items = new ArrayList<>();
        }
        this.mContext = mContext;
        this.onCheckChanged = onCheckChanged;
    }

    @NotNull
    @Override
    public RoutineCheckUpChildAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        LayoutRoutineCheckChildAdapterBinding mLayoutRoutineCheckChildAdapterBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.layout_routine_check_child_adapter, parent, false);
        return new RoutineCheckUpChildAdapter.ViewHolder(mLayoutRoutineCheckChildAdapterBinding);
    }


    @Override
    public void onBindViewHolder(@NotNull RoutineCheckUpChildAdapter.ViewHolder holder, final int position) {
        try {
            holder.mLayoutRoutineCheckChildAdapterBinding.rbAnswers.setText(items.get(position).getValue());
            holder.mLayoutRoutineCheckChildAdapterBinding.rbAnswers.setChecked(items.get(position).getIsSelected());

            if (items.get(position).getIsSelected()) {
                if (position == 0) {
                    if (valueYes != null) {
                        if (valueYes.getType().equals("TextBox")) {
                            holder.mLayoutRoutineCheckChildAdapterBinding.lnrSpinner.setVisibility(View.GONE);
                            holder.mLayoutRoutineCheckChildAdapterBinding.edtValue.setVisibility(View.VISIBLE);
                            holder.mLayoutRoutineCheckChildAdapterBinding.edtValue.setText(valueYes.getStr());
                            holder.mLayoutRoutineCheckChildAdapterBinding.edtValue.setText(valueYes.getStr());

                        } else {
                            holder.mLayoutRoutineCheckChildAdapterBinding.lnrSpinner.setVisibility(View.VISIBLE);
                            holder.mLayoutRoutineCheckChildAdapterBinding.edtValue.setVisibility(View.GONE);
                            addSpinnerData(holder.mLayoutRoutineCheckChildAdapterBinding.spnValue, position, valueYes.getValue());
                            holder.mLayoutRoutineCheckChildAdapterBinding.spnValue.setSelection(valueYes.getSpnPosition());
                        }
                    }
                } else if (position == 1) {
                    if (valueNo != null) {
                        if (valueNo.getType().equals("TextBox")) {
                            holder.mLayoutRoutineCheckChildAdapterBinding.lnrSpinner.setVisibility(View.GONE);
                            holder.mLayoutRoutineCheckChildAdapterBinding.edtValue.setVisibility(View.VISIBLE);
                            holder.mLayoutRoutineCheckChildAdapterBinding.edtValue.setText(valueNo.getStr());

                        } else {
                            holder.mLayoutRoutineCheckChildAdapterBinding.lnrSpinner.setVisibility(View.VISIBLE);
                            holder.mLayoutRoutineCheckChildAdapterBinding.edtValue.setVisibility(View.GONE);
                            addSpinnerData(holder.mLayoutRoutineCheckChildAdapterBinding.spnValue, position, valueNo.getValue());
                            holder.mLayoutRoutineCheckChildAdapterBinding.spnValue.setSelection(valueNo.getSpnPosition());

                        }
                    }
                }
            } else {
                holder.mLayoutRoutineCheckChildAdapterBinding.lnrSpinner.setVisibility(View.GONE);
                holder.mLayoutRoutineCheckChildAdapterBinding.edtValue.setVisibility(View.GONE);
            }


            holder.mLayoutRoutineCheckChildAdapterBinding.edtValue.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable value) {
                    onCheckChanged.onChecked(position, true, value.toString());
                    if (position == 0) {
                        valueYes.setStr(value.toString());
                    } else if (position == 1) {
                        valueNo.setStr(value.toString());
                    }
                }
            });

            holder.mLayoutRoutineCheckChildAdapterBinding.rbAnswers.setOnClickListener(v -> {
                try {
                    selectedPos = position;
                    holder.mLayoutRoutineCheckChildAdapterBinding.rbAnswers.setChecked(true);
                    onCheckChanged.onChecked(position, true, "NA");

                    if (selectedPos == 0) {
                        if (valueYes != null) {
                            try {
                                Log.i("RoutineChild", valueYes.getType() + "");
                            } catch (Exception e) {
                                Log.i("RoutineChild", e.getMessage());
                                e.printStackTrace();
                            }
                            if (valueYes.getType().equals("TextBox")) {
                                holder.mLayoutRoutineCheckChildAdapterBinding.edtValue.setVisibility(View.VISIBLE);
                                holder.mLayoutRoutineCheckChildAdapterBinding.lnrSpinner.setVisibility(View.GONE);
                            } else {
                                holder.mLayoutRoutineCheckChildAdapterBinding.edtValue.setVisibility(View.GONE);
                                holder.mLayoutRoutineCheckChildAdapterBinding.lnrSpinner.setVisibility(View.VISIBLE);
                                addSpinnerData(holder.mLayoutRoutineCheckChildAdapterBinding.spnValue, position, valueYes.getValue());
                            }
                        }
                    } else if (selectedPos == 1) {
                        if (valueNo != null) {
                            try {
                                Log.i("RoutineChild", valueNo.getType() + "");
                            } catch (Exception e) {
                                Log.i("RoutineChild", e.getMessage());
                                e.printStackTrace();
                            }
                            if (valueNo.getType().equals("DropDown")) {
                                holder.mLayoutRoutineCheckChildAdapterBinding.edtValue.setVisibility(View.GONE);
                                holder.mLayoutRoutineCheckChildAdapterBinding.lnrSpinner.setVisibility(View.VISIBLE);
                                addSpinnerData(holder.mLayoutRoutineCheckChildAdapterBinding.spnValue, position, valueNo.getValue());

                            } else {
                                holder.mLayoutRoutineCheckChildAdapterBinding.edtValue.setVisibility(View.VISIBLE);
                                holder.mLayoutRoutineCheckChildAdapterBinding.lnrSpinner.setVisibility(View.GONE);
                            }
                        }
                    }
                    for (int i = 0; i < items.size(); i++) {
                        if (selectedPos != i) {
                            holder.mLayoutRoutineCheckChildAdapterBinding.rbAnswers.setChecked(false);
                            items.get(i).setIsSelected(false);
                        }
                    }

                    onItemClickHandler.onItemClick(position);
                    notifyDataSetChanged();
                } catch (Exception e) {
                    Log.i("Click", e.getMessage());
                }

            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addSpinnerData(AppCompatSpinner spnValue, int position, List<RoutineNoValue> value) {
        List<String> valueItems = new ArrayList<>();
        for (int i = 0; i < value.size(); i++) {
            valueItems.add(value.get(i).getValue());
        }

        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(mContext,
                R.layout.layout_routine_spinner_adapter, valueItems);
        statusAdapter.setDropDownViewResource(R.layout.spinner_popup);
        spnValue.setAdapter(statusAdapter);
        spnValue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                onCheckChanged.onChecked(position, true, statusAdapter.getItem(pos));
                if (position == 0) {
                    valueYes.setStr(statusAdapter.getItem(pos));
                } else if (position == 1) {
                    valueNo.setStr(statusAdapter.getItem(pos));
                }
                valueYes.setSpnPosition(pos);
                valueNo.setSpnPosition(pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void setOnItemClickHandler(OnListItemClickHandler onItemClickHandler) {
        this.onItemClickHandler = onItemClickHandler;
    }

    public RoutineOption getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    //
    public void addData(List<RoutineOption> data, ValueYes valueYes, ValueNo valueNo) {
        items.clear();
        items.addAll(data);
        this.valueYes = valueYes;
        this.valueNo = valueNo;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final LayoutRoutineCheckChildAdapterBinding mLayoutRoutineCheckChildAdapterBinding;

        public ViewHolder(LayoutRoutineCheckChildAdapterBinding mLayoutRoutineCheckChildAdapterBinding) {
            super(mLayoutRoutineCheckChildAdapterBinding.getRoot());
            this.mLayoutRoutineCheckChildAdapterBinding = mLayoutRoutineCheckChildAdapterBinding;
        }
    }

    public interface OnCheckChanged {
        void onChecked(int position, boolean isChecked, String value);
    }
}

