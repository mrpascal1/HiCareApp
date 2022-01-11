package com.ab.hicarerun.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ab.hicarerun.BaseApplication;
import com.ab.hicarerun.BaseFragment;
import com.ab.hicarerun.R;
import com.ab.hicarerun.adapter.RewardLeadersAdapter;
import com.ab.hicarerun.databinding.FragmentLeaderBoardBinding;
import com.ab.hicarerun.network.NetworkCallController;
import com.ab.hicarerun.network.NetworkResponseListner;
import com.ab.hicarerun.network.models.leaderboardmodel.RewardLeaders;
import com.ab.hicarerun.network.models.LoginResponse;
import com.ab.hicarerun.utils.AppUtils;

import java.util.List;

import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class LeaderBoardFragment extends BaseFragment {
    FragmentLeaderBoardBinding mFragmentLeaderBoardBinding;
    private static final int REQ_LEADERS = 1000;
    private static final int RESOURCE_REQ = 1000;

    RewardLeadersAdapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    List<RewardLeaders> rewardLeaders;

    public LeaderBoardFragment() {
        // Required empty public constructor
    }

    public static LeaderBoardFragment newInstance() {
        Bundle args = new Bundle();
        LeaderBoardFragment fragment = new LeaderBoardFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFragmentLeaderBoardBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_leader_board, container, false);
        getActivity().setTitle(getResources().getString(R.string.leader_board));

//        if (getActivity() != null) {
//            LinearLayout toolbar = getActivity().findViewById(R.id.toolbar);
//            toolbar.setVisibility(View.GONE);
//            LinearLayout custom_toolbar = getActivity().findViewById(R.id.customToolbar);
//            custom_toolbar.setVisibility(View.VISIBLE);
//            TextView tool = getActivity().findViewById(R.id.txtTool);
//            tool.setText(getResources().getString(R.string.incentives_in));
//        }

        return mFragmentLeaderBoardBinding.getRoot();
    }

    @Override
    public void onSaveInstanceState(Bundle oldInstanceState) {
        super.onSaveInstanceState(oldInstanceState);
        oldInstanceState.clear();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFragmentLeaderBoardBinding.recycleView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        mFragmentLeaderBoardBinding.recycleView.setLayoutManager(layoutManager);
        mAdapter = new RewardLeadersAdapter(getContext());
        mFragmentLeaderBoardBinding.recycleView.setAdapter(mAdapter);
        getRewardLeaders();
    }

    private void getRewardLeaders() {
        try {
            if (getActivity() != null) {
                RealmResults<LoginResponse> LoginRealmModels =
                        BaseApplication.getRealm().where(LoginResponse.class).findAll();
                assert LoginRealmModels.get(0) != null;
                String userId = LoginRealmModels.get(0).getUserID();
                NetworkCallController controller = new NetworkCallController(this);
                controller.setListner(new NetworkResponseListner<List<RewardLeaders>>() {

                    @Override
                    public void onResponse(int requestCode, List<RewardLeaders> items) {
                        if (items != null && items.size() > 0) {
                            rewardLeaders = items;
                            try {
                                String FirstName = items.get(0).getResourceName();
                                String FirstCentre = items.get(0).getServiceCenterName();
                                int FirstPoints = items.get(0).getTotalPoints();
                                int firstMissed = items.get(0).getTotalMissedPoints();
                                String FirstBadge = items.get(0).getBadgeName();
                                int FirstRank = items.get(0).getRank();

                                AppUtils.getResourceImage(items.get(0).getResourceId(), mFragmentLeaderBoardBinding.imgFirst);
                                AppUtils.getResourceImage(items.get(1).getResourceId(), mFragmentLeaderBoardBinding.imgSecond);
                                AppUtils.getResourceImage(items.get(2).getResourceId(), mFragmentLeaderBoardBinding.imgThird);


                                mFragmentLeaderBoardBinding.txtFirstName.setText(FirstName);
                                mFragmentLeaderBoardBinding.txtFirstCentre.setText(FirstCentre);
                                mFragmentLeaderBoardBinding.txtFirstPoints.setText(String.valueOf(FirstPoints) + " Pts.");
                                mFragmentLeaderBoardBinding.txtFirstMissed.setText("(Lost " + String.valueOf(firstMissed) + " Pts.)");
                                mFragmentLeaderBoardBinding.txtFirstRank.setText(String.valueOf(FirstRank));
                                if (items.get(0).getBadgeName() != null && !items.get(0).getBadgeName().equals("")) {
                                    mFragmentLeaderBoardBinding.imgFirstBadge.setImageResource(AppUtils.getBadgeImage(items.get(0).getBadgeName()));
                                }else {
                                    mFragmentLeaderBoardBinding.imgFirstBadge.setImageResource(R.drawable.ic_profile);
                                }

                                String secondName = items.get(1).getResourceName();
                                String secondCentre = items.get(1).getServiceCenterName();
                                int secondPoint = items.get(1).getTotalPoints();
                                int secondMissed = items.get(1).getTotalMissedPoints();
                                int secondRank = items.get(1).getRank();

                                mFragmentLeaderBoardBinding.txtSecondName.setText(secondName);
                                mFragmentLeaderBoardBinding.txtSecondCentre.setText(secondCentre);
                                mFragmentLeaderBoardBinding.txtSecondPoints.setText(String.valueOf(secondPoint) + " Pts.");
                                mFragmentLeaderBoardBinding.txtSecondMissed.setText("(Lost " + String.valueOf(secondMissed) + " Pts.)");
                                mFragmentLeaderBoardBinding.txtSecondRank.setText(String.valueOf(secondRank));
                                if (items.get(1).getBadgeName() != null && !items.get(1).getBadgeName().equals("")) {
                                    mFragmentLeaderBoardBinding.imgCrown.setVisibility(View.VISIBLE);
                                    mFragmentLeaderBoardBinding.imgSecondBadge.setImageResource(AppUtils.getBadgeImage(items.get(1).getBadgeName()));
                                }else {
                                    mFragmentLeaderBoardBinding.imgCrown.setVisibility(View.VISIBLE);
                                    mFragmentLeaderBoardBinding.imgFirstBadge.setImageResource(R.drawable.ic_profile);
                                }

                                String thirdName = items.get(2).getResourceName();
                                String thirdCentre = items.get(2).getServiceCenterName();
                                int thirdPoint = items.get(2).getTotalPoints();
                                int thirdMissed = items.get(2).getTotalMissedPoints();
                                int thirdRank = items.get(2).getRank();

                                mFragmentLeaderBoardBinding.txtThirdName.setText(thirdName);
                                mFragmentLeaderBoardBinding.txtThirdCentre.setText(thirdCentre);
                                mFragmentLeaderBoardBinding.txtThirdPoints.setText(String.valueOf(thirdPoint) + " Pts.");
                                mFragmentLeaderBoardBinding.txtThirdMissed.setText("(Lost " + String.valueOf(thirdMissed) + " Pts.)");
                                mFragmentLeaderBoardBinding.txtThirdRank.setText(String.valueOf(thirdRank));
                                if (items.get(2).getBadgeName() != null && !items.get(2).getBadgeName().equals("")) {
                                    mFragmentLeaderBoardBinding.imgThirdBadge.setImageResource(AppUtils.getBadgeImage(items.get(2).getBadgeName()));
                                }else {
                                    mFragmentLeaderBoardBinding.imgFirstBadge.setImageResource(R.drawable.ic_profile);
                                }

                                items.remove(0);
                                items.remove(0);
                                items.remove(0);
                                mAdapter.notifyDataSetChanged();
                                mAdapter.addData(items);
                                mAdapter.notifyDataSetChanged();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }

                    @Override
                    public void onFailure(int requestCode) {

                    }
                });
                controller.getRewardLeaders(REQ_LEADERS, userId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
