package org.unesco.mgiep.dali.Fragments

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.res.ResourcesCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.common.io.Resources
import kotlinx.android.synthetic.main.fragment_screening.*
import org.unesco.mgiep.dali.Dagger.MyApplication
import org.unesco.mgiep.dali.Data.FirebaseScreening
import org.unesco.mgiep.dali.Data.ViewModels.ScreeningParticipantViewModel
import org.unesco.mgiep.dali.Data.ViewModels.ScreeningViewModel
import org.unesco.mgiep.dali.R
import org.unesco.mgiep.dali.Repositories.MainReposirtory
import org.unesco.mgiep.dali.Utility.showFragment
import javax.inject.Inject

class Screening : Fragment() {

    lateinit var screeningParticipantViewModel: ScreeningParticipantViewModel
    private lateinit var screeningViewModel: ScreeningViewModel

    lateinit var mainReposirtory: MainReposirtory
    lateinit var screening: FirebaseScreening

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainReposirtory = MainReposirtory()
        (activity!!.application as MyApplication).component.inject(this)
        screeningViewModel = ViewModelProviders.of(activity!!).get(ScreeningViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?=
            inflater.inflate(R.layout.fragment_screening, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        screening = screeningViewModel.getScreening().value!!.copy()
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
        screening.questionsCompleted += 1
        screening.totalScore += score
        if(screening.questionsCompleted == screening.totalQuestions){
            showFragment(
                    Fragment.instantiate(
                            activity,
                            Comments::class.java.name
                    ),
                    false
            )
        }else{
            tv_question.text = resources.getStringArray(R.array.jst_questions)[screening.questionsCompleted]
            tv_example.text = resources.getStringArray(R.array.jst_examples)[screening.questionsCompleted]
        }
    }

    private fun showFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        fragment.showFragment(container = R.id.fragment_container,
                fragmentManager = activity!!.supportFragmentManager,
                addToBackStack = addToBackStack)
    }
}