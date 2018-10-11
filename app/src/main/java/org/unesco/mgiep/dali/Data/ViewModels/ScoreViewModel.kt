package org.unesco.mgiep.dali.Data.ViewModels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import org.unesco.mgiep.dali.Data.QuestionWiseScore

class QuestionWiseScoreViewModel: ViewModel() {
    private var nextScore = MutableLiveData<QuestionWiseScore>()

    fun select(score: QuestionWiseScore){
        nextScore.value = score
    }

    fun getScore(): LiveData<QuestionWiseScore> {
        return nextScore
    }
}