package com.ab.hicarerun.network.models.ExtendRecentModel;

import android.annotation.SuppressLint;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

/**
 * Created by Arjun Bhatt on 12/31/2019.
 */
@SuppressLint("ParcelCreator")
public class ParentRecent extends ExpandableGroup<ChildRecent> {

    public ParentRecent(String title, List<ChildRecent> items) {
        super(title, items);
    }
}
