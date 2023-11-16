package com.wzlibs.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import org.greenrobot.eventbus.EventBus

abstract class BaseCoreFragment<T : ViewBinding> : Fragment() {

    open val binding by lazy { bindingView() }

    open var registerEventBus = false

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
        if (registerEventBus) EventBus.getDefault().register(this)
        release()
        super.onDestroyView()
    }

    open fun initObserver() {}

    open fun initConfig(view: View, savedInstanceState: Bundle?) {}

    open fun initListener() {}

    open fun initTask() {}

    open fun release() {}

    abstract fun bindingView(): T

    fun onFragmentBackPressed(){
        (requireActivity() as BaseCoreActivity<*>).onActivityBackPressed()
    }

}