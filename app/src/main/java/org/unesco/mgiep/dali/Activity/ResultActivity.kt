package org.unesco.mgiep.dali.Activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import org.unesco.mgiep.dali.Fragments.Result
import org.unesco.mgiep.dali.R
import org.unesco.mgiep.dali.Utility.showFragment

class ResultActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        showFragment(
                Fragment.instantiate(
                        this,
                        Result::class.java.name
                ),
                false
        )
    }

    private fun showFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        fragment.showFragment(container = R.id.fragment_result_container,
                fragmentManager = supportFragmentManager,
                addToBackStack = addToBackStack)
    }
}