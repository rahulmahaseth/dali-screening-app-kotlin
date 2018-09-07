package org.unesco.mgiep.dali.Data


data class Participant (
        val name: String = "",
        val sClass: Int = 0,
        val motherTongue: String = "",
        val institution: String = "",
        val dob: Long = 0,
        val gender: String = "",
        val relationShipWithChild: String = "",
        val timeSpentWithChild: Int = 0,
        val createdBy: String = ""
)