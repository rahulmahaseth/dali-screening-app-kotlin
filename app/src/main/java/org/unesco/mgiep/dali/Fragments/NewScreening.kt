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
import org.unesco.mgiep.dali.Dagger.MyApplication
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

class NewScreening : Fragment() {

    val calendar = Calendar.getInstance()

    val myFormat = "dd/MM/yyyy"
    val onlyDate = "MMM DD,YYYY"

    val sdf = SimpleDateFormat(myFormat, Locale.ENGLISH)
    val sdf2 = SimpleDateFormat(onlyDate, Locale.ENGLISH)

    private var gender = Gender.MALE
    private var relationShipWithChild = Relationship.CT

    private var selectedDate : Date = calendar.time
    private var scheduleDate: Date = calendar.time

    private lateinit var screeningViewModel: ScreeningViewModel
    private lateinit var screeningParticipantViewModel: ScreeningParticipantViewModel

    private lateinit var mAuth: FirebaseAuth

    private lateinit var firebaseRepository: FirebaseRepository
    private lateinit var mainRepository: MainReposirtory

    private lateinit var intent : Intent

    private val participantId = UUID.randomUUID().toString()

    private val participant = Participant()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity!!.application as MyApplication).component.inject(this)
        screeningParticipantViewModel = ViewModelProviders.of(activity!!).get(ScreeningParticipantViewModel::class.java)
        screeningViewModel = ViewModelProviders.of(activity!!).get(ScreeningViewModel::class.java)
        mAuth = FirebaseAuth.getInstance()
        firebaseRepository = FirebaseRepository()
        mainRepository = MainReposirtory()
        intent = Intent(activity, ScreeningActivity::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?=
            inflater.inflate(R.layout.fragment_new_screening, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        edit_regscreen_radio_female.setOnClickListener {
            gender = Gender.FEMALE
        }

        edit_regscreen_radio_male.setOnClickListener {
            gender = Gender.MALE
        }

        radio_class_teacher.setOnClickListener {
            relationShipWithChild = Relationship.CT
        }
        radio_language_teacher.setOnClickListener {
            relationShipWithChild = Relationship.LT
        }
        radio_others.setOnClickListener {
            relationShipWithChild = Relationship.OT
        }

        edit_regscreen_name.setOnFocusChangeListener { view, b ->
            if(!b){
                if(edit_regscreen_name.text.isEmpty()){
                    edit_regscreen_name.hint = getString(R.string.name)
                }else{
                    participant.name = edit_regscreen_name.text.toString()
                }
            }else{
                edit_regscreen_name.hint = ""
            }
        }

        edit_regscreen_class.setOnFocusChangeListener { view, b ->
            if(!b){
                if(edit_regscreen_class.text.isEmpty()){
                    edit_regscreen_class.hint = getString(R.string.sgd)
                }else{
                    participant.sClass = edit_regscreen_class.text.toString().toInt()
                }
            }else{
                edit_regscreen_class.hint = ""
            }
        }

        edit_regscreen_section.setOnFocusChangeListener { view, b ->
            if(!b){
                if(edit_regscreen_section.text.isEmpty()){
                    edit_regscreen_section.hint = getString(R.string.section)
                }else{
                    participant.section = edit_regscreen_section.text.toString()
                }
            }else{
                edit_regscreen_section.hint = ""
            }
        }

        edit_regscreen_mothertongue.setOnFocusChangeListener { view, b ->
            if(!b){
                if(edit_regscreen_mothertongue.text.isEmpty()){
                    edit_regscreen_mothertongue.hint = getString(R.string.mother_tongue)
                }else{
                    participant.motherTongue = edit_regscreen_mothertongue.text.toString()
                }
            }else{
                edit_regscreen_mothertongue.hint = ""
            }
        }

        edit_regscreen_school.setOnFocusChangeListener { view, b ->
            if(!b){
                if(edit_regscreen_school.text.isEmpty()){
                    edit_regscreen_school.hint = getString(R.string.school_university)
                }else{
                    participant.institution = edit_regscreen_school.text.toString()
                }
            }else{
                edit_regscreen_school.hint = ""
            }
        }

        edit_regscreen_dob.setOnFocusChangeListener { view, b ->
            if(!b){
                if(edit_regscreen_dob.text.isEmpty()){
                    edit_regscreen_dob.hint = getString(R.string.date_of_birth)
                }else{
                    participant.dob = edit_regscreen_dob.text.toString().toLong()
                }
            }else{
                edit_regscreen_dob.hint = ""
            }
        }

        edit_time_spent_with_child.setOnFocusChangeListener { view, b ->
            if(!b){
                if(edit_time_spent_with_child.text.isEmpty()){
                    edit_time_spent_with_child.hint = getString(R.string.estimated_time_spent_with_child)
                }else{
                    participant.name = edit_time_spent_with_child.text.toString()
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

        btn_submit_participant.setOnClickListener {
            when{
                edit_regscreen_name.text.isEmpty()->edit_regscreen_name.error = getString(R.string.required)
                edit_regscreen_class.text.isEmpty() -> edit_regscreen_class.error = getString(R.string.required)
                edit_regscreen_section.text.isEmpty() -> edit_regscreen_section.error = getString(R.string.required)
                edit_regscreen_mothertongue.text.isEmpty()-> edit_regscreen_name.error = getString(R.string.required)
                edit_regscreen_school.text.isEmpty() -> edit_regscreen_school.error = getString(R.string.required)
                edit_regscreen_dob.text.isEmpty() -> edit_regscreen_dob.error = getString(R.string.required)
                !edit_regscreen_radio_female.isChecked && !edit_regscreen_radio_male.isChecked ->{
                    Toast.makeText(activity,getString(R.string.select_gender),Toast.LENGTH_SHORT).show()
                }
                else->{
                    participant_input_layout.visibility = View.GONE
                    metadata_input_layout.visibility = View.VISIBLE
                }
            }
        }

        btn_screenreg_submit.setOnClickListener {
            Log.d("screening-reg","onClick")

            when {
                !radio_class_teacher.isChecked && !radio_language_teacher.isChecked && !radio_others.isChecked ->{
                    Toast.makeText(activity,getString(R.string.relationship_with_child_none_select_error),Toast.LENGTH_SHORT).show()
                }
                edit_time_spent_with_child.text.isEmpty() -> edit_time_spent_with_child.error = getString(R.string.required)
                else -> {
                    screeningParticipantViewModel.select(Participant(
                            id= participantId,
                            name = edit_regscreen_name.text.toString(),
                            sClass = edit_regscreen_class.text.toString().toInt(),
                            section = edit_regscreen_section.text.toString(),
                            motherTongue = edit_regscreen_mothertongue.text.toString(),
                            institution = edit_regscreen_school.text.toString(),
                            dob = selectedDate.time,
                            gender = gender.toString(),
                            relationShipWithChild = relationShipWithChild.toString(),
                            timeSpentWithChild = edit_time_spent_with_child.text.toString().toInt(),
                            createdBy = mAuth.currentUser!!.uid
                    ))

                    mainRepository.saveParticipant(
                            participantId,
                            Participant(
                                    id= participantId,
                                    name = edit_regscreen_name.text.toString(),
                                    sClass = edit_regscreen_class.text.toString().toInt(),
                                    section = edit_regscreen_section.text.toString(),
                                    motherTongue = edit_regscreen_mothertongue.text.toString(),
                                    institution = edit_regscreen_school.text.toString(),
                                    dob = selectedDate.time,
                                    gender = gender.toString(),
                                    relationShipWithChild = relationShipWithChild.toString(),
                                    timeSpentWithChild = edit_time_spent_with_child.text.toString().toInt(),
                                    createdBy = mAuth.currentUser!!.uid
                            )
                    )

                    val age = Date().year - selectedDate.year
                    Log.d("Participant Age - ","$age")
                    intent.putExtra("participantId",participantId)
                    intent.putExtra("participantName", edit_regscreen_name.text.toString())
                    if(age <= 7){
                        intent.putExtra("type",Type.JST.toString())
                    }else{
                        intent.putExtra("type",Type.MST.toString())
                    }
                    startActivity(intent)

                }
            }
        }

        edit_regscreen_dob.setOnClickListener {
            DatePickerDialog(
                    activity,
                    date,
                    calendar.get(Calendar.DAY_OF_MONTH),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.YEAR)
            ).show()
        }

        btn_screenreg_schedule_submit.setOnClickListener {
            when {
                edit_regscreen_name.text.isEmpty()->edit_regscreen_name.error = getString(R.string.required)
                edit_regscreen_mothertongue.text.isEmpty()->edit_regscreen_name.error = getString(R.string.required)
                !edit_regscreen_radio_female.isChecked && !edit_regscreen_radio_male.isChecked ->{
                    Toast.makeText(activity,getString(R.string.select_gender),Toast.LENGTH_SHORT).show()
                }
                else -> {

                    mainRepository.saveParticipant(
                            participantId,
                            Participant(
                                    id= participantId,
                                    name = edit_regscreen_name.text.toString(),
                                    sClass = edit_regscreen_class.text.toString().toInt(),
                                    section = edit_regscreen_section.text.toString(),
                                    motherTongue = edit_regscreen_mothertongue.text.toString(),
                                    institution = edit_regscreen_school.text.toString(),
                                    dob = selectedDate.time,
                                    gender = gender.toString(),
                                    relationShipWithChild = relationShipWithChild.toString(),
                                    timeSpentWithChild = edit_time_spent_with_child.text.toString().toInt(),
                                    createdBy = mAuth.currentUser!!.uid
                            )
                    )


                    val age = calendar.time.year - selectedDate.year
                    if(age <= 7){
                        mainRepository.saveScreening(
                                UUID.randomUUID().toString(),
                                Screening(
                                        type = Type.JST.toString(),
                                        completed = false,
                                        mediumOfInstruction = getString(R.string.locale_type),
                                        participantId = participantId,
                                        userId = mAuth.uid.toString(),
                                        totalScore = 0,
                                        scheduledDate = scheduleDate.time,
                                        comments = "",
                                        participantName = edit_regscreen_name.text.toString()
                                )
                        )

                    }else{
                        mainRepository.saveScreening(
                                UUID.randomUUID().toString(),
                                Screening(
                                        type = Type.MST.toString(),
                                        completed = false,
                                        mediumOfInstruction = getString(R.string.locale_type),
                                        participantId = participantId,
                                        userId = mAuth.uid.toString(),
                                        totalScore = 0,
                                        scheduledDate = scheduleDate.time,
                                        comments = "",
                                        participantName = edit_regscreen_name.text.toString()
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

    var date = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, monthOfYear)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        edit_regscreen_dob.setText(sdf.format(calendar.time))
        selectedDate = calendar.time
    }

    var date2 = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, monthOfYear)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        scheduleDate = calendar.time
        btn_change_scheduled_date.visibility = View.VISIBLE
        tv_screenreg_schedule_date.text = sdf2.format(scheduleDate)
    }

    private fun showFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        fragment.showFragment(container = R.id.fragment_container,
                fragmentManager = activity!!.supportFragmentManager,
                addToBackStack = addToBackStack)
    }
}