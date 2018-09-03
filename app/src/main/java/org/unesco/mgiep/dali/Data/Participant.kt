package org.unesco.mgiep.dali.Data


data class Participant (
        val name: String,
        val sClass: Int,
        val motherTongue: String,
        val institution: String,
        val dob: Long,
        val gender: String
)