package org.unesco.mgiep.dali.Fragments

import android.app.DatePickerDialog
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_new_screening.*
import org.unesco.mgiep.dali.Activity.ScreeningActivity
import org.unesco.mgiep.dali.Data.*
import org.unesco.mgiep.dali.Data.Participant
import org.unesco.mgiep.dali.Data.Screening
import org.unesco.mgiep.dali.Data.ViewModels.ScreeningParticipantViewModel
import org.unesco.mgiep.dali.Data.ViewModels.ScreeningViewModel
import org.unesco.mgiep.dali.R
import org.unesco.mgiep.dali.Repositories.FirebaseRepository
import org.unesco.mgiep.dali.Repositories.MainReposirtory
import org.unesco.mgiep.dali.Utility.showFragment
import java.text.SimpleDateFormat
import java.util.*

class NewScreening2 : Fragment() {

    val calendar = Calendar.getInstance()

    val onlyDate = "MMM DD,YYYY"

    val sdf = SimpleDateFormat(onlyDate, Locale.ENGLISH)

    private var relationShipWithChild = Relationship.CT

    private var scheduleDate: Date = calendar.time

    private lateinit var screeningViewModel: ScreeningViewModel
    private lateinit var screeningParticipantViewModel: ScreeningParticipantViewModel

    private lateinit var mAuth: FirebaseAuth

    private lateinit var firebaseRepository: FirebaseRepository
    private lateinit var mainRepository: MainReposirtory

    private lateinit var intent : Intent

    private val participantId = UUID.randomUUID().toString()

    private var participant = Participant()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        screeningParticipantViewModel = ViewModelProviders.of(activity!!).get(ScreeningParticipantViewModel::class.java)
        screeningViewModel = ViewModelProviders.of(activity!!).get(ScreeningViewModel::class.java)
        mAuth = FirebaseAuth.getInstance()
        firebaseRepository = FirebaseRepository()
        mainRepository = MainReposirtory()
        intent = Intent(activity, ScreeningActivity::class.java)
        participant = screeningParticipantViewModel.getParticipant().value!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?=
            inflater.inflate(R.layout.fragment_newscreening2, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        radio_class_teacher.setOnClickListener {
            relationShipWithChild = Relationship.CT
        }
        radio_language_teacher.setOnClickListener {
            relationShipWithChild = Relationship.LT
        }
        radio_others.setOnClickListener {
            relationShipWithChild = Relationship.OT
        }

        edit_time_spent_with_child.setOnFocusChangeListener { view, b ->
            if(!b){
                if(edit_time_spent_with_child.text.isEmpty()){
                    edit_time_spent_with_child.hint = getString(R.string.estimated_time_spent_with_child)
                }else{
                    participant.timeSpentWithChild = edit_time_spent_with_child.text.toString().toInt()
                }
            }else{
                edit_time_spent_with_child.hint = ""
            }
        }

        val dateDialog = DatePickerDialog(
                activity,
                date2,
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.YEAR)
        )


        switch_schedule.setOnCheckedChangeListener { compoundButton, b ->
            if(compoundButton.isChecked){
                dateDialog.show()
                btn_screenreg_schedule_submit.visibility = View.VISIBLE
                btn_screenreg_submit.visibility = View.GONE
            }else{
                btn_change_scheduled_date.visibility = View.GONE
                btn_screenreg_schedule_submit.visibility = View.GONE
                btn_screenreg_submit.visibility = View.VISIBLE
            }
        }

        dateDialog.setOnCancelListener {
            switch_schedule.isChecked = false
        }

