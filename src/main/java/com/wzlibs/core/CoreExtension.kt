package com.wzlibs.core

import android.content.Context

fun Context.isDebug(): Boolean {
    return (this as CoreApplication).isDebug()
}