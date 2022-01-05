package com.ab.hicarerun.network.models.InventoryModel

import com.google.gson.annotations.SerializedName

data class Data (
	@SerializedName("TechnicianList") val technicianList : List<TechnicianList>?,
	@SerializedName("ActionList") val actionList : List<ActionList>?
)