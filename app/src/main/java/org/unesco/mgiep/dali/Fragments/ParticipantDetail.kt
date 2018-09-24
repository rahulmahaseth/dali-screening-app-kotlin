package org.unesco.mgiep.dali.Fragments

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_participant.*
import org.unesco.mgiep.dali.Activity.MainActivity
import org.unesco.mgiep.dali.Activity.ScreeningActivity
import org.unesco.mgiep.dali.Dagger.MyApplication
import org.unesco.mgiep.dali.Data.Participant
import org.unesco.mgiep.dali.Data.Screening
import org.unesco.mgiep.dali.Data.Type
import org.unesco.mgiep.dali.Data.ViewModels.ScreeningParticipantViewModel
import org.unesco.mgiep.dali.R
import org.unesco.mgiep.dali.Repositories.FirebaseRepository
import org.unesco.mgiep.dali.Utility.hide
import org.unesco.mgiep.dali.Utility.show
import org.unesco.mgiep.dali.Utility.showFragment
import java.util.*


class ParticipantDetail: Fragment() {

    private lateinit var participantViewModel: ScreeningParticipantViewModel
    private lateinit var participant: Participant
    private lateinit var screening: Screening
    private lateinit var firebaseRepository: FirebaseRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity!!.application as MyApplication).component.inject(this)
        participantViewModel = ViewModelProviders.of(activity!!).get(ScreeningParticipantViewModel::class.java)
        activity!!.title = participantViewModel.getParticipant().value!!.name
        participant = participantViewModel.getParticipant().value!!
        firebaseRepository = FirebaseRepository()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?=
            inflater.inflate(R.layout.fragment_participant, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        tv_participant_name.text = participant.name
        tv_participant_class.text = participant.sClass.toString()
        tv_participant_section.text = participant.section
        tv_participant_instituion.text = participant.institution
        tv_participant_mother_tongue.text = participant.motherTongue
        tv_participant_age.text = (Date().year - Date(participant.dob).year).toString()

        participant_progressBar?.show()

        firebaseRepository.fetchParticipantScreenings(participant.id)
                .addOnSuccessListener {
                    if(!it.isEmpty){
                        it.documents.forEach {
                            screening = it.toObject(Screening::class.java)!!
                            participant_progressBar?.hide()
                            tv_participant_score.text = "${screening.totalScore}"
                            if(screening.type == Type.JST.toString()){
                                tv_participant_total.text = "15"
                            }else{
                                tv_participant_total.text = "21"
                            }
                        }
                    }else{
                        participant_progressBar?.hide()
                        Log.d("fetchPScreenings","empty doc")
                    }
                }
                .addOnFailureListener {
                    participant_progressBar?.hide()
                    Log.d("fetchPScreenings","error")
                }

        btn_participant_done.setOnClickListener {

                showFragment(
                        Fragment.instantiate(
                                activity,
                                ParticipantList::class.java.name
                        ),
                        false
                )
            }

    }

    private fun showFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        fragment.showFragment(container = R.id.fragment_container,
                fragmentManager = activity!!.supportFragmentManager,
                addToBackStack = addToBackStack)
    }
}