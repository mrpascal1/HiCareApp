package com.ab.hicarerun.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.AttachmentListAdapterBinding;
import com.ab.hicarerun.databinding.NewAttachmentListAdapterBinding;
import com.ab.hicarerun.handler.OnDeleteListItemClickHandler;
import com.ab.hicarerun.handler.OnListItemClickHandler;
import com.ab.hicarerun.network.models.AttachmentModel.GetAttachmentList;
import com.ab.hicarerun.utils.AppUtils;
import com.ab.hicarerun.viewmodel.AttachmentListViewModel;
import com.bumptech.glide.Glide;


import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arjun Bhatt on 12/2/2019.
 */
public class NewAttachmentListAdapter extends RecyclerView.Adapter<NewAttachmentListAdapter.ViewHolder> {

    private OnDeleteListItemClickHandler onItemClickHandler;
    private final Context mContext;
    private static List<AttachmentListViewModel> items = null;
    private AppCompatImageView imgSelect;
    private AppCompatTextView txtDelcount;
    private String status = "";


    public NewAttachmentListAdapter(Context context, String schedulingStatus) {
        if (items == null) {
            items = new ArrayList<>();
        }
        this.mContext = context;
        this.status = schedulingStatus;
    }


    @NotNull
    @Override
    public NewAttachmentListAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        NewAttachmentListAdapterBinding mAttachmentListAdapterBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.new_attachment_list_adapter, parent, false);
        return new NewAttachmentListAdapter.ViewHolder(mAttachmentListAdapterBinding);
    }

    @Override
    public void onBindViewHolder(@NotNull final NewAttachmentListAdapter.ViewHolder holder, final int position) {
        try {
            final AttachmentListViewModel model = items.get(position);
            int[] attrs = new int[]{R.attr.selectableItemBackground};
            TypedArray typedArray = mContext.obtainStyledAttributes(attrs);
            int backgroundResource = typedArray.getResourceId(0, 0);
            holder.mAttachmentListAdapterBinding.lnrAttachment.setBackgroundResource(backgroundResource);
            Glide.with(mContext)
                    .load(model.getFilePath())
                    .error(android.R.drawable.stat_notify_error)
                    .into(holder.mAttachmentListAdapterBinding.imgJob);

            if (status.equalsIgnoreCase("Completed")) {
                holder.mAttachmentListAdapterBinding.imgDelete.setVisibility(View.GONE);
            } else {
                holder.mAttachmentListAdapterBinding.imgDelete.setVisibility(View.VISIBLE);
            }

            try {
                String date = AppUtils.reFormatDateTime(items.get(position).getCreated_On(), "dd-MMM-yyyy");
                holder.mAttachmentListAdapterBinding.txtCreatedDate.setText(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            holder.mAttachmentListAdapterBinding.txtTitle.setText(model.getFileName());
            holder.mAttachmentListAdapterBinding.imgDelete.setOnClickListener(v -> onItemClickHandler.onDeleteItemClicked(position));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return items.size();

    }

    public void setOnItemClickHandler(OnDeleteListItemClickHandler onItemClickHandler) {
        this.onItemClickHandler = onItemClickHandler;
    }


    public void setData(List<GetAttachmentList> data) {
        items.clear();
        for (int index = 0; index < data.size(); index++) {
            AttachmentListViewModel attachmentViewModel = new AttachmentListViewModel();
            attachmentViewModel.clone(data.get(index));
            items.add(attachmentViewModel);
        }
    }

    public void addData(List<GetAttachmentList> data) {
        items.clear();
        for (int index = 0; index < data.size(); index++) {
            AttachmentListViewModel attachmentViewModel = new AttachmentListViewModel();
            attachmentViewModel.clone(data.get(index));
            items.add(attachmentViewModel);
        }
    }

    public void removeAll() {
        items.removeAll(items);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(AttachmentListViewModel item, int position) {
        items.add(position, item);
        notifyItemInserted(position);
    }

    public AttachmentListViewModel getItem(int position) {
        return items.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final NewAttachmentListAdapterBinding mAttachmentListAdapterBinding;

        public ViewHolder(NewAttachmentListAdapterBinding mAttachmentListAdapterBinding) {
            super(mAttachmentListAdapterBinding.getRoot());
            this.mAttachmentListAdapterBinding = mAttachmentListAdapterBinding;
        }
    }

}
