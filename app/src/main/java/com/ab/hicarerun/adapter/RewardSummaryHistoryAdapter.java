package com.ab.hicarerun.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ab.hicarerun.R;
import com.ab.hicarerun.network.models.offersmodel.RewardDetailHistorySummary;
import com.ab.hicarerun.network.models.offersmodel.RewardHistoryList;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by Arjun Bhatt on 3/27/2020.
 */
public class RewardSummaryHistoryAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> expandableListTitle;
    private HashMap<String, List<RewardDetailHistorySummary>> expandableListDetail;
    private List<RewardHistoryList> listTitle;

    public RewardSummaryHistoryAdapter(Context context, List<String> expandableListTitle, HashMap<String, List<RewardDetailHistorySummary>> expandableListDetail, List<RewardHistoryList> listTitle) {
        this.context = context;
        this.expandableListDetail = expandableListDetail;
        this.expandableListTitle = expandableListTitle;
        this.listTitle = listTitle;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return Objects.requireNonNull(this.expandableListDetail.get(this.expandableListTitle.get(listPosition))).get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final RewardDetailHistorySummary child = (RewardDetailHistorySummary) getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert layoutInflater != null;
            convertView = layoutInflater.inflate(R.layout.offers_history_footer_adapter, null);
        }

        TextView txtTitle = convertView.findViewById(R.id.txtTitle);
        TextView txtDescription = convertView.findViewById(R.id.txtDescription);
        TextView txtPoints = convertView.findViewById(R.id.txtPoints);
        TextView txtPtsLabel = convertView.findViewById(R.id.txtPtsLabel);
        TextView txtProb = convertView.findViewById(R.id.txtProb);
        ImageView imgBadge = convertView.findViewById(R.id.imgBadge);

        if (child.getScoreName().equals("Receiving Good Feedback from the Customer")) {
            if (child.getMissedEvent()) {
                imgBadge.setImageResource(R.drawable.ic_feedback_badge);
            } else {
                imgBadge.setImageResource(R.drawable.ic_feedback_green);
            }
        } else if (child.getScoreName().equals("Reporting On-Time (before 8:30 am) at the Service Center")) {
            if (child.getMissedEvent()) {
                imgBadge.setImageResource(R.drawable.ic_clock_badge);
            } else {
                imgBadge.setImageResource(R.drawable.ic_clock_green);
            }
        } else if (child.getScoreName().equals("Delivering the Jobs On-Time on the basis of Assignment Time")) {
            if (child.getMissedEvent()) {
                imgBadge.setImageResource(R.drawable.ic_clock_badge);
            } else {
                imgBadge.setImageResource(R.drawable.ic_clock_green);
            }
        }else {
            if (child.getMissedEvent()) {
                imgBadge.setImageResource(R.drawable.ic_rewards_badge);
            } else {
                imgBadge.setImageResource(R.drawable.ic_rewards_green);
            }
        }

        if (child.getMissedEvent()) {
            txtProb.setText("-");
            txtProb.setTextColor(context.getResources().getColor(R.color.lightRed));
            txtPoints.setTextColor(context.getResources().getColor(R.color.lightRed));
            txtPtsLabel.setTextColor(context.getResources().getColor(R.color.lightRed));
        } else {
            txtProb.setText("+");
            txtProb.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            txtPtsLabel.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            txtPoints.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }

        txtTitle.setText(child.getEventName());
        txtDescription.setText(child.getScoreDescription());
        txtPoints.setText(" " + String.valueOf(child.getPointsEarned()) + " ");

        return convertView;
    }


    @Override
    public int getChildrenCount(int listPosition) {
        return Objects.requireNonNull(this.expandableListDetail.get(this.expandableListTitle.get(listPosition))).size();
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
//        String lTitle = (String) getGroup(listPosition);
        String title = String.valueOf(listTitle.get(listPosition).getTotalPointsEarned()) + " Pts." + " | " + listTitle.get(listPosition).getRewardDateFormat();
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert layoutInflater != null;
            convertView = layoutInflater.inflate(R.layout.offers_history_header_adapter, null);
        }
        TextView listTitleTextView = convertView
                .findViewById(R.id.txtHeader);

        listTitleTextView.setText(title);
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