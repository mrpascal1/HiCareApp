package com.ab.hicarerun.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.LeaderBoardAdapterBinding;
import com.ab.hicarerun.databinding.OnsiteRecentAdapterBinding;
import com.ab.hicarerun.handler.OnRecentTaskClickHandler;
import com.ab.hicarerun.network.NetworkCallController;
import com.ab.hicarerun.network.NetworkResponseListner;
import com.ab.hicarerun.network.models.LeaderBoardModel.RewardLeaders;
import com.ab.hicarerun.network.models.OffersModel.RewardList;
import com.ab.hicarerun.network.models.OnSiteModel.OnSiteRecent;
import com.ab.hicarerun.network.models.ProfileModel.Profile;
import com.ab.hicarerun.utils.AppUtils;
import com.ab.hicarerun.viewmodel.RecentActivityViewModel;
import com.ab.hicarerun.viewmodel.RewardLeadersViewModel;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arjun Bhatt on 4/9/2020.
 */
public class RewardLeadersAdapter extends RecyclerView.Adapter<RewardLeadersAdapter.ViewHolder> {
    private final Context mContext;
    private List<RewardLeadersViewModel> items = null;
    private static final int RESOURCE_REQ = 1000;

    public RewardLeadersAdapter(Context context) {
        if (items == null) {
            items = new ArrayList<>();
        }
        this.mContext = context;
    }

    @NotNull
    @Override
    public RewardLeadersAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        LeaderBoardAdapterBinding mLeaderBoardAdapterBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.leader_board_adapter, parent, false);
        return new RewardLeadersAdapter.ViewHolder(mLeaderBoardAdapterBinding);
    }

    @Override
    public void onBindViewHolder(@NotNull final RewardLeadersAdapter.ViewHolder holder, final int position) {
        try {

            if (items.get(position).getRecourceId() != null) {
                try {
                    NetworkCallController controller = new NetworkCallController();
                    controller.setListner(new NetworkResponseListner() {
                        @Override
                        public void onResponse(int requestCode, Object response) {
                            String base64 = (String) response;
                            byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            if (base64.length() > 0) {
                                holder.mLeaderBoardAdapterBinding.imgRest.setImageBitmap(decodedByte);
                            }
                        }

                        @Override
                        public void onFailure(int requestCode) {

                        }
                    });
                    controller.getResourceProfilePicture(RESOURCE_REQ, items.get(position).getRecourceId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (items.get(position).getSameResource()) {
                holder.mLeaderBoardAdapterBinding.lnrRest.setBackgroundColor(mContext.getResources().getColor(R.color.greenlight));
            } else {
                holder.mLeaderBoardAdapterBinding.lnrRest.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            }
            if (items.get(position).getBadgeName() != null && !items.get(position).getBadgeName().equals("")) {
                holder.mLeaderBoardAdapterBinding.imgBadge.setVisibility(View.VISIBLE);
                holder.mLeaderBoardAdapterBinding.imgBadge.setImageResource(AppUtils.getBadgeImage(items.get(position).getBadgeName()));
            }else {
                holder.mLeaderBoardAdapterBinding.imgBadge.setVisibility(View.GONE);
            }
            holder.mLeaderBoardAdapterBinding.txtName.setText(items.get(position).getRecourceName());
            holder.mLeaderBoardAdapterBinding.txtCentre.setText(items.get(position).getServiceCentreName());
            holder.mLeaderBoardAdapterBinding.txtRank.setText(String.valueOf(items.get(position).getRank()));
            holder.mLeaderBoardAdapterBinding.txtPoints.setText(String.valueOf(items.get(position).getTotalPoints()) + " Pts.");
            holder.mLeaderBoardAdapterBinding.txtMissed.setText("(Lost " + String.valueOf(items.get(position).getMissedPoints()) + " Pts.)");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setData(List<RewardLeaders> data) {
        items.clear();
        for (int index = 0; index < data.size(); index++) {
            RewardLeadersViewModel rewardLeadersViewModel = new RewardLeadersViewModel();
            rewardLeadersViewModel.clone(data.get(index));
            items.add(rewardLeadersViewModel);
        }
    }

    public void addData(List<RewardLeaders> data) {
        items.clear();
        for (int index = 0; index < data.size(); index++) {
            RewardLeadersViewModel rewardLeadersViewModel = new RewardLeadersViewModel();
            rewardLeadersViewModel.clone(data.get(index));
            items.add(rewardLeadersViewModel);
        }
    }

    public void removeAll() {
        items.removeAll(items);
        notifyDataSetChanged();
    }

    public RewardLeadersViewModel getItem(int position) {
        return items.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final LeaderBoardAdapterBinding mLeaderBoardAdapterBinding;

        public ViewHolder(LeaderBoardAdapterBinding mLeaderBoardAdapterBinding) {
            super(mLeaderBoardAdapterBinding.getRoot());
            this.mLeaderBoardAdapterBinding = mLeaderBoardAdapterBinding;
        }
    }


}
