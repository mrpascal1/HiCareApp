package com.ab.hicarerun.fragments;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.telephony.TelephonyManager;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.ab.hicarerun.BaseFragment;
import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.FragmentLoginBinding;
import com.ab.hicarerun.handler.UserLoginClickHandler;
import com.ab.hicarerun.network.NetworkCallController;
import com.ab.hicarerun.network.NetworkResponseListner;
import com.ab.hicarerun.network.models.LoginResponse;
import com.ab.hicarerun.utils.AppUtils;
import com.ab.hicarerun.utils.notifications.OneSIgnalHelper;
import com.ab.hicarerun.viewmodel.UserLoginViewModel;

import org.jetbrains.annotations.NotNull;


public class LoginFragment extends BaseFragment implements UserLoginClickHandler {
    FragmentLoginBinding mFragmentLoginBinding;
    private static final int LOGIN_REQUEST = 1000;
    private static final int REQUEST_READ_PHONE_STATE = 1;
    private static final int PERMISSION_REQUEST_CODE = 1000;
    private Boolean isGetInside = false;
    private UserLoginTask mAuthTask = null;
    private String profilePic = "";

    public LoginFragment() {
        // Required empty public constructor
    }


    public static LoginFragment newInstance() {
        Bundle args = new Bundle();
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFragmentLoginBinding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);
        mFragmentLoginBinding.setModel(new UserLoginViewModel());

        mFragmentLoginBinding.setHandler(this);

        mFragmentLoginBinding.checkbox.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                // show password
                mFragmentLoginBinding.edtpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                // hide password
                mFragmentLoginBinding.edtpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });

        return mFragmentLoginBinding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onLoginClicked(View view) {

        if (validateSignUp(mFragmentLoginBinding.getModel())) {

            attemptLogin();

        }
    }

    private boolean validateSignUp(UserLoginViewModel userLoginViewModel) {
        if (userLoginViewModel.username.trim().equalsIgnoreCase("")) {
            mFragmentLoginBinding.edtusername.setError(getString(R.string.invalid_userid));
            return false;
        } else if (userLoginViewModel.password.trim().equalsIgnoreCase("")) {
            mFragmentLoginBinding.edtpassword.setError(getString(R.string.invalid_password));
            return false;
        } else {
            return true;
        }
    }


    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }


        String imei = "", device_info = "";

        TelephonyManager telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        device_info = Build.MODEL;
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        assert telephonyManager != null;
        imei = telephonyManager.getDeviceId();
        PackageInfo pinfo = null;
        try {
            pinfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        assert pinfo != null;
        String versionName = pinfo.versionName;
        OneSIgnalHelper oneSIgnalHelper = new OneSIgnalHelper(getActivity());
        String mStrPlayerId = oneSIgnalHelper.getmStrUserID();

        try {
            UserLoginViewModel model = mFragmentLoginBinding.getModel();
            NetworkCallController controller = new NetworkCallController(this);
            controller.setListner(new NetworkResponseListner<LoginResponse>() {
                @Override
                public void onResponse(int requestCode, LoginResponse response) {

                    // delete all previous record
                    getRealm().beginTransaction();
                    getRealm().deleteAll();
                    getRealm().commitTransaction();
                    profilePic = response.getUserProfilePic();

                    // add new record
                    getRealm().beginTransaction();
                    getRealm().copyToRealmOrUpdate(response);
                    getRealm().commitTransaction();
                    if (isVisible()) {
                        isGetInside = true;
                        new UserLoginTask(isGetInside).execute((Void) null);
                    }
                }

                @Override
                public void onFailure(int requestCode) {
                }

            });

            controller.login(LOGIN_REQUEST, model.getUsername(),
                    model.getPassword(), versionName, imei, device_info, mStrPlayerId);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private Boolean isPass = false;
        private UserLoginViewModel model = mFragmentLoginBinding.getModel();

        UserLoginTask(Boolean isGetInside) {
            isPass = isGetInside;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                if (isPass) {
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;

            if (success) {
                if (profilePic.trim().length() == 0) {
                    replaceFragment(FaceRecognizationFragment.newInstance(false, model.getUsername(), ""), "LoginFragment-FaceRecognizationFragment");
                } else {
                    AppUtils.getHandShakeCall(model.getUsername(), getActivity());
                }
            } else {
                mFragmentLoginBinding.edtpassword.setError("Invalid Password!");
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }

    }


}






