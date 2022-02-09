package com.ab.hicarerun.network.models.pulsemodel

import com.google.gson.annotations.SerializedName

data class PulseResponse (
	@SerializedName("IsSuccess") val isSuccess : Boolean?,
	@SerializedName("Data") val data : Data?,
	@SerializedName("ErrorMessage") val errorMessage : String?,
	@SerializedName("Param1") val param1 : Boolean?,
	@SerializedName("ResponseMessage") val responseMessage : String?
)