package org.unesco.mgiep.dali.Data

data class FirebaseScreening (
        val type: String,
        val totalQuestions: Int,
        var questionsCompleted: Int,
        var completed: Boolean,
        val mediumOfInstruction: String,
        val participantId: String,
        val userId: String,
        val scheduledDate: Long,
        var totalScore: Int
)