package com.ab.hicarerun.fragments;

import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Switch;

import com.ab.hicarerun.BaseApplication;
import com.ab.hicarerun.BaseFragment;
import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.FragmentKarmaVideoBinding;
import com.ab.hicarerun.network.NetworkCallController;
import com.ab.hicarerun.network.NetworkResponseListner;
import com.ab.hicarerun.network.models.KarmaModel.SaveKarmaRequest;
import com.ab.hicarerun.network.models.KarmaModel.SaveKarmaResponse;
import com.ab.hicarerun.network.models.LoginResponse;
import com.ab.hicarerun.utils.AppUtils;
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

import java.util.Objects;

import io.realm.RealmResults;

public class KarmaVideoFragment extends BaseFragment implements Player.EventListener {
    FragmentKarmaVideoBinding mFragmentKarmaVideoBinding;
    public static final String ARGS_URL = "ARGS_URL";
    public static final String ARGS_ID = "ARGS_ID";
    public static final String ARGS_USER = "ARGS_USER";
    public static final String ARGS_LIFE = "ARGS_LIFE";
    private static final int REQ_KARMA = 1000;
    private Handler handler;
    private long duration = 0;

    private String videoUrl = "";
    private String userId = "";
    private int videoId = 0;
    private int lifeIndex = 0;
    private final int SECONDS = 1000;
    private int MIN_BUFF_MS = 3000;
    private int MAX_BUFF_MS = 5000;
    private int BUFF_PLAYBACK_MS = 1000;
    private int REBUFF_MS = 2500;
    private int TARGET_BUFF_BYTES = -1;
    SimpleExoPlayer player;
    private PlayerView videoSurfaceView;

    private enum OrientationState {PORTRAIT, LANDSCAPE}

    private OrientationState orientationState;
    public static boolean isBack = false;


    public static KarmaVideoFragment newInstance(String videoURL, Integer videoId, String resourceId, Integer lifeIndex) {
        KarmaVideoFragment fragment = new KarmaVideoFragment();
        Bundle args = new Bundle();
        args.putString(ARGS_URL, videoURL);
        args.putInt(ARGS_ID, videoId);
        args.putInt(ARGS_LIFE, lifeIndex);
        args.putString(ARGS_USER, resourceId);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            videoUrl = getArguments().getString(ARGS_URL);
            videoId = getArguments().getInt(ARGS_ID);
            userId = getArguments().getString(ARGS_USER);
            lifeIndex = getArguments().getInt(ARGS_LIFE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFragmentKarmaVideoBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_karma_video, container, false);
        return mFragmentKarmaVideoBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        Objects.requireNonNull(getActivity()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Toolbar toolbar = Objects.requireNonNull(getActivity()).findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);
        mFragmentKarmaVideoBinding.lnrClose.setVisibility(View.GONE);
        showVideo(videoUrl);
        mFragmentKarmaVideoBinding.btnClose.setOnClickListener(v -> {
            NetworkCallController controller = new NetworkCallController();
            SaveKarmaRequest request = new SaveKarmaRequest();
            request.setLifeLineId(lifeIndex);
            request.setResourceId(userId);
            request.setVideoId(videoId);
            controller.setListner(new NetworkResponseListner<SaveKarmaResponse>() {
                @Override
                public void onResponse(int requestCode, SaveKarmaResponse response) {
                    try {
                        isBack = true;
                        getActivity().onBackPressed();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int requestCode) {
                }
            });
            controller.saveKarmaDetails(REQ_KARMA, request);
        });
    }

    private void showVideo(String videoUrl) {
        try {
            initializePlayer();
            if (videoUrl == null) {
                return;
            }
            buildMediaSource(Uri.parse(videoUrl));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void startTimer() {
        try {
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    if (duration - player.getCurrentPosition() < 3000) {
                        player.stop();
                        mFragmentKarmaVideoBinding.lnrClose.setVisibility(View.VISIBLE);
                    }

                    handler.postDelayed(this, SECONDS);
                }
            }, SECONDS);
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
                mFragmentKarmaVideoBinding.exoPlayerView.setPlayer(player);

                // Bind the player to the view.
                videoSurfaceView.setUseController(false);
                setOrientationState(OrientationState.LANDSCAPE);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setOrientationState(OrientationState state) {
        try {
            orientationState = state;
            if (state == OrientationState.LANDSCAPE) {
                Objects.requireNonNull(getActivity()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
//                animateVolumeControl();
            } else if (state == OrientationState.PORTRAIT) {
                Objects.requireNonNull(getActivity()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
//                animateVolumeControl();
            }
        } catch (Exception e) {
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
        switch (playbackState) {
            case Player.STATE_BUFFERING:
                mFragmentKarmaVideoBinding.progress.setVisibility(View.VISIBLE);
                break;

            case Player.STATE_ENDED:
                break;

            case Player.STATE_IDLE:
                break;

            case Player.STATE_READY:
                mFragmentKarmaVideoBinding.progress.setVisibility(View.GONE);
                duration = player.getDuration();
                startTimer();
                break;

            default:
                break;
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        player.stop();
        handler.removeCallbacksAndMessages(null);
    }


}