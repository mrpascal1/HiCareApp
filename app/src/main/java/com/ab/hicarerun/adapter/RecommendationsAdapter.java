package com.ab.hicarerun.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.LayoutProductCartAdapterBinding;
import com.ab.hicarerun.databinding.LayoutRecommendationsAdapterBinding;
import com.ab.hicarerun.handler.OnCartItemClickHandler;
import com.ab.hicarerun.handler.OnListItemClickHandler;
import com.ab.hicarerun.network.models.ConsulationModel.Recommendations;
import com.ab.hicarerun.network.models.ProductCartModel.ProductCart;
import com.ab.hicarerun.utils.ImageOverlayStfalcon;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static com.ab.hicarerun.BaseApplication.getRealm;

/**
 * Created by Arjun Bhatt on 7/23/2020.
 */
public class RecommendationsAdapter extends RecyclerView.Adapter<RecommendationsAdapter.ViewHolder> {

    private OnImageClicked onImageClicked;
    private final Context mContext;
    private List<Recommendations> items = null;
    private String type = "";
    private boolean isPLAYING = false;
    MediaPlayer mp;
    private int lastPlaying = -1;
    int imgCount = 0;
    private boolean startedPlaying = false;

    public RecommendationsAdapter(Context context, String type) {
        if (items == null) {
            items = new ArrayList<>();
        }
        this.mContext = context;
        this.type = type;
        this.onImageClicked = null;
    }

