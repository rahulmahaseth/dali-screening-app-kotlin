package org.unesco.mgiep.dali.Fragments

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_comments.*
import org.unesco.mgiep.dali.Activity.MainActivity
import org.unesco.mgiep.dali.Activity.ResultActivity
import org.unesco.mgiep.dali.Dagger.MyApplication
import org.unesco.mgiep.dali.Data.Screening
import org.unesco.mgiep.dali.Data.ViewModels.ScreeningViewModel
import org.unesco.mgiep.dali.R
import org.unesco.mgiep.dali.Repositories.FirebaseRepository
import org.unesco.mgiep.dali.Repositories.MainReposirtory
import org.unesco.mgiep.dali.Utility.hide
import org.unesco.mgiep.dali.Utility.show
import org.unesco.mgiep.dali.Utility.showAsToast
import java.util.*

class Comments: Fragment(){

    private lateinit var screeningViewModel: ScreeningViewModel
    lateinit var mainReposirtory: MainReposirtory
    lateinit var firebaseRepository: FirebaseRepository
    lateinit var screening: Screening

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity!!.application as MyApplication).component.inject(this)
        mainReposirtory = MainReposirtory()
        firebaseRepository = FirebaseRepository()
        screeningViewModel = ViewModelProviders.of(activity!!).get(ScreeningViewModel::class.java)
        screening = screeningViewModel.getScreening().value!!.copy()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?=
            inflater.inflate(R.layout.fragment_comments, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        btn_submit_screening.setOnClickListener { v ->
            screening.comments = edit_comments.text.toString()
            comments_progressBar?.show()
            btn_submit_screening.isEnabled = false
            mainReposirtory.saveScreening(screening.id, screening)
                    .addOnSuccessListener {
                        comments_progressBar?.hide()
                        startActivity(
                                Intent(activity, ResultActivity::class.java)
                                        .putExtra("name",screening.participantName)
                                        .putExtra("score",screening.totalScore)
                                        .putExtra("type",screening.type)
                                        .putExtra("language", screening.assesmentLanguage)
                        )
                    }
                    .addOnFailureListener {
                        comments_progressBar?.hide()
                        getString(R.string.save_screening_error).showAsToast(activity!!)
                    }
        }
    }
}