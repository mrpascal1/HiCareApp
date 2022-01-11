package com.ab.hicarerun.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.LayoutQuizParentAdapterBinding;
import com.ab.hicarerun.handler.OnConsultationClickHandler;
import com.ab.hicarerun.handler.OnOptionClickListener;
import com.ab.hicarerun.handler.OnVideoOptionClickListener;
import com.ab.hicarerun.network.models.quizmodel.QuizAnswer;
import com.ab.hicarerun.network.models.quizmodel.VideoDependentQuest;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Arjun Bhatt on 3/2/2021.
 */

public class QuizVideoParentAdapter extends RecyclerView.Adapter<QuizVideoParentAdapter.ViewHolder> {
    private OnConsultationClickHandler onItemClickHandler;
    private OnOptionClickListener onOptionClickListener = null;
    private OnVideoOptionClickListener onVideoOptionClickListener = null;
    private final Context mContext;
    private List<VideoDependentQuest> items = null;
    private HashMap<Integer, String> checkItems = null;
    private String strAnswer = "";
    private OnOptionClicked onOptionClicked;
    private int selectedPos = -1;
    private List<QuizAnswer> answerList = null;
    int index = 0;

    public QuizVideoParentAdapter(Context context, OnOptionClicked onOptionClicked) {
        if (items == null) {
            items = new ArrayList<>();
        }
        if (answerList == null){
            answerList = new ArrayList<>();
        }

        if (checkItems == null) {
            checkItems = new HashMap<>();
        }
        this.onOptionClicked = onOptionClicked;
        this.mContext = context;
    }

    @NotNull
    @Override
    public QuizVideoParentAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        LayoutQuizParentAdapterBinding mLayoutQuizParentAdapterBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.layout_quiz_parent_adapter, parent, false);
        return new QuizVideoParentAdapter.ViewHolder(mLayoutQuizParentAdapterBinding);
    }

    @Override
    public void onBindViewHolder(@NotNull QuizVideoParentAdapter.ViewHolder holder, final int position) {
        try {
            holder.mLayoutQuizParentAdapterBinding.txtQuestion.setText(items.get(position).getPuzzleQuestionTitle());
            QuizVideoChildAdapter childAdapter = new QuizVideoChildAdapter(mContext, (position1, option) -> {
                String optionValue = items.get(position).getOptions().get(position1).getOptionValue();

                /*if (true) {
                    String newAppendValue = (checkItems.get(position) != null) ? checkItems.get(position) + "," + optionValue : optionValue;
                    checkItems.put(position, newAppendValue);
                    Log.d("Parent_Position", String.valueOf(position));
                    Log.d("Parent_Position", newAppendValue);
                } else {
                    String newAppendValue = checkItems.get(position);
                    newAppendValue = newAppendValue.replace("," + optionValue, "");
                    newAppendValue = newAppendValue.replace(optionValue, "");
                    checkItems.put(position, newAppendValue);
                    Log.d("Parent_Position", String.valueOf(position));
                    Log.d("Parent_Position", newAppendValue);
                }

                strAnswer = (checkItems.get(position) == null) ? "" : checkItems.get(position);
                Log.d("Parent_Position", "Final Value : " + strAnswer);
                onOptionClicked.onClicked(position, optionValue);*/
            });

            holder.mLayoutQuizParentAdapterBinding.recycleView.setLayoutManager(new LinearLayoutManager(mContext));
            holder.mLayoutQuizParentAdapterBinding.recycleView.setHasFixedSize(true);
            holder.mLayoutQuizParentAdapterBinding.recycleView.setClipToPadding(false);
            if (items.get(position).getOptions() != null && items.get(position).getOptions().size() > 0) {
                childAdapter.addData(items.get(position).getOptions(), items.get(position).getPuzzleQuestionSelectionType(), answerList);
                Log.d("Parent_Position-Radio", "Final Value : " + strAnswer);
            }
            holder.mLayoutQuizParentAdapterBinding.recycleView.setAdapter(childAdapter);

            childAdapter.setOnVideoOptionClickListener(((position1, quizOption, title, optionType, childHolder) -> {
                //Log.d("Listener", position1 + "" + quizOption + "" + optionType);
                onVideoOptionClickListener.onItemClick(position1, quizOption, holder.mLayoutQuizParentAdapterBinding.txtQuestion.getText().toString().trim(), optionType, childHolder);
//                childAdapter.updateAnswers(answerList);
            }));
            /*childAdapter.setOnOptionClickListener(((position1, quizOption, title, optionType) -> {
                //Log.d("Listener", position1 + "" + quizOption + "" + optionType);
                onOptionClickListener.onItemClick(position1, quizOption, holder.mLayoutQuizParentAdapterBinding.txtQuestion.getText().toString().trim());
//                childAdapter.updateAnswers(answerList);
            }));*/
            childAdapter.setOnItemClickHandler(positionChild -> {
                if (items.get(position).getPuzzleQuestionSelectionType().equals("Radio")) {
                    //onOptionClicked.onClicked(position, childAdapter.getItem(position).getOptionTitle());
                    childAdapter.getItem(positionChild).setIsSelected(true);
                    //childAdapter.notifyItemChanged(position);

                } else {
                    //childAdapter.notifyItemChanged(position);
                    //onOptionClicked.onClicked(position, strAnswer);
                    childAdapter.getItem(positionChild).setIsSelected(true);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setOnItemClickHandler(OnConsultationClickHandler onItemClickHandler) {
        this.onItemClickHandler = onItemClickHandler;
    }

    public void setOnOptionClicked(OnOptionClicked onOptionClicked) {
        this.onOptionClicked = onOptionClicked;
    }

    public void setOnOptionClickListener(OnOptionClickListener onOptionClickListener){
        this.onOptionClickListener = onOptionClickListener;
    }

    public void setOnVideoOptionClickListener(OnVideoOptionClickListener onVideoOptionClickListener){
        this.onVideoOptionClickListener = onVideoOptionClickListener;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public VideoDependentQuest getItem(int position) {
        return items.get(position);
    }

    public void setData(VideoDependentQuest data, List<QuizAnswer> correctAnswers) {
        items.clear();
        items.add(data);
        answerList.addAll(correctAnswers);
    }

    public void updateAnswers(List<QuizAnswer> correctAnswers){
        answerList.clear();
        answerList.addAll(correctAnswers);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final LayoutQuizParentAdapterBinding mLayoutQuizParentAdapterBinding;

        public ViewHolder(LayoutQuizParentAdapterBinding mLayoutQuizParentAdapterBinding) {
            super(mLayoutQuizParentAdapterBinding.getRoot());
            this.mLayoutQuizParentAdapterBinding = mLayoutQuizParentAdapterBinding;
        }
    }

    public interface OnOptionClicked {
        void onClicked(int position, String option);
    }
}
