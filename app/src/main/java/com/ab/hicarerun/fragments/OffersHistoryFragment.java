package com.ab.hicarerun.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.ab.hicarerun.BaseApplication;
import com.ab.hicarerun.BaseFragment;
import com.ab.hicarerun.R;
import com.ab.hicarerun.adapter.JobCardMSTAdapter;
import com.ab.hicarerun.adapter.RewardSummaryHistoryAdapter;
import com.ab.hicarerun.databinding.FragmentOffersBinding;
import com.ab.hicarerun.databinding.FragmentOffersHistoryBinding;
import com.ab.hicarerun.network.NetworkCallController;
import com.ab.hicarerun.network.NetworkResponseListner;
import com.ab.hicarerun.network.models.AttachmentModel.GetAttachmentList;
import com.ab.hicarerun.network.models.AttachmentModel.MSTAttachment;
import com.ab.hicarerun.network.models.LoginResponse;
import com.ab.hicarerun.network.models.OffersModel.RewardDetailHistorySummary;
import com.ab.hicarerun.network.models.OffersModel.RewardHistoryList;
import com.ab.hicarerun.network.models.OffersModel.RewardsHistoryData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class OffersHistoryFragment extends BaseFragment {
    FragmentOffersHistoryBinding mFragmentOffersBinding;
    List<String> expandableListTitle;
    List<RewardHistoryList> listTitle;
    List<RewardHistoryList> listHistory;
    HashMap<String, List<RewardDetailHistorySummary>> expandableListDetail;

    private static final int REWARDS_HISTORY_REQ = 1000;
    private RewardSummaryHistoryAdapter mAdapter;

    public OffersHistoryFragment() {
        // Required empty public constructor
    }

    public static OffersHistoryFragment newInstance() {
        Bundle args = new Bundle();
        OffersHistoryFragment fragment = new OffersHistoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle oldInstanceState)
    {
        super.onSaveInstanceState(oldInstanceState);
        oldInstanceState.clear();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFragmentOffersBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_offers_history, container, false);
        return mFragmentOffersBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getString(R.string.rewards_history));
        getRewardsHistory();

        mFragmentOffersBinding.expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                return parent.isGroupExpanded(groupPosition);
            }
        });

    }

    private void getRewardsHistory() {
        try {
            RealmResults<LoginResponse> LoginRealmModels =
                    BaseApplication.getRealm().where(LoginResponse.class).findAll();
            if (LoginRealmModels != null && LoginRealmModels.size() > 0) {
                String userId = LoginRealmModels.get(0).getUserID();
                NetworkCallController controller = new NetworkCallController(this);
                controller.setListner(new NetworkResponseListner<RewardsHistoryData>() {
                    @Override
                    public void onResponse(int requestCode, RewardsHistoryData response) {
                        if(response != null){
                            mFragmentOffersBinding.txtTotalPoints.setText(String.valueOf(response.getTotalPointsEarned()) + " Pts.");
                            mFragmentOffersBinding.txtEarnedPoints.setText(String.valueOf(response.getTotalPointsEarned()) + " Pts.");
                            mFragmentOffersBinding.txtMissedPoints.setText(String.valueOf(response.getTotalPointsMissed()) + " Pts.");
                            expandableListDetail = new HashMap<>();
                            listTitle = new ArrayList<>();
                            listHistory = new ArrayList<>();
                            expandableListTitle = new ArrayList<>(expandableListDetail.keySet());
                            listHistory = response.getRewardList();

                            if (listHistory != null && listHistory.size() > 0) {
                                for (int i = 0; i < listHistory.size(); i++) {
                                    expandableListTitle.add(listHistory.get(i).getRewardDateFormat());
                                    expandableListDetail.put(listHistory.get(i).getRewardDateFormat(), listHistory.get(i).getRewardDetailSummary());
                                }
                            }else {
                                mFragmentOffersBinding.emptyTask.setVisibility(View.VISIBLE);
                            }
                            mAdapter = new RewardSummaryHistoryAdapter(getActivity(), expandableListTitle, expandableListDetail, listHistory);
                            mFragmentOffersBinding.expandableListView.setAdapter(mAdapter);

                            for (int i = 0; i < mFragmentOffersBinding.expandableListView.getExpandableListAdapter().getGroupCount(); i++) {
                                mFragmentOffersBinding.expandableListView.expandGroup(i);
                            }
                        }else {
                            mFragmentOffersBinding.emptyTask.setVisibility(View.VISIBLE);
                        }

                    }

                    @Override
                    public void onFailure(int requestCode) {

                    }
                });
                controller.getAllRewardsHistory(REWARDS_HISTORY_REQ, userId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
