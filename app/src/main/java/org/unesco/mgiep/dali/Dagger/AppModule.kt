package org.unesco.mgiep.dali.Dagger

import android.arch.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import org.unesco.mgiep.dali.Data.ViewModels.ScreeningsListViewModelFactory
import javax.inject.Singleton

@Module
class AppModule(private val app: MyApplication) {
    @Singleton
    @Provides
    @ForApplication
    fun provideApplication() = app

    @Provides
    fun provideScreeningsListViewModelFactory(factory: ScreeningsListViewModelFactory):
            ViewModelProvider.Factory = factory
}