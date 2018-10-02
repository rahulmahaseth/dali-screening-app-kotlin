package org.unesco.mgiep.dali.Fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_result.*
import org.unesco.mgiep.dali.Activity.MainActivity
import org.unesco.mgiep.dali.Data.AssessmentLanguage
import org.unesco.mgiep.dali.Data.Type
import org.unesco.mgiep.dali.R
import org.unesco.mgiep.dali.Utility.show

class Result: Fragment() {

    private var name = ""
    private var score = 0
    private var type = ""
    private var language = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        name = activity!!.intent.getStringExtra("name")
        score = activity!!.intent.getIntExtra("score", 0)
        type = activity!!.intent.getStringExtra("type")
        language = activity!!.intent.getStringExtra("language")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_result,container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tv_result_student_name.text = name
        if(language == AssessmentLanguage.ENGLISH.toString()){
            if(type == Type.JST.toString()){
                //tv_result_total.text = 30.toString()
                if(score > 12){
                    result_recommendation_layout.show()
                    tv_result_summary.text = getString(R.string.at_risk)

                }else{
                    tv_result_summary.text = getString(R.string.not_at_risk)
                }
            }else{
                //tv_result_total.text = 42.toString()
                if(score > 23){
                    result_recommendation_layout.show()
                    tv_result_summary.text = getString(R.string.at_risk)
                }else{
                    tv_result_summary.text = getString(R.string.not_at_risk)
                }
            }
        }else{
            if(type == Type.JST.toString()){
                //tv_result_total.text = 30.toString()
                if(score > 16){
                    result_recommendation_layout.show()
                    tv_result_summary.text = getString(R.string.at_risk)

                }else{
                    tv_result_summary.text = getString(R.string.not_at_risk)
                }
            }else{
                //tv_result_total.text = 42.toString()
                if(score > 19){
                    result_recommendation_layout.show()
                    tv_result_summary.text = getString(R.string.at_risk)
                }else{
                    tv_result_summary.text = getString(R.string.not_at_risk)
                }
            }
        }

        btn_done_result.setOnClickListener {
            startActivity(Intent(activity!!, MainActivity::class.java))
            activity!!.finish()
        }

    }
}