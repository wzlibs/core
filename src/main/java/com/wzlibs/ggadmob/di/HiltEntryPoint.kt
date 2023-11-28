package com.wzlibs.ggadmob.di

import com.wzlibs.ggadmob.ad_configs.AdmobConfigShared
import com.wzlibs.ggadmob.managers.ids.BannerAdsIdManager
import com.wzlibs.ggadmob.managers.ids.InterstitialAdsIdManager
import com.wzlibs.ggadmob.managers.ids.NativeAdsIdManager
import com.wzlibs.ggadmob.managers.ids.OpenAdsIdManager
import com.wzlibs.ggadmob.managers.ids.RewardAdsIdManager
import com.wzlibs.ggadmob.managers.ids.RewardInterstitialAdsIdManager
import com.wzlibs.ggadmob.native_ad.NativeManager
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface HiltEntryPoint {
    fun bannerAdsIdManager(): BannerAdsIdManager
    fun openAdsIdManager(): OpenAdsIdManager
    fun interstitialAdsIdManager(): InterstitialAdsIdManager
    fun rewardInterstitialAdsIdManager(): RewardInterstitialAdsIdManager
    fun rewardAdsIdManager(): RewardAdsIdManager
    fun nativeAdsIdManager(): NativeAdsIdManager
    fun admobConfigShared(): AdmobConfigShared
    fun nativeManager(): NativeManager
}