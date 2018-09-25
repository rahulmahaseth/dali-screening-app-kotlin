package org.unesco.mgiep.dali.Utility

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.util.Log
import org.unesco.mgiep.dali.Data.AppPref
import java.util.*

class LocaleManager {

    val langEnglish = "en"
    val langHindi = "hi"

    fun setLocale(c: Context?): Context {
        Log.d("setLocale","${getLanguage(c!!)}")
        return setNewLocale(c!!, getLanguage(c))
    }

    fun setNewLocale(c: Context, language: String): Context {
        return updatedResources(c, language)
    }

    private fun getLanguage(c: Context): String{
        return AppPref(c).locale
    }

    private fun updatedResources(context: Context, language: String):Context{
        Log.d("updatedResources","$language")
        var c= context
        val locale = Locale(language)
        Locale.setDefault(locale)

        val res = context.resources
        val config = Configuration(res.configuration)


        if (Build.VERSION.SDK_INT >= 17) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Log.d("1PRECHANGE-BUILD > N)","${config.locales[0]}")
            }else{
                Log.d("1PRECHANGE-BUILD > N","${config.locale}")
            }
            config.setLocale(locale)
            c = context.createConfigurationContext(config)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Log.d("1POSTCHANGE-BUILD > N)","${config.locales[0]}")
            }else{
                Log.d("1POSTCHANGE-BUILD > N","${config.locale}")
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Log.d("2PRECHANGE-BUILD > N)","${config.locales[0]}")
            }else{
                Log.d("2PRECHANGE-BUILD ELSE","${config.locale}")
            }
            config.locale = locale
            res.updateConfiguration(config, res.displayMetrics)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Log.d("2POSTCHANGE-BUILD > N)","${config.locales[0]}")
            }else{
                Log.d("2POSTCHANGE-BUILD ELSE","${config.locale}")
            }
        }
        return c
    }

    fun getLocale(res: Resources): Locale{
        val config = res.configuration
        return if(Build.VERSION.SDK_INT >= 24) config.locales[0] else  config.locale
    }
}