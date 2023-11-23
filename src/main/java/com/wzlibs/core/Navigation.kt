package com.wzlibs.core

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class Navigation private constructor(
    private val fragmentManager: FragmentManager,
    private val fragment: Fragment,
    @IdRes private val containerViewId: Int,
    private val isAddFragment: Boolean = true,
    private val fragmentTag: String? = null,
    private val backStackName: String? = null,
    private var shouldAddToBackStack: Boolean = true
) {

    private fun transaction() {
        val transaction = fragmentManager.beginTransaction()
        if (isAddFragment) {
            transaction.add(containerViewId, fragment, fragmentTag)
        } else {
            transaction.replace(containerViewId, fragment, fragmentTag)
        }
        if (shouldAddToBackStack) {
            transaction.addToBackStack(backStackName)
        }
        transaction.commit()
    }

    class Builder(
        private val fragmentManager: FragmentManager,
        private val fragment: Fragment,
        @IdRes private val containerViewId: Int
    ) {
        private var isAddFragment: Boolean = true
        private var tag: String? = null
        private var backStackName: String? = null
        private var shouldAddToBackStack: Boolean = false
        fun isAddFragment(isAddFragment: Boolean) = apply { this.isAddFragment = isAddFragment }
        fun setFragmentTag(tag: String?) = apply { this.tag = tag }
        fun addToBackStack(name: String?) = apply {
            shouldAddToBackStack = true
            backStackName = name
        }

        fun build() = Navigation(
            fragmentManager,
            fragment,
            containerViewId,
            isAddFragment,
            tag,
            backStackName,
            shouldAddToBackStack
        ).apply { transaction() }
    }
}