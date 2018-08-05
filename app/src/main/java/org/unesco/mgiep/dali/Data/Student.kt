package org.unesco.mgiep.dali.Data

import java.util.*

data class Student (
        val name: String,
        val sClass: Int,
        val Section: String,
        val motherTongue: String,
        val instructionMedium: String,
        val institution: String,
        val dob: Date,
        val boy: Boolean,
        val girl: Boolean
)