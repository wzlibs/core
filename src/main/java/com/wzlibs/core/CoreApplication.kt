package com.wzlibs.core

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.google.android.gms.ads.MobileAds
import com.wzlibs.ggadmob.ad_configs.AdmobConfig
import com.wzlibs.localehelper.LocaleAwareApplication

abstract class CoreApplication : LocaleAwareApplication(), Application.ActivityLifecycleCallbacks {

    abstract fun isDebug(): Boolean

    override fun onCreate() {
        super.onCreate()
        AdmobConfig.isBuildDebug = isDebug()
        registerActivityLifecycleCallbacks(this)
        MobileAds.initialize(this) {}
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

    }

    override fun onActivityStarted(activity: Activity) {

    }

    override fun onActivityResumed(activity: Activity) {

    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {

    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityDestroyed(activity: Activity) {

    }
}