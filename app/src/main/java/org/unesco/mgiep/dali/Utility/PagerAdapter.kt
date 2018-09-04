package org.unesco.mgiep.dali.Utility

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import org.unesco.mgiep.dali.Fragments.Dashboard

class PagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    private var fragment = Fragment()
    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> {
                fragment = Dashboard()
                fragment.arguments = Bundle().apply {
                    putBoolean("pending", false)
                }
                fragment
            }
            1 -> {
                fragment = Dashboard()
                fragment.arguments = Bundle().apply {
                    putBoolean("pending", true)
                }
                fragment
            }
            else -> {
                fragment = Dashboard()
                fragment.arguments = Bundle().apply {
                    putBoolean("pending", false)
                }
                fragment
            }
        }
    }

    override fun getCount(): Int = 2
}