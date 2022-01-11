package com.ab.hicarerun.network.models.tmsmodel

import com.google.gson.annotations.SerializedName


data class QuestionOption (
	@SerializedName("OptionDisplayText") val optionDisplayText : String?,
	@SerializedName("OptionText") val optionText : String?,
	@SerializedName("IsSelected") var isSelected : Boolean?
)