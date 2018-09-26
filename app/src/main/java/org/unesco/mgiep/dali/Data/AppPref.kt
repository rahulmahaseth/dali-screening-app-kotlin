package org.unesco.mgiep.dali.Data

import android.content.Context
import com.chibatching.kotpref.KotprefModel

class AppPref(context: Context): KotprefModel(context) {
    var locale by stringPref("en")
    var loading by booleanPref(false)
}