package com.ab.hicarerun.adapter;

import android.view.View;
import android.widget.TextView;

import com.ab.hicarerun.R;
import com.ab.hicarerun.network.models.ExtendRecentModel.ChildRecent;
import com.ab.hicarerun.network.models.OnSiteModel.OnSiteRecent;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;
import java.util.List;

/**
 * Created by Arjun Bhatt on 12/31/2019.
 */
public class RecentDataViewHolder extends ChildViewHolder {

    private TextView title;

    public RecentDataViewHolder(View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.txtTitle);
    }

    public void onBind(OnSiteRecent task) {
        title.setText(task.getAreaType());
    }
}