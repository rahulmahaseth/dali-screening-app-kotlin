package org.unesco.mgiep.dali.Utility

import android.content.Context
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

fun Any.showAsToast(context : Context, alertType: Boolean = false, duration: Int =  Toast.LENGTH_SHORT){
    val toast = Toast.makeText(context, this.toString(), duration)
    val toastView = toast.view
    val toastMessage = toastView.findViewById<TextView>(android.R.id.message)
    toastMessage.textSize = 18f
    toastMessage.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
    toastMessage.gravity = Gravity.CENTER
    toastMessage.compoundDrawablePadding = 8
    toastView.background =ContextCompat.getDrawable(context, R.drawable.curved_rectangle)
    toast.show()
}