package com.ab.hicarerun.network.models.TSScannerModel

import com.google.gson.annotations.SerializedName

data class OrderDetails (
	@SerializedName("IsSuccess") val isSuccess : Boolean?,
	@SerializedName("com.ab.hicarerun.network.models.TSScannerModel.Data") val data : Data?,
	@SerializedName("ErrorMessage") val errorMessage : String?,
	@SerializedName("Param1") val param1 : Boolean?,
	@SerializedName("ResponseMessage") val responseMessage : String?
)