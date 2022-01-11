package com.ab.hicarerun.handler;

import com.ab.hicarerun.adapter.QuizVideoChildAdapter;
import com.ab.hicarerun.network.models.quizmodel.QuizOption;

public interface OnVideoOptionClickListener {
    void onItemClick(int position, QuizOption quizOption, String title, String optionType, QuizVideoChildAdapter.ViewHolder holder);
}
