package com.ab.hicarerun.fragments;


import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ab.hicarerun.BaseApplication;
import com.ab.hicarerun.BaseFragment;
import com.ab.hicarerun.R;
import com.ab.hicarerun.adapter.ChemicalDialogAdapter;
import com.ab.hicarerun.adapter.RecycleOffersAdapter;
import com.ab.hicarerun.adapter.RewardsSummaryAdapter;
import com.ab.hicarerun.databinding.FragmentOffersBinding;
import com.ab.hicarerun.handler.OnRewardtemClickHandler;
import com.ab.hicarerun.network.NetworkCallController;
import com.ab.hicarerun.network.NetworkResponseListner;
import com.ab.hicarerun.network.models.LoginResponse;
import com.ab.hicarerun.network.models.OffersModel.OffersData;
import com.ab.hicarerun.network.models.OffersModel.RewardDetailSummary;
import com.ab.hicarerun.network.models.OffersModel.RewardList;
import com.ab.hicarerun.network.models.OffersModel.UpdateRewardScratchRequest;
import com.ab.hicarerun.network.models.OffersModel.UpdateRewardScratchResponse;
import com.ab.hicarerun.network.models.RewardsModel.AvailableOffer;
import com.ab.hicarerun.network.models.RewardsModel.RewardsData;
import com.ab.hicarerun.network.models.RewardsModel.SaveRedeemRequest;
import com.ab.hicarerun.network.models.RewardsModel.SaveRedeemResponse;
import com.ab.hicarerun.utils.AppUtils;
import com.ab.hicarerun.utils.notifications.ScratchRelativeLayout;
import com.clock.scratch.ScratchView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class OffersFragment extends BaseFragment implements OnRewardtemClickHandler {
    FragmentOffersBinding mFragmentOffersBinding;
    RecyclerView.LayoutManager layoutManager;
    private RecycleOffersAdapter mAdapter;
    private static final int REWARDS_REQ = 2000;
    private static final int UPDATE_SCRATCH_REQ = 3000;
    private TextView txtTotalPoints;
    private LinearLayout lnrRew;
    private Integer pageNumber = 1;
    private List<RewardList> offerList;
    private List<RewardDetailSummary> rewardSummaryList;
    private int Incentive = 0;
    private RewardsSummaryAdapter summaryAdapter;
    private boolean isRewardScratchDone = false;


    public OffersFragment() {
        // Required empty public constructor
    }

    public static OffersFragment newInstance() {
        Bundle args = new Bundle();
        OffersFragment fragment = new OffersFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentOffersBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_offers, container, false);
//        fragmentOnsiteAccountBinding.setHandler(this);
//        if ((HomeActivity) getActivity() != null) {
//            LinearLayout toolbar = getActivity().findViewById(R.id.toolbar);
//            toolbar.setVisibility(View.GONE);
//            LinearLayout custom_toolbar = getActivity().findViewById(R.id.customToolbar);
//            custom_toolbar.setVisibility(View.VISIBLE);
//            TextView tool = getActivity().findViewById(R.id.txtTool);
//            tool.setText("RewardsData");
//        }
        txtTotalPoints = getActivity().findViewById(R.id.txtPoints);
        lnrRew = getActivity().findViewById(R.id.empty_task);
        return mFragmentOffersBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFragmentOffersBinding.recycleView.setHasFixedSize(true);
//        layoutManager = new GridLayoutManager(getActivity(), 2);
        mFragmentOffersBinding.recycleView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mAdapter = new RecycleOffersAdapter(getActivity());
        mAdapter.setOnRewardClickListener(this);
        mFragmentOffersBinding.recycleView.setAdapter(mAdapter);
//        setOffers();
        getAllRewards();

    }

    private void getAllRewards() {
        try {
            RealmResults<LoginResponse> LoginRealmModels =
                    BaseApplication.getRealm().where(LoginResponse.class).findAll();
            if (LoginRealmModels != null && LoginRealmModels.size() > 0) {
                String userId = LoginRealmModels.get(0).getUserID();
                NetworkCallController controller = new NetworkCallController(this);
                controller.setListner(new NetworkResponseListner<OffersData>() {
                    @Override
                    public void onResponse(int requestCode, OffersData response) {
                        DecimalFormat formatter = new DecimalFormat("#,###,###");

                        if (response != null) {
                            String formattedPoints = formatter.format(response.getTotalPointsEarned());
                            txtTotalPoints.setText(formattedPoints);
                            if (response.getRewardList() != null) {
                                offerList = new ArrayList<>();
                                offerList = response.getRewardList();
                                if (pageNumber == 1 && response.getRewardList().size() > 0) {
                                    mAdapter.setData(response.getRewardList());
                                    mAdapter.notifyDataSetChanged();
                                } else if (response.getRewardList().size() > 0) {
                                    mAdapter.addData(response.getRewardList());
                                    mAdapter.notifyDataSetChanged();
                                } else {
                                    lnrRew.setVisibility(View.VISIBLE);
                                    pageNumber--;
                                }
                            } else {
                               lnrRew.setVisibility(View.VISIBLE);
                            }
                        } else {
                            lnrRew.setVisibility(View.VISIBLE);
                        }

                    }

                    @Override
                    public void onFailure(int requestCode) {

                    }
                });
                controller.getAllRewards(REWARDS_REQ, userId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onRewardClicked(int position) {
        showRewardsDialog(position);
    }


    private void showRewardsDialog(int position) {
        try {
            View view = getLayoutInflater().inflate(R.layout.rewards_scrachcard_layout, null);
            final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
            Window window = dialog.getWindow();
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            assert window != null;
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            window.setBackgroundDrawableResource(R.color.darkblack);
            dialog.setContentView(view);
            dialog.show();
            final AppCompatTextView txtReward =
                    view.findViewById(R.id.txtReward);
            final AppCompatTextView txtIncentive =
                    view.findViewById(R.id.txtIncentive);
            final AppCompatTextView txtLose =
                    view.findViewById(R.id.txtLose);
            final AppCompatImageView imgAward =
                    view.findViewById(R.id.imgAward);
            final AppCompatImageView imgNoAward =
                    view.findViewById(R.id.imgNoAward);
            final AppCompatImageView imgCancel =
                    view.findViewById(R.id.imgCancel);
            final ScratchRelativeLayout scratch =
                    view.findViewById(R.id.scratch);
            final AppCompatTextView txtMsg =
                    view.findViewById(R.id.winningMsg);
            final AppCompatTextView txtEarned =
                    view.findViewById(R.id.txtEarned);
            final AppCompatTextView txtHicare =
                    view.findViewById(R.id.txtHicare);
            final RecyclerView recyclerView =
                    view.findViewById(R.id.recycleView);
            rewardSummaryList = offerList.get(position).getRewardDetailSummary();
            isRewardScratchDone = offerList.get(position).getIsRewardScratchDone();
            String scratchedId = offerList.get(position).getId();
            RecyclerView.LayoutManager lm = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(lm);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            summaryAdapter = new RewardsSummaryAdapter(getActivity(), rewardSummaryList);
            recyclerView.setAdapter(summaryAdapter);
            if (rewardSummaryList != null && rewardSummaryList.size() > 0) {
                summaryAdapter.addData(rewardSummaryList);
                summaryAdapter.notifyDataSetChanged();
            }

            Incentive = offerList.get(position).getTotalPointsEarned();
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            Objects.requireNonNull(dialog.getWindow()).setLayout(width, height);
            imgCancel.setOnClickListener(v -> dialog.dismiss());
            String[] array = getActivity().getResources().getStringArray(R.array.randomApplause);
            String randomStr = array[new Random().nextInt(array.length)];
            if (Incentive == 0) {
                imgAward.setVisibility(View.INVISIBLE);
                imgNoAward.setVisibility(View.VISIBLE);
                txtLose.setVisibility(View.VISIBLE);
                txtIncentive.setVisibility(View.GONE);
                txtReward.setVisibility(View.GONE);
                txtMsg.setText("Oops!");

            } else {
                imgAward.setVisibility(View.VISIBLE);
                imgNoAward.setVisibility(View.INVISIBLE);
                txtLose.setVisibility(View.GONE);
                txtIncentive.setVisibility(View.VISIBLE);
                txtReward.setVisibility(View.VISIBLE);
                txtIncentive.setText(Incentive + " Points");
                txtMsg.setText(randomStr);
            }

            if (isRewardScratchDone) {
                scratch.setVisibility(View.GONE);
                txtMsg.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                txtEarned.setVisibility(View.VISIBLE);
                txtHicare.setVisibility(View.VISIBLE);
            } else {
                scratch.setVisibility(View.VISIBLE);
            }

//            int[] images = {R.drawable.offer_cover_img};
//            Random rand = new Random();
            scratch.setWatermark(R.drawable.offer_cover_img);
            scratch.setEraseStatusListener(new ScratchView.EraseStatusListener() {
                @Override
                public void onProgress(int percent) {
                    try {
                        if (percent >= 30 && percent <= 50) {
                            imgCancel.setVisibility(View.VISIBLE);
                            txtMsg.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.VISIBLE);
                            RealmResults<LoginResponse> LoginRealmModels =
                                    BaseApplication.getRealm().where(LoginResponse.class).findAll();
                            if (LoginRealmModels != null && LoginRealmModels.size() > 0) {
                                String userId = LoginRealmModels.get(0).getUserID();
                                NetworkCallController controller = new NetworkCallController();
                                UpdateRewardScratchRequest request = new UpdateRewardScratchRequest();
                                request.setId(scratchedId);
                                request.setIsRewardScratchDone(true);
                                request.setResourceId(userId);
                                controller.setListner(new NetworkResponseListner<UpdateRewardScratchResponse>() {
                                    @Override
                                    public void onResponse(int requestCode, UpdateRewardScratchResponse response) {
                                        getAllRewards();
                                    }

                                    @Override
                                    public void onFailure(int requestCode) {

                                    }
                                });
                                controller.updateRewardScratch(UPDATE_SCRATCH_REQ, request);
                            }
                            if (Incentive > 0) {
                                txtEarned.setVisibility(View.VISIBLE);
                                txtHicare.setVisibility(View.VISIBLE);
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCompleted(View view) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onItemClick(int position) {

    }
}
