package org.unesco.mgiep.dali.Fragments

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.android.synthetic.main.fragment_comments.*
import org.unesco.mgiep.dali.Activity.ResultActivity
import org.unesco.mgiep.dali.Dagger.MyApplication
import org.unesco.mgiep.dali.Data.AppPref
import org.unesco.mgiep.dali.Data.QuestionWiseScore
import org.unesco.mgiep.dali.Data.Screening
import org.unesco.mgiep.dali.Data.ViewModels.QuestionWiseScoreViewModel
import org.unesco.mgiep.dali.Data.ViewModels.ScreeningViewModel
import org.unesco.mgiep.dali.R
import org.unesco.mgiep.dali.Repositories.FirebaseRepository
import org.unesco.mgiep.dali.Repositories.MainReposirtory
import org.unesco.mgiep.dali.Utility.hide
import org.unesco.mgiep.dali.Utility.show
import org.unesco.mgiep.dali.Utility.showAsToast

class Comments: Fragment(){
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var screeningViewModel: ScreeningViewModel
    private lateinit var questionWiseScoreViewModel: QuestionWiseScoreViewModel
    private lateinit var questionWiseScore: QuestionWiseScore
    lateinit var mainReposirtory: MainReposirtory
    lateinit var firebaseRepository: FirebaseRepository
    lateinit var screening: Screening

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity!!.application as MyApplication).component.inject(this)
        firebaseAnalytics = FirebaseAnalytics.getInstance(context!!)
        mainReposirtory = MainReposirtory()
        firebaseRepository = FirebaseRepository()
        screeningViewModel = ViewModelProviders.of(activity!!).get(ScreeningViewModel::class.java)
        questionWiseScoreViewModel = ViewModelProviders.of(activity!!).get(QuestionWiseScoreViewModel::class.java)
        screening = screeningViewModel.getScreening().value!!.copy()
        questionWiseScore = questionWiseScoreViewModel.getScore().value!!.copy()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?=
            inflater.inflate(R.layout.fragment_comments, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        btn_submit_screening.setOnClickListener { v ->
            screening.comments = edit_comments.text.toString()
            comments_progressBar?.show()
            disableViews()
            AppPref(activity!!).loading = true
            mainReposirtory.saveScreening(screening.id, screening)
                    .addOnSuccessListener {
                        mainReposirtory.saveScore(questionWiseScore.id, questionWiseScore)
                                .addOnSuccessListener {
                                    comments_progressBar?.hide()
                                    AppPref(activity!!).loading = false
                                    getString(R.string.screening_saved).showAsToast(activity!!)
                                    startActivity(
                                            Intent(activity, ResultActivity::class.java)
                                                    .putExtra("screening", true)
                                                    .putExtra("name",screening.participantName)
                                                    .putExtra("score",screening.totalScore)
                                                    .putExtra("type",screening.type)
                                                    .putExtra("language", screening.assesmentLanguage)
                                    )
                                    activity!!.finish()
                                }
                                .addOnCanceledListener {
                                    comments_progressBar?.hide()
                                    AppPref(activity!!).loading = false
                                    enableViews()
                                    getString(R.string.save_screening_error).showAsToast(activity!!, true)
                                }
                    }
                    .addOnFailureListener {
                        comments_progressBar?.hide()
                        AppPref(activity!!).loading = false
                        enableViews()
                        getString(R.string.save_screening_error).showAsToast(activity!!, true)
                    }
        }
    }

    private fun disableViews(){
        edit_comments.isEnabled = false
        btn_submit_screening.isEnabled = false
    }

    private fun enableViews(){
        edit_comments.isEnabled = true
        btn_submit_screening.isEnabled = true
    }

    override fun onResume() {
        super.onResume()
        firebaseAnalytics.setCurrentScreen(activity!!, "comments", Comments::class.java.simpleName)
    }

}