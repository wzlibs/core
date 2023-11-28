package com.wzlibs.core

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.viewbinding.ViewBinding
import com.wzlibs.localehelper.LocaleAwareCompatActivity
import org.greenrobot.eventbus.EventBus


abstract class CoreActivity<T : ViewBinding> : LocaleAwareCompatActivity() {

    open val binding by lazy { bindingView() }

    open val registerEventBus = false

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
        startActivity(intent)
    }

    open fun navigateTo(navigation: Navigation) {
        navigation.transaction()
    }

}