package com.ab.hicarerun.network.models.tmsmodel

import com.google.gson.annotations.SerializedName


data class QuestionList (
	@SerializedName("QuestionId") val questionId : Int?,
	@SerializedName("QuestionText") val questionText : String?,
	@SerializedName("QuestionOption") val questionOption : List<QuestionOption>?,
	@SerializedName("QuestionStrOption") val questionStrOption : ArrayList<String>?,
	@SerializedName("QuestionImageUrl") val questionImageUrl : String?,
	@SerializedName("QuestionDisplayText") val questionDisplayText : String?,
	@SerializedName("QuestionAudioUrl") val questionAudioUrl : String?,
	@SerializedName("Answer") var answer : String?,
	@SerializedName("IsDisabled") var isDisabled : Boolean,
	@SerializedName("QuestionType") val questionType : String?,
	@SerializedName("IsPictureRequired") val isPictureRequired : Boolean?,
	@SerializedName("PictureURL") var pictureURL : ArrayList<String>?,
	@SerializedName("TabName") var tabName : String?,
	@SerializedName("TabIndex") var tabIndex : Int?,
	var qPictureURL : ArrayList<QuestionImageUrl>?
)