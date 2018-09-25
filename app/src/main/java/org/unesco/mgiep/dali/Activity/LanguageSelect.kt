package org.unesco.mgiep.dali.Activity

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.select_language.*
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
        }

        btn_language_hindi.setOnClickListener {
            setLocale("hi")
        }
    }


    fun setLocale(lang: String) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = Configuration()
        //val conf = resources.configuration
        //val dm = resources.displayMetrics
        config.locale = locale
        resources.updateConfiguration(config, resources.displayMetrics)
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
}