    @NotNull
    @Override
    public RecommendationsAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        LayoutRecommendationsAdapterBinding layoutRecommendationsAdapterBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.layout_recommendations_adapter, parent, false);
        return new RecommendationsAdapter.ViewHolder(layoutRecommendationsAdapterBinding);
    }

    @Override
    public void onBindViewHolder(@NotNull RecommendationsAdapter.ViewHolder holder, final int position) {
        try {
            if (lastPlaying != -1 && (lastPlaying == position && isPLAYING)){
                holder.layoutRecommendationsAdapterBinding.progressBar.setVisibility(GONE);
                holder.layoutRecommendationsAdapterBinding.speakerIv.setVisibility(View.VISIBLE);
                holder.layoutRecommendationsAdapterBinding.speakerIv.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_music_stop));
            }else {
                holder.layoutRecommendationsAdapterBinding.progressBar.setVisibility(GONE);
                holder.layoutRecommendationsAdapterBinding.speakerIv.setVisibility(View.VISIBLE);
                holder.layoutRecommendationsAdapterBinding.speakerIv.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_speaker));
            }
            if (position == lastPlaying && startedPlaying){
                holder.layoutRecommendationsAdapterBinding.progressBar.setVisibility(GONE);
                holder.layoutRecommendationsAdapterBinding.speakerIv.setVisibility(View.VISIBLE);
            }
            if (items.get(position).getRecommendationTitle() != null || !items.get(position).getRecommendationTitle().equals("")) {
                holder.layoutRecommendationsAdapterBinding.txtTitle.setText(items.get(position).getRecommendationTitle());
            }else {
                holder.layoutRecommendationsAdapterBinding.txtTitle.setVisibility(GONE);
            }
            if (items.get(position).getRecommendationDescription() != null && !items.get(position).getRecommendationDescription().equals("")) {
                if (type.equals("TMS")) {
                    holder.layoutRecommendationsAdapterBinding.txtDefaultArea.setTypeface(holder.layoutRecommendationsAdapterBinding.txtDefaultArea.getTypeface(), Typeface.BOLD);
                    holder.layoutRecommendationsAdapterBinding.txtDescLabel.setVisibility(View.GONE);
                    holder.layoutRecommendationsAdapterBinding.txtColon.setVisibility(View.GONE);
                }
                holder.layoutRecommendationsAdapterBinding.lnrDescription.setVisibility(View.VISIBLE);
                holder.layoutRecommendationsAdapterBinding.txtDescription.setText(Html.fromHtml(items.get(position).getRecommendationDescription()));
            } else {
                holder.layoutRecommendationsAdapterBinding.lnrDescription.setVisibility(View.GONE);
            }

            if (items.get(position).getChemicalValue() != null && !items.get(position).getChemicalValue().equals("")) {
                //holder.layoutRecommendationsAdapterBinding.lnrChem.setVisibility(View.VISIBLE);
                holder.layoutRecommendationsAdapterBinding.txtChemValue.setText(items.get(position).getChemicalValue());
                holder.layoutRecommendationsAdapterBinding.txtChemValue.setTypeface(holder.layoutRecommendationsAdapterBinding.txtChemValue.getTypeface(), Typeface.BOLD);
                /*LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        1.4f
                );
                holder.layoutRecommendationsAdapterBinding.lnrData.setLayoutParams(param);*/
            } else {
                //holder.layoutRecommendationsAdapterBinding.lnrChem.setVisibility(View.GONE);
                holder.layoutRecommendationsAdapterBinding.lnrChemicalValue.setVisibility(View.GONE);
                /*LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                      0,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        2f
                );
                holder.layoutRecommendationsAdapterBinding.lnrData.setLayoutParams(param);*/
            }

            if (items.get(position).getDefaultArea() != null && !items.get(position).getDefaultArea().equals("")) {
                holder.layoutRecommendationsAdapterBinding.lnrDefaultArea.setVisibility(View.VISIBLE);
                holder.layoutRecommendationsAdapterBinding.txtDefaultArea.setText(items.get(position).getDefaultArea());
            } else {
                holder.layoutRecommendationsAdapterBinding.lnrDefaultArea.setVisibility(View.GONE);
            }

            if (items.get(position).getExtraArea() != null && !items.get(position).getExtraArea().equals("")) {
                holder.layoutRecommendationsAdapterBinding.lnrExtra.setVisibility(View.VISIBLE);
                holder.layoutRecommendationsAdapterBinding.txtExtra.setText(items.get(position).getExtraArea());
            } else {
                holder.layoutRecommendationsAdapterBinding.lnrExtra.setVisibility(View.GONE);
            }

            if (items.get(position).getDuration() != null && !items.get(position).getDuration().equals("")) {
                holder.layoutRecommendationsAdapterBinding.lnrDuration.setVisibility(View.VISIBLE);
                holder.layoutRecommendationsAdapterBinding.txtDuration.setText(items.get(position).getDuration());
            } else {
                holder.layoutRecommendationsAdapterBinding.lnrDuration.setVisibility(View.GONE);
            }
            if (items.get(position).isAudioEnabled() && items.get(position).getRecommendationAudioUrl() != null){
                holder.layoutRecommendationsAdapterBinding.lnrSpeaker.setVisibility(View.VISIBLE);
                holder.layoutRecommendationsAdapterBinding.speakerIv.setVisibility(View.VISIBLE);
            }else {
                holder.layoutRecommendationsAdapterBinding.lnrSpeaker.setVisibility(View.GONE);
                holder.layoutRecommendationsAdapterBinding.speakerIv.setVisibility(View.GONE);
            }
            if (items.get(position).getRecommendationImageUrl() != null){
                Picasso.get().load(items.get(position).getRecommendationImageUrl()).fit().centerCrop().into(holder.layoutRecommendationsAdapterBinding.recommendationIv);
                holder.layoutRecommendationsAdapterBinding.lnrRecommendationIv.setVisibility(View.VISIBLE);
            }else {
                holder.layoutRecommendationsAdapterBinding.lnrRecommendationIv.setVisibility(View.GONE);
            }

            if (items.get(position).getRecommendationImageUrl() == null && items.get(position).getChemicalValue() == null){
                holder.layoutRecommendationsAdapterBinding.lnrRecommendationIv.setVisibility(GONE);
                holder.layoutRecommendationsAdapterBinding.lnrChemicalValue.setVisibility(GONE);
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        2f
                );
                holder.layoutRecommendationsAdapterBinding.lnrChem.setVisibility(GONE);
                holder.layoutRecommendationsAdapterBinding.lnrData.setLayoutParams(param);
            }
            holder.layoutRecommendationsAdapterBinding.recommendationIv.setOnClickListener(v -> {
                //onImageClicked.onClicked(items.get(position).getRecommendationImageUrl(), position);
                new ImageOverlayStfalcon(mContext, new String[]{items.get(position).getRecommendationImageUrl()});
            });
            /*holder.layoutRecommendationsAdapterBinding.recommendationIv.setOnClickListener(view -> {
                onImageClicked.onClicked(items.get(position).getRecommendationImageUrl(), position);
            });*/

            if (mp != null) {
                mp.setOnCompletionListener(mediaPlayer -> {
                    if (mp != null) {
                        mp.release();
                        isPLAYING = false;
                        mp = null;
                        notifyItemChanged(lastPlaying);
                        lastPlaying = -1;
                    }
                });
                mp.setOnPreparedListener(mediaPlayer -> {
                    mediaPlayer.start();
                    if (mp.isPlaying()) {
                        startedPlaying = true;
                        notifyDataSetChanged();
                    }
                });
                if (position == lastPlaying) {
                    if (!mp.isPlaying()) {
                        Log.d("TAG", "Not Playing");
                        holder.layoutRecommendationsAdapterBinding.progressBar.setVisibility(View.VISIBLE);
                        holder.layoutRecommendationsAdapterBinding.speakerIv.setVisibility(GONE);
                    } else {
                        holder.layoutRecommendationsAdapterBinding.speakerIv.setVisibility(View.VISIBLE);
                        holder.layoutRecommendationsAdapterBinding.progressBar.setVisibility(GONE);
                    }
                }
            }
            holder.layoutRecommendationsAdapterBinding.speakerIv.setOnClickListener(view -> {
                holder.layoutRecommendationsAdapterBinding.progressBar.setVisibility(View.VISIBLE);
                holder.layoutRecommendationsAdapterBinding.speakerIv.setVisibility(GONE);
                if (position == lastPlaying){
                    stopPlaying();
                    holder.layoutRecommendationsAdapterBinding.speakerIv.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_speaker));
                    notifyItemChanged(lastPlaying);
                    lastPlaying = -1;
                }else {
                    stopPlaying();
                    if (items.get(position).getRecommendationAudioUrl() != null) {
                        playAudio(holder.layoutRecommendationsAdapterBinding.speakerIv, items.get(position).getRecommendationAudioUrl(), position);
                    } else {
                        playAudio(holder.layoutRecommendationsAdapterBinding.speakerIv, "https://www.kozco.com/tech/piano2-CoolEdit.mp3", position);
                    }
                    notifyDataSetChanged();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playAudio(ImageView view, String url, int position) {
        mp = new MediaPlayer();
        if (!isPLAYING) {
            isPLAYING = true;
            lastPlaying = position;
            try {
                view.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_music_stop));
                mp.setDataSource(mContext, Uri.parse(url));
                //mp.setOnPreparedListener(MediaPlayer::start);
                mp.prepareAsync();
                //mp.start();
            } catch (IOException e) {
                Log.d("TAG", "prepare() failed");
            }
        } else {
            isPLAYING = false;
            stopPlaying();
            view.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_speaker));
        }
    }

    public void stopPlaying() {
        if (mp != null) {
            isPLAYING = false;
            mp.stop();
            mp.release();
            mp = null;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public Recommendations getItem(int position) {
        return items.get(position);
    }

    public void setData(List<Recommendations> data) {
        items.clear();
        items.addAll(data);
    }

    public void removeAll() {
        items.removeAll(items);
        notifyDataSetChanged();
    }

    public void remove(int position) {
        items.remove(items.get(position));
        notifyDataSetChanged();
    }

    public void setOnItemClickHandler(OnImageClicked onImageClicked){
        this.onImageClicked = onImageClicked;
    }

    public interface OnImageClicked{
        void onClicked(String image, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final LayoutRecommendationsAdapterBinding layoutRecommendationsAdapterBinding;

        public ViewHolder(LayoutRecommendationsAdapterBinding layoutRecommendationsAdapterBinding) {
            super(layoutRecommendationsAdapterBinding.getRoot());
            this.layoutRecommendationsAdapterBinding = layoutRecommendationsAdapterBinding;
        }
    }
}
