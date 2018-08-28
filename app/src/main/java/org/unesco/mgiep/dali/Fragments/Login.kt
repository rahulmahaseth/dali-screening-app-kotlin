package org.unesco.mgiep.dali.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_login.*
import org.unesco.mgiep.dali.Data.AppPref
import org.unesco.mgiep.dali.R
import org.unesco.mgiep.dali.Utility.showFragment
import com.google.firebase.auth.FirebaseUser



class Login : Fragment() {

    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?=
            inflater.inflate(R.layout.fragment_login, container, false)


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        btn_login.setOnClickListener {
            when{
                edit_email.text.isEmpty() -> edit_email.error = "Email Required"
                edit_password.text.isEmpty() -> edit_password.error = "Password Required"
                else -> {
                    mAuth.signInWithEmailAndPassword(edit_email.text.toString(), edit_password.text.toString())
                            .addOnCompleteListener(activity!!) {
                                if(it.isSuccessful){
                                    Toast.makeText(activity, "Authentication Success.",
                                            Toast.LENGTH_SHORT).show()
                                    showFragment(
                                            Fragment.instantiate(
                                                    activity,
                                                    Dashboard::class.java.name
                                            ),
                                            false
                                    )
                                }else{
                                    Toast.makeText(activity, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show()
                                }
                            }
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

        btn_forgot_password.setOnClickListener {
            if(edit_email.text.isEmpty()){edit_email.error = "Email Required!"}
            else {
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

    }

    private fun showFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        fragment.showFragment(container = R.id.fragment_container,
                fragmentManager = activity!!.supportFragmentManager,
                addToBackStack = addToBackStack)
    }

    public override fun onStart() {
        super.onStart()
    }

}