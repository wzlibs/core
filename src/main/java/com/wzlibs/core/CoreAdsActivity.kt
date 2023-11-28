package com.wzlibs.core

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.wzlibs.ggadmob.ad_configs.AdmobConfigShared
import com.wzlibs.ggadmob.banner_ad.BannerManager
import com.wzlibs.ggadmob.ad_interstitial.InterstitialAdManager
import com.wzlibs.ggadmob.native_ad.NativeManager
import javax.inject.Inject

abstract class CoreAdsActivity<T : ViewBinding> : CoreActivity<T>() {

    open val bannerAd: ViewGroup? = null

    open val registerNativeLister = false

    @Inject
    lateinit var interstitialAdManager: InterstitialAdManager

    @Inject
    lateinit var admobConfigShared: AdmobConfigShared

    @Inject
    lateinit var nativeManager: NativeManager

    private val nativeListener = object : NativeManager.IOnNativeChanged {
        override fun onNativeChanged() {
            this@CoreAdsActivity.onNativeChanged()
        }
    }

    open fun onNativeChanged() {}

    override fun initConfig(savedInstanceState: Bundle?) {
        super.initConfig(savedInstanceState)
        loadAds()
        initBannerAds()
    }

    override fun onResume() {
        super.onResume()
        if (registerNativeLister) {
            nativeManager.addListener(nativeListener)
        }
    }

    override fun onPause() {
        if (registerNativeLister) {
            nativeManager.removeListener(nativeListener)
        }
        super.onPause()
    }

    private fun loadAds() {
        interstitialAdManager.load()
        nativeManager.load()
    }

    private fun initBannerAds() {
        bannerAd?.let { bannerAd ->
            if (admobConfigShared.isUnlockedAd) {
                bannerAd.visibility = View.GONE
            } else {
                BannerManager(this, bannerAd)
            }
        }
    }

    override fun navigateTo(intent: Intent) {
        if (admobConfigShared.isUnlockedAd) {
            super.navigateTo(intent)
        } else {
            interstitialAdManager.show(this) {
                super.navigateTo(intent)
            }
        }
    }

    override fun navigateTo(navigation: Navigation) {
        if (admobConfigShared.isUnlockedAd) {
            super.navigateTo(navigation)
        } else {
            interstitialAdManager.show(this) {
                super.navigateTo(navigation)
            }
        }
    }

}