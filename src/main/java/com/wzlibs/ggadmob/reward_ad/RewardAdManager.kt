package com.wzlibs.ggadmob.reward_ad

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.wzlibs.ggadmob.managers.GoogleMobileAdsConsentManager
import com.wzlibs.ggadmob.ad_configs.AdmobConfigShared
import com.wzlibs.ggadmob.di.HiltEntryPoint
import dagger.hilt.android.EntryPointAccessors

class RewardAdManager constructor(
    private val context: Context,
    private val sharedPref: AdmobConfigShared
) {
    private var rewardedAd: RewardedAd? = null
    private var isLoading: Boolean = false
    private var lasTimeLoaded = 0L

    var onUserEarnedRewardListener: IOnUserEarnedRewardListener? = null
    var onAdDismissedFullScreenContent: IOnAdDismissedFullScreenContent? = null
    var onAdShowedFullScreenContent: IOnAdShowedFullScreenContent? = null
    var onAdFailedToShowFullScreenContent: IOnAdFailedToShowFullScreenContent? = null

    private var rewardRequest: Any? = null
    private val idsManager =
        EntryPointAccessors.fromApplication(context, HiltEntryPoint::class.java)
            .rewardAdsIdManager()

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

    fun isAdAlready(): Boolean = rewardedAd != null

    private fun load(id: String, onContinueLoading: (() -> Unit)? = null) {
        if (isLoading) return
        if (rewardedAd != null) return
        if (!GoogleMobileAdsConsentManager.getInstance(context).canRequestAds) return
        isLoading = true
        RewardedAd.load(context,
            id,
            AdManagerAdRequest.Builder().build(),
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(p0: LoadAdError) {
                    if (onContinueLoading == null) {
                        isLoading = false
                    } else {
                        onContinueLoading.invoke()
                    }
                }

                override fun onAdLoaded(ad: RewardedAd) {
                    super.onAdLoaded(ad)
                    lasTimeLoaded = System.currentTimeMillis()
                    rewardedAd = ad
                    isLoading = false
                }
            })
    }

    fun show(activity: Activity, rewardRequest: Any?) {
        this.rewardRequest = rewardRequest
        val ad = rewardedAd
        if (ad == null) {
            onAdFailedToShowFullScreenContent?.onAdFailedToShowFullScreenContent(rewardRequest)
            load()
        } else {
            setAdListener(ad)
            ad.show(activity,
                OnUserEarnedRewardListener {
                    onUserEarnedRewardListener?.onUserEarnedRewardListener(rewardRequest)
                })
        }
    }

    private fun setAdListener(ad: RewardedAd) {
        sharedPref.isAdShowFullScreen = true
        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                sharedPref.isAdShowFullScreen = false
                super.onAdDismissedFullScreenContent()
                onAdDismissedFullScreenContent?.onAdDismissedFullScreenContent(rewardRequest)
            }

            override fun onAdShowedFullScreenContent() {
                super.onAdShowedFullScreenContent()
                rewardedAd = null
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
        fun onAdDismissedFullScreenContent(rewardRequest: Any?)
    }

    interface IOnAdShowedFullScreenContent {
        fun onAdShowedFullScreenContent(rewardRequest: Any?)
    }

    interface IOnAdFailedToShowFullScreenContent {
        fun onAdFailedToShowFullScreenContent(rewardRequest: Any?)
    }

}