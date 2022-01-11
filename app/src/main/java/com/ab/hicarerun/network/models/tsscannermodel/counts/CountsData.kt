package com.ab.hicarerun.network.models.tsscannermodel.counts

import com.google.gson.annotations.SerializedName

data class CountsData(
    @SerializedName("TotalScanned") val totalScanned: Int?,
    @SerializedName("Deployed") val deployed: Int?
)
