package com.ab.hicarerun.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ab.hicarerun.R;

/**
 * Created by Arjun Bhatt on 1/8/2020.
 */
public class HeaderViewHolder extends RecyclerView.ViewHolder{
    public TextView headerTitle;
    public HeaderViewHolder(View itemView) {
        super(itemView);
        headerTitle = (TextView)itemView.findViewById(R.id.txtHeader);
    }
}
