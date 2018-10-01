package org.unesco.mgiep.dali.Fragments

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.google.firebase.auth.FirebaseAuth
import io.grpc.internal.SharedResourceHolder
import kotlinx.android.synthetic.main.fragment_newscreening2.*
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

class NewScreening2 : Fragment(), AdapterView.OnItemSelectedListener {


    val calendar = Calendar.getInstance()

    val onlyDate = "dd/MM/yyyy"

    val sdf = SimpleDateFormat(onlyDate, Locale.ENGLISH)

    private var relationShipWithChild = Relationship.CT

    private var scheduleDate: Date = calendar.time

    private lateinit var screeningParticipantViewModel: ScreeningParticipantViewModel
    private lateinit var screeningViewModel: ScreeningViewModel

    private lateinit var mAuth: FirebaseAuth


    private val participantId = UUID.randomUUID().toString()
    private val screeningId = UUID.randomUUID().toString()

    private var participant = Participant()
    private var mediumOfInst = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity!!.application as MyApplication).component.inject(this)
        screeningParticipantViewModel = ViewModelProviders.of(activity!!).get(ScreeningParticipantViewModel::class.java)
        mAuth = FirebaseAuth.getInstance()
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

        edit_time_spent_with_child_year.setOnFocusChangeListener { view, b ->
            if(!b){
                if(edit_time_spent_with_child_year.text.isEmpty()){
                    edit_time_spent_with_child_year.hint = getString(R.string.years)
                }else{
                    participant.timeSpentWithChild = edit_time_spent_with_child_year.text.toString().toInt()
                }
            }else{
                edit_time_spent_with_child_year.hint = ""
            }
        }

        edit_time_spent_with_child_month.setOnFocusChangeListener { view, b ->
            if(!b){
                if(edit_time_spent_with_child_month.text.isEmpty()){
                    edit_time_spent_with_child_month.hint = getString(R.string.months)
                }else{
                    participant.timeSpentWithChild = edit_time_spent_with_child_month.text.toString().toInt()
                    if(edit_time_spent_with_child_month.text.toString().toInt() !in 0..12){
                        edit_time_spent_with_child_month.error = getString(R.string.month_range_error)
                    }
                }
            }else{
                edit_time_spent_with_child_month.hint = ""
            }
        }

        spinner_medium_of_inst.onItemSelectedListener = this
        val adapter = ArrayAdapter<String>(activity, R.layout.spinner_item, resources.getStringArray(R.array.languages))
        adapter.setDropDownViewResource(R.layout.item_spinner)

        spinner_medium_of_inst.adapter = adapter

        btn_screenreg_submit.setOnClickListener {
            Log.d("screening-reg","onClick")

            when {
                !radio_class_teacher.isChecked && !radio_language_teacher.isChecked && !radio_others.isChecked ->{
                    getString(R.string.relationship_with_child_none_select_error).showAsToast(activity!!)
                }
                edit_time_spent_with_child_month.text.isEmpty() && edit_time_spent_with_child_year.text.isEmpty() -> edit_time_spent_with_child_month.error = getString(R.string.required)
                edit_time_spent_with_child_year.text.toString().toInt() == 0  && edit_time_spent_with_child_month.text.toString().toInt() !in 3..12 -> {
                    edit_time_spent_with_child_month.error = getString(R.string.month_range_error2)
                }
                (!edit_time_spent_with_child_year.text.isEmpty()) && (edit_time_spent_with_child_month.text.toString().toInt() !in 0..12) -> {
                    edit_time_spent_with_child_month.error = getString(R.string.month_range_error)
                }
                edit_time_spent_with_child_year.text.isEmpty() && (edit_time_spent_with_child_month.text.toString().toInt() !in 3..12) -> {
                    edit_time_spent_with_child_month.error = getString(R.string.month_range_error2)
                }


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
                            timeSpentWithChild = getTimeSpentWithchild(),
                            createdBy = mAuth.currentUser!!.uid
                    ))

                    showFragment(
                            Fragment.instantiate(
                                    activity,
                                    ParticipantConfirm::class.java.name
                            ),
                            true
                    )

                }
            }
        }

    }

    private fun getTimeSpentWithchild(): Int {
        return if(!edit_time_spent_with_child_year.text.isEmpty()){
            edit_time_spent_with_child_month.text.toString().toInt() + (edit_time_spent_with_child_year.text.toString().toInt() * 12)
        }else{
            edit_time_spent_with_child_month.text.toString().toInt()
        }
    }


    private fun showFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        fragment.showFragment(container = R.id.new_screening_fragment_container,
                fragmentManager = activity!!.supportFragmentManager,
                addToBackStack = addToBackStack)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        mediumOfInst = parent!!.getItemAtPosition(position).toString()
        AppPref(activity!!.applicationContext).instructionMedium = mediumOfInst
        Log.d("medium:",mediumOfInst)
    }

}