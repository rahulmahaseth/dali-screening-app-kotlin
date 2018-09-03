package org.unesco.mgiep.dali.Activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import org.unesco.mgiep.dali.Fragments.Screening
import org.unesco.mgiep.dali.R
import org.unesco.mgiep.dali.Utility.showFragment

class ScreeningActivity: AppCompatActivity() {

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

    private fun showFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        fragment.showFragment(container = R.id.screening_fragment_container,
                fragmentManager = supportFragmentManager,
                addToBackStack = addToBackStack)
    }
}