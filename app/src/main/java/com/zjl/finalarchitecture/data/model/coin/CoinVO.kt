package com.zjl.finalarchitecture.data.model.coin

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

/**
 * 积分数据信息VO
 **/
@Serializable
@Parcelize
data class CoinVO(
    /**
     * 积分数
     */
    val coinCount: Int = 0,
    /**
     * 当前排名数
     */
    val rank: Int = 0,
    /**
     * 等级
     */
    val level: Int = 0
): Parcelable