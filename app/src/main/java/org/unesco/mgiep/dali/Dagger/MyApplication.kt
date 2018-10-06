package org.unesco.mgiep.dali.Dagger

import android.app.Application
import com.chibatching.kotpref.Kotpref

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
        Kotpref.init(instance)
    }

    companion object {
        lateinit var instance: MyApplication
    }

    /*override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(LocaleManager().setLocale(base))
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        LocaleManager().setLocale(this)
    }
*/
}