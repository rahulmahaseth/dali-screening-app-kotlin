package org.unesco.mgiep.dali.Fragments

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_login.*
import org.unesco.mgiep.dali.Data.AppPref
import org.unesco.mgiep.dali.R
import org.unesco.mgiep.dali.Utility.showFragment
import durdinapps.rxfirebase2.RxFirebaseAuth
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.unesco.mgiep.dali.Activity.MainActivity
import org.unesco.mgiep.dali.Dagger.MyApplication
import org.unesco.mgiep.dali.Data.Login
import java.util.*


class Login : Fragment() {

    private lateinit var mAuth: FirebaseAuth
    private val login = Login()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity!!.application as MyApplication).component.inject(this)
        mAuth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_login, container, false)


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        edit_email.setOnFocusChangeListener { view, b ->
            if (!b) {
                if (edit_email.text.isEmpty()) {
                    edit_email.hint = getString(R.string.email)
                } else {
                    login.email = edit_email.text.toString()
                }
            } else {
                edit_email.hint = ""
            }
        }

        edit_password.setOnFocusChangeListener { view, b ->
            if (!b) {
                if (edit_password.text.isEmpty()) {
                    edit_password.hint = getString(R.string.password)
                } else {
                    login.password = edit_password.text.toString()
                }
            } else {
                edit_password.hint = ""
            }
        }

        btn_login.setOnClickListener { v ->
            when {
                edit_email.text.isEmpty() -> edit_email.error = getString(R.string.required)
                edit_password.text.isEmpty() -> edit_password.error = getString(R.string.required)
                else -> {
                    RxFirebaseAuth.signInWithEmailAndPassword(mAuth, login.email, login.password)
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                startActivity(Intent(activity, MainActivity::class.java))
                            }, {
                                Toast.makeText(activity, getString(R.string.login_fail), Toast.LENGTH_SHORT).show()
                            })
                }
            }
            AppPref.email = edit_email.text.toString()

        }
        btn_signup.setOnClickListener {
            showFragment(
                    Fragment.instantiate(
                            activity,
                            SignUp::class.java.name
                    ),
                    true
            )
        }

        tv_forgot_password.setOnClickListener {

            AppPref.email = edit_email.text.toString()
            showFragment(
                    Fragment.instantiate(
                            activity,
                            ForgotPassword::class.java.name
                    ),
                    true
            )
        }
    }

    fun setLocale(lang: String) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        activity!!.resources.updateConfiguration(config, activity!!.resources.displayMetrics)
        Toast.makeText(activity, "Locale in $lang !", Toast.LENGTH_LONG).show()
    }

    private fun showFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        fragment.showFragment(container = R.id.splash_fragment_container,
                fragmentManager = activity!!.supportFragmentManager,
                addToBackStack = addToBackStack)
    }
}