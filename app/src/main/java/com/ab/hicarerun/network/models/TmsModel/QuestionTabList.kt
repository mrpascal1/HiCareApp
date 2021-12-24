package com.ab.hicarerun.network.models.TmsModel

import com.google.gson.annotations.SerializedName


data class QuestionTabList (
	@SerializedName("QuestionTab") val questionTab : String?,
	@SerializedName("TabIndex") val tabIndex : Int?,
	@SerializedName("QuestionDisplayTab") val QuestionDisplayTab : String?,
	@SerializedName("QuestionList") val questionList : List<QuestionList>
)