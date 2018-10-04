package org.unesco.mgiep.dali.Utility

import android.content.Context
import android.support.design.widget.TextInputLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import org.unesco.mgiep.dali.R


fun Fragment.showFragment(container: Int, fragmentManager: FragmentManager,
                          addToBackStack: Boolean = false, animate: Boolean = true) {
    val fm = fragmentManager.beginTransaction()
    if(animate){
        fm.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
    }
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
    if(visibility != View.GONE && visibility != View.INVISIBLE){
        visibility = View.GONE
    }
}

fun Any.showAsToast(context : Context, warn: Boolean = false, greet: Boolean = false, duration: Int =  Toast.LENGTH_SHORT){
    val toast = Toast.makeText(context, this.toString(), duration)
    val toastView = toast.view
    val toastMessage = toastView.findViewById<TextView>(android.R.id.message)
    toastMessage.textSize = 16f
    toastMessage.setTextColor(ContextCompat.getColor(context, R.color.white))
    toastMessage.gravity = Gravity.CENTER
    toastMessage.compoundDrawablePadding = 4
    if(warn){
        toastView.background =ContextCompat.getDrawable(context, R.drawable.curved_rectangle)
    }else{
        toastView.background =ContextCompat.getDrawable(context, R.drawable.curved_rectangle_green)
    }
    toast.show()
}

fun TextInputLayout.markRequired(){
    hint = "$hint *"
}