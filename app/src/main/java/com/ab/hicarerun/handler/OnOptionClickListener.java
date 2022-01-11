package com.ab.hicarerun.handler;

import com.ab.hicarerun.adapter.QuizOptionAdapter;
import com.ab.hicarerun.network.models.quizmodel.QuizOption;

public interface OnOptionClickListener{
    //void onItemClick(int position, QuizOption quizOption, String title, String optionType);
    void onItemClick(int position, QuizOption quizOption, String givenAnswersIds, String title, String optionType, QuizOptionAdapter.ViewHolder holder);
}
