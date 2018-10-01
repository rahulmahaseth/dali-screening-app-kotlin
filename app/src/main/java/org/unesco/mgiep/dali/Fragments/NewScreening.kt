package org.unesco.mgiep.dali.Fragments

import android.app.DatePickerDialog
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.fragment_new_screening.*
import org.unesco.mgiep.dali.Dagger.MyApplication
import org.unesco.mgiep.dali.Data.*
import org.unesco.mgiep.dali.Data.Participant
import org.unesco.mgiep.dali.Data.ViewModels.ScreeningParticipantViewModel
import org.unesco.mgiep.dali.Data.ViewModels.ScreeningViewModel
import org.unesco.mgiep.dali.R
import org.unesco.mgiep.dali.Utility.showAsToast
import org.unesco.mgiep.dali.Utility.showFragment
import java.text.SimpleDateFormat
import java.util.*
import java.lang.Exception


class NewScreening : Fragment() {

    val calendar = Calendar.getInstance()

    val myFormat = "dd/MM/yyyy"

    val sdf = SimpleDateFormat(myFormat, Locale.ENGLISH)

    private var gender = Gender.MALE

    var selectedDate : Date = Date()

    private lateinit var screeningViewModel: ScreeningViewModel
    private lateinit var screeningParticipantViewModel: ScreeningParticipantViewModel

    private val participant = Participant()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity!!.application as MyApplication).component.inject(this)
        screeningParticipantViewModel = ViewModelProviders.of(activity!!).get(ScreeningParticipantViewModel::class.java)
        screeningViewModel = ViewModelProviders.of(activity!!).get(ScreeningViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?=
            inflater.inflate(R.layout.fragment_new_screening, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val stringArray = resources.getStringArray(R.array.languages)
        val adapter = ArrayAdapter<String>(activity, R.layout.select_dialog_singlechoice_material, stringArray)
        edit_regscreen_mothertongue.threshold = 1
        edit_regscreen_mothertongue.setAdapter(adapter)

        edit_regscreen_radio_female.setOnClickListener {
            gender = Gender.FEMALE
        }

        edit_regscreen_radio_male.setOnClickListener {
            gender = Gender.MALE
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
                    if(edit_regscreen_class.text.toString().toInt() !in 1..5){
                        edit_regscreen_class.error = getString(R.string.clas_range_error)
                    }
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

        btn_submit_participant.setOnClickListener {
            when{
                edit_regscreen_name.text.isEmpty()->edit_regscreen_name.error = getString(R.string.required)
                edit_regscreen_class.text.isEmpty() -> edit_regscreen_class.error = getString(R.string.required)
                edit_regscreen_class.text.toString().toInt() !in 1..12 -> edit_regscreen_class.error = getString(R.string.clas_range_error)
                edit_regscreen_section.text.isEmpty() -> edit_regscreen_section.error = getString(R.string.required)
                edit_regscreen_mothertongue.text.isEmpty()-> edit_regscreen_name.error = getString(R.string.required)
                edit_regscreen_school.text.isEmpty() -> edit_regscreen_school.error = getString(R.string.required)
                edit_regscreen_dob.text.isEmpty() -> edit_regscreen_dob.error = getString(R.string.required)
                !edit_regscreen_radio_female.isChecked && !edit_regscreen_radio_male.isChecked ->{
                    getString(R.string.select_gender).showAsToast(activity!!)
                }
                else->{
                    screeningParticipantViewModel.select(
                            Participant(
                                    name = edit_regscreen_name.text.toString(),
                                    sClass = edit_regscreen_class.text.toString().toInt(),
                                    section = edit_regscreen_section.text.toString(),
                                    motherTongue = edit_regscreen_mothertongue.text.toString(),
                                    institution = edit_regscreen_school.text.toString(),
                                    dob = selectedDate.time,
                                    gender = gender.toString()
                            )
                    )

                    showFragment(
                            Fragment.instantiate(
                                    activity,
                                    NewScreening2::class.java.name
                            ),
                            true
                    )
                }
            }
        }

        var dialogPicker = DatePickerDialog(
                activity,
                date,
                selectedDate.year,
                selectedDate.month,
                selectedDate.day
        )

        dialogPicker.updateDate(selectedDate.year, selectedDate.month, selectedDate.day)
        dialogPicker.datePicker.maxDate = Date().time
        dialogPicker.datePicker.minDate = subtractYear(Date())

        edit_regscreen_dob.setOnClickListener {
            Log.d("selected date","$selectedDate")
            dialogPicker.show()
        }

    }

    var date = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, monthOfYear)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        edit_regscreen_dob.setText(sdf.format(calendar.time))
        selectedDate = calendar.time
    }

    private fun subtractYear(date: Date): Long{
        return try {
            calendar.time = date
            calendar.add(Calendar.YEAR, -15)
            calendar.timeInMillis
        }catch ( e: Exception )
        {
            e.printStackTrace()
            0
        }
    }

    private fun showFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        fragment.showFragment(container = R.id.new_screening_fragment_container,
                fragmentManager = activity!!.supportFragmentManager,
                addToBackStack = addToBackStack)
    }
}
