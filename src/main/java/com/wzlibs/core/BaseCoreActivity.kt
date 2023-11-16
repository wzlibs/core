package com.wzlibs.core

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import org.greenrobot.eventbus.EventBus

abstract class BaseCoreActivity<T : ViewBinding> : AppCompatActivity() {

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

    open fun onActivityBackPressed(){
        if (supportFragmentManager.backStackEntryCount == 0) {
            finish()
        } else {
            supportFragmentManager.popBackStack()
        }
    }

}