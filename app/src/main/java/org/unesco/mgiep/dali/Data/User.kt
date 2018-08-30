package org.unesco.mgiep.dali.Data

data class User (
        val email: String,
        val name: String,
        val designation: String,
        val institution: String?,
        val age: Int,
        val gender: String
)