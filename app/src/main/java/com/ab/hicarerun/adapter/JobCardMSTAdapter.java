package com.ab.hicarerun.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ab.hicarerun.R;
import com.ab.hicarerun.handler.OnAddJobCardClickHandler;
import com.ab.hicarerun.handler.OnDeleteListItemClickHandler;
import com.ab.hicarerun.handler.OnJobCardDeleteClickHandler;
import com.ab.hicarerun.handler.OnRecentTaskClickHandler;
import com.ab.hicarerun.network.models.AttachmentModel.GetAttachmentList;
import com.ab.hicarerun.network.models.AttachmentModel.MSTAttachment;
import com.ab.hicarerun.network.models.OnSiteModel.OnSiteRecent;
import com.ab.hicarerun.utils.AppUtils;
import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Arjun Bhatt on 1/6/2020.
 */
public class JobCardMSTAdapter extends BaseExpandableListAdapter {
    private OnAddJobCardClickHandler onItemClickHandler;
    private OnJobCardDeleteClickHandler onDeleteItemClickHandler;

    private Context context;
    private List<String> expandableListTitle;
    private HashMap<String, List<GetAttachmentList>> expandableListDetail;
    private ExpandableListView mExpandableListView;
    private String status;
    private List<MSTAttachment> listMSTTitle;

    public JobCardMSTAdapter(Context context, List<String> expandableListTitle,
                             HashMap<String, List<GetAttachmentList>> expandableListDetail, ExpandableListView expandableListView, String schedulingStatus, List<MSTAttachment> listTitle) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
        this.mExpandableListView = expandableListView;
        this.status = schedulingStatus;
        this.listMSTTitle = listTitle;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition)).get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final GetAttachmentList child = (GetAttachmentList) getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.new_attachment_list_adapter, null);
        }
        TextView txtTitle = convertView
                .findViewById(R.id.txtTitle);
        TextView txtCreatedDate = convertView.findViewById(R.id.txtCreatedDate);
        ImageView imgJob = convertView.findViewById(R.id.imgJob);
        ImageView imgDelete = convertView.findViewById(R.id.imgDelete);
        if (status.equalsIgnoreCase("Completed")) {
            imgDelete.setVisibility(View.GONE);
        } else {
            imgDelete.setVisibility(View.VISIBLE);
        }
        LinearLayout lnrAttachment = convertView.findViewById(R.id.lnrAttachment);
        int[] attrs = new int[]{R.attr.selectableItemBackground};
        TypedArray typedArray = context.obtainStyledAttributes(attrs);
        int backgroundResource = typedArray.getResourceId(0, 0);
        lnrAttachment.setBackgroundResource(backgroundResource);
        imgDelete.setOnClickListener(v -> onDeleteItemClickHandler.onDeleteJobCard(listPosition, expandedListPosition));
        txtTitle.setText(child.getFileName());
        Glide.with(context)
                .load(child.getFilePath())
                .error(android.R.drawable.stat_notify_error)
                .into(imgJob);

        try {
            String date = AppUtils.reFormatDateTime(child.getCreated_On(), "dd-MMM-yyyy");
            txtCreatedDate.setText(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return convertView;
    }

    public void setOnItemClickHandler(OnAddJobCardClickHandler onItemClickHandler) {
        this.onItemClickHandler = onItemClickHandler;
    }

    public void setOnDeleteItemClickHandler(OnJobCardDeleteClickHandler onDeleteItemClickHandler) {
        this.onDeleteItemClickHandler = onDeleteItemClickHandler;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition)).size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListTitle.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);
        String title = listMSTTitle.get(listPosition).getTaskType();
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.layout_jobcard_header, null);
        }
        TextView listTitleTextView = convertView
                .findViewById(R.id.txtHeader);
        LinearLayout lnrAdd = convertView.findViewById(R.id.addCard);
        if (status.equalsIgnoreCase("Completed")) {
            lnrAdd.setVisibility(View.GONE);
        } else {
            lnrAdd.setVisibility(View.VISIBLE);
        }
        listTitleTextView.setText(title);
        lnrAdd.setOnClickListener(view -> onItemClickHandler.onAddJobCardClicked(listPosition));
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }
}
