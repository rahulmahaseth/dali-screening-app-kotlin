package org.unesco.mgiep.dali.Fragments

import android.arch.lifecycle.ViewModelProviders
import android.databinding.ObservableArrayList
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_screening_detail.*
import org.unesco.mgiep.dali.Dagger.MyApplication
import org.unesco.mgiep.dali.Data.FirebaseScreening
import org.unesco.mgiep.dali.Data.Participant
import org.unesco.mgiep.dali.Data.ViewModels.ScreeningViewModel
import org.unesco.mgiep.dali.R
import org.unesco.mgiep.dali.Repositories.FirebaseRepository
import org.unesco.mgiep.dali.Repositories.MainReposirtory
import org.unesco.mgiep.dali.Utility.showFragment


class ScreeningDetails: Fragment() {

    private lateinit var participant: Participant
    private lateinit var screening: FirebaseScreening
    private lateinit var screeningViewModel: ScreeningViewModel
    private lateinit var firebaseRepository: FirebaseRepository
    private lateinit var mainRepository: MainReposirtory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity!!.application as MyApplication).component.inject(this)
        screeningViewModel = ViewModelProviders.of(activity!!).get(ScreeningViewModel::class.java)
        firebaseRepository = FirebaseRepository()
        mainRepository = MainReposirtory()
        screening = screeningViewModel.getScreening().value!!


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?=
            inflater.inflate(R.layout.fragment_screening_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainRepository.getParticipant(screening.participantId)!!
                .doOnSuccess {
                    if(it.exists()){
                        participant = it.toObject(Participant::class.java)!!
                        Log.d("Participant-fetch","Success")
                        tv_screeeningdetail_name.text = participant.name
                        tv_screeningdetail_language.text = screening.mediumOfInstruction
                        tv_screeningdetail_score.text = screening.totalScore.toString()
                        tv_screeningdetail_type.text = screening.type
                        tv_screeeningdetail_comment.text = screening.comments
                    }else{
                        Log.d("Participant-fetch","Failure")
                    }
                }
                .doOnError {
                    Log.d("Participant-fetch","Error",it)
                }


    }

    private fun showFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        fragment.showFragment(container = R.id.fragment_container,
                fragmentManager = activity!!.supportFragmentManager,
                addToBackStack = addToBackStack)
    }
}