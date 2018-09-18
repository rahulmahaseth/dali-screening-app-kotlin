package org.unesco.mgiep.dali.Fragments

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.fragment_settings.*
import org.unesco.mgiep.dali.Activity.MainActivity
import org.unesco.mgiep.dali.Dagger.MyApplication
import org.unesco.mgiep.dali.Data.AppPref
import org.unesco.mgiep.dali.R
import org.unesco.mgiep.dali.Utility.showFragment
import java.util.*

class Settings : Fragment(), AdapterView.OnItemSelectedListener {
    private val stringArray = ArrayList<String>()
    private lateinit var arrayAdapter: ArrayAdapter<String>

    private var spinnerTouched = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity!!.application as MyApplication).component.inject(this)
        arrayAdapter = ArrayAdapter(activity, R.layout.item_spinner, stringArray)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?=
            inflater.inflate(R.layout.fragment_settings, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        stringArray.add(getString(R.string.language))
        stringArray.add("English")
        stringArray.add("हिन्दी")
        setting_language_spinner.adapter = arrayAdapter

        setting_language_spinner.onItemSelectedListener = this

        setting_language_spinner.setOnTouchListener { view, motionEvent ->
            spinnerTouched = true
            false
        }

    }

    fun setLocale(lang: String) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = Configuration()
        //val conf = resources.configuration
        //val dm = resources.displayMetrics
        config.locale = locale
        activity!!.resources.updateConfiguration(config, activity!!.resources.displayMetrics)
        startActivity(Intent(activity, MainActivity::class.java))
        activity!!.finish()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        //do nothing
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {

        when (position) {
            1 -> {
                if (spinnerTouched) {
                    setLocale("en")
                    AppPref(activity!!.baseContext).locale = "en"
                }
            }
            2 -> {
                if (spinnerTouched) {
                    setLocale("hi")
                    AppPref(activity!!.baseContext).locale = "en"
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        activity!!.title = getString(R.string.settings)
    }
}