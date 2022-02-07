package com.ab.hicarerun.network.models.inventorymodel.InventoryListModel

import com.google.gson.annotations.SerializedName

data class Data (
	@SerializedName("Id") val id : Int?,
	@SerializedName("Inventory_Code") val inventory_Code : String?,
	@SerializedName("Item_Code") val item_Code : String?,
	@SerializedName("Manufacturing_Date") val manufacturing_Date : String?,
	@SerializedName("Manufacturing_Date__Date") val manufacturing_Date__Date : String?,
	@SerializedName("Expiry_Date") val expiry_Date : String?,
	@SerializedName("Expiry_Date__Date") val expiry_Date__Date : String?,
	@SerializedName("Make") val make : String?,
	@SerializedName("Model") val model : String?,
	@SerializedName("Barcode_Data") val barcode_Data : String?,
	@SerializedName("Used_Count") val used_Count : Int?,
	@SerializedName("Status") val status : String?,
	@SerializedName("Last_Bucket_Id") val last_Bucket_Id : Int?,
	@SerializedName("Service_Center_Id") val service_Center_Id : String?,
	@SerializedName("Service_Center_Code") val service_Center_Code : String?,
	@SerializedName("Region_Name") val region_Name : String?,
	@SerializedName("SC_Region_Name") val sC_Region_Name : String?,
	@SerializedName("Bucket_Name") val bucket_Name : String?,
	@SerializedName("Is_Active") val is_Active : Boolean?,
	@SerializedName("Created_On") val created_On : String?,
	@SerializedName("Created_By") val created_By : String?,
	@SerializedName("Updated_On") val updated_On : String?,
	@SerializedName("Updated_By") val updated_By : String?
)