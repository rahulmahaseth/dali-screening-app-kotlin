package org.unesco.mgiep.dali.Dagger

import dagger.Component
import org.unesco.mgiep.dali.Activity.MainActivity
import org.unesco.mgiep.dali.Fragments.*
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(app: MyApplication)
    fun inject(mainActivity: MainActivity)
    fun inject(dashboard: Dashboard)
    fun inject(screeningDashboard: ScreeningDashboard)
    fun inject(newScreening: NewScreening)
    fun inject(scoreCard: ScoreCard)
    fun inject(languageSelect: LanguageSelect)
    fun inject(login: Login)
    fun inject(forgotPassword: ForgotPassword)
    fun inject(signUp: SignUp)

}