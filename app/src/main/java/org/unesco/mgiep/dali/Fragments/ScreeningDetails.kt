package org.unesco.mgiep.dali.Fragments

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.*
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.android.synthetic.main.fragment_screening_detail.*
import org.unesco.mgiep.dali.Activity.LanguageSelect
import org.unesco.mgiep.dali.Activity.ResultActivity
import org.unesco.mgiep.dali.Dagger.MyApplication
import org.unesco.mgiep.dali.Data.Gender
import org.unesco.mgiep.dali.Data.Screening
import org.unesco.mgiep.dali.Data.Participant
import org.unesco.mgiep.dali.Data.ViewModels.ScreeningParticipantViewModel
import org.unesco.mgiep.dali.Data.ViewModels.ScreeningViewModel
import org.unesco.mgiep.dali.R
import org.unesco.mgiep.dali.Repositories.FirebaseRepository
import org.unesco.mgiep.dali.Repositories.MainReposirtory
import org.unesco.mgiep.dali.Utility.hide
import org.unesco.mgiep.dali.Utility.show
import org.unesco.mgiep.dali.Utility.showFragment


class ScreeningDetails : Fragment() {

    private lateinit var participant: Participant
    private lateinit var screening: Screening
    private lateinit var screeningViewModel: ScreeningViewModel
    private lateinit var participantViewModel: ScreeningParticipantViewModel
    private lateinit var firebaseRepository: FirebaseRepository
    private lateinit var mainRepository: MainReposirtory
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    private var scheduled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        (activity!!.application as MyApplication).component.inject(this)
        firebaseAnalytics = FirebaseAnalytics.getInstance(context!!)
        screeningViewModel = ViewModelProviders.of(activity!!).get(ScreeningViewModel::class.java)
        participantViewModel = ViewModelProviders.of(activity!!).get(ScreeningParticipantViewModel::class.java)
        firebaseRepository = FirebaseRepository()
        mainRepository = MainReposirtory()
        screening = screeningViewModel.getScreening().value!!
        participant = participantViewModel.getParticipant().value!!
        scheduled = !screening.completed
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_screening_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp)

        tv_screeeningdetail_name.text = participant.name
        tv_screeningdetail_class.text = participant.sClass.toString()
        tv_screeeningdetail_section.text = participant.section
        tv_screeningdetail_mothertongue.text = participant.motherTongue
        tv_screeningdetail_score.text = screening.totalScore.toString()
        tv_screeningdetail_type.text = screening.type
        tv_screeeningdetail_comment.text = screening.comments
        tv_screeningdetail_lang.text = screening.assesmentLanguage

        if (scheduled) {
            tv_screeeningdetail_comments_title.hide()
            screening_detail_langugage_layout.hide()
            btn_screening_detail_result.hide()
            screening_detail_score_layout.hide()
            screening_comment_layout.hide()
            btn_screening_detail_start.show()
        }

        btn_screening_detail_start.setOnClickListener {
            startActivity(Intent(activity!!, LanguageSelect::class.java)
                    .putExtra("screeningId", screening.id)
                    .putExtra("type", screening.type)
                    .putExtra("participantId", screening.participantId)
                    .putExtra("participantName", screening.participantName))
        }

        if (participant.gender == Gender.FEMALE.toString()) {
            screendetail_female.visibility = View.VISIBLE
            screendetail_male.visibility = View.GONE
        } else {
            screendetail_female.visibility = View.GONE
            screendetail_male.visibility = View.VISIBLE
        }

        btn_screening_detail_result.setOnClickListener {
            startActivity(
                    Intent(activity, ResultActivity::class.java)
                            .putExtra("screening", false)
                            .putExtra("name", screening.participantName)
                            .putExtra("score", screening.totalScore)
                            .putExtra("type", screening.type)
                            .putExtra("language", screening.assesmentLanguage)
            )
        }
    }



override fun onResume() {
    super.onResume()
    firebaseAnalytics.setCurrentScreen(activity!!, "screening_details", ScreeningDetails::class.java.simpleName)
    activity!!.title = screening.participantName
}

private fun showFragment(fragment: Fragment, addToBackStack: Boolean = true) {
    fragment.showFragment(container = R.id.fragment_container,
            fragmentManager = activity!!.supportFragmentManager,
            addToBackStack = addToBackStack)
}
}