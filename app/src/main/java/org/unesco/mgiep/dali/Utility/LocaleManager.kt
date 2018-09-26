package org.unesco.mgiep.dali.Utility

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.util.Log
import org.unesco.mgiep.dali.Data.AppPref
import java.util.*
import android.R.id.edit
import android.preference.PreferenceManager
import android.content.SharedPreferences
import android.annotation.SuppressLint



class LocaleManager {

    val LANGUAGE_ENGLISH = "en"
    val LANGUAGE_HINDI = "hi"
    private val LANGUAGE_KEY = "language_key"


    fun setLocale(c: Context?): Context {
        Log.d("setLocale","${getLanguage(c!!)}")
        return setNewLocale(c!!, getLanguage(c)!!)
    }

    fun setNewLocale(c: Context, language: String): Context {
        return updatedResources(c, language)
    }

    fun getLanguage(c: Context): String? {
        val prefs = PreferenceManager.getDefaultSharedPreferences(c)
        return prefs.getString(LANGUAGE_KEY, LANGUAGE_ENGLISH)
    }

    @SuppressLint("ApplySharedPref")
    fun persistLanguage(c: Context, language: String) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(c)
        prefs.edit().putString(LANGUAGE_KEY, language).commit()
    }

    private fun updatedResources(context: Context, language: String):Context{
        Log.d("updatedResources","$language")
        var c= context
        val locale = Locale(language)
        Locale.setDefault(locale)

        val res = context.resources
        val config = Configuration(res.configuration)


        if (Build.VERSION.SDK_INT >= 17) {config.setLocale(locale)
            config.setLocale(locale)
            c = context.createConfigurationContext(config)
        } else {
            config.locale = locale
            res.updateConfiguration(config, res.displayMetrics)
        }
        return c
    }

    fun getLocale(res: Resources): Locale{
        val config = res.configuration
        return if(Build.VERSION.SDK_INT >= 24) config.locales[0] else  config.locale
    }
}