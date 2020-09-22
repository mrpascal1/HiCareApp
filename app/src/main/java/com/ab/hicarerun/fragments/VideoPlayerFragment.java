package com.ab.hicarerun.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.os.Handler;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;


import com.ab.hicarerun.BaseFragment;
import com.ab.hicarerun.R;
import com.ab.hicarerun.activities.HomeActivity;
import com.ab.hicarerun.activities.LoginActivity;
import com.ab.hicarerun.activities.SplashActiviy;
import com.ab.hicarerun.databinding.FragmentVideoPlayerBinding;
import com.ab.hicarerun.network.NetworkCallController;
import com.ab.hicarerun.network.NetworkResponseListner;
import com.ab.hicarerun.network.models.TrainingModel.Videos;
import com.ab.hicarerun.utils.AppUtils;
import com.ab.hicarerun.utils.SharedPreferencesUtility;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoPlayerFragment extends BaseFragment implements SurfaceHolder.Callback {
    private FragmentVideoPlayerBinding mFragmentVideoPlayerBinding;
    private static final String ARG_PROFILE_PIC = "ARG_PROFILE_PIC";
    private static final String ARG_MOBILE = "ARG_MOBILE";
    private static final String ARG_URL = "ARG_URL";
    private static final int VIDEO_REQUEST = 3000;

    //    private String profilePic = "";
//    private String mobile = "";
    private String url = "";
    private final int SECONDS = 1000;
    private Handler handler;
    private ProgressDialog progress;
    private int Duration = 0;
    private String video_url = "http://apps.hicare.in/video1.mp4";
    private MediaPlayer mediaPlayer;
    private SurfaceHolder surfaceHolder;
    private boolean putinside = true;

    public VideoPlayerFragment() {
        // Required empty public constructor
    }

    public static VideoPlayerFragment newInstance() {
        Bundle args = new Bundle();
        VideoPlayerFragment fragment = new VideoPlayerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            url = getArguments().getString(ARG_URL);
//            profilePic = getArguments().getString(ARG_PROFILE_PIC);
//            mobile = getArguments().getString(ARG_MOBILE);
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFragmentVideoPlayerBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_video_player, container, false);
        getWelcomeVideo();
        progress = new ProgressDialog(getActivity(), R.style.TransparentProgressDialog);
//        progress.setTitle("Please wait while we load video...");
        progress.setCancelable(false);
        progress.show();
        Objects.requireNonNull(getActivity()).getWindow().setFormat(PixelFormat.UNKNOWN);
        surfaceHolder = mFragmentVideoPlayerBinding.surfaceview.getHolder();
        surfaceHolder.addCallback(this);
//        surfaceHolder.setFixedSize(176, 144);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mediaPlayer = new MediaPlayer();


        mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
            @Override
            public void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i1) {
                progress.dismiss();
            }
        });


        return mFragmentVideoPlayerBinding.getRoot();
    }

    private void getWelcomeVideo() {
        NetworkCallController controller = new NetworkCallController();
        controller.setListner(new NetworkResponseListner() {
            @Override
            public void onResponse(int requestCode, Object response) {
                Videos items = (Videos) response;
                if (items != null) {
                    if (video_url.length() > 0) {
                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        mediaPlayer.setDisplay(surfaceHolder);
                        try {
                            mediaPlayer.setDataSource(video_url);
                            mediaPlayer.prepare();
                        } catch (IllegalArgumentException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (IllegalStateException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        mediaPlayer.start();
                        Duration = mediaPlayer.getDuration();
                        startTimer();
                        Log.i("video_duration", String.valueOf(mediaPlayer.getDuration()));
                    } else {
                        startActivity(new Intent(getActivity(), HomeActivity.class));
                    }
                }
            }

            @Override
            public void onFailure(int requestCode) {

            }
        });

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
//        toolbar.setVisibility(View.GONE);
        url = SharedPreferencesUtility.getPrefString(getActivity(), SharedPreferencesUtility.PREF_VIDEO_URL);
        SharedPreferencesUtility.savePrefBoolean(getActivity(), SharedPreferencesUtility.IS_SKIP_VIDEO, false);
        mFragmentVideoPlayerBinding.lnrSkip.setVisibility(View.GONE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                Window window = getActivity().getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.parseColor("#000000"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        mFragmentVideoPlayerBinding.land.setOnClickListener(view1 -> {
            mFragmentVideoPlayerBinding.land.setVisibility(View.GONE);
            mFragmentVideoPlayerBinding.potrait.setVisibility(View.VISIBLE);
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        });

        mFragmentVideoPlayerBinding.potrait.setOnClickListener(view12 -> {
            mFragmentVideoPlayerBinding.land.setVisibility(View.VISIBLE);
            mFragmentVideoPlayerBinding.potrait.setVisibility(View.GONE);
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        });


//        try {
//            Uri videoUri = Uri.parse(url);
//            mFragmentVideoPlayerBinding.videoView.setVideoURI(videoUri);
//            mFragmentVideoPlayerBinding.videoView.start();
//            mFragmentVideoPlayerBinding.videoView.getCurrentPosition();
//            startTimer();
//        }catch (Exception e){
//            e.printStackTrace();
//        }

//        mFragmentVideoPlayerBinding.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mediaPlayer) {
//                mediaPlayer.start();
//                mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
//
//                    @Override
//                    public void onVideoSizeChanged(MediaPlayer mp, int arg1, int arg2) {
//                        // TODO Auto-generated method stub
//                        Log.e("VIDEO", "Changed");
//                        progress.dismiss();
//                        mp.start();
//                    }
//                });
//            }
//        });

//        MediaPlayer mp = MediaPlayer.create(getActivity(), Uri.parse(url));
//        int duration = mp.getDuration();
//        mp.seekTo(10000);
//        mp.getCurrentPosition();
//        mp.release();
//        mFragmentVideoPlayerBinding.surfaceview.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mFragmentVideoPlayerBinding.btnSkip.setVisibility(View.VISIBLE);
//            }
//        }, 10000);

        mFragmentVideoPlayerBinding.lnrSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferencesUtility.savePrefBoolean(getActivity(), SharedPreferencesUtility.IS_SKIP_VIDEO, true);
                SharedPreferencesUtility.savePrefBoolean(getActivity(), SharedPreferencesUtility.IS_USER_LOGIN, true);
//                replaceFragment(HomeFragment.newInstance(), "VideoPlayerFragment-HomeFragment");
                startActivity(new Intent(getActivity(), HomeActivity.class));
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });


//        mFragmentVideoPlayerBinding.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
//                    @Override
//                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
//                        /*
//                         *  add media controller
//                         */
//                        mediaController = new MediaController(getActivity());
//                        mediaController.setPadding(0, 10, 0, 0);
//                        mFragmentVideoPlayerBinding.videoView.setMediaController(mediaController);
//                        /*
//                         * and set its position on screen
//                         */
//                        mediaController.setAnchorView(mFragmentVideoPlayerBinding.videoView);
//                    }
//                });
//            }
//        });
    }


    private void startTimer() {
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                showSkip();          // this method will contain your almost-finished HTTP calls
                handler.postDelayed(this, SECONDS);
            }
        }, SECONDS);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void showSkip() {
        try {
            Log.i("video_position", String.valueOf(mediaPlayer.getCurrentPosition()));
            if (Duration - mediaPlayer.getCurrentPosition() < 1500 && putinside) {
                Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.push_left_in);
                mFragmentVideoPlayerBinding.lnrSkip.setVisibility(View.VISIBLE);
                mFragmentVideoPlayerBinding.lnrSkip.startAnimation(animation);
                putinside = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        final Surface surface = surfaceHolder.getSurface();
        if (surface != null) {
            getWelcomeVideo();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        progress.dismiss();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        startActivity(new Intent(getActivity(), HomeActivity.class));
        getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }
}
