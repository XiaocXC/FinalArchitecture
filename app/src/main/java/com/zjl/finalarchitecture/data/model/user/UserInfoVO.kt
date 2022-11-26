package com.zjl.finalarchitecture.data.model.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

/**
 * 账户信息
 */
@Parcelize
@Serializable
data class UserInfoVO(
    var admin: Boolean = false,
    var chapterTops: List<String> = listOf(),
    var collectIds: MutableList<String> = mutableListOf(),
    var email: String = "",
    var icon: String = "",
    var id: String = "",
    var nickname: String = "",
    var password: String = "",
    var coinCount: Int = 0,
    var token: String = "",
    var type: Int = 0,
    var username: String = ""
) : Parcelable
