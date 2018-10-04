package org.unesco.mgiep.dali.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.drawer_layout.*
import kotlinx.android.synthetic.main.fragment_about.*
import org.unesco.mgiep.dali.R
import org.unesco.mgiep.dali.Utility.showFragment

class About:Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_about, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)

        activity!!.nav_view.menu.getItem(3).isChecked = true

        tv_privacy.setOnClickListener {
            showFragment(
                    Fragment.instantiate(
                            activity!!,
                            PrivacyPolicy::class.java.name
                    ),
                    true
            )
        }
    }

    private fun showFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        fragment.showFragment(container = R.id.fragment_container,
                fragmentManager = activity!!.supportFragmentManager,
                addToBackStack = addToBackStack)
    }

    override fun onResume() {
        super.onResume()
        activity!!.title = getString(R.string.about)
    }
}