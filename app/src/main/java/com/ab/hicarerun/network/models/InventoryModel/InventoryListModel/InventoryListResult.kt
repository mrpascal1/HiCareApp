package com.ab.hicarerun.network.models.InventoryModel.InventoryListModel

import com.google.gson.annotations.SerializedName

data class InventoryListResult (
	@SerializedName("IsSuccess") val isSuccess : Boolean?,
	@SerializedName("Data") val data : List<Data>?,
	@SerializedName("ErrorMessage") val errorMessage : String?,
	@SerializedName("Param1") val param1 : Boolean?,
	@SerializedName("ResponseMessage") val responseMessage : String?
)