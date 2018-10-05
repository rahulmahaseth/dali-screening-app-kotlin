package org.unesco.mgiep.dali.Activity

import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import org.unesco.mgiep.dali.Data.AppPref
import org.unesco.mgiep.dali.Fragments.ScreeningTutorial1
import org.unesco.mgiep.dali.R
import org.unesco.mgiep.dali.Utility.showFragment
import java.util.*

class ScreeningActivity: BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_screening
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screening)
        showFragment(
                Fragment.instantiate(
                        this,
                        ScreeningTutorial1::class.java.name
                ),
                false
        )

    }

    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount
        when {
            !AppPref(this).loading -> {
                AlertDialog.Builder(this)
                        .setMessage(getString(R.string.exit_screening_prompt))
                        .setPositiveButton(getString(R.string.yes)){ _, _->
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                setLocale(Resources.getSystem().configuration.locales[0].language)
                            }else{
                                setLocale(Resources.getSystem().configuration.locale.language)
                            }
                        }
                        .setNegativeButton(getString(R.string.no)){ _, _->}
                        .create()
                        .show()
            }
            AppPref(this).loading -> {

            }
            else -> supportFragmentManager.popBackStack()
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
                Intent(this, MainActivity::class.java)
        )
        finish()
    }

    private fun showFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        fragment.showFragment(container = R.id.screening_fragment_container,
                fragmentManager = supportFragmentManager,
                addToBackStack = addToBackStack)
    }
}