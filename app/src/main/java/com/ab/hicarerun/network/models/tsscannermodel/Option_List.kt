package com.ab.hicarerun.network.models.tsscannermodel

import com.google.gson.annotations.SerializedName

data class Option_List(
    @SerializedName("OptionId") var id: Int?,
    @SerializedName("Text") val text: String?,
    @SerializedName("Value") val value: String?,
)
