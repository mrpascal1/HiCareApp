package com.ab.hicarerun.fragments;


import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.ab.hicarerun.BaseApplication;
import com.ab.hicarerun.BaseFragment;
import com.ab.hicarerun.R;
import com.ab.hicarerun.activities.TechnicianSeniorActivity;
import com.ab.hicarerun.databinding.FragmentTechChemicalCountBinding;
import com.ab.hicarerun.network.NetworkCallController;
import com.ab.hicarerun.network.NetworkResponseListner;
import com.ab.hicarerun.network.models.ChemicalCountModel.ChemicalCount;
import com.ab.hicarerun.network.models.ChemicalCountModel.ChemicalCountResponse;
import com.ab.hicarerun.network.models.LoginResponse;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class TechChemicalCountFragment extends BaseFragment {
    FragmentTechChemicalCountBinding mFragmentTechChemicalCountBinding;
    private static final int GET_COUNT = 1000;
    private List<String> headerList = null;
    TableRow tableRow = null;

    public TechChemicalCountFragment() {
        // Required empty public constructor
    }

    public static TechChemicalCountFragment newInstance() {
        Bundle args = new Bundle();
        TechChemicalCountFragment fragment = new TechChemicalCountFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle oldInstanceState)
    {
        super.onSaveInstanceState(oldInstanceState);
        oldInstanceState.clear();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFragmentTechChemicalCountBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_tech_chemical_count, container, false);
        getActivity().setTitle("Today's Job Count");
        return mFragmentTechChemicalCountBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mFragmentTechChemicalCountBinding.swipeRefreshLayout.setOnRefreshListener(
                this::getChemicalsCount);
        mFragmentTechChemicalCountBinding.swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_dark, android.R.color.holo_blue_light,
                android.R.color.holo_red_dark, android.R.color.holo_red_light,
                android.R.color.holo_green_dark, android.R.color.holo_green_light,
                android.R.color.holo_red_dark, android.R.color.holo_red_light);
        getChemicalsCount();
        mFragmentTechChemicalCountBinding.swipeRefreshLayout.setRefreshing(true);
    }

    private void getChemicalsCount() {
        try {
            if (getActivity() != null) {
                RealmResults<LoginResponse> LoginRealmModels =
                        BaseApplication.getRealm().where(LoginResponse.class).findAll();
                if (LoginRealmModels != null && LoginRealmModels.size() > 0) {
                    String userId = LoginRealmModels.get(0).getUserID();
                    NetworkCallController contoller = new NetworkCallController(this);
                    contoller.setListner(new NetworkResponseListner() {
                        @Override
                        public void onResponse(int requestCode, Object data) {
                            mFragmentTechChemicalCountBinding.swipeRefreshLayout.setRefreshing(false);
                            ChemicalCount response = (ChemicalCount) data;
                            if (response != null) {
                                headerList = new ArrayList<>();
                                headerList = response.getHeader();
                                addHeader(headerList);
                                addData(response.getData());
                            }
                        }

                        @Override
                        public void onFailure(int requestCode) {
                            mFragmentTechChemicalCountBinding.swipeRefreshLayout.setRefreshing(false);
                        }
                    });
                    contoller.getTechnicianJobSummary(GET_COUNT, userId);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private TextView getTextView(int id, String title, int color, int typeface, int bgColor) {
        TextView tv = new TextView(getActivity());
        tv.setId(id);
        tv.setText(title);
        tv.setTextColor(color);
        tv.setPadding(10, 10, 10, 10);
        tv.setGravity(Gravity.CENTER);
        tv.setTypeface(tv.getTypeface(), typeface);
        tv.setBackgroundColor(bgColor);
        tv.setLayoutParams(getLayoutParams());
        return tv;
    }


    @NonNull
    private TableRow.LayoutParams getLayoutParams() {
        TableRow.LayoutParams params = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        params.setMargins(2, 0, 0, 2);
        return params;
    }

    @NonNull
    private TableLayout.LayoutParams getTblLayoutParams() {
        return new TableLayout.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT);
    }

    private void addHeader(List<String> headerList) {
        try {
            TableLayout tl = getActivity().findViewById(R.id.table);
            tl.removeAllViews();
            tableRow = new TableRow(getActivity());
            tableRow.setLayoutParams(getLayoutParams());
            for (int i = 0; i < headerList.size(); i++) {
                String header = headerList.get(i).replace("_", " ");
                tableRow.addView(getTextView(0, header, Color.WHITE, Typeface.NORMAL, ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)));
            }
            tl.addView(tableRow, getTblLayoutParams());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void addData(List<Object> data) {
        try {
            TableLayout tl = getActivity().findViewById(R.id.table);

            for (int i = 0; i < data.size(); i++) {
                tableRow = new TableRow(getActivity());
                tableRow.setLayoutParams(getLayoutParams());
                Object someObject = data.get(i);
                LinkedTreeMap<Object, Object> t = (LinkedTreeMap) someObject;
                for (Map.Entry<Object, Object> entry : t.entrySet()) {
                    Object val = entry.getValue();
                    if (val != null) {
                        tableRow.addView(getTextView(i, val.toString(), Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(getActivity(), R.color.smoke_gray)));
                    } else {
                        tableRow.addView(getTextView(i, "0", Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(getActivity(), R.color.smoke_gray)));
                    }
                }
                tl.addView(tableRow, getTblLayoutParams());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
