package com.ab.hicarerun.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.LayoutOptionAdapterBinding;
import com.ab.hicarerun.databinding.QuizCategoryAdapterBinding;
import com.ab.hicarerun.handler.OnListItemClickHandler;
import com.ab.hicarerun.network.models.QuizModel.QuizAnswer;
import com.ab.hicarerun.network.models.QuizModel.QuizCategoryData;
import com.ab.hicarerun.network.models.QuizModel.QuizData;
import com.ab.hicarerun.network.models.QuizModel.QuizOption;
import com.ab.hicarerun.viewmodel.QuizCategoryViewModel;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arjun Bhatt on 2/11/2021.
 * Later modified by Shahid Raza on 7/15/2021
 */
public class QuizOptionAdapter extends RecyclerView.Adapter<QuizOptionAdapter.ViewHolder> {
    private OnListItemClickHandler onItemClickHandler;
    private final Context mContext;
    private List<QuizOption> items = null;
    private String questionType = "";
    private String optionType = "";
    int correctAnswers = 0;
    int correctAnsPos = -1;
    boolean isWrongSelected = false;

    boolean isRadioAndText = false;
    boolean isRadioAndImage = false;
    boolean isCheckboxAndText = false;
    boolean isCheckboxAndImage = false;

    /**
     * <b>isRadioOptionSelected</b> is used as a flag for selected radio button
     * which is later used in code to disable the rest of the answers
     * so the user can't select it
     */
    int isRadioOptionSelected = 0;
    boolean isCheckOptionSelected = false;
    private int selectedPos = -1;
    private String correctAns = "";
    private List<QuizAnswer> answerList = null;
    private boolean isNextPressed = false;


