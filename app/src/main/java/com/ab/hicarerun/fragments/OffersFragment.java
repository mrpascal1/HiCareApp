package com.ab.hicarerun.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.ab.hicarerun.BaseApplication;
import com.ab.hicarerun.BaseFragment;
import com.ab.hicarerun.R;
import com.ab.hicarerun.activities.HomeActivity;
import com.ab.hicarerun.adapter.OnSiteAccountAdapter;
import com.ab.hicarerun.adapter.RecycleOffersAdapter;
import com.ab.hicarerun.databinding.FragmentOffersBinding;
import com.ab.hicarerun.handler.OnRewardClickItemClickHandler;
import com.ab.hicarerun.network.NetworkCallController;
import com.ab.hicarerun.network.NetworkResponseListner;
import com.ab.hicarerun.network.models.LoginResponse;
import com.ab.hicarerun.network.models.RewardsModel.AvailableOffer;
import com.ab.hicarerun.network.models.RewardsModel.RewardsData;
import com.ab.hicarerun.network.models.RewardsModel.SaveRedeemRequest;
import com.ab.hicarerun.network.models.RewardsModel.SaveRedeemResponse;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class OffersFragment extends BaseFragment implements OnRewardClickItemClickHandler {
    FragmentOffersBinding mFragmentOffersBinding;
    RecyclerView.LayoutManager layoutManager;
    private RecycleOffersAdapter mAdapter;
    private static final int REDEEM_REQ = 1000;
    private static final int SAVE_REDEEM_REQ = 2000;
    private TextView txtTotalPoints;
    private Integer pageNumber = 1;
    private List<AvailableOffer> offerList;

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
        return mFragmentOffersBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFragmentOffersBinding.recycleView.setHasFixedSize(true);
//        layoutManager = new GridLayoutManager(getActivity(), 2);
        mFragmentOffersBinding.recycleView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mAdapter = new RecycleOffersAdapter(getActivity());
        mAdapter.setOnRedeemClickHandler(this);
        mFragmentOffersBinding.recycleView.setAdapter(mAdapter);
        setOffers();


    }


    private void setOffers() {
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
                    txtTotalPoints.setText(formattedPoints);
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
                            pageNumber--;
                        }
                    }
                }


                @Override
                public void onFailure(int requestCode) {

                }
            });
            controller.getResourceRedeemedData(REDEEM_REQ, userId);
        }
    }

    @Override
    public void onOfferRedeemClicked(int position) {
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

                    }
                }

                @Override
                public void onFailure(int requestCode) {

                }
            });
            controller.getSaveRedeemOffer(SAVE_REDEEM_REQ, request);
        }
    }

    @Override
    public void onItemClick(int position) {

    }
}
