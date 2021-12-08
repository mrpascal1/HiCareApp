package com.ab.hicarerun.network.models.TSScannerModel.counts

import com.google.gson.annotations.SerializedName

data class CountsResponse (
    @SerializedName("IsSuccess") val isSuccess : Boolean?,
    @SerializedName("Data") val data : CountsData?,
    @SerializedName("ErrorMessage") val errorMessage : String?,
    @SerializedName("Param1") val param1 : Boolean?,
    @SerializedName("ResponseMessage") val responseMessage : String?
)