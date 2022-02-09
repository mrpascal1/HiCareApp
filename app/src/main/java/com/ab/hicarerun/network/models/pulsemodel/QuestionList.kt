package com.ab.hicarerun.network.models.pulsemodel

import com.google.gson.annotations.SerializedName


data class QuestionList (
	@SerializedName("QuestionId") val questionId : Int?,
	@SerializedName("Id") val id : Int?,
	@SerializedName("QuestionText") val questionText : String?,
	@SerializedName("QuestionImageUrl") val questionImageUrl : String?,
	@SerializedName("QuestionDisplayText") val questionDisplayText : String?,
	@SerializedName("QuestionAudioUrl") val questionAudioUrl : String?,
	@SerializedName("QuestionOption") val questionOption : List<QuestionOption>?,
	@SerializedName("QuestionStrOption") val questionStrOption : List<String>?,
	@SerializedName("Answer") val answer : String?,
	@SerializedName("QuestionType") val questionType : String?,
	@SerializedName("IsPictureRequired") val isPictureRequired : Boolean?,
	@SerializedName("IsDisabled") val isDisabled : Boolean?,
	@SerializedName("PictureURL") val pictureURL : List<String>?,
	@SerializedName("TabName") val tabName : String?,
	@SerializedName("TabIndex") val tabIndex : Int?
)