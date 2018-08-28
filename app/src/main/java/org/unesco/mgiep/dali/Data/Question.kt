package org.unesco.mgiep.dali.Data

data class Question (
        val category: String,
        val description: String,
        val example: String,
        val score: Int,
        val completed: Boolean
)