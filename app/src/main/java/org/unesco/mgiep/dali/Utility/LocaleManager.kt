package org.unesco.mgiep.dali.Utility

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import org.unesco.mgiep.dali.Data.AppPref
import java.util.*

class LocaleManager {

    fun setLocale(c: Context): Context {
        return setNewLocale(c, getLanguage(c))
    }

    fun setNewLocale(c: Context, language: String): Context {
        return updatedResources(c, language)
    }

    private fun getLanguage(c: Context): String{
        return AppPref.locale
    }

    private fun updatedResources(context: Context, language: String):Context{
        var c= context
        val locale = Locale(language)
        Locale.setDefault(locale)

        val res = c.resources
        val config = Configuration(res.configuration)


        if (Build.VERSION.SDK_INT >= 17) {
            config.setLocale(locale)
            c = c.createConfigurationContext(config)
        } else {
            config.locale = locale
            res.updateConfiguration(config, res.displayMetrics)
        }
        return c
    }
}