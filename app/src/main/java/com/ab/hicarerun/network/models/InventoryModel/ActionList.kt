package com.ab.hicarerun.network.models.InventoryModel

import com.google.gson.annotations.SerializedName

data class ActionList (
	@SerializedName("Id") val id : Int?,
	@SerializedName("Bucket_Id") val bucket_Id : Int?,
	@SerializedName("Reasons") val reasons : String?,
	@SerializedName("Is_Active") val is_Active : Boolean?,
	@SerializedName("Allocate_Bucket_Id") val allocate_Bucket_Id : Int?
)