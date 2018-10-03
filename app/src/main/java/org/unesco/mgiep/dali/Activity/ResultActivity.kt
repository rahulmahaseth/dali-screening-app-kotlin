package org.unesco.mgiep.dali.Activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import org.unesco.mgiep.dali.Fragments.Result
import org.unesco.mgiep.dali.R
import org.unesco.mgiep.dali.Utility.showFragment

class ResultActivity: AppCompatActivity() {

    private var screening = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        screening = intent.getBooleanExtra("screening", false)

        showFragment(
                Fragment.instantiate(
                        this,
                        Result::class.java.name
                ),
                false
        )
    }

    override fun onBackPressed() {
        if(screening){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }else{
            super.onBackPressed()
        }
    }

    private fun showFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        fragment.showFragment(container = R.id.fragment_result_container,
                fragmentManager = supportFragmentManager,
                addToBackStack = addToBackStack)
    }
}