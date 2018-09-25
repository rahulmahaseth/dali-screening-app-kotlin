package org.unesco.mgiep.dali.Fragments

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_pendingscreenings.*
import kotlinx.android.synthetic.main.fragment_screening_detail.*
import kotlinx.android.synthetic.main.fragment_screening_detail.view.*
import org.unesco.mgiep.dali.Activity.ScreeningActivity
import org.unesco.mgiep.dali.Dagger.MyApplication
import org.unesco.mgiep.dali.Data.Screening
import org.unesco.mgiep.dali.Data.Participant
import org.unesco.mgiep.dali.Data.Type
import org.unesco.mgiep.dali.Data.ViewModels.ScreeningParticipantViewModel
import org.unesco.mgiep.dali.Data.ViewModels.ScreeningViewModel
import org.unesco.mgiep.dali.R
import org.unesco.mgiep.dali.Repositories.FirebaseRepository
import org.unesco.mgiep.dali.Repositories.MainReposirtory
import org.unesco.mgiep.dali.Utility.hide
import org.unesco.mgiep.dali.Utility.show
import org.unesco.mgiep.dali.Utility.showAsToast
import org.unesco.mgiep.dali.Utility.showFragment


class ScreeningDetails: Fragment() {

    private lateinit var participant: Participant
    private lateinit var screening: Screening
    private lateinit var screeningViewModel: ScreeningViewModel
    private lateinit var participantViewModel: ScreeningParticipantViewModel
    private lateinit var firebaseRepository: FirebaseRepository
    private lateinit var mainRepository: MainReposirtory

    private var scheduled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity!!.application as MyApplication).component.inject(this)
        screeningViewModel = ViewModelProviders.of(activity!!).get(ScreeningViewModel::class.java)
        participantViewModel = ViewModelProviders.of(activity!!).get(ScreeningParticipantViewModel::class.java)
        firebaseRepository = FirebaseRepository()
        mainRepository = MainReposirtory()
        screening = screeningViewModel.getScreening().value!!
        participant = participantViewModel.getParticipant().value!!
        scheduled = !screening.completed
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?=
            inflater.inflate(R.layout.fragment_screening_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       /* screeningdetail_progressBar.setOnKeyListener { view, i, keyEvent ->
            if(i == KeyEvent.KEYCODE_BACK && !keyEvent.isCanceled){
                if(screeningdetail_progressBar.isEnabled){
                    screeningdetail_progressBar.hide()
                }
                true
            }
            false
        }
            */
        tv_screeeningdetail_name.text = participant.name
        tv_screeningdetail_class.text = participant.sClass.toString()
        tv_screeeningdetail_section.text = participant.section
        tv_screeningdetail_language.text = screening.mediumOfInstruction
        tv_screeningdetail_score.text = screening.totalScore.toString()
        tv_screeningdetail_type.text = screening.type
        tv_screeeningdetail_comment.text = screening.comments
        tv_screening_detail_separator.text = "/"
        tv_screeningdetail_total_score.text = if(screening.type == Type.JST.toString())"30" else "42"

        if(scheduled){
            screening_comment_layout.visibility = View.GONE
            btn_screening_detail_start.visibility = View.VISIBLE
        }

        btn_screening_detail_start.setOnClickListener {
            startActivity(Intent(activity!!, ScreeningActivity::class.java)
                    .putExtra("screeningId", screening.id)
                    .putExtra("type", screening.type)
                    .putExtra("participantId", screening.participantId)
                    .putExtra("participantName", screening.participantName))
        }
    }

    override fun onResume() {
        super.onResume()
        activity!!.title = screening.participantName
    }

    private fun showFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        fragment.showFragment(container = R.id.fragment_container,
                fragmentManager = activity!!.supportFragmentManager,
                addToBackStack = addToBackStack)
    }
}