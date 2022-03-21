package com.ab.hicarerun.network.models.roachmodel.roachlistmodel

import com.google.gson.annotations.SerializedName

data class ResponseData (
	@SerializedName("roachList") val roachList : List<RoachList>?,
	@SerializedName("locationList") val locationList : List<String>?
)