    public QuizOptionAdapter(Context context, String QuestionType, String OptionType, List<QuizAnswer> optionValue, boolean isNextPressed) {
        if (items == null) {
            items = new ArrayList<>();
        }
        this.mContext = context;
        this.isNextPressed = isNextPressed;
        this.questionType = QuestionType;
        this.optionType = OptionType;
        if (answerList == null) {
            answerList = new ArrayList<>();
        }
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        LayoutOptionAdapterBinding mLayoutOptionAdapterBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.layout_option_adapter, parent, false);
        return new ViewHolder(mLayoutOptionAdapterBinding);
    }

    @Override
    public void onBindViewHolder(@NotNull ViewHolder holder, final int position) {

        try {
            if (isRadioOptionSelected == 1){
                for (int i = 0; i < items.size(); i++){
                    if (i != selectedPos) {
                        holder.mLayoutOptionAdapterBinding.radioOption.setEnabled(false);
                        holder.itemView.setEnabled(false);
                    }
                }
            }

            if (isWrongSelected && isRadioOptionSelected == 1){
                for (QuizAnswer ans : answerList) {
                    if (items.get(position).getOptionValue().equalsIgnoreCase(ans.getOptionValue())) {
                        holder.mLayoutOptionAdapterBinding.lnrRadio.setBackground(mContext.getResources().getDrawable(R.drawable.option_right_border));
                        fadeOut(holder.mLayoutOptionAdapterBinding.lnrRadio);
                    }/* else {
                        holder.mLayoutOptionAdapterBinding.lnrRadio.setBackground(mContext.getResources().getDrawable(R.drawable.option_wrong_border));
                    }*/
                }

                //isRadioOptionSelected = 0;
                isWrongSelected = false;
                notifyItemChanged(position);
            }

            if (isCheckOptionSelected && isWrongSelected){
                for (int i = 0; i < items.size(); i++){
                    if (i != selectedPos) {
                        holder.mLayoutOptionAdapterBinding.checkOption.setEnabled(false);
                        holder.itemView.setEnabled(false);
                    }
                }

                for (QuizAnswer ans : answerList) {
                    if (items.get(position).getOptionValue().equalsIgnoreCase(ans.getOptionValue())) {
                        holder.mLayoutOptionAdapterBinding.lnrCheck.setBackground(mContext.getResources().getDrawable(R.drawable.option_right_border));
                        fadeOut(holder.mLayoutOptionAdapterBinding.lnrCheck);
                    }
                }
            }

            if (isCheckboxAndImage){
                if (isWrongSelected && isCheckboxAndImage){
                    for (int i = 0; i < items.size(); i++){
                        if (i != selectedPos) {
                            holder.mLayoutOptionAdapterBinding.imgOption.setEnabled(false);
                            holder.itemView.setEnabled(false);
                        }
                    }

                    for (QuizAnswer ans : answerList) {
                        if (items.get(position).getOptionValue().equalsIgnoreCase(ans.getOptionValue())) {
                            holder.mLayoutOptionAdapterBinding.lnrImgOption.setBackground(mContext.getResources().getDrawable(R.drawable.option_right_border));
                            fadeOut(holder.mLayoutOptionAdapterBinding.lnrImgOption);
                        }
                    }
                }
            }

//            Picasso.get().load(items.get(position).getPuzzleUrl()).into(holder.mQuizCategoryAdapterBinding.imgCategory);
            if (optionType.equalsIgnoreCase("Radio") && items.get(position).getOptionType().equalsIgnoreCase("Text")) {
                Log.d("TAG-Which", "1");
                holder.mLayoutOptionAdapterBinding.lnrRadio.setVisibility(View.VISIBLE);
                holder.mLayoutOptionAdapterBinding.lnrCheck.setVisibility(View.GONE);
                holder.mLayoutOptionAdapterBinding.lnrImgOption.setVisibility(View.GONE);
                holder.mLayoutOptionAdapterBinding.txtRadioOption.setText(items.get(position).getOptionValue());
                isRadioAndText = true;
                isRadioAndImage = false;
                isCheckboxAndText = false;
                isCheckboxAndImage = false;
            } else if (optionType.equalsIgnoreCase("Radio") && items.get(position).getOptionType().equalsIgnoreCase("Image")) {
                Log.d("TAG-Which", "2");
                holder.mLayoutOptionAdapterBinding.lnrRadio.setVisibility(View.GONE);
                holder.mLayoutOptionAdapterBinding.lnrCheck.setVisibility(View.GONE);
                holder.mLayoutOptionAdapterBinding.lnrImgOption.setVisibility(View.VISIBLE);
                Picasso.get().load(items.get(position).getOptionUrl()).into(holder.mLayoutOptionAdapterBinding.imgOption);
                isRadioAndImage = true;
                isRadioAndText = false;
                isCheckboxAndText = false;
                isCheckboxAndImage = false;
            } else if (optionType.equalsIgnoreCase("Checkbox") && items.get(position).getOptionType().equalsIgnoreCase("Image")) {
                Log.d("TAG-Which", "3");
                isCheckboxAndImage = true;
                isRadioAndText = false;
                isRadioAndImage = false;
                isCheckboxAndText = false;
                holder.mLayoutOptionAdapterBinding.lnrRadio.setVisibility(View.GONE);
                holder.mLayoutOptionAdapterBinding.lnrCheck.setVisibility(View.GONE);
                holder.mLayoutOptionAdapterBinding.lnrImgOption.setVisibility(View.VISIBLE);
                Picasso.get().load(items.get(position).getOptionUrl()).into(holder.mLayoutOptionAdapterBinding.imgOption);
            } else if (optionType.equalsIgnoreCase("Checkbox") && items.get(position).getOptionType().equalsIgnoreCase("Text")) {
                Log.d("TAG-Which", "4");
                holder.mLayoutOptionAdapterBinding.lnrRadio.setVisibility(View.GONE);
                holder.mLayoutOptionAdapterBinding.lnrCheck.setVisibility(View.VISIBLE);
                holder.mLayoutOptionAdapterBinding.lnrImgOption.setVisibility(View.GONE);
                holder.mLayoutOptionAdapterBinding.txtCheckOption.setText(items.get(position).getOptionValue());
                isCheckboxAndText = true;
                isRadioAndText = false;
                isRadioAndImage = false;
                isCheckboxAndImage = false;
            }
//            holder.itemView.setOnClickListener(view -> onItemClickHandler.onItemClick(position));

            holder.mLayoutOptionAdapterBinding.cardImage.setOnClickListener(view -> {
                for (QuizAnswer ans : answerList) {
                    if (items.get(position).getOptionTitle().equalsIgnoreCase(ans.getOptionTitle())) {
                        holder.mLayoutOptionAdapterBinding.lnrImgOption.setBackground(mContext.getResources().getDrawable(R.drawable.option_right_border));
                        break;
                    } else {
                        for (int i = 0; i < answerList.size(); i++) {
                            if (items.get(position).getOptionTitle().equalsIgnoreCase(answerList.get(i).getOptionTitle())) {
                                holder.mLayoutOptionAdapterBinding.lnrImgOption.setBackground(mContext.getResources().getDrawable(R.drawable.option_right_border));
                            }
                        }
                        holder.mLayoutOptionAdapterBinding.lnrImgOption.setBackground(mContext.getResources().getDrawable(R.drawable.option_wrong_border));
                    }
                }
                notifyDataSetChanged();
            });

            holder.mLayoutOptionAdapterBinding.checkOption.setOnClickListener(v -> {
                isCheckOptionSelected = true;
                holder.mLayoutOptionAdapterBinding.checkOption.setChecked(true);
                holder.mLayoutOptionAdapterBinding.checkOption.setEnabled(false);
                holder.itemView.setEnabled(false);
                for (QuizAnswer ans : answerList) {
                    if (items.get(position).getOptionValue().equalsIgnoreCase(ans.getOptionValue())) {
                        holder.mLayoutOptionAdapterBinding.lnrCheck.setBackground(mContext.getResources().getDrawable(R.drawable.option_right_border));
                        isWrongSelected = false;
                        return;
                    } else {
                        isWrongSelected = true;
                        holder.mLayoutOptionAdapterBinding.lnrCheck.setBackground(mContext.getResources().getDrawable(R.drawable.option_wrong_border));
                    }
                }
                notifyDataSetChanged();
            });

            //holder.mLayoutOptionAdapterBinding.
            holder.mLayoutOptionAdapterBinding.radioOption.setOnClickListener(v -> {
                isRadioOptionSelected = 1;
                selectedPos = position;
                holder.mLayoutOptionAdapterBinding.radioOption.setChecked(true);
                for (QuizAnswer ans : answerList) {
                    if (items.get(position).getOptionValue().equalsIgnoreCase(ans.getOptionValue())) {
                        correctAnswers++;
                        isWrongSelected = false;
                        holder.mLayoutOptionAdapterBinding.lnrRadio.setBackground(mContext.getResources().getDrawable(R.drawable.option_right_border));
                    } else {
                        isWrongSelected = true;
                        holder.mLayoutOptionAdapterBinding.lnrRadio.setBackground(mContext.getResources().getDrawable(R.drawable.option_wrong_border));
                    }
                }
                notifyDataSetChanged();
            });

            holder.mLayoutOptionAdapterBinding.imgOption.setOnClickListener(v -> {
                selectedPos = position;
                isCheckOptionSelected = true;
                holder.mLayoutOptionAdapterBinding.imgOption.setEnabled(false);
                holder.itemView.setEnabled(false);
                for (QuizAnswer ans : answerList) {
                    if (items.get(position).getOptionValue().equalsIgnoreCase(ans.getOptionValue())) {
                        holder.mLayoutOptionAdapterBinding.lnrImgOption.setBackground(mContext.getResources().getDrawable(R.drawable.option_right_border));
                        isWrongSelected = false;
                        return;
                    } else {
                        holder.mLayoutOptionAdapterBinding.lnrImgOption.setBackground(mContext.getResources().getDrawable(R.drawable.option_wrong_border));
                        isWrongSelected = true;
                    }
                }
                notifyDataSetChanged();
            });

            holder.itemView.setOnClickListener(view -> {
                selectedPos = position;
                if (optionType.equalsIgnoreCase("Radio")) {
                    isRadioOptionSelected = 1;
                    holder.mLayoutOptionAdapterBinding.radioOption.setChecked(true);
                    for (QuizAnswer ans : answerList) {
                        if (items.get(position).getOptionValue().equalsIgnoreCase(ans.getOptionValue())) {
                            correctAnswers++;
                            isWrongSelected = false;
                            holder.mLayoutOptionAdapterBinding.lnrRadio.setBackground(mContext.getResources().getDrawable(R.drawable.option_right_border));
                            //fadeOut(holder.mLayoutOptionAdapterBinding.lnrRadio);
                        } else {
                            isWrongSelected = true;
                            holder.mLayoutOptionAdapterBinding.lnrRadio.setBackground(mContext.getResources().getDrawable(R.drawable.option_wrong_border));
                        }
                    }
                    notifyDataSetChanged();
                } else if (optionType.equalsIgnoreCase("Checkbox") && items.get(position).getOptionType().equalsIgnoreCase("Text")) {
                    isCheckOptionSelected = true;
                    holder.mLayoutOptionAdapterBinding.checkOption.setChecked(true);
                    holder.mLayoutOptionAdapterBinding.checkOption.setEnabled(false);
                    holder.itemView.setEnabled(false);
                    for (QuizAnswer ans : answerList) {
                        if (items.get(position).getOptionValue().equalsIgnoreCase(ans.getOptionValue())) {
                            holder.mLayoutOptionAdapterBinding.lnrCheck.setBackground(mContext.getResources().getDrawable(R.drawable.option_right_border));
                            isWrongSelected = false;
                            return;
                        } else {
                            holder.mLayoutOptionAdapterBinding.lnrCheck.setBackground(mContext.getResources().getDrawable(R.drawable.option_wrong_border));
                            isWrongSelected = true;
                        }
                    }
                    notifyDataSetChanged();
                } else if (optionType.equalsIgnoreCase("Checkbox") && items.get(position).getOptionType().equalsIgnoreCase("Image")) {
                    //isCheckOptionSelected = true;
                    holder.mLayoutOptionAdapterBinding.imgOption.setEnabled(false);
                    holder.itemView.setEnabled(false);
                    for (QuizAnswer ans : answerList) {
                        if (items.get(position).getOptionValue().equalsIgnoreCase(ans.getOptionValue())) {
                            holder.mLayoutOptionAdapterBinding.lnrImgOption.setBackground(mContext.getResources().getDrawable(R.drawable.option_right_border));
                            isWrongSelected = false;
                            return;
                        } else {
                            holder.mLayoutOptionAdapterBinding.lnrImgOption.setBackground(mContext.getResources().getDrawable(R.drawable.option_wrong_border));
                            isWrongSelected = true;
                        }
                    }
                    notifyDataSetChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fadeOut(View view){
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "alpha", 1f, 0.2f);
        objectAnimator.setDuration(400L);
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                fadeIn(view);
            }
        });
        objectAnimator.start();
    }

    private void fadeIn(View view){
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "alpha", 0.2f, 1f);
        objectAnimator.setDuration(400L);
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                fadeOut(view);
            }
        });
        objectAnimator.start();
    }

    public void setOnItemClickHandler(OnListItemClickHandler onItemClickHandler) {
        this.onItemClickHandler = onItemClickHandler;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setData(List<QuizOption> data, List<QuizAnswer> correctAnswers) {
        items.clear();
        items.addAll(data);
        answerList.addAll(correctAnswers);
        //        for (int index = 0; index < data.size(); index++) {
//            QuizCategoryViewModel quizCategoryViewModel = new QuizCategoryViewModel();
//            quizCategoryViewModel.clone(data.get(index));
//            items.add(quizCategoryViewModel);
//        }
    }

    public QuizOption getItem(int position) {
        return items.get(position);
    }
//
//    public void removeAll() {
//        items.removeAll(items);
//        notifyDataSetChanged();
//    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final LayoutOptionAdapterBinding mLayoutOptionAdapterBinding;

        public ViewHolder(LayoutOptionAdapterBinding mLayoutOptionAdapterBinding) {
            super(mLayoutOptionAdapterBinding.getRoot());
            this.mLayoutOptionAdapterBinding = mLayoutOptionAdapterBinding;
        }
    }
}
