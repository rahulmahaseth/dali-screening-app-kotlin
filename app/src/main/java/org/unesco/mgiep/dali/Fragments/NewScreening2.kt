package org.unesco.mgiep.dali.Fragments

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_newscreening2.*
import org.unesco.mgiep.dali.Dagger.MyApplication
import org.unesco.mgiep.dali.Data.*
import org.unesco.mgiep.dali.Data.Participant
import org.unesco.mgiep.dali.Data.ViewModels.ScreeningParticipantViewModel
import org.unesco.mgiep.dali.R
import org.unesco.mgiep.dali.Utility.showAsToast
import org.unesco.mgiep.dali.Utility.showFragment
import java.util.*

class NewScreening2 : Fragment(), AdapterView.OnItemSelectedListener {

    private var relationShipWithChild = Relationship.CT


    private lateinit var screeningParticipantViewModel: ScreeningParticipantViewModel

    private lateinit var mAuth: FirebaseAuth


    private val participantId = UUID.randomUUID().toString()

    private var participant = Participant()

    private var l1 = ""
    private var l2 = ""
    private var l3 = ""

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

        spinner_medium_of_inst.onItemSelectedListener = this
        spinner_l2.onItemSelectedListener = this
        spinner_l3.onItemSelectedListener = this
        val adapter = ArrayAdapter<String>(activity, R.layout.spinner_item, resources.getStringArray(R.array.languages))
        adapter.setDropDownViewResource(R.layout.item_spinner)

        val adapter2 = ArrayAdapter<String>(activity, R.layout.spinner_item, resources.getStringArray(R.array.languages2))
        adapter.setDropDownViewResource(R.layout.item_spinner)

        val adapter3 = ArrayAdapter<String>(activity, R.layout.spinner_item, resources.getStringArray(R.array.languages3))
        adapter.setDropDownViewResource(R.layout.item_spinner)

        spinner_medium_of_inst.adapter = adapter
        spinner_l2.adapter = adapter2
        spinner_l3.adapter = adapter3



        btn_screenreg_submit.setOnClickListener {
            Log.d("screening-reg","onClick")

            when {
                !radio_class_teacher.isChecked && !radio_language_teacher.isChecked && !radio_others.isChecked ->{
                    getString(R.string.relationship_with_child_none_select_error).showAsToast(activity!!, true)
                }
                edit_time_spent_with_child_month.text.isEmpty() && edit_time_spent_with_child_year.text.isEmpty() -> edit_time_spent_with_child_month.error = getString(R.string.required)

                edit_time_spent_with_child_year.text.isEmpty() && !edit_time_spent_with_child_month.text.isEmpty() -> {
                    if(edit_time_spent_with_child_month.text.toString().toInt() !in 3..12){
                        getString(R.string.month_range_error2).showAsToast(activity!!, true)
                    }else{
                        submit()
                    }
                }

                !edit_time_spent_with_child_year.text.isEmpty() && edit_time_spent_with_child_month.text.isEmpty() ->{
                    if(edit_time_spent_with_child_year.text.toString().toInt() <= 0){
                        edit_time_spent_with_child_month.error = getString(R.string.required)
                    }else{
                        submit()
                    }
                }

                !edit_time_spent_with_child_year.text.isEmpty() && !edit_time_spent_with_child_month.text.isEmpty() -> {
                    if(edit_time_spent_with_child_year.text.toString().toInt() <= 0){
                        if(edit_time_spent_with_child_month.text.toString().toInt() !in 3..12){
                            getString(R.string.month_range_error2).showAsToast(activity!!, true)
                        }else{
                            submit()
                        }
                    }else{
                        if(edit_time_spent_with_child_month.text.toString().toInt() !in 0..12){
                            getString(R.string.month_range_error).showAsToast(activity!!, true)
                        }else{
                            submit()
                        }
                    }
                }
                else -> {
                    submit()
                }
            }
        }

    }

    private fun submit(){
        if(l1 == l2 || l2 == l3 || l1 == l3){
            getString(R.string.same_instruction_language_error).showAsToast(activity!!,  true)
        }else{
            screeningParticipantViewModel.select(Participant(
                    id = participantId,
                    name = participant.name,
                    sClass = participant.sClass,
                    section = participant.section,
                    motherTongue = participant.motherTongue,
                    institution = participant.institution,
                    dob = participant.dob,
                    gender = participant.gender,
                    relationShipWithChild = relationShipWithChild.toString(),
                    timeSpentWithChild = getTimeSpentWithchild(),
                    createdBy = mAuth.currentUser!!.uid,
                    l1 = l1,
                    l2 = l2,
                    l3 = l3
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

    private fun getTimeSpentWithchild(): Int {
        return if(!edit_time_spent_with_child_year.text.isEmpty() && !edit_time_spent_with_child_month.text.isEmpty()){
            edit_time_spent_with_child_month.text.toString().toInt() + (edit_time_spent_with_child_year.text.toString().toInt() * 12)
        }
        else if(!edit_time_spent_with_child_year.text.isEmpty() && edit_time_spent_with_child_month.text.isEmpty()){
            edit_time_spent_with_child_year.text.toString().toInt() * 12
        }
        else if(edit_time_spent_with_child_year.text.isEmpty() && !edit_time_spent_with_child_month.text.isEmpty()){
            edit_time_spent_with_child_month.text.toString().toInt()
        }
        else{
            0
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

        when(parent!!.id){
            R.id.spinner_medium_of_inst -> {
                l1 = parent.getItemAtPosition(position).toString()
            }
            R.id.spinner_l2 -> {
                l2 = parent.getItemAtPosition(position).toString()
            }
            R.id.spinner_l3 -> {
                l3 = parent.getItemAtPosition(position).toString()
            }
        }
    }

}