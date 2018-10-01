package org.unesco.mgiep.dali.Activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.crashlytics.android.Crashlytics
import com.google.firebase.auth.FirebaseAuth
import io.fabric.sdk.android.Fabric
import org.unesco.mgiep.dali.Dagger.MyApplication
import org.unesco.mgiep.dali.Data.AppPref
import org.unesco.mgiep.dali.Fragments.Login
import org.unesco.mgiep.dali.R
import org.unesco.mgiep.dali.Utility.showFragment


class SplashActivity: BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_splash
    }

    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        (application as MyApplication).component.inject(this)

        Fabric.with(this, Crashlytics())
        
        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser

        if(currentUser != null){
            if(AppPref(applicationContext).userEmail == "" && AppPref(applicationContext).userInstitution == "" && AppPref(applicationContext).userName == ""){
                mAuth.signOut()
                showFragment(
                        Fragment.instantiate(
                                this,
                                Login::class.java.name
                        ),
                        addToBackStack = false
                )
            }else{
                startActivity(Intent(this, MainActivity::class.java))
            }

        }else{
            showFragment(
                    Fragment.instantiate(
                            this,
                            Login::class.java.name
                    ),
                    addToBackStack = false
            )
        }
    }

    private fun showFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        fragment.showFragment(container = R.id.splash_fragment_container,
                fragmentManager = supportFragmentManager,
                addToBackStack = addToBackStack,
                animate = false)
    }


    override fun onResume() {
        super.onResume()

    }

    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount
        when {
            count == 0 -> {
                startActivity(Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                finish()
            }
            else -> supportFragmentManager.popBackStack()
        }
    }

}