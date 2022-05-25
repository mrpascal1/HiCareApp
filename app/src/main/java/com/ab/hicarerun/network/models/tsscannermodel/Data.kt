package com.ab.hicarerun.network.models.tsscannermodel

import com.google.gson.annotations.SerializedName

data class Data (
	@SerializedName("AccountNo") val accountNo : String?,
	@SerializedName("OrderNo") val orderNo : String?,
	@SerializedName("AccountName") val accountName : String?,
	@SerializedName("StartDate") val startDate : String?,
	@SerializedName("EndDate") val endDate : String?,
	@SerializedName("RegionName") val regionName : String?,
	@SerializedName("ServiceGroup") val serviceGroup : String?,
	@SerializedName("ServicePlan") val servicePlan : String?,
	@SerializedName("BarcodeType") val barcodeType : List<BarcodeType>?,
	@SerializedName("BarcodeList") val barcodeList : List<BarcodeList>?,
	@SerializedName("Service_Units") val service_Units : List<BarcodeType>?,
	@SerializedName("Additional_Info") val additional_Info : String?
)