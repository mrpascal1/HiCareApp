package com.ab.hicarerun.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.Camera;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.format.Time;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Chronometer;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.ab.hicarerun.BaseApplication;
import com.ab.hicarerun.BaseFragment;
import com.ab.hicarerun.R;
import com.ab.hicarerun.activities.HomeActivity;
import com.ab.hicarerun.databinding.TaskListAdapterBinding;
import com.ab.hicarerun.fragments.HomeFragment;
import com.ab.hicarerun.handler.OnCallListItemClickHandler;
import com.ab.hicarerun.handler.OnDeleteListItemClickHandler;
import com.ab.hicarerun.handler.OnListItemClickHandler;
import com.ab.hicarerun.network.NetworkCallController;
import com.ab.hicarerun.network.NetworkResponseListner;
import com.ab.hicarerun.network.models.LoginResponse;
import com.ab.hicarerun.network.models.ProfileModel.Profile;
import com.ab.hicarerun.network.models.TaskModel.Tasks;
import com.ab.hicarerun.utils.AppUtils;
import com.ab.hicarerun.utils.MyCountDownTimer;
import com.ab.hicarerun.viewmodel.TaskViewModel;
import com.orhanobut.logger.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import io.realm.RealmResults;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;


public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ViewHolder> {

    private OnListItemClickHandler onItemClickHandler;
    private OnCallListItemClickHandler onCallListItemClickHandler;
    private String street = "";
    private boolean isShown = false;
    private final Context mContext;
    private final HomeFragment mFragment;
    private Activity activity = null;
    private List<TaskViewModel> items = null;
    String currentdate;
    long milli = 0;
    private Handler customHandler = new Handler();
    private long startTime = 0L;
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    Runnable updateTimerThread;

    private int time = 20;
    private Timer timer;
    private String Flat = "";
    private static final int RESOURCE_REQ = 1000;
    private static final int PROFILE_REQ = 2000;
    private Profile profile = null;

    public TaskListAdapter(Activity context, HomeFragment homeFragment) {
        if (items == null) {
            items = new ArrayList<>();
        }
        this.mContext = context;
        this.activity = context;
        this.mFragment = homeFragment;
        currentdate = AppUtils.currentDateTime();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TaskListAdapterBinding mTaskListAdapterBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.task_list_adapter, parent, false);
        return new ViewHolder(mTaskListAdapterBinding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if (items.get(position).getHelperResourceId() != null && !items.get(position).getHelperResourceId().equals("")) {
            try {
                holder.mTaskListAdapterBinding.lnrPartnerPic.setVisibility(View.VISIBLE);
                NetworkCallController controller = new NetworkCallController();
                controller.setListner(new NetworkResponseListner() {
                    @Override
                    public void onResponse(int requestCode, Object response) {
                        String base64 = (String) response;
                        byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        if (base64.length() > 0) {
                            holder.mTaskListAdapterBinding.imgPartner.setImageBitmap(decodedByte);
                        }
                        NetworkCallController controller1 = new NetworkCallController(mFragment);
                        controller1.setListner(new NetworkResponseListner() {
                            @Override
                            public void onResponse(int requestCode, Object data) {
                                Profile response = (Profile) data;
                                profile = response;
                                holder.mTaskListAdapterBinding.txtHelperName.setText(response.getFirstName());
                            }

                            @Override
                            public void onFailure(int requestCode) {

                            }
                        });
                        controller1.getTechnicianProfile(PROFILE_REQ, items.get(position).getHelperResourceId());
                    }

                    @Override
                    public void onFailure(int requestCode) {

                    }
                });
                controller.getResourceProfilePicture(RESOURCE_REQ, items.get(position).getHelperResourceId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            holder.mTaskListAdapterBinding.lnrPartnerPic.setVisibility(View.GONE);
        }

        holder.mTaskListAdapterBinding.txtTime.setText(items.get(position).getTaskAssignmentStartTime() + " - " + items.get(position).getTaskAssignmentEndTime());
        holder.mTaskListAdapterBinding.txtName.setText(items.get(position).getAccountName());
        holder.mTaskListAdapterBinding.status.setText(items.get(position).getStatus());

        int[] attrs = new int[]{R.attr.selectableItemBackground};
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs);
        int backgroundResource = typedArray.getResourceId(0, 0);
        holder.mTaskListAdapterBinding.dispatchTaskAltMobileNo.setBackgroundResource(backgroundResource);
        holder.mTaskListAdapterBinding.dispatchTaskMobileNo.setBackgroundResource(backgroundResource);
        holder.mTaskListAdapterBinding.dispatchTaskPhoneNo.setBackgroundResource(backgroundResource);
        holder.mTaskListAdapterBinding.lnrDetail.setBackgroundResource(backgroundResource);
        holder.mTaskListAdapterBinding.lnrMain.setBackgroundResource(backgroundResource);
        holder.mTaskListAdapterBinding.constraint.setBackgroundResource(backgroundResource);
        holder.mTaskListAdapterBinding.constraint2.setBackgroundResource(backgroundResource);
        holder.mTaskListAdapterBinding.lnrMap.setBackgroundResource(backgroundResource);
        holder.mTaskListAdapterBinding.btnHelpline.setBackgroundResource(backgroundResource);
        if (items.get(position).getCombineTask()) {
            String order = items.get(position).getCombineOrderNumber().replace(",", ", ");
            holder.mTaskListAdapterBinding.txtOrderno.setText(order);
            holder.mTaskListAdapterBinding.lnrMST.setVisibility(View.VISIBLE);
        } else {
            holder.mTaskListAdapterBinding.txtOrderno.setText(items.get(position).getOrderNumber());
            holder.mTaskListAdapterBinding.lnrMST.setVisibility(View.GONE);
        }

        try {
            String mDate = AppUtils.reFormatDateTime(items.get(position).getTaskAssignmentStartDate(), "dd MMM, yyyy");
            holder.mTaskListAdapterBinding.txtDate.setText(mDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.mTaskListAdapterBinding.status.setTypeface(Typeface.DEFAULT_BOLD, Typeface.NORMAL);
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.blink);
        holder.mTaskListAdapterBinding.imgWarning.startAnimation(animation);
        if (items.get(position).getSequenceNumber() == 1 && items.get(position).getAccountType().equals("Individual")) {
            holder.mTaskListAdapterBinding.lnrMain.setBackgroundColor(Color.parseColor("#ffc0cb"));
        } else {
            holder.mTaskListAdapterBinding.lnrMain.setBackgroundColor(Color.parseColor("#ffffff"));
        }

        if (items.get(position).getStatus().equalsIgnoreCase("Completed")) {
            holder.mTaskListAdapterBinding.imgWarning.setVisibility(View.GONE);
            holder.mTaskListAdapterBinding.warning.setVisibility(View.GONE);
//            holder.mTaskListAdapterBinding.status.setTextColor(Color.parseColor("#1E90FF"));
            holder.mTaskListAdapterBinding.lnrStatus.setBackgroundColor(Color.parseColor("#1E90FF"));
        } else if (items.get(position).getStatus().equalsIgnoreCase("Dispatched")) {
//            holder.mTaskListAdapterBinding.status.setTextColor(Color.parseColor("#ff6700"));
            holder.mTaskListAdapterBinding.lnrStatus.setBackgroundColor(Color.parseColor("#ff6700"));
        } else if (items.get(position).getStatus().equalsIgnoreCase("On-Site")) {
            holder.mTaskListAdapterBinding.imgWarning.setVisibility(View.GONE);
            holder.mTaskListAdapterBinding.warning.setVisibility(View.GONE);
//            holder.mTaskListAdapterBinding.status.setTextColor(Color.parseColor("#e1ad01"));
            holder.mTaskListAdapterBinding.lnrStatus.setBackgroundColor(Color.parseColor("#e1ad01"));
        } else if (items.get(position).getStatus().equalsIgnoreCase("Incomplete")) {
            holder.mTaskListAdapterBinding.imgWarning.setVisibility(View.GONE);
            holder.mTaskListAdapterBinding.warning.setVisibility(View.GONE);
//            holder.mTaskListAdapterBinding.status.setTextColor(Color.parseColor("#FF69B4"));
            holder.mTaskListAdapterBinding.lnrStatus.setBackgroundColor(Color.parseColor("#FF69B4"));
        }
        if (items.get(position).getTag() != null && items.get(position).getTag().length() > 0) {
            holder.mTaskListAdapterBinding.lnrTag.setVisibility(View.VISIBLE);
            holder.mTaskListAdapterBinding.txtTag.setText(items.get(position).getTag());
        } else {
            holder.mTaskListAdapterBinding.lnrTag.setVisibility(View.GONE);
        }

        if (items.get(position).getSequenceNumber() != 0) {
            holder.mTaskListAdapterBinding.lnrSequence.setVisibility(View.VISIBLE);
            holder.mTaskListAdapterBinding.txtSequence.setText(String.valueOf(items.get(position).getSequenceNumber()));
        } else {
            holder.mTaskListAdapterBinding.lnrSequence.setVisibility(View.GONE);
        }

        if (items.get(position).getCombineTask()) {
            String service = items.get(position).getCombineTaskType().replace(",", ", ");
            holder.mTaskListAdapterBinding.txtService.setText(service);
            holder.mTaskListAdapterBinding.txtType.setText(service);
        } else {
            holder.mTaskListAdapterBinding.txtService.setText(items.get(position).getServicePlan());
            holder.mTaskListAdapterBinding.txtType.setText(items.get(position).getServiceType());
        }

        if (items.get(position).getStreet() != null) {
            street = items.get(position).getStreet();
        }

        if (items.get(position).getBuildingName() != null && items.get(position).getBuildingName().trim().length() != 0) {
            holder.mTaskListAdapterBinding.lnrAddress.setVisibility(View.VISIBLE);
            if (items.get(position).getWingFlatOrUnitNumber() != null && !items.get(position).getWingFlatOrUnitNumber().equals("")) {
                Flat = items.get(position).getWingFlatOrUnitNumber() + ", ";
            }
            holder.mTaskListAdapterBinding.txtAddress.setText(Flat + items.get(position).getBuildingName() + ", "
                    + items.get(position).getLocality() + ", "
                    + street);
        } else {
            holder.mTaskListAdapterBinding.lnrAddress.setVisibility(View.GONE);
        }

        if (items.get(position).getLandmark() != null && !items.get(position).getLandmark().equals("")) {
            holder.mTaskListAdapterBinding.lnrLandmark.setVisibility(View.VISIBLE);
            holder.mTaskListAdapterBinding.txtLandmark.setText(items.get(position).getLandmark());
        } else {
            holder.mTaskListAdapterBinding.lnrLandmark.setVisibility(View.GONE);
        }
        holder.mTaskListAdapterBinding.txtPostcode.setText(items.get(position).getPostalCode());
        if (items.get(position).getAmount().equals("0")) {
            holder.mTaskListAdapterBinding.lnrAmount.setVisibility(View.GONE);
        } else {
            holder.mTaskListAdapterBinding.lnrAmount.setVisibility(View.VISIBLE);
            holder.mTaskListAdapterBinding.txtAmount.setText(items.get(position).getAmount());
        }
        holder.itemView.setOnClickListener(v -> onItemClickHandler.onItemClick(position));


        holder.mTaskListAdapterBinding.dispatchTaskMobileNo.setOnClickListener(v -> onCallListItemClickHandler.onPrimaryMobileClicked(position));

        holder.mTaskListAdapterBinding.dispatchTaskAltMobileNo.setOnClickListener(v -> onCallListItemClickHandler.onAlternateMobileClicked(position));

        holder.mTaskListAdapterBinding.dispatchTaskPhoneNo.setOnClickListener(v -> onCallListItemClickHandler.onTelePhoneClicked(position));
        holder.mTaskListAdapterBinding.lnrMap.setOnClickListener(v -> onCallListItemClickHandler.onTrackLocationIconClicked(position));

        holder.mTaskListAdapterBinding.btnHelpline.setOnClickListener(view -> onCallListItemClickHandler.onTechnicianHelplineClicked(position));

        holder.mTaskListAdapterBinding.lnrPartnerPic.setOnClickListener(view -> onCallListItemClickHandler.onResourcePartnerPic(profile));

        final Handler ha = new Handler();
        ha.postDelayed(new Runnable() {

            @Override
            public void run() {
                //call function

                if (items.get(position).getStatus().equalsIgnoreCase("Dispatched")) {
                    startCountUpTimer(holder, position);
                } else {
                    holder.mTaskListAdapterBinding.imgWarning.setVisibility(View.GONE);
                    holder.mTaskListAdapterBinding.warning.setVisibility(View.GONE);
                }
                ha.postDelayed(this, 1000);
            }
        }, 1000);


        holder.mTaskListAdapterBinding.imgWarning.setOnClickListener(v -> {
            try {

                String titleText = "Running Late...";
                // Initialize a new foreground color span instance
                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.BLACK);
                // Initialize a new spannable string builder instance
                SpannableStringBuilder ssBuilder = new SpannableStringBuilder(titleText);
                // Apply the text color span
                ssBuilder.setSpan(
                        foregroundColorSpan,
                        0,
                        titleText.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                );
                startCountUpTimer(holder, position);
                String time = AppUtils.millisTOHr(milli);
                getErrorDialog(ssBuilder, "You're running late by " + time + ".");

            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
    }


    private void startCountUpTimer(final ViewHolder holder, int position) {
        String sdate = items.get(position).getTaskAssignmentStartDate();
        String edate = items.get(position).getTaskAssignmentEndDate();
//        yyyy-MM-dd HH:mm:ss

        String[] split_start = sdate.split("T");
        String start_date = (split_start[0]);
        String start_time = (split_start[1]);
        final String sDate = start_date + " " + start_time;

        String[] split_end = edate.split("T");
        String end_date = (split_end[0]);
        String end_time = (split_end[1]);
        String eDate = end_date + " " + end_time;


        String[] split_sDate = start_date.split("-");
        String year_start = split_sDate[0];
        String month_start = split_sDate[1];
        String day_start = split_sDate[2];


        String[] split_sTime = start_time.split(":");
        String hr_start = split_sTime[0];
        String mn_start = split_sTime[1];
        String secs_start = split_sTime[2];


        String[] split_eDate = end_date.split("-");
        String year_end = split_eDate[0];
        String month_end = split_eDate[1];
        String day_end = split_eDate[2];

        String[] split_eTime = end_time.split(":");
        String hr_end = split_eTime[0];
        String mn_end = split_eTime[1];
        String sec_end = split_eTime[2];


        final Time conferenceTime = new Time(Time.getCurrentTimezone());

        final int hour = Integer.parseInt(hr_start);
        final int minute = Integer.parseInt(mn_start);
        final int second = Integer.parseInt(secs_start);
        final int monthDay = Integer.parseInt(day_start);
        final int month = Integer.parseInt(month_start) - 1;
        final int year = Integer.parseInt(year_start);
        String isStartDate = AppUtils.compareDates(AppUtils.currentDateTime(), sDate);

        if (items.get(position).getStatus().equalsIgnoreCase("Dispatched")) {
            if (isStartDate.equalsIgnoreCase("afterdate")) {
                conferenceTime.set(second, minute, hour, monthDay, month, year);
                holder.mTaskListAdapterBinding.lnrTimer.setVisibility(View.GONE);
                holder.mTaskListAdapterBinding.imgWarning.setVisibility(View.VISIBLE);
                holder.mTaskListAdapterBinding.warning.setVisibility(View.VISIBLE);

            } else {
                holder.mTaskListAdapterBinding.lnrTimer.setVisibility(View.GONE);
                holder.mTaskListAdapterBinding.imgWarning.setVisibility(View.GONE);
                holder.mTaskListAdapterBinding.warning.setVisibility(View.GONE);
                conferenceTime.set(second, minute, hour, monthDay, month, year);
            }
        } else {
            holder.mTaskListAdapterBinding.imgWarning.setVisibility(View.GONE);
            holder.mTaskListAdapterBinding.warning.setVisibility(View.GONE);
        }

        conferenceTime.normalize(true);
        final long confMillis = conferenceTime.toMillis(true);
        Time nowTime = new Time(Time.getCurrentTimezone());
        nowTime.setToNow();
        nowTime.normalize(true);
        long nowMillis = nowTime.toMillis(true);
        long milliDiff = nowMillis - confMillis;
        milli = milliDiff;
        long millis = 900000 - milliDiff;
        timeSwapBuff += timeInMilliseconds;
    }


    public void getErrorDialog(SpannableStringBuilder title, String message) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle(title);
            builder.setMessage(message);
            builder.setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss()).create().show();
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("error", e.getMessage());
        }
    }


    @Override
    public int getItemCount() {
        return items.size();

    }

    public void setOnItemClickHandler(OnListItemClickHandler onItemClickHandler) {
        this.onItemClickHandler = onItemClickHandler;
    }

    public void setOnCallClickHandler(OnCallListItemClickHandler onCallListItemClickHandler) {
        this.onCallListItemClickHandler = onCallListItemClickHandler;
    }

    public void setData(List<Tasks> data) {
        items.clear();
        for (int index = 0; index < data.size(); index++) {
            TaskViewModel taskViewModel = new TaskViewModel();
            taskViewModel.clone(data.get(index));
            items.add(taskViewModel);
        }
    }

    public void addData(List<Tasks> data) {
        items.clear();
        for (int index = 0; index < data.size(); index++) {
            TaskViewModel taskViewModel = new TaskViewModel();
            taskViewModel.clone(data.get(index));
            items.add(taskViewModel);
        }
    }

    public TaskViewModel getItem(int position) {
        return items.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TaskListAdapterBinding mTaskListAdapterBinding;

        public ViewHolder(TaskListAdapterBinding mTaskListAdapterBinding) {
            super(mTaskListAdapterBinding.getRoot());
            this.mTaskListAdapterBinding = mTaskListAdapterBinding;
            if (!isShown) {
                ShowcaseConfig config = new ShowcaseConfig();
                config.setDelay(500); // half second between each showcase view

                MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(activity, "Hicare");

                sequence.setConfig(config);


                sequence.addSequenceItem(mTaskListAdapterBinding.imgTracklocation,
                        "Hi there, using this you can track customer location.", "GOT IT");

                sequence.addSequenceItem(mTaskListAdapterBinding.dispatchTaskMobileNo,
                        "Using this you can call customer.", "GOT IT");

                sequence.addSequenceItem(mTaskListAdapterBinding.status,
                        "You can check your task status here.", "GOT IT");

                sequence.addSequenceItem(mTaskListAdapterBinding.helpline,
                        "This is technician helpline number.", "GOT IT");

                sequence.start();
                isShown = true;
            }
        }
    }


}
