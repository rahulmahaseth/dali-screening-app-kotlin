package org.unesco.mgiep.dali.Data.ViewModels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import org.unesco.mgiep.dali.Data.Question

class QuestionViewModel: ViewModel() {
    private var nextQuestion = MutableLiveData<Question>()

    fun select(question: Question){
        nextQuestion.value = question
    }

    fun getQuestion(): LiveData<Question>{
        return nextQuestion
    }
}