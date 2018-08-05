package org.unesco.mgiep.dali.Data

data class Teacher (
        val email: String,
        val name: String,
        val password: String,
        val confirmPassword: String,
        val institution: String,
        val age: Int,
        val male: Boolean,
        val female: Boolean
)