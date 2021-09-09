package com.ab.hicarerun.network.models.TSScannerModel

import com.google.gson.annotations.SerializedName

data class Data (
	@SerializedName("AccountNo") val accountNo : String?,
	@SerializedName("OrderNo") val orderNo : Long?,
	@SerializedName("AccountName") val accountName : String?,
	@SerializedName("StartDate") val startDate : String?,
	@SerializedName("EndDate") val endDate : String?,
	@SerializedName("RegionName") val regionName : String?,
	@SerializedName("ServiceGroup") val serviceGroup : String?,
	@SerializedName("ServicePlan") val servicePlan : String?,
	@SerializedName("BarcodeType") val barcodeType : List<BarcodeType>?,
	@SerializedName("BarcodeList") val barcodeList : List<BarcodeList>?
)