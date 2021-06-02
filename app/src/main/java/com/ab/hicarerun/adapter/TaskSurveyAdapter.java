package com.ab.hicarerun.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ab.hicarerun.R;
import com.ab.hicarerun.network.models.GeneralModel.TaskCheckList;
import com.ab.hicarerun.network.models.OffersModel.RewardDetailHistorySummary;
import com.ab.hicarerun.network.models.OffersModel.RewardHistoryList;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by Arjun Bhatt on 7/15/2020.
 */
public class TaskSurveyAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> expandableListTitle;
    private List<String> listOptions;
    private HashMap<String, List<String>> expandableListDetail;
    private List<TaskCheckList> listTitle;

    public TaskSurveyAdapter(Context context, List<String> expandableListTitle, HashMap<String, List<String>> expandableListDetail, List<TaskCheckList> listTitle, List<String> listOptions) {
        this.context = context;
        this.expandableListDetail = expandableListDetail;
        this.expandableListTitle = expandableListTitle;
        this.listTitle = listTitle;
        this.listOptions = listOptions;
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
        final String child = (String) getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert layoutInflater != null;
            convertView = layoutInflater.inflate(R.layout.layout_survey_answers_adapter, null);
            TextView txtOptions = convertView.findViewById(R.id.txtOptions);
            RelativeLayout relPhoto = convertView.findViewById(R.id.relPhoto);
            txtOptions.setText(child);
            if (expandedListPosition == expandableListDetail.get(this.expandableListTitle.get(listPosition)).size()-1) {
                relPhoto.setVisibility(View.VISIBLE);
            } else {
                relPhoto.setVisibility(View.GONE);
            }
        }
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
        String title = String.valueOf(listTitle.get(listPosition).getTitle());
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert layoutInflater != null;
            convertView = layoutInflater.inflate(R.layout.layout_survey_questions_adapter, null);
        }
        TextView listTitleTextView = convertView
                .findViewById(R.id.txtQuestion);
        listTitleTextView.setTypeface(listTitleTextView.getTypeface(), Typeface.BOLD);
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