package org.unesco.mgiep.dali.Dagger

import dagger.Component
import org.unesco.mgiep.dali.Activity.MainActivity
import org.unesco.mgiep.dali.Activity.NewScreeningActivity
import org.unesco.mgiep.dali.Activity.SplashActivity
import org.unesco.mgiep.dali.Fragments.*
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(app: MyApplication)
    fun inject(mainActivity: MainActivity)
    fun inject(dashboard: Dashboard)
    fun inject(newScreening: NewScreening)
    fun inject(login: Login)
    fun inject(forgotPassword: ForgotPassword)
    fun inject(signUp: SignUp)
    fun inject(splashActivity: SplashActivity)
    fun inject(screening: Screening)
    fun inject(comments: Comments)
    fun inject(settings: Settings)
    fun inject(pendingScreenings: PendingScreenings)
    fun inject(screeningDetails: ScreeningDetails)
    fun inject(newScreeningActivity: NewScreeningActivity)
    fun inject(home: Home)
    fun inject(newScreening2: NewScreening2)
    fun inject(participantConfirm: ParticipantConfirm)
    fun inject(preScreeningIntro: PreScreeningIntro)
}