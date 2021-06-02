package com.ab.hicarerun.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ab.hicarerun.BaseApplication;
import com.ab.hicarerun.BaseFragment;
import com.ab.hicarerun.R;
import com.ab.hicarerun.adapter.RecycleOfferHistoryAdapter;
import com.ab.hicarerun.adapter.RecycleOffersAdapter;
import com.ab.hicarerun.databinding.FragmentRewardsHistoryBinding;
import com.ab.hicarerun.network.NetworkCallController;
import com.ab.hicarerun.network.NetworkResponseListner;
import com.ab.hicarerun.network.models.LoginResponse;
import com.ab.hicarerun.network.models.RewardsModel.RewardsData;

import java.util.ArrayList;

import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class RewardsHistoryFragment extends BaseFragment {
    FragmentRewardsHistoryBinding mFragmentRewardsHistoryBinding;
    RecyclerView.LayoutManager layoutManager;
    private RecycleOfferHistoryAdapter mAdapter;
    private static final int REDEEM_REQ = 1000;
    private Integer pageNumber = 1;

    public RewardsHistoryFragment() {
        // Required empty public constructor
    }

    public static RewardsHistoryFragment newInstance() {
        Bundle args = new Bundle();
        RewardsHistoryFragment fragment = new RewardsHistoryFragment();
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
        mFragmentRewardsHistoryBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_rewards_history, container, false);
        return mFragmentRewardsHistoryBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getString(R.string.bazaar_history));
        mFragmentRewardsHistoryBinding.recycleView.setHasFixedSize(true);
//        layoutManager = new GridLayoutManager(getActivity(), 2);
        mFragmentRewardsHistoryBinding.recycleView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mAdapter = new RecycleOfferHistoryAdapter(getActivity());
        mFragmentRewardsHistoryBinding.recycleView.setAdapter(mAdapter);
        getOffersHistory();
    }

    private void getOffersHistory() {
        try {
            RealmResults<LoginResponse> LoginRealmModels =
                    BaseApplication.getRealm().where(LoginResponse.class).findAll();
            if (LoginRealmModels != null && LoginRealmModels.size() > 0) {
                String userId = LoginRealmModels.get(0).getUserID();
                NetworkCallController controller = new NetworkCallController(this);
                controller.setListner(new NetworkResponseListner<RewardsData>() {
                    @Override
                    public void onResponse(int requestCode, RewardsData response) {
                        if (response.getRedeemedData() != null) {
                            if (pageNumber == 1 && response.getRedeemedData().size() > 0) {
                                mAdapter.setData(response.getRedeemedData());
                                mAdapter.notifyDataSetChanged();
                            } else if (response.getAvailableOffers().size() > 0) {
                                mAdapter.addData(response.getRedeemedData());
                                mAdapter.notifyDataSetChanged();
                            } else {
                                mFragmentRewardsHistoryBinding.emptyTask.setVisibility(View.VISIBLE);
                                pageNumber--;
                            }
                        }else {
                            mFragmentRewardsHistoryBinding.emptyTask.setVisibility(View.VISIBLE);
                        }


                    }

                    @Override
                    public void onFailure(int requestCode) {

                    }
                });
                controller.getResourceRedeemedData(REDEEM_REQ, userId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
