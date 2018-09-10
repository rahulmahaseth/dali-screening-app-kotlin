package org.unesco.mgiep.dali.Fragments

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.res.ResourcesCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_comments.*
import kotlinx.android.synthetic.main.fragment_screening.*
import org.unesco.mgiep.dali.Dagger.MyApplication
import org.unesco.mgiep.dali.Data.Screening
import org.unesco.mgiep.dali.Data.Type
import org.unesco.mgiep.dali.Data.ViewModels.ScreeningViewModel
import org.unesco.mgiep.dali.R
import org.unesco.mgiep.dali.Repositories.MainReposirtory
import org.unesco.mgiep.dali.Utility.showFragment
import java.util.*
import kotlin.collections.HashMap

class Screening : Fragment() {

    private lateinit var screeningViewModel: ScreeningViewModel

    lateinit var mainReposirtory: MainReposirtory
    private var totalQuestions = 0
    private var questionsCompleted = 0
    private var totalScore = 0
    private var screeningType = ""
    private var participantId = ""
    private lateinit var mAuth: FirebaseAuth
    private val questionAnswerMap: HashMap<Int, Int> = hashMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity!!.application as MyApplication).component.inject(this)
        mainReposirtory = MainReposirtory()
        screeningViewModel = ViewModelProviders.of(activity!!).get(ScreeningViewModel::class.java)
        mAuth = FirebaseAuth.getInstance()
        screeningType = activity!!.intent.getStringExtra("type")
        participantId = activity!!.intent.getStringExtra("participantId")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_screening, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        updateButtons()

        totalQuestions = if (screeningType == Type.JST.toString()) {
            15
        } else {
            21
        }

        tv_questions_completed.text = (questionsCompleted+1).toString()
        tv_total_questions.text = totalQuestions.toString()

        if (screeningType == Type.JST.toString()) {
            addCategoryImage(resources.getStringArray(R.array.jst_categories)[questionsCompleted])
            tv_question.text = resources.getStringArray(R.array.jst_questions)[questionsCompleted]
            tv_example.text = resources.getStringArray(R.array.jst_examples)[questionsCompleted]
        } else {

            addCategoryImage(resources.getStringArray(R.array.mst_categories)[questionsCompleted])
            tv_question.text = resources.getStringArray(R.array.mst_questions)[questionsCompleted]
            tv_example.text = resources.getStringArray(R.array.mst_examples)[questionsCompleted]
        }

        btn_usually.setOnClickListener {
            questionAnswerMap[questionsCompleted] = 2
            updateButtons()
        }

        btn_sometimes.setOnClickListener {
            questionAnswerMap[questionsCompleted] = 1
            updateButtons()
        }

        btn_never.setOnClickListener {
            questionAnswerMap[questionsCompleted] = 0
            updateButtons()
        }

        btn_screening_next.setOnClickListener {
            next()
        }

        btn_screening_back.setOnClickListener {
            back()
        }

        btn_screening_submit.setOnClickListener {
            submit()
        }
    }

    private fun updateButtons() {
        when (questionAnswerMap[questionsCompleted]) {
            -1 -> {
                btn_usually.background = ResourcesCompat.getDrawable(resources, R.drawable.custom_radio_button, null)
                btn_sometimes.background = ResourcesCompat.getDrawable(resources, R.drawable.custom_radio_button, null)
                btn_never.background = ResourcesCompat.getDrawable(resources, R.drawable.custom_radio_button, null)
            }
            2 -> {
                btn_usually.isChecked = true
                btn_usually.background = ResourcesCompat.getDrawable(resources, R.drawable.dotted_boundary_rectangle, null)
                btn_sometimes.background = ResourcesCompat.getDrawable(resources, R.drawable.custom_radio_button, null)
                btn_never.background = ResourcesCompat.getDrawable(resources, R.drawable.custom_radio_button, null)
            }
            1 -> {
                btn_sometimes.isChecked = true
                btn_usually.background = ResourcesCompat.getDrawable(resources, R.drawable.custom_radio_button, null)
                btn_sometimes.background = ResourcesCompat.getDrawable(resources, R.drawable.dotted_boundary_rectangle, null)
                btn_never.background = ResourcesCompat.getDrawable(resources, R.drawable.custom_radio_button, null)
            }
            0 -> {
                btn_never.isChecked = true
                btn_usually.background = ResourcesCompat.getDrawable(resources, R.drawable.custom_radio_button, null)
                btn_sometimes.background = ResourcesCompat.getDrawable(resources, R.drawable.custom_radio_button, null)
                btn_never.background = ResourcesCompat.getDrawable(resources, R.drawable.dotted_boundary_rectangle, null)
            }
        }
    }

    private fun next() {
        if (!btn_usually.isChecked && !btn_sometimes.isChecked && !btn_never.isChecked) {
            Toast.makeText(activity, getString(R.string.select_option_error), Toast.LENGTH_SHORT).show()
        } else {
            questionsCompleted += 1
            questionAnswerMap[questionsCompleted] = -1
            updateButtons()
            screening_radio_group.clearCheck()

            tv_questions_completed.text = questionsCompleted.toString()
            tv_total_questions.text = totalQuestions.toString()
            if (questionsCompleted == totalQuestions) {
                btn_screening_next.visibility = View.GONE
                btn_screening_submit.visibility = View.VISIBLE
            } else {
                if (screeningType == Type.JST.toString()) {
                    addCategoryImage(resources.getStringArray(R.array.jst_categories)[questionsCompleted])
                    tv_question.text = resources.getStringArray(R.array.jst_questions)[questionsCompleted]
                    tv_example.text = resources.getStringArray(R.array.jst_examples)[questionsCompleted]
                } else {
                    addCategoryImage(resources.getStringArray(R.array.mst_categories)[questionsCompleted])
                    tv_question.text = resources.getStringArray(R.array.mst_questions)[questionsCompleted]
                    tv_example.text = resources.getStringArray(R.array.mst_examples)[questionsCompleted]
                }
            }
        }
    }

    private fun back() {
        questionsCompleted -= 1
        updateButtons()
        tv_questions_completed.text = questionsCompleted.toString()
        tv_total_questions.text = totalQuestions.toString()

        if (screeningType == Type.JST.toString()) {
            tv_questions_completed.text = questionsCompleted.toString()
            tv_total_questions.text = totalQuestions.toString()
            addCategoryImage(resources.getStringArray(R.array.jst_categories)[questionsCompleted])
            tv_question.text = resources.getStringArray(R.array.jst_questions)[questionsCompleted]
            tv_example.text = resources.getStringArray(R.array.jst_examples)[questionsCompleted]
        } else {
            tv_questions_completed.text = questionsCompleted.toString()
            tv_total_questions.text = totalQuestions.toString()
            addCategoryImage(resources.getStringArray(R.array.mst_categories)[questionsCompleted])
            tv_question.text = resources.getStringArray(R.array.mst_questions)[questionsCompleted]
            tv_example.text = resources.getStringArray(R.array.mst_examples)[questionsCompleted]
        }
    }

    private fun submit() {

        if (!btn_usually.isChecked && !btn_sometimes.isChecked && !btn_never.isChecked) {
            Toast.makeText(activity, getString(R.string.select_option_error), Toast.LENGTH_SHORT).show()
        } else {
            questionAnswerMap.forEach {
                Log.d("Q - ${it.key}", "${it.value}")
                totalScore += it.value
            }

            Log.d("totalScore", "$totalScore")

            screeningViewModel.select(
                    Screening(
                            type = screeningType,
                            completed = true,
                            mediumOfInstruction = "English",
                            participantId = participantId,
                            userId = mAuth.currentUser!!.uid,
                            scheduledDate = Date().time,
                            totalScore = totalScore,
                            comments = ""
                    )
            )
            showFragment(
                    Fragment.instantiate(
                            activity,
                            Comments::class.java.name
                    ),
                    false
            )
        }
    }

    fun addCategoryImage(category: String) {
        when (category) {
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