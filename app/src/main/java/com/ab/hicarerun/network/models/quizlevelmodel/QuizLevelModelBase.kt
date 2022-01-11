package com.ab.hicarerun.network.models.quizlevelmodel

import com.google.gson.annotations.SerializedName

data class QuizLevelModelBase(
    @SerializedName("IsSuccess") val isSuccess : Boolean?,
    @SerializedName("Data") val data : List<QuizLevelData>?,
    @SerializedName("ErrorMessage") val errorMessage : String?,
    @SerializedName("Param1") val param1 : Boolean?,
    @SerializedName("ResponseMessage") val responseMessage : String?
)
