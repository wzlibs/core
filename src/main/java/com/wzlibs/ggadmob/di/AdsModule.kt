package com.wzlibs.ggadmob.di

import android.content.Context
import com.wzlibs.core.CoreApplication
import com.wzlibs.ggadmob.ad_configs.AdmobConfigShared
import com.wzlibs.ggadmob.ad_interstitial.InterstitialAdManager
import com.wzlibs.ggadmob.ad_interstitial.RewardInterstitialAdManager
import com.wzlibs.ggadmob.managers.LoadingGapManager
import com.wzlibs.ggadmob.managers.ids.BannerAdsIdManager
import com.wzlibs.ggadmob.managers.ids.InterstitialAdsIdManager
import com.wzlibs.ggadmob.managers.ids.NativeAdsIdManager
import com.wzlibs.ggadmob.managers.ids.OpenAdsIdManager
import com.wzlibs.ggadmob.managers.ids.RewardAdsIdManager
import com.wzlibs.ggadmob.managers.ids.RewardInterstitialAdsIdManager
import com.wzlibs.ggadmob.native_ad.NativeManager
import com.wzlibs.ggadmob.open_ad.OpenAdManager
import com.wzlibs.ggadmob.reward_ad.RewardAdManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AdsModule {

    @Provides
    @Singleton
    fun providerBannerAdsIdManager(@ApplicationContext context: Context): BannerAdsIdManager {
        return BannerAdsIdManager(context)
    }

    @Provides
    @Singleton
    fun providerOpenAdsIdManager(@ApplicationContext context: Context): OpenAdsIdManager {
        return OpenAdsIdManager(context)
    }

    @Provides
    @Singleton
    fun providerInterstitialAdsIdManager(@ApplicationContext context: Context): InterstitialAdsIdManager {
        return InterstitialAdsIdManager(context)
    }

    @Provides
    @Singleton
    fun providerRewardInterstitialAdsIdManager(@ApplicationContext context: Context): RewardInterstitialAdsIdManager {
        return RewardInterstitialAdsIdManager(context)
    }

    @Provides
    @Singleton
    fun providerRewardAdsIdManager(@ApplicationContext context: Context): RewardAdsIdManager {
        return RewardAdsIdManager(context)
    }

    @Provides
    @Singleton
    fun providerNativeAdsIdManager(@ApplicationContext context: Context): NativeAdsIdManager {
        return NativeAdsIdManager(context)
    }

    @Provides
    @Singleton
    fun providerAdmobShared(@ApplicationContext context: Context): AdmobConfigShared {
        return AdmobConfigShared(context)
    }

    @Provides
    @Singleton
    fun providerNativeManager(
        @ApplicationContext context: Context,
        admobShared: AdmobConfigShared
    ): NativeManager {
        return NativeManager(context, admobShared)
    }

    @Provides
    @Singleton
    fun providerInterstitialAdsManager(
        @ApplicationContext context: Context,
        admobShared: AdmobConfigShared
    ): InterstitialAdManager {
        return InterstitialAdManager(
            context,
            admobShared,
            LoadingGapManager(admobShared)
        )
    }

    @Provides
    @Singleton
    fun providerRewardInterstitialAdManager(
        @ApplicationContext context: Context,
        admobShared: AdmobConfigShared
    ): RewardInterstitialAdManager {
        return RewardInterstitialAdManager(context, admobShared)
    }

    @Provides
    @Singleton
    fun providerRewardAdManager(
        @ApplicationContext context: Context,
        admobShared: AdmobConfigShared
    ): RewardAdManager {
        return RewardAdManager(context, admobShared)
    }

    @Provides
    @Singleton
    fun providerOpenAdManager(@ApplicationContext context: Context): OpenAdManager {
        return OpenAdManager(context.applicationContext as CoreApplication)
    }

}
