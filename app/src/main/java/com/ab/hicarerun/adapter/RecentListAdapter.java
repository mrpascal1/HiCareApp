//package com.ab.hicarerun.adapter;
//
//import android.app.Activity;
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.fragment.app.FragmentActivity;
//
//import com.ab.hicarerun.R;
//import com.ab.hicarerun.network.models.ExtendRecentModel.ChildRecent;
//import com.ab.hicarerun.network.models.ExtendRecentModel.ParentRecent;
//import com.ab.hicarerun.network.models.OnSiteModel.OnSiteHead;
//import com.ab.hicarerun.network.models.OnSiteModel.OnSiteRecent;
//import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
//import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
//
//import java.util.List;
//import java.util.zip.Inflater;
//
///**
// * Created by Arjun Bhatt on 12/31/2019.
// */
//public class RecentListAdapter extends ExpandableRecyclerViewAdapter<RecentHeaderViewHolder, RecentDataViewHolder> {
//    Context mContext;
//
//    public RecentListAdapter(List<? extends ExpandableGroup> groups, FragmentActivity activity) {
//        super(groups);
//        mContext = activity;
//    }
//
//    @Override
//    public RecentHeaderViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
//        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
//        View view = inflater.inflate(R.layout.layout_header_adapter, parent, false);
//
//        return new RecentHeaderViewHolder(view);
//    }
//
//    @Override
//    public RecentDataViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
//        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
//        View view = inflater.inflate(R.layout.onsite_recent_adapter, parent, false);
//        return new RecentDataViewHolder(view);
//    }
//
//    @Override
//    public void onBindChildViewHolder(RecentDataViewHolder holder, int flatPosition, ExpandableGroup group,
//                                      int childIndex) {
//        final OnSiteRecent activity = (OnSiteRecent) (group).getItems().get(childIndex);
//        holder.onBind(activity);
//    }
//
//    @Override
//    public void onBindGroupViewHolder(RecentHeaderViewHolder holder, int flatPosition,
//                                      ExpandableGroup group) {
//        final OnSiteHead activity = (OnSiteHead) (group);
//        holder.setHeader(activity);
//    }
//}