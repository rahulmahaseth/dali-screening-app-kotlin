package org.unesco.mgiep.dali.Data

import android.content.Context
import com.chibatching.kotpref.KotprefModel

class AppPref(context: Context): KotprefModel(context) {
    var locale by stringPref("en")
    var userEmail by stringPref("")
    var userName by stringPref("")
    var userDesignation by stringPref("")
    var userInstitution by stringPref("")
    var userAge by intPref(0)
}