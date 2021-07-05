package com.ab.hicarerun.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ab.hicarerun.BaseFragment;
import com.ab.hicarerun.R;
import com.ab.hicarerun.adapter.ActivityTowerAdapter;
import com.ab.hicarerun.adapter.ChemicalActivityAdapter;
import com.ab.hicarerun.adapter.ChemicalTowerAdapter;
import com.ab.hicarerun.adapter.RecycleByActivityAdapter;
import com.ab.hicarerun.databinding.FragmentServiceUnitBinding;
import com.ab.hicarerun.handler.OnAddActivityClickHandler;
import com.ab.hicarerun.network.NetworkCallController;
import com.ab.hicarerun.network.NetworkResponseListner;
import com.ab.hicarerun.network.models.ActivityModel.ActivityData;
import com.ab.hicarerun.network.models.ActivityModel.AreaActivity;
import com.ab.hicarerun.network.models.ActivityModel.ServiceActivity;
import com.ab.hicarerun.network.models.ChemicalModel.AreaData;
import com.ab.hicarerun.network.models.ChemicalModel.ServiceChemicalData;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ServiceUnitFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ServiceUnitFragment extends BaseFragment implements OnAddActivityClickHandler, FloorBottomSheetFragment.onAreaSelectListener {
    FragmentServiceUnitBinding mFragmentServiceUnitBinding;
    public static final String ARGS_COMBINE_ORDER = "ARGS_COMBINE_ORDER";
    public static final String ARGS_SEQUENCE = "ARGS_SEQUENCE";
    public static final String ARGS_ORDER = "ARGS_ORDER";
    public static final String ARGS_IS_COMBINE = "ARGS_IS_COMBINE";
    RecyclerView.LayoutManager layoutManager;
    ActivityTowerAdapter mAdapter;
    RecycleByActivityAdapter mActivityAdapter;

    private String combinedOrderId = "";
    private int sequenceNo = 0;
    private boolean isCombineTask = false;
    private String orderId = "";
    private String floor = "";
    List<AreaActivity> subItems = null;
    List<ServiceActivity> mActivityList = null;
    private List<String> mFloorList;


    public ServiceUnitFragment() {
        // Required empty public constructor
    }

    public static ServiceUnitFragment newInstance(boolean isCombinedTask, String combinedOrderId, int sequenceNo, String orderId) {
        ServiceUnitFragment fragment = new ServiceUnitFragment();
        Bundle args = new Bundle();
        args.putString(ARGS_COMBINE_ORDER, combinedOrderId);
        args.putString(ARGS_ORDER, orderId);
        args.putBoolean(ARGS_IS_COMBINE, isCombinedTask);
        args.putInt(ARGS_SEQUENCE, sequenceNo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isCombineTask = getArguments().getBoolean(ARGS_COMBINE_ORDER, false);
            orderId = getArguments().getString(ARGS_ORDER);
            combinedOrderId = getArguments().getString(ARGS_COMBINE_ORDER);
            sequenceNo = getArguments().getInt(ARGS_SEQUENCE, 0);
            combinedOrderId = getArguments().getString(ARGS_ORDER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFragmentServiceUnitBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_service_unit, container, false);
        return mFragmentServiceUnitBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFragmentServiceUnitBinding.recycleTower.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        mFragmentServiceUnitBinding.recycleTower.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mAdapter = new ActivityTowerAdapter(getActivity());
        mFragmentServiceUnitBinding.recycleTower.setAdapter(mAdapter);

        mFragmentServiceUnitBinding.recycleArea.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        mFragmentServiceUnitBinding.recycleArea.setLayoutManager(layoutManager);
        mActivityAdapter = new RecycleByActivityAdapter(getActivity());
        mFragmentServiceUnitBinding.recycleArea.setAdapter(mActivityAdapter);
        mActivityAdapter.setOnItemClickHandler(this);
        getServiceByActivity(floor);
        mActivityAdapter.notifyDataSetChanged();


        mFragmentServiceUnitBinding.cardSheet.setOnClickListener(view1 -> {
            try {
                FloorBottomSheetFragment bottomSheetFragment = new FloorBottomSheetFragment(mFloorList);
                bottomSheetFragment.setListener(this);
                bottomSheetFragment.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), bottomSheetFragment.getTag());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void getServiceByActivity(String flr) {
        try {
            NetworkCallController controller = new NetworkCallController(this);
            controller.setListner(new NetworkResponseListner<List<ActivityData>>() {
                @Override
                public void onResponse(int requestCode, List<ActivityData> items) {
                    if (items != null && items.size() > 0) {
                        mFragmentServiceUnitBinding.lnrData.setVisibility(View.VISIBLE);
                        mFragmentServiceUnitBinding.txtNoData.setVisibility(View.GONE);
                        mActivityList = new ArrayList<>();
                        mAdapter.setData(items);
                        mAdapter.notifyDataSetChanged();
                        if (flr.equals("")) {
                            floor = items.get(0).getFloorList().get(0);
                            mFragmentServiceUnitBinding.txtArea.setText("Floor " + floor);
                        } else {
                            floor = flr;
                            mFragmentServiceUnitBinding.txtArea.setText("Floor " + floor);
                        }
                        mActivityAdapter.addData(items.get(0).getServiceActivity());
                        mActivityAdapter.notifyDataSetChanged();
                        mActivityList = mAdapter.getItem(0).getServiceActivity();
                        mFloorList = mAdapter.getItem(0).getFloorList();


                        mAdapter.setOnItemClickHandler(position -> {
                            mActivityList = new ArrayList<>();
                            mFloorList = new ArrayList<>();
                            mActivityList = mAdapter.getItem(position).getServiceActivity();
                            mFloorList = mAdapter.getItem(position).getFloorList();
                            mActivityAdapter.addData(mActivityList);
                            mActivityAdapter.notifyDataSetChanged();
                        });

                    } else {
                        mFragmentServiceUnitBinding.lnrData.setVisibility(View.GONE);
                        mFragmentServiceUnitBinding.txtNoData.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(int requestCode) {
                    mFragmentServiceUnitBinding.lnrData.setVisibility(View.GONE);
                    mFragmentServiceUnitBinding.txtNoData.setVisibility(View.VISIBLE);
                }
            });
            if (isCombineTask) {
                controller.getServiceActivityChemical(combinedOrderId, sequenceNo, "", true);
            } else {
                controller.getServiceActivityChemical(orderId, sequenceNo, "", true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFloorSelect(String floor, int position) {
        try {
            subItems = new ArrayList<>();
            mFragmentServiceUnitBinding.txtArea.setText("Floor " + floor);
            this.floor = floor;
//            try {
//                floor = mFloorList.get(position);
//                for (int i = 0; i < mAreaList.size(); i++) {
//                    AreaData mOnSiteArea = new AreaData();
//                    if (floor.equals(mAreaList.get(position).getFloorNo())) {
//                        mOnSiteArea.setActivity(mAreaList.get(i).getActivity());
//                        mOnSiteArea.setActivityId(mAreaList.get(i).getActivityId());
//                        mOnSiteArea.setAreaId(mAreaList.get(i).getAreaId());
//                        mOnSiteArea.setAreaName(mAreaList.get(i).getAreaName());
//                        mOnSiteArea.setFloorNo(mAreaList.get(i).getFloorNo());
//                        mOnSiteArea.setServices(mAreaList.get(i).getServices());
//
//                        subItems.add(mOnSiteArea);
//                    }
//                }
//
//                if (subItems != null && subItems.size() > 0) {
//                    mAreaAdapter.addData(subItems);
//                    mAreaAdapter.notifyDataSetChanged();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAddActivityClick(int position) {

    }

    @Override
    public void onNotDoneClick(int position) {

    }

    @Override
    public void onItemClick(int position) {

    }
}