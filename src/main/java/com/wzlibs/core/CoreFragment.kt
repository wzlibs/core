package com.wzlibs.core

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.wzlibs.ggadmob.ad_configs.AdmobConfigShared
import com.wzlibs.ggadmob.ad_interstitial.InterstitialAdManager
import com.wzlibs.ggadmob.native_ad.NativeManager
import com.wzlibs.ggadmob.reward_ad.RewardAdManager
import org.greenrobot.eventbus.EventBus

abstract class CoreFragment<T : ViewBinding> : Fragment() {

    open val binding by lazy { bindingView() }

    open val registerEventBus = false

    open val registerNativeLister = false

    val admobConfigShared: AdmobConfigShared
        get() = (requireActivity() as CoreActivity<*>).admobConfigShared

    val nativeManager: NativeManager
        get() = (requireActivity() as CoreActivity<*>).nativeManager

    val interstitialAdManager: InterstitialAdManager
        get() = (requireActivity() as CoreActivity<*>).interstitialAdManager

    val rewardAdManager: RewardAdManager
        get() = (requireActivity() as CoreActivity<*>).rewardAdManager

    private val nativeListener = object : NativeManager.IOnNativeChanged {
        override fun onNativeChanged() {
            this@CoreFragment.onNativeChanged()
        }
    }

    open fun onNativeChanged() {}

    override fun onResume() {
        super.onResume()
        if (registerNativeLister) {
            (requireActivity() as CoreActivity<*>).addNativeListener(nativeListener)
        }
    }

    override fun onPause() {
        super.onPause()
        if (registerNativeLister) {
            (requireActivity() as CoreActivity<*>).removeNativeListener(nativeListener)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (registerEventBus) EventBus.getDefault().register(this)
        initConfig(view, savedInstanceState)
        initObserver()
        initListener()
        initTask()
    }

    override fun onDestroyView() {
        if (registerEventBus) EventBus.getDefault().unregister(this)
        release()
        super.onDestroyView()
    }

    open fun initObserver() {}

    open fun initConfig(view: View, savedInstanceState: Bundle?) {}

    open fun initListener() {}

    open fun initTask() {}

    open fun release() {}

    abstract fun bindingView(): T

    fun onFragmentBackPressed() {
        (requireActivity() as CoreActivity<*>).onActivityBackPressed()
    }

    open fun navigateTo(intent: Intent) = (requireActivity() as CoreActivity<*>).navigateTo(intent)
    open fun navigateTo(navigation: Navigation) =
        (requireActivity() as CoreActivity<*>).navigateTo(navigation)

}