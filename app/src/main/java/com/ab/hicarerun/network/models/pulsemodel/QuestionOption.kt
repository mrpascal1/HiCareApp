package com.ab.hicarerun.network.models.pulsemodel

import com.google.gson.annotations.SerializedName


data class QuestionOption (

	@SerializedName("OptionDisplayText") val optionDisplayText : String?,
	@SerializedName("OptionText") val optionText : String?,
	@SerializedName("IsSelected") val isSelected : Boolean?,
	@SerializedName("IsSubQuestion") val isSubQuestion : Boolean?,
	@SerializedName("SubQuestionList") val subQuestionList : List<SubQuestionList>?
)