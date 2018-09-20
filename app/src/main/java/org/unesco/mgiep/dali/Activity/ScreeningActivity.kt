package org.unesco.mgiep.dali.Activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.drawer_layout.*
import org.unesco.mgiep.dali.Fragments.Screening
import org.unesco.mgiep.dali.R
import org.unesco.mgiep.dali.Utility.LocaleManager
import org.unesco.mgiep.dali.Utility.showFragment

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
                        Screening::class.java.name
                ),
                false
        )

    }

    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount
        when (count) {
            0 -> {
                AlertDialog.Builder(this)
                        .setMessage(getString(R.string.exit_screening_prompt))
                        .setPositiveButton(getString(R.string.yes)){ _, _->
                            startActivity(Intent(this, MainActivity::class.java))
                        }
                        .setNegativeButton(getString(R.string.no)){ _, _->}
                        .create()
                        .show()
            }
            else -> supportFragmentManager.popBackStack()
        }
    }

    private fun showFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        fragment.showFragment(container = R.id.screening_fragment_container,
                fragmentManager = supportFragmentManager,
                addToBackStack = addToBackStack)
    }
}