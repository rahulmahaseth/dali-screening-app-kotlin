package org.unesco.mgiep.dali.Activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.drawer_layout.*
import org.unesco.mgiep.dali.Dagger.MyApplication
import org.unesco.mgiep.dali.Fragments.Dashboard
import org.unesco.mgiep.dali.Fragments.Login
import org.unesco.mgiep.dali.R
import org.unesco.mgiep.dali.Utility.showFragment

class MainActivity: AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp)
        (application as MyApplication).component.inject(this)

        showFragment(
                Fragment.instantiate(
                this,
                        Dashboard::class.java.name
                ),
                addToBackStack = false
        )
    }

    override fun onStart() {
        super.onStart()
    }

    private fun showFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        fragment.showFragment(container = R.id.fragment_container,
                fragmentManager = supportFragmentManager,
                addToBackStack = addToBackStack)
    }


}