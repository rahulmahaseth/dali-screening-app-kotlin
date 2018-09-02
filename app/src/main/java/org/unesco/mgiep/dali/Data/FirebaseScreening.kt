package org.unesco.mgiep.dali.Data

import java.util.*

data class FirebaseScreening (
        val type: Type,
        val totalQuestions: Int,
        var questionsCompleted: Int,
        var completed: Boolean,
        val mediumOfInstruction: String,
        val participantId: String,
        val userId: String,
        val ScheduledDate: Date,
        var totalScore: Int
)