package com.zjl.finalarchitecture.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

/**
 * 体系VO
 */
@Serializable
@Parcelize
data class SystemVO(
    val id: String,
    val name: String,
    val order: Int,
    val parentChapterId: Int,
    val userControlSetTop: Boolean,
    val visible: Int,
    val children: List<ClassifyVO>
): Parcelable