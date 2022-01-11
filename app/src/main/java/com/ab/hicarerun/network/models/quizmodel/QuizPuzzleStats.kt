package com.ab.hicarerun.network.models.quizmodel

import com.google.gson.annotations.SerializedName

data class QuizPuzzleStats(
    @SerializedName("IsSuccess") val isSuccess : Boolean,
    @SerializedName("Data") val data : QuizPuzzleStatsData,
    @SerializedName("ErrorMessage") val errorMessage : String,
    @SerializedName("Param1") val param1 : Boolean,
    @SerializedName("ResponseMessage") val responseMessage : String
)
