package org.unesco.mgiep.dali.Data.ViewModels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.unesco.mgiep.dali.Data.Screening
import org.unesco.mgiep.dali.Repositories.MainReposirtory
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ScreeningsListViewModel @Inject constructor(val mainRepository: MainReposirtory): ViewModel() {
    private val screeningsSuccess = MutableLiveData<List<Screening>>()
    private val screeningsError : MutableLiveData<String> = MutableLiveData()

    lateinit var disposableObserver: DisposableObserver<List<Screening>>

    fun screeningResult(): MutableLiveData<List<Screening>>{
        return screeningsSuccess
    }

    fun screeningError(): MutableLiveData<String>{
        return screeningsError
    }

    fun loadScreenings(){
        disposableObserver = object : DisposableObserver<List<Screening>>(){
            override fun onError(error: Throwable) {
                screeningsError.postValue(error.message)
            }

            override fun onNext(screenings: List<Screening>) {
                screeningsSuccess.postValue(screenings)
            }

            override fun onComplete() {
            }

        }

        mainRepository.getScreenings()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .debounce(400, TimeUnit.MILLISECONDS)
                .subscribe(disposableObserver)

    }

    fun disposeElements(){
        if(!disposableObserver.isDisposed) disposableObserver.dispose()
    }
}