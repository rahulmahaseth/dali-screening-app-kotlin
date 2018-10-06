package org.unesco.mgiep.dali.Activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity


abstract class BaseActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
    }

    /*override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleManager().setLocale(base))
    }*/

    protected abstract fun getLayoutId(): Int
}