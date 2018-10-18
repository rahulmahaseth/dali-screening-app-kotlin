package org.unesco.mgiep.dali.Fragments

import android.arch.lifecycle.ViewModelProviders
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_screening.*
import org.unesco.mgiep.dali.Dagger.MyApplication
import org.unesco.mgiep.dali.Data.QuestionWiseScore
import org.unesco.mgiep.dali.Data.Screening
import org.unesco.mgiep.dali.Data.Type
import org.unesco.mgiep.dali.Data.ViewModels.QuestionWiseScoreViewModel
import org.unesco.mgiep.dali.Data.ViewModels.ScreeningViewModel
import org.unesco.mgiep.dali.R
import org.unesco.mgiep.dali.Repositories.MainReposirtory
import org.unesco.mgiep.dali.Utility.showAsToast
import org.unesco.mgiep.dali.Utility.showFragment
import java.util.*
import kotlin.collections.HashMap

class Screening : Fragment() {

    private lateinit var screeningViewModel: ScreeningViewModel
    private lateinit var questionWiseScoreViewModel: QuestionWiseScoreViewModel
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    lateinit var mainReposirtory: MainReposirtory
    private var totalQuestions = 0
    private var questionsCompleted = 0
    private var totalScore = 0
    private var screeningType = ""
    private var participantId = ""
    private var participantName = ""
    private var assessmentLanguage = ""
    private var screeningId = ""
    private var questionWiseScoreId = UUID.randomUUID().toString()
    private lateinit var mAuth: FirebaseAuth
    private val questionAnswerMap: HashMap<Int, Int> = hashMapOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity!!.application as MyApplication).component.inject(this)
        mainReposirtory = MainReposirtory()

        screeningViewModel = ViewModelProviders.of(activity!!).get(ScreeningViewModel::class.java)
        questionWiseScoreViewModel = ViewModelProviders.of(activity!!).get(QuestionWiseScoreViewModel::class.java)

        firebaseAnalytics = FirebaseAnalytics.getInstance(context!!)
        mAuth = FirebaseAuth.getInstance()

        screeningId = activity!!.intent.getStringExtra("screeningId")
        screeningType = activity!!.intent.getStringExtra("type")
        participantId = activity!!.intent.getStringExtra("participantId")
        participantName = activity!!.intent.getStringExtra("participantName")
        assessmentLanguage = activity!!.intent.getStringExtra("screeningLang")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_screening, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if(Locale.getDefault().language == "hi"){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tv_question.letterSpacing = 0.2f
                tv_example.letterSpacing = 0.2f
            }
        }

        updateButtons()
        updateNavButtons()

        totalQuestions = if (screeningType == Type.JST.toString()) {
            15
        } else {
            21
        }

        tv_questions_completed.text = (questionsCompleted + 1).toString()
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
            if(questionsCompleted < (totalQuestions - 1)){
                next()
            }
        }

        btn_sometimes.setOnClickListener {
            questionAnswerMap[questionsCompleted] = 1
            updateButtons()
            if(questionsCompleted < (totalQuestions - 1)){
                next()
            }
        }

        btn_never.setOnClickListener {
            questionAnswerMap[questionsCompleted] = 0
            updateButtons()
            if(questionsCompleted < (totalQuestions - 1)){
                next()
            }
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
            null -> {
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

    private fun updateNavButtons() {
        when {
            questionsCompleted == 0 -> {
                btn_screening_back.isEnabled = false
                btn_screening_back.setTextColor(ContextCompat.getColor(activity!!,R.color.colorPrimaryDark))
            }
            questionsCompleted > 0 && questionsCompleted < (totalQuestions - 1) -> {
                btn_screening_back.setTextColor(ContextCompat.getColor(activity!!,R.color.white))
                btn_screening_back.isEnabled = true
                btn_screening_next.visibility = View.VISIBLE
                btn_screening_submit.visibility = View.GONE
            }
            else -> {

                btn_screening_next.visibility = View.GONE
                btn_screening_submit.visibility = View.VISIBLE
            }

        }

    }

    private fun next() {

        if (!btn_usually.isChecked && !btn_sometimes.isChecked && !btn_never.isChecked) {
            getString(R.string.select_option_error).showAsToast(activity!!, true)
        } else {
            ++questionsCompleted
            Log.d("Q", "$questionsCompleted / $totalQuestions")

            screening_radio_group.clearCheck()

            updateButtons()
            updateNavButtons()



            tv_questions_completed.text = (questionsCompleted + 1).toString()
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

        }
    }

    private fun back() {
        --questionsCompleted
        Log.d("Q", "$questionsCompleted / $totalQuestions")
        updateNavButtons()
        updateButtons()
        tv_questions_completed.text = questionsCompleted.toString()
        tv_total_questions.text = totalQuestions.toString()



        if (screeningType == Type.JST.toString()) {
            tv_questions_completed.text = (questionsCompleted + 1).toString()
            tv_total_questions.text = totalQuestions.toString()
            addCategoryImage(resources.getStringArray(R.array.jst_categories)[questionsCompleted])
            tv_question.text = resources.getStringArray(R.array.jst_questions)[questionsCompleted]
            tv_example.text = resources.getStringArray(R.array.jst_examples)[questionsCompleted]
        } else {
            tv_questions_completed.text = (questionsCompleted + 1).toString()
            tv_total_questions.text = totalQuestions.toString()
            addCategoryImage(resources.getStringArray(R.array.mst_categories)[questionsCompleted])
            tv_question.text = resources.getStringArray(R.array.mst_questions)[questionsCompleted]
            tv_example.text = resources.getStringArray(R.array.mst_examples)[questionsCompleted]
        }
    }

    private fun submit() {

        if (!btn_usually.isChecked && !btn_sometimes.isChecked && !btn_never.isChecked) {
            getString(R.string.select_option_error).showAsToast(activity!!, true)
        } else {

            AlertDialog.Builder(activity!!)
                    .setMessage(getString(R.string.submit_screening_prompt))
                    .setPositiveButton(getString(R.string.yes)){ _, _->
                        questionAnswerMap.forEach {
                            Log.d("Q - ${it.key}", "${it.value}")
                            totalScore += it.value
                        }

                        Log.d("totalScore", "$totalScore")

                        screeningViewModel.select(
                                Screening(
                                        id = screeningId,
                                        type = screeningType,
                                        completed = true,
                                        participantId = participantId,
                                        userId = mAuth.currentUser!!.uid,
                                        scheduledDate = Date().time,
                                        totalScore = totalScore,
                                        comments = "",
                                        participantName = participantName,
                                        assesmentLanguage = assessmentLanguage
                                )
                        )

                        questionWiseScoreViewModel.select(
                                QuestionWiseScore(
                                        questionWiseScoreId,
                                        screeningId,
                                        getStringKeyScores()
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
                    .setNegativeButton(getString(R.string.no)){ _, _->}
                    .create()
                    .show()

        }
    }

    private fun getStringKeyScores(): HashMap<String,Int>{
        var temp = hashMapOf<String, Int>()
        questionAnswerMap.forEach {
            temp[it.key.toString()] = it.value
        }
        return  temp
    }

    fun addCategoryImage(category: String) {
        when (category) {
            "sound" -> {
                tv_category.text = getString(R.string.cat_sound)
                image_category.setImageResource(R.drawable.ic_hearing_01)
            }
            "communication" -> {
                tv_category.text = getString(R.string.cat_communication)
                image_category.setImageResource(R.drawable.ic_communication_01)
            }
            "number" -> {
                tv_category.text = getString(R.string.cat_number)
                image_category.setImageResource(R.drawable.ic_numberconcept_01)
            }
            "memory" -> {
                tv_category.text = getString(R.string.cat_memory)
                image_category.setImageResource(R.drawable.ic_memory_01)
            }
            "motor" -> {
                tv_category.text = getString(R.string.cat_motor)
                image_category.setImageResource(R.drawable.ic_motorcordination_01)
            }
            "behavior" -> {
                tv_category.text = getString(R.string.cat_behavior)
                image_category.setImageResource(R.drawable.ic_behaviour_01)
            }
            "reading" -> {
                tv_category.text = getString(R.string.cat_print)
                image_category.setImageResource(R.drawable.ic_reading_01)
            }
            "writing" -> {
                tv_category.text = getString(R.string.cat_writing)
                image_category.setImageResource(R.drawable.ic_writing_01)
            }
            "readingmst" -> {
                tv_category.text = getString(R.string.reading_mst)
                image_category.setImageResource(R.drawable.ic_reading_01)
            }
            "writingmst" -> {
                tv_category.text = getString(R.string.writing_mst)
                image_category.setImageResource(R.drawable.ic_writing_01)
            }
            "numbermst" -> {
                tv_category.text = getString(R.string.mathematics)

                image_category.setImageResource(R.drawable.ic_numberconcept_01)
            }
        }
    }

    private fun showFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        fragment.showFragment(container = R.id.screening_fragment_container,
                fragmentManager = activity!!.supportFragmentManager,
                addToBackStack = addToBackStack)
    }

    override fun onResume() {
        super.onResume()
        firebaseAnalytics.setCurrentScreen(activity!!, "screening", org.unesco.mgiep.dali.Fragments.Screening::class.java.simpleName)
    }
}