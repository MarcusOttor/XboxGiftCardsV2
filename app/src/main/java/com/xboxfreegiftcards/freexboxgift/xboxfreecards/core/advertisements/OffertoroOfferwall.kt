package com.xboxfreegiftcards.freexboxgift.xboxfreecards.core.advertisements

import android.app.Activity
import android.widget.TextView
import com.xboxfreegiftcards.freexboxgift.xboxfreecards.AppTools
import com.xboxfreegiftcards.freexboxgift.xboxfreecards.core.analytics.Analytics
import com.xboxfreegiftcards.freexboxgift.xboxfreecards.core.managers.CoinsManager
import com.xboxfreegiftcards.freexboxgift.xboxfreecards.core.managers.PreferencesManager
import com.offertoro.sdk.OTOfferWallSettings
import com.offertoro.sdk.interfaces.OfferWallListener
import com.offertoro.sdk.interfaces.VideoOfferListener
import com.offertoro.sdk.sdk.OffersInit

class OffertoroOfferwall(private var preferencesManager: PreferencesManager) {

    private var coinsText: TextView? = null
    lateinit var coinManager: CoinsManager

    constructor(coinManager: CoinsManager, preferencesManager: PreferencesManager) : this(preferencesManager) {
        this.coinManager = coinManager
    }

    fun init(activity: Activity) {

        OTOfferWallSettings.getInstance().configInit(
                "3949",
                "766e5309001ee1e42ce1fea77680ec49",
                AppTools.uniqueId(activity))
        OffersInit.getInstance().create(activity)

        OffersInit.getInstance().setOfferWallListener(object : OfferWallListener {
            override fun onOTOfferWallInitSuccess() {

            }

            override fun onOTOfferWallInitFail(s: String) {

            }

            override fun onOTOfferWallOpened() {
            }

            override fun onOTOfferWallCredited(v: Double, v1: Double) {
                if (v.toInt() != 0) {
                    coinManager.addCoins(v.toInt())
                    Analytics.report(Analytics.OFFER, Analytics.OFFERTORO, Analytics.REWARD)
                } else if (v1.toInt() != 0) {
                    coinManager.addCoins(v1.toInt())
                    Analytics.report(Analytics.OFFER, Analytics.OFFERTORO, Analytics.REWARD)
                }
                try {
                    coinsText!!.text = coinManager.getCoins().toString()
                } catch (ex: Exception) {}
            }

            override fun onOTOfferWallClosed() {

            }
        })

        OffersInit.getInstance().setVideoOfferListener(object : VideoOfferListener {
            override fun onOTRVideosInitSuccess() {

            }

            override fun onOTRVideosInitFail(s: String) {

            }

            override fun onOTRVideoAvailability(b: Boolean) {

            }

            override fun onOTRVideoStart() {
            }

            override fun onOTRVideoCredited(v: Double) {
                if (v.toInt() > 0) {
                    Analytics.report(Analytics.OFFER, Analytics.OFFERTORO, Analytics.REWARD)
                }
                try {
                    coinManager.addCoins(v.toInt())
                } catch (ex: Exception) {}
            }
        })

    }


    fun show(activity: Activity, coinsText: TextView): Boolean {
        this.coinsText = coinsText
        OffersInit.getInstance().showOfferWall(activity)
        Analytics.report(Analytics.OFFER, Analytics.OFFERTORO, Analytics.OPEN)
        return true
    }

    fun showVideo(activity: Activity) {
        OffersInit.getInstance().showVideoOffer(activity)
    }
}
