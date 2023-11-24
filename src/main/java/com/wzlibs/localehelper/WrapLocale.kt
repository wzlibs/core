package com.wzlibs.localehelper

import java.util.Locale

class WrapLocale(val id: Int, val flag: Int, val locale: Locale) {
    fun getLocaleDisplayName(): String = locale.displayName
}