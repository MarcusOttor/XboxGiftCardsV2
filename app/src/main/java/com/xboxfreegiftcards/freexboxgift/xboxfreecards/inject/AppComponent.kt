package com.xboxfreegiftcards.freexboxgift.xboxfreecards.inject

import com.xboxfreegiftcards.freexboxgift.xboxfreecards.core.MyApplication
import com.xboxfreegiftcards.freexboxgift.xboxfreecards.core.services.ClaimService
import com.xboxfreegiftcards.freexboxgift.xboxfreecards.screens.dialogs.LoginDialog
import com.xboxfreegiftcards.freexboxgift.xboxfreecards.screens.dialogs.PromocodeDialog
import com.xboxfreegiftcards.freexboxgift.xboxfreecards.screens.dialogs.RedeemDialog
import com.xboxfreegiftcards.freexboxgift.xboxfreecards.screens.dialogs.SignupDialog
import com.xboxfreegiftcards.freexboxgift.xboxfreecards.screens.BaseActivity
import dagger.Component

@Component(modules = arrayOf(AppModule::class, MainModule::class))
interface AppComponent {

    fun inject(screen: BaseActivity)
    fun inject(app: MyApplication)
    fun inject(dialog: LoginDialog)
    fun inject(dialog: SignupDialog)
    fun inject(dialog: PromocodeDialog)
    fun inject(dialog: RedeemDialog)
    fun inject(service: ClaimService)
}
