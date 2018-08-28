package org.unesco.mgiep.dali.Dagger

import android.app.Application

class MyApplication: Application() {
    val component: AppComponent by lazy {
        DaggerAppComponent
                .builder()
                .appModule(AppModule(this))
                .build()
    }

    override fun onCreate() {
        super.onCreate()
        component.inject(this)
        instance = this
    }

    companion object {
        lateinit var instance: MyApplication
    }
}