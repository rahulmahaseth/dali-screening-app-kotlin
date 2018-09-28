package org.unesco.mgiep.dali.Fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.drawer_layout.*
import kotlinx.android.synthetic.main.fragment_settings.*
import org.unesco.mgiep.dali.Dagger.MyApplication
import org.unesco.mgiep.dali.Data.AppPref
import org.unesco.mgiep.dali.R
import org.unesco.mgiep.dali.Repositories.MainReposirtory

class Settings : Fragment() {

    private lateinit var mainReposirtory: MainReposirtory
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity!!.application as MyApplication).component.inject(this)
        mainReposirtory = MainReposirtory()
        mAuth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?=
            inflater.inflate(R.layout.fragment_settings, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity!!.nav_view.menu.getItem(3).isChecked = true

        /*if(user_name.text == ""){
            fetchUser()
        }*/
        user_name.text = AppPref(activity!!.applicationContext).userName

        user_age.text = AppPref(activity!!.applicationContext).userAge
        user_designation.text = AppPref(activity!!.applicationContext).userDesignation
        user_email.text = AppPref(activity!!.applicationContext).userEmail
        user_institution.text = AppPref(activity!!.applicationContext).userInstitution


    }

   /* private fun fetchUser(){
        setting_progressBar?.show()
        mainReposirtory.getUser(mAuth.currentUser!!.uid)
                .addOnSuccessListener {
                    if(it.exists()){
                        val user = it.toObject(User::class.java)
                        AppPref(activity!!.applicationContext).userEmail = user!!.email
                        AppPref(activity!!.applicationContext).userName = user.name
                        AppPref(activity!!.applicationContext).userDesignation = user.designation
                        AppPref(activity!!.applicationContext).userInstitution = user.institution
                        AppPref(activity!!.applicationContext).userAge = user.age.toString()
                        setting_progressBar?.hide()
                        refreshView()

                    }else{
                        setting_progressBar?.hide()
                        getString(R.string.user_sync_error).showAsToast(activity!!)
                    }
                }
                .addOnCanceledListener {
                    setting_progressBar?.hide()
                    getString(R.string.network_error).showAsToast(activity!!)
                }
    }
    */
/*

    private fun refreshView(){
        val curr = fragmentManager!!.findFragmentByTag(Settings::class.java.name)
        val fragTransaction = fragmentManager!!.beginTransaction()
        fragTransaction.detach(curr)
        fragTransaction.attach(curr)
        fragTransaction.commit()
    }

    private fun showFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        fragment.showFragment(container = R.id.fragment_container,
                fragmentManager = activity!!.supportFragmentManager,
                addToBackStack = addToBackStack,
                animate = false)
    }
    */

    override fun onResume() {
        super.onResume()
        activity!!.title = getString(R.string.profile)
    }
}