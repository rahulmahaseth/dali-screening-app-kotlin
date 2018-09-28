package org.unesco.mgiep.dali.Fragments

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_newscreening2.*
import org.unesco.mgiep.dali.Dagger.MyApplication
import org.unesco.mgiep.dali.Data.*
import org.unesco.mgiep.dali.Data.Participant
import org.unesco.mgiep.dali.Data.ViewModels.ScreeningParticipantViewModel
import org.unesco.mgiep.dali.R
import org.unesco.mgiep.dali.Utility.showAsToast
import org.unesco.mgiep.dali.Utility.showFragment
import java.text.SimpleDateFormat
import java.util.*

class NewScreening2 : Fragment() {

    val calendar = Calendar.getInstance()

    val onlyDate = "dd/MM/yyyy"

    val sdf = SimpleDateFormat(onlyDate, Locale.ENGLISH)

    private var relationShipWithChild = Relationship.CT

    private var scheduleDate: Date = calendar.time

    private lateinit var screeningParticipantViewModel: ScreeningParticipantViewModel

    private lateinit var mAuth: FirebaseAuth


    private val participantId = UUID.randomUUID().toString()
    private val screeningId = UUID.randomUUID().toString()

    private var participant = Participant()



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


        btn_screenreg_submit.setOnClickListener {
            Log.d("screening-reg","onClick")

            when {
                !radio_class_teacher.isChecked && !radio_language_teacher.isChecked && !radio_others.isChecked ->{
                    getString(R.string.relationship_with_child_none_select_error).showAsToast(activity!!)
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

                    //saveParticipant()

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


    private fun showFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        fragment.showFragment(container = R.id.new_screening_fragment_container,
                fragmentManager = activity!!.supportFragmentManager,
                addToBackStack = addToBackStack)
    }

}