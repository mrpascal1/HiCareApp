package com.ab.hicarerun.network.models.QuizLeaderBoardModel

import com.google.gson.annotations.SerializedName

data class QuizLBData(
    @SerializedName("LevelName") val levelName : String?,
    @SerializedName("ResourceList") val resourceList : List<QuizLBResourceList>?
)
