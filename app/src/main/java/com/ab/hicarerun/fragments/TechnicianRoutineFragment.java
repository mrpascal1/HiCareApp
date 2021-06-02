package com.ab.hicarerun.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.ab.hicarerun.BaseApplication;
import com.ab.hicarerun.BaseFragment;
import com.ab.hicarerun.R;
import com.ab.hicarerun.activities.TechnicianRoutineActivity;
import com.ab.hicarerun.activities.TechnicianSeniorActivity;
import com.ab.hicarerun.adapter.TechnicianRoutineAdapter;
import com.ab.hicarerun.databinding.FragmentTechnicianRoutineBinding;
import com.ab.hicarerun.handler.OnListItemClickHandler;
import com.ab.hicarerun.network.NetworkCallController;
import com.ab.hicarerun.network.NetworkResponseListner;
import com.ab.hicarerun.network.models.LoginResponse;
import com.ab.hicarerun.network.models.TechnicianRoutineModel.TechnicianData;
import com.ab.hicarerun.utils.MyDividerItemDecoration;

import java.util.List;

import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class TechnicianRoutineFragment extends BaseFragment  {
    FragmentTechnicianRoutineBinding mFragmentTechnicianRoutineBinding;
    private TechnicianRoutineAdapter mAdapter;
    private static final int TECH_REQUEST = 1000;
    RoutineCheckFragment.RoutineDialogInterface mCallback= () -> getTechnicianRoutine();

    public TechnicianRoutineFragment() {
        // Required empty public constructor
    }

    public static TechnicianRoutineFragment newInstance() {
        Bundle args = new Bundle();
        TechnicianRoutineFragment fragment = new TechnicianRoutineFragment();
        fragment.setArguments(args);
        return fragment;
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFragmentTechnicianRoutineBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_technician_routine, container, false);
        getActivity().setTitle("Daily Check-Up");

        return mFragmentTechnicianRoutineBinding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new TechnicianRoutineAdapter(getActivity());
        mFragmentTechnicianRoutineBinding.recycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mFragmentTechnicianRoutineBinding.recycleView.addItemDecoration(new MyDividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL, 0));
        mFragmentTechnicianRoutineBinding.recycleView.setHasFixedSize(true);
        mFragmentTechnicianRoutineBinding.recycleView.setClipToPadding(false);
        mFragmentTechnicianRoutineBinding.recycleView.setAdapter(mAdapter);
        getTechnicianRoutine();
    }


    private void getTechnicianRoutine() {
        try {
            if ((TechnicianRoutineActivity) getActivity() != null) {
                RealmResults<LoginResponse> LoginRealmModels =
                        BaseApplication.getRealm().where(LoginResponse.class).findAll();
                if (LoginRealmModels != null && LoginRealmModels.size() > 0) {

                    String resourceId = LoginRealmModels.get(0).getUserID();
                    NetworkCallController controller = new NetworkCallController(this);
                    controller.setListner(new NetworkResponseListner<List<TechnicianData>>() {
                        @Override
                        public void onResponse(int requestCode, List<TechnicianData> items) {
                            if (items != null && items.size() > 0) {
                                mAdapter.addData(items);
                                mAdapter.notifyDataSetChanged();
                                mAdapter.setOnItemClickHandler(position -> {
                                    if (items.get(position).getRoutineChecklistSubmitted()) {
                                        RoutineSubmittedFragment dialog = RoutineSubmittedFragment.newInstance(items.get(position).getTechnicianId());
                                        dialog.show(getActivity().getSupportFragmentManager(), "check_up");

                                    } else {
                                        RoutineCheckFragment dialog = RoutineCheckFragment.newInstance(items.get(position).getTechnicianId(), mCallback);
                                        dialog.show(getActivity().getSupportFragmentManager(), "check_up");
                                    }

                                });
                            }
                        }

                        @Override
                        public void onFailure(int requestCode) {

                        }
                    });
                    controller.getTechnicianRoutineChecklist(TECH_REQUEST, resourceId); }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
