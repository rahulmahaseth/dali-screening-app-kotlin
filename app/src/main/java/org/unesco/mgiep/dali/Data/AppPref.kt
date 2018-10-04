package org.unesco.mgiep.dali.Data

import android.content.Context
import com.chibatching.kotpref.KotprefModel

class AppPref(context: Context): KotprefModel(context) {
    var loggedIn by booleanPref(false)
    var locale by stringPref("en")
    var userEmail by stringPref("")
    var userName by stringPref("")
    var userInstitution by stringPref("")
    var l2 by stringPref("")
    var l3 by stringPref("")
    var assessmentLanguage by stringPref("")
}