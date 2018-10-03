package org.unesco.mgiep.dali.Fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import kotlinx.android.synthetic.main.fragment_post_registration_info.*
import org.unesco.mgiep.dali.Activity.MainActivity
import org.unesco.mgiep.dali.R

class PostSignUpInfo : Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_post_registration_info, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        btn_submit_post_reg.setOnClickListener {
            startActivity(Intent(activity, MainActivity::class.java))
            activity!!.finish()
        }
    }
}