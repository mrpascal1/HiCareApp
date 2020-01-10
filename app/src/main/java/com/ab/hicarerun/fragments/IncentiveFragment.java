package com.ab.hicarerun.fragments;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ab.hicarerun.BaseApplication;
import com.ab.hicarerun.BaseFragment;
import com.ab.hicarerun.R;
import com.ab.hicarerun.activities.IncentivesActivity;
import com.ab.hicarerun.adapter.ChemicalRecycleAdapter;
import com.ab.hicarerun.adapter.IncentiveMatrixAdapter;
import com.ab.hicarerun.adapter.ReferralListAdapter;
import com.ab.hicarerun.databinding.FragmentIncentiveBinding;
import com.ab.hicarerun.network.NetworkCallController;
import com.ab.hicarerun.network.NetworkResponseListner;
import com.ab.hicarerun.network.models.IncentiveModel.Incentive;
import com.ab.hicarerun.network.models.LoginResponse;
import com.ab.hicarerun.network.models.ProfileModel.Profile;

import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class IncentiveFragment extends BaseFragment {

    private FragmentIncentiveBinding mFragmentIncentiveBinding;
    private static final int REQ_PROFILE = 1000;
    private static final int REQ_INCENTIVE = 2000;
    IncentiveMatrixAdapter mAdapter;
    RecyclerView.LayoutManager layoutManager;

    public IncentiveFragment() {
        // Required empty public constructor
    }

    public static IncentiveFragment newInstance() {
        Bundle args = new Bundle();
        IncentiveFragment fragment = new IncentiveFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentIncentiveBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_incentive, container, false);
        getActivity().setTitle("Incentive");
        return mFragmentIncentiveBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String text = "<font>Incentive Earned</font><font color=#ff0000>*</font>";
        mFragmentIncentiveBinding.txtEarned.setText(Html.fromHtml(text));

        mFragmentIncentiveBinding.recycleView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        mFragmentIncentiveBinding.recycleView.setLayoutManager(layoutManager);
        mAdapter = new IncentiveMatrixAdapter();
        mFragmentIncentiveBinding.recycleView.setAdapter(mAdapter);
        getIncentiveDetails();
        getTechDeails();
    }

    private void getTechDeails() {
        try {
            if ((IncentivesActivity) getActivity() != null) {
                RealmResults<LoginResponse> LoginRealmModels =
                        BaseApplication.getRealm().where(LoginResponse.class).findAll();
                String userId = LoginRealmModels.get(0).getUserID();
                if (LoginRealmModels != null && LoginRealmModels.size() > 0) {
                    NetworkCallController controller = new NetworkCallController(this);
                    controller.setListner(new NetworkResponseListner() {
                        @Override
                        public void onResponse(int requestCode, Object data) {
                            Profile response = (Profile) data;
                            String TechName = response.getFirstName();

                            mFragmentIncentiveBinding.txtUser.setText(TechName);
                            if (response.getProfilePic() != null) {
                                String base64 = response.getProfilePic();
                                byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
                                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                if (base64.length() > 0) {
                                    mFragmentIncentiveBinding.imgUser.setImageBitmap(decodedByte);
                                }
                            }

                        }

                        @Override
                        public void onFailure(int requestCode) {

                        }
                    });
                    controller.getTechnicianProfile(REQ_PROFILE, userId);
                }
            }
        } catch (Exception e) {

        }
    }

    private void getIncentiveDetails() {

        if (getActivity() != null) {
            RealmResults<LoginResponse> LoginRealmModels =
                    BaseApplication.getRealm().where(LoginResponse.class).findAll();
            String userId = LoginRealmModels.get(0).getUserID();
            if (LoginRealmModels != null && LoginRealmModels.size() > 0) {
                NetworkCallController controller = new NetworkCallController(this);
                controller.setListner(new NetworkResponseListner() {
                    @Override
                    public void onResponse(int requestCode, Object data) {
                        Incentive response = (Incentive) data;
                        mFragmentIncentiveBinding.txtPoints.setText(response.getTotalPoint());
                        mFragmentIncentiveBinding.txtIncentive.setText("\u20B9" + response.getTotalIncentive());
                        if (response.getIncentiveDate() != null) {
                            mFragmentIncentiveBinding.txtDate.setVisibility(View.VISIBLE);
                            mFragmentIncentiveBinding.txtDate.setText("Incentive as on: " + response.getIncentiveDate());
                        } else {
                            mFragmentIncentiveBinding.txtDate.setVisibility(View.GONE);
                        }
                        if (response.getMatrixList() != null) {
                            mAdapter.setData(response.getMatrixList());
                            mAdapter.notifyDataSetChanged();
                        }

                    }

                    @Override
                    public void onFailure(int requestCode) {

                    }
                });
                controller.getTechnicianIncentive(REQ_INCENTIVE, userId);
            }
        }
    }

}
