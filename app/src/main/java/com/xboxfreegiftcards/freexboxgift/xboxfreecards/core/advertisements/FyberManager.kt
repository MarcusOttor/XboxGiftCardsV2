package com.xboxfreegiftcards.freexboxgift.xboxfreecards.core.advertisements

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.xboxfreegiftcards.freexboxgift.xboxfreecards.core.analytics.Analytics
import com.fyber.Fyber
import com.fyber.ads.AdFormat
import com.fyber.currency.VirtualCurrencyErrorResponse
import com.fyber.currency.VirtualCurrencyResponse
import com.fyber.requesters.*
import com.xboxfreegiftcards.freexboxgift.xboxfreecards.core.managers.CoinsManager
import com.xboxfreegiftcards.freexboxgift.xboxfreecards.core.managers.PreferencesManager

class FyberManager(
        private var coinsManager: CoinsManager,
        private var preferencesManager: PreferencesManager) {

    private lateinit var coinView: TextView
    private var isAvailable = false
    private var offerWallIntent: Intent? = null

    fun init(activity: AppCompatActivity) {
        Fyber.with(appId, activity)
                .withSecurityToken(SecurityToken)
                .start()
        OfferWallRequester.create(object : RequestCallback {

            override fun onAdNotAvailable(p0: AdFormat?) {
                isAvailable = false
            }

            override fun onRequestError(p0: RequestError?) {
                isAvailable = false
            }

            override fun onAdAvailable(offerWallIntent: Intent?) {
                this@FyberManager.offerWallIntent = offerWallIntent
                isAvailable = true
            }
        }).request(activity)

        VirtualCurrencyRequester.create(currencyCallback).request(activity)
    }

    private var currencyCallback = object : VirtualCurrencyCallback {
        override fun onSuccess(p0: VirtualCurrencyResponse?) {
            if (p0?.deltaOfCoins?.toInt() ?: 0 > 0) {
                coinsManager.addCoins(p0?.deltaOfCoins?.toInt() ?: 0)
                coinView.text = coinsManager.getCoins().toString()
                Analytics.report(Analytics.OFFER, Analytics.FYBER, Analytics.REWARD)
            }
        }

        override fun onRequestError(p0: RequestError?) {}
        override fun onError(p0: VirtualCurrencyErrorResponse?) {}
    }

    fun onResume(activity: AppCompatActivity) {
        VirtualCurrencyRequester.create(currencyCallback).request(activity)
    }

    fun show(activity: AppCompatActivity, coinsView: TextView): Boolean {
        this.coinView = coinsView
        if (isAvailable) activity.startActivity(offerWallIntent)
        Analytics.report(Analytics.OFFER, Analytics.FYBER, Analytics.OPEN)
        return isAvailable

    }

    companion object {
        val appId = "109610"
        val SecurityToken = "4255447eee4baece62389d318068bf34"
    }
}
