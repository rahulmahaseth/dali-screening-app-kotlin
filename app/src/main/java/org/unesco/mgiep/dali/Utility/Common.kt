package org.unesco.mgiep.dali.Utility

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.View


fun Fragment.showFragment(container: Int, fragmentManager: FragmentManager,
                          addToBackStack: Boolean = false, animate: Boolean = true) {
    val fm = fragmentManager.beginTransaction()
    fm.replace(container, this, this.javaClass.name)
    if (addToBackStack) fm.addToBackStack(null)
    fm.commit()
}

fun View.show(){
    if(visibility != View.VISIBLE){
        visibility = View.VISIBLE
    }
}

fun View.hide(){
    if(visibility != View.GONE || visibility != View.INVISIBLE){
        visibility = View.GONE
    }
}