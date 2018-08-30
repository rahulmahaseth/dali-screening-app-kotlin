package org.unesco.mgiep.dali.Repositories

import android.content.res.Resources
import io.reactivex.Observable
import org.unesco.mgiep.dali.Data.Screening
import org.unesco.mgiep.dali.Data.Type
import org.unesco.mgiep.dali.R

class MainReposirtory {

    private val jstQuestionsArray = Resources.getSystem().getStringArray(R.array.jst_questions)
    private val jstExampleArray = Resources.getSystem().getStringArray(R.array.jst_examples)
    private val mstQuestionsArray = Resources.getSystem().getStringArray(R.array.mst_questions)
    private val mstExamplesArray =  Resources.getSystem().getStringArray(R.array.mst_examples)

    fun getScreenings(): Observable<List<Screening>> {
        return
    }

    fun nextQuestion(questionNumber : Int, type: Type):String{
        return if(type == Type.JST){
             jstQuestionsArray[questionNumber]
        }else{
            mstQuestionsArray[questionNumber]
        }
    }

    fun nextExample(questionNumber : Int, type: Type):String{
        return if(type == Type.JST){
            jstExampleArray[questionNumber]
        }else{
            mstExamplesArray[questionNumber]
        }
    }

}