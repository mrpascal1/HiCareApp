package com.ab.hicarerun.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;

import com.ab.hicarerun.BaseFragment;
import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.FragmentKycGalleryBinding;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class KycGalleryFragment extends BaseFragment {
    FragmentKycGalleryBinding mFragmentKycGalleryBinding;
    public static final String ARGS_URL = "ARGS_URL";
    private String imgURL = "";



    public KycGalleryFragment() {
        // Required empty public constructor
    }

    public static KycGalleryFragment newInstance(String document_url) {
        Bundle args = new Bundle();
        args.putString(ARGS_URL, document_url);
        KycGalleryFragment fragment = new KycGalleryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            imgURL = getArguments().getString(ARGS_URL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFragmentKycGalleryBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_kyc_gallery, container, false);
        getActivity().setTitle("KYC Verification");
        return mFragmentKycGalleryBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFragmentKycGalleryBinding.imgDocument.getSettings().setBuiltInZoomControls(true);
        mFragmentKycGalleryBinding.imgDocument.getSettings().setLoadWithOverviewMode(true);
        mFragmentKycGalleryBinding.imgDocument.getSettings().setUseWideViewPort(true);
        mFragmentKycGalleryBinding.imgDocument.loadUrl(imgURL);
    }


}
