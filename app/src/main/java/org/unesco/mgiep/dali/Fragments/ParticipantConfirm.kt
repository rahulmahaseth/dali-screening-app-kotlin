package org.unesco.mgiep.dali.Fragments

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_participantconfirm.*
import org.unesco.mgiep.dali.Activity.LanguageSelect
import org.unesco.mgiep.dali.Activity.MainActivity
import org.unesco.mgiep.dali.Dagger.MyApplication
import org.unesco.mgiep.dali.Data.*
import org.unesco.mgiep.dali.Data.Screening
import org.unesco.mgiep.dali.Data.ViewModels.ScreeningParticipantViewModel
import org.unesco.mgiep.dali.R
import org.unesco.mgiep.dali.Repositories.FirebaseRepository
import org.unesco.mgiep.dali.Repositories.MainReposirtory
import org.unesco.mgiep.dali.Utility.hide
import org.unesco.mgiep.dali.Utility.show
import org.unesco.mgiep.dali.Utility.showAsToast
import java.text.SimpleDateFormat
import java.util.*

class ParticipantConfirm: Fragment() {

    val calendar = Calendar.getInstance()
    val onlyDate = "dd/MM/yyyy"

    val sdf = SimpleDateFormat(onlyDate, Locale.ENGLISH)
    private lateinit var screeningParticipantViewModel: ScreeningParticipantViewModel
    private lateinit var firebaseRepository: FirebaseRepository
    private lateinit var mainRepository: MainReposirtory
    private lateinit var intent : Intent
    private var participant = Participant()
    private val participantId = UUID.randomUUID().toString()
    private val screeningId = UUID.randomUUID().toString()
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity!!.application as MyApplication).component.inject(this)
        screeningParticipantViewModel = ViewModelProviders.of(activity!!).get(ScreeningParticipantViewModel::class.java)
        firebaseRepository = FirebaseRepository()
        mainRepository = MainReposirtory()
        intent = Intent(activity, LanguageSelect::class.java)
        participant = screeningParticipantViewModel.getParticipant().value!!
        mAuth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_participantconfirm, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tv_confirm_participant_name.text = participant.name
        tv_confirm_participant_class.text = participant.sClass.toString()
        tv_confirm_participant_gender.text = participant.gender
        tv_confirm_participant_mother_tongue.text = participant.motherTongue

        when {
            participant.relationShipWithChild == Relationship.LT.toString() -> tv_confirm_participant_relationship.text = getString(R.string.language_teacher)
            participant.relationShipWithChild == Relationship.CT.toString() -> tv_confirm_participant_relationship.text = getString(R.string.class_teacher)
            else -> tv_confirm_participant_relationship.text = getString(R.string.other)
        }

        tv_confirm_participant_timespent.text = participant.timeSpentWithChild.toString()
        tv_confirm_participant_school.text = participant.institution
        tv_confirm_participant_section.text = participant.section
        tv_confirm_participant_dob.text = sdf.format(participant.dob)

        btn_confirm_partiicpant.setOnClickListener {
            saveParticipant()
        }

        btn_confirm_partiicpant_save.setOnClickListener {
            confirmparticiapnt_progressBar?.show()
            mainRepository.saveParticipant(participant.id,participant)
                    .addOnSuccessListener {
                        val age = calendar.time.year - Date(participant.dob).year
                        if(age <= 7){
                            saveScreening(Type.JST.toString())
                        }else{
                            saveScreening(Type.MST.toString())
                        }
                    }
                    .addOnCanceledListener {
                        confirmparticiapnt_progressBar?.hide()
                        getString(R.string.participate_saving_error).showAsToast(activity!!)
                    }
        }
    }

    private fun saveParticipant() {
        confirmparticiapnt_progressBar?.show()
        mainRepository.saveParticipant(participant.id, participant)
                .addOnSuccessListener {
                    startScreening()
                }
                .addOnCanceledListener {
                    confirmparticiapnt_progressBar?.hide()
                    getString(R.string.participate_saving_error).showAsToast(activity!!)
                }
    }


    private fun startScreening(){
        val age = Date().year - Date(participant.dob).year
        Log.d("ParticipantDetailAge - ","$age")
        intent.putExtra("screeningId", screeningId)
        intent.putExtra("participantId",participantId)
        intent.putExtra("participantName", participant.name)
        if(age <= 7){
            intent.putExtra("type", Type.JST.toString())
        }else{
            intent.putExtra("type", Type.MST.toString())
        }
        confirmparticiapnt_progressBar?.hide()
        startActivity(intent)
        activity!!.finish()
    }

    private fun saveScreening(type: String){
        mainRepository.saveScreening(
                UUID.randomUUID().toString(),
                Screening(
                        id = screeningId,
                        type = type,
                        completed = false,
                        mediumOfInstruction = AppPref(activity!!.baseContext).locale,
                        participantId = participantId,
                        userId = mAuth.uid.toString(),
                        totalScore = 0,
                        scheduledDate = Date().time,
                        comments = "",
                        participantName = participant.name
                )
        )
                .addOnSuccessListener {
                    confirmparticiapnt_progressBar?.hide()
                    getString(R.string.screening_saved).showAsToast(activity!!)
                    startActivity(Intent(activity, MainActivity::class.java))
                    activity!!.finish()
                }
                .addOnCanceledListener {
                    confirmparticiapnt_progressBar?.hide()
                    getString(R.string.participate_saving_error).showAsToast(activity!!)
                }
    }

}