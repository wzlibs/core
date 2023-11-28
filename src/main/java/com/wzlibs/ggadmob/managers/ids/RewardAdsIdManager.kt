package com.wzlibs.ggadmob.managers.ids

import android.content.Context
import com.wzlibs.base.R

class RewardAdsIdManager(private val context: Context) : MediationAdsIdManager(context) {
    override val debugId: String
        get() = context.getString(R.string.id_reward_test)
}