package com.ab.hicarerun.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.hicarerun.BaseApplication;
import com.ab.hicarerun.BaseFragment;
import com.ab.hicarerun.R;
import com.ab.hicarerun.adapter.QuizOptionAdapter;
import com.ab.hicarerun.adapter.QuizVideoParentAdapter;
import com.ab.hicarerun.adapter.RecycleBazaarAdapter;
import com.ab.hicarerun.databinding.FragmentQuizBinding;
import com.ab.hicarerun.handler.OnListItemClickHandler;
import com.ab.hicarerun.network.NetworkCallController;
import com.ab.hicarerun.network.NetworkResponseListner;
import com.ab.hicarerun.network.models.LoginResponse;
import com.ab.hicarerun.network.models.QuizModel.QuizData;
import com.ab.hicarerun.network.models.QuizModel.QuizSaveAnswers;
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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import io.realm.RealmResults;

public class QuizFragment extends BaseFragment implements Player.EventListener {
    FragmentQuizBinding mFragmentQuizBinding;
    private static final int REQ_QUESTIONS = 1000;
    public static final String ARGS_ID = "ARGS_ID";
    private QuizOptionAdapter mAdapter;
    private QuizVideoParentAdapter mVideoAdapter;
    List<QuizData> questions;
    int index = 0;
    QuizData question;
    CountDownTimer timer;
    private int puzzleId = 0;
    private final int SECONDS = 1000;
    private int MIN_BUFF_MS = 3000;
    private int MAX_BUFF_MS = 5000;
    private int BUFF_PLAYBACK_MS = 1000;
    private int REBUFF_MS = 2500;
    private int TARGET_BUFF_BYTES = -1;
    SimpleExoPlayer player;
    private PlayerView videoSurfaceView;
    private List<QuizSaveAnswers> saveAnswers;
    int points = 0;

