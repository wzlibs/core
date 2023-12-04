package com.wzlibs.ggadmob.ad_interstitial

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.wzlibs.ggadmob.managers.GoogleMobileAdsConsentManager
import com.wzlibs.ggadmob.managers.LoadingGapManager
import com.wzlibs.ggadmob.ad_configs.AdmobConfigShared
import com.wzlibs.ggadmob.ad_configs.AdmobConfig.INTERSTITIAL_AD_VALID_TIME
import com.wzlibs.ggadmob.di.HiltEntryPoint
import dagger.hilt.android.EntryPointAccessors

class InterstitialAdManager constructor(
    private val context: Context,
    private val sharedPref: AdmobConfigShared,
    private val loadingGapManager: LoadingGapManager
) {
    private var interstitialAd: InterstitialAd? = null
    private var isLoading: Boolean = false
    private var lasTimeLoaded = 0L
    private var lasTimeDismissAd = 0L

    fun isAdAlready(): Boolean = interstitialAd != null

    private val idsManager =
        EntryPointAccessors.fromApplication(context, HiltEntryPoint::class.java)
            .interstitialAdsIdManager()

    fun load() {
        if (sharedPref.monetization) {
            load(idsManager.getHighFloorId()) {
                load(idsManager.getMediumFloorId()) {
                    load(idsManager.getAllPricesId())
                }
            }
        } else {
            load(idsManager.getNormalId())
        }
    }

    private fun load(id: String, onContinueLoading: (() -> Unit)? = null) {
        if (isLoading) return
        if (interstitialAd != null) return
        if (!loadingGapManager.isOverGap()) return
        if (!GoogleMobileAdsConsentManager.getInstance(context).canRequestAds) return
        isLoading = true
        InterstitialAd.load(
            context,
            id,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(p0: LoadAdError) {
                    if (onContinueLoading == null) {
                        isLoading = false
                        loadingGapManager.updateGap()
                    } else {
                        onContinueLoading.invoke()
                    }
                }

                override fun onAdLoaded(ad: InterstitialAd) {
                    super.onAdLoaded(ad)
                    lasTimeLoaded = System.currentTimeMillis()
                    interstitialAd = ad
                    isLoading = false
                    loadingGapManager.resetGap()
                }
            })
    }

    fun show(activity: Activity, onTransition: () -> Unit) {
        val ad = interstitialAd
        if (ad == null) {
            onTransition.invoke()
            load()
        } else {
            if (shouldShow()) {
                setAdListener(ad, onTransition)
                ad.show(activity)
            } else {
                onTransition.invoke()
            }
        }
    }

    private fun shouldShow(): Boolean {
        if (sharedPref.isAdShowFullScreen) return false
        if (sharedPref.isUnlockedAd) return false
        if (interstitialAd == null) {
            load()
            return false
        }
        if (System.currentTimeMillis() - lasTimeLoaded > INTERSTITIAL_AD_VALID_TIME) {
            interstitialAd = null
            load()
            return false
        }
        if (System.currentTimeMillis() - lasTimeDismissAd < sharedPref.popupAdsGap) {
            return false
        }
        return true
    }

    fun forceShow(activity: Activity, onTransition: () -> Unit) {
        val ad = interstitialAd
        interstitialAd = null
        if (ad == null) {
            onTransition.invoke()
            load()
        } else {
            setAdListener(ad, onTransition)
            ad.show(activity)
        }
    }

    private fun setAdListener(ad: InterstitialAd, onTransition: () -> Unit) {
        sharedPref.isAdShowFullScreen = true
        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                sharedPref.isAdShowFullScreen = false
                super.onAdDismissedFullScreenContent()
                onTransition.invoke()
                lasTimeDismissAd = System.currentTimeMillis()
            }

            override fun onAdShowedFullScreenContent() {
                super.onAdShowedFullScreenContent()
                interstitialAd = null
                load()
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                super.onAdFailedToShowFullScreenContent(p0)
                sharedPref.isAdShowFullScreen = false
                load()
                onTransition.invoke()
                lasTimeDismissAd = System.currentTimeMillis()
            }
        }
    }

}