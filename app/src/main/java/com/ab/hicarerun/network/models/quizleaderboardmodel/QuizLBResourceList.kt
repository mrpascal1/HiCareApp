package com.ab.hicarerun.network.models.quizleaderboardmodel

import com.google.gson.annotations.SerializedName

data class QuizLBResourceList(
    @SerializedName("LevelName") val levelName : String?,
    @SerializedName("LevelNameDisplay") val levelNameDisplay : String?,
    @SerializedName("LevelIcon") val levelIcon : String?,
    @SerializedName("ResourceId") val resourceId : String?,
    @SerializedName("ResourceName") val resourceName : String?,
    @SerializedName("IsSelf") val isSelf : Boolean?,
    @SerializedName("ResourceRank") val resourceRank : Int?,
    @SerializedName("Points") val points : Int?,
    @SerializedName("LastPlayedOn") val lastPlayedOn : String?,
    @SerializedName("LastPlayedOnDisplay") val lastPlayedOnDisplay : String?,
    var highest : Int?
)