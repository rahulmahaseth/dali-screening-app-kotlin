package org.unesco.mgiep.dali.Data

import android.content.Context
import com.chibatching.kotpref.KotprefModel

class AppPref(context: Context): KotprefModel(context) {
    var loading by booleanPref(false)
    var userEmail by stringPref("")
    var userName by stringPref("")
    var userInstitution by stringPref("")
    var assessmentLanguage by stringPref("")
}