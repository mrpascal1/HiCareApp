package com.ab.hicarerun.network.models.roachmodel.roachlistmodel

import com.google.gson.annotations.SerializedName

data class RoachBase (
	@SerializedName("isSuccess") val isSuccess : Boolean?,
	@SerializedName("responseData") val responseData : ResponseData?,
	@SerializedName("errorMessage") val errorMessage : String?,
	@SerializedName("uiMessage") val uiMessage : String?
)