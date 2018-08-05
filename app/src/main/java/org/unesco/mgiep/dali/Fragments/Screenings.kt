package org.unesco.mgiep.dali.Fragments

import android.support.v4.app.Fragment
import org.unesco.mgiep.dali.R
import org.unesco.mgiep.dali.Utility.showFragment

class Screenings : Fragment() {

    private fun showFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        fragment.showFragment(container = R.id.fragment_container,
                fragmentManager = activity!!.supportFragmentManager,
                addToBackStack = addToBackStack)
    }
}