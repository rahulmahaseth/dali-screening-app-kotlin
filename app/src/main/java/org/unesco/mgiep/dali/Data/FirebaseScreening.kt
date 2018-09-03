package org.unesco.mgiep.dali.Data

data class FirebaseScreening (
        val type: String,
        var completed: Boolean,
        val mediumOfInstruction: String,
        val participantId: String,
        val userId: String,
        val scheduledDate: Long,
        var totalScore: Int,
        var comments: String
)