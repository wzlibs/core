package com.wzlibs.core

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.viewbinding.ViewBinding
import com.wzlibs.ggadmob.ad_configs.AdmobConfig
import com.wzlibs.ggadmob.ad_configs.AdmobConfigShared
import com.wzlibs.ggadmob.banner_ad.BannerManager
import com.wzlibs.ggadmob.interstitial_ad.InterstitialAdManager
import com.wzlibs.ggadmob.native_ad.NativeManager
import com.wzlibs.localehelper.LocaleAwareCompatActivity
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject


abstract class CoreActivity<T : ViewBinding> : LocaleAwareCompatActivity() {

    open val bannerAd: ViewGroup? = null

    open val binding by lazy { bindingView() }

    open val registerEventBus = false

    @Inject
    lateinit var interstitialAdManager: InterstitialAdManager

    @Inject
    lateinit var admobConfigShared: AdmobConfigShared

    @Inject
    lateinit var nativeManager: NativeManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (registerEventBus) EventBus.getDefault().register(this)
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onActivityBackPressed()
            }
        })
        setContentView(binding.root)
        initConfig(savedInstanceState)
        initObserver()
        initListener()
        initTask()
        initAds()
    }

    private fun initAds() {
        bannerAd?.let { bannerAd ->
            if (admobConfigShared.isUnlockedAd) {
                bannerAd.visibility = View.GONE
            } else {
                BannerManager(this, AdmobConfig.getBannerId(), bannerAd)
            }
        }
    }

    override fun onDestroy() {
        if (registerEventBus) EventBus.getDefault().unregister(this)
        release()
        super.onDestroy()
    }

    open fun initConfig(savedInstanceState: Bundle?) {}

    open fun initObserver() {}

    open fun initListener() {}

    open fun initTask() {}

    open fun release() {}

    abstract fun bindingView(): T

    open fun onActivityBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 0) {
            finish()
        } else {
            supportFragmentManager.popBackStack()
        }
    }

    fun navigateTo(intent: Intent) {
        if (admobConfigShared.isUnlockedAd) {
            startActivity(intent)
        } else {
            interstitialAdManager.show(this) {
                startActivity(intent)
            }
        }
    }

    fun navigateTo(navigation: Navigation) {
        if (admobConfigShared.isUnlockedAd) {
            navigation.transaction()
        } else {
            interstitialAdManager.show(this) {
                navigation.transaction()
            }
        }
    }

}