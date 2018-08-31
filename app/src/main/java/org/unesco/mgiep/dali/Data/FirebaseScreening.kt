package org.unesco.mgiep.dali.Data

import java.util.*

data class FirebaseScreening (
        val type: Type,
        val totalQuestions: Int,
        val questionsCompleted: Int,
        val completed: Boolean,
        val mediumOfInstruction: String,
        val participantId: String,
        val userId: String,
        val ScheduledDate: Date
)