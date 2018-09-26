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
import kotlinx.android.synthetic.main.drawer_layout.*
import kotlinx.android.synthetic.main.fragment_settings.*
import org.unesco.mgiep.dali.Activity.MainActivity
import org.unesco.mgiep.dali.Dagger.MyApplication
import org.unesco.mgiep.dali.Data.AppPref
import org.unesco.mgiep.dali.R
import org.unesco.mgiep.dali.Utility.LocaleManager
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
        activity!!.nav_view.menu.getItem(3).isChecked = true


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


    override fun onNothingSelected(p0: AdapterView<*>?) {
        //do nothing
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {

        when (position) {
            1 -> {
                if (spinnerTouched) {
                    //setLocale("en")
                    LocaleManager().persistLanguage(activity!!.applicationContext, "en")
                    AppPref(activity!!.applicationContext).locale = "en"
                    LocaleManager().setLocale(activity!!.applicationContext)
                    startActivity(Intent(activity, MainActivity::class.java))
                    activity!!.finish()
                }
            }
            2 -> {
                if (spinnerTouched) {
                    //setLocale("hi")
                    LocaleManager().persistLanguage(activity!!.applicationContext, "hi")
                    AppPref(activity!!.applicationContext).locale = "hi"
                    LocaleManager().setLocale(activity!!.applicationContext)
                    startActivity(Intent(activity, MainActivity::class.java))
                    activity!!.finish()

                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        activity!!.title = getString(R.string.settings)
    }
}