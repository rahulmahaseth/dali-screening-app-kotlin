package org.unesco.mgiep.dali.Activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import org.unesco.mgiep.dali.Dagger.MyApplication
import org.unesco.mgiep.dali.Data.AppPref
import org.unesco.mgiep.dali.Fragments.PreScreeningIntro
import org.unesco.mgiep.dali.R
import org.unesco.mgiep.dali.Utility.showFragment

class NewScreeningActivity : BaseActivity(){
    override fun getLayoutId(): Int {
        return R.layout.activity_newscreening
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newscreening)
        (application as MyApplication).component.inject(this)
        showFragment(
                Fragment.instantiate(
                        this,
                        PreScreeningIntro::class.java.name
                ),
                false
        )
    }

    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount
        when {
            count == 0 -> {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            AppPref(this).loading -> {

            }
            else -> supportFragmentManager.popBackStack()
        }
    }

    private fun showFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        fragment.showFragment(container = R.id.new_screening_fragment_container,
                fragmentManager = supportFragmentManager,
                addToBackStack = addToBackStack)
    }

}