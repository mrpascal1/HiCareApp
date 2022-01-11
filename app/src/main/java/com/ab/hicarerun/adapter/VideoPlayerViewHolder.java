package com.ab.hicarerun.adapter;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ab.hicarerun.R;
import com.ab.hicarerun.activities.ExoplayerActivity;
import com.ab.hicarerun.network.models.trainingmodel.Videos;
import com.bumptech.glide.RequestManager;

import java.util.concurrent.TimeUnit;

public class VideoPlayerViewHolder extends RecyclerView.ViewHolder {
    CardView exoCard;
    FrameLayout media_container;
    TextView title, description, duration;
    ImageView thumbnail, volumeControl, playIcon;
    ProgressBar progressBar;
    View parent;
    RequestManager requestManager;
    Activity mActivity;

    VideoPlayerViewHolder(@NonNull View itemView, Activity mActivity) {
        super(itemView);
        parent = itemView;
        this.mActivity = mActivity;
        exoCard = itemView.findViewById(R.id.exoCard);
        media_container = itemView.findViewById(R.id.media_container);
        thumbnail = itemView.findViewById(R.id.thumbnail);
        title = itemView.findViewById(R.id.title);
        duration = itemView.findViewById(R.id.duration);
        description = itemView.findViewById(R.id.description);
        progressBar = itemView.findViewById(R.id.progressBar);
        volumeControl = itemView.findViewById(R.id.volume_control);
        playIcon = itemView.findViewById(R.id.video_play);
    }

    void onBind(final Videos videos, RequestManager requestManager) {
        this.requestManager = requestManager;
        parent.setTag(this);
        title.setText(videos.getVideoTitle());
        description.setText(videos.getVideoDescription());
        this.requestManager
                .load(videos.getVideoThumbnail())
                .into(thumbnail);

        exoCard.setOnClickListener(v -> {
            Intent intent = new Intent(mActivity, ExoplayerActivity.class);
            intent.putExtra(ExoplayerActivity.VIDEO_URI, videos.getVideoUrl());
            mActivity.startActivity(intent);
        });

        try {
            duration.setText(getDuration(videos.getVideoUrl()));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private String getDuration(String videoUrl) {
        MediaPlayer mp = MediaPlayer.create(mActivity, Uri.parse(videoUrl));
        int duration = mp.getDuration();
        mp.release();
        /*convert millis to appropriate time*/
        return String.format("%d:%d",
                TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
    }

}














