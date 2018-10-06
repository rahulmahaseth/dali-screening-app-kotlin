package org.unesco.mgiep.dali.Activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.drawer_layout.*
import org.unesco.mgiep.dali.Dagger.MyApplication
import org.unesco.mgiep.dali.Data.AppPref
import org.unesco.mgiep.dali.Fragments.*
import org.unesco.mgiep.dali.R
import org.unesco.mgiep.dali.Utility.showFragment

class MainActivity: BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    private lateinit var mAuth: FirebaseAuth
    private var registered = false

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
        registered = intent.getBooleanExtra("registered", false)
        if(registered){
            showFragment(
                    Fragment.instantiate(
                            this,
                            PostSignUpInfo::class.java.name
                    ),
                    false
            )
        }else{
            showFragment(
                    Fragment.instantiate(
                            this,
                            Home::class.java.name
                    ),
                    addToBackStack = false
            )
        }

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

                R.id.nav_about -> {
                    showFragment(
                            Fragment.instantiate(
                                    this,
                                    About::class.java.name
                            ),
                            true
                    )
                }
                R.id.nav_logout -> {
                    AlertDialog.Builder(this)
                            .setMessage(getString(R.string.logout_warn_message))
                            .setPositiveButton(getString(R.string.yes)){_,_->
                                mAuth.signOut()
                                AppPref(applicationContext).userEmail = ""
                                AppPref(applicationContext).userName = ""
                                AppPref(applicationContext).userInstitution = ""
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

        this.supportFragmentManager.addOnBackStackChangedListener {
            if(getCurrentFragment() == Home::class.java){
                nav_view.setCheckedItem(R.id.nav_home)
            }

        }
    }

    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount
        when {
            drawer_layout.isDrawerOpen(GravityCompat.START) -> drawer_layout.closeDrawer(GravityCompat.START)
            count == 0 -> {
                AlertDialog.Builder(this)
                        .setMessage(getString(R.string.exit_warn_message))
                        .setPositiveButton(getString(R.string.yes)){ _, _->
                            startActivity(Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                            finish()
                        }
                        .setNegativeButton(getString(R.string.no)){ _, _->}
                        .create()
                        .show()
            }
            count > 0 -> {
                Log.d("pop","$count")
                supportFragmentManager.popBackStack()

            }
            else -> {
                super.onBackPressed()
            }
        }
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){
            android.R.id.home -> {
                Log.d("BackStackCt","${fragmentManager.backStackEntryCount}")
                if(supportFragmentManager.backStackEntryCount == 2){
                    onBackPressed()
                }else{
                    drawer_layout.openDrawer(GravityCompat.START)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showFragment(fragment: Fragment, addToBackStack: Boolean = true) {

        if(supportFragmentManager.backStackEntryCount > 0){
            supportFragmentManager.popBackStackImmediate()
        }

        fragment.showFragment(container = R.id.fragment_container,
                fragmentManager = supportFragmentManager,
                addToBackStack = addToBackStack,
                animate = false)
    }

    private fun getCurrentFragment(): Fragment {
        return supportFragmentManager.findFragmentById(R.id.fragment_container)
    }

    override fun onResume() {
        super.onResume()
        title = getString(R.string.app_name)
    }

}