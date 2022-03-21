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
	@SerializedName("QuestionStrOption") val questionStrOption : ArrayList<String>?,
	@SerializedName("Answer") var answer : String?,
	@SerializedName("QuestionType") val questionType : String?,
	@SerializedName("IsPictureRequired") val isPictureRequired : Boolean?,
	@SerializedName("IsDisabled") val isDisabled : Boolean?,
	@SerializedName("IsDependentQuestion") val isDependentQuestion : Boolean?,
	@SerializedName("PictureURL") var pictureURL : ArrayList<String>?,
	@SerializedName("TabName") val tabName : String?,
	@SerializedName("TabIndex") val tabIndex : Int?
)