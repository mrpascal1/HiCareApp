package com.ab.hicarerun.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.AttachmentListAdapterBinding;
import com.ab.hicarerun.databinding.TaskListAdapterBinding;
import com.ab.hicarerun.handler.OnListItemClickHandler;
import com.ab.hicarerun.network.models.AttachmentModel.GetAttachmentList;
import com.ab.hicarerun.network.models.ReferralModel.ReferralList;
import com.ab.hicarerun.network.models.TaskModel.Tasks;
import com.ab.hicarerun.utils.AppUtils;
import com.ab.hicarerun.viewmodel.AttachmentListViewModel;
import com.ab.hicarerun.viewmodel.TaskViewModel;
import com.bumptech.glide.Glide;


import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class AttachmentListAdapter extends RecyclerView.Adapter<AttachmentListAdapter.ViewHolder> {

    private OnListItemClickHandler onItemClickHandler;
    private final Context mContext;
    private static List<AttachmentListViewModel> items = null;
    private AppCompatImageView imgSelect;
    private AppCompatTextView txtDelcount;


    public AttachmentListAdapter(Context context, AppCompatImageView imgSelect, AppCompatTextView txtDelcount) {
        if (items == null) {
            items = new ArrayList<>();
        }
        this.mContext = context;
        this.imgSelect = imgSelect;
        this.txtDelcount = txtDelcount;
    }

    public AttachmentListAdapter(Context context) {
        if (items == null) {
            items = new ArrayList<>();
        }
        this.mContext = context;
    }


    @NotNull
    @Override
    public AttachmentListAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        AttachmentListAdapterBinding mAttachmentListAdapterBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.attachment_list_adapter, parent, false);
        return new AttachmentListAdapter.ViewHolder(mAttachmentListAdapterBinding);
    }

    @Override
    public void onBindViewHolder(@NotNull final AttachmentListAdapter.ViewHolder holder, final int position) {
        final AttachmentListViewModel model = items.get(position);

        Glide.with(mContext)
                .load(model.getFilePath())
                .error(android.R.drawable.stat_notify_error)
                .into(holder.mAttachmentListAdapterBinding.imgJob);

        try {
            String date = AppUtils.reFormatDateTime(items.get(position).getCreated_On(), "dd-MMM-yyyy");
            holder.mAttachmentListAdapterBinding.txtDate.setText(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        holder.mAttachmentListAdapterBinding.txtName.setText(model.getFileName());


    }

    @Override
    public int getItemCount() {
        return items.size();

    }

    public void setOnItemClickHandler(OnListItemClickHandler onItemClickHandler) {
        this.onItemClickHandler = onItemClickHandler;
    }


    public void setData(List<GetAttachmentList> data) {
        items.clear();
        for (int index = 0; index < data.size(); index++) {
            AttachmentListViewModel attachmentViewModel = new AttachmentListViewModel();
            for (int i = 0; i < data.size(); i++) {
                attachmentViewModel.setVisible(false);
            }
            attachmentViewModel.clone(data.get(index));
            items.add(attachmentViewModel);
        }
    }

    public void addData(List<GetAttachmentList> data) {
        items.clear();
        for (int index = 0; index < data.size(); index++) {
            AttachmentListViewModel attachmentViewModel = new AttachmentListViewModel();
            for (int i = 0; i < data.size(); i++) {
                attachmentViewModel.setVisible(false);
            }
            attachmentViewModel.clone(data.get(index));
            items.add(attachmentViewModel);
        }
    }

    public void removeAll() {
        items.removeAll(items);
        notifyDataSetChanged();
    }

    public AttachmentListViewModel getItem(int position) {
        return items.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final AttachmentListAdapterBinding mAttachmentListAdapterBinding;

        public ViewHolder(AttachmentListAdapterBinding mAttachmentListAdapterBinding) {
            super(mAttachmentListAdapterBinding.getRoot());
            this.mAttachmentListAdapterBinding = mAttachmentListAdapterBinding;
        }
    }

}
