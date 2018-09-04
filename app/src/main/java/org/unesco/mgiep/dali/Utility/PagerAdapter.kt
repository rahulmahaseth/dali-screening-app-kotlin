package org.unesco.mgiep.dali.Utility

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import org.unesco.mgiep.dali.Fragments.Dashboard

class PagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> {
               Dashboard()
            }
            2 -> {
                Dashboard()
            }
            else -> Dashboard()
        }
    }

    override fun getCount(): Int = 2
}