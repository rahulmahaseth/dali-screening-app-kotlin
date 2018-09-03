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
import durdinapps.rxfirebase2.RxFirebaseAuth
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_registration.*
import org.unesco.mgiep.dali.Activity.MainActivity
import org.unesco.mgiep.dali.Dagger.MyApplication
import org.unesco.mgiep.dali.Data.Gender
import org.unesco.mgiep.dali.Data.User
import org.unesco.mgiep.dali.R
import org.unesco.mgiep.dali.Repositories.MainReposirtory
import org.unesco.mgiep.dali.Utility.showFragment


class SignUp :Fragment() {

    private var gender = Gender.MALE
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mainReposirtory: MainReposirtory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity!!.application as MyApplication).component.inject(this)
        mAuth = FirebaseAuth.getInstance()
        mainReposirtory = MainReposirtory()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?=
            inflater.inflate(R.layout.fragment_registration, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

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
                edit_register_email.text.isEmpty() -> edit_register_email.error = getString(R.string.required)
                edit_register_name.text.isEmpty() -> edit_register_name.error = getString(R.string.required)
                edit_register_password.text.isEmpty() -> edit_register_password.error = getString(R.string.required)
                edit_register_school.text.isEmpty() -> edit_register_school.error = getString(R.string.required)
                edit_register_age.text.isEmpty() -> edit_register_age.error = getString(R.string.required)
                edit_register_age.text.toString().toInt() <= 0 -> edit_register_age.error = getString(R.string.invalid_age)
                edit_register_designation.text.isEmpty() -> edit_register_designation.error = getString(R.string.required)
                !edit_radio_male.isChecked && !edit_radio_female.isChecked -> {
                    Toast.makeText(activity, getString(R.string.select_gender),Toast.LENGTH_SHORT).show()
                }
                else->{
                    RxFirebaseAuth.createUserWithEmailAndPassword(mAuth,edit_register_email.text.toString(),edit_register_password.text.toString())
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe ({ authResult ->
                                Log.d("userid-on-signup", authResult.user.uid)
                                mainReposirtory.saveUser(
                                        authResult.user.uid,
                                        User(
                                                edit_register_email.text.toString(),
                                                edit_register_name.text.toString(),
                                                edit_register_designation.text.toString(),
                                                edit_register_school.text.toString(),
                                                edit_register_age.text.toString().toInt(),
                                                gender.toString()
                                        )
                                )
                                        .subscribeOn(Schedulers.newThread())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .doOnComplete{
                                            Toast.makeText(activity,getString(R.string.user_saved),Toast.LENGTH_SHORT).show()
                                            startActivity(Intent(activity,MainActivity::class.java))
                                        }
                                        .doOnError {
                                            Toast.makeText(activity,getString(R.string.user_sync_error),Toast.LENGTH_SHORT).show()
                                            Log.d("signup", getString(R.string.user_sync_error))
                                        }

                            },{
                                Toast.makeText(activity, getString(R.string.signup_fail), Toast.LENGTH_SHORT).show()
                                Log.d("signup", getString(R.string.signup_fail))
                            })
                }
            }
        }
    }
}