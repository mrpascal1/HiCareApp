package com.ab.hicarerun.adapter;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ab.hicarerun.R;

/**
 * Created by Arjun Bhatt on 1/8/2020.
 */
public class ChemicalViewHolder extends RecyclerView.ViewHolder {
    public TextView chemName;
    public TextView chemConsumption;
    public EditText edtActual;

    public ChemicalViewHolder(View itemView) {
        super(itemView);
        chemName = (TextView) itemView.findViewById(R.id.chem_name);
        chemConsumption = (TextView) itemView.findViewById(R.id.chem_consumption);
        edtActual = (EditText) itemView.findViewById(R.id.edt_actual);

    }
}
