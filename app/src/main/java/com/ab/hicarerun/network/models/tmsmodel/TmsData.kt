package com.ab.hicarerun.network.models.tmsmodel

import com.google.gson.annotations.SerializedName

data class TmsData (
    @SerializedName("TaskId") val taskId : String?,
    @SerializedName("Type") val type : String?,
    @SerializedName("QuestionTabList") val questionTabList : List<QuestionTabList>?
)
