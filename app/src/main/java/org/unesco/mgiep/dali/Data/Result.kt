package org.unesco.mgiep.dali.Data

import java.util.*

data class Result (
    val questionWiseScore: List<Question>,
    val totalScroe: Int,
    val comments: String,
    val atRisk: Boolean,
    val screeningId: String,
    val participantId: String,
    val generatedOn: Date
)