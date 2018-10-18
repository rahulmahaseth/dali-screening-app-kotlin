package org.unesco.mgiep.dali.Fragments

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.google.firebase.analytics.FirebaseAnalytics
import com.tsongkha.spinnerdatepicker.DatePickerDialog
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder
import kotlinx.android.synthetic.main.fragment_participant_form_1.*
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


class ParticipantForm1 : Fragment() {

    val calendar = Calendar.getInstance()

    val myFormat = "dd/MM/yyyy"

    val sdf = SimpleDateFormat(myFormat, Locale.ENGLISH)

    private var gender = Gender.MALE

    var selectedDate : Date = Date()

    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var screeningViewModel: ScreeningViewModel
    private lateinit var screeningParticipantViewModel: ScreeningParticipantViewModel

    private val participant = Participant()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity!!.application as MyApplication).component.inject(this)
        firebaseAnalytics = FirebaseAnalytics.getInstance(context!!)
        screeningParticipantViewModel = ViewModelProviders.of(activity!!).get(ScreeningParticipantViewModel::class.java)
        screeningViewModel = ViewModelProviders.of(activity!!).get(ScreeningViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?=
            inflater.inflate(R.layout.fragment_participant_form_1, container, false)

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

        btn_submit_participant.setOnClickListener {
            when{
                edit_regscreen_name.text.isEmpty()->edit_regscreen_name.error = getString(R.string.required)
                edit_regscreen_class.text.isEmpty() -> edit_regscreen_class.error = getString(R.string.required)
                edit_regscreen_class.text.toString().toInt() !in 1..5 -> edit_regscreen_class.error = getString(R.string.clas_range_error)
                edit_regscreen_section.text.isEmpty() -> edit_regscreen_section.error = getString(R.string.required)
                edit_regscreen_mothertongue.text.isEmpty()-> edit_regscreen_mothertongue.error = getString(R.string.required)
                edit_regscreen_school.text.isEmpty() -> edit_regscreen_school.error = getString(R.string.required)
                edit_regscreen_dob.text.isEmpty() -> edit_regscreen_dob.error = getString(R.string.required)
                !edit_regscreen_radio_female.isChecked && !edit_regscreen_radio_male.isChecked ->{
                    getString(R.string.select_gender).showAsToast(activity!!, true)
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
                                    ParticipantForm2::class.java.name
                            ),
                            true
                    )
                }
            }
        }

        val dialogPicker = SpinnerDatePickerDialogBuilder()
                .context(activity!!)
                .callback(date)
                .showTitle(true)
                .defaultDate(selectedDate.year, selectedDate.month, selectedDate.day)
                .maxDate(calendar.get(Calendar.YEAR).minus(2), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
                .minDate(calendar.get(Calendar.YEAR ).minus(15), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
                .build()

        edit_regscreen_dob.setOnClickListener {
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

    override fun onResume() {
        super.onResume()
        firebaseAnalytics.setCurrentScreen(activity!!, "participant_form_1", ParticipantForm1::class.java.simpleName)
    }

    private fun showFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        fragment.showFragment(container = R.id.new_screening_fragment_container,
                fragmentManager = activity!!.supportFragmentManager,
                addToBackStack = addToBackStack)
    }
}
