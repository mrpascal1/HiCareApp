package com.ab.hicarerun.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.ab.hicarerun.R;
import com.ab.hicarerun.network.models.ExtendRecentModel.ParentRecent;
import com.ab.hicarerun.network.models.OnSiteModel.OnSiteHead;
import com.ab.hicarerun.network.models.OnSiteModel.OnSiteRecent;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

/**
 * Created by Arjun Bhatt on 12/31/2019.
 */
public class RecentHeaderViewHolder extends GroupViewHolder {

    private TextView genreTitle;
    private CardView cardArea;

    public RecentHeaderViewHolder(View itemView) {
        super(itemView);
        genreTitle = itemView.findViewById(R.id.txtHeader);
        cardArea = itemView.findViewById(R.id.cardArea);
    }

    public void setHeader(OnSiteHead group) {
        genreTitle.setText(group.getHead());
    }

//    @Override
//    public void expand() {
//        genreTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_downn, 0);
//    }
//
//    @Override
//    public void collapse() {
//        genreTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_up, 0);
//    }

}