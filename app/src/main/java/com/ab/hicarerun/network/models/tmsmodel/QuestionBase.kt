package com.ab.hicarerun.network.models.tmsmodel

import com.google.gson.annotations.SerializedName

data class QuestionBase (
	@SerializedName("IsSuccess") val isSuccess : Boolean?,
	@SerializedName("Data") val data : List<TmsData>?,
	@SerializedName("ErrorMessage") val errorMessage : String?,
	@SerializedName("Param1") val param1 : Boolean?,
	@SerializedName("ResponseMessage") val responseMessage : String?
)