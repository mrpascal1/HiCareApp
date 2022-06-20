package com.ab.hicarerun.network.models.chemicalcmodel;

import com.google.gson.annotations.SerializedName

data class Data (
	@SerializedName("ActivityId") val activityId : Int?,
	@SerializedName("ServiceActivityId") val serviceActivityId : Int?,
	@SerializedName("ServiceActivityName") val serviceActivityName : String?,
	@SerializedName("ChemicalId") val chemicalId : Int?,
	@SerializedName("ChemicalName") val chemicalName : String?,
	@SerializedName("ChemicalCode") val chemicalCode : String?,
	@SerializedName("ChemicalQuantity") val chemicalQuantity : Double?,
	@SerializedName("OrderNo") val orderNo : String?,
	@SerializedName("ServiceSequenceNo") val serviceSequenceNo : String?,
	@SerializedName("AreaType") val areaType : String?,
	@SerializedName("FloorNo") val floorNo : String?
)