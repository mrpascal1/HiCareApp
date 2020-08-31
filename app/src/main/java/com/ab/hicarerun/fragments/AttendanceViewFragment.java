package com.ab.hicarerun.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ab.hicarerun.BaseApplication;
import com.ab.hicarerun.BaseFragment;
import com.ab.hicarerun.R;
import com.ab.hicarerun.activities.HomeActivity;
import com.ab.hicarerun.databinding.FragmentAttendanceViewBinding;
import com.ab.hicarerun.network.NetworkCallController;
import com.ab.hicarerun.network.NetworkResponseListner;
import com.ab.hicarerun.network.models.AttendanceModel.AttendanceDetail;
import com.ab.hicarerun.network.models.LoginResponse;

import org.jetbrains.annotations.NotNull;

import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class AttendanceViewFragment extends BaseFragment {
    FragmentAttendanceViewBinding mFragmentAttendanceViewBinding;
    private static final int ATTENDANCE_REQ = 1000;

    public AttendanceViewFragment() {
        // Required empty public constructor
    }

    public static AttendanceViewFragment newInstance() {
        Bundle args = new Bundle();
        AttendanceViewFragment fragment = new AttendanceViewFragment();
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
        mFragmentAttendanceViewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_attendance_view, container, false);

        if ((HomeActivity) getActivity() != null) {
            LinearLayout toolbar = getActivity().findViewById(R.id.toolbar);
            toolbar.setVisibility(View.GONE);
            LinearLayout custom_toolbar = getActivity().findViewById(R.id.customToolbar);
            custom_toolbar.setVisibility(View.VISIBLE);
            LinearLayout lnrInfo = getActivity().findViewById(R.id.lnrInfo);
            lnrInfo.setVisibility(View.GONE);
            TextView tool = getActivity().findViewById(R.id.txtTool);
            tool.setText(getResources().getString(R.string.your_attendance));
        }
        return mFragmentAttendanceViewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getResources().getString(R.string.tool_your_attendance));
        getAttendanceDetails();
    }

    private void getAttendanceDetails() {
        RealmResults<LoginResponse> LoginRealmModels =
                BaseApplication.getRealm().where(LoginResponse.class).findAll();
        if (LoginRealmModels != null && LoginRealmModels.size() > 0) {
            String resourceId = LoginRealmModels.get(0).getUserID();
            NetworkCallController controller = new NetworkCallController(this);
            controller.setListner(new NetworkResponseListner() {
                @Override
                public void onResponse(int requestCode, Object response) {
                    AttendanceDetail data = (AttendanceDetail) response;
                    mFragmentAttendanceViewBinding.txtDays.setText(String.valueOf(data.getTotalNoOfDaysPresent())+"/"+String.valueOf(data.getTotalNoOfDays()));
                    mFragmentAttendanceViewBinding.txtPresent.setText(String.valueOf(data.getTotalNoOfDaysPresent()));
                    mFragmentAttendanceViewBinding.txtLate.setText(String.valueOf(data.getTotalDaysLateCome()));
                    mFragmentAttendanceViewBinding.txtAbsent.setText(String.valueOf(data.getTotalNoOfDays() - data.getTotalNoOfDaysPresent()));
                }

                @Override
                public void onFailure(int requestCode) {

                }
            });
            controller.getAttendanceDetail(ATTENDANCE_REQ, resourceId);
        }
    }
}
