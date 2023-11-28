package com.wzlibs.core

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.google.android.gms.ads.MobileAds
import com.wzlibs.ggadmob.ad_configs.AdmobConfig
import com.wzlibs.ggadmob.di.HiltEntryPoint
import com.wzlibs.localehelper.LocaleAwareApplication
import dagger.hilt.android.EntryPointAccessors

abstract class CoreApplication : LocaleAwareApplication(), Application.ActivityLifecycleCallbacks {

    open val idBanner: String? = null

    open val idNative: String? = null

    open val idOpen: String? = null

    open val idInter: String? = null

    open val idInterHigh: String? = null

    open val idInterMedium: String? = null

    open val idInterAll: String? = null

    open val idRewardInter: String? = null

    open val idRewardInterHigh: String? = null

    open val idRewardInterMedium: String? = null

    open val idRewardInterAll: String? = null

    open val idReward: String? = null

    open val idRewardHigh: String? = null

    open val idRewardMedium: String? = null

    open val idRewardAll: String? = null

    abstract fun isDebug(): Boolean

    private val hiltEntryPoint by lazy {
        EntryPointAccessors.fromApplication(this, HiltEntryPoint::class.java)
    }

    override fun onCreate() {
        super.onCreate()
        hiltEntryPoint.admobConfigShared().isAdShowFullScreen = false
        registerActivityLifecycleCallbacks(this)
        MobileAds.initialize(this) {}
        initAdIds()
    }

    private fun initAdIds() {
        idBanner?.let { hiltEntryPoint.bannerAdsIdManager().setNormalId(it) }

        idNative?.let { hiltEntryPoint.nativeAdsIdManager().setNormalId(it) }

        idOpen?.let { hiltEntryPoint.openAdsIdManager().setNormalId(it) }

        idInter?.let { hiltEntryPoint.interstitialAdsIdManager().setNormalId(it) }
        idInterHigh?.let { hiltEntryPoint.interstitialAdsIdManager().setHighFloorId(it) }
        idInterMedium?.let { hiltEntryPoint.interstitialAdsIdManager().setMediumFloorId(it) }
        idInterAll?.let { hiltEntryPoint.interstitialAdsIdManager().setAllPricesId(it) }

        idRewardInter?.let { hiltEntryPoint.rewardInterstitialAdsIdManager().setNormalId(it) }
        idRewardInterHigh?.let {
            hiltEntryPoint.rewardInterstitialAdsIdManager().setHighFloorId(it)
        }
        idRewardInterMedium?.let {
            hiltEntryPoint.rewardInterstitialAdsIdManager().setMediumFloorId(it)
        }
        idRewardInterAll?.let { hiltEntryPoint.rewardInterstitialAdsIdManager().setAllPricesId(it) }

        idReward?.let { hiltEntryPoint.rewardAdsIdManager().setNormalId(it) }
        idRewardHigh?.let { hiltEntryPoint.rewardAdsIdManager().setHighFloorId(it) }
        idRewardMedium?.let { hiltEntryPoint.rewardAdsIdManager().setMediumFloorId(it) }
        idRewardAll?.let { hiltEntryPoint.rewardAdsIdManager().setAllPricesId(it) }

    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

    }

    override fun onActivityStarted(activity: Activity) {

    }

    override fun onActivityResumed(activity: Activity) {

    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {

    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityDestroyed(activity: Activity) {

    }
}