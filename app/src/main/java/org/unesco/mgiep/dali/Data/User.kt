package org.unesco.mgiep.dali.Data

data class User (
        var email: String = "",
        var name: String = "",
        var designation: String = "",
        var institution: String = "",
        var age: Int = 0,
        var gender: String = ""
)