        btn_change_scheduled_date.setOnClickListener {
            DatePickerDialog(
                    activity,
                    date2,
                    calendar.get(Calendar.DAY_OF_MONTH),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.YEAR)
            ).show()
        }

        btn_screenreg_submit.setOnClickListener {
            Log.d("screening-reg","onClick")

            when {
                !radio_class_teacher.isChecked && !radio_language_teacher.isChecked && !radio_others.isChecked ->{
                    Toast.makeText(activity,getString(R.string.relationship_with_child_none_select_error), Toast.LENGTH_SHORT).show()
                }
                edit_time_spent_with_child.text.isEmpty() -> edit_time_spent_with_child.error = getString(R.string.required)
                else -> {
                    screeningParticipantViewModel.select(Participant(
                            id= participantId,
                            name = participant.name,
                            sClass = participant.sClass,
                            section = participant.section,
                            motherTongue = participant.motherTongue,
                            institution = participant.institution,
                            dob = participant.dob,
                            gender = participant.gender,
                            relationShipWithChild = relationShipWithChild.toString(),
                            timeSpentWithChild = edit_time_spent_with_child.text.toString().toInt(),
                            createdBy = mAuth.currentUser!!.uid
                    ))

                    mainRepository.saveParticipant(
                            participantId,
                            Participant(
                                    id= participantId,
                                    name = participant.name,
                                    sClass = participant.sClass,
                                    section = participant.section,
                                    motherTongue = participant.motherTongue,
                                    institution = participant.institution,
                                    dob = participant.dob,
                                    gender = participant.gender,
                                    relationShipWithChild = relationShipWithChild.toString(),
                                    timeSpentWithChild = edit_time_spent_with_child.text.toString().toInt(),
                                    createdBy = mAuth.currentUser!!.uid
                            )
                    )

                    val age = Date().year - Date(participant.dob).year
                    Log.d("Participant Age - ","$age")
                    intent.putExtra("participantId",participantId)
                    intent.putExtra("participantName", participant.name)
                    if(age <= 7){
                        intent.putExtra("type", Type.JST.toString())
                    }else{
                        intent.putExtra("type", Type.MST.toString())
                    }
                    startActivity(intent)

                }
            }
        }

        btn_screenreg_schedule_submit.setOnClickListener {
            when {
                !radio_class_teacher.isChecked && !radio_language_teacher.isChecked && !radio_others.isChecked ->{
                    Toast.makeText(activity,getString(R.string.relationship_with_child_none_select_error), Toast.LENGTH_SHORT).show()
                }
                edit_time_spent_with_child.text.isEmpty() -> edit_time_spent_with_child.error = getString(R.string.required)
                else -> {
                    mainRepository.saveParticipant(
                            participantId,
                            Participant(
                                    id= participantId,
                                    name = participant.name,
                                    sClass = participant.sClass,
                                    section = participant.section,
                                    motherTongue = participant.motherTongue,
                                    institution = participant.institution,
                                    dob = participant.dob,
                                    gender = participant.gender,
                                    relationShipWithChild = relationShipWithChild.toString(),
                                    timeSpentWithChild = edit_time_spent_with_child.text.toString().toInt(),
                                    createdBy = mAuth.currentUser!!.uid
                            )
                    )


                    val age = calendar.time.year - Date(participant.dob).year
                    if(age <= 7){
                        mainRepository.saveScreening(
                                UUID.randomUUID().toString(),
                                Screening(
                                        type = Type.JST.toString(),
                                        completed = false,
                                        mediumOfInstruction = AppPref.locale,
                                        participantId = participantId,
                                        userId = mAuth.uid.toString(),
                                        totalScore = 0,
                                        scheduledDate = scheduleDate.time,
                                        comments = "",
                                        participantName = participant.name
                                )
                        )

                    }else{
                        mainRepository.saveScreening(
                                UUID.randomUUID().toString(),
                                Screening(
                                        type = Type.MST.toString(),
                                        completed = false,
                                        mediumOfInstruction = AppPref.locale,
                                        participantId = participantId,
                                        userId = mAuth.uid.toString(),
                                        totalScore = 0,
                                        scheduledDate = scheduleDate.time,
                                        comments = "",
                                        participantName = participant.name
                                )
                        )

                    }

                    showFragment(
                            Fragment.instantiate(
                                    activity,
                                    Dashboard::class.java.name
                            ),
                            false
                    )
                }
            }
        }

    }

    var date2 = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, monthOfYear)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        scheduleDate = calendar.time
        btn_change_scheduled_date.visibility = View.VISIBLE
        tv_screenreg_schedule_date.text = sdf.format(scheduleDate)
    }

    private fun showFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        fragment.showFragment(container = R.id.fragment_container,
                fragmentManager = activity!!.supportFragmentManager,
                addToBackStack = addToBackStack)
    }
}