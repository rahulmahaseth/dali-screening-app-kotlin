package org.unesco.mgiep.dali.Activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.drawer_layout.*
import org.unesco.mgiep.dali.Dagger.MyApplication
import org.unesco.mgiep.dali.Fragments.*
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
        handleNavigationClick()
        showFragment(
                Fragment.instantiate(
                this,
                        Dashboard::class.java.name
                ),
                addToBackStack = false
        )
    }

    private fun handleNavigationClick(){
        nav_view.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            when(menuItem.itemId){
                R.id.nav_home -> {
                    showFragment(
                            Fragment.instantiate(
                                    this,
                                    Dashboard::class.java.name
                            ),
                            false
                    )
                }
                R.id.nav_screenings -> {
                    showFragment(
                            Fragment.instantiate(
                                    this,
                                    Dashboard::class.java.name
                            ),
                            false
                    )
                }
                R.id.nav_participants ->{
                    showFragment(
                            Fragment.instantiate(
                                    this,
                                    ParticipantList::class.java.name
                            ),
                            false
                    )
                }
                R.id.nav_settings -> {
                    showFragment(
                            Fragment.instantiate(
                                    this,
                                    Settings::class.java.name
                            ),
                            false
                    )
                }
            }
            drawer_layout.closeDrawers()
            true
        }
    }

    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount
        if(count == 0){
            AlertDialog.Builder(this)
                    .setMessage("Do you want to exit?")
                    .setPositiveButton("Yes"){_,_->finish()}
                    .setNegativeButton("No"){_,_->}
                    .create()
                    .show()
        }else {
            supportFragmentManager.popBackStack()
        }
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){
            android.R.id.home -> {
                drawer_layout.openDrawer(GravityCompat.START)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        fragment.showFragment(container = R.id.fragment_container,
                fragmentManager = supportFragmentManager,
                addToBackStack = addToBackStack)
    }

}