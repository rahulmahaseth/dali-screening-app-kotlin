package org.unesco.mgiep.dali.Data.ViewModels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import org.unesco.mgiep.dali.Data.Screening

class ScreeningViewModel: ViewModel() {
    private var nextScreening = MutableLiveData<Screening>()

    fun select(screening: Screening){
        nextScreening.value = screening
    }

    fun getScreening(): LiveData<Screening> {
        return nextScreening
    }
}