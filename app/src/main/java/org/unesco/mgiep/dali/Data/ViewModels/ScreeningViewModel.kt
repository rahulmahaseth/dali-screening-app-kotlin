package org.unesco.mgiep.dali.Data.ViewModels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import org.unesco.mgiep.dali.Data.FirebaseScreening

class ScreeningViewModel: ViewModel() {
    private var nextScreening = MutableLiveData<FirebaseScreening>()

    fun select(screening: FirebaseScreening){
        nextScreening.value = screening
    }

    fun getQuestion(): LiveData<FirebaseScreening> {
        return nextScreening
    }
}