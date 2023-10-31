package com.kapitalletter.wardoffice.view.mainview.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.kapitalletter.wardoffice.MyGlobals

class AdmobController(
    private val context: Context
    ) {

    private var mInterstitialAd: InterstitialAd? = null

    fun detailFragmentClickCounter() {
        MyGlobals.instance?.adMobCount = MyGlobals.instance?.adMobCount!! + 1
    }

    fun mapInfoClickCallAd() {
        if (MyGlobals.instance?.adMobCount!! % 5 == 0) {
            MyGlobals.instance?.fullAD!!.show(context as Activity)
        }
    }

    @SuppressLint("MissingPermission")
    fun setAdFullScreen(
        mAdView : AdView,
        adRequest : AdRequest
    ) {
        mAdView.loadAd(adRequest)

        mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {}
            override fun onAdFailedToShowFullScreenContent(p0: AdError) {}
            override fun onAdShowedFullScreenContent() {
                mInterstitialAd = null
            }
        }
        getAD(adRequest)
    }

    private fun getAD(
        adRequest: AdRequest
    ) {
        InterstitialAd.load(
            context,
            "ca-app-pub-6701701941192034/1024816172",
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(p0: LoadAdError) {
                    mInterstitialAd = null
                }

                override fun onAdLoaded(p0: InterstitialAd) {
                    mInterstitialAd = p0
                    MyGlobals.instance?.fullAD = mInterstitialAd
                }
            })
    }

}