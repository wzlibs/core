package com.wzlibs.ggadmob.managers.ids

import android.content.Context
import com.wzlibs.core.isDebug

abstract class MediationAdsIdManager(private val context: Context) : AdsIdManager(context) {

    private lateinit var highFloorId: String

    private lateinit var mediumFloorId: String

    private lateinit var allPricesId: String

    fun getHighFloorId(): String {
        return if (context.isDebug()) {
            debugId
        } else {
            highFloorId
        }
    }

    fun getMediumFloorId(): String {
        return if (context.isDebug()) {
            debugId
        } else {
            mediumFloorId
        }
    }

    fun getAllPricesId(): String {
        return if (context.isDebug()) {
            debugId
        } else {
            allPricesId
        }
    }

    fun setHighFloorId(id: String) {
        highFloorId = id
    }

    fun setMediumFloorId(id: String) {
        mediumFloorId = id
    }

    fun setAllPricesId(id: String) {
        allPricesId = id
    }

}