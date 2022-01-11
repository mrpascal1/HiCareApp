package com.ab.hicarerun.network.models.walletmodel

import com.google.gson.annotations.SerializedName

data class HistoryList (
	@SerializedName("description") val description : String?,
	@SerializedName("eventName") val eventName : String?,
	@SerializedName("scoredFor") val scoredFor : String?,
	@SerializedName("points") val points : Int?,
	@SerializedName("scoreDate") val scoreDate : String?
)