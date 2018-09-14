package org.unesco.mgiep.dali.Activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import org.unesco.mgiep.dali.Fragments.NewScreening
import org.unesco.mgiep.dali.R
import org.unesco.mgiep.dali.Utility.showFragment

class NewScreeningActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newscreening)

        showFragment(
                Fragment.instantiate(
                        this,
                        NewScreening::class.java.name
                ),
                false
        )
    }

    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount
        if(count == 0){
            startActivity(Intent(this, MainActivity::class.java))
        }else {
            supportFragmentManager.popBackStack()
        }
    }

    private fun showFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        fragment.showFragment(container = R.id.new_screening_fragment_container,
                fragmentManager = supportFragmentManager,
                addToBackStack = addToBackStack)
    }

}