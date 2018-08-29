package org.unesco.mgiep.dali.Fragments

import android.app.DatePickerDialog
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_new_screening.*
import org.unesco.mgiep.dali.Data.Gender
import org.unesco.mgiep.dali.Data.Participant
import org.unesco.mgiep.dali.Data.ViewModels.ScreeningPariticipantViewModel
import org.unesco.mgiep.dali.R
import org.unesco.mgiep.dali.Utility.showFragment
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class NewScreening : Fragment() {

    val calendar = Calendar.getInstance()
    val myFormat = "dd/MM/yy"
    val sdf = SimpleDateFormat(myFormat, Locale.ENGLISH)
    private var gender = Gender.MALE
    private var selectedDate : Date = calendar.time

    @Inject
    lateinit var screeningPariticipantViewModel: ScreeningPariticipantViewModel

    var date = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, monthOfYear)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        edit_regscreen_dob.setText(sdf.format(calendar.time))
        selectedDate = calendar.time
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        screeningPariticipantViewModel = ViewModelProviders.of(activity!!).get(ScreeningPariticipantViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?=
            inflater.inflate(R.layout.fragment_new_screening, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        btn_screenreg_submit.setOnClickListener {
            when {
                edit_regscreen_name.text.isEmpty()->edit_regscreen_name.error = "Name Required"
                edit_regscreen_mothertongue.text.isEmpty()->edit_regscreen_name.error = "Mother Tongue Required"
                edit_regscreen_dob.text.isEmpty()->edit_regscreen_name.error = "Date of Birth Required"
                edit_regscreen_radio_male.isChecked -> {
                    edit_regscreen_radio_female.isChecked = false
                    gender = Gender.MALE
                }
                edit_regscreen_radio_female.isChecked -> {
                    edit_regscreen_radio_male.isChecked = false
                    gender = Gender.FEMALE
                }
                !edit_regscreen_radio_female.isChecked && !edit_regscreen_radio_male.isChecked ->{
                    Toast.makeText(activity,"Select Gender",Toast.LENGTH_SHORT).show()
                }
                else -> {
                    screeningPariticipantViewModel.select(Participant(
                            name = edit_regscreen_name.text.toString(),
                            sClass = edit_regscreen_class.text.toString().toInt(),
                            section = edit_regscreen_section.text.toString(),
                            motherTongue = edit_regscreen_mothertongue.text.toString(),
                            institution = edit_regscreen_school.text.toString(),
                            dob = selectedDate,
                            gender = gender
                    ))

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



    }

    private fun showFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        fragment.showFragment(container = R.id.fragment_container,
                fragmentManager = activity!!.supportFragmentManager,
                addToBackStack = addToBackStack)
    }
}