package com.ab.hicarerun.network.models.tsscannermodel

import com.google.gson.annotations.SerializedName

/**
 * Created by Arjun Bhatt on 7/10/2021.
 */

data class BarcodeDetailsResponse (
        @SerializedName("IsSuccess") val isSuccess : Boolean?,
        @SerializedName("Data") val data : List<BarcodeDetailsData>?,
        @SerializedName("ErrorMessage") val errorMessage : String?,
        @SerializedName("Param1") val param1 : Boolean?,
        @SerializedName("ResponseMessage") val responseMessage : String?
)
