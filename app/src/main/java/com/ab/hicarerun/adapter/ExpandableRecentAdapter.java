package com.ab.hicarerun.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.ab.hicarerun.R;
import com.ab.hicarerun.handler.OnRecentTaskClickHandler;
import com.ab.hicarerun.network.models.OnSiteModel.OnSiteRecent;
import com.ab.hicarerun.utils.AppUtils;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by Arjun Bhatt on 12/31/2019.
 */
public class ExpandableRecentAdapter extends BaseExpandableListAdapter {
    private OnRecentTaskClickHandler onItemClickHandler;
    private Context context;
    private List<String> expandableListTitle;
    private HashMap<String, List<OnSiteRecent>> expandableListDetail;
    private ExpandableListView mExpandableListView;

    public ExpandableRecentAdapter(Context context, List<String> expandableListTitle,
                                   HashMap<String, List<OnSiteRecent>> expandableListDetail, ExpandableListView expandableListView) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
        this.mExpandableListView = expandableListView;
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
        final OnSiteRecent child = (OnSiteRecent) getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert layoutInflater != null;
            convertView = layoutInflater.inflate(R.layout.onsite_recent_adapter, null);
        }
        TextView txtTitle = convertView
                .findViewById(R.id.txtTitle);
        TextView txtArea = convertView.findViewById(R.id.txtArea);
        TextView txtService = convertView.findViewById(R.id.txtServiceType);
        TextView txtTime = convertView.findViewById(R.id.txtTime);
        TextView txtReason = convertView.findViewById(R.id.txtReason);
        LinearLayout lnrView = convertView.findViewById(R.id.lnrView);
        LinearLayout lnrViewInner = convertView.findViewById(R.id.innerAdd);
        LinearLayout lnrViewOuter = convertView.findViewById(R.id.outerAdd);
        LinearLayout lnrDeleteInner = convertView.findViewById(R.id.innerNotDone);
        LinearLayout lnrDeleteOuter = convertView.findViewById(R.id.outerNotdone);
        LinearLayout lnrReason = convertView.findViewById(R.id.lnrReason);
        LinearLayout lnrDelete = convertView.findViewById(R.id.lnrDelete);
        View bgView = convertView.findViewById(R.id.bgView);

        txtTitle.setText(child.getAreaSubType());
        txtArea.setText(child.getAreaType());
        ((GradientDrawable) lnrViewInner.getBackground()).setColor(context.getResources().getColor(R.color.outerBlue));
        ((GradientDrawable) lnrViewOuter.getBackground()).setColor(context.getResources().getColor(R.color.innerBlue));
        ((GradientDrawable) lnrDeleteInner.getBackground()).setColor(context.getResources().getColor(R.color.taskPink));
        ((GradientDrawable) lnrDeleteOuter.getBackground()).setColor(context.getResources().getColor(R.color.red));
        if (child.getActivityDetail() != null && child.getActivityDetail().size() > 0) {
            try {
                String dt = child.getActivityDetail().get(0).getStartTime();
                String time = AppUtils.reFormatDateAndTime(dt, "hh:mm aa");
                txtTime.setText(child.getActivityDetail().get(0).getStartTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        String service = child.getServiceType().replace(",", ", ");
        txtService.setText(service);

        if (child.getServiceType().equalsIgnoreCase("Not Done")) {
            lnrReason.setVisibility(View.VISIBLE);
            bgView.setBackgroundColor(context.getResources().getColor(R.color.red));
            txtReason.setText(child.getActivityDetail().get(0).getServiceNotDoneReason());

        } else {
            lnrReason.setVisibility(View.GONE);
            bgView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        }
        lnrView.setOnClickListener(view -> onItemClickHandler.onViewItemClicked(listPosition, expandedListPosition));

        lnrDelete.setOnClickListener(view -> onItemClickHandler.onDeleteItemClicked(listPosition, expandedListPosition));
        return convertView;
    }

    public void setOnItemClickHandler(OnRecentTaskClickHandler onItemClickHandler) {
        this.onItemClickHandler = onItemClickHandler;
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
        String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert layoutInflater != null;
            convertView = layoutInflater.inflate(R.layout.layout_header_adapter, null);
        }
        TextView listTitleTextView = convertView
                .findViewById(R.id.txtHeader);
        ImageView imgArrow = convertView.findViewById(R.id.imgArrow);

        LinearLayout cardArea = convertView.findViewById(R.id.cardArea);
        listTitleTextView.setText(listTitle);


        cardArea.setOnClickListener(view -> {
            if (imgArrow.getTag() != null && imgArrow.getTag().toString().equals("180")) {
                ObjectAnimator anim = ObjectAnimator.ofFloat(imgArrow, "rotation", 180, 0);
                anim.setDuration(300);
                anim.start();
                imgArrow.setTag("");
                mExpandableListView.expandGroup(listPosition);


            } else {
                ObjectAnimator anim = ObjectAnimator.ofFloat(imgArrow, "rotation", 0, 180);
                anim.setDuration(300);
                anim.start();
                imgArrow.setTag(180 + "");
                mExpandableListView.collapseGroup(listPosition);
            }
        });

//        if (isExpanded) {
//            cardArea.setOnClickListener(view -> {
//                if (imgArrow.getTag() != null && imgArrow.getTag().toString().equals("180") && isExpanded) {
//                    ObjectAnimator anim = ObjectAnimator.ofFloat(imgArrow, "rotation", 180, 0);
//                    anim.setDuration(500);
//                    anim.start();
//                    imgArrow.setTag("");
//
//
//                } else {
//                    ObjectAnimator anim = ObjectAnimator.ofFloat(imgArrow, "rotation", 0, 180);
//                    anim.setDuration(500);
//                    anim.start();
//                    imgArrow.setTag(180 + "");
//                }
//            });
//        }
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
