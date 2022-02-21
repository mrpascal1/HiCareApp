package com.ab.hicarerun.network.models.inventorymodel.inventorylistmodel

import com.google.gson.annotations.SerializedName

data class InventoryListResult (
	@SerializedName("IsSuccess") val isSuccess : Boolean?,
	@SerializedName("Data") val data : List<InventoryListData>?,
	@SerializedName("ErrorMessage") val errorMessage : String?,
	@SerializedName("Param1") val param1 : Boolean?,
	@SerializedName("ResponseMessage") val responseMessage : String?
)