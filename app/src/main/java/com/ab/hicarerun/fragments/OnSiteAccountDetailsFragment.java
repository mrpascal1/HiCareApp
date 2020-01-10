package com.ab.hicarerun.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ab.hicarerun.BaseFragment;
import com.ab.hicarerun.R;
import com.ab.hicarerun.adapter.TaskViewPagerAdapter;
import com.ab.hicarerun.databinding.FragmentOnSiteAccountDetailsBinding;
import com.ab.hicarerun.network.models.OnSiteModel.Account;
import com.ab.hicarerun.network.models.OnSiteModel.OnSiteAccounts;

/**
 * A simple {@link Fragment} subclass.
 */
public class OnSiteAccountDetailsFragment extends BaseFragment {
    FragmentOnSiteAccountDetailsBinding mFragmentOnSiteAccountDetailsBinding;
    private TaskViewPagerAdapter mAdapter;
    Account model;
    private static final String ARG_ACCOUNT = "ARG_ACCOUNT";

    public OnSiteAccountDetailsFragment() {
        // Required empty public constructor
    }

    public static OnSiteAccountDetailsFragment newInstance(OnSiteAccounts onSiteAccounts) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_ACCOUNT, onSiteAccounts);
        OnSiteAccountDetailsFragment fragment = new OnSiteAccountDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            model = getArguments().getParcelable(ARG_ACCOUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFragmentOnSiteAccountDetailsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_on_site_account_details, container, false);
        getActivity().setTitle("On-Site Accounts Details");
        setViewPagerView();
        return mFragmentOnSiteAccountDetailsBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void setViewPagerView() {
        mAdapter = new TaskViewPagerAdapter(getActivity().getSupportFragmentManager());
        mAdapter.addFragment(OnSiteTaskFragment.newInstance(model), "On-Site Tasks");
        mAdapter.addFragment(RecentOnsiteTaskFragment.newInstance(model), "Recent Tasks");
        mFragmentOnSiteAccountDetailsBinding.viewpagertab.setDistributeEvenly(true);
        mFragmentOnSiteAccountDetailsBinding.pager.setAdapter(mAdapter);
        mFragmentOnSiteAccountDetailsBinding.viewpagertab.setViewPager(mFragmentOnSiteAccountDetailsBinding.pager);
    }

}
