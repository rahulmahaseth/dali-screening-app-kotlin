package org.unesco.mgiep.dali.Data

data class Result (
    val question: Question,
    val totalScroe: Int,
    val comments: String,
    val atRisk: Boolean
)