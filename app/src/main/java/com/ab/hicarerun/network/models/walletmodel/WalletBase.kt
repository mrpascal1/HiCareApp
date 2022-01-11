package com.ab.hicarerun.network.models.walletmodel

import com.google.gson.annotations.SerializedName

data class WalletBase (
	@SerializedName("IsSuccess") val isSuccess : Boolean?,
	@SerializedName("Data") val data : WalletData?,
	@SerializedName("ErrorMessage") val errorMessage : String?,
	@SerializedName("Param1") val param1 : Boolean?,
	@SerializedName("ResponseMessage") val responseMessage : String?
)