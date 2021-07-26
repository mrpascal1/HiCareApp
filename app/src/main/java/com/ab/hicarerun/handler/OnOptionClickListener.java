package com.ab.hicarerun.handler;

import com.ab.hicarerun.network.models.QuizModel.QuizOption;

public interface OnOptionClickListener{
    void onItemClick(int position, QuizOption quizOption, String optionType);
}
