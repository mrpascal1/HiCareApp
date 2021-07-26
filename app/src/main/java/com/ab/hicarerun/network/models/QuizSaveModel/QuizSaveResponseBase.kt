package com.ab.hicarerun.network.models.QuizSaveModel

import com.google.gson.annotations.SerializedName

data class QuizSaveResponseBase(
    @SerializedName("IsSuccess") val isSuccess : Boolean?,
    @SerializedName("Data") val data : QuizSaveResponseData?,
    @SerializedName("ErrorMessage") val errorMessage : String?,
    @SerializedName("Param1") val param1 : Boolean?,
    @SerializedName("ResponseMessage") val responseMessage : String?
)
