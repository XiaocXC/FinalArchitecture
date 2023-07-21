package com.zjl.finalarchitecture.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

/**
 * @author: 周健力
 * @e-mail: zhoujl@eetrust.com
 * @time: 2023/7/21
 * @version: 1.0
 * @description: 积分排行接口返回数据
 */
@Serializable
@Parcelize
data class RankVO(
    val coinCount: Int = 0,
    val level: Int = 0,
    val nickname: String = "",
    val rank: String = "",
    val userId: Int = 0,
    val username: String = ""
) : Parcelable
