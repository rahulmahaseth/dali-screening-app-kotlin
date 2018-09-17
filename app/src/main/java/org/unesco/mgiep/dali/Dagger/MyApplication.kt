package org.unesco.mgiep.dali.Dagger

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import org.unesco.mgiep.dali.Utility.LocaleManager

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

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(LocaleManager().setLocale(base!!))
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        LocaleManager().setLocale(this)
    }
}