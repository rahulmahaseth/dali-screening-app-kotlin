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
import org.unesco.mgiep.dali.Activity.MainActivity
import org.unesco.mgiep.dali.Activity.ScreeningActivity
import org.unesco.mgiep.dali.Dagger.MyApplication
import org.unesco.mgiep.dali.Data.*
import org.unesco.mgiep.dali.Data.Participant
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
    val myFormat = "dd/MM/yy"
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

        switch_schedule.setOnCheckedChangeListener { compoundButton, b ->
            if(compoundButton.isEnabled){
                DatePickerDialog(
                        activity,
                        date2,
                        calendar.get(Calendar.DAY_OF_MONTH),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.YEAR)
                ).show()
                btn_screenreg_schedule_submit.visibility = View.VISIBLE
                btn_screenreg_submit.visibility = View.GONE
            }else{
                btn_change_scheduled_date.visibility = View.GONE
                btn_screenreg_schedule_submit.visibility = View.GONE
                btn_screenreg_submit.visibility = View.VISIBLE
            }
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
                edit_regscreen_mothertongue.text.isEmpty()->edit_regscreen_name.error = getString(R.string.required)
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
                            name = edit_regscreen_name.text.toString(),
                            sClass = edit_regscreen_class.text.toString().toInt(),
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
                                    name = edit_regscreen_name.text.toString(),
                                    sClass = edit_regscreen_class.text.toString().toInt(),
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
                    if(age <= 7){
                        intent.putExtra("type",Type.JST.toString())
                        intent.putExtra("participantId",participantId)
                    }else{
                        intent.putExtra("type",Type.MST.toString())
                        intent.putExtra("participantId",participantId)
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
                                    name = edit_regscreen_name.text.toString(),
                                    sClass = edit_regscreen_class.text.toString().toInt(),
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
                                FirebaseScreening(
                                        type = Type.JST.toString(),
                                        completed = false,
                                        mediumOfInstruction = getString(R.string.locale_type),
                                        participantId = participantId,
                                        userId = mAuth.uid.toString(),
                                        totalScore = 0,
                                        scheduledDate = scheduleDate.time,
                                        comments = "",
                                        tempId = UUID.randomUUID().toString()
                                )
                        )

                    }else{
                        mainRepository.saveScreening(
                                UUID.randomUUID().toString(),
                                FirebaseScreening(
                                        type = Type.MST.toString(),
                                        completed = false,
                                        mediumOfInstruction = getString(R.string.locale_type),
                                        participantId = participantId,
                                        userId = mAuth.uid.toString(),
                                        totalScore = 0,
                                        scheduledDate = scheduleDate.time,
                                        comments = "",
                                        tempId = UUID.randomUUID().toString()
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