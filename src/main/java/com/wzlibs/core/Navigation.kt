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
    private val tag: String? = null,
    private val shouldAddToBackStack: Boolean = true,
    private val backStackName: String? = null
) {

    private fun transaction() {
        val transaction = fragmentManager.beginTransaction()
        if (navigationType == ADD_FRAGMENT) {
            transaction.add(containerViewId, fragment, tag)
        } else {
            transaction.replace(containerViewId, fragment, tag)
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
        private var shouldAddToBackStack: Boolean = true

        fun navigationType(@NavigationType navigationType: Int) =
            apply { this.navigationType = navigationType }

        fun tag(tag: String?) = apply { this.tag = tag }
        fun backStackName(backStackName: String?) = apply { this.backStackName = backStackName }
        fun shouldAddToBackStack(shouldAddToBackStack: Boolean) =
            apply { this.shouldAddToBackStack = shouldAddToBackStack }

        fun build() = Navigation(
            fragmentManager,
            fragment,
            containerViewId,
            navigationType,
            tag,
            shouldAddToBackStack,
            backStackName
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