package com.ab.hicarerun.handler;

import com.ab.hicarerun.network.models.chemicalmodel.ActivityData;

import java.util.List;

/**
 * Created by Arjun Bhatt on 6/27/2021.
 */
public interface OnAddChemicalActivity {
    void onAddActivityClicked(int parentPosition, int childPosition, List<ActivityData> activity);
}
