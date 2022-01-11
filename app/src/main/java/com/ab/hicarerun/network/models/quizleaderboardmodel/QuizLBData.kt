package com.ab.hicarerun.network.models.quizleaderboardmodel

import com.google.gson.annotations.SerializedName

data class QuizLBData(
    @SerializedName("LevelName") val levelName : String?,
    @SerializedName("LevelNameDisplay") val levelNameDisplay : String?,
    @SerializedName("LevelIcon") val levelIcon : String?,
    @SerializedName("ResourceList") val resourceList : List<QuizLBResourceList>?
)
