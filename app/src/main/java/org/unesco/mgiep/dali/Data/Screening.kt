package org.unesco.mgiep.dali.Data

import java.util.*

data class Screening (
    val participant: Participant,
    val mediumOfInstruction: InstructionMedium,
    val scheduledDate : Date,
    val completed: Boolean,
    val result: Result
)