package com.ab.hicarerun.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.ab.hicarerun.BaseApplication;
import com.ab.hicarerun.BuildConfig;
import com.ab.hicarerun.R;
import com.ab.hicarerun.activities.KycActivity;
import com.ab.hicarerun.databinding.FragmentUploadKycDialogBinding;
import com.ab.hicarerun.network.NetworkCallController;
import com.ab.hicarerun.network.NetworkResponseListner;
import com.ab.hicarerun.network.models.CheckListModel.UploadCheckListData;
import com.ab.hicarerun.network.models.CheckListModel.UploadCheckListRequest;
import com.ab.hicarerun.network.models.KycModel.KycTypesData;
import com.ab.hicarerun.network.models.KycModel.SaveKycRequest;
import com.ab.hicarerun.network.models.KycModel.SaveKycResponse;
import com.ab.hicarerun.network.models.LoginResponse;
import com.ab.hicarerun.utils.AppUtils;
import com.ab.hicarerun.utils.LocaleHelper;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;
import io.realm.RealmResults;

import static android.app.Activity.RESULT_OK;
import static com.ab.hicarerun.BaseApplication.getRealm;

/**
 * A simple {@link Fragment} subclass.
 */
public class UploadKycDialogFragment extends DialogFragment {
    FragmentUploadKycDialogBinding mFragmentUploadKycDialogBinding;
    private static final int KYC_REQUEST = 1000;
    private static final int KYC_TYPE_REQUEST = 2000;
    private static final int UPLOAD_REQ = 3000;
    static final int REQUEST_TAKE_PHOTO = 1;
    private String selectedImagePath = "";
    private String imgUrl = "";
    private String cardNumber = "";
    private String type = "";
    private int typeId = 0;
    private File mPhotoFile;
    private Bitmap bitmap;
    private static KycDialogInterface mCallback;
    private ProgressDialog progress;



    public UploadKycDialogFragment() {
        // Required empty public constructor
    }

