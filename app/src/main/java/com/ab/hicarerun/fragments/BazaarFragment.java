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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ab.hicarerun.BaseApplication;
import com.ab.hicarerun.BaseFragment;
import com.ab.hicarerun.R;
import com.ab.hicarerun.adapter.RecycleBazaarAdapter;
import com.ab.hicarerun.databinding.FragmentBazaarBinding;
import com.ab.hicarerun.handler.OnBazaarItemClickHandler;
import com.ab.hicarerun.network.NetworkCallController;
import com.ab.hicarerun.network.NetworkResponseListner;
import com.ab.hicarerun.network.models.LoginResponse;
import com.ab.hicarerun.network.models.rewardsmodel.AvailableOffer;
import com.ab.hicarerun.network.models.rewardsmodel.RewardsData;
import com.ab.hicarerun.network.models.rewardsmodel.SaveRedeemRequest;
import com.ab.hicarerun.network.models.rewardsmodel.SaveRedeemResponse;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class BazaarFragment extends BaseFragment implements OnBazaarItemClickHandler {
    FragmentBazaarBinding mFragmentBazaarBinding;
    RecyclerView.LayoutManager layoutManager;
    private RecycleBazaarAdapter mAdapter;
    private static final int REDEEM_REQ = 1000;
    private static final int SAVE_REDEEM_REQ = 2000;
    private List<AvailableOffer> offerList;
    private Integer pageNumber = 1;
    private TextView txtTotalPoints;
    private TextView txtEarnedPoints;
    private LinearLayout lnrRew;

    public BazaarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onSaveInstanceState(Bundle oldInstanceState)
    {
        super.onSaveInstanceState(oldInstanceState);
        oldInstanceState.clear();
    }

    public static BazaarFragment newInstance() {
        Bundle args = new Bundle();
        BazaarFragment fragment = new BazaarFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentBazaarBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_bazaar, container, false);
        getActivity().setTitle(getResources().getString(R.string.bazaar));
        txtTotalPoints = getActivity().findViewById(R.id.txtPoints);
        txtEarnedPoints = getActivity().findViewById(R.id.txtEarnedPoints);
        lnrRew = getActivity().findViewById(R.id.empty_task);
        return mFragmentBazaarBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFragmentBazaarBinding.recycleView.setHasFixedSize(true);
        mFragmentBazaarBinding.recycleView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mAdapter = new RecycleBazaarAdapter(getActivity());
        mAdapter.setOnRedeemClickHandler(this);
        mFragmentBazaarBinding.recycleView.setAdapter(mAdapter);
        setOffers();
    }

    private void setOffers() {
        try {
            RealmResults<LoginResponse> LoginRealmModels =
                    BaseApplication.getRealm().where(LoginResponse.class).findAll();
            if (LoginRealmModels != null && LoginRealmModels.size() > 0) {
                String userId = LoginRealmModels.get(0).getUserID();
                NetworkCallController controller = new NetworkCallController(this);
                controller.setListner(new NetworkResponseListner<RewardsData>() {
                    @Override
                    public void onResponse(int requestCode, RewardsData response) {
                        DecimalFormat formatter = new DecimalFormat("#,###,###");
                        String formattedPoints = formatter.format(response.getTotalPendingPoints());
                        String formattedEarned = formatter.format(response.getTotalPointsEarned());
                        txtTotalPoints.setText(formattedPoints);
                        txtEarnedPoints.setText(formattedEarned);
                        if (response.getAvailableOffers() != null) {
                            offerList = new ArrayList<>();
                            offerList = response.getAvailableOffers();
                            if (pageNumber == 1 && response.getAvailableOffers().size() > 0) {
                                mAdapter.setData(response.getAvailableOffers(), response.getTotalPendingPoints());
                                mAdapter.notifyDataSetChanged();
                            } else if (response.getAvailableOffers().size() > 0) {
                                mAdapter.addData(response.getAvailableOffers(), response.getTotalPendingPoints());
                                mAdapter.notifyDataSetChanged();
                            } else {
//                                mFragmentBazaarBinding.emptyTask.setVisibility(View.VISIBLE);
                                lnrRew.setVisibility(View.VISIBLE);
                                pageNumber--;
                            }
                        } else {
//                            mFragmentBazaarBinding.emptyTask.setVisibility(View.VISIBLE);
                            lnrRew.setVisibility(View.VISIBLE);
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

    @Override
    public void onOfferRedeemClicked(int position) {
        try {
            RealmResults<LoginResponse> LoginRealmModels =
                    BaseApplication.getRealm().where(LoginResponse.class).findAll();
            if (LoginRealmModels != null && LoginRealmModels.size() > 0) {
                String userId = LoginRealmModels.get(0).getUserID();
                NetworkCallController controller = new NetworkCallController(this);
                SaveRedeemRequest request = new SaveRedeemRequest();
                request.setOfferId(offerList.get(position).getOfferId());
                request.setPointsUsed(offerList.get(position).getPointsRequired());
                request.setResourceId(userId);
                controller.setListner(new NetworkResponseListner<SaveRedeemResponse>() {

                    @Override
                    public void onResponse(int requestCode, SaveRedeemResponse response) {
                        if (response.getIsSuccess()) {
                            setOffers();
                        }
                    }

                    @Override
                    public void onFailure(int requestCode) {

                    }
                });
                controller.getSaveRedeemOffer(SAVE_REDEEM_REQ, request);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(int position) {

    }
}
