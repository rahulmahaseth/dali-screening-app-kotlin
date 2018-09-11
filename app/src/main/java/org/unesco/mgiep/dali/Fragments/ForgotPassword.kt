package org.unesco.mgiep.dali.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_forgotpassword.*
import org.unesco.mgiep.dali.Dagger.MyApplication
import org.unesco.mgiep.dali.R
import org.unesco.mgiep.dali.Utility.showFragment
import javax.annotation.meta.When

class ForgotPassword : Fragment(){

    private lateinit var mAuth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity!!.application as MyApplication).component.inject(this)
        mAuth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?=
            inflater.inflate(R.layout.fragment_forgotpassword, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        edit_reset_pass_email.setOnFocusChangeListener { view, b ->
            if(!b){
                if(edit_reset_pass_email.text.isEmpty()){
                    edit_reset_pass_email.hint = getString(R.string.email)
                }else{

                }
            }else{
                edit_reset_pass_email.hint = ""
            }
        }

        btn_reset_password_submit.setOnClickListener {v->
            when{
                edit_reset_pass_email.text.isEmpty() -> edit_reset_pass_email.error = getString(R.string.required)
                else -> {
                    mAuth.sendPasswordResetEmail(edit_reset_pass_email.text.toString())
                            .addOnSuccessListener {
                                Toast.makeText(activity, "Mail Sent", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener {
                                Toast.makeText(activity, "Error while sending mail!", Toast.LENGTH_SHORT).show()
                            }
                }
            }
        }

        tv_reset_password_back.setOnClickListener {
            showFragment(
                    Fragment.instantiate(
                            activity,
                            Login::class.java.name
                    ),
                    false
            )
        }
    }

    private fun showFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        fragment.showFragment(container = R.id.splash_fragment_container,
                fragmentManager = activity!!.supportFragmentManager,
                addToBackStack = addToBackStack)
    }
}