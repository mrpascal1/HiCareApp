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

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

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
    public void onSaveInstanceState(Bundle oldInstanceState)
    {
        super.onSaveInstanceState(oldInstanceState);
        oldInstanceState.clear();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            model = getArguments().getParcelable(ARG_ACCOUNT);
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFragmentOnSiteAccountDetailsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_on_site_account_details, container, false);
        getActivity().setTitle(getResources().getString(R.string.on_site_activity_details));
        setViewPagerView();
        return mFragmentOnSiteAccountDetailsBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void setViewPagerView() {
        try {
            mAdapter = new TaskViewPagerAdapter(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), getActivity());
            mAdapter.addFragment(OnSiteTaskFragment.newInstance(model), getResources().getString(R.string.on_site_tasks_tab));
            mAdapter.addFragment(RecentOnsiteTaskFragment.newInstance(model), getResources().getString(R.string.recent_tasks_tab));
            mFragmentOnSiteAccountDetailsBinding.viewpagertab.setDistributeEvenly(true);
            mFragmentOnSiteAccountDetailsBinding.pager.setAdapter(mAdapter);
            mFragmentOnSiteAccountDetailsBinding.viewpagertab.setViewPager(mFragmentOnSiteAccountDetailsBinding.pager);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
