package com.wzlibs.core

import androidx.annotation.IdRes
import androidx.annotation.IntDef
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class Navigation private constructor(
    private val fragmentManager: FragmentManager,
    private val fragment: Fragment,
    @IdRes private val containerViewId: Int,
    @NavigationType private val navigationType: Int = ADD_FRAGMENT,
    private val fragmentTag: String? = null,
    private val backStackName: String? = null,
    private var shouldAddToBackStack: Boolean = true
) {

    private fun transaction() {
        val transaction = fragmentManager.beginTransaction()
        if (navigationType == ADD_FRAGMENT) {
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
        @NavigationType
        private var navigationType: Int = ADD_FRAGMENT
        private var tag: String? = null
        private var backStackName: String? = null
        private var shouldAddToBackStack: Boolean = false

        fun navigationType(@NavigationType navigationType: Int) =
            apply { this.navigationType = navigationType }

        fun setFragmentTag(tag: String?) = apply { this.tag = tag }
        fun addToBackStack(name: String?) = apply {
            shouldAddToBackStack = true
            backStackName = name
        }

        fun shouldAddToBackStack(shouldAddToBackStack: Boolean) =
            apply { this.shouldAddToBackStack = shouldAddToBackStack }

        fun build() = Navigation(
            fragmentManager,
            fragment,
            containerViewId,
            navigationType,
            tag,
            backStackName,
            shouldAddToBackStack
        ).apply { transaction() }
    }
}

@IntDef(
    ADD_FRAGMENT,
    REPLACE_FRAGMENT
)
@Retention(AnnotationRetention.SOURCE)
annotation class NavigationType

const val ADD_FRAGMENT = 0

const val REPLACE_FRAGMENT = 1