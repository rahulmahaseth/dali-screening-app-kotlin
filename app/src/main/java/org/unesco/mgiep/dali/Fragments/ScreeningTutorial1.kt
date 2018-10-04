package org.unesco.mgiep.dali.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_screeningtutorial1.*
import org.unesco.mgiep.dali.R
import org.unesco.mgiep.dali.Utility.showFragment

class ScreeningTutorial1: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_screeningtutorial1, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_screening_tut_1.setOnClickListener {
            showFragment(
                    Fragment.instantiate(
                            activity!!,
                            Screening::class.java.name
                    ),
                    false
            )
        }
    }

    private fun showFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        fragment.showFragment(container = R.id.screening_fragment_container,
                fragmentManager = activity!!.supportFragmentManager,
                addToBackStack = addToBackStack)
    }
}