    public static UploadKycDialogFragment newInstance(KycDialogInterface mCallbackk) {
        Bundle args = new Bundle();
        UploadKycDialogFragment fragment = new UploadKycDialogFragment();
        mCallback = mCallbackk;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFragmentUploadKycDialogBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_upload_kyc_dialog, container, false);
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        return mFragmentUploadKycDialogBinding.getRoot();
    }

    public void onResume() {
        Window window = getDialog().getWindow();
        Point size = new Point();


        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        window.setLayout((int) (size.x * 0.9), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        super.onResume();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progress = new ProgressDialog(getActivity(), R.style.TransparentProgressDialog);
        progress.setCancelable(false);
        mFragmentUploadKycDialogBinding.btnSave.setOnClickListener(v -> {

            cardNumber = mFragmentUploadKycDialogBinding.edtCardNumber.getText().toString().trim();
            type = mFragmentUploadKycDialogBinding.spnTypes.getSelectedItem().toString();
            saveKYCDocument(cardNumber, type);
        });
        getKYCTypes();
        mFragmentUploadKycDialogBinding.lnrUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestStoragePermission(true);
            }
        });
        mFragmentUploadKycDialogBinding.imageCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgUrl = "";
                mFragmentUploadKycDialogBinding.lnrImage.setVisibility(View.GONE);
                mFragmentUploadKycDialogBinding.lnrUpload.setVisibility(View.VISIBLE);
            }
        });
        mFragmentUploadKycDialogBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK) {
                if (requestCode == REQUEST_TAKE_PHOTO) {
                    try {
                        selectedImagePath = mPhotoFile.getPath();
                        if (selectedImagePath != null || !selectedImagePath.equals("")) {
                            Bitmap bit = new BitmapDrawable(getResources(),
                                    selectedImagePath).getBitmap();
                            int i = (int) (bit.getHeight() * (1024.0 / bit.getWidth()));
                            bitmap = Bitmap.createScaledBitmap(bit, 1024, i, true);
                        }

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);


                        byte[] b = baos.toByteArray();
                        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                        uploadKYCImage(encodedImage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void uploadKYCImage(String base64) {
        try {
            RealmResults<LoginResponse> LoginRealmModels =
                    getRealm().where(LoginResponse.class).findAll();
            if (LoginRealmModels != null && LoginRealmModels.size() > 0) {
                String UserId = LoginRealmModels.get(0).getUserID();
                UploadCheckListRequest request = new UploadCheckListRequest();
                request.setResourceId(UserId);
                request.setFileUrl("");
                request.setFileName("");
                request.setTaskId("");
                request.setFileContent(base64);

                NetworkCallController controller = new NetworkCallController();
                controller.setListner(new NetworkResponseListner<UploadCheckListData>() {
                    @Override
                    public void onResponse(int requestCode, UploadCheckListData response) {

                        imgUrl = response.getFileUrl();
                        Picasso.get().load(response.getFileUrl()).into(mFragmentUploadKycDialogBinding.imgUploadedCheque);
                        mFragmentUploadKycDialogBinding.lnrUpload.setVisibility(View.GONE);
                        mFragmentUploadKycDialogBinding.lnrImage.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFailure(int requestCode) {

                    }
                });
                controller.uploadCheckListAttachment(UPLOAD_REQ, request);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void requestStoragePermission(boolean isCamera) {
        try {
            Dexter.withActivity(getActivity())
                    .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            // check if all permissions are granted
                            if (report.areAllPermissionsGranted()) {
                                if (isCamera) {
                                    dispatchTakePictureIntent();
                                }
                            }
                            // check for permanent denial of any permission
                            if (report.isAnyPermissionPermanentlyDenied()) {
                                // show alert dialog navigating to Settings
                                showSettingsDialog();
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions,
                                                                       PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    })
                    .withErrorListener(
                            error -> Toast.makeText(getActivity(), "Error occurred! ", Toast.LENGTH_SHORT)
                                    .show())
                    .onSameThread()
                    .check();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showSettingsDialog() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Need Permissions");
            builder.setMessage(
                    "This app needs permission to use this feature. You can grant them in app settings.");
            builder.setPositiveButton("GOTO SETTINGS", (dialog, which) -> {
                dialog.cancel();
                openSettings();
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void openSettings() {
        try {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
            intent.setData(uri);
            startActivityForResult(intent, 101);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void dispatchTakePictureIntent() {
        try {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    // Error occurred while creating the File
                }
                if (photoFile != null) {
                    try {
                        Uri photoURI = FileProvider.getUriForFile(getActivity(),
                                BuildConfig.APPLICATION_ID + ".provider",
                                photoFile);
                        mPhotoFile = photoFile;
                        takePictureIntent.putExtra("android.intent.extras.CAMERA_FACING", Camera.CameraInfo.CAMERA_FACING_BACK);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String mFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File mFile = File.createTempFile(mFileName, ".jpg", storageDir);
        return mFile;
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
                                List<String> valueItems = new ArrayList<>();
                                for (int i = 0; i < items.size(); i++) {
                                    valueItems.add(items.get(i).getName());
                                }

                                ArrayAdapter<String> typesAdapter = new ArrayAdapter<>(getActivity(),
                                        R.layout.layout_routine_spinner_adapter, valueItems);
                                typesAdapter.setDropDownViewResource(R.layout.spinner_popup);
                                mFragmentUploadKycDialogBinding.spnTypes.setAdapter(typesAdapter);

                                mFragmentUploadKycDialogBinding.spnTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        typesAdapter.getItem(position);
                                        typeId = items.get(position).getId();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
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

    private void saveKYCDocument(String edtNumber, String spnType) {
        try {
            if (getActivity() != null) {
                RealmResults<LoginResponse> LoginRealmModels =
                        BaseApplication.getRealm().where(LoginResponse.class).findAll();
                if (LoginRealmModels != null && LoginRealmModels.size() > 0) {

                    String resourceId = LoginRealmModels.get(0).getUserID();
                    SaveKycRequest request = new SaveKycRequest();
                    request.setCreatedOn(AppUtils.currentDateTime());
                    request.setDocumentUrl(imgUrl);
                    request.setDocument_No(edtNumber);
                    request.setId(typeId);
                    request.setRecordType(spnType);
                    request.setResourceId(resourceId);
                    if (isValidated(request)) {
                        progress.show();
                        NetworkCallController controller = new NetworkCallController();
                        controller.setListner(new NetworkResponseListner<SaveKycResponse>() {
                            @Override
                            public void onResponse(int requestCode, SaveKycResponse response) {
                                progress.dismiss();
                                dismiss();
                            }

                            @Override
                            public void onFailure(int requestCode) {
                                progress.dismiss();
                            }
                        });
                        controller.saveKYCDocument(KYC_REQUEST, request);
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            progress.dismiss();
        }
    }

    private boolean isValidated(SaveKycRequest request) {
        if (request.getRecordType() == null || request.getRecordType().equals("")) {
            Toasty.error(getActivity(), "Please select KYC type.", Toasty.LENGTH_SHORT).show();
            return false;
        } else if (request.getDocument_No() == null || request.getDocument_No().equals("")) {
            Toasty.error(getActivity(), "Please enter card number.", Toasty.LENGTH_SHORT).show();
            return false;
        } else if (request.getDocumentUrl() == null || request.getDocumentUrl().equals("")) {
            Toasty.error(getActivity(), "Please capture KYC image.", Toasty.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        try {
            mCallback.onDialogDismissed();
        } catch (ClassCastException e) {
            e.getStackTrace();
        }
    }

    public interface KycDialogInterface {
        void onDialogDismissed();
    }

}
