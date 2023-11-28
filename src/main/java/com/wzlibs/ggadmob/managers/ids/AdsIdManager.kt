package com.wzlibs.ggadmob.managers.ids

import android.content.Context
import com.wzlibs.core.isDebug

abstract class AdsIdManager(private val context: Context) {

    protected abstract val debugId: String

    protected lateinit var id: String

    fun getNormalId(): String {
        return if (context.isDebug()) {
            debugId
        } else {
            id
        }
    }

    fun setNormalId(id: String) {
        this.id = id
    }

}