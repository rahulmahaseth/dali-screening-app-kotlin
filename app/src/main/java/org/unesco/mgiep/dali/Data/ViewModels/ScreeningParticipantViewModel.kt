package org.unesco.mgiep.dali.Data.ViewModels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import org.unesco.mgiep.dali.Data.Participant

class ScreeningParticipantViewModel : ViewModel() {
    private var screeningParticipant = MutableLiveData<Participant>()

    fun select(participant: Participant){
        screeningParticipant.value = participant
    }

    fun getParticipant(): LiveData<Participant>{
        return screeningParticipant
    }
}