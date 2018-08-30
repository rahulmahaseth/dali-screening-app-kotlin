package org.unesco.mgiep.dali.Data

import java.util.*

data class FirebaseScreening (
        val completed: Boolean,
        val mediumOfInstruction: String,
        val participantId: String,
        val ScheduledDate: Date,
        val userId: String
)