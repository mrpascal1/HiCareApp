package com.ab.hicarerun.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.ProductUnitsAdapterBinding;
import com.ab.hicarerun.databinding.QuizCategoryAdapterBinding;
import com.ab.hicarerun.databinding.QuizCategoryAdapterBindingImpl;
import com.ab.hicarerun.handler.OnListItemClickHandler;
import com.ab.hicarerun.network.models.KarmaModel.KarmaHistoryDetails;
import com.ab.hicarerun.network.models.ProductModel.ServicePlanUnits;
import com.ab.hicarerun.network.models.QuizModel.QuizCategoryData;
import com.ab.hicarerun.viewmodel.ProductViewModel;
import com.ab.hicarerun.viewmodel.QuizCategoryViewModel;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arjun Bhatt on 2/11/2021.
 */
public class QuizCategoryAdapter extends RecyclerView.Adapter<QuizCategoryAdapter.ViewHolder> {
    private OnListItemClickHandler onItemClickHandler;
    private final Context mContext;
    private List<QuizCategoryViewModel> items = null;

    public QuizCategoryAdapter(Context context) {
        if (items == null) {
            items = new ArrayList<>();
        }
        this.mContext = context;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        QuizCategoryAdapterBinding mQuizCategoryAdapterBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.quiz_category_adapter, parent, false);
        return new ViewHolder(mQuizCategoryAdapterBinding);
    }


    @Override
    public void onBindViewHolder(@NotNull ViewHolder holder, final int position) {
        try {
            holder.mQuizCategoryAdapterBinding.pTitleTv.setText(items.get(position).getPuzzleTitle());

            if (items.get(position).isCompleted()){
                holder.mQuizCategoryAdapterBinding.isCompletedCheck.setVisibility(View.VISIBLE);
                holder.mQuizCategoryAdapterBinding.playCard.setCardBackgroundColor(mContext.getResources().getColor(R.color.md_green_100));
                holder.mQuizCategoryAdapterBinding.playTv.setText("Completed");
            }else {
                holder.mQuizCategoryAdapterBinding.isCompletedCheck.setVisibility(View.GONE);
                holder.mQuizCategoryAdapterBinding.playCard.setCardBackgroundColor(mContext.getResources().getColor(R.color.md_red_50));
                holder.mQuizCategoryAdapterBinding.playTv.setText("Play Now");
            }
            holder.mQuizCategoryAdapterBinding.pProgress.setMax(100);
            int percentage = items.get(position).getCompletionPecentage();
            holder.mQuizCategoryAdapterBinding.pProgress.setProgress(percentage);
            if (percentage <= 30){
                holder.mQuizCategoryAdapterBinding.pProgress.setProgressDrawable(mContext.getDrawable(R.drawable.custom_progress_red));
            }else if (percentage <= 60){
                holder.mQuizCategoryAdapterBinding.pProgress.setProgressDrawable(mContext.getDrawable(R.drawable.custom_progress_yellow));
            }else{
                holder.mQuizCategoryAdapterBinding.pProgress.setProgressDrawable(mContext.getDrawable(R.drawable.custom_progress_green));
            }
            Picasso.get().load(items.get(position).getPuzzleUrl()).into(holder.mQuizCategoryAdapterBinding.imgCategory);
            holder.mQuizCategoryAdapterBinding.txtTitle.setText(items.get(position).getPuzzleTitle());
            holder.mQuizCategoryAdapterBinding.completedPercentTv.setText(percentage+"%");
            holder.itemView.setOnClickListener(view -> {
                if (!items.get(position).isCompleted()){
                    /*holder.itemView.setEnabled(false);
                    holder.mQuizCategoryAdapterBinding.playBtn.setEnabled(false);*/
                    onItemClickHandler.onItemClick(position);
                }else {
                    Toast.makeText(mContext, "Already played", Toast.LENGTH_SHORT).show();
                }
            });
            holder.mQuizCategoryAdapterBinding.playBtn.setOnClickListener(view -> {
                if (!items.get(position).isCompleted()){
                    /*holder.itemView.setEnabled(false);
                    holder.mQuizCategoryAdapterBinding.playBtn.setEnabled(false);*/
                    onItemClickHandler.onItemClick(position);
                }else {
                    Toast.makeText(mContext, "Already played", Toast.LENGTH_SHORT).show();
                }
            });
            //holder.mQuizCategoryAdapterBinding.playIv.setOnClickListener(view -> onItemClickHandler.onItemClick(position));
            //holder.mQuizCategoryAdapterBinding.playTv.setOnClickListener(view -> onItemClickHandler.onItemClick(position));
            //holder.mQuizCategoryAdapterBinding.playCard.setOnClickListener(view -> onItemClickHandler.onItemClick(position));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setOnItemClickHandler(OnListItemClickHandler onItemClickHandler) {
        this.onItemClickHandler = onItemClickHandler;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public QuizCategoryViewModel getItem(int position) {
        return items.get(position);
    }

    public void setData(List<QuizCategoryData> data) {
        items.clear();
        for (int index = 0; index < data.size(); index++) {
            QuizCategoryViewModel quizCategoryViewModel = new QuizCategoryViewModel();
            quizCategoryViewModel.clone(data.get(index));
            items.add(quizCategoryViewModel);
        }
    }
//
//    public void removeAll() {
//        items.removeAll(items);
//        notifyDataSetChanged();
//    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final QuizCategoryAdapterBinding mQuizCategoryAdapterBinding;

        public ViewHolder(QuizCategoryAdapterBinding mQuizCategoryAdapterBinding) {
            super(mQuizCategoryAdapterBinding.getRoot());
            this.mQuizCategoryAdapterBinding = mQuizCategoryAdapterBinding;
        }
    }
}
