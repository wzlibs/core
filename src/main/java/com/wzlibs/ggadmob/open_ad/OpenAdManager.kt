package com.wzlibs.ggadmob.open_ad

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.wzlibs.ggadmob.ad_configs.AdmobConfigShared
import com.wzlibs.ggadmob.di.HiltEntryPoint
import com.wzlibs.ggadmob.managers.GoogleMobileAdsConsentManager
import dagger.hilt.android.EntryPointAccessors
import java.util.Date

class OpenAdManager(private val application: Application) : Application.ActivityLifecycleCallbacks,
    LifecycleObserver {

    private var appOpenAd: AppOpenAd? = null
    private var isLoadingAd = false
    private var loadTime: Long = 0

    private val shared by lazy { AdmobConfigShared(application) }
    private var currentActivity: Activity? = null

    private val googleMobileAdsConsentManager: GoogleMobileAdsConsentManager by lazy {
        GoogleMobileAdsConsentManager.getInstance(application)
    }

    private val idsManager =
        EntryPointAccessors.fromApplication(application, HiltEntryPoint::class.java)
            .openAdsIdManager()

    init {
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        application.registerActivityLifecycleCallbacks(this)
        loadAd(application)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onMoveToForeground() {
        currentActivity?.let {
            showAdIfAvailable(it)
        }
    }

    fun loadAd(context: Context) {
        if (shared.isUnlockedAd) return
        if (isLoadingAd) return
        if (isAdAvailable()) return
        isLoadingAd = true
        val request = AdRequest.Builder().build()
        AppOpenAd.load(context, idsManager.getNormalId(), request,
            object : AppOpenAd.AppOpenAdLoadCallback() {
                override fun onAdLoaded(ad: AppOpenAd) {
                    appOpenAd = ad
                    isLoadingAd = false
                    loadTime = Date().time
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    isLoadingAd = false
                }
            }
        )
    }

    private fun isAdAvailable(): Boolean {
        return appOpenAd != null && wasLoadTimeLessThanNHoursAgo()
    }

    private fun wasLoadTimeLessThanNHoursAgo(numHours: Long = 4): Boolean {
        val dateDifference: Long = Date().time - loadTime
        val numMilliSecondsPerHour: Long = 3600000
        return dateDifference < numMilliSecondsPerHour * numHours
    }

    private fun showAdIfAvailable(activity: Activity) =
        showAdIfAvailable(activity, object : OnShowAdCompleteListener {
            override fun onShowAdComplete() {}
        })

    private fun showAdIfAvailable(
        activity: Activity,
        onShowAdCompleteListener: OnShowAdCompleteListener
    ) {
        if (shared.isUnlockedAd) return
        if (shared.isAdShowFullScreen) return
        if (!isAdAvailable()) {
            onShowAdCompleteListener.onShowAdComplete()
            if (googleMobileAdsConsentManager.canRequestAds) {
                loadAd(activity)
            }
            return
        }
        appOpenAd?.fullScreenContentCallback =
            object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    shared.isAdShowFullScreen = false
                    appOpenAd = null
                    onShowAdCompleteListener.onShowAdComplete()
                    if (googleMobileAdsConsentManager.canRequestAds) {
                        loadAd(activity)
                    }
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    shared.isAdShowFullScreen = false
                    appOpenAd = null
                    onShowAdCompleteListener.onShowAdComplete()
                    if (googleMobileAdsConsentManager.canRequestAds) {
                        loadAd(activity)
                    }
                }
            }
        shared.isAdShowFullScreen = true
        appOpenAd?.show(activity)
    }

    interface OnShowAdCompleteListener {
        fun onShowAdComplete()
    }

    override fun onActivityCreated(p0: Activity, p1: Bundle?) {}

    override fun onActivityStarted(activity: Activity) {
        if (!shared.isAdShowFullScreen) {
            currentActivity = activity
        }
    }

    override fun onActivityResumed(p0: Activity) {}

    override fun onActivityPaused(p0: Activity) {}

    override fun onActivityStopped(p0: Activity) {}

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {}

    override fun onActivityDestroyed(p0: Activity) {}

}