package com.ab.hicarerun.network.models.TmsModel

data class QuestionsResponse(
    val category: String? = "",
    val questionList: ArrayList<Questions>?,
)

data class Questions(
    val question: String? = "",
    val optionList: ArrayList<Option>?,
    var selected: Boolean
)

data class Option(
    val id: Int,
    val option: String = "",
    var selected: Boolean
)