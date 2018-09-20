package org.unesco.mgiep.dali.Fragments

import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import android.widget.Toast
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_login.*
import org.unesco.mgiep.dali.Data.AppPref
import org.unesco.mgiep.dali.R
import org.unesco.mgiep.dali.Activity.MainActivity
import org.unesco.mgiep.dali.Activity.SplashActivity
import org.unesco.mgiep.dali.Dagger.MyApplication
import org.unesco.mgiep.dali.Data.Login
import org.unesco.mgiep.dali.Data.User
import org.unesco.mgiep.dali.Repositories.MainReposirtory
import org.unesco.mgiep.dali.Utility.*
import java.util.*
import kotlin.collections.ArrayList


class Login : Fragment() {

    private lateinit var mAuth: FirebaseAuth
    private val login = Login()
    private lateinit var mainReposirtory: MainReposirtory

    private val stringArray = ArrayList<String>()
    private lateinit var arrayAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity!!.application as MyApplication).component.inject(this)
        mAuth = FirebaseAuth.getInstance()
        mainReposirtory = MainReposirtory()
        arrayAdapter = ArrayAdapter(activity, R.layout.item_spinner, stringArray)
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
                    signIn()
                }
            }
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
            showFragment(
                    Fragment.instantiate(
                            activity,
                            ForgotPassword::class.java.name
                    ),
                    true
            )
        }
    }

    private fun signIn() {
        disableViews()
        progressBar1.show()
        mAuth.signInWithEmailAndPassword(edit_email.text.toString(), edit_password.text.toString())
                .addOnSuccessListener { authResult ->
                    Log.d("Login", "Success")
                    fetchUser(authResult)
                }
                .addOnFailureListener {
                    Log.d("Login", "Failed")
                    progressBar1.hide()
                    enableViews()
                    getString(R.string.login_fail).showAsToast(activity!!)
                }
    }

    private fun fetchUser(authResult: AuthResult) {
        mainReposirtory.getUser(authResult.user.uid)
                .addOnSuccessListener {
                    Log.d("Fetch-User", "Success")
                    if (it.exists()) {
                        Log.d("Fetch-User", "Document Exists")
                        val user = it.toObject(User::class.java)
                        progressBar1.hide()
                        startActivity(Intent(activity, MainActivity::class.java))
                    } else {
                        Log.d("Fetch-User", "Document doesn't Exists")
                        enableViews()
                        progressBar1.hide()
                        mAuth.signOut()
                        getString(R.string.login_fail).showAsToast(activity!!)
                    }
                }
                .addOnFailureListener {
                    Log.d("Fetch-User", "Failed")
                    progressBar1.hide()
                    enableViews()
                    mAuth.signOut()
                    getString(R.string.login_fail).showAsToast(activity!!)
                }
    }

    private fun disableViews() {
        edit_email.isEnabled = false
        edit_password.isEnabled = false
        btn_login.isEnabled = false
        btn_signup.isEnabled = false
        tv_forgot_password.isEnabled = false
        //spinner_language.isEnabled = false
    }

    private fun enableViews() {
        edit_email.isEnabled = true
        edit_password.isEnabled = true
        btn_login.isEnabled = true
        btn_signup.isEnabled = true
        tv_forgot_password.isEnabled = true
        //spinner_language.isEnabled = true
    }

    private fun showFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        fragment.showFragment(container = R.id.splash_fragment_container,
                fragmentManager = activity!!.supportFragmentManager,
                addToBackStack = addToBackStack)
    }
}
