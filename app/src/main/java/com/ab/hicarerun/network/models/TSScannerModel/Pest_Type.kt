package com.ab.hicarerun.network.models.TSScannerModel

import com.google.gson.annotations.SerializedName

data class Pest_Type (
    @SerializedName("Text") val text : String,
    @SerializedName("Value") val value : String
)
