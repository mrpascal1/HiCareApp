package com.ab.hicarerun.network.models.pulsemodel

import com.ab.hicarerun.network.models.tmsmodel.QuestionTabList
import com.google.gson.annotations.SerializedName


data class PulseData (
	@SerializedName("TaskId") val taskId : String?,
	@SerializedName("Type") val type : String?,
	@SerializedName("QuestionTabList") val questionTabList : List<QuestionTabList>?,
	@SerializedName("IsTabList") val isTabList : Boolean?,
	@SerializedName("QuestionList") val questionList : List<QuestionList>?
)