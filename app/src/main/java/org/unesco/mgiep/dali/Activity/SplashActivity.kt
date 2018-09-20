package org.unesco.mgiep.dali.Activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import org.unesco.mgiep.dali.Dagger.MyApplication
import org.unesco.mgiep.dali.Fragments.Dashboard
import org.unesco.mgiep.dali.Fragments.Login
import org.unesco.mgiep.dali.R
import org.unesco.mgiep.dali.Utility.LocaleManager
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
        mAuth = FirebaseAuth.getInstance()
        showFragment(
                Fragment.instantiate(
                        this,
                        Login::class.java.name
                ),
                addToBackStack = false
        )
    }

    private fun showFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        fragment.showFragment(container = R.id.splash_fragment_container,
                fragmentManager = supportFragmentManager,
                addToBackStack = addToBackStack)
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = mAuth.currentUser
        if(currentUser != null){
            startActivity(Intent(this, MainActivity::class.java))
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

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocaleManager().setLocale(newBase!!))
    }
}