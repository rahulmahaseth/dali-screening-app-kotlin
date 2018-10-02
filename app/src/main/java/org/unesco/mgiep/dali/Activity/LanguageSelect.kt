package org.unesco.mgiep.dali.Activity

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.select_language.*
import org.unesco.mgiep.dali.Data.AppPref
import org.unesco.mgiep.dali.Data.AssessmentLanguage
import org.unesco.mgiep.dali.Data.ViewModels.ScreeningViewModel
import org.unesco.mgiep.dali.R
import java.util.*


class LanguageSelect: AppCompatActivity() {

    private var screeningType = ""
    private var participantId = ""
    private var participantName = ""
    private var screeningId = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.select_language)

        screeningId = intent.getStringExtra("screeningId")
        screeningType = intent.getStringExtra("type")
        participantId = intent.getStringExtra("participantId")
        participantName = intent.getStringExtra("participantName")

        btn_language_english.setOnClickListener {
            setLocale("en")
            AppPref(applicationContext).assessmentLanguage = AssessmentLanguage.ENGLISH.toString()
        }

        btn_language_hindi.setOnClickListener {
            setLocale("hi")
            AppPref(applicationContext).assessmentLanguage = AssessmentLanguage.HINDI.toString()
        }
    }


    fun setLocale(lang: String) {
        val locale = getLocale(lang)

        val res = this.getResources()
        // Change locale settings in the app.
        val dm = res.getDisplayMetrics()
        val conf = res.getConfiguration()
        // TODO(raacker): deprecated. change to setLocale(). It needs to be set minSdk to 17. Configure project's minSdk first
        conf.locale = locale
        res.updateConfiguration(conf, dm)

        Locale.setDefault(locale)

        startActivity(
                Intent(this, ScreeningActivity::class.java)
                    .putExtra("type", screeningType)
                    .putExtra("screeningId", screeningId)
                    .putExtra("participantId",participantId)
                    .putExtra("participantName", participantName)
                    .putExtra("screeningLang", lang)
        )
        finish()
    }

    private fun getLocale(languageTag: String?): Locale {
        return if (languageTag == null) {
            getDeviceLocale()
        } else {
            val values = languageTag.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            when (values.size) {
                3 -> Locale(values[0], values[1], values[2])
                2 -> Locale(values[0], values[1])
                else -> Locale(values[0])
            }
        }
    }

    fun getDeviceLocale(): Locale {
        return Resources.getSystem().configuration.locale
    }

}