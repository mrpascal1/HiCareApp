package com.ab.hicarerun.network.models.roachmodel.saveroachmodel

import com.google.gson.annotations.SerializedName

data class RoachSaveBase (
    @SerializedName("isSuccess") val isSuccess : Boolean?,
    @SerializedName("responseData") val responseData : String?,
    @SerializedName("errorMessage") val errorMessage : String?,
    @SerializedName("uiMessage") val uiMessage : String?
)