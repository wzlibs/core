package com.wzlibs.ggadmob.native_ad

import android.content.Context
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.wzlibs.ggadmob.ad_configs.AdmobConfigShared
import com.wzlibs.ggadmob.di.HiltEntryPoint
import com.wzlibs.ggadmob.managers.GoogleMobileAdsConsentManager
import com.wzlibs.ggadmob.managers.LoadingGapManager
import dagger.hilt.android.EntryPointAccessors

class NativeManager(
    private val context: Context,
    private val sharedPref: AdmobConfigShared
) {
    private val loadingGapManager = LoadingGapManager(sharedPref)
    private val onNativeChangedList = ArrayList<IOnNativeChanged>()
    private val nativeAds = ArrayList<NativeAd>()
    private var adLoader: AdLoader? = null
    private val idsManager =
        EntryPointAccessors.fromApplication(context, HiltEntryPoint::class.java)
            .nativeAdsIdManager()

    fun addListener(l: IOnNativeChanged) = onNativeChangedList.add(l)
    fun removeListener(l: IOnNativeChanged) = onNativeChangedList.remove(l)
    private fun notifyToUpdate() = onNativeChangedList.forEach { it.onNativeChanged() }
    fun hasData(): Boolean = nativeAds.isNotEmpty()
    fun load() {
        if (sharedPref.isUnlockedAd) {
            return
        }
        if (!GoogleMobileAdsConsentManager.getInstance(context).canRequestAds) {
            return
        }
        if (adLoader?.isLoading == true) {
            return
        }
        if (nativeAds.size >= sharedPref.numberNativeNeedLoad) {
            return
        }
        if (!loadingGapManager.isOverGap()) return

        val adLoader =
            AdLoader.Builder(context, idsManager.getNormalId()).forNativeAd { ad: NativeAd ->
                nativeAds.add(ad)
                notifyToUpdate()
                loadingGapManager.resetGap()
            }.withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    loadingGapManager.updateGap()
                    notifyToUpdate()
                }
            }).build()
        this.adLoader = adLoader
        adLoader.loadAds(
            AdRequest.Builder().build(),
            sharedPref.numberNativeNeedLoad - nativeAds.size
        )
    }

    fun get(): NativeAd? {
        return try {
            val nativeAd = nativeAds[0]
            nativeAds.remove(nativeAd)
            nativeAd
        } catch (e: Exception) {
            null
        }
    }

    interface IOnNativeChanged {
        fun onNativeChanged()
    }

}