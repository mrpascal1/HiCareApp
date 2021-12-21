package com.ab.hicarerun.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.ViewCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ab.hicarerun.BaseFragment;
import com.ab.hicarerun.R;
import com.ab.hicarerun.activities.BarcodeVerificatonActivity;
import com.ab.hicarerun.adapter.AddActivityAdapter;
import com.ab.hicarerun.adapter.AddChemicalActivityAdapter;
import com.ab.hicarerun.adapter.ChemicalAreaParentAdapter;
import com.ab.hicarerun.adapter.ChemicalRecycleAdapter;
import com.ab.hicarerun.adapter.ChemicalTowerAdapter;
import com.ab.hicarerun.databinding.FragmentChemicalActualBinding;
import com.ab.hicarerun.databinding.FragmentChemicalStandardBinding;
import com.ab.hicarerun.handler.OnAddChemicalActivity;
import com.ab.hicarerun.handler.OnSaveEventHandler;
import com.ab.hicarerun.handler.OnSelectChemicalClickHandler;
import com.ab.hicarerun.handler.OnSelectServiceClickHandler;
import com.ab.hicarerun.network.NetworkCallController;
import com.ab.hicarerun.network.NetworkResponseListner;
import com.ab.hicarerun.network.models.ChemicalModel.ActivityData;
import com.ab.hicarerun.network.models.ChemicalModel.Chemicals;
import com.ab.hicarerun.network.models.ChemicalModel.ServiceChemicalData;
import com.ab.hicarerun.network.models.ChemicalModel.ServiceData;
import com.ab.hicarerun.network.models.ChemicalModel.TowerData;
import com.ab.hicarerun.network.models.GeneralModel.GeneralData;
import com.ab.hicarerun.network.models.OnSiteModel.ActivityDetail;
import com.ab.hicarerun.network.models.TaskModel.TaskChemicalList;
import com.ab.hicarerun.utils.AppUtils;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChemicalStandardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChemicalStandardFragment extends BaseFragment implements NetworkResponseListner<List<Chemicals>> {
    FragmentChemicalStandardBinding mFragmentChemicalInfoBinding;
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
    RealmResults<GeneralData> mGeneralRealmData = null;
    private String status = "";
    private String ActualStatus = "";
    private boolean isChemicalChecked = false;
    //    private Tasks model;
    private String taskId = "";
    private String combinedTaskId = "";
    private boolean isCombinedTask = false;
    private boolean showStandardChemicals = false;

    public ChemicalStandardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onSaveInstanceState(Bundle oldInstanceState) {
        super.onSaveInstanceState(oldInstanceState);
        oldInstanceState.clear();
    }

    public static ChemicalStandardFragment newInstance(String taskId, String combinedTaskId, boolean isCombinedTasks) {
        Bundle args = new Bundle();
        args.putString(ARGS_TASKS, taskId);
        args.putString(ARGS_COMBINED_ID, combinedTaskId);
        args.putBoolean(ARGS_COMBINED_TASKS, isCombinedTasks);
        ChemicalStandardFragment fragment = new ChemicalStandardFragment();
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
        mFragmentChemicalInfoBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_chemical_standard, container, false);
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
//        if (isCombinedTask) {
//            mFragmentChemicalInfoBinding.txtType.setVisibility(View.VISIBLE);
//        } else {
//            mFragmentChemicalInfoBinding.txtType.setVisibility(View.GONE);
//        }
        mFragmentChemicalInfoBinding.txtType.setVisibility(View.GONE);
        mFragmentChemicalInfoBinding.txtActual.setVisibility(View.GONE);
        mFragmentChemicalInfoBinding.txtUnit.setVisibility(View.GONE);

        if (mGeneralRealmData != null && mGeneralRealmData.size() > 0) {
            showStandardChemicals = mGeneralRealmData.get(0).getShow_Standard_Chemicals();
        }
//        if (showStandardChemicals) {
//            mFragmentChemicalInfoBinding.txtStandard.setVisibility(View.VISIBLE);
//        } else {
//            mFragmentChemicalInfoBinding.txtStandard.setVisibility(View.GONE);
//        }
        mAdapter = new ChemicalRecycleAdapter(getActivity(), isCombinedTask, showStandardChemicals, "Standard", (position, charSeq) -> {


        });



        mFragmentChemicalInfoBinding.recycleView.setAdapter(mAdapter);

        mFragmentChemicalInfoBinding.btnRodentScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BarcodeVerificatonActivity.class);
                intent.putExtra("barcodeType", "");
//                intent.putExtra(ServiceRenewalActivity.ARGS_TASKS, taskId);
                startActivity(intent);
            }
        });

        setChemicals();
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(int requestCode) {
    }

}
