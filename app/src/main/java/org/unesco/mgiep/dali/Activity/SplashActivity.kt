package org.unesco.mgiep.dali.Activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import org.unesco.mgiep.dali.R
import org.unesco.mgiep.dali.Utility.showFragment


class SplashActivity: AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance();
    }

    private fun showFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        fragment.showFragment(container = R.id.fragment_container,
                fragmentManager = supportFragmentManager,
                addToBackStack = addToBackStack)
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = mAuth.currentUser
    }
}