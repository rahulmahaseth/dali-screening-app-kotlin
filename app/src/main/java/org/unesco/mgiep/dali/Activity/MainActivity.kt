package org.unesco.mgiep.dali.Activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.AttributeSet
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.drawer_layout.*
import kotlinx.android.synthetic.main.nav_header_main.*
import org.unesco.mgiep.dali.Dagger.MyApplication
import org.unesco.mgiep.dali.Data.AppPref
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
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)
        (application as MyApplication).component.inject(this)
        handleNavigationClick()
        mAuth = FirebaseAuth.getInstance()

        showFragment(
                Fragment.instantiate(
                this,
                        Home::class.java.name
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
                                    Home::class.java.name
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
                            true
                    )
                }
                R.id.nav_scheduled -> {
                    showFragment(
                            Fragment.instantiate(
                                    this,
                                    PendingScreenings::class.java.name
                            ),
                            true
                    )
                }
                R.id.nav_participants ->{
                    showFragment(
                            Fragment.instantiate(
                                    this,
                                    ParticipantList::class.java.name
                            ),
                            true
                    )
                }
                R.id.nav_settings -> {
                    showFragment(
                            Fragment.instantiate(
                                    this,
                                    Settings::class.java.name
                            ),
                            true
                    )
                }
                R.id.nav_logout -> {
                    AlertDialog.Builder(this)
                            .setMessage(getString(R.string.logout_warn_message))
                            .setPositiveButton(getString(R.string.yes)){_,_->
                                mAuth.signOut()
                                startActivity(Intent(this, SplashActivity::class.java))
                            }
                            .setNegativeButton(getString(R.string.no)){_,_->}
                            .create()
                            .show()

                }
            }
            drawer_layout.closeDrawers()
            true
        }
    }

    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount
        when {
            drawer_layout.isDrawerOpen(GravityCompat.START) -> drawer_layout.closeDrawer(GravityCompat.START)
            count == 0 -> {
                AlertDialog.Builder(this)
                        .setMessage(getString(R.string.exit_warn_message))
                        .setPositiveButton(getString(R.string.yes)){ _, _->finish()}
                        .setNegativeButton(getString(R.string.no)){ _, _->}
                        .create()
                        .show()
            }
            else -> supportFragmentManager.popBackStack()
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