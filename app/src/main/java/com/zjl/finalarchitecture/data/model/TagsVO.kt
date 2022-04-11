package com.zjl.finalarchitecture.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

/**
 * @description:
 * @author: zhou
 * @date : 2022/2/7 19:04
 */
@Serializable
@Parcelize
data class TagsVO(var name:String, var url:String): Parcelable