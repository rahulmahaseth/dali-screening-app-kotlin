package org.unesco.mgiep.dali.Data

data class Screening (
        var id: String = "",
        val type: String = Type.JST.toString(),
        var completed: Boolean = false,
        val mediumOfInstruction: String = "English",
        val participantId: String = "",
        val participantName: String = "",
        val userId: String = "",
        val scheduledDate: Long = 0,
        var totalScore: Int = 0,
        var comments: String = "",
        var assesmentLanguage: String
)