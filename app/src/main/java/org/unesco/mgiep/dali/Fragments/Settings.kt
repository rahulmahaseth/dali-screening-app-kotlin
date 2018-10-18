package org.unesco.mgiep.dali.Fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.drawer_layout.*
import kotlinx.android.synthetic.main.fragment_settings.*
import org.unesco.mgiep.dali.Dagger.MyApplication
import org.unesco.mgiep.dali.Data.AppPref
import org.unesco.mgiep.dali.R
import org.unesco.mgiep.dali.Repositories.MainReposirtory
import org.unesco.mgiep.dali.Utility.hide

class Settings : Fragment() {

    private lateinit var mainReposirtory: MainReposirtory
    private lateinit var mAuth: FirebaseAuth
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity!!.application as MyApplication).component.inject(this)
        firebaseAnalytics = FirebaseAnalytics.getInstance(context!!)
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
        user_email.text = AppPref(activity!!.applicationContext).userEmail
        if(AppPref(activity!!.applicationContext).userInstitution.isEmpty()){
            card_user_institution.hide()
        }else{
            user_institution.text = AppPref(activity!!.applicationContext).userInstitution
        }


    }

    override fun onResume() {
        super.onResume()
        firebaseAnalytics.setCurrentScreen(activity!!, "settings", Settings::class.java.simpleName)
        activity!!.title = getString(R.string.profile)
    }
}