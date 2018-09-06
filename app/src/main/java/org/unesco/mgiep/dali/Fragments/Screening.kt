package org.unesco.mgiep.dali.Fragments

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.res.ResourcesCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.common.io.Resources
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_screening.*
import org.unesco.mgiep.dali.Dagger.MyApplication
import org.unesco.mgiep.dali.Data.FirebaseScreening
import org.unesco.mgiep.dali.Data.Type
import org.unesco.mgiep.dali.Data.ViewModels.ScreeningParticipantViewModel
import org.unesco.mgiep.dali.Data.ViewModels.ScreeningViewModel
import org.unesco.mgiep.dali.R
import org.unesco.mgiep.dali.Repositories.MainReposirtory
import org.unesco.mgiep.dali.Utility.showFragment
import java.util.*
import javax.inject.Inject

class Screening : Fragment() {

    private lateinit var screeningViewModel: ScreeningViewModel

    lateinit var mainReposirtory: MainReposirtory
    private var totalQuestions = 0
    private var questionsCompleted = 0
    private var totalScore = 0
    private var screeningType = ""
    private var participantId= ""
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity!!.application as MyApplication).component.inject(this)
        mainReposirtory = MainReposirtory()
        screeningViewModel = ViewModelProviders.of(activity!!).get(ScreeningViewModel::class.java)
        mAuth = FirebaseAuth.getInstance()
        screeningType = activity!!.intent.getStringExtra("type")
        participantId = activity!!.intent.getStringExtra("participantId")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?=
            inflater.inflate(R.layout.fragment_screening, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        totalQuestions = if( screeningType == Type.JST.toString()){
            15
        }else{
            21
        }

        if(screeningType == Type.JST.toString()){
            addCategoryImage(resources.getStringArray(R.array.jst_categories)[questionsCompleted])
            tv_question.text = resources.getStringArray(R.array.jst_questions)[questionsCompleted]
            tv_example.text = resources.getStringArray(R.array.jst_examples)[questionsCompleted]
        }else{
            addCategoryImage(resources.getStringArray(R.array.mst_categories)[questionsCompleted])
            tv_question.text = resources.getStringArray(R.array.mst_questions)[questionsCompleted]
            tv_example.text = resources.getStringArray(R.array.mst_examples)[questionsCompleted]
        }

        btn_usually.setOnClickListener {
            update(2)
        }

        btn_sometimes.setOnClickListener {
            update(1)
        }

        btn_never.setOnClickListener {
            update(1)
        }
    }

    private fun update(score: Int){
        questionsCompleted += 1
        totalScore += score
        if(questionsCompleted == totalQuestions){

            screeningViewModel.select(
                    FirebaseScreening(
                            type = screeningType,
                            completed = true,
                            mediumOfInstruction = "English",
                            participantId = participantId,
                            userId = mAuth.currentUser!!.uid,
                            scheduledDate = Date().time,
                            totalScore = totalScore,
                            comments = "",
                            tempId = UUID.randomUUID().toString()
                    )
            )
            showFragment(
                    Fragment.instantiate(
                            activity,
                            Comments::class.java.name
                    ),
                    false
            )
        }else{
            if(screeningType == Type.JST.toString()){
                addCategoryImage(resources.getStringArray(R.array.jst_categories)[questionsCompleted])
                tv_question.text = resources.getStringArray(R.array.jst_questions)[questionsCompleted]
                tv_example.text = resources.getStringArray(R.array.jst_examples)[questionsCompleted]
            }else{
                addCategoryImage(resources.getStringArray(R.array.mst_categories)[questionsCompleted])
                tv_question.text = resources.getStringArray(R.array.mst_questions)[questionsCompleted]
                tv_example.text = resources.getStringArray(R.array.mst_examples)[questionsCompleted]
            }
        }
    }

    fun addCategoryImage(category: String){
        when(category){
            "sound" -> image_category.setImageResource(R.drawable.ic_hearing_01)
            "communication" -> image_category.setImageResource(R.drawable.ic_communication_01)
            "number" -> image_category.setImageResource(R.drawable.ic_numberconcept_01)
            "memory" -> image_category.setImageResource(R.drawable.ic_memory_01)
            "motor" -> image_category.setImageResource(R.drawable.ic_motorcordination_01)
            "behavior" -> image_category.setImageResource(R.drawable.ic_behaviour_01)
            "reading" -> image_category.setImageResource(R.drawable.ic_reading_01)
            "writing" -> image_category.setImageResource(R.drawable.ic_writing_01)
        }
    }

    private fun showFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        fragment.showFragment(container = R.id.screening_fragment_container,
                fragmentManager = activity!!.supportFragmentManager,
                addToBackStack = addToBackStack)
    }
}