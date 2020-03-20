package com.ab.hicarerun.fragments;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.hicarerun.BaseApplication;
import com.ab.hicarerun.BaseFragment;
import com.ab.hicarerun.R;
import com.ab.hicarerun.activities.VoucherActivity;
import com.ab.hicarerun.databinding.FragmentVoucherBinding;
import com.ab.hicarerun.handler.UserVoucherClickHandler;
import com.ab.hicarerun.network.NetworkCallController;
import com.ab.hicarerun.network.NetworkResponseListner;
import com.ab.hicarerun.network.models.LoginResponse;
import com.ab.hicarerun.network.models.voucher.VoucherRequest;
import com.ab.hicarerun.network.models.voucher.VoucherResponse;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.squareup.picasso.Picasso;
import com.waynell.library.DropAnimationView;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import io.realm.RealmResults;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class VoucherFragment extends BaseFragment implements UserVoucherClickHandler {
    FragmentVoucherBinding mFragmentVoucherBinding;
    private String shareText = "";
    private Uri imgBanner = null;
    private static final int VOUCHER_REQUEST = 1000;

    public VoucherFragment() {
        // Required empty public constructor
    }


    public static VoucherFragment newInstance() {
        Bundle args = new Bundle();
        VoucherFragment fragment = new VoucherFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFragmentVoucherBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_voucher, container, false);
//        getActivity().setTitle("Referral Code");
        mFragmentVoucherBinding.setHandler(this);
        if (getActivity() != null) {
            LinearLayout toolbar = getActivity().findViewById(R.id.toolbar);
            toolbar.setVisibility(View.GONE);
            LinearLayout custom_toolbar = getActivity().findViewById(R.id.customToolbar);
            custom_toolbar.setVisibility(View.VISIBLE);
            TextView tool = getActivity().findViewById(R.id.txtTool);
            tool.setText("Referral Code");
        }
        return mFragmentVoucherBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getVoucherCode();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mFragmentVoucherBinding.dropAnimationView.setDrawables(R.drawable.ic_rupee,
                    R.drawable.ic_rupee_reward);
            mFragmentVoucherBinding.dropAnimationView.startAnimation();
        }
    }

    private void getVoucherCode() {
        try {
            if ((VoucherActivity) getActivity() != null) {
                RealmResults<LoginResponse> LoginRealmModels =
                        BaseApplication.getRealm().where(LoginResponse.class).findAll();
                if (LoginRealmModels != null && LoginRealmModels.size() > 0) {
                    String userId = LoginRealmModels.get(0).getUserID();
                    NetworkCallController controller = new NetworkCallController(this);
                    controller.setListner(new NetworkResponseListner() {
                        @Override
                        public void onResponse(int requestCode, Object data) {
                            VoucherResponse response = (VoucherResponse) data;
                            Picasso.get().load(response.getImageUrl()).into(mFragmentVoucherBinding.banner);

//                        Hey! Let your home become Pest Free like mine! Use my referral code [CODE] to get 10%+10% off on all services from HiCare!
                            String ReferText = "Hey! Let your home become Pest Free like mine! Use my referral code " + "_*" + response.getReferralCode()
                                    + "*_" + " to get 10%+10% off on all services from HiCare! Call us at 8828333888 or visit https://hicare.in/";
                            shareText = String.valueOf(Html.fromHtml(ReferText));
                            imgBanner = Uri.parse(response.getImageUrl());
                            mFragmentVoucherBinding.txtTitle.setText(Html.fromHtml(response.getTitle()));
                            mFragmentVoucherBinding.txtDescription.setText(Html.fromHtml(response.getDescription()));
                            mFragmentVoucherBinding.txtTitle.setTypeface(Typeface.DEFAULT_BOLD, Typeface.NORMAL);
                            mFragmentVoucherBinding.voucherCode.setTypeface(Typeface.DEFAULT_BOLD, Typeface.NORMAL);
                            mFragmentVoucherBinding.voucherCode.setText(response.getReferralCode());
                        }

                        @Override
                        public void onFailure(int requestCode) {

                        }
                    });
                    controller.getVoucherCode(VOUCHER_REQUEST, userId);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onWhatsAppClicked(View view) {
        openWhatsApp(shareText, imgBanner);
    }

    @Override
    public void onFacebookClicked(View view) {
        openFacebook(shareText, imgBanner);
    }

    @Override
    public void onInstagramClicked(View view) {
        openInstagram(shareText, imgBanner);
    }


    @Override
    public void onGooglePlusClicked(View view) {
        openGooglePlus(shareText, imgBanner);
    }


    @Override
    public void onMessengerClicked(View view) {
        openMessenger(shareText, imgBanner);
    }

    @Override
    public void onShareClicked(View view) {
        getShare(shareText, imgBanner);
    }


    private void openWhatsApp(String shareText, Uri imgBanner) {


        try {
            Bitmap bitmap = getBitmapFromView(mFragmentVoucherBinding.banner);
            File file = new File(getActivity().getExternalCacheDir(), "hicare_offer.png");
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true, false);
            Intent shareIntent = new Intent();
            shareIntent.setPackage("com.whatsapp");
            shareIntent.setAction(Intent.ACTION_SEND);

            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            Uri photoURI = FileProvider.getUriForFile(getActivity(), getActivity().getApplicationContext().getPackageName() + ".provider", file);
            shareIntent.putExtra(Intent.EXTRA_STREAM, photoURI);
            shareIntent.setType("image/*");
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(shareIntent, "Share Referral code via WhatsApp"));

        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), "WhatsApp have not been installed...", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {

            e.printStackTrace();
        }


    }

    private Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        } else {
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        return returnedBitmap;
    }

    private void openFacebook(String shareText, Uri imgBanner) {
        try {
            Bitmap bitmap = getBitmapFromView(mFragmentVoucherBinding.banner);
            File file = new File(getActivity().getExternalCacheDir(), "hicare_offer.png");
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true, false);
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_TEXT, shareText);
            Uri photoURI = FileProvider.getUriForFile(getActivity(), getActivity().getApplicationContext().getPackageName() + ".provider", file);
            share.putExtra(Intent.EXTRA_STREAM, photoURI);
            share.setType("image/*");
            share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            share.setPackage("com.facebook.katana"); //Facebook App package
            startActivity(Intent.createChooser(share, "Share Referral code via Facebook"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), "Facebook have not been installed...", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openInstagram(String shareText, Uri imgBanner) {
        try {
            Bitmap bitmap = getBitmapFromView(mFragmentVoucherBinding.banner);
            File file = new File(getActivity().getExternalCacheDir(), "hicare_offer.png");
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true, false);
            Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TITLE, shareText);
            Uri photoURI = FileProvider.getUriForFile(getActivity(), getActivity().getApplicationContext().getPackageName() + ".provider", file);
            shareIntent.putExtra(Intent.EXTRA_STREAM, photoURI);
            shareIntent.setType("image/*");
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            shareIntent.setPackage("com.instagram.android");
            startActivity(Intent.createChooser(shareIntent, "Share Referral code via Instagram"));

        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), "Instagram have not been installed...", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getShare(String shareText, Uri imgBanner) {
        try {
            Bitmap bitmap = getBitmapFromView(mFragmentVoucherBinding.banner);
            File file = new File(getActivity().getExternalCacheDir(), "hicare_offer.png");
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true, false);
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareText);
            Uri photoURI = FileProvider.getUriForFile(getActivity(), getActivity().getApplicationContext().getPackageName() + ".provider", file);
            sharingIntent.putExtra(Intent.EXTRA_STREAM, photoURI);
            sharingIntent.setType("image/*");
            sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(sharingIntent, "Share Referral code via..."));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void openGooglePlus(String shareText, Uri imgBanner) {
        try {
            Bitmap bitmap = getBitmapFromView(mFragmentVoucherBinding.banner);
            File file = new File(getActivity().getExternalCacheDir(), "hicare_offer.png");
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true, false);
            Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TITLE, shareText);
            Uri photoURI = FileProvider.getUriForFile(getActivity(), getActivity().getApplicationContext().getPackageName() + ".provider", file);
            shareIntent.putExtra(Intent.EXTRA_STREAM, photoURI);
            shareIntent.setType("image/*");
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            shareIntent.setPackage("com.google.android.apps.plus");
            startActivity(shareIntent);

        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), "Google Plus have not been installed...", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void openMessenger(String shareText, Uri imgBanner) {
        try {
            Bitmap bitmap = getBitmapFromView(mFragmentVoucherBinding.banner);
            File file = new File(getActivity().getExternalCacheDir(), "hicare_offer.png");
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true, false);
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            Uri photoURI = FileProvider.getUriForFile(getActivity(), getActivity().getApplicationContext().getPackageName() + ".provider", file);
            sendIntent.putExtra(Intent.EXTRA_STREAM, photoURI);
            sendIntent.setType("image/*");
            sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            sendIntent.setPackage("com.facebook.orca");
            startActivity(sendIntent);

        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), "Messenger have not been installed...", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
