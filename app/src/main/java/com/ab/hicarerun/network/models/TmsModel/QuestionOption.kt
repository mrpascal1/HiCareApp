package com.ab.hicarerun.network.models.TmsModel

import com.google.gson.annotations.SerializedName


data class QuestionOption (
	@SerializedName("OptionText") val optionText : String?,
	@SerializedName("IsSelected") var isSelected : Boolean?
)