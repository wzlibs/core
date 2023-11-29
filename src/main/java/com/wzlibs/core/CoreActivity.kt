package com.wzlibs.core

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.viewbinding.ViewBinding
import com.wzlibs.ggadmob.ad_configs.AdmobConfigShared
import com.wzlibs.ggadmob.ad_interstitial.InterstitialAdManager
import com.wzlibs.ggadmob.banner_ad.BannerManager
import com.wzlibs.ggadmob.di.HiltEntryPoint
import com.wzlibs.ggadmob.native_ad.NativeManager
import com.wzlibs.ggadmob.reward_ad.RewardAdManager
import com.wzlibs.localehelper.LocaleAwareCompatActivity
import dagger.hilt.android.EntryPointAccessors
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject


abstract class CoreActivity<T : ViewBinding> : LocaleAwareCompatActivity() {

    open val binding by lazy { bindingView() }

    open val registerEventBus = false

    open val bannerAd: ViewGroup? = null

    open val registerNativeLister = false

    open val fullScreen = false

    val admobConfigShared: AdmobConfigShared by lazy {
        EntryPointAccessors.fromApplication(this, HiltEntryPoint::class.java).admobConfigShared()
    }

    val nativeManager: NativeManager by lazy {
        EntryPointAccessors.fromApplication(this, HiltEntryPoint::class.java).nativeManager()
    }

    val interstitialAdManager by lazy {
        EntryPointAccessors.fromApplication(this, HiltEntryPoint::class.java)
            .interstitialAdManager()
    }

    val rewardAdManager: RewardAdManager by lazy {
        EntryPointAccessors.fromApplication(this, HiltEntryPoint::class.java).rewardManager()
    }

    private val nativeListener = object : NativeManager.IOnNativeChanged {
        override fun onNativeChanged() {
            this@CoreActivity.onNativeChanged()
        }
    }

    open fun onNativeChanged() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (fullScreen) {
            window.fullScreen()
        }
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
        loadAds()
        initBannerAds()
    }

    fun addNativeListener(l: NativeManager.IOnNativeChanged) {
        nativeManager.addListener(l)
    }

    fun removeNativeListener(l: NativeManager.IOnNativeChanged) {
        nativeManager.removeListener(l)
    }

    override fun onResume() {
        super.onResume()
        if (registerNativeLister) {
            addNativeListener(nativeListener)
        }
    }

    override fun onPause() {
        if (registerNativeLister) {
            removeNativeListener(nativeListener)
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

    open fun navigateTo(intent: Intent) {
        if (admobConfigShared.isUnlockedAd) {
            startActivity(intent)
        } else {
            interstitialAdManager.show(this) {
                startActivity(intent)
            }
        }
    }

    open fun navigateTo(navigation: Navigation) {
        if (admobConfigShared.isUnlockedAd) {
            navigation.transaction()
        } else {
            interstitialAdManager.show(this) {
                navigation.transaction()
            }
        }
    }

}