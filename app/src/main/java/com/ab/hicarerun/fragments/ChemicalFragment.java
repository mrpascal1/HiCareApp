package com.ab.hicarerun.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ab.hicarerun.BaseFragment;
import com.ab.hicarerun.R;
import com.ab.hicarerun.adapter.ChemicalRecycleAdapter;
import com.ab.hicarerun.databinding.FragmentChemicalBinding;
import com.ab.hicarerun.handler.OnSaveEventHandler;
import com.ab.hicarerun.network.NetworkCallController;
import com.ab.hicarerun.network.NetworkResponseListner;
import com.ab.hicarerun.network.models.ChemicalModel.Chemicals;
import com.ab.hicarerun.network.models.GeneralModel.GeneralData;
import com.ab.hicarerun.network.models.TaskModel.TaskChemicalList;
import com.ab.hicarerun.utils.AppUtils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.realm.RealmResults;


public class ChemicalFragment extends BaseFragment implements NetworkResponseListner {
    FragmentChemicalBinding mFragmentChemicalBinding;
    private static final String ARG_ISVERIFIED = "ARG_ISVERIFIED";
    private static final String ARG_TASK = "ARG_TASK";
    private static final int CHEMICAL_REQ = 1000;
    private Boolean isVerified = false;
    RecyclerView.LayoutManager layoutManager;
    private String taskId = "";
    private Integer pageNumber = 1;
    ProgressDialog mProgressBar;
    ChemicalRecycleAdapter mAdapter;
    private OnSaveEventHandler mCallback;
    private HashMap<Integer, String> map = new HashMap<>();
    private List<TaskChemicalList> ChemList = new ArrayList<>();
    RealmResults<GeneralData> mGeneralRealmData = null;
    private String status = "";
    private String ActualStatus = "";
    private boolean isChemicalChecked = false;


    public ChemicalFragment() {
        // Required empty public constructor
    }

    public static ChemicalFragment newInstance(String taskId) {
        Bundle args = new Bundle();
        args.putString(ARG_TASK, taskId);
        ChemicalFragment fragment = new ChemicalFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onResume() {
        super.onResume();
        try {
            AppUtils.statusCheck(getActivity());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnSaveEventHandler) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement FragmentToActivity");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            taskId = getArguments().getString(ARG_TASK);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentChemicalBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_chemical, container, false);
        mProgressBar = new ProgressDialog(getActivity());
        return mFragmentChemicalBinding.getRoot();
    }

    @Override

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFragmentChemicalBinding.recycleView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        mFragmentChemicalBinding.recycleView.setLayoutManager(layoutManager);
        mGeneralRealmData =
                getRealm().where(GeneralData.class).findAll();
        mAdapter = new ChemicalRecycleAdapter(getActivity(),false, new ChemicalRecycleAdapter.OnEditTextChanged() {
            @Override
            public void onTextChanged(int position, String charSeq) {
                try {
                    if (charSeq != null && map != null) {
                        map.put(position, charSeq);
                        Log.i("MAP_VALUE", map.get(position));
                        if (map.containsValue("")) {
                            map.remove(position);
                        } else {
                            ChemList.clear();
                            for (int i = 0; i < map.size(); i++) {
                                TaskChemicalList ChemModel = new TaskChemicalList();
                                ChemModel.setId(mAdapter.getItem(i).getId());
                                ChemModel.setCWFProductName(mAdapter.getItem(i).getName());
                                ChemModel.setConsumption(mAdapter.getItem(i).getConsumption());
                                ChemModel.setStandard(mAdapter.getItem(i).getStandard());
                                ChemModel.setActual(map.get(i));
                                ChemList.add(ChemModel);
                            }
                            mCallback.chemReqList(ChemList);

                        }
                        for (int i = 0; i < mAdapter.getItemCount(); i++)
                            getValidation(i);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
//                    RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
//                    if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
//                        String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
//                        String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
//                        String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
//                        AppUtils.sendErrorLogs(e.getMessage(), getClass().getSimpleName(), "chemicalFragmentonViewCreated", lineNo, userName, DeviceName);
//                    }
                }

            }
        });


        mFragmentChemicalBinding.checkChemVerified.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    isChemicalChecked = isChecked;
                    for (int i = 0; i < mAdapter.getItemCount(); i++)
                        getValidation(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        mFragmentChemicalBinding.recycleView.setAdapter(mAdapter);
        setChemicals();
    }

    private void setChemicals() {
        try {
//            mGeneralRealmData =
//                    getRealm().where(GeneralData.class).findAll();

            if (mGeneralRealmData != null && mGeneralRealmData.size() > 0) {
                isVerified = mGeneralRealmData.get(0).getAutoSubmitChemicals();
                ActualStatus = mGeneralRealmData.get(0).getSchedulingStatus();
                NetworkCallController controller = new NetworkCallController(this);
                controller.setListner(this);
                controller.getChemicals(CHEMICAL_REQ, taskId);

                callAfterResponse();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onResponse(int requestCode, Object data) {
        List<Chemicals> items = (List<Chemicals>) data;
        if (items != null) {

            if (pageNumber == 1 && items.size() > 0) {
                mAdapter.setData(items);
                mAdapter.notifyDataSetChanged();
            } else if (items.size() > 0) {
                mAdapter.addData(items);
                mAdapter.notifyDataSetChanged();
            } else {
                pageNumber--;
            }
            callAfterResponse();
        }
    }

    @Override
    public void onFailure(int requestCode) {
    }

    public void getValidation(int position) {
        try {
//            mGeneralRealmData =
//                    getRealm().where(GeneralData.class).findAll();
            if (mGeneralRealmData != null && mGeneralRealmData.size() > 0) {
                isVerified = mGeneralRealmData.get(0).getAutoSubmitChemicals();
                Log.i("chemicalcount", String.valueOf(mAdapter.getItemCount()));
                Log.i("mapcount", String.valueOf(map.size()));

                if (!isVerified) {
                    if (map.size() == mAdapter.getItemCount()) {
                        mCallback.isChemicalChanged(false);
                    } else {
                        mCallback.isChemicalChanged(true);
                    }

                    if (isChemicalChecked) {
                        mCallback.isChemicalVerified(false);
                    } else {
                        mCallback.isChemicalVerified(true);
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void callAfterResponse() {
        try {
            if (isVerified || ActualStatus.equals("Completed") || ActualStatus.equals("Incomplete")) {
                mFragmentChemicalBinding.checkChemVerified.setEnabled(false);
                mFragmentChemicalBinding.checkChemVerified.setChecked(true);
            } else {
                mFragmentChemicalBinding.relChemicals.setVisibility(View.VISIBLE);
                mFragmentChemicalBinding.checkChemVerified.setEnabled(true);
                mFragmentChemicalBinding.checkChemVerified.setChecked(false);
            }

            for (int i = 0; i < mAdapter.getItemCount(); i++) {
                getValidation(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
