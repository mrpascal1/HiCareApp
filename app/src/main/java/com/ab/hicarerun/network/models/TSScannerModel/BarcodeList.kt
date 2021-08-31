package com.ab.hicarerun.network.models.TSScannerModel

import com.google.gson.annotations.SerializedName


data class BarcodeList (
	@SerializedName("Id") val id : Int?,
	@SerializedName("Account_No") val account_No : String?,
	@SerializedName("Order_No") val order_No : String?,
	@SerializedName("Account_Name") val account_Name : String?,
	@SerializedName("Barcode_Data") val barcode_Data : String?,
	@SerializedName("Last_Verified_On") val last_Verified_On : String?,
	@SerializedName("Last_Verified_By") val last_Verified_By : Int?,
	@SerializedName("Created_On") val created_On : String?,
	@SerializedName("Created_By_Id_User") val created_By_Id_User : Int?,
	@SerializedName("Verified_By") val verified_By : String?,
	@SerializedName("Created_By") val created_By : String?,
	@SerializedName("IsVerified") val isVerified : Boolean?,
	@SerializedName("Pest_Type") val pest_Type : List<Pest_Type>?,
	var callForDelete : String?
)