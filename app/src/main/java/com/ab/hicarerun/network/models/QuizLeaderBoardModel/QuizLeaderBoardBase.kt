package com.ab.hicarerun.network.models.QuizLeaderBoardModel

import com.google.gson.annotations.SerializedName

data class QuizLeaderBoardBase(
    @SerializedName("IsSuccess") val isSuccess : Boolean?,
    @SerializedName("Data") val data : List<QuizLBData>?,
    @SerializedName("ErrorMessage") val errorMessage : String?,
    @SerializedName("Param1") val param1 : Boolean?,
    @SerializedName("ResponseMessage") val responseMessage : String?
)
