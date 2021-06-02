package com.ab.hicarerun.utils.notifications;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ab.hicarerun.BaseActivity;
import com.ab.hicarerun.R;
import com.ab.hicarerun.activities.HomeActivity;
import com.ab.hicarerun.databinding.ActivityTransparentPopupBinding;
import com.ab.hicarerun.fragments.IncentiveFragment;
import com.ab.hicarerun.utils.AppUtils;
import com.ab.hicarerun.utils.GifDrawableImageViewTarget;
import com.bumptech.glide.Glide;

import java.util.Objects;

public class ActivityTransparentPopup extends BaseActivity {
    private ActivityTransparentPopupBinding mActivityTransparentPopupBinding;

    protected int mIntPopupType = 0;
    protected String mStrPopupHeader = "";
    protected String mStrPopupDescription = "";

    public static final String INTENT_CONSTANT_ARG_POPUP_TYPE = "popup_type";
    public static final String INTENT_CONSTANT_ARG_POPUP_HEADER = "popup_header";
    public static final String INTENT_CONSTANT_ARG_POPUP_DESCRIPTION = "popup_description";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityTransparentPopupBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_transparent_popup);
//        moveTaskToBack(true);
        mIntPopupType = getIntent().getIntExtra(INTENT_CONSTANT_ARG_POPUP_TYPE, 0);
        mStrPopupHeader = getIntent().getStringExtra(INTENT_CONSTANT_ARG_POPUP_HEADER);
        mStrPopupDescription = getIntent().getStringExtra(INTENT_CONSTANT_ARG_POPUP_DESCRIPTION);
        setPaymentNotification();
    }

    private void setPaymentNotification() {
        Glide.with(this)
                .load(R.drawable.tick_marks)
                .into(new GifDrawableImageViewTarget(mActivityTransparentPopupBinding.fragPopupLIvImageCenter, 1));
        mActivityTransparentPopupBinding.activityNotifdetailsLRowBtnAction.setOnClickListener(view -> {
            finish();
//            startActivity(new Intent(ActivityTransparentPopup.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        });
        Log.v("description", mStrPopupHeader);
        Log.v("description", mStrPopupDescription);
        String[] arrayString = mStrPopupDescription.split("\\|", -1);
        String custName = arrayString[0];
        String[] custArray = custName.split(": ", -1);
        String orderNo = arrayString[1];
        String[] orderArray = orderNo.split(": ", -1);
        String Service = arrayString[2];
        String[] serviceArray = Service.split(": ", -1);
        String Amount = arrayString[3];
        String[] amountArray = Amount.split(": ", -1);
        String paymentMode = arrayString[4];
        String[] modeArray = paymentMode.split(": ", -1);

        mActivityTransparentPopupBinding.txtName.setText(custArray[1]);
        mActivityTransparentPopupBinding.txtOrder.setText(orderArray[1]);
        mActivityTransparentPopupBinding.txtService.setText(serviceArray[1]);
        mActivityTransparentPopupBinding.txtAmount.setText(amountArray[1]);
        mActivityTransparentPopupBinding.txtType.setText(modeArray[1]);
        mActivityTransparentPopupBinding.txtDate.setText(AppUtils.formattedCurrentDate());
        mActivityTransparentPopupBinding.txtPaymentHeader.setTypeface(mActivityTransparentPopupBinding.txtPaymentHeader.getTypeface(), Typeface.BOLD);
        mActivityTransparentPopupBinding.txtPaymentStmt.setText(Html.fromHtml("Amount recieved " + "<b>\u20B9</b>" + "<b>" + amountArray[1] + "</b>"));
    }
}
