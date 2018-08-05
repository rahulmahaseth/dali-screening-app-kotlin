package org.unesco.mgiep.dali.Activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import org.unesco.mgiep.dali.Fragments.Login
import org.unesco.mgiep.dali.R
import org.unesco.mgiep.dali.Utility.showFragment

class MainActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showFragment(
                Fragment.instantiate(
                this,
                        Login::class.java.name
                ),
                addToBackStack = false
        )
    }

    override fun onResume() {
        super.onResume()
    }

    private fun showFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        fragment.showFragment(container = R.id.fragment_container,
                fragmentManager = supportFragmentManager,
                addToBackStack = addToBackStack)
    }


}