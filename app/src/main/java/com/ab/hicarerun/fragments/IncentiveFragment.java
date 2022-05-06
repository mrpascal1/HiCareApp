package com.ab.hicarerun.fragments;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ab.hicarerun.BaseApplication;
import com.ab.hicarerun.BaseFragment;
import com.ab.hicarerun.R;
import com.ab.hicarerun.adapter.IncentiveDetailsAdapter;
import com.ab.hicarerun.adapter.IncentiveMatrixAdapter;
import com.ab.hicarerun.databinding.FragmentIncentiveBinding;
import com.ab.hicarerun.network.NetworkCallController;
import com.ab.hicarerun.network.NetworkResponseListner;
import com.ab.hicarerun.network.models.incentivemodel.IncentiveCriteriaList;
import com.ab.hicarerun.network.models.incentivemodel.IncentiveData;
import com.ab.hicarerun.network.models.LoginResponse;
import com.ab.hicarerun.network.models.incentivemodel.IncentiveMonthList;
import com.ab.hicarerun.network.models.profilemodel.Profile;
import com.ab.hicarerun.utils.AppUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class IncentiveFragment extends BaseFragment {

    private FragmentIncentiveBinding mFragmentIncentiveBinding;
    private static final int REQ_PROFILE = 1000;
    private static final int REQ_INCENTIVE = 2000;
    IncentiveMatrixAdapter mAdapter;
    IncentiveDetailsAdapter mDetailAdapter;
    RecyclerView.LayoutManager layoutManager;
    private List<IncentiveCriteriaList> criteriaLists = null;
    private LinearLayout lnrInfo;

    public IncentiveFragment() {
        // Required empty public constructor
    }


    public static IncentiveFragment newInstance() {
        Bundle args = new Bundle();
        IncentiveFragment fragment = new IncentiveFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentIncentiveBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_incentive, container, false);
        getActivity().setTitle(getResources().getString(R.string.incentives_in));

        if (getActivity() != null) {
            LinearLayout toolbar = getActivity().findViewById(R.id.toolbar);
            toolbar.setVisibility(View.GONE);
            LinearLayout custom_toolbar = getActivity().findViewById(R.id.customToolbar);
            custom_toolbar.setVisibility(View.VISIBLE);
            TextView tool = getActivity().findViewById(R.id.txtTool);
            lnrInfo = getActivity().findViewById(R.id.lnrInfo);
            lnrInfo.setVisibility(View.VISIBLE);
            tool.setText(getResources().getString(R.string.incentives_in));
        }
        return mFragmentIncentiveBinding.getRoot();
    }

    private void setSpinner(List<IncentiveMonthList> monthList){
        ArrayList<String> modifiedList = new ArrayList<>();
        for (IncentiveMonthList i : monthList){
            if (!modifiedList.contains(i.getMonthAndYear())){
                if (i.getSelected()){
                    modifiedList.add(0, i.getMonthAndYear());
                }else {
                    modifiedList.add(i.getMonthAndYear());
                }
            }
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(requireContext(), R.layout.spinner_layout_new, modifiedList){
            @Override
            public boolean isEnabled(int position) {
                return position == 0;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0){
                    tv.setTextColor(Color.GRAY);
                }else{
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        arrayAdapter.setDropDownViewResource(R.layout.spinner_popup);
        mFragmentIncentiveBinding.spnMonth.setAdapter(arrayAdapter);
    }

    private void showMatrixDialog() {

        try {
            LayoutInflater li = LayoutInflater.from(getActivity());
            View promptsView = li.inflate(R.layout.layout_matrix_dialog, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
            alertDialogBuilder.setView(promptsView);
            final AlertDialog alertDialog = alertDialogBuilder.create();
            final AppCompatButton btnOk =
                    (AppCompatButton) promptsView.findViewById(R.id.btnOk);
            final RecyclerView recyclerView =
                    (RecyclerView) promptsView.findViewById(R.id.recycleView);


            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
            mAdapter = new IncentiveMatrixAdapter();
            recyclerView.setAdapter(mAdapter);

            if (criteriaLists != null) {
                mAdapter.addData(criteriaLists);
                mAdapter.notifyDataSetChanged();
            }

            btnOk.setOnClickListener(v -> alertDialog.dismiss());

            alertDialog.show();
            alertDialog.setCancelable(false);
            Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            alertDialog.setCanceledOnTouchOutside(false);

        } catch (Exception e) {
            RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
            if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                AppUtils.sendErrorLogs(e.getMessage(), getClass().getSimpleName(), "onAddReferralClicked", lineNo, userName, DeviceName);
            }
        }

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mFragmentIncentiveBinding.recycleView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        mFragmentIncentiveBinding.recycleView.setLayoutManager(layoutManager);
        mAdapter = new IncentiveMatrixAdapter();
        mDetailAdapter = new IncentiveDetailsAdapter(getActivity());
        mFragmentIncentiveBinding.recycleView.setAdapter(mDetailAdapter);
        lnrInfo.setOnClickListener(v -> showMatrixDialog());
        getIncentiveDetails("");
        getTechDeails();
        mFragmentIncentiveBinding.spnMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getIncentiveDetails(mFragmentIncentiveBinding.spnMonth.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void getTechDeails() {
        try {
            if (getActivity() != null) {
                RealmResults<LoginResponse> LoginRealmModels =
                        BaseApplication.getRealm().where(LoginResponse.class).findAll();
                assert LoginRealmModels.get(0) != null;
                String userId = LoginRealmModels.get(0).getUserID();
                if (LoginRealmModels != null && LoginRealmModels.size() > 0) {
                    NetworkCallController controller = new NetworkCallController(this);
                    controller.setListner(new NetworkResponseListner() {
                        @Override
                        public void onResponse(int requestCode, Object data) {
                            Profile response = (Profile) data;
                            String TechName = response.getFirstName();

                            mFragmentIncentiveBinding.txtUser.setText(TechName);
                            if (response.getProfilePic() != null) {
                                String base64 = response.getProfilePic();
                                byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
                                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                if (base64.length() > 0) {
                                    mFragmentIncentiveBinding.imgUser.setImageBitmap(decodedByte);
                                }
                            }

                        }

                        @Override
                        public void onFailure(int requestCode) {

                        }
                    });
                    controller.getTechnicianProfile(REQ_PROFILE, userId);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getIncentiveDetails(String selectedMonthAndYear) {
        try {
            if (getActivity() != null) {
                RealmResults<LoginResponse> LoginRealmModels =
                        BaseApplication.getRealm().where(LoginResponse.class).findAll();
                assert LoginRealmModels.get(0) != null;
                String userId = LoginRealmModels.get(0).getUserID();
                if (LoginRealmModels != null && LoginRealmModels.size() > 0) {
                    NetworkCallController controller = new NetworkCallController(this);
                    controller.setListner(new NetworkResponseListner() {
                        @Override
                        public void onResponse(int requestCode, Object data) {
                            IncentiveData response = (IncentiveData) data;
                            mFragmentIncentiveBinding.txtBadgePts.setText(String.valueOf(response.getTotalPoints()));
                            mFragmentIncentiveBinding.txtOutOff.setText(String.valueOf(response.getTotalPoints() + "/" + "200"));
                            mFragmentIncentiveBinding.txtPoints.setText(String.valueOf(response.getTotalPoints()) + " Pts.");
                            mFragmentIncentiveBinding.txtBadgePts.setTypeface(Typeface.DEFAULT_BOLD, Typeface.BOLD);
                            mFragmentIncentiveBinding.txtOutOff.setTypeface(Typeface.DEFAULT_BOLD, Typeface.BOLD);
                            mFragmentIncentiveBinding.progressBar.setMax(130);
                            mFragmentIncentiveBinding.progressBar.setProgress(response.getTotalPoints());
                            mFragmentIncentiveBinding.txtIncentive.setText(String.valueOf(response.getTotalIncentiveAmount() + " Rs."));
                            if (response.getMonth() != null) {
                                mFragmentIncentiveBinding.txtDate.setVisibility(View.VISIBLE);
                                mFragmentIncentiveBinding.txtDate.setText(getResources().getString(R.string.incentive_as_on) + " " + response.getMonth() + " " + response.getYear());
                            } else {
                                mFragmentIncentiveBinding.txtDate.setVisibility(View.GONE);
                            }
                            if (response.getIncentiveDetailList() != null) {
                                mDetailAdapter.setData(response.getIncentiveDetailList());
                                mDetailAdapter.notifyDataSetChanged();
                            }
                            criteriaLists = new ArrayList<>();
                            criteriaLists = response.getIncentiveCriteriaList();
                            setSpinner(response.getIncentiveMonthList());
                        }

                        @Override
                        public void onFailure(int requestCode) {

                        }
                    });
                    controller.getResourceIncentive(REQ_INCENTIVE, userId, selectedMonthAndYear);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
