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
import com.ab.hicarerun.databinding.FragmentTermsAndConditionsBinding;

import org.jetbrains.annotations.NotNull;

/**
 * A simple {@link Fragment} subclass.
 */
public class TermsAndConditionsFragment extends BaseFragment {
    FragmentTermsAndConditionsBinding mFragmentTermsAndConditionsBinding;

    public TermsAndConditionsFragment() {
        // Required empty public constructor
    }

    public static TermsAndConditionsFragment newInstance() {
        Bundle args = new Bundle();
        TermsAndConditionsFragment fragment = new TermsAndConditionsFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFragmentTermsAndConditionsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_terms_and_conditions, container, false);
        return mFragmentTermsAndConditionsBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFragmentTermsAndConditionsBinding.webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
        });
        try {
            mFragmentTermsAndConditionsBinding.webView.getSettings().setJavaScriptEnabled(true);
            mFragmentTermsAndConditionsBinding.webView.loadUrl("https://hicare.in/terms-conditions");
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
