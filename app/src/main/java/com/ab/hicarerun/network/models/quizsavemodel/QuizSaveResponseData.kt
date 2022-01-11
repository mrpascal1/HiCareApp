package com.ab.hicarerun.network.models.quizsavemodel

import com.google.gson.annotations.SerializedName

data class QuizSaveResponseData(
    @SerializedName("ResourceMessage") val resourceMessage : String?,
    @SerializedName("ResourceId") val resourceId : String?,
    @SerializedName("TotalPoints") val totalPoints : Int?,
    @SerializedName("CurrentLevelId") val currentLevelId : Int?,
    @SerializedName("UpgradedLevelId") val upgradedLevelId : Int?,
    @SerializedName("CurrentLevelName") val currentLevelName : String?,
    @SerializedName("CurrentIconUrl") val currentIconUrl : String?,
    @SerializedName("UpgradedLevelName") val upgradedLevelName : String?,
    @SerializedName("UpgradedIconUrl") val upgradedIconUrl : String?
)
