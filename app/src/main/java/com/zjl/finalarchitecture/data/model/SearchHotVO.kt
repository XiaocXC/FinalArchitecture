package com.zjl.finalarchitecture.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

/**
 * @author Xiaoc
 * @since 2022-02-02
 *
 * 搜索热词VO
 */
@Serializable
@Parcelize
data class SearchHotVO(
    val id: Int,
    val link: String,
    val name: String,
    val order: Int,
    val visible: Int
): Parcelable