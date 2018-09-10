package org.unesco.mgiep.dali.Data


data class Participant (
        var name: String = "",
        var sClass: Int = 0,
        var motherTongue: String = "",
        var institution: String = "",
        var dob: Long = 0,
        var gender: String = "",
        var relationShipWithChild: String = "",
        var timeSpentWithChild: Int = 0,
        val createdBy: String = ""
)