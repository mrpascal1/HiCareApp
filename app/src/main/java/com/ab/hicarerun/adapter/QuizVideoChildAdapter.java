package com.ab.hicarerun.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.LayoutOptionAdapterBinding;
import com.ab.hicarerun.handler.OnListItemClickHandler;
import com.ab.hicarerun.handler.OnOptionClickListener;
import com.ab.hicarerun.handler.OnVideoOptionClickListener;
import com.ab.hicarerun.network.models.quizmodel.QuizAnswer;
import com.ab.hicarerun.network.models.quizmodel.QuizOption;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arjun Bhatt on 3/3/2021.
 */
public class QuizVideoChildAdapter extends RecyclerView.Adapter<QuizVideoChildAdapter.ViewHolder> {
    private OnListItemClickHandler onItemClickHandler = null;
    private OnOptionClickListener onOptionClickListener = null;
    private OnVideoOptionClickListener onVideoOptionClickListener = null;
    private final Context mContext;
    private List<QuizOption> items = null;
    private List<QuizAnswer> answerList = null;
    private int selectedPos = -1;
    private String QuestionType = "";
    private onOptionClicked onOptionClicked;
    private String type = "";
    String whichType = "";
    boolean isWrongSelected = false;
    boolean isRadioSelected = false;

    public QuizVideoChildAdapter(Context mContext, onOptionClicked onOptionClicked) {
        if (items == null) {
            items = new ArrayList<>();
        }
        if (answerList == null){
            answerList = new ArrayList<>();
        }
        this.onOptionClicked = onOptionClicked;
        this.mContext = mContext;
    }

