package org.unesco.mgiep.dali.Data

data class Screening (
    val participant: Participant,
    val mediumOfInstruction: InstructionMedium,
    val scheduledDate : Long,
    val completed: Boolean,
    val result: Result
)