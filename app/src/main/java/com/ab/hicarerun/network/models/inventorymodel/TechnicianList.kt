package com.ab.hicarerun.network.models.inventorymodel

import com.google.gson.annotations.SerializedName

data class TechnicianList (
	@SerializedName("TechnicianId") val technicianId : String?,
	@SerializedName("TechnicianName") val technicianName : String?,
	@SerializedName("EmployeeCode") val employeeCode : String?,
	@SerializedName("MobileNo") val mobileNo : String?
)