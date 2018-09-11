package org.unesco.mgiep.dali.Data

import com.chibatching.kotpref.KotprefModel

object AppPref: KotprefModel() {
    var loggedIn by booleanPref(false)
    var name by stringPref("")
    var designation by stringPref("")
}