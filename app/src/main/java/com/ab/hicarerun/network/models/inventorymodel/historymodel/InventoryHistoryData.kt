package com.ab.hicarerun.network.models.inventorymodel.historymodel

import com.google.gson.annotations.SerializedName

data class InventoryHistoryData (
	@SerializedName("Id") val id : Int?,
	@SerializedName("Inventory_id") val inventory_id : String?,
	@SerializedName("Bucket_Id") val bucket_Id : Int?,
	@SerializedName("Bucket_Name") val bucket_Name : String?,
	@SerializedName("Assigned_Type") val assigned_Type : String?,
	@SerializedName("Assigned_To") val assigned_To : String?,
	@SerializedName("Ref_Id") val ref_Id : String?,
	@SerializedName("Assigned_Date") val assigned_Date : String?,
	@SerializedName("Assigned_Date_Formated") val assigned_Date_Formated : String?,
	@SerializedName("Status") val status : String?,
	@SerializedName("Created_By") val created_By : String?
)