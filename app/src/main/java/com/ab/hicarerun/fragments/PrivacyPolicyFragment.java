package com.ab.hicarerun.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ab.hicarerun.BaseFragment;
import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.FragmentPrivacyPolicyBinding;

import org.jetbrains.annotations.NotNull;

/**
 * A simple {@link Fragment} subclass.
 */
public class PrivacyPolicyFragment extends BaseFragment {
    FragmentPrivacyPolicyBinding mFragmentPrivacyPolicyBinding;

    public PrivacyPolicyFragment() {
        // Required empty public constructor
    }

    public static PrivacyPolicyFragment newInstance() {
        Bundle args = new Bundle();
        PrivacyPolicyFragment fragment = new PrivacyPolicyFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFragmentPrivacyPolicyBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_privacy_policy, container, false);
        return mFragmentPrivacyPolicyBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFragmentPrivacyPolicyBinding.webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
        });
        try {
            mFragmentPrivacyPolicyBinding.webView.getSettings().setJavaScriptEnabled(true);
            mFragmentPrivacyPolicyBinding.webView.loadUrl("http://connect.hicare.in/privacy-policy.html");
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
