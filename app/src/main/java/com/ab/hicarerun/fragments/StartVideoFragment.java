package com.ab.hicarerun.fragments;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.ab.hicarerun.BaseApplication;
import com.ab.hicarerun.BaseFragment;
import com.ab.hicarerun.R;
import com.ab.hicarerun.activities.HomeActivity;
import com.ab.hicarerun.databinding.FragmentStartVideoBinding;
import com.ab.hicarerun.network.NetworkCallController;
import com.ab.hicarerun.network.NetworkResponseListner;
import com.ab.hicarerun.network.models.LoginResponse;
import com.ab.hicarerun.network.models.TrainingModel.Videos;
import com.ab.hicarerun.utils.AppUtils;
import com.ab.hicarerun.utils.SharedPreferencesUtility;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class StartVideoFragment extends BaseFragment implements Player.EventListener {
    FragmentStartVideoBinding mFragmentStartVideoBinding;
    private PlayerView videoSurfaceView;

    private enum OrientationState {PORTRAIT, LANDSCAPE}

    private OrientationState orientationState;
    //    private String video_url = "http://apps.hicare.in/video1.mp4";
    private Handler handler;
    private static final int VIDEO_REQUEST = 3000;
    private final int SECONDS = 1000;
    private int MIN_BUFF_MS = 3000;
    private int MAX_BUFF_MS = 5000;
    private int BUFF_PLAYBACK_MS = 1000;
    private int REBUFF_MS = 2500;
    private int TARGET_BUFF_BYTES = -1;
    SimpleExoPlayer player;
    private static final String ARG_USER = "ARG_USER";
    private static final String ARG_URL = "ARG_URL";
    private String username = "";
    private String URL = "";
    private long duration = 0;
    private boolean isShown = false;

    public StartVideoFragment() {
        // Required empty public constructor
    }

    public static StartVideoFragment newInstance(String username, String URL) {
        Bundle args = new Bundle();
        args.putString(ARG_USER, username);
        args.putString(ARG_URL, URL);
        StartVideoFragment fragment = new StartVideoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            username = getArguments().getString(ARG_USER);
            URL = getArguments().getString(ARG_URL);
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFragmentStartVideoBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_start_video, container, false);
        return mFragmentStartVideoBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Objects.requireNonNull(getActivity()).getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mFragmentStartVideoBinding.progress.setVisibility(View.VISIBLE);
        mFragmentStartVideoBinding.lnrSkip.setVisibility(View.GONE);
        mFragmentStartVideoBinding.exoPlayerView.setOnClickListener(view1 -> animateVolumeControl());

        mFragmentStartVideoBinding.view.setOnClickListener(view12 -> animateVolumeControl());

        mFragmentStartVideoBinding.imgOrientation.setOnClickListener(view13 -> toggleOrientation());
        mFragmentStartVideoBinding.lnrSkip.setOnClickListener(view14 -> {
            SharedPreferencesUtility.savePrefBoolean(getActivity(), SharedPreferencesUtility.IS_SKIP_VIDEO, true);
            SharedPreferencesUtility.savePrefBoolean(getActivity(), SharedPreferencesUtility.IS_USER_LOGIN, true);
            AppUtils.getHandShakeCall(username, getActivity());
        });
        setUp(URL);
    }

    private void getWelcomeVideo() {
        NetworkCallController controller = new NetworkCallController();
        controller.setListner(new NetworkResponseListner() {
            @Override
            public void onResponse(int requestCode, Object response) {
                Videos items = (Videos) response;
                if (items != null) {
                    if (items.getVideoUrl().length() > 0) {
                        setUp(items.getVideoUrl());
                    } else {
                        startActivity(new Intent(getActivity(), HomeActivity.class));
                    }
                }
            }

            @Override
            public void onFailure(int requestCode) {

            }
        });
        controller.getStartingVideos(VIDEO_REQUEST);
    }

    private void startTimer() {
        try {
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    showSkip();
                    handler.postDelayed(this, SECONDS);
                }
            }, SECONDS);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void setUp(String video_url) {
        try {
            initializePlayer();
            if (video_url == null) {
                return;
            }
            buildMediaSource(Uri.parse(video_url));
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void initializePlayer() {
        try {
            if (player == null) {
                videoSurfaceView = new PlayerView(getActivity());
                videoSurfaceView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);
                // 1. Create a default TrackSelector
                LoadControl loadControl = new DefaultLoadControl(
                        new DefaultAllocator(true, 16),
                        MIN_BUFF_MS,
                        MAX_BUFF_MS,
                        BUFF_PLAYBACK_MS,
                        REBUFF_MS,
                        TARGET_BUFF_BYTES,
                        true);

                BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                TrackSelection.Factory videoTrackSelectionFactory =
                        new AdaptiveTrackSelection.Factory(bandwidthMeter);
                TrackSelector trackSelector =
                        new DefaultTrackSelector(videoTrackSelectionFactory);
                // 2. Create the player
                player = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(getActivity()), trackSelector, loadControl);
                mFragmentStartVideoBinding.exoPlayerView.setPlayer(player);

                // Bind the player to the view.
                videoSurfaceView.setUseController(false);
                setOrientationState(OrientationState.LANDSCAPE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void toggleOrientation() {
        try {
            if (mFragmentStartVideoBinding.exoPlayerView != null) {
                if (orientationState == OrientationState.LANDSCAPE) {
                    setOrientationState(OrientationState.PORTRAIT);

                } else if (orientationState == OrientationState.PORTRAIT) {
                    setOrientationState(OrientationState.LANDSCAPE);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    private void setOrientationState(OrientationState state) {
        try {
            orientationState = state;
            if (state == OrientationState.LANDSCAPE) {
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                animateVolumeControl();
            } else if (state == OrientationState.PORTRAIT) {
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                animateVolumeControl();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void animateVolumeControl() {
        try {
            if (orientationState != null) {
                mFragmentStartVideoBinding.imgOrientation.bringToFront();
                if (orientationState == OrientationState.LANDSCAPE) {
                    Glide.with(Objects.requireNonNull(getActivity())).load(R.drawable.ic_landscape).into(mFragmentStartVideoBinding.imgOrientation);
                } else if (orientationState == OrientationState.PORTRAIT) {
                    Glide.with(Objects.requireNonNull(getActivity())).load(R.drawable.ic_portrait).into(mFragmentStartVideoBinding.imgOrientation);
                }
                mFragmentStartVideoBinding.lnrOrientation.animate().cancel();

                mFragmentStartVideoBinding.lnrOrientation.setAlpha(1f);

                mFragmentStartVideoBinding.lnrOrientation.animate()
                        .alpha(0f)
                        .setDuration(1500)
                        .setStartDelay(1000);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void buildMediaSource(Uri mUri) {
        try {
            // Measures bandwidth during playback. Can be null if not required.
            DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            // Produces DataSource instances through which media data is loaded.
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(Objects.requireNonNull(getContext()),
                    Util.getUserAgent(getContext(), getString(R.string.app_name)), bandwidthMeter);
            // This is the MediaSource representing the media to be played.
            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(mUri);
            // Prepare the player with the source.
            player.prepare(videoSource);
            player.setPlayWhenReady(true);
            player.addListener(this);
        } catch (Exception e) {
            RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
            if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                assert mLoginRealmModels.get(0) != null;
                String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                AppUtils.sendErrorLogs(e.toString(), getClass().getSimpleName(), "buildMediaSource", lineNo, userName, DeviceName);
            }
        }
    }

    private void showSkip() {
        try {
            Log.i("video_position", String.valueOf(player.getCurrentPosition()));
            if (duration - player.getCurrentPosition() < 10000 && !isShown) {
                Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.push_left_in);
                mFragmentStartVideoBinding.lnrSkip.setVisibility(View.VISIBLE);
                mFragmentStartVideoBinding.lnrSkip.startAnimation(animation);
                isShown = true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    @Override
    public void onConfigurationChanged(@NotNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    @Override
    public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        try {
            switch (playbackState) {

                case Player.STATE_BUFFERING:
                    mFragmentStartVideoBinding.progress.setVisibility(View.VISIBLE);
                    break;
                case Player.STATE_ENDED:
//                Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.push_left_in);
//                mFragmentStartVideoBinding.lnrSkip.setVisibility(View.VISIBLE);
//                mFragmentStartVideoBinding.lnrSkip.startAnimation(animation);
                    break;
                case Player.STATE_IDLE:

                    break;
                case Player.STATE_READY:
                    startTimer();
                    mFragmentStartVideoBinding.progress.setVisibility(View.GONE);
                    duration = player.getDuration();
                    break;


                default:
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        mFragmentStartVideoBinding.lnrSkip.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }
}
