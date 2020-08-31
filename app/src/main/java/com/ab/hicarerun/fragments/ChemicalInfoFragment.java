package com.ab.hicarerun.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ab.hicarerun.BaseFragment;
import com.ab.hicarerun.R;
import com.ab.hicarerun.adapter.ChemicalRecycleAdapter;
import com.ab.hicarerun.databinding.FragmentChemicalInfoBinding;
import com.ab.hicarerun.handler.OnSaveEventHandler;
import com.ab.hicarerun.network.NetworkCallController;
import com.ab.hicarerun.network.NetworkResponseListner;
import com.ab.hicarerun.network.models.ChemicalModel.Chemicals;
import com.ab.hicarerun.network.models.GeneralModel.GeneralData;
import com.ab.hicarerun.network.models.TaskModel.TaskChemicalList;
import com.ab.hicarerun.network.models.TaskModel.Tasks;
import com.ab.hicarerun.utils.AppUtils;
import com.ab.hicarerun.utils.GPSUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChemicalInfoFragment extends BaseFragment implements NetworkResponseListner<List<Chemicals>> {
    FragmentChemicalInfoBinding mFragmentChemicalInfoBinding;
    public static final String ARGS_TASKS = "ARGS_TASKS";
    public static final String ARGS_COMBINED_TASKS = "ARGS_COMBINED_TASKS";
    public static final String ARGS_COMBINED_ID = "ARGS_COMBINED_ID";
    private static final int CHEMICAL_REQ = 1000;
    private Boolean isVerified = false;
    RecyclerView.LayoutManager layoutManager;
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
    //    private Tasks model;
    private String taskId = "";
    private String combinedTaskId = "";
    private boolean isCombinedTask = false;
    private boolean showStandardChemicals = false;

    public ChemicalInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onSaveInstanceState(Bundle oldInstanceState)
    {
        super.onSaveInstanceState(oldInstanceState);
        oldInstanceState.clear();
    }

    public static ChemicalInfoFragment newInstance(String taskId, String combinedTaskId, boolean isCombinedTasks) {
        Bundle args = new Bundle();
        args.putString(ARGS_TASKS, taskId);
        args.putString(ARGS_COMBINED_ID, combinedTaskId);
        args.putBoolean(ARGS_COMBINED_TASKS, isCombinedTasks);
        ChemicalInfoFragment fragment = new ChemicalInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            taskId = getArguments().getString(ARGS_TASKS);
            combinedTaskId = getArguments().getString(ARGS_COMBINED_ID);
            isCombinedTask = getArguments().getBoolean(ARGS_COMBINED_TASKS);
        }
    }


    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnSaveEventHandler) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement FragmentToActivity");
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFragmentChemicalInfoBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_chemical_info, container, false);
        return mFragmentChemicalInfoBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mGeneralRealmData =
                getRealm().where(GeneralData.class).findAll();
        mFragmentChemicalInfoBinding.recycleView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        mFragmentChemicalInfoBinding.recycleView.setLayoutManager(layoutManager);
        ViewCompat.setNestedScrollingEnabled(mFragmentChemicalInfoBinding.recycleView, false);
        if (isCombinedTask) {
            mFragmentChemicalInfoBinding.txtType.setVisibility(View.VISIBLE);
        } else {
            mFragmentChemicalInfoBinding.txtType.setVisibility(View.GONE);
        }
        if (map != null) {
            map.clear();
        }
        if (mGeneralRealmData != null && mGeneralRealmData.size() > 0) {
            showStandardChemicals = mGeneralRealmData.get(0).getShow_Standard_Chemicals();
        }
        if (showStandardChemicals) {
            mFragmentChemicalInfoBinding.txtStandard.setVisibility(View.VISIBLE);
        } else {
            mFragmentChemicalInfoBinding.txtStandard.setVisibility(View.GONE);
        }
        mAdapter = new ChemicalRecycleAdapter(getActivity(), isCombinedTask, showStandardChemicals, (position, charSeq) -> {
            try {
                if (charSeq != null && map != null) {
                    map.put(position, charSeq);
                    Log.i("MAP_VALUE", Objects.requireNonNull(map.get(position)));
                    if (map.containsValue("")) {
                        map.remove(position);
                    } else {
//                        Toast.makeText(getActivity(), String.valueOf(map.get(position)), Toast.LENGTH_SHORT).show();
                        ChemList.clear();
                        for (int i = 0; i < map.size(); i++) {
                            TaskChemicalList ChemModel = new TaskChemicalList();
                            ChemModel.setId(mAdapter.getItem(i).getId());
                            ChemModel.setCWFProductName(mAdapter.getItem(i).getName());
                            ChemModel.setConsumption(mAdapter.getItem(i).getConsumption());
                            ChemModel.setStandard(mAdapter.getItem(i).getStandard());
                            ChemModel.setOrignal(mAdapter.getItem(i).getOrignal());
                            if (mAdapter.getItem(i).getOrignal() != null) {
                                if (mAdapter.getItem(i).getOrignal().equals(map.get(i))) {
                                    ChemModel.setChemicalChanged(false);
                                } else {
                                    ChemModel.setChemicalChanged(true);
                                }
                            } else {
                                ChemModel.setChemicalChanged(true);
                            }

                            ChemModel.setActual(map.get(i));
                            ChemList.add(ChemModel);
                        }
                        mCallback.chemReqList(ChemList);
                    }
                    mCallback.isActualChemicalChanged(isChemicalChanged(ChemList));
                    for (int i = 0; i < mAdapter.getItemCount(); i++)
                        getValidation(i);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        });


        mFragmentChemicalInfoBinding.checkChemVerified.setOnCheckedChangeListener((buttonView, isChecked) -> {
            try {
                isChemicalChecked = isChecked;
                for (int i = 0; i < mAdapter.getItemCount(); i++)
                    getValidation(i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        mFragmentChemicalInfoBinding.recycleView.setAdapter(mAdapter);
        setChemicals();
    }

    private static boolean isChemicalChanged(List<TaskChemicalList> arraylist) {
        for (TaskChemicalList list : arraylist) {
            if (list.getChemicalChanged()) {
                return true;
            }
        }
        return false;
    }

    private void setChemicals() {
        try {
            if (mGeneralRealmData != null && mGeneralRealmData.size() > 0) {
                assert mGeneralRealmData.get(0) != null;
                isVerified = mGeneralRealmData.get(0).getAutoSubmitChemicals();
                assert mGeneralRealmData.get(0) != null;
                ActualStatus = mGeneralRealmData.get(0).getSchedulingStatus();
                NetworkCallController controller = new NetworkCallController(this);
                controller.setListner(this);
                if (isCombinedTask) {
                    controller.getMSTChemicals(CHEMICAL_REQ, combinedTaskId);
                } else {
                    controller.getChemicals(CHEMICAL_REQ, taskId);
                }

                callAfterResponse();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onResponse(int requestCode, List<Chemicals> items) {
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(int requestCode) {
    }

    private void getValidation(int position) {
        try {
            if (mGeneralRealmData != null && mGeneralRealmData.size() > 0) {
                assert mGeneralRealmData.get(0) != null;
                isVerified = mGeneralRealmData.get(0).getAutoSubmitChemicals();
                Log.i("chemicalcount", String.valueOf(mAdapter.getItemCount()));
                Log.i("mapcount", String.valueOf(map.size()));
                Log.i("mapcount", String.valueOf(map.keySet() + " , " + map.values()));
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

    private void callAfterResponse() {
        try {
            if (isVerified || ActualStatus.equals("Completed") || ActualStatus.equals("Incomplete")) {
                mFragmentChemicalInfoBinding.checkChemVerified.setEnabled(false);
                mFragmentChemicalInfoBinding.checkChemVerified.setChecked(true);
            } else {
                mFragmentChemicalInfoBinding.relChemicals.setVisibility(View.VISIBLE);
                mFragmentChemicalInfoBinding.checkChemVerified.setEnabled(true);
                mFragmentChemicalInfoBinding.checkChemVerified.setChecked(false);
            }
            for (int i = 0; i < mAdapter.getItemCount(); i++) {
                getValidation(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
