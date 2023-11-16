package com.wzlibs.core

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
import org.greenrobot.eventbus.EventBus

abstract class BaseDialogFragment<T : ViewBinding> : DialogFragment() {

    open val binding by lazy { bindingView() }

    open val registerEventBus = false

    open val fullScreen = false

    open val transparentBackground: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (registerEventBus) EventBus.getDefault().register(this)
        initConfig(savedInstanceState)
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

    open fun initConfig(savedInstanceState: Bundle?) {}

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

}