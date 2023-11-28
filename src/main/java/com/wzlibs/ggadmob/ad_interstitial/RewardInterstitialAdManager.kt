package com.wzlibs.ggadmob.ad_interstitial

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback
import com.wzlibs.ggadmob.managers.GoogleMobileAdsConsentManager
import com.wzlibs.ggadmob.managers.LoadingGapManager
import com.wzlibs.ggadmob.ad_configs.AdmobConfigShared
import com.wzlibs.ggadmob.di.HiltEntryPoint
import dagger.hilt.android.EntryPointAccessors

class RewardInterstitialAdManager constructor(
    private val context: Context, private val sharedPref: AdmobConfigShared
) {
    private var rewardedInterstitialAd: RewardedInterstitialAd? = null
    private var isLoading: Boolean = false
    private var lasTimeLoaded = 0L
    private val loadingGapManager: LoadingGapManager = LoadingGapManager(sharedPref)

    var onUserEarnedRewardListener: IOnUserEarnedRewardListener? = null
    var onAdDismissedFullScreenContent: IOnAdDismissedFullScreenContent? = null
    var onAdShowedFullScreenContent: IOnAdShowedFullScreenContent? = null
    var onAdFailedToShowFullScreenContent: IOnAdFailedToShowFullScreenContent? = null

    private var rewardRequest: Any? = null
    private var isUserEarnedReward: Boolean = false

    private val idsManager =
        EntryPointAccessors.fromApplication(context, HiltEntryPoint::class.java)
            .rewardInterstitialAdsIdManager()

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

    fun isAdAlready(): Boolean = rewardedInterstitialAd != null

    private fun load(id: String, onContinueLoading: (() -> Unit)? = null) {
        if (isLoading) return
        if (rewardedInterstitialAd != null) return
        if (!loadingGapManager.isOverGap()) return
        if (!GoogleMobileAdsConsentManager.getInstance(context).canRequestAds) return
        isLoading = true
        RewardedInterstitialAd.load(context,
            id,
            AdManagerAdRequest.Builder().build(),
            object : RewardedInterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(p0: LoadAdError) {
                    if (onContinueLoading == null) {
                        isLoading = false
                        loadingGapManager.updateGap()
                    } else {
                        onContinueLoading.invoke()
                    }
                }

                override fun onAdLoaded(ad: RewardedInterstitialAd) {
                    super.onAdLoaded(ad)
                    lasTimeLoaded = System.currentTimeMillis()
                    rewardedInterstitialAd = ad
                    isLoading = false
                    loadingGapManager.resetGap()
                }
            })
    }

    fun show(activity: Activity, rewardRequest: Any?) {
        if (sharedPref.isAdShowFullScreen) return
        isUserEarnedReward = false
        val ad = rewardedInterstitialAd
        if (ad == null) {
            onAdFailedToShowFullScreenContent?.onAdFailedToShowFullScreenContent(rewardRequest)
            load()
        } else {
            setAdListener(ad)
            ad.show(activity, OnUserEarnedRewardListener {
                isUserEarnedReward = true
                onUserEarnedRewardListener?.onUserEarnedRewardListener(
                    rewardRequest
                )
            })
        }
    }

    private fun setAdListener(ad: RewardedInterstitialAd) {
        sharedPref.isAdShowFullScreen = true
        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent()
                sharedPref.isAdShowFullScreen = false
                onAdDismissedFullScreenContent?.onAdDismissedFullScreenContent(
                    isUserEarnedReward, rewardRequest
                )
            }

            override fun onAdShowedFullScreenContent() {
                super.onAdShowedFullScreenContent()
                rewardedInterstitialAd = null
                onAdShowedFullScreenContent?.onAdShowedFullScreenContent(rewardRequest)
                load()
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                super.onAdFailedToShowFullScreenContent(p0)
                sharedPref.isAdShowFullScreen = false
                onAdFailedToShowFullScreenContent?.onAdFailedToShowFullScreenContent(rewardRequest)
                load()
            }
        }
    }

    interface IOnUserEarnedRewardListener {
        fun onUserEarnedRewardListener(rewardRequest: Any?)
    }

    interface IOnAdDismissedFullScreenContent {
        fun onAdDismissedFullScreenContent(isUserEarnedReward: Boolean, rewardRequest: Any?)
    }

    interface IOnAdShowedFullScreenContent {
        fun onAdShowedFullScreenContent(rewardRequest: Any?)
    }

    interface IOnAdFailedToShowFullScreenContent {
        fun onAdFailedToShowFullScreenContent(rewardRequest: Any?)
    }

}