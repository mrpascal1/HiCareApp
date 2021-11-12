package com.ab.hicarerun.network.models.TmsModel

import com.google.gson.annotations.SerializedName


data class QuestionList (
	@SerializedName("QuestionId") val questionId : Int?,
	@SerializedName("QuestionText") val questionText : String?,
	@SerializedName("QuestionOption") val questionOption : List<QuestionOption>?,
	@SerializedName("Answer") var answer : String?,
	@SerializedName("QuestionType") val questionType : String?,
	@SerializedName("IsPictureRequired") val isPictureRequired : Boolean?,
	@SerializedName("PictureURL") var pictureURL : String?
)