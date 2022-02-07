package com.ab.hicarerun.network.models.chemicalcmodel;

import com.google.gson.annotations.SerializedName

data class ChemicalConsumption (
	@SerializedName("IsSuccess") val isSuccess : Boolean?,
	@SerializedName("Data") val data : List<Data>?,
	@SerializedName("ResponseMessage") val responseMessage : String?
)