package com.zjl.finalarchitecture.data.model.user

import android.os.Parcelable
import com.zjl.finalarchitecture.data.model.coin.CoinVO
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

/**
 * 详细的用户信息，包含积分数据
 **/
@Serializable
@Parcelize
data class CombineUserInfoVO(
    val coinInfo: CoinVO,
    val userInfo: UserInfoVO,
): Parcelable