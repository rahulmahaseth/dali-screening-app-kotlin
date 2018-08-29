package org.unesco.mgiep.dali.Data.ViewModels

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import javax.inject.Inject

class ScreeningsListViewModelFactory @Inject constructor(val screeningsListViewModel: ScreeningsListViewModel)
    :ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(screeningsListViewModel::class.java)){
            return screeningsListViewModel as T
        }
        throw IllegalArgumentException("Unknown Class Name")
    }
}