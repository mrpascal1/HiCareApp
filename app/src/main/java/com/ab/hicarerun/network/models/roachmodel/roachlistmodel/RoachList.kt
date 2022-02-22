package com.ab.hicarerun.network.models.roachmodel.roachlistmodel

import com.google.gson.annotations.SerializedName

data class RoachList (
	@SerializedName("id") val id : String?,
	@SerializedName("deviceName") val deviceName : String?,
	@SerializedName("deviceDisplayName") val deviceDisplayName : String?,
	@SerializedName("deployedLocation") val deployedLocation : String?,
	@SerializedName("servicecentre") val servicecentre : String?,
	@SerializedName("region") val region : String?,
	@SerializedName("createdOn") val createdOn : String?,
	@SerializedName("createdOnDisplay") val createdOnDisplay : String?,
	@SerializedName("accountNo") val accountNo : String?,
	@SerializedName("isActive") val isActive : Boolean?,
	@SerializedName("isDeviceUpdateDone") var isDeviceUpdateDone : Boolean?
)