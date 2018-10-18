package org.unesco.mgiep.dali.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.android.synthetic.main.fragment_screeningtutorial1.*
import org.unesco.mgiep.dali.R
import org.unesco.mgiep.dali.Utility.showFragment

class ScreeningTutorial1: Fragment() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAnalytics = FirebaseAnalytics.getInstance(context!!)
    }
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

    override fun onResume() {
        super.onResume()
        firebaseAnalytics.setCurrentScreen(activity!!, "screening_tutoriual_1", ScreeningTutorial1::class.java.simpleName)
    }

    private fun showFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        fragment.showFragment(container = R.id.screening_fragment_container,
                fragmentManager = activity!!.supportFragmentManager,
                addToBackStack = addToBackStack)
    }
}