    public QuizFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static QuizFragment newInstance(int puzzleId) {
        QuizFragment fragment = new QuizFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_ID, puzzleId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            puzzleId = getArguments().getInt(ARGS_ID);
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFragmentQuizBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_quiz, container, false);
        requireActivity().setTitle("");
        return mFragmentQuizBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        questions = new ArrayList<>();
        saveAnswers = new ArrayList<>();
//        mFragmentQuizBinding.recycleView.setHasFixedSize(true);
//        mFragmentQuizBinding.recycleView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
//        mAdapter = new QuizOptionAdapter(getActivity());
//        mFragmentQuizBinding.recycleView.setAdapter(mAdapter);
        getQuestions();
        resetTimer();
        mFragmentQuizBinding.nextBtn.setOnClickListener(view1 -> {
//            reset();
            if (index < questions.size()-1) {
                index++;
                setNextQuestion(true);

            } else {
//                Intent intent = new Intent(this, ResultActivity.class);
//                intent.putExtra("correct", correctAnswers);
//                intent.putExtra("total", questions.size());
//                startActivity(intent);
                Toast.makeText(getActivity(), "Quiz Finished.", Toast.LENGTH_SHORT).show();
                requireActivity().finish();
            }
        });
        if (mAdapter != null) {

        }
    }

    void resetTimer() {
        timer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mFragmentQuizBinding.timer.setText(String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                if (index < questions.size()) {
                    index++;
                    setNextQuestion(true);
                } else {
//                Intent intent = new Intent(this, ResultActivity.class);
//                intent.putExtra("correct", correctAnswers);
//                intent.putExtra("total", questions.size());
//                startActivity(intent);

                    requireActivity().finish();
                    Toast.makeText(getActivity(), "Quiz Finished.", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private void getQuestions() {
        try {
            RealmResults<LoginResponse> LoginRealmModels =
                    BaseApplication.getRealm().where(LoginResponse.class).findAll();
            if (LoginRealmModels != null && LoginRealmModels.size() > 0) {
                String resourceId = Objects.requireNonNull(LoginRealmModels.get(0)).getUserID();
                NetworkCallController controller = new NetworkCallController(this);
                controller.setListner(new NetworkResponseListner<List<QuizData>>() {
                    @Override
                    public void onResponse(int requestCode, List<QuizData> item) {
                        if (item != null && item.size() > 0) {
                            if (index < item.size()) {
                                questions = item;
                                setNextQuestion(false);

                            }
                        }
                    }

                    @Override
                    public void onFailure(int requestCode) {

                    }
                });
                controller.getQuizQuestions(REQ_QUESTIONS, resourceId, puzzleId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setNextQuestion(boolean isNextPressed) {

        if (timer != null) {
            timer.start();
        }
        mFragmentQuizBinding.timer.setVisibility(View.VISIBLE);
        if (index < questions.size()) {

            mFragmentQuizBinding.questionCounter.setText(points+"");
            //mFragmentQuizBinding.questionCounter.setText(String.format("%d/%d", (index + 1), questions.size()));
            question = questions.get(index);

            if (question.getIsDependentQuestionExist()) {
                Log.d("TAG", "Called");
                timer.cancel();
                mFragmentQuizBinding.timer.setVisibility(View.INVISIBLE);
                GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
                layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int pos) {
                        if (question.getDependentQuestionList().get(0).getPuzzleQuestionSelectionType().equalsIgnoreCase("Image")) {
                            return 1;
                        } else {
                            return 2;
                        }
                    }
                });
                mFragmentQuizBinding.recycleView.setLayoutManager(layoutManager);
                mVideoAdapter = new QuizVideoParentAdapter(getActivity());
                mVideoAdapter.setData(question.getDependentQuestionList(), question.getDependentQuestionList().get(0).getCorrectAnswers());
                mVideoAdapter.setOnOptionClicked((position, option) -> {
                    Log.d("ACT", option);
                });
                mFragmentQuizBinding.recycleView.setAdapter(mVideoAdapter);

                initializePlayer();
                buildMediaSource(Uri.parse(question.getPuzzleQuestionURL()));

            } else {
                GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
                layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int pos) {
                        if (question.getOptions().get(0).getOptionType().equalsIgnoreCase("Image")) {
                            return 1;
                        } else {
                            return 2;
                        }
                    }
                });
                mFragmentQuizBinding.recycleView.setLayoutManager(layoutManager);
                mAdapter = new QuizOptionAdapter(getActivity(), question.getPuzzleQuestionType(), question.getPuzzleQuestionSelectionType(), question.getCorrectAnswers(), isNextPressed);
                mAdapter.setData(question.getOptions(), question.getCorrectAnswers());
                mAdapter.setOnOptionClickListener((position, quizOption) -> {
                    if (question.getCorrectAnswerIds().contains(quizOption.getOptionId().toString())){
                        points = points + question.getPoints();
                    }
                    mFragmentQuizBinding.questionCounter.setText(points+"");
                    saveAnswers.add(new QuizSaveAnswers(question.getPuzzleId(), question.getPuzzleQuestionId(), question.getCorrectAnswerIds(), quizOption.getOptionId().toString(), "a1r9D000000OUNqQAO", question.getPoints()));
                });
                mFragmentQuizBinding.recycleView.setAdapter(mAdapter);
            }

            if (question.getPuzzleQuestionType().equals("Video")) {
                timer.cancel();
                mFragmentQuizBinding.timer.setVisibility(View.GONE);
                mFragmentQuizBinding.cardImage.setVisibility(View.GONE);
                mFragmentQuizBinding.question.setVisibility(View.GONE);
                mFragmentQuizBinding.videoQuestion.setVisibility(View.VISIBLE);

            } else if (question.getPuzzleQuestionType().equals("Image")) {
                mFragmentQuizBinding.question.setVisibility(View.GONE);
                mFragmentQuizBinding.cardImage.setVisibility(View.VISIBLE);
                mFragmentQuizBinding.videoQuestion.setVisibility(View.GONE);
            } else {
                mFragmentQuizBinding.cardImage.setVisibility(View.GONE);
                mFragmentQuizBinding.question.setVisibility(View.VISIBLE);
                mFragmentQuizBinding.videoQuestion.setVisibility(View.GONE);
                mFragmentQuizBinding.question.setText(question.getPuzzleQuestionTitle());
            }
            /*mAdapter.setOnItemClickHandler(position -> {
                if (timer != null)
                    timer.cancel();
            });*/
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
                mFragmentQuizBinding.videoQuestion.setPlayer(player);

                // Bind the player to the view.
                videoSurfaceView.setUseController(true);

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
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(requireActivity(),
                    Util.getUserAgent(getActivity(), getString(R.string.app_name)), bandwidthMeter);
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
                String userName = "TECHNICIAN NAME : " + Objects.requireNonNull(mLoginRealmModels.get(0)).getUserName();
                String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                AppUtils.sendErrorLogs(e.toString(), getClass().getSimpleName(), "buildMediaSource", lineNo, userName, DeviceName);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (player != null) {
            player.stop();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (player != null) {
            buildMediaSource(Uri.parse(question.getPuzzleQuestionURL()));
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
}
