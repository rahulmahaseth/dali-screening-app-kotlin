package org.unesco.mgiep.dali.Fragments

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_pendingscreenings.*
import kotlinx.android.synthetic.main.fragment_screening_detail.*
import org.unesco.mgiep.dali.Activity.ScreeningActivity
import org.unesco.mgiep.dali.Dagger.MyApplication
import org.unesco.mgiep.dali.Data.Screening
import org.unesco.mgiep.dali.Data.Participant
import org.unesco.mgiep.dali.Data.ViewModels.ScreeningViewModel
import org.unesco.mgiep.dali.R
import org.unesco.mgiep.dali.Repositories.FirebaseRepository
import org.unesco.mgiep.dali.Repositories.MainReposirtory
import org.unesco.mgiep.dali.Utility.hide
import org.unesco.mgiep.dali.Utility.show
import org.unesco.mgiep.dali.Utility.showFragment


class ScreeningDetails: Fragment() {

    private lateinit var participant: Participant
    private lateinit var screening: Screening
    private lateinit var screeningViewModel: ScreeningViewModel
    private lateinit var firebaseRepository: FirebaseRepository
    private lateinit var mainRepository: MainReposirtory

    private var scheduled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity!!.application as MyApplication).component.inject(this)
        screeningViewModel = ViewModelProviders.of(activity!!).get(ScreeningViewModel::class.java)
        firebaseRepository = FirebaseRepository()
        mainRepository = MainReposirtory()
        screening = screeningViewModel.getScreening().value!!
        scheduled = !screening.completed
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?=
            inflater.inflate(R.layout.fragment_screening_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        screeningdetail_progressBar.show()
        mainRepository.getParticipant(screening.participantId)
                .addOnSuccessListener {
                    if(it.exists()){
                        participant = it.toObject(Participant::class.java)!!
                        Log.d("ParticipantDetail-fetch","Success")
                        tv_screeeningdetail_name.text = participant.name
                        tv_screeningdetail_language.text = screening.mediumOfInstruction
                        tv_screeningdetail_score.text = screening.totalScore.toString()
                        tv_screeningdetail_type.text = screening.type
                        tv_screeeningdetail_comment.text = screening.comments
                        screeningdetail_progressBar.hide()
                    }else{
                        Log.d("ParticipantDetail-fetch","Failure")
                        screeningdetail_progressBar.hide()
                        Toast.makeText(activity, getString(R.string.data_not_found), Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    Log.d("ParticipantDetail-fetch","Error",it)
                    screeningdetail_progressBar.hide()
                    Toast.makeText(activity, getString(R.string.network_error), Toast.LENGTH_SHORT).show()
                }

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