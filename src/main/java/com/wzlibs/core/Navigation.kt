package com.wzlibs.core

import androidx.annotation.IdRes
import androidx.annotation.IntDef
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class Navigation(
    private val fragmentManager: FragmentManager,
    private val fragment: Fragment,
    @IdRes private val containerViewId: Int
) {

    @NavigationType
    private var navigationType: Int = ADD_FRAGMENT
    private var tag: String? = null
    private var backStackName: String? = null
    private var shouldAddToBackStack: Boolean = false

    fun navigationType(@NavigationType navigationType: Int) = apply {
        this.navigationType = navigationType
    }

    fun setFragmentTag(tag: String?) = apply { this.tag = tag }

    fun addToBackStack(name: String?) = apply {
        shouldAddToBackStack = true
        backStackName = name
    }

    fun transaction() {
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
}

@IntDef(
    ADD_FRAGMENT,
    REPLACE_FRAGMENT
)
@Retention(AnnotationRetention.SOURCE)
annotation class NavigationType

const val ADD_FRAGMENT = 0

const val REPLACE_FRAGMENT = 1