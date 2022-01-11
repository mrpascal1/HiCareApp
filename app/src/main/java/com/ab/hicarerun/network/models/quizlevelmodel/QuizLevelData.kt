package com.ab.hicarerun.network.models.quizlevelmodel

import com.google.gson.annotations.SerializedName

data class QuizLevelData(
    @SerializedName("Id") val id : Int,
    @SerializedName("LevelName") val levelName : String,
    @SerializedName("PointsInfo") val pointsInfo : String
)