    @NotNull
    @Override
    public QuizVideoChildAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        LayoutOptionAdapterBinding mLayoutOptionAdapterBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.layout_option_adapter, parent, false);
        return new QuizVideoChildAdapter.ViewHolder(mLayoutOptionAdapterBinding);
    }

    @Override
    public void onBindViewHolder(@NotNull QuizVideoChildAdapter.ViewHolder holder, final int position) {
        try {
            if (isRadioSelected) {
                for (int i = 0; i < items.size(); i++) {
                    if (selectedPos != i) {
                        holder.mLayoutOptionAdapterBinding.radioOption.setEnabled(false);
                        holder.itemView.setEnabled(false);
                    }
                }
            }
            if (isWrongSelected && isRadioSelected){
                for (QuizAnswer ans : answerList) {
                    if (items.get(position).getOptionValue().equalsIgnoreCase(ans.getOptionValue()) && items.get(position).getOptionId().equals(ans.getOptionId())) {
                        holder.mLayoutOptionAdapterBinding.lnrRadio.setBackground(mContext.getResources().getDrawable(R.drawable.option_right_border));
                        fadeOut(holder.mLayoutOptionAdapterBinding.lnrRadio);
                    }
                }

                isWrongSelected = false;
                notifyItemChanged(position);
            }
            if (whichType.equals("checkbox") && isWrongSelected){
                for (int i = 0; i < items.size(); i++){
                    if (i != selectedPos) {
                        holder.mLayoutOptionAdapterBinding.checkOption.setEnabled(false);
                        holder.itemView.setEnabled(false);
                    }
                }

                for (QuizAnswer ans : answerList) {
                    if (items.get(position).getOptionValue().equalsIgnoreCase(ans.getOptionValue()) && items.get(position).getOptionId().equals(ans.getOptionId())) {
                        holder.mLayoutOptionAdapterBinding.lnrCheck.setBackground(mContext.getResources().getDrawable(R.drawable.option_right_border));
                        fadeOut(holder.mLayoutOptionAdapterBinding.lnrCheck);
                    }
                }
            }
            if (type.equals("Radio")) {
                whichType = "radio";
                holder.mLayoutOptionAdapterBinding.txtRadioOption.setText(items.get(position).getOptionTitle());
                //holder.mLayoutOptionAdapterBinding.radioOption.setText(items.get(position).getOptionValue());
                holder.mLayoutOptionAdapterBinding.lnrRadio.setVisibility(View.VISIBLE);
                holder.mLayoutOptionAdapterBinding.lnrCheck.setVisibility(View.GONE);
                holder.mLayoutOptionAdapterBinding.lnrImgOption.setVisibility(View.GONE);
                holder.mLayoutOptionAdapterBinding.radioOption.setChecked(items.get(position).getIsSelected());


            } else {
                whichType = "checkbox";
                holder.mLayoutOptionAdapterBinding.txtCheckOption.setText(items.get(position).getOptionTitle());
                holder.mLayoutOptionAdapterBinding.lnrImgOption.setVisibility(View.GONE);
                holder.mLayoutOptionAdapterBinding.lnrRadio.setVisibility(View.GONE);
                holder.mLayoutOptionAdapterBinding.lnrCheck.setVisibility(View.VISIBLE);
                holder.mLayoutOptionAdapterBinding.checkOption.setChecked(items.get(position).getIsSelected());
                holder.mLayoutOptionAdapterBinding.checkOption.setVisibility(View.VISIBLE);
            }

            Log.d("TAG", answerList.toString());
            holder.mLayoutOptionAdapterBinding.radioOption.setOnClickListener(v -> {
                whichType = "radio";
                isRadioSelected = true;
                selectedPos = position;
                //onOptionClickListener.onItemClick(position, items.get(position), "", whichType, holder);
                onVideoOptionClickListener.onItemClick(position, items.get(position), "", whichType, holder);
                holder.mLayoutOptionAdapterBinding.radioOption.setChecked(true);
                for (QuizAnswer ans : answerList) {
                    if (items.get(position).getOptionValue().equalsIgnoreCase(ans.getOptionValue()) && items.get(position).getOptionId().equals(ans.getOptionId())) {
                        holder.mLayoutOptionAdapterBinding.lnrRadio.setBackground(mContext.getResources().getDrawable(R.drawable.option_right_border));
                        isWrongSelected = false;
                    } else {
                        isWrongSelected = true;
                        holder.mLayoutOptionAdapterBinding.lnrRadio.setBackground(mContext.getResources().getDrawable(R.drawable.option_wrong_border));
                    }
                }
                onItemClickHandler.onItemClick(position);
                notifyDataSetChanged();
            });

            holder.mLayoutOptionAdapterBinding.checkOption.setOnClickListener(v -> {
                whichType = "checkbox";
                isRadioSelected = false;
                selectedPos = position;
                //onOptionClickListener.onItemClick(position, items.get(position), "", whichType, holder);
                onVideoOptionClickListener.onItemClick(position, items.get(position), "", whichType, holder);
                holder.mLayoutOptionAdapterBinding.checkOption.setChecked(true);
                holder.mLayoutOptionAdapterBinding.checkOption.setEnabled(false);
                holder.itemView.setEnabled(false);
                for (QuizAnswer ans : answerList) {
                    if (items.get(position).getOptionValue().equalsIgnoreCase(ans.getOptionValue()) && items.get(position).getOptionId().equals(ans.getOptionId())) {
                        holder.mLayoutOptionAdapterBinding.lnrCheck.setBackground(mContext.getResources().getDrawable(R.drawable.option_right_border));
                        isWrongSelected = false;
                        return;
                    } else {
                        isWrongSelected = true;
                        holder.mLayoutOptionAdapterBinding.lnrCheck.setBackground(mContext.getResources().getDrawable(R.drawable.option_wrong_border));
                    }
                }
                onItemClickHandler.onItemClick(position);
                notifyDataSetChanged();
            });

            holder.itemView.setOnClickListener(v -> {
                selectedPos = position;
                if (type.equalsIgnoreCase("Radio")) {
                    holder.mLayoutOptionAdapterBinding.radioOption.setChecked(true);
                    whichType = "radio";
                    isRadioSelected = true;
                    //onOptionClickListener.onItemClick(position, items.get(position), "SomeTest", whichType, holder);
                    onVideoOptionClickListener.onItemClick(position, items.get(position), "SomeTest", whichType, holder);
                    for (QuizAnswer ans : answerList) {
                        Log.d("TAG", ans.getOptionValue());
                        if (items.get(position).getOptionValue().equalsIgnoreCase(ans.getOptionValue()) && items.get(position).getOptionId().equals(ans.getOptionId())) {
                            isWrongSelected = false;
                            holder.mLayoutOptionAdapterBinding.lnrRadio.setBackground(mContext.getResources().getDrawable(R.drawable.option_right_border));
                            break;
                        } else {
                            isWrongSelected = true;
                            Log.d("TAG", "Wrong");
                            holder.mLayoutOptionAdapterBinding.lnrRadio.setBackground(mContext.getResources().getDrawable(R.drawable.option_wrong_border));
                        }
                    }
                    onItemClickHandler.onItemClick(position);
                    notifyDataSetChanged();
                }else {
                    whichType = "checkbox";
                    isRadioSelected = false;
                    //onOptionClickListener.onItemClick(position, items.get(position), "SomeTest", whichType, holder);
                    onVideoOptionClickListener.onItemClick(position, items.get(position), "SomeTest", whichType, holder);
                    holder.mLayoutOptionAdapterBinding.checkOption.setChecked(true);
                    holder.mLayoutOptionAdapterBinding.checkOption.setEnabled(false);
                    holder.itemView.setEnabled(false);
                    for (QuizAnswer ans : answerList) {
                        if (items.get(position).getOptionValue().equalsIgnoreCase(ans.getOptionValue()) && items.get(position).getOptionId().equals(ans.getOptionId())) {
                            holder.mLayoutOptionAdapterBinding.lnrCheck.setBackground(mContext.getResources().getDrawable(R.drawable.option_right_border));
                            isWrongSelected = false;
                            return;
                        } else {
                            isWrongSelected = true;
                            holder.mLayoutOptionAdapterBinding.lnrCheck.setBackground(mContext.getResources().getDrawable(R.drawable.option_wrong_border));
                        }
                    }
                    onItemClickHandler.onItemClick(position);
                    notifyDataSetChanged();
                }
            });
        }catch (Exception e){
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

    public void setOnOptionClickListener(OnOptionClickListener onOptionClickListener){
        this.onOptionClickListener = onOptionClickListener;
    }

    public void setOnVideoOptionClickListener(OnVideoOptionClickListener onVideoOptionClickListener){
        this.onVideoOptionClickListener = onVideoOptionClickListener;
    }

    public QuizOption getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addData(List<QuizOption> data, String type, List<QuizAnswer> correctAnswers) {
        items.clear();
        items.addAll(data);
        answerList.addAll(correctAnswers);
        this.type = type;
    }

    public void updateAnswers(List<QuizAnswer> correctAnswers){
        answerList.clear();
        answerList.addAll(correctAnswers);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final LayoutOptionAdapterBinding mLayoutOptionAdapterBinding;

        public ViewHolder(LayoutOptionAdapterBinding mLayoutOptionAdapterBinding) {
            super(mLayoutOptionAdapterBinding.getRoot());
            this.mLayoutOptionAdapterBinding = mLayoutOptionAdapterBinding;
        }
    }

    public interface onOptionClicked {
        void onOptionClicked(int position, String option);
    }
}
