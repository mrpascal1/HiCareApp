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
import com.ab.hicarerun.databinding.LayoutConsulationChildAdapterBinding;
import com.ab.hicarerun.databinding.LayoutOptionAdapterBinding;
import com.ab.hicarerun.handler.OnListItemClickHandler;
import com.ab.hicarerun.network.models.ConsulationModel.Optionlist;
import com.ab.hicarerun.network.models.QuizModel.QuizAnswer;
import com.ab.hicarerun.network.models.QuizModel.QuizOption;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arjun Bhatt on 3/3/2021.
 */
public class QuizVideoChildAdapter extends RecyclerView.Adapter<QuizVideoChildAdapter.ViewHolder> {
    private OnListItemClickHandler onItemClickHandler;
    private final Context mContext;
    private List<QuizOption> items = null;
    private List<QuizAnswer> answerList = null;
    private int selectedPos = -1;
    private String QuestionType = "";
    private OnCheckChanged onCheckChanged;
    private String type = "";
    boolean isWrongSelected = false;
    boolean isRadioSelected = false;

    public QuizVideoChildAdapter(Context mContext, QuizVideoChildAdapter.OnCheckChanged onCheckChanged) {
        if (items == null) {
            items = new ArrayList<>();
        }
        if (answerList == null){
            answerList = new ArrayList<>();
        }
        this.onCheckChanged = onCheckChanged;
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
                        holder.mLayoutOptionAdapterBinding.radioOption.setChecked(false);
                        holder.mLayoutOptionAdapterBinding.radioOption.setEnabled(false);
                        holder.itemView.setEnabled(false);
                        items.get(i).setIsSelected(false);
                    }
                }
            }
            if (isWrongSelected && isRadioSelected){
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
            if (type.equals("Radio")) {

                holder.mLayoutOptionAdapterBinding.txtRadioOption.setText(items.get(position).getOptionTitle());
                holder.mLayoutOptionAdapterBinding.lnrRadio.setVisibility(View.VISIBLE);
                holder.mLayoutOptionAdapterBinding.lnrCheck.setVisibility(View.GONE);
                holder.mLayoutOptionAdapterBinding.lnrImgOption.setVisibility(View.GONE);
                holder.mLayoutOptionAdapterBinding.radioOption.setChecked(items.get(position).getIsSelected());


            } else {
                holder.mLayoutOptionAdapterBinding.txtCheckOption.setText(items.get(position).getOptionTitle());
                holder.mLayoutOptionAdapterBinding.lnrImgOption.setVisibility(View.GONE);
                holder.mLayoutOptionAdapterBinding.lnrRadio.setVisibility(View.GONE);
                holder.mLayoutOptionAdapterBinding.lnrCheck.setVisibility(View.VISIBLE);
                holder.mLayoutOptionAdapterBinding.checkOption.setChecked(items.get(position).getIsSelected());
                holder.mLayoutOptionAdapterBinding.checkOption.setVisibility(View.VISIBLE);
            }
            holder.mLayoutOptionAdapterBinding.radioOption.setOnClickListener(v -> {
                isRadioSelected = true;
                selectedPos = position;
                holder.mLayoutOptionAdapterBinding.radioOption.setChecked(true);
                for (QuizAnswer ans : answerList) {
                    if (items.get(position).getOptionValue().equalsIgnoreCase(ans.getOptionValue())) {
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

            holder.itemView.setOnClickListener(v -> {
                isRadioSelected = true;
                selectedPos = position;
                if (type.equalsIgnoreCase("Radio")) {
                    holder.mLayoutOptionAdapterBinding.radioOption.setChecked(true);
                    for (QuizAnswer ans : answerList) {
                        Log.d("TAG", ans.getOptionValue());
                        if (items.get(position).getOptionValue().equalsIgnoreCase(ans.getOptionValue())) {
                            holder.mLayoutOptionAdapterBinding.lnrRadio.setBackground(mContext.getResources().getDrawable(R.drawable.option_right_border));
                            isWrongSelected = false;
                        } else {
                            isWrongSelected = true;
                            holder.mLayoutOptionAdapterBinding.lnrRadio.setBackground(mContext.getResources().getDrawable(R.drawable.option_wrong_border));
                        }
                    }
                    onItemClickHandler.onItemClick(position);
                    notifyDataSetChanged();
                }else {
                    holder.mLayoutOptionAdapterBinding.checkOption.setChecked(true);
                    for (QuizAnswer ans : answerList) {
                        Log.d("TAG", ans.getOptionValue());
                        if (items.get(position).getOptionValue().equalsIgnoreCase(ans.getOptionValue())) {
                            holder.mLayoutOptionAdapterBinding.lnrCheck.setBackground(mContext.getResources().getDrawable(R.drawable.option_right_border));
                            isWrongSelected = false;
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final LayoutOptionAdapterBinding mLayoutOptionAdapterBinding;

        public ViewHolder(LayoutOptionAdapterBinding mLayoutOptionAdapterBinding) {
            super(mLayoutOptionAdapterBinding.getRoot());
            this.mLayoutOptionAdapterBinding = mLayoutOptionAdapterBinding;
        }
    }

    public interface OnCheckChanged {
        void onChecked(int position, boolean isChecked);
    }
}
