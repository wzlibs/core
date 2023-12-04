package com.wzlibs.core

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.wzlibs.ggadmob.ad_configs.AdmobConfigShared
import com.wzlibs.ggadmob.ad_interstitial.InterstitialAdManager
import com.wzlibs.ggadmob.di.HiltEntryPoint
import com.wzlibs.ggadmob.native_ad.NativeManager
import com.wzlibs.ggadmob.reward_ad.RewardAdManager
import com.wzlibs.localehelper.currentLocale
import dagger.hilt.android.EntryPointAccessors
import org.greenrobot.eventbus.EventBus

abstract class CoreBottomSheetDialog<T : ViewBinding> : BottomSheetDialogFragment() {

    open val binding by lazy { bindingView() }

    open var registerEventBus = false

    open val transparentBackground: Boolean = false

    open val registerNativeLister = false

    private val nativeListener = object : NativeManager.IOnNativeChanged {
        override fun onNativeChanged() {
            this@CoreBottomSheetDialog.onNativeChanged()
        }
    }

    val admobConfigShared: AdmobConfigShared
        get() = (requireActivity() as CoreActivity<*>).admobConfigShared

    val nativeManager: NativeManager
        get() = (requireActivity() as CoreActivity<*>).nativeManager

    val interstitialAdManager: InterstitialAdManager
        get() = (requireActivity() as CoreActivity<*>).interstitialAdManager

    val rewardAdManager: RewardAdManager
        get() = (requireActivity() as CoreActivity<*>).rewardAdManager

    open fun onNativeChanged() {}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (registerEventBus) EventBus.getDefault().register(this)
        nativeManager.load()
        initObserver()
        initConfig()
        initListener()
        initTask()
    }

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

    override fun onStart() {
        super.onStart()
        if (transparentBackground) {
            dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        }
    }

    override fun onDestroyView() {
        if (registerEventBus) EventBus.getDefault().unregister(this)
        release()
        super.onDestroyView()
    }

    override fun show(manager: FragmentManager, tag: String?) {
        manager.findFragmentByTag(tag).let { fragment ->
            fragment ?: let {
                manager.beginTransaction().let { transition ->
                    this.show(transition, tag)
                }
            }
        }
    }

    open fun initObserver() {}

    open fun initConfig() {}

    open fun initListener() {}

    open fun initTask() {}

    open fun release() {}

    abstract fun bindingView(): T

    open fun navigateTo(intent: Intent) = (requireActivity() as CoreActivity<*>).navigateTo(intent)
}