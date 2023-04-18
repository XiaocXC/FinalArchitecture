package com.zjl.finalarchitecture.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

/**
 * 个人积分VO
 */
@Serializable
@Parcelize
data class CoinRecordVO(
    val coinCount: Int = 0,
    val date: Long = 0L,
    val desc: String = "",
    val id: Int = 0,
    val reason:String = "",
    val userId: Int = 0,
    val username: String = ""
) : Parcelable