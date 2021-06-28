package com.ab.hicarerun.fragments;

import android.app.Activity;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ab.hicarerun.BaseFragment;
import com.ab.hicarerun.R;
import com.ab.hicarerun.adapter.AddActivityAdapter;
import com.ab.hicarerun.adapter.AddChemicalActivityAdapter;
import com.ab.hicarerun.adapter.ChemicalAreaParentAdapter;
import com.ab.hicarerun.adapter.ChemicalTowerAdapter;
import com.ab.hicarerun.databinding.FragmentChemicalStandardBinding;
import com.ab.hicarerun.handler.OnAddChemicalActivity;
import com.ab.hicarerun.handler.OnSelectChemicalClickHandler;
import com.ab.hicarerun.handler.OnSelectServiceClickHandler;
import com.ab.hicarerun.network.NetworkCallController;
import com.ab.hicarerun.network.NetworkResponseListner;
import com.ab.hicarerun.network.models.ChemicalModel.ActivityData;
import com.ab.hicarerun.network.models.ChemicalModel.ServiceChemicalData;
import com.ab.hicarerun.network.models.ChemicalModel.ServiceData;
import com.ab.hicarerun.network.models.ChemicalModel.TowerData;
import com.ab.hicarerun.network.models.OnSiteModel.ActivityDetail;
import com.ab.hicarerun.utils.AppUtils;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChemicalStandardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChemicalStandardFragment extends BaseFragment implements OnAddChemicalActivity {
    FragmentChemicalStandardBinding mFragmentChemicalStandardBinding;
    ChemicalTowerAdapter mAdapter;
    ChemicalAreaParentAdapter mParentAdapter;
    AddChemicalActivityAdapter mAddAdapter;
    RecyclerView.LayoutManager layoutManager;
    private List<TowerData> mTowerList;
    private List<TowerData> mCommonTowerList;
    private List<TowerData> mRegularTowerList;
    private List<ServiceData> mServiceList;
    private List<ActivityData> mActivityList;
    List<ActivityDetail> activityItems = null;
    HashMap<String, ActivityDetail> hashActivity = null;

    public ChemicalStandardFragment() {
        // Required empty public constructor
    }


    public static ChemicalStandardFragment newInstance() {
        ChemicalStandardFragment fragment = new ChemicalStandardFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFragmentChemicalStandardBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_chemical_standard, container, false);
        return mFragmentChemicalStandardBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTowerList = new ArrayList<>();
        mCommonTowerList = new ArrayList<>();
        mRegularTowerList = new ArrayList<>();
        mFragmentChemicalStandardBinding.recycleTower.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        mFragmentChemicalStandardBinding.recycleTower.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mAdapter = new ChemicalTowerAdapter(getActivity());
        mFragmentChemicalStandardBinding.recycleTower.setAdapter(mAdapter);

        mFragmentChemicalStandardBinding.recycleArea.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        mFragmentChemicalStandardBinding.recycleArea.setLayoutManager(layoutManager);
        mParentAdapter = new ChemicalAreaParentAdapter(getActivity());
        mFragmentChemicalStandardBinding.recycleArea.setAdapter(mParentAdapter);
        mParentAdapter.setOnAddChemicalActivity(this);
        getServiceAreaChemical();
        mParentAdapter.notifyDataSetChanged();
    }

    private void getServiceAreaChemical() {
        try {
            NetworkCallController controller = new NetworkCallController(this);
            controller.setListner(new NetworkResponseListner<List<ServiceChemicalData>>() {
                @Override
                public void onResponse(int requestCode, List<ServiceChemicalData> items) {
                    if (items != null && items.size() > 0) {
                        for (ServiceChemicalData data : items) {
                            if (data.getAreaType().equals("Common Area")) {
                                mCommonTowerList.addAll(data.getTower());
                            } else {
                                mRegularTowerList.addAll(data.getTower());
                            }
                            mTowerList.addAll(mCommonTowerList);
                            mTowerList.addAll(mRegularTowerList);
                            mTowerList.get(0).setTower(0);
                            mAdapter.setData(mTowerList);
                            mAdapter.notifyDataSetChanged();

                            mAdapter.setOnItemClickHandler(position -> {
                                mServiceList = new ArrayList<>();
                                mServiceList = mTowerList.get(position).getServices();
                                mParentAdapter.addData(mServiceList);
                                mParentAdapter.notifyDataSetChanged();
                            });
                        }
                    }
                }

                @Override
                public void onFailure(int requestCode) {

                }
            });
            controller.getServiceAreaChemical(149, 1, "CMS", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAddActivityClicked(int parentPosition, int childPosition, List<ActivityData> activity) {
        try {
            mActivityList = new ArrayList<>();
            mActivityList = activity;
            showAddActivityDialog(mActivityList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAddActivityDialog(List<ActivityData> mActivityList) {
        try {
            LayoutInflater li = LayoutInflater.from(getActivity());
            View promptsView = li.inflate(R.layout.layout_add_chemcal_activity_dialog, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
            alertDialogBuilder.setView(promptsView);
            final AlertDialog alertDialog = alertDialogBuilder.create();
            activityItems = new ArrayList<>();
            hashActivity = new HashMap<>();
            alertDialog.setCancelable(false);
            final RecyclerView recyclerView =
                    (RecyclerView) promptsView.findViewById(R.id.recycleView);
            final Button btnDone =
                    (Button) promptsView.findViewById(R.id.btnDone);
            final Button btnCancel =
                    (Button) promptsView.findViewById(R.id.btnCancel);
            final TextView txtTitle =
                    (TextView) promptsView.findViewById(R.id.txtTitle);
            txtTitle.setTypeface(txtTitle.getTypeface(), Typeface.BOLD);
            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
            mAddAdapter = new AddChemicalActivityAdapter(getActivity(), mActivityList, (position, isCheckedList) -> {
                if (isAnyTrue(isCheckedList)) {
                    btnDone.setEnabled(false);
                    btnDone.setText(getResources().getString(R.string.button_select));
                    btnDone.setAlpha(0.5f);

                } else {
                    btnDone.setEnabled(true);
                    btnDone.setText(getResources().getString(R.string.done_onsite));
                    btnDone.setAlpha(1f);
                }

            });
            recyclerView.setAdapter(mAddAdapter);
            btnDone.setOnClickListener(v -> {
//                getSaveActivity(onSiteArea, true, "");
                alertDialog.dismiss();
            });

            mAddAdapter.setOnSelectServiceClickHandler(new OnSelectChemicalClickHandler() {
                @Override
                public void onRadioYesClicked(int position) {
               /*     ActivityDetail activityDetail = new ActivityDetail();
                    activityDetail.setActivityId(0);
                    activityDetail.setCreatedBy(0);
                    activityDetail.setServiceType(serviceList.get(position));
                    activityDetail.setLat(String.valueOf(Lat));
                    activityDetail.setLon(String.valueOf(Lon));
                    activityDetail.setStartTime(AppUtils.currentDateTime());
                    activityDetail.setEndTime(AppUtils.currentDateTime());
                    activityDetail.setIsServiceDone(true);
                    activityDetail.setModifiedBy(0);
                    activityDetail.setServiceNotDoneReason("");
                    hashActivity.put(serviceList.get(position), activityDetail);*/

                }

                @Override
                public void onRadioNoClicked(int position) {
                   /* ActivityDetail activityDetail = new ActivityDetail();
                    activityDetail.setActivityId(0);
                    activityDetail.setCreatedBy(0);
                    activityDetail.setServiceType(serviceList.get(position));
                    activityDetail.setLat(String.valueOf(Lat));
                    activityDetail.setLon(String.valueOf(Lon));
                    activityDetail.setStartTime(AppUtils.currentDateTime());
                    activityDetail.setEndTime(AppUtils.currentDateTime());
                    activityDetail.setIsServiceDone(false);
                    activityDetail.setModifiedBy(0);
                    activityDetail.setServiceNotDoneReason("");
                    hashActivity.put(serviceList.get(position), activityDetail);*/
                }

                @Override
                public void onRadioNotDoneClicked(int position) {

                }

            });

            btnCancel.setOnClickListener(view -> alertDialog.cancel());
            Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            alertDialog.setIcon(R.mipmap.logo);
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isAnyTrue(List<String> arraylist) {
        for (String str : arraylist) {
            if (str.equals("true")) {
                return false;
            }
        }
        return true;
    }

}