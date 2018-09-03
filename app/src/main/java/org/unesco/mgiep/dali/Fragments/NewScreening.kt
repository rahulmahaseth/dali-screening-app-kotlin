package org.unesco.mgiep.dali.Fragments

import android.app.DatePickerDialog
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_new_screening.*
import org.unesco.mgiep.dali.Dagger.MyApplication
import org.unesco.mgiep.dali.Data.FirebaseScreening
import org.unesco.mgiep.dali.Data.Gender
import org.unesco.mgiep.dali.Data.Participant
import org.unesco.mgiep.dali.Data.Type
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
    val sdf = SimpleDateFormat(myFormat, Locale.ENGLISH)
    private var gender = Gender.MALE
    private var selectedDate : Date = calendar.time
    private var scheduleDate: Date = calendar.time
    private lateinit var screening: Screening

    private lateinit var screeningViewModel: ScreeningViewModel
    lateinit var screeningParticipantViewModel: ScreeningParticipantViewModel

    private lateinit var mAuth: FirebaseAuth

    private lateinit var firebaseRepository: FirebaseRepository
    private lateinit var mainRepository: MainReposirtory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity!!.application as MyApplication).component.inject(this)
        screeningParticipantViewModel = ViewModelProviders.of(activity!!).get(ScreeningParticipantViewModel::class.java)
        screeningViewModel = ViewModelProviders.of(activity!!).get(ScreeningViewModel::class.java)
        mAuth = FirebaseAuth.getInstance()
        firebaseRepository = FirebaseRepository()
        mainRepository = MainReposirtory()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?=
            inflater.inflate(R.layout.fragment_new_screening, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        edit_regscreen_radio_female.setOnClickListener {
            edit_regscreen_radio_male.isChecked = false
            gender = Gender.FEMALE
        }

        edit_regscreen_radio_male.setOnClickListener {
            edit_regscreen_radio_female.isChecked = false
            gender = Gender.MALE
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
            }
        }

        tv_screenreg_schedule_date.setOnClickListener {
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
                edit_regscreen_name.text.isEmpty()->edit_regscreen_name.error = getString(R.string.required)
                edit_regscreen_mothertongue.text.isEmpty()->edit_regscreen_name.error = getString(R.string.required)
                !edit_regscreen_radio_female.isChecked && !edit_regscreen_radio_male.isChecked ->{
                    Toast.makeText(activity,getString(R.string.select_gender),Toast.LENGTH_SHORT).show()
                }
                else -> {
                    screeningParticipantViewModel.select(Participant(
                            name = edit_regscreen_name.text.toString(),
                            sClass = edit_regscreen_class.text.toString().toInt(),
                            motherTongue = edit_regscreen_mothertongue.text.toString(),
                            institution = edit_regscreen_school.text.toString(),
                            dob = selectedDate.time,
                            gender = gender.toString()
                    ))

                    mainRepository.saveParticipant(
                            UUID.randomUUID().toString(),
                            Participant(
                                    name = edit_regscreen_name.text.toString(),
                                    sClass = edit_regscreen_class.text.toString().toInt(),
                                    motherTongue = edit_regscreen_mothertongue.text.toString(),
                                    institution = edit_regscreen_school.text.toString(),
                                    dob = selectedDate.time,
                                    gender = gender.toString()
                            )
                    )

                    val age = calendar.time.year - selectedDate.year
                    if(age <= 7){
                        screeningViewModel.select(
                                FirebaseScreening(
                                        type = Type.JST,
                                        totalQuestions = 15,
                                        questionsCompleted = 0,
                                        completed = false,
                                        mediumOfInstruction = getString(R.string.locale_type),
                                        participantId = UUID.randomUUID().toString(),
                                        userId = mAuth.uid.toString(),
                                        totalScore = 0,
                                        scheduledDate = calendar.time.time
                                )
                        )
                    }else{
                        screeningViewModel.select(
                                FirebaseScreening(
                                        type = Type.JST,
                                        totalQuestions = 21,
                                        questionsCompleted = 0,
                                        completed = false,
                                        mediumOfInstruction = getString(R.string.locale_type),
                                        participantId = UUID.randomUUID().toString(),
                                        userId = mAuth.uid.toString(),
                                        totalScore = 0,
                                        scheduledDate = calendar.time.time
                                )
                        )
                    }

                    showFragment(
                            Fragment.instantiate(
                                    activity,
                                    Screening::class.java.name
                            ),
                            false
                    )
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
                    screeningParticipantViewModel.select(Participant(
                            name = edit_regscreen_name.text.toString(),
                            sClass = edit_regscreen_class.text.toString().toInt(),
                            motherTongue = edit_regscreen_mothertongue.text.toString(),
                            institution = edit_regscreen_school.text.toString(),
                            dob = selectedDate.time,
                            gender = gender.toString()
                    ))

                    mainRepository.saveParticipant(
                            UUID.randomUUID().toString(),
                            Participant(
                                    name = edit_regscreen_name.text.toString(),
                                    sClass = edit_regscreen_class.text.toString().toInt(),
                                    motherTongue = edit_regscreen_mothertongue.text.toString(),
                                    institution = edit_regscreen_school.text.toString(),
                                    dob = selectedDate.time,
                                    gender = gender.toString()
                            )
                    )

                    screeningViewModel.select(
                            FirebaseScreening(
                                    type = Type.JST,
                                    totalQuestions = 21,
                                    questionsCompleted = 0,
                                    completed = false,
                                    mediumOfInstruction = getString(R.string.locale_type),
                                    participantId = UUID.randomUUID().toString(),
                                    userId = mAuth.uid.toString(),
                                    totalScore = 0,
                                    scheduledDate = scheduleDate.time
                            )
                    )

                    showFragment(
                            Fragment.instantiate(
                                    activity,
                                    Screening::class.java.name
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
        tv_screenreg_schedule_date.text = ""
        tv_screenreg_schedule_time.text = ""
    }

    private fun showFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        fragment.showFragment(container = R.id.fragment_container,
                fragmentManager = activity!!.supportFragmentManager,
                addToBackStack = addToBackStack)
    }
}