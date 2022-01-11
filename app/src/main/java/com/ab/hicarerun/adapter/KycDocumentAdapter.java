package com.ab.hicarerun.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.LayoutKycAdapterBinding;
import com.ab.hicarerun.handler.OnKycClickHandler;
import com.ab.hicarerun.handler.OnListItemClickHandler;
import com.ab.hicarerun.network.models.kycmodel.DocumentData;
import com.ab.hicarerun.viewmodel.KycDocumentViewModel;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arjun Bhatt on 9/23/2020.
 */
public class KycDocumentAdapter extends RecyclerView.Adapter<KycDocumentAdapter.ViewHolder> {
    private OnListItemClickHandler onItemClickHandler;
    private OnKycClickHandler mOnKycClickHandler;
    private final Context mContext;
    private List<KycDocumentViewModel> items = null;
    private static final int RESOURCE_REQ = 1000;


    public KycDocumentAdapter(Context context) {
        if (items == null) {
            items = new ArrayList<>();
        }
        this.mContext = context;
    }

    @NotNull
    @Override
    public KycDocumentAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        LayoutKycAdapterBinding mLayoutKycAdapterBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.layout_kyc_adapter, parent, false);
        return new KycDocumentAdapter.ViewHolder(mLayoutKycAdapterBinding);
    }

    @Override
    public void onBindViewHolder(@NotNull KycDocumentAdapter.ViewHolder holder, final int position) {
        try {
            holder.mLayoutKycAdapterBinding.txtTypeName.setTypeface(holder.mLayoutKycAdapterBinding.txtTypeName.getTypeface(), Typeface.BOLD);
            holder.mLayoutKycAdapterBinding.txtCardNumber.setText(items.get(position).getDocument_No());
            holder.mLayoutKycAdapterBinding.txtTypeName.setText(items.get(position).getRecord_Type());
            Picasso.get().load(items.get(position).getDocument_Url()).into(holder.mLayoutKycAdapterBinding.imgType);
            holder.mLayoutKycAdapterBinding.btnView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnKycClickHandler.onViewImageClicked(position);
                }
            });
            holder.mLayoutKycAdapterBinding.btnDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnKycClickHandler.onDownloadClicked(position);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setOnItemClickHandler(OnListItemClickHandler onItemClickHandler) {
        this.onItemClickHandler = onItemClickHandler;
    }

    public void setOnKycClickHandler(OnKycClickHandler mOnKycClickHandler) {
        this.mOnKycClickHandler = mOnKycClickHandler;
    }
    @Override
    public int getItemCount() {
        return items.size();
    }

    public KycDocumentViewModel getItem(int position) {
        return items.get(position);
    }


    //
    public void addData(List<DocumentData> data) {
        items.clear();
        for (int index = 0; index < data.size(); index++) {
            KycDocumentViewModel kycDocumentViewModel = new KycDocumentViewModel();
            kycDocumentViewModel.clone(data.get(index));
            items.add(kycDocumentViewModel);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final LayoutKycAdapterBinding mLayoutKycAdapterBinding;

        public ViewHolder(LayoutKycAdapterBinding mLayoutKycAdapterBinding) {
            super(mLayoutKycAdapterBinding.getRoot());
            this.mLayoutKycAdapterBinding = mLayoutKycAdapterBinding;
        }
    }

}
