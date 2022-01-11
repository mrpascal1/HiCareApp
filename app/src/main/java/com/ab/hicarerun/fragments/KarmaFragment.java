package com.ab.hicarerun.fragments;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ab.hicarerun.BaseApplication;
import com.ab.hicarerun.BaseFragment;
import com.ab.hicarerun.R;
import com.ab.hicarerun.adapter.KarmaAdapter;
import com.ab.hicarerun.adapter.KarmaProgressAdapter;
import com.ab.hicarerun.databinding.FragmentKarmaBinding;
import com.ab.hicarerun.network.NetworkCallController;
import com.ab.hicarerun.network.NetworkResponseListner;
import com.ab.hicarerun.network.models.karmamodel.KarmaHistoryData;
import com.ab.hicarerun.network.models.karmamodel.KarmaHistoryDetails;
import com.ab.hicarerun.network.models.LoginResponse;
import com.ab.hicarerun.network.models.profilemodel.Profile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link KarmaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class KarmaFragment extends BaseFragment {
    private static final int REQ_PROFILE = 1000;
    private static final int REQ_KARMA = 2000;
    FragmentKarmaBinding mFragmentKarmaBinding;
    KarmaAdapter mAdapter;
    KarmaProgressAdapter mProgressAdapter;
    RecyclerView.LayoutManager layoutManager;
    GridLayoutManager gridLayoutManager;
    private List<KarmaHistoryDetails> mHistoryDetails;

    public KarmaFragment() {
        // Required empty public constructor
    }

    public static KarmaFragment newInstance() {
        KarmaFragment fragment = new KarmaFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFragmentKarmaBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_karma, container, false);
        getActivity().setTitle("Your Karma");
        return mFragmentKarmaBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Objects.requireNonNull(getActivity()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        mFragmentKarmaBinding.recycleView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        mFragmentKarmaBinding.recycleView.setLayoutManager(layoutManager);
        mAdapter = new KarmaAdapter(getActivity());
        mFragmentKarmaBinding.recycleView.setAdapter(mAdapter);
        mFragmentKarmaBinding.recycleProgress.setHasFixedSize(true);
        mFragmentKarmaBinding.recycleProgress.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        mProgressAdapter = new KarmaProgressAdapter(getActivity());
        mFragmentKarmaBinding.recycleProgress.setAdapter(mProgressAdapter);

//        setResourceProfile();
        getKarmaHistoryResponse();

    }

    private void getKarmaHistoryResponse() {
        try {
            if (getActivity() != null) {
                RealmResults<LoginResponse> LoginRealmModels =
                        BaseApplication.getRealm().where(LoginResponse.class).findAll();
                assert LoginRealmModels.get(0) != null;
                String userId = LoginRealmModels.get(0).getUserID();
                if (LoginRealmModels != null && LoginRealmModels.size() > 0) {
                    NetworkCallController controller = new NetworkCallController(this);
                    controller.setListner(new NetworkResponseListner<List<KarmaHistoryData>>() {

                        @Override
                        public void onResponse(int requestCode, List<KarmaHistoryData> item) {
                            if (item != null && item.size() > 0) {
                                mHistoryDetails = new ArrayList<>();
                                mProgressAdapter.addData(item);
                                mProgressAdapter.notifyDataSetChanged();
                                mFragmentKarmaBinding.txtKarma.setTypeface(mFragmentKarmaBinding.txtKarma.getTypeface(), Typeface.BOLD);

                                for (int i = 0; i < item.size(); i++) {
                                    if (item.get(i).getIsActiveLifeLine()) {
                                        mHistoryDetails.addAll(item.get(i).getKarmaDetailList());
                                        mFragmentKarmaBinding.arcProgress.setProgress(item.get(i).getTotalPointsPending());
                                        mFragmentKarmaBinding.txtHeader.setText(String.valueOf("Karma of life " + item.get(i).getLifeLineIndex()));
                                        mFragmentKarmaBinding.arcProgress.setBottomText(item.get(i).getLivesLeftDisplay());
                                        if (item.get(i).getTotalPointsPending() >= 80) {
                                            mFragmentKarmaBinding.arcProgress.setFinishedStrokeColor(Objects.requireNonNull(getActivity()).getResources().getColor(R.color.karmaGreen));
                                        } else if (item.get(i).getTotalPointsPending() >= 50 && item.get(i).getTotalPointsPending() < 79) {
                                            mFragmentKarmaBinding.arcProgress.setFinishedStrokeColor(Objects.requireNonNull(getActivity()).getResources().getColor(R.color.amber));
                                        } else {
                                            mFragmentKarmaBinding.arcProgress.setFinishedStrokeColor(Objects.requireNonNull(getActivity()).getResources().getColor(R.color.red));
                                        }
                                    }
                                }

                                if (mHistoryDetails != null && mHistoryDetails.size() > 0) {
                                    mAdapter.addData(mHistoryDetails);
                                    mAdapter.notifyDataSetChanged();
                                    mAdapter.setOnItemClickHandler(position -> {
                                        try {
                                            if (mAdapter.getItem(position).getVideoURL() != null && !mAdapter.getItem(position).getVideoURL().equals("")) {
                                                replaceFragment(KarmaVideoFragment.newInstance(mAdapter.getItem(position).getVideoURL(), mAdapter.getItem(position).getVideoId(), userId, mAdapter.getItem(position).getLifeIndex()), "KarmaFragment - KarmaVideoFragment");
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    });
                                }

                                mProgressAdapter.setOnItemClickHandler(position -> {
                                    if (item.get(position).getKarmaDetailList() != null && item.get(position).getKarmaDetailList().size() > 0) {
                                        mAdapter.addData(item.get(position).getKarmaDetailList());
                                        mFragmentKarmaBinding.txtHeader.setText("Karma of life " + String.valueOf(item.get(position).getLifeLineIndex()));
                                        mAdapter.notifyDataSetChanged();
                                    }
                                });

                            }
                        }

                        @Override
                        public void onFailure(int requestCode) {

                        }
                    });
                    controller.getKarmaHistoryResources(REQ_KARMA, userId);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setResourceProfile() {
        try {
            if (getActivity() != null) {
                RealmResults<LoginResponse> LoginRealmModels =
                        BaseApplication.getRealm().where(LoginResponse.class).findAll();
                assert LoginRealmModels.get(0) != null;
                String userId = LoginRealmModels.get(0).getUserID();
                if (LoginRealmModels != null && LoginRealmModels.size() > 0) {
                    NetworkCallController controller = new NetworkCallController(this);
                    controller.setListner(new NetworkResponseListner() {
                        @Override
                        public void onResponse(int requestCode, Object data) {
                            Profile response = (Profile) data;
                            String TechName = response.getFirstName();
                            mFragmentKarmaBinding.txtUser.setText(TechName);
                            mFragmentKarmaBinding.txtUser.setTypeface(mFragmentKarmaBinding.txtUser.getTypeface(), Typeface.BOLD);
                            if (response.getProfilePic() != null) {
                                String base64 = response.getProfilePic();
                                byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
                                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                if (base64.length() > 0) {
                                    mFragmentKarmaBinding.imgUser.setImageBitmap(decodedByte);
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
            e.printStackTrace();
        }
    }
}