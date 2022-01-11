package com.ab.hicarerun.fragments;

import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ab.hicarerun.BaseApplication;
import com.ab.hicarerun.BaseFragment;
import com.ab.hicarerun.R;
import com.ab.hicarerun.activities.KycActivity;
import com.ab.hicarerun.adapter.KycDocumentAdapter;
import com.ab.hicarerun.databinding.FragmentUserKycBinding;
import com.ab.hicarerun.handler.OnKycClickHandler;
import com.ab.hicarerun.network.NetworkCallController;
import com.ab.hicarerun.network.NetworkResponseListner;
import com.ab.hicarerun.network.models.kycmodel.DocumentData;
import com.ab.hicarerun.network.models.kycmodel.KycTypesData;
import com.ab.hicarerun.network.models.LoginResponse;
import com.ab.hicarerun.utils.LocaleHelper;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserKycFragment extends BaseFragment implements OnKycClickHandler {
    private static final int PERMISSION_WRITE = 0;
    FragmentUserKycBinding mFragmentUserKycBinding;
    private static final int KYC_REQUEST = 1000;
    private static final int KYC_TYPE_REQUEST = 2000;
    private KycDocumentAdapter mAdapter;
//    UploadKycDialogFragment.KycDialogInterface mCallback = () ->{
//    }
    UploadKycDialogFragment.KycDialogInterface mCallback = () -> {
        getKYCDocuments();
        getKYCTypes();
    };
    private ProgressDialog mProgressDialog;

    public UserKycFragment() {
        // Required empty public constructor
    }

    public static UserKycFragment newInstance() {
        Bundle args = new Bundle();
        UserKycFragment fragment = new UserKycFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFragmentUserKycBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_kyc, container, false);
        getActivity().setTitle("KYC Verification");
        mProgressDialog = new ProgressDialog(getActivity());
        return mFragmentUserKycBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFragmentUserKycBinding.lnrAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadKycDialogFragment dialog = UploadKycDialogFragment.newInstance(mCallback);
                dialog.show(getActivity().getSupportFragmentManager(), "kyc");
            }
        });
        mAdapter = new KycDocumentAdapter(getActivity());
        mAdapter.setOnKycClickHandler(this);
        mFragmentUserKycBinding.recycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mFragmentUserKycBinding.recycleView.setHasFixedSize(true);
        mFragmentUserKycBinding.recycleView.setClipToPadding(false);
        mFragmentUserKycBinding.recycleView.setAdapter(mAdapter);
        getKYCDocuments();
        getKYCTypes();
    }

    private void getKYCTypes() {
        try {
            if (getActivity() != null) {
                RealmResults<LoginResponse> LoginRealmModels =
                        BaseApplication.getRealm().where(LoginResponse.class).findAll();
                if (LoginRealmModels != null && LoginRealmModels.size() > 0) {
                    String resourceId = LoginRealmModels.get(0).getUserID();
                    NetworkCallController controller = new NetworkCallController();
                    controller.setListner(new NetworkResponseListner<List<KycTypesData>>() {
                        @Override
                        public void onResponse(int requestCode, List<KycTypesData> items) {
                            try {
                            if(items!=null && items.size()>0){
                                mFragmentUserKycBinding.lnrAdd.setVisibility(View.VISIBLE);
                            }else {
                                mFragmentUserKycBinding.lnrAdd.setVisibility(View.GONE);
                            }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(int requestCode) {

                        }
                    });
                    controller.getKYCTypes(KYC_TYPE_REQUEST, resourceId, LocaleHelper.getLanguage(getActivity()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getKYCDocuments() {
        try {
            if ((KycActivity) getActivity() != null) {
                RealmResults<LoginResponse> LoginRealmModels =
                        BaseApplication.getRealm().where(LoginResponse.class).findAll();
                if (LoginRealmModels != null && LoginRealmModels.size() > 0) {
                    String resourceId = LoginRealmModels.get(0).getUserID();
                    NetworkCallController controller = new NetworkCallController(this);
                    controller.setListner(new NetworkResponseListner<List<DocumentData>>() {

                        @Override
                        public void onResponse(int requestCode, List<DocumentData> items) {
                            if (items != null && items.size() > 0) {
                                mAdapter.addData(items);
                                mAdapter.notifyDataSetChanged();
                                mFragmentUserKycBinding.emptyTask.setVisibility(View.GONE);
                                mFragmentUserKycBinding.recycleView.setVisibility(View.VISIBLE);


                            } else {
                                mFragmentUserKycBinding.emptyTask.setVisibility(View.VISIBLE);
                                mFragmentUserKycBinding.recycleView.setVisibility(View.GONE);
                            }

                        }

                        @Override
                        public void onFailure(int requestCode) {

                        }
                    });
                    controller.getKYCDocuments(KYC_REQUEST, resourceId, LocaleHelper.getLanguage(getActivity()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onViewImageClicked(int position) {
        replaceFragment(KycGalleryFragment.newInstance(mAdapter.getItem(position).getDocument_Url()), "UserKycFragment - KycGalleryFragment");
    }

    @Override
    public void onDownloadClicked(int position) {
        if (checkPermission()) {
            new Downloading().execute(mAdapter.getItem(position).getDocument_Url());
        }
    }

    private class Downloading extends AsyncTask<String, Integer, String> {

        @Override
        public void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.setMessage("Please wait");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected String doInBackground(String... url) {
            File mydir = new File(Environment.getExternalStorageDirectory() + "/HiCare");
            if (!mydir.exists()) {
                mydir.mkdirs();
            }

            DownloadManager manager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
            Uri downloadUri = Uri.parse(url[0]);
            DownloadManager.Request request = new DownloadManager.Request(downloadUri);

            SimpleDateFormat dateFormat = new SimpleDateFormat("mmddyyyyhhmmss");
            String date = dateFormat.format(new Date());

            request.setAllowedNetworkTypes(
                    DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false)
                    .setTitle("Downloading")
                    .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES,"HiCare.jpeg");

            manager.enqueue(request);
            return mydir.getAbsolutePath() + File.separator + date + ".jpeg";
        }

        @Override
        public void onPostExecute(String s) {
            super.onPostExecute(s);
            mProgressDialog.dismiss();
            Toast.makeText(getActivity(), "Image Saved", Toast.LENGTH_SHORT).show();
        }
    }

    //runtime storage permission
    public boolean checkPermission() {
        int READ_EXTERNAL_PERMISSION = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        if ((READ_EXTERNAL_PERMISSION != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_WRITE);
            return false;
        }
        return true;
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_WRITE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //do somethings
        }
    }
}

