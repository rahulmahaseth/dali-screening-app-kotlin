package org.unesco.mgiep.dali.Activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.select_language.*
import org.unesco.mgiep.dali.Data.AssessmentLanguage
import org.unesco.mgiep.dali.R


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
            start(AssessmentLanguage.ENGLISH.toString())
        }

        btn_language_hindi.setOnClickListener {
            start(AssessmentLanguage.HINDI.toString())
        }

        btn_language_marathi.setOnClickListener {
            start(AssessmentLanguage.MARATHI.toString())
        }

        btn_language_kannada.setOnClickListener {
            start(AssessmentLanguage.KANNADA.toString())
        }
        btn_language_telugu.setOnClickListener {
            start(AssessmentLanguage.TELUGU.toString())
        }
    }

    private fun start(lang: String){
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