package com.ab.hicarerun.fragments;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.ab.hicarerun.BaseApplication;
import com.ab.hicarerun.BaseFragment;
import com.ab.hicarerun.R;
import com.ab.hicarerun.activities.HomeActivity;
import com.ab.hicarerun.activities.TechIdActivity;
import com.ab.hicarerun.activities.TechnicianSeniorActivity;
import com.ab.hicarerun.databinding.FragmentTechIdBinding;
import com.ab.hicarerun.network.NetworkCallController;
import com.ab.hicarerun.network.NetworkResponseListner;
import com.ab.hicarerun.network.models.LoginResponse;
import com.ab.hicarerun.network.models.ProfileModel.Profile;
import com.ab.hicarerun.network.models.ProfileModel.TechnicianProfileDetails;
import com.ab.hicarerun.utils.AppUtils;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;

import io.realm.RealmResults;

public class TechIdFragment extends BaseFragment {

    FragmentTechIdBinding mFragmentTechIdBinding;
    private static final int GET_TECHNICIAN_PROFILE = 1000;

    public TechIdFragment() {
        // Required empty public constructor
    }

    public static TechIdFragment newInstance() {
        Bundle args = new Bundle();
        TechIdFragment fragment = new TechIdFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFragmentTechIdBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_tech_id, container, false);
        getActivity().setTitle(getString(R.string.id_card_bottom));
        if ((HomeActivity) getActivity() != null) {
            LinearLayout toolbar = getActivity().findViewById(R.id.toolbar);
            toolbar.setVisibility(View.GONE);
            LinearLayout custom_toolbar = getActivity().findViewById(R.id.customToolbar);
            custom_toolbar.setVisibility(View.VISIBLE);
            TextView tool = getActivity().findViewById(R.id.txtTool);
            tool.setText(getString(R.string.id_card_bottom));
        }
        return mFragmentTechIdBinding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getTechnicianProfile();
    }

    private void getTechnicianProfile() {
        try {

            if ( getActivity() != null) {
                RealmResults<LoginResponse> LoginRealmModels =
                        BaseApplication.getRealm().where(LoginResponse.class).findAll();
                String userId = LoginRealmModels.get(0).getUserID();
                if (LoginRealmModels != null && LoginRealmModels.size() > 0) {
                    NetworkCallController controller = new NetworkCallController(this);
                    controller.setListner(new NetworkResponseListner() {
                        @Override
                        public void onResponse(int requestCode, Object data) {
                            Profile response = (Profile) data;
                            String TechName = response.getFirstName();
                            if (response.getEmployeeCode() != null) {
                                String EmpCode = response.getEmployeeCode();
                                mFragmentTechIdBinding.txtCode.setText(EmpCode);
                                mFragmentTechIdBinding.lnrAdded.setVisibility(View.VISIBLE);
                            } else {
                                mFragmentTechIdBinding.lnrAdded.setVisibility(View.GONE);
                            }
                            mFragmentTechIdBinding.txtTechName.setText(TechName);
                            if(response.getProfilePic()!= null){
                                String base64 = response.getProfilePic();
                                byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
                                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                if (base64.length() > 0) {
                                    mFragmentTechIdBinding.imgTech.setImageBitmap(decodedByte);
                                }
                            }

                        }

                        @Override
                        public void onFailure(int requestCode) {

                        }
                    });
                    controller.getTechnicianProfile(GET_TECHNICIAN_PROFILE, userId);
                }
            }
        }catch (Exception e){
            RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
            if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                String userName = "TECHNICIAN NAME : "+mLoginRealmModels.get(0).getUserName();
                String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                String DeviceName = "DEVICE_NAME : "+ Build.DEVICE+", DEVICE_VERSION : "+ Build.VERSION.SDK_INT;
                AppUtils.sendErrorLogs(e.getMessage(), getClass().getSimpleName(), "getTechnicianProfile", lineNo,userName,DeviceName);
            }
        }
    }

}
