package com.ab.hicarerun.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.FragmentUpdateAppBottomBinding;
import com.ab.hicarerun.utils.AppUtils;
import com.ab.hicarerun.utils.DownloadApk;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

public class UpdateAppBottomFragment extends BottomSheetDialogFragment {
    FragmentUpdateAppBottomBinding fragmentUpdateAppBottomBinding;
    private String Apk_Type = "";
    private String Apk_URL = "";
    private String Version = "";
    private Context mContext;

    public UpdateAppBottomFragment(String type, String Url, String version) {
        this.Apk_Type = type;
        this.Apk_URL = Url;
        this.Version = version;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onSaveInstanceState(Bundle oldInstanceState) {
        super.onSaveInstanceState(oldInstanceState);
        oldInstanceState.clear();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        bottomSheetDialog.setOnShowListener(dia -> {
            BottomSheetDialog dialog = (BottomSheetDialog) dia;
            FrameLayout bottomSheet = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            bottomSheet.setBackgroundResource(android.R.color.transparent);
            BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
            BottomSheetBehavior.from(bottomSheet).setSkipCollapsed(false);
            BottomSheetBehavior.from(bottomSheet).setHideable(true);
        });
        return bottomSheetDialog;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        fragmentUpdateAppBottomBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_update_app_bottom, container, false);
        return fragmentUpdateAppBottomBinding.getRoot();    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentUpdateAppBottomBinding.txtVersion.setTypeface(fragmentUpdateAppBottomBinding.txtVersion.getTypeface(), Typeface.BOLD);
        fragmentUpdateAppBottomBinding.txtVersion.setText("V "+Version);
        fragmentUpdateAppBottomBinding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpgradeApp();
            }
        });

    }

    private void UpgradeApp() {
        if (AppUtils.checkConnection(Objects.requireNonNull(getActivity()))) {
            ProgressDialog progress = new ProgressDialog(getActivity());
            if (Apk_Type.equalsIgnoreCase("url")) {
                final String appPackageName = getActivity().getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            } else {
                DownloadApk downloadAndInstall = new DownloadApk();
                progress.setCancelable(false);
                progress.setMessage("Downloading...");
                downloadAndInstall.setContext(getActivity(), progress);
                downloadAndInstall.execute(Apk_URL);
            }

        } else {
            AppUtils.showOkActionAlertBox(getActivity(), "No Internet Found.", (dialogInterface1, i1) -> getActivity().finish());
        }
    }
}