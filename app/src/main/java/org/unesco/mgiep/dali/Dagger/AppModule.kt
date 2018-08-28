package org.unesco.mgiep.dali.Dagger

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val app: MyApplication) {
    @Singleton
    @Provides
    @ForApplication
    fun provideApplication() = app
}