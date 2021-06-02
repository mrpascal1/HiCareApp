package com.ab.hicarerun.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
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
 */
public class QuizOptionAdapter extends RecyclerView.Adapter<QuizOptionAdapter.ViewHolder> {
    private OnListItemClickHandler onItemClickHandler;
    private final Context mContext;
    private List<QuizOption> items = null;
    private String questionType = "";
    private String optionType = "";
    int correctAnswers = 0;
    private String correctAns = "";
    private List<QuizAnswer> answerList = null;


    public QuizOptionAdapter(Context context, String QuestionType, String OptionType, List<QuizAnswer> optionValue) {
        if (items == null) {
            items = new ArrayList<>();
        }
        this.mContext = context;
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
//            Picasso.get().load(items.get(position).getPuzzleUrl()).into(holder.mQuizCategoryAdapterBinding.imgCategory);
            if (optionType.equalsIgnoreCase("Radio") && items.get(position).getOptionType().equalsIgnoreCase("Text")) {
                holder.mLayoutOptionAdapterBinding.lnrRadio.setVisibility(View.VISIBLE);
                holder.mLayoutOptionAdapterBinding.lnrCheck.setVisibility(View.GONE);
                holder.mLayoutOptionAdapterBinding.lnrImgOption.setVisibility(View.GONE);
                holder.mLayoutOptionAdapterBinding.txtRadioOption.setText(items.get(position).getOptionValue());
            } else if (optionType.equalsIgnoreCase("Radio") && items.get(position).getOptionType().equalsIgnoreCase("Image")) {
                holder.mLayoutOptionAdapterBinding.lnrRadio.setVisibility(View.GONE);
                holder.mLayoutOptionAdapterBinding.lnrCheck.setVisibility(View.GONE);
                holder.mLayoutOptionAdapterBinding.lnrImgOption.setVisibility(View.VISIBLE);
                Picasso.get().load(items.get(position).getOptionUrl()).into(holder.mLayoutOptionAdapterBinding.imgOption);
            } else if (optionType.equalsIgnoreCase("Checkbox") && items.get(position).getOptionType().equalsIgnoreCase("Image")) {
                holder.mLayoutOptionAdapterBinding.lnrRadio.setVisibility(View.GONE);
                holder.mLayoutOptionAdapterBinding.lnrCheck.setVisibility(View.GONE);
                holder.mLayoutOptionAdapterBinding.lnrImgOption.setVisibility(View.VISIBLE);
                Picasso.get().load(items.get(position).getOptionUrl()).into(holder.mLayoutOptionAdapterBinding.imgOption);
            } else if (optionType.equalsIgnoreCase("Checkbox") && items.get(position).getOptionType().equalsIgnoreCase("Text")) {
                holder.mLayoutOptionAdapterBinding.lnrRadio.setVisibility(View.GONE);
                holder.mLayoutOptionAdapterBinding.lnrCheck.setVisibility(View.VISIBLE);
                holder.mLayoutOptionAdapterBinding.lnrImgOption.setVisibility(View.GONE);
                holder.mLayoutOptionAdapterBinding.txtCheckOption.setText(items.get(position).getOptionValue());
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

            holder.itemView.setOnClickListener(view -> {
                if (optionType.equalsIgnoreCase("Radio")) {
                    for (QuizAnswer ans : answerList) {
                        if (items.get(position).getOptionValue().equalsIgnoreCase(ans.getOptionValue())) {
                            correctAnswers++;
                            holder.mLayoutOptionAdapterBinding.lnrRadio.setBackground(mContext.getResources().getDrawable(R.drawable.option_right_border));
                        } else {
                            if (items.get(position).getOptionValue().equalsIgnoreCase(ans.getOptionValue())) {
                                holder.mLayoutOptionAdapterBinding.lnrRadio.setBackground(mContext.getResources().getDrawable(R.drawable.option_right_border));
                                notifyItemChanged(position);
                            }
                            holder.mLayoutOptionAdapterBinding.lnrRadio.setBackground(mContext.getResources().getDrawable(R.drawable.option_wrong_border));
                        }
                    }
                } else if (optionType.equalsIgnoreCase("Checkbox")) {
                    for (QuizAnswer ans : answerList) {
                        if (items.get(position).getOptionValue().equalsIgnoreCase(ans.getOptionValue())) {
                            holder.mLayoutOptionAdapterBinding.lnrCheck.setBackground(mContext.getResources().getDrawable(R.drawable.option_right_border));
                            break;
                        } else {
                            if (items.get(position).getOptionValue().equalsIgnoreCase(ans.getOptionValue())) {
                                holder.mLayoutOptionAdapterBinding.lnrCheck.setBackground(mContext.getResources().getDrawable(R.drawable.option_right_border));
                                notifyItemChanged(position);
                            }
                            holder.mLayoutOptionAdapterBinding.lnrCheck.setBackground(mContext.getResources().getDrawable(R.drawable.option_wrong_border));
                        }
                    }
//                    for (int i = 0; i < answerList.size(); i++) {
//                        if (items.get(position).getOptionValue().equalsIgnoreCase(answerList.get(i).getOptionValue())) {
//                            correctAnswers++;
//                            holder.mLayoutOptionAdapterBinding.lnrCheck.setBackground(mContext.getResources().getDrawable(R.drawable.option_right_border));
//                        } else {
//                            holder.mLayoutOptionAdapterBinding.lnrCheck.setBackground(mContext.getResources().getDrawable(R.drawable.option_wrong_border));
//                        }
//                    }
                }
                notifyDataSetChanged();
            });
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
