package com.wzlibs.core

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.viewbinding.ViewBinding
import com.wzlibs.ggadmob.ad_configs.AdmobConfigShared
import com.wzlibs.ggadmob.ad_interstitial.InterstitialAdManager
import com.wzlibs.ggadmob.native_ad.NativeManager
import com.wzlibs.ggadmob.reward_ad.RewardAdManager
import org.greenrobot.eventbus.EventBus

abstract class CoreDialogFragment<T : ViewBinding> : DialogFragment() {

    open val binding by lazy { bindingView() }

    open val registerEventBus = false

    open val fullScreen = false

    open val transparentBackground: Boolean = true

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
            this@CoreDialogFragment.onNativeChanged()
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
    ): View? {
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

    override fun onStart() {
        super.onStart()
        if (transparentBackground) {
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        if (fullScreen) {
            dialog?.window?.setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT
            )
        } else {
            dialog?.window?.setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }
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

    override fun show(manager: FragmentManager, tag: String?) {
        manager.findFragmentByTag(tag).let { fragment ->
            fragment ?: let {
                super.show(manager, tag)
            }
        }
    }

    open fun navigateTo(intent: Intent) = (requireActivity() as CoreActivity<*>).navigateTo(intent)

}