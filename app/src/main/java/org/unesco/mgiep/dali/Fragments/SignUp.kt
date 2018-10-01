package org.unesco.mgiep.dali.Fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_registration.*
import org.unesco.mgiep.dali.Activity.MainActivity
import org.unesco.mgiep.dali.Dagger.MyApplication
import org.unesco.mgiep.dali.Data.AppPref
import org.unesco.mgiep.dali.Data.Gender
import org.unesco.mgiep.dali.Data.User
import org.unesco.mgiep.dali.R
import org.unesco.mgiep.dali.Repositories.FirebaseRepository
import org.unesco.mgiep.dali.Repositories.MainReposirtory
import org.unesco.mgiep.dali.Utility.hide
import org.unesco.mgiep.dali.Utility.show
import org.unesco.mgiep.dali.Utility.showAsToast
import org.unesco.mgiep.dali.Utility.showFragment


class SignUp :Fragment() {

    private var gender = Gender.MALE
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mainReposirtory: MainReposirtory
    private lateinit var firebaseRepository: FirebaseRepository

    private val user = User()
    private var user2 = User()

    private var password = ""

    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity!!.application as MyApplication).component.inject(this)
        mAuth = FirebaseAuth.getInstance()
        mainReposirtory = MainReposirtory()
        firebaseRepository = FirebaseRepository()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?=
            inflater.inflate(R.layout.fragment_registration, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        edit_register_name.setOnFocusChangeListener { view, b ->
            if(!b){
                if(edit_register_name.text.isEmpty()){
                    edit_register_name.hint = getString(R.string.name)
                }else{
                    user.name = edit_register_name.text.toString()
                }
            }else{
                edit_register_name.hint = ""
            }
        }

        edit_register_email.setOnFocusChangeListener { view, b ->
            if(!b){
                if(edit_register_email.text.isEmpty()){
                    edit_register_email.hint = getString(R.string.email)
                }else{
                    user.email = edit_register_email.text.toString()
                }
            }else{
                edit_register_email.hint = ""
            }
        }

        edit_register_password.setOnFocusChangeListener { view, b ->
            if(!b){
                if(edit_register_password.text.isEmpty()){
                    edit_register_password.hint = getString(R.string.password)
                }else{
                    password = edit_register_password.text.toString()
                }
            }else{
                edit_register_password.hint = ""
            }
        }


        edit_register_school.setOnFocusChangeListener { view, b ->
            if(!b){
                if(edit_register_school.text.isEmpty()){
                    edit_register_school.hint = getString(R.string.school_university)
                }else{
                    user.institution = edit_register_school.text.toString()
                }
            }else{
                edit_register_school.hint = ""
            }
        }


        tv_already_registered.setOnClickListener {
            showFragment(
                    Fragment.instantiate(
                            activity,
                            Login::class.java.name
                    ),
                    false
            )
        }

        edit_radio_female.setOnClickListener {
            edit_radio_male.isChecked = false
            gender = Gender.FEMALE
        }

        edit_radio_male.setOnClickListener {
            edit_radio_female.isChecked = false
            gender = Gender.MALE
        }

        btn_register_submit.setOnClickListener { v->
            when {
                edit_register_name.text.isEmpty() -> edit_register_name.error = getString(R.string.required)
                edit_register_email.text.isEmpty() -> edit_register_email.error = getString(R.string.required)
                !edit_register_email.text.matches(emailPattern) -> Toast.makeText(activity, getString(R.string.improper_email), Toast.LENGTH_SHORT).show()
                edit_register_password.text.isEmpty() -> edit_register_password.error = getString(R.string.required)
                edit_register_password.text.length < 6 -> Toast.makeText(activity, getString(R.string.password_length_error), Toast.LENGTH_SHORT).show()
                !edit_radio_male.isChecked && !edit_radio_female.isChecked -> {
                    getString(R.string.select_gender).showAsToast(activity!!)
                }
                else->{
                    progressBar2?.show()
                    mAuth.createUserWithEmailAndPassword(edit_register_email.text.toString(),edit_register_password.text.toString())
                            .addOnSuccessListener{ authResult ->
                                Log.d("userid-on-signup", authResult.user.uid)
                                user2 = User(
                                        edit_register_email.text.toString(),
                                        edit_register_name.text.toString(),
                                        edit_register_school.text.toString(),
                                        gender.toString()
                                )
                                mainReposirtory.saveUser(
                                        authResult.user.uid,
                                        user2
                                )
                                        .addOnSuccessListener {
                                            getString(R.string.user_saved).showAsToast(activity!!)
                                            progressBar2?.hide()
                                            AppPref(activity!!.applicationContext).userEmail = user.email
                                            AppPref(activity!!.applicationContext).userName = user.name
                                            AppPref(activity!!.applicationContext).userInstitution = user.institution

                                            if(AppPref(activity!!.applicationContext).userEmail == "" && AppPref(activity!!.applicationContext).userInstitution == "" && AppPref(activity!!.applicationContext).userName == ""){
                                                mAuth.signOut()
                                            }else{
                                                progressBar2?.hide()
                                                startActivity(Intent(activity, MainActivity::class.java))
                                                activity!!.finish()
                                            }
                                        }
                                        .addOnFailureListener {
                                            progressBar2?.hide()
                                            getString(R.string.user_sync_error).showAsToast(activity!!)
                                        }

                            }
                            .addOnFailureListener{
                                progressBar2.hide()
                                getString(R.string.signup_fail).showAsToast(activity!!)
                                Log.d("signup", getString(R.string.signup_fail),it)
                            }
                }
            }
        }
    }

    private fun showFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        fragment.showFragment(container = R.id.splash_fragment_container,
                fragmentManager = activity!!.supportFragmentManager,
                addToBackStack = addToBackStack)
    }

}

