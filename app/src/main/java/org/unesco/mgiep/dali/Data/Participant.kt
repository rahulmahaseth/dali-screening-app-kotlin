package org.unesco.mgiep.dali.Data

import java.util.*

data class Participant (
        val name: String,
        val sClass: Int,
        val section: String = "",
        val motherTongue: String,
        val institution: String,
        val dob: Date,
        val gender: